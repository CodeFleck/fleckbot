package br.com.codefleck.tradebot.controllers;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import br.com.codefleck.tradebot.core.engine.TradingEngine;
import br.com.codefleck.tradebot.tradingInterfaces.BalanceInfo;
import br.com.codefleck.tradebot.tradingInterfaces.ExchangeNetworkException;
import br.com.codefleck.tradebot.tradingInterfaces.TradingApiException;

@Controller
@RequestMapping("/carteiras")
@Transactional
public class CarteirasController {

    private static final Logger LOG = LogManager.getLogger();

    @Autowired
    TradingEngine fleckBot;

    @GetMapping
    public ModelAndView carteirasLandingDataProvider(ModelAndView model) throws ExchangeNetworkException, TradingApiException {


        if (!fleckBot.isRunning()){
            fleckBot.initConfig();
        }

        BalanceInfo balanceInfo = fleckBot.getExchangeAdapter().getBalanceInfo();

        model.addObject("carteiras", balanceInfo);
        model.addObject("botStatus", fleckBot.isRunning());

        model.setViewName("carteiras");
        return model;
    }
}

