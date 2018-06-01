package br.com.codefleck.tradebot.controllers;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

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
        model.setViewName("redes-neurais");

//        List<DataPointsListModel> todosDataPoints = dataPointListDao.all();

//        if (todosDataPoints != null && todosDataPoints.size() > 0){
//            DataPointsListModel ultimoDataPointModel = dataPointListDao.findById(todosDataPoints.size());
//            model.addObject("predictsDataPoints", ultimoDataPointModel.getPredictsDataPoints());
//            model.addObject("actualsDataPoints", ultimoDataPointModel.getActualsDataPoints());
//            model.addObject("categoria", ultimoDataPointModel.getName());
//        }

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

        String file = predictionService.createCSVFileForNeuralNets(begingDate, endingDate, period);

        List<String> dataPointList = predictionService.initTraining(epocas, simbolo, categoria, file);

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
