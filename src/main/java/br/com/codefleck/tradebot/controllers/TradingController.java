package br.com.codefleck.tradebot.controllers;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import br.com.codefleck.tradebot.core.engine.TradingEngine;
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

    @GetMapping("/ligar")
    public String ligarBot(ModelAndView modelAndView) {

        if (!fleckBot.isRunning()) {
            fleckBot.initConfig();
            fleckBot.start();
            modelAndView.addObject("botStatus", fleckBot.isRunning());
        }
        loadEntries(modelAndView);
        return "redirect:/trading";
    }

    @GetMapping("/desligar")
    public String desligarBot(ModelAndView modelAndView) {

        if (fleckBot.isRunning()) {

            for (LogEntryImpl logEntry : fleckBot.logEntryList) {
                logEntryImplDao.save(logEntry);
            }
            fleckBot.shutdown();
            modelAndView.addObject("botStatus", fleckBot.isRunning());
        }
        loadEntries(modelAndView);
        return "redirect:/trading";
    }

    private ModelAndView loadEntries(ModelAndView modelAndView) {
        if (fleckBot.isRunning()){
            modelAndView.addObject("botStatus", true);
        } else {
            modelAndView.addObject("botStatus", false);
        }
        modelAndView.addObject("logEntryList", logEntryImplDao.all());
        return modelAndView;
    }

    @GetMapping
    public ModelAndView list(@RequestParam(defaultValue = "0", required = false) int page) {
        ModelAndView modelAndView = new ModelAndView("trading");
        loadEntries(modelAndView);
        modelAndView.addObject("paginatedList", logEntryImplDao.paginated(page, 20));
        return modelAndView;
    }


}
