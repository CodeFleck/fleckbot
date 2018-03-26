package br.com.codefleck.tradebot.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.ta4j.core.*;
import org.ta4j.core.analysis.CashFlow;
import org.ta4j.core.analysis.criteria.*;

import com.google.common.base.Stopwatch;

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
    public ModelAndView loadData(@RequestParam("strategyName") String strategyNome,
                                 @RequestParam("beginDate") String beginDate,
                                 @RequestParam("endDate") String endDate) throws ParseException {

        ModelAndView model = new ModelAndView("playground");

        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");

        String strategyName = strategyNome;

        Date begingDate = formato.parse(beginDate);
        Date endingDate = formato.parse(endDate);

        TimeSeries series = new CustomTimeSeries();

        series = CsvBarsLoader.loadCoinBaseSeries(begingDate, endingDate);


        MovingMomentumStrategy movingMomentumStrategy = new MovingMomentumStrategy();

        // Building the trading strategy
        Strategy strategy = movingMomentumStrategy.buildStrategy(series);

        // Running the strategy
        TimeSeriesManager seriesManager = new TimeSeriesManager(series);


        Stopwatch timer = Stopwatch.createStarted();
        System.out.println("Start time: " + timer);
        TradingRecord tradingRecord = seriesManager.run(strategy);

        List<Trade> tradesList = tradingRecord.getTrades();

        for (Trade trade : tradesList) {
            System.out.print("Entry : " + trade.getEntry());
            System.out.print(" | Exit : " + trade.getExit());
            Double lucro = trade.getEntry().getPrice().doubleValue();
            lucro -= (trade.getExit().getPrice().doubleValue());
            System.out.println(" | Lucro: " + lucro*(-1));
        }
        
        
        System.out.println("Method took: " + timer.stop());
        System.out.println("Total number of trades for the strategy: " + tradingRecord.getTradeCount());

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

        return model;

    }
}