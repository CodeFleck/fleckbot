package br.com.codefleck.tradebot.controllers;

import br.com.codefleck.tradebot.core.engine.TradingEngine;
import br.com.codefleck.tradebot.core.util.CsvBarsLoader;
import br.com.codefleck.tradebot.daos.StockDataDao;
import br.com.codefleck.tradebot.models.PriceCategory;
import br.com.codefleck.tradebot.models.StockData;
import br.com.codefleck.tradebot.services.impl.ForecastServiceImpl;
import br.com.codefleck.tradebot.services.impl.PredictionServiceImpl;
import br.com.codefleck.tradebot.tradingInterfaces.ExchangeNetworkException;
import br.com.codefleck.tradebot.tradingInterfaces.Ticker;
import br.com.codefleck.tradebot.tradingInterfaces.TradingApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.ta4j.core.BaseTimeSeries;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    public ModelAndView analiseLandingPage(ModelAndView model) throws ExchangeNetworkException, TradingApiException, IOException {

        model.addObject("botStatus", fleckBot.isRunning());
        model.setViewName("analise");

        fleckBot.initConfig();

        final Ticker ticker = fleckBot.getExchangeAdapter().getTicker("BTCUSD");
        StockData stockData = predictionService.transformTickerInStockData(ticker);
        stockDataDao.save(stockData);
        List<StockData> stockDataListLast24h = stockDataDao.ListLatest(1440);

        HashMap<String, Double> predictions = forecastService.initializeForecasts(stockDataListLast24h);

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
