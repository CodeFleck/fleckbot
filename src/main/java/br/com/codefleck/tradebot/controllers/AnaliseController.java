package br.com.codefleck.tradebot.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import br.com.codefleck.tradebot.core.engine.TradingEngine;
import br.com.codefleck.tradebot.daos.StockDataDao;
import br.com.codefleck.tradebot.models.StockData;
import br.com.codefleck.tradebot.services.impl.ForecastServiceImpl;
import br.com.codefleck.tradebot.services.impl.PredictionServiceImpl;
import br.com.codefleck.tradebot.tradingInterfaces.ExchangeNetworkException;
import br.com.codefleck.tradebot.tradingInterfaces.Ticker;
import br.com.codefleck.tradebot.tradingInterfaces.TradingApiException;

@Controller
@RequestMapping("/analise")
@Transactional
public class AnaliseController {

    @Autowired
    TradingEngine fleckBot;

    @Autowired
    ForecastServiceImpl forecastService;

    @Autowired
    StockDataDao stockDataDao;

    @Autowired
    PredictionServiceImpl predictionService;

    @GetMapping
    public ModelAndView noticiasLandingDataProvider(ModelAndView model) throws ExchangeNetworkException, TradingApiException, IOException {

        model.addObject("botStatus", fleckBot.isRunning());
        model.setViewName("analise");

        fleckBot.initConfig();

        final Ticker ticker = fleckBot.getExchangeAdapter().getTicker("btcusd");
        StockData stockData = predictionService.transformTickerInStockData(ticker);
        stockDataDao.save(stockData);

        List<StockData> stockDataList = stockDataDao.ListLatest(10000);

        List<String> predictionList = forecastService.initializeForecasts(stockDataList);

        System.out.println("Final results coming in ....");

        //predictionList.stream().forEach(s -> System.out.println(s));

        HashMap<String, Double> predictions = new HashMap<>();

        predictions.put("oneMinute", 1.0);
        predictions.put("fifteenMinutes", 15.0);
        predictions.put("thirtyMinutes", 30.0);
        predictions.put("oneHour", 1.0);
        predictions.put("twoHours", 2.0);
        predictions.put("fourHours", 4.0);
        predictions.put("twentyFourHours", 24.0);

        model.addObject("oneMinute", predictions.get("oneMinute"));
        model.addObject("fifteenMinutes", predictions.get("fifteenMinutes"));
        model.addObject("thirtyMinutes", predictions.get("thirtyMinutes"));
        model.addObject("oneHour", predictions.get("oneHour"));
        model.addObject("twoHours", predictions.get("twoHours"));
        model.addObject("fourHours", predictions.get("fourHours"));
        model.addObject("twentyFourHours", predictions.get("twentyFourHours"));

        return model;
    }


}
