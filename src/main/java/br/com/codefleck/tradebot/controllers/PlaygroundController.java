package br.com.codefleck.tradebot.controllers;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.ta4j.core.*;
import org.ta4j.core.analysis.CashFlow;
import org.ta4j.core.analysis.criteria.*;

import br.com.codefleck.tradebot.core.engine.TradingEngine;
import br.com.codefleck.tradebot.core.util.CsvBarsLoader;
import br.com.codefleck.tradebot.core.util.CustomTimeSeries;
import br.com.codefleck.tradebot.strategies.MovingMomentumStrategy;

@Controller
@RequestMapping("/playground")
@Transactional
public class PlaygroundController {

    @Autowired
    TradingEngine fleckBot;

    @GetMapping
    public ModelAndView noticiasLandingDataProvider(ModelAndView model) {

        model.addObject("botStatus", fleckBot.isRunning());
        model.setViewName("/playground");

        return model;
    }

    @PostMapping("/load")
    public ModelAndView loadData(@Valid @ModelAttribute("dataSet")String algumaCoisa,
                                 @RequestParam("dataSizeName") String dataSize,
                                 @RequestParam("strategyName") String strategyNome) {

        ModelAndView model = new ModelAndView("playground");

        String strategyName = strategyNome;
        String dataSizeForTesting = dataSize;

        TimeSeries series = new CustomTimeSeries();

//        series = CsvBarsLoader.loadCoinBaseSeriesForMiniTesting();
          series = CsvBarsLoader.loadCoinBaseSeriesForTesting();
//        series = CsvBarsLoader.loadCoinBaseSeries();


        MovingMomentumStrategy movingMomentumStrategy = new MovingMomentumStrategy();

        // Building the trading strategy
        Strategy strategy = movingMomentumStrategy.buildStrategy(series);

        // Running the strategy
        TimeSeriesManager seriesManager = new TimeSeriesManager(series);

        long startTime = System.currentTimeMillis();
        System.out.println("Start time: " + startTime);

        TradingRecord tradingRecord = seriesManager.run(strategy);

        long endTime = System.currentTimeMillis();
        long totalTimeInSeconds = (endTime - startTime)/1000;
        System.out.println("That took " + totalTimeInSeconds + " sec");

        System.out.println("Number of trades for the strategy: " + tradingRecord.getTradeCount());

        // Analysis
        System.out.println("Total profit for the strategy: " + new TotalProfitCriterion().calculate(series, tradingRecord));

        // Number of bars
        System.out.println("Number of bars: " + new NumberOfBarsCriterion().calculate(series, tradingRecord));

        // Average profit (per bar)
        System.out.println("Average profit (per bar): " + new AverageProfitCriterion().calculate(series, tradingRecord));

        // Getting the cash flow of the resulting trades
        CashFlow cashFlow = new CashFlow(series, tradingRecord);

        // Getting the profitable trades ratio
        AnalysisCriterion profitTradesRatio = new AverageProfitableTradesCriterion();
        System.out.println("Profitable trades ratio: " + profitTradesRatio.calculate(series, tradingRecord));
        // Getting the reward-risk ratio
        AnalysisCriterion rewardRiskRatio = new RewardRiskRatioCriterion();
        System.out.println("Reward-risk ratio: " + rewardRiskRatio.calculate(series, tradingRecord));

        // Total profit of our strategy
        // vs total profit of a buy-and-hold strategy
        AnalysisCriterion vsBuyAndHold = new VersusBuyAndHoldCriterion(new TotalProfitCriterion());
        System.out.println("Our profit vs buy-and-hold profit: " + vsBuyAndHold.calculate(series, tradingRecord));

        /*
          Analysis criteria
         */

        // Number of trades
        System.out.println("Number of trades: " + new NumberOfTradesCriterion().calculate(series, tradingRecord));
        // Profitable trades ratio
        System.out.println("Profitable trades ratio: " + new AverageProfitableTradesCriterion().calculate(series, tradingRecord));
        // Maximum drawdown
        System.out.println("Maximum drawdown: " + new MaximumDrawdownCriterion().calculate(series, tradingRecord));
        // Total transaction cost
        System.out.println("Total transaction cost (from $1000): " + new LinearTransactionCostCriterion(1000, 0.1).calculate(series, tradingRecord));
        // Buy-and-hold
        System.out.println("Buy-and-hold: " + new BuyAndHoldCriterion().calculate(series, tradingRecord));
        // Total profit vs buy-and-hold
        TotalProfitCriterion totalProfit = new TotalProfitCriterion();
        System.out.println("Total profit: " + totalProfit.calculate(series, tradingRecord));
        System.out.println("Custom strategy profit vs buy-and-hold strategy profit: " + new VersusBuyAndHoldCriterion(totalProfit).calculate(series, tradingRecord));

        return model;

    }
}