package br.com.codefleck.tradebot.controllers;

import br.com.codefleck.tradebot.core.engine.TradingEngine;
import br.com.codefleck.tradebot.daos.StockDataDao;
import br.com.codefleck.tradebot.models.DataPointsListModel;
import br.com.codefleck.tradebot.models.StockData;
import br.com.codefleck.tradebot.redesneurais.PriceCategory;
import br.com.codefleck.tradebot.services.impl.ForecastServiceImpl;
import br.com.codefleck.tradebot.services.impl.PredictionServiceImpl;
import br.com.codefleck.tradebot.tradingInterfaces.ExchangeNetworkException;
import br.com.codefleck.tradebot.tradingInterfaces.Ticker;
import br.com.codefleck.tradebot.tradingInterfaces.TradingApiException;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.transaction.Transactional;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
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
    public ModelAndView analiseLandingDataProvider(ModelAndView model) throws ExchangeNetworkException, TradingApiException, IOException {

        model.addObject("botStatus", fleckBot.isRunning());
        model.setViewName("analise");

        fleckBot.initConfig();

        //gather market current ticker
        final Ticker ticker = fleckBot.getExchangeAdapter().getTicker("BTCUSD");
        StockData stockData = predictionService.transformTickerInStockData(ticker);
        stockDataDao.save(stockData);

        List<StockData> stockDataList = stockDataDao.ListLatest(1000);

        List<String> results = forecastService.initializeForecasts(stockDataList, PriceCategory.CLOSE);

        DataPointsListModel dataPointsList = predictionService.prepareDataPointToBeSaved(results, null);
        Double errorPercentageAvg = predictionService.calculateErrorPercentageAverage(dataPointsList);
        Double errorPercentageLastDay = predictionService.calculateErrorPercentageLastDay(dataPointsList);
        Double majorError = predictionService.calculateMajorError(dataPointsList);
        Double minorError = predictionService.calculateMinorError(dataPointsList);

        NumberFormat formatter = new DecimalFormat("#0.00");

        model.addObject("errorPercentageAvg", formatter.format(errorPercentageAvg));
        model.addObject("errorPercentageLastDay", formatter.format(errorPercentageLastDay));
        model.addObject("majorError", formatter.format(majorError));
        model.addObject("minorError", formatter.format(minorError));

        System.out.println("Final results coming in ....");

        HashMap<String, Double> predictions = new HashMap<>();

        Lists.reverse(dataPointsList.getPredictDataPointsModelList());

        predictions.put("oneMinute", 1.0);
        predictions.put("fifteenMinutes", 15.0);
        predictions.put("thirtyMinutes", 30.0);
        predictions.put("oneHour", dataPointsList.getPredictDataPointsModelList().get(0).getY());
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
