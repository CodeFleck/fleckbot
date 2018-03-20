package br.com.codefleck.tradebot.controllers;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.ta4j.core.*;
import org.ta4j.core.analysis.CashFlow;
import org.ta4j.core.analysis.criteria.AverageProfitableTradesCriterion;
import org.ta4j.core.analysis.criteria.RewardRiskRatioCriterion;
import org.ta4j.core.analysis.criteria.TotalProfitCriterion;
import org.ta4j.core.analysis.criteria.VersusBuyAndHoldCriterion;

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

    @GetMapping("/load")
    public ModelAndView loadData(@RequestParam("dataSize") String dataSize,
                                 @RequestParam("strategyName") String strategyNome) {

        ModelAndView model = new ModelAndView("playground");

        String strategyName = strategyNome;
        String dataSizeForTesting = dataSize;

        TimeSeries series = new CustomTimeSeries();

        if (dataSizeForTesting.equals("coinbaseMini")){
             series = CsvBarsLoader.loadCoinBaseSeriesForMiniTesting();
        } else if (dataSizeForTesting.equals("coinbaseLong")){
             series = CsvBarsLoader.loadCoinBaseSeries();
        }

        if (strategyName.equals("movingMomentumStrategy")){

            MovingMomentumStrategy movingMomentumStrategy = new MovingMomentumStrategy();

            // Building the trading strategy
            Strategy strategy = movingMomentumStrategy.buildStrategy(series);

            // Running the strategy
            TimeSeriesManager seriesManager = new TimeSeriesManager(series);
            TradingRecord tradingRecord = seriesManager.run(strategy);
            System.out.println("Number of trades for the strategy: " + tradingRecord.getTradeCount());

            // Analysis
            System.out.println("Total profit for the strategy: " + new TotalProfitCriterion().calculate(series, tradingRecord));

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

        }

        return model;

    }
}