package br.com.codefleck.tradebot.controllers;

import br.com.codefleck.tradebot.core.engine.TradingEngine;
import br.com.codefleck.tradebot.core.util.PlotUtil;
import br.com.codefleck.tradebot.daos.DataPointsDao;
import br.com.codefleck.tradebot.daos.StockDataDao;
import br.com.codefleck.tradebot.models.DataPoints;
import br.com.codefleck.tradebot.models.DataPointsListResultSet;
import br.com.codefleck.tradebot.models.DataPointsListType;
import br.com.codefleck.tradebot.services.impl.PredictionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

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
    DataPointsDao dataPointsDao;
    @Autowired
    StockDataDao stockDataDao;
    @Autowired
    PlotUtil plotUtil;

    @GetMapping
    public ModelAndView redesNeuraisLandingPage(ModelAndView model) {

        model.addObject("botStatus", fleckBot.isRunning());

        List<DataPoints> todosDataPoints = dataPointsDao.ListLatest(1000);

        List<DataPoints> predictsList = new ArrayList<>();
        List<DataPoints> actualsList = new ArrayList<>();

        for (DataPoints dataPoint : todosDataPoints) {

            if (dataPoint.getDataPointsListType().equals(DataPointsListType.PREDICTDATAPOINTSLIST)){
                predictsList.add(dataPoint);
            } else {
                actualsList.add(dataPoint);

            }
        }

        DataPointsListResultSet resultSet = new DataPointsListResultSet();
        resultSet.setPredictDataPointsList(predictsList);
        resultSet.setActualDataPointsList(actualsList);

        List<String> dataPointsStringList = plotUtil.plot(resultSet);

        model.addObject("nomeConjunto", predictsList.get(0).getNomeConjunto());
        model.addObject("beginDate", predictsList.get(0).getLocalDateTime().toLocalDate().toString());
        model.addObject("endDate", predictsList.get(predictsList.size()-1).getLocalDateTime().toLocalDate().toString());
        model.addObject("predictsDataPoints", dataPointsStringList.get(0));
        model.addObject("actualsDataPoints", dataPointsStringList.get(1));

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

        predictionService.createCSVFileForNeuralNets(begingDate, endingDate, period);

        DataPointsListResultSet resultSet = null;
        try {
            try {
                resultSet = predictionService.initTraining(epocas, simbolo, categoria, period, nomeDoConjunto);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<String> dataPointsStringList = plotUtil.plot(resultSet);

        ModelAndView model = new ModelAndView();
        model.setViewName("admin/redes-neurais");
        model.addObject("nomeConjunto", nomeDoConjunto);
        model.addObject("predictsDataPoints", dataPointsStringList.get(0));
        model.addObject("actualsDataPoints", dataPointsStringList.get(1));
        model.addObject("epocas", epocas);
        model.addObject("beginDate", beginDate);
        model.addObject("endDate", endDate);
        model.addObject("period", period);

        String cat = categoria;
        model.addObject("categoria", cat);

        Double errorPercentageAvg = predictionService.calculateErrorPercentageAverage(resultSet);
        Double errorPercentageLastDay = predictionService.calculateErrorPercentageLastDay(resultSet);
        Double majorError = predictionService.calculateMajorError(resultSet);
        Double minorError = predictionService.calculateMinorError(resultSet);

        NumberFormat formatter = new DecimalFormat("#0.00");

        model.addObject("errorPercentageAvg", formatter.format(errorPercentageAvg));
        model.addObject("errorPercentageLastDay", formatter.format(errorPercentageLastDay));
        model.addObject("majorError", formatter.format(majorError));
        model.addObject("minorError", formatter.format(minorError));

        return model;
    }
}
