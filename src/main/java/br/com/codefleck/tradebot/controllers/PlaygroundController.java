package br.com.codefleck.tradebot.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.ta4j.core.*;
import org.ta4j.core.analysis.criteria.NumberOfBarsCriterion;

import com.google.common.base.Stopwatch;

import br.com.codefleck.tradebot.core.engine.TradingEngine;
import br.com.codefleck.tradebot.core.util.CsvBarsLoader;
import br.com.codefleck.tradebot.core.util.DownSamplingTimeSeries;
import br.com.codefleck.tradebot.exchanges.trading.api.impl.CustomBaseBarForGraph;
import br.com.codefleck.tradebot.strategies.MyStrategy;

@Controller
@RequestMapping("/playground")
@Transactional
public class PlaygroundController {

    private static final Logger LOG = LogManager.getLogger();

    @Autowired
    TradingEngine fleckBot;

    @GetMapping
    public ModelAndView playgroundLandingDataProvider(ModelAndView model) {

        model.addObject("botStatus", fleckBot.isRunning());
        model.setViewName("/playground");
        return model;
    }

    @PostMapping("/load")
    public ModelAndView loadData(@RequestParam("beginDate") String beginDate,
                                 @RequestParam("endDate") String endDate) throws ParseException {

        ModelAndView modelAndView = new ModelAndView("playground");

        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");


        Date begingDate = formato.parse(beginDate);
        Date endingDate = formato.parse(endDate);

        TimeSeries series = CsvBarsLoader.loadCoinBaseSeries(begingDate, endingDate);



        DownSamplingTimeSeries downSamplingTimeSeries = new DownSamplingTimeSeries();

        TimeSeries customTimeSeries = downSamplingTimeSeries.aggregateTimeSeriesToTenMinutes(series);

        // Building the trading strategy
        MyStrategy myStrategy = new MyStrategy();
        Strategy strategy = myStrategy.buildStrategy(customTimeSeries);

        // Running the strategy
        TimeSeriesManager seriesManager = new TimeSeriesManager(customTimeSeries);

        Stopwatch timer = Stopwatch.createStarted();

//     Strategy strategy, OrderType orderType, Decimal amount, int startIndex, int finishIndex
        TradingRecord tradingRecord = seriesManager.run(strategy, Order.OrderType.BUY, Decimal.valueOf(1), customTimeSeries.getBeginIndex(), customTimeSeries.getEndIndex());

        List<Trade> tradesList = tradingRecord.getTrades();

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

        List<CustomBaseBarForGraph> customBaseBarForGraphList = formatDateForFrontEnd(barListForGraph);

        modelAndView.addObject("listaDeBarras", customBaseBarForGraphList);

        modelAndView.addObject("series", customTimeSeries);
        modelAndView.addObject("tradeList", tradesList);
        modelAndView.addObject("totalGrossProfit",totalGrossProfit);
        modelAndView.addObject("totalNetProfit",totalNetProfit);
        modelAndView.addObject("totalFees",totalFees);

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