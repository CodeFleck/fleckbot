package br.com.codefleck.tradebot.controllers;

import static jdk.nashorn.internal.objects.NativeMath.round;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.ta4j.core.*;
import org.ta4j.core.analysis.criteria.NumberOfBarsCriterion;

import com.google.common.base.Stopwatch;

import br.com.codefleck.tradebot.core.engine.TradingEngine;
import br.com.codefleck.tradebot.core.util.CsvBarsLoader;
import br.com.codefleck.tradebot.core.util.CustomTimeSeriesManager;
import br.com.codefleck.tradebot.core.util.DownSamplingTimeSeries;
import br.com.codefleck.tradebot.exchanges.trading.api.impl.CustomBaseBarForGraph;
import br.com.codefleck.tradebot.services.impl.EventServiceImpl;
import br.com.codefleck.tradebot.strategies.MyStrategy;

@Controller
@RequestMapping("/playground")
@Transactional
public class PlaygroundController {

    @Autowired
    TradingEngine fleckBot;
    @Autowired
    private EventServiceImpl eventService;

    @GetMapping
    public ModelAndView playgroundLandingDataProvider(ModelAndView model) {

        model.addObject("botStatus", fleckBot.isRunning());
        model.setViewName("/playground");
        return model;
    }

    @PostMapping("/load")
    public ModelAndView loadData(@ModelAttribute("period") String period,
                                 @RequestParam("beginDate") String beginDate,
                                 @RequestParam("endDate") String endDate) throws ParseException {

        ModelAndView modelAndView = new ModelAndView("playground");

        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");


        Date begingDate = formato.parse(beginDate);
        Date endingDate = formato.parse(endDate);

        TimeSeries series = CsvBarsLoader.loadCoinBaseSeries(begingDate, endingDate);

        DownSamplingTimeSeries downSamplingTimeSeries = new DownSamplingTimeSeries(period);

        BaseTimeSeries customTimeSeries = downSamplingTimeSeries.aggregate(series);

        // Building the trading strategy
        MyStrategy myStrategy = new MyStrategy();
        Strategy strategy = myStrategy.buildStrategy(customTimeSeries);

        // Running the strategy
        CustomTimeSeriesManager seriesManager = new CustomTimeSeriesManager(customTimeSeries);

        Stopwatch timer = Stopwatch.createStarted();

//     Strategy strategy, OrderType orderType, Decimal amount, int startIndex, int finishIndex
        TradingRecord tradingRecord = seriesManager.run(strategy);

        List<Trade> tradesList = tradingRecord.getTrades();

        List<String> tradesListForGraph = eventService.getStockEvents(tradesList, customTimeSeries);

        Double grossProfit=0d;
        Double netProfit=0d;
        Double totalGrossProfit=0d;
        Double totalNetProfit=0d;
        Double fee=0d;
        Double totalFees=0d;

        for (Trade trade : tradesList) {
            grossProfit = trade.getExit().getPrice().doubleValue() - trade.getEntry().getPrice().doubleValue();
            totalGrossProfit += grossProfit;
            netProfit = grossProfit * 0.999;
            totalNetProfit += netProfit;
            fee = grossProfit - netProfit;
            totalFees += fee;
        }

        List<Bar> barListForGraph = new ArrayList<>();
        for(int k=0; k <= customTimeSeries.getEndIndex(); k++){
            barListForGraph.add(customTimeSeries.getBar(k));
        }


        BigDecimal totalGP = new BigDecimal(totalGrossProfit).setScale(2, RoundingMode.HALF_EVEN);
        BigDecimal totalNP = new BigDecimal(totalNetProfit).setScale(2, RoundingMode.HALF_EVEN);
        BigDecimal totalF = new BigDecimal(totalFees).setScale(2, RoundingMode.HALF_EVEN);

        List<CustomBaseBarForGraph> customBaseBarForGraphList = formatDateForFrontEnd(barListForGraph);

        modelAndView.addObject("listaDeBarras", customBaseBarForGraphList);
        modelAndView.addObject("series", customTimeSeries);
        modelAndView.addObject("tradeListForGraph", tradesListForGraph);
        modelAndView.addObject("tradeList", tradesList);
        modelAndView.addObject("totalGrossProfit",totalGP.doubleValue());
        modelAndView.addObject("totalNetProfit",totalNP.doubleValue());
        modelAndView.addObject("totalFees",totalF.doubleValue());

        System.out.println("Method took: " + timer.stop());
        modelAndView.addObject("stopTime", timer);

        int qtdOrdens = (int) tradingRecord.getTradeCount();
        modelAndView.addObject("numberOfTrades", qtdOrdens);

        // Number of bars
        int numberOfBars = (int) new NumberOfBarsCriterion().calculate(customTimeSeries, tradingRecord);
        modelAndView.addObject("numberOfBars", numberOfBars);

        // Getting the cash flow of the resulting trades
//        CashFlow cashFlow = new CashFlow(series, tradingRecord);
//        List<Decimal> moneyList = new ArrayList<>();
//        System.out.println("/n***   CASH FLOW   ***");
//        for (int i = 0; i <= cashFlow.getSize(); i++){
//            moneyList.add(cashFlow.getValue(i));
//            System.out.println("$$$: " + cashFlow.getValue(i));
//        }
//        modelAndView.addObject("moneyList", moneyList);

        // Total profit of our strategy vs total profit of a buy-and-hold strategy
        Decimal buyAndHoldResul = calculateBuyAndHold(customTimeSeries);
        modelAndView.addObject("buyAndHoldResult", buyAndHoldResul);

        return modelAndView;

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

    public Decimal calculateBuyAndHold(TimeSeries customTimeSeries){
            Decimal beginPrice = customTimeSeries.getFirstBar().getClosePrice();
            Decimal beginPriceMinusTax = beginPrice.multipliedBy(0.999);
            Decimal endPrice = customTimeSeries.getLastBar().getClosePrice();
            endPrice = endPrice.minus(beginPriceMinusTax).multipliedBy(0.999);

            return endPrice;
        }
}