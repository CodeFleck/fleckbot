package br.com.codefleck.tradebot.controllers;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import org.ta4j.core.analysis.criteria.*;

import com.google.common.base.Stopwatch;

import br.com.codefleck.tradebot.core.engine.TradingEngine;
import br.com.codefleck.tradebot.core.util.CsvBarsLoader;
import br.com.codefleck.tradebot.core.util.DownSamplingTimeSeries;
import br.com.codefleck.tradebot.strategies.MyStrategy;

@Controller
@RequestMapping("/playground")
@Transactional
public class PlaygroundController {

    private static final Logger LOG = LogManager.getLogger();

    @Autowired
    TradingEngine fleckBot;

    @GetMapping
    public ModelAndView noticiasLandingDataProvider(ModelAndView model) {

        model.addObject("botStatus", fleckBot.isRunning());
        model.setViewName("/playground");

        return model;
    }

    @PostMapping("/load")
    public ModelAndView loadData(@RequestParam("strategyName") String strategyNome,
                                 @RequestParam("beginDate") String beginDate,
                                 @RequestParam("endDate") String endDate) throws ParseException {

        ModelAndView modelAndView = new ModelAndView("playground");

        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");

        String strategyName = strategyNome;

        Date begingDate = formato.parse(beginDate);
        Date endingDate = formato.parse(endDate);

        TimeSeries series = CsvBarsLoader.loadCoinBaseSeries(begingDate, endingDate);
        modelAndView.addObject("periodoEscolhido", series.getSeriesPeriodDescription());

//        DownSamplingTimeSeries downSamplingTimeSeries = new DownSamplingTimeSeries();

//        TimeSeries weeklyTimeSeries = downSamplingTimeSeries.aggregateTimeSeriesDtoW(series);

        MyStrategy myStrategy = new MyStrategy();

        // Building the trading strategy
        Strategy strategy = myStrategy.buildStrategy(series);

        // Running the strategy
        TimeSeriesManager seriesManager = new TimeSeriesManager(series);

        Stopwatch timer = Stopwatch.createStarted();
        System.out.println("Start Time: " + timer);
        modelAndView.addObject("startTime", timer);

//     Strategy strategy, OrderType orderType, Decimal amount, int startIndex, int finishIndex
        TradingRecord tradingRecord = seriesManager.run(strategy, Order.OrderType.BUY, Decimal.valueOf(10000), series.getBeginIndex(), series.getEndIndex());

        List<Trade> tradesList = tradingRecord.getTrades();
        Double lucroBruto=0d;
        Double totallucroBruto=0d;
        for (Trade trade : tradesList) {
            System.out.print("Entry : " + trade.getEntry());
            System.out.print(" | Exit : " + trade.getExit());

            lucroBruto = trade.getEntry().getPrice().doubleValue();
            lucroBruto -= (trade.getExit().getPrice().doubleValue());
            totallucroBruto += lucroBruto;
        }
        DecimalFormat numberFormat = new DecimalFormat("##.00");
        modelAndView.addObject("lucroBruto", numberFormat.format(totallucroBruto*(-1)));
        modelAndView.addObject("tradeList", tradesList);

        System.out.println("Method took: " + timer.stop());
        modelAndView.addObject("stopTime", timer);

        int qtdOrdens = (int) tradingRecord.getTradeCount();
        modelAndView.addObject("numberOfTrades", qtdOrdens);

        // Number of bars
        int numberOfBars = (int) new NumberOfBarsCriterion().calculate(series, tradingRecord);
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

        // Getting the profitable trades ratio
        AnalysisCriterion profitTradesRatio = new AverageProfitableTradesCriterion();
        modelAndView.addObject("profitableTradesRatio", profitTradesRatio.calculate(series, tradingRecord));

        // Total profit of our strategy vs total profit of a buy-and-hold strategy
        AnalysisCriterion vsBuyAndHold = new VersusBuyAndHoldCriterion(new TotalProfitCriterion());
        modelAndView.addObject("profitVsBuyAndHold", vsBuyAndHold.calculate(series, tradingRecord));

        // Maximum drawdown
        modelAndView.addObject("maximumDrawdown", new MaximumDrawdownCriterion().calculate(series, tradingRecord));

        // Total transaction cost
        double linearTransactionCostCriterion = new LinearTransactionCostCriterion(0, 0.1).calculate(series, tradingRecord);
        modelAndView.addObject("totalTransactionCosts", new LinearTransactionCostCriterion(0, 0.1).calculate(series, tradingRecord));

        return modelAndView;

    }
}