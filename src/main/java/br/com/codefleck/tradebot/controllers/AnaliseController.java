package br.com.codefleck.tradebot.controllers;

import java.io.IOException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import br.com.codefleck.tradebot.core.engine.TradingEngine;
import br.com.codefleck.tradebot.services.prediction.PricePrediction;

@Controller
@RequestMapping("/analise")
@Transactional
public class AnaliseController {

    @Autowired
    TradingEngine fleckBot;

    @GetMapping
    public ModelAndView noticiasLandingDataProvider(ModelAndView model) {

        model.addObject("botStatus", fleckBot.isRunning());
        model.setViewName("analise");

        return model;
    }

    @PostMapping("/treinar-redes")
    public ModelAndView treinarRedes(@ModelAttribute("tamanhoLote")int tamanhoLote,
                                     @ModelAttribute("epocas") int epocas,
                                     @ModelAttribute("simbolo") String simbolo,
                                     @ModelAttribute("categoria") String categoria){

        PricePrediction pricePrediction = new PricePrediction();
        try {
            pricePrediction.initTraining(tamanhoLote, epocas, simbolo, categoria);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ModelAndView model = new ModelAndView();
        model.setViewName("/analise");

        return model;
    }
}
