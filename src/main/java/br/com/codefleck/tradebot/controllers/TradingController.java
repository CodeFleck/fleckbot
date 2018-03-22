package br.com.codefleck.tradebot.controllers;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.ta4j.core.TimeSeries;

import br.com.codefleck.tradebot.core.engine.TradingEngine;
import br.com.codefleck.tradebot.core.util.CsvBarsLoader;
import br.com.codefleck.tradebot.daos.LogEntryImplDao;
import br.com.codefleck.tradebot.exchanges.trading.api.impl.LogEntryImpl;

@Controller
@RequestMapping("/trading")
@Transactional
public class TradingController {

    final Logger LOG = LogManager.getLogger();

    @Autowired
    TradingEngine fleckBot;

    @Autowired
    LogEntryImplDao logEntryImplDao;

    @GetMapping
    public ModelAndView tradingConfig(){

        ModelAndView modelAndView = new ModelAndView("trading/trading");

        return loadEntries(modelAndView);
    }

    @GetMapping("/ligar")
    public ModelAndView ligarBot(ModelAndView model){

        if (!fleckBot.isRunning()) {
            fleckBot.initConfig();
            fleckBot.start();
            model.addObject("botStatus", fleckBot.isRunning());
        }

        model.setViewName("trading/trading");
        return model;
    }

    @GetMapping("/desligar")
    public ModelAndView desligarBot(ModelAndView model){

        if (fleckBot.isRunning()) {
            fleckBot.shutdown();
            model.addObject("botStatus", fleckBot.isRunning());
        }

        model.setViewName("trading/trading");
        return model;
    }

    private ModelAndView loadEntries(ModelAndView modelAndView)
    {
        if (fleckBot.isRunning()){
            modelAndView.addObject("botStatus", true);
        } else {
            modelAndView.addObject("botStatus", false);
        }
        modelAndView.addObject("logEntryList", logEntryImplDao.all());
        return modelAndView;
    }



}
