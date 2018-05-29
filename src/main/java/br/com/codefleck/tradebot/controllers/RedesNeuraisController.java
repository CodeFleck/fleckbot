package br.com.codefleck.tradebot.controllers;

import java.io.IOException;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import br.com.codefleck.tradebot.core.engine.TradingEngine;
import br.com.codefleck.tradebot.redesneurais.DataPointsListModel;
import br.com.codefleck.tradebot.redesneurais.PricePrediction;

@Controller
@RequestMapping("/redes-neurais")
@Transactional
public class RedesNeuraisController {

    @Autowired
    TradingEngine fleckBot;

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
    public ModelAndView treinarRedes(@ModelAttribute("tamanhoLote")int tamanhoLote,
                                     @ModelAttribute("epocas") int epocas,
                                     @ModelAttribute("simbolo") String simbolo,
                                     @ModelAttribute("categoria") String categoria) throws IOException {

        PricePrediction pricePrediction = new PricePrediction();

        ModelAndView model = new ModelAndView();
        model.setViewName("/redes-neurais");

        List<String> dataPointList = pricePrediction.initTraining(tamanhoLote, epocas, simbolo, categoria);
        model.addObject("predictsDataPoints", dataPointList.get(0));
        model.addObject("actualsDataPoints", dataPointList.get(1));
        model.addObject("categoria", categoria);

        return model;
    }
}
