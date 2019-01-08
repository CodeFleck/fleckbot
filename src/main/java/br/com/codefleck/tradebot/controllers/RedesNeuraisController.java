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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.ta4j.core.BaseTimeSeries;

import com.google.gson.Gson;

import br.com.codefleck.tradebot.core.engine.TradingEngine;
import br.com.codefleck.tradebot.daos.DataPointsModelDao;
import br.com.codefleck.tradebot.redesneurais.DataPointsListModel;
import br.com.codefleck.tradebot.redesneurais.DataPointsModel;
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

    @GetMapping
    public ModelAndView redesNeuraisLandingDataProvider(ModelAndView model) {

        model.addObject("botStatus", fleckBot.isRunning());

        List<DataPointsModel> todosDataPoints = dataPointsModelDao.all();

        List<DataPointsModel> predictsList = new ArrayList<>();
        List<DataPointsModel> actualsList = new ArrayList<>();

        for (DataPointsModel dataPoint : todosDataPoints) {

            if (dataPoint.getNomeConjunto().equals("b)5epocas-jan17jun17-1D")){
                predictsList.add(dataPoint);
            }
            if (dataPoint.getNomeConjunto().equals("b)5epocas-jan17jun17-1D")){
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

        List<String> dataPointList = predictionService.initTraining(epocas, simbolo, categoria, customTimeSeries);

        ModelAndView model = new ModelAndView();
        model.setViewName("admin/redes-neurais");
        model.addObject("predictsDataPoints", dataPointList.get(0));
        model.addObject("actualsDataPoints", dataPointList.get(1));
        String cat = categoria;
        model.addObject("categoria", cat);

        DataPointsListModel dataPointsList = predictionService.prepareDataPointToBeSaved(dataPointList, nomeDoConjunto, categoria);
        Double errorPercentageAvg = predictionService.calculateErrorPercentageAverage(dataPointsList);
        Double errorPercentageLastDay = predictionService.calculateErrorPercentageLastDay(dataPointsList);

        NumberFormat formatter = new DecimalFormat("#0.00");

        model.addObject("errorPercentageAvg", formatter.format(errorPercentageAvg));
        model.addObject("errorPercentageLastDay", formatter.format(errorPercentageLastDay));
        System.out.println("Prediction: ");
            System.out.println("*** BEGIN   ->>>");
        for (DataPointsModel dataPoint : dataPointsList.getPredictDataPointsModelList()) {
           System.out.println(dataPoint.getX() + "," + dataPoint.getY());
            //dataPointsModelDao.save(dataPoint);
        }
        System.out.println("Actual price: ");
        for (DataPointsModel dataPoint : dataPointsList.getActualDataPointsModelList()) {
            System.out.println(dataPoint.getX() + "," + dataPoint.getY());
            //dataPointsModelDao.save(dataPoint);
        }
            System.out.println("<<<- END ***");

        return model;
    }
}
