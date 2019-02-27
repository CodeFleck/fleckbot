package br.com.codefleck.tradebot.controllers;

import br.com.codefleck.tradebot.core.engine.TradingEngine;
import br.com.codefleck.tradebot.daos.DataPointsModelDao;
import br.com.codefleck.tradebot.daos.StockDataDao;
import br.com.codefleck.tradebot.models.DataPointsListResultSet;
import br.com.codefleck.tradebot.models.DataPointsModel;
import br.com.codefleck.tradebot.services.impl.PredictionServiceImpl;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.ta4j.core.BaseTimeSeries;

import javax.transaction.Transactional;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    public ModelAndView redesNeuraisLandingPage(ModelAndView model) {

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
                                     @ModelAttribute("nomeDoConjunto") String nomeDoConjunto) {

        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");

        Date begingDate = null;
        try {
            begingDate = formato.parse(beginDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date endingDate = null;
        try {
            endingDate = formato.parse(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        BaseTimeSeries customTimeSeries = predictionService.createCSVFileForNeuralNets(begingDate, endingDate, period);

        List<String> dataPointList = null;
        try {
            dataPointList = predictionService.initTraining(epocas, simbolo, categoria, period);
        } catch (IOException e) {
            e.printStackTrace();
        }

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

        DataPointsListResultSet dataPointsListResultSet = predictionService.prepareDataPointToBeSaved(dataPointList, nomeDoConjunto, customTimeSeries);
        Double errorPercentageAvg = predictionService.calculateErrorPercentageAverage(dataPointsListResultSet);
        Double errorPercentageLastDay = predictionService.calculateErrorPercentageLastDay(dataPointsListResultSet);
        Double majorError = predictionService.calculateMajorError(dataPointsListResultSet);
        Double minorError = predictionService.calculateMinorError(dataPointsListResultSet);

        NumberFormat formatter = new DecimalFormat("#0.00");

        model.addObject("errorPercentageAvg", formatter.format(errorPercentageAvg));
        model.addObject("errorPercentageLastDay", formatter.format(errorPercentageLastDay));
        model.addObject("majorError", formatter.format(majorError));
        model.addObject("minorError", formatter.format(minorError));


        return model;
    }
}
