package br.com.codefleck.tradebot.controllers;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.experimental.theories.DataPoints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;

import br.com.codefleck.tradebot.core.engine.TradingEngine;
import br.com.codefleck.tradebot.daos.DataPointsModelDao;
import br.com.codefleck.tradebot.redesneurais.DataPointsListModel;
import br.com.codefleck.tradebot.redesneurais.DataPointsModel;
import br.com.codefleck.tradebot.services.impl.PredictionServiceImpl;

@Controller
@RequestMapping("/redes-neurais")
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

            if (dataPoint.getNomeConjunto().equals("2-epocas-1m-abril2017Prediction-Fechamento")){
                predictsList.add(dataPoint);
            }
            if (dataPoint.getNomeConjunto().equals("2-epocas-1m-abril2017Actual-Fechamento")){
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

        predictionService.createCSVFileForNeuralNets(begingDate, endingDate, period);

        List<String> dataPointList = predictionService.initTraining(epocas, simbolo, categoria);

        ModelAndView model = new ModelAndView();
        model.setViewName("/redes-neurais");
        model.addObject("predictsDataPoints", dataPointList.get(0));
        model.addObject("actualsDataPoints", dataPointList.get(1));
        model.addObject("categoria", categoria);

        DataPointsListModel dataPointsListModel = predictionService.prepareDataPointToBeSaved(dataPointList, nomeDoConjunto, categoria);

        for (DataPointsModel dataPoint : dataPointsListModel.getPredictDataPointsModelList()) {
            dataPointsModelDao.save(dataPoint);
        }
        for (DataPointsModel dataPoint : dataPointsListModel.getActualDataPointsModelList()) {
            dataPointsModelDao.save(dataPoint);
        }

        return model;
    }
}
