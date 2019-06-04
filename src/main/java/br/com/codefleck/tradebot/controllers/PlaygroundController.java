package br.com.codefleck.tradebot.controllers;

import br.com.codefleck.tradebot.core.engine.TradingEngine;
import br.com.codefleck.tradebot.core.util.*;
import br.com.codefleck.tradebot.daos.SMADao;
import br.com.codefleck.tradebot.exchanges.trading.CustomBaseBarForGraph;
import br.com.codefleck.tradebot.services.impl.EventServiceImpl;
import br.com.codefleck.tradebot.services.impl.TradeServiceImpl;
import br.com.codefleck.tradebot.strategies.DailyPredictionTradeStrategy;
import br.com.codefleck.tradebot.strategies.SMADuplo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.ta4j.core.*;
import org.ta4j.core.analysis.criteria.NumberOfBarsCriterion;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/playground")
@Transactional
public class PlaygroundController {

    @Autowired
    TradingEngine fleckBot;
    @Autowired
    private EventServiceImpl eventService;
    @Autowired
    private TradeServiceImpl tradeService;
    @Autowired
    private  SMADao smaDao;

    @GetMapping
    public ModelAndView playgroundLandingDataProvider(ModelAndView model) {

        model.addObject("botStatus", fleckBot.isRunning());
        model.setViewName("/playground");
        return model;
    }

    @PostMapping("/load")
    public ModelAndView loadData(@ModelAttribute("strategy") String strategy,
                                 @ModelAttribute("period") String period,
                                 @ModelAttribute("montante") Double montante,
                                 @ModelAttribute("saldo") Double saldo,
                                 @RequestParam("beginDate") String beginDate,
                                 @RequestParam("endDate") String endDate) throws ParseException {

        ModelAndView modelAndView = new ModelAndView("playground");

        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");

        Date begingDate = formato.parse(beginDate);
        Date endingDate = formato.parse(endDate);
        Double orderAmount = montante;
        Decimal balance = Decimal.valueOf(saldo);
        Decimal initialBalance = Decimal.valueOf(balance);
        Decimal profitUSD = Decimal.ZERO;
        Decimal totalProfitUSD = Decimal.ZERO;
        Decimal fee = Decimal.valueOf(0.999);
        Decimal totalFees = Decimal.ZERO;
        Decimal bitcoinBalance = Decimal.ZERO;


        TimeSeries series = CsvBarsLoader.loadCoinBaseSeries(begingDate, endingDate);

        DownSamplingTimeSeries downSamplingTimeSeries = new DownSamplingTimeSeries(period);

        BaseTimeSeries customTimeSeries = downSamplingTimeSeries.aggregate(series);

        List<SMA> smaList = smaDao.all();

        // Building the trading strategy
        Strategy chosenStrategy = resolveStrategy(strategy, customTimeSeries, smaList);

        // Running the strategy
        CustomTimeSeriesManager seriesManager = new CustomTimeSeriesManager(customTimeSeries);

        // Strategy strategy, OrderType orderType, Decimal amount
        TradingRecord tradingRecord = seriesManager.run(chosenStrategy, Order.OrderType.BUY, Decimal.valueOf(orderAmount));

        List<Trade> tradesList = tradingRecord.getTrades();

        // Create a tradeList for Graph
        List<String> tradesListForGraph = eventService.getStockEvents(tradesList, customTimeSeries);

        // Create a CustomTrade List that helds the profit amount for the trades.
        List<CustomTrade> customTradeList = new ArrayList<>();

        for (Trade trade : tradesList) {

            bitcoinBalance = tradeService.updateBitcoinBalance(trade, fee, bitcoinBalance);
            balance = tradeService.updateBalanceUSD(balance, trade);
            profitUSD = tradeService.calculateProfitUSD(trade, fee);
            totalProfitUSD = totalProfitUSD.plus(profitUSD);

            CustomTrade customTrade = new CustomTrade();
            customTrade.setEntry(trade.getEntry());
            customTrade.setExit(trade.getExit());
            customTrade.setTradeProfit(profitUSD);
            customTrade.setTradeProfitPercentage(tradeService.calculateProfitPercentageForTrade(profitUSD, balance));
            customTradeList.add(customTrade);

            totalFees = Decimal.valueOf(totalFees.plus(tradeService.calculateTotalSpentOnFees(trade, fee)));
        }

        List<Bar> barListForGraph = new ArrayList<>();
        for(int k=0; k <= customTimeSeries.getEndIndex(); k++){
            barListForGraph.add(customTimeSeries.getBar(k));
        }

        BigDecimal totalProfit = new BigDecimal(totalProfitUSD.doubleValue()).setScale(2, RoundingMode.HALF_EVEN);
        BigDecimal totalF = new BigDecimal(totalFees.doubleValue()).setScale(2, RoundingMode.HALF_EVEN);
        BigDecimal finalBalance = new BigDecimal(balance.doubleValue()).setScale(2, RoundingMode.HALF_EVEN);

        Decimal tradePercentage = tradeService.calculateProfitPercentage(totalProfit, initialBalance);
        BigDecimal finalTradePercentage = new BigDecimal(String.valueOf(tradePercentage)).setScale(2, RoundingMode.HALF_EVEN);

        List<CustomBaseBarForGraph> customBaseBarForGraphList = formatDateForFrontEnd(barListForGraph);
        modelAndView.addObject("chosenPeriod", period);
        modelAndView.addObject("bitcoinAmount", bitcoinBalance);
        modelAndView.addObject("finalBalance", finalBalance);
        modelAndView.addObject("listaDeBarras", customBaseBarForGraphList);
        modelAndView.addObject("series", customTimeSeries);
        modelAndView.addObject("tradeListForGraph", tradesListForGraph);
        modelAndView.addObject("tradeList", customTradeList);
        modelAndView.addObject("totalProfitUSD",totalProfit.doubleValue());
        modelAndView.addObject("totalFees",totalF.doubleValue());
        modelAndView.addObject("tradePercentage",finalTradePercentage.doubleValue());

        int qtdOrdens = (int) tradingRecord.getTradeCount();
        modelAndView.addObject("numberOfTrades", qtdOrdens);

        // Number of bars
        int numberOfBars = (int) new NumberOfBarsCriterion().calculate(customTimeSeries, tradingRecord);
        modelAndView.addObject("numberOfBars", numberOfBars);

        // Total profit of our strategy vs total profit of a buy-and-hold strategy
        Decimal buyAndHoldPercentage = tradeService.calculateBuyAndHoldPercentage(customTimeSeries);
        BigDecimal buyHoldPercentage = new BigDecimal(String.valueOf(buyAndHoldPercentage)).setScale(2, RoundingMode.HALF_EVEN);
        modelAndView.addObject("buyAndHoldPercentage", buyHoldPercentage);

        return modelAndView;

    }

    private Strategy resolveStrategy(String strategy, BaseTimeSeries customTimeSeries, List<SMA> smaList) {

        if (strategy.equals("SMA Duplo")){
            SMADuplo myStrategy = new SMADuplo();
            return myStrategy.buildStrategy(customTimeSeries, smaList);
        }
        if (strategy.equals("LSTM Prediction")){
            DailyPredictionTradeStrategy dayDailyPredictionTradeStrategy = new DailyPredictionTradeStrategy();
        return dayDailyPredictionTradeStrategy.buildStrategy(customTimeSeries, smaList);
        }

        return null;

    }

    private List<CustomBaseBarForGraph> formatDateForFrontEnd(List<Bar> barListForGraph) {

        List<CustomBaseBarForGraph> tempCustomBaseBarForGraphList = new ArrayList<>();

        for (Bar bar: barListForGraph) {

            Date d = Date.from(bar.getEndTime().toInstant());
            Long timeAsMilisecondsForGraph = d.getTime();

            CustomBaseBarForGraph baseBarForGraph = new CustomBaseBarForGraph(bar.getEndTime(), bar.getOpenPrice(),bar.getMaxPrice(),bar.getMinPrice(), bar.getClosePrice(), bar.getVolume());

            baseBarForGraph.setCustomEndTimeForGraph(timeAsMilisecondsForGraph);

            tempCustomBaseBarForGraphList.add(baseBarForGraph);
        }
        return tempCustomBaseBarForGraphList;
    }
}