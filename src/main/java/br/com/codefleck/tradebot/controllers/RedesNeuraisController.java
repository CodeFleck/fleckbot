package br.com.codefleck.tradebot.controllers;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.avro.ipc.specific.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.ta4j.core.BaseTimeSeries;

import com.google.gson.Gson;

import br.com.codefleck.tradebot.core.engine.TradingEngine;
import br.com.codefleck.tradebot.daos.DataPointsModelDao;
import br.com.codefleck.tradebot.daos.StockDataDao;
import br.com.codefleck.tradebot.models.DataPointsListModel;
import br.com.codefleck.tradebot.models.DataPointsModel;
import br.com.codefleck.tradebot.models.StockData;
import br.com.codefleck.tradebot.services.impl.PredictionServiceImpl;

@Controller
@RequestMapping("/admin/redes-neurais")
@Transactional
public class RedesNeuraisController {

    @Autowired
    TradingEngine fleckBot;
    @Autowired
    PredictionServiceImpl predictionService;
    @Autowired
    DataPointsModelDao dataPointsModelDao;
    @Autowired
    StockDataDao stockDataDao;

    @GetMapping
    public ModelAndView redesNeuraisLandingDataProvider(ModelAndView model) {

        model.addObject("botStatus", fleckBot.isRunning());

        List<DataPointsModel> todosDataPoints = dataPointsModelDao.all();

        List<DataPointsModel> predictsList = new ArrayList<>();
        List<DataPointsModel> actualsList = new ArrayList<>();

        for (DataPointsModel dataPoint : todosDataPoints) {

            if (dataPoint.getNomeConjunto().equals("LR0.001-15min_1ep_jun16jun17Prediction-Fechamento")){
                predictsList.add(dataPoint);
            }
            if (dataPoint.getNomeConjunto().equals("LR0.001-15min_1ep_jun16jun17Actual-Fechamento")) {
                actualsList.add(dataPoint);
            }
        }

        Gson gsonPredict = new Gson();
        Gson gsonActual = new Gson();

        model.addObject("predictsDataPoints", gsonPredict.toJson(predictsList));
        model.addObject("actualsDataPoints", gsonActual.toJson(actualsList));

        return model;
    }

    @PostMapping("/treinar-redes")
    public ModelAndView treinarRedes(@ModelAttribute("simbolo") String simbolo,
                                     @ModelAttribute("categoria") String categoria,
                                     @ModelAttribute("epocas") int epocas,
                                     @RequestParam("beginDate") String beginDate,
                                     @RequestParam("endDate") String endDate,
                                     @ModelAttribute("period") String period,
                                     @ModelAttribute("nomeDoConjunto") String nomeDoConjunto) throws IOException, ParseException {

        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");

        Date begingDate = formato.parse(beginDate);
        Date endingDate = formato.parse(endDate);

        BaseTimeSeries customTimeSeries = predictionService.createCSVFileForNeuralNets(begingDate, endingDate, period);

        List<String> dataPointList = predictionService.initTraining(epocas, simbolo, categoria, customTimeSeries, Integer.valueOf(period));

        ModelAndView model = new ModelAndView();
        model.setViewName("admin/redes-neurais");
        model.addObject("predictsDataPoints", dataPointList.get(0));
        model.addObject("actualsDataPoints", dataPointList.get(1));
        model.addObject("epocas", epocas);
        model.addObject("beginDate", beginDate);
        model.addObject("endDate", endDate);
        model.addObject("period", period);

        String cat = categoria;
        model.addObject("categoria", cat);

        DataPointsListModel dataPointsList = predictionService.prepareDataPointToBeSaved(dataPointList, nomeDoConjunto, categoria);
        Double errorPercentageAvg = predictionService.calculateErrorPercentageAverage(dataPointsList);
        Double errorPercentageLastDay = predictionService.calculateErrorPercentageLastDay(dataPointsList);
        Double majorError = predictionService.calculateMajorError(dataPointsList);
        Double minorError = predictionService.calculateMinorError(dataPointsList);

        NumberFormat formatter = new DecimalFormat("#0.00");

        model.addObject("errorPercentageAvg", formatter.format(errorPercentageAvg));
        model.addObject("errorPercentageLastDay", formatter.format(errorPercentageLastDay));
        model.addObject("majorError", formatter.format(majorError));
        model.addObject("minorError", formatter.format(minorError));

        for (DataPointsModel dataPoint : dataPointsList.getPredictDataPointsModelList()) {
            //dataPointsModelDao.save(dataPoint);
        }

        for (DataPointsModel dataPoint : dataPointsList.getActualDataPointsModelList()) {
            //dataPointsModelDao.save(dataPoint);
        }
        System.out.println("finished saving...");

        return model;
    }

    public void saveAllStockData(BaseTimeSeries customTimeSeries) {

            List<StockData> stockDataList = new ArrayList<>();
        for(int i=0; i<customTimeSeries.getEndIndex();i++){

            StockData stockData = new StockData(customTimeSeries.getBar(i).getSimpleDateName(),
                "BTC",
                customTimeSeries.getBar(i).getOpenPrice().doubleValue(),
                customTimeSeries.getBar(i).getClosePrice().doubleValue(),
                customTimeSeries.getBar(i).getMinPrice().doubleValue(),
                customTimeSeries.getBar(i).getMaxPrice().doubleValue(),
                customTimeSeries.getBar(i).getVolume().doubleValue());
            stockDataList.add(stockData);
        }

        stockDataList.stream().forEach(s -> stockDataDao.save(s));
    }
}
