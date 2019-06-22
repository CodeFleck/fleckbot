package br.com.codefleck.tradebot.controllers;

import br.com.codefleck.tradebot.daos.StockDataDao;
import br.com.codefleck.tradebot.models.StockData;
import br.com.codefleck.tradebot.services.impl.ModelServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/admin/configuration")
@Transactional
public class ConfigurationController {

    @Autowired
    ModelServiceImpl modelService;
    @Autowired
    StockDataDao stockDataDao;

    @GetMapping
    public ModelAndView redesNeuraisLandingPage(ModelAndView model) {

        return model;
    }

    @PostMapping("/generate-model")
    public ModelAndView treinarRedes(@ModelAttribute("coin") String coin,
                                     @ModelAttribute("period") String period,
                                     @ModelAttribute("category") String category, ModelAndView model){

        modelService.trainModel(period, category);
        model.setViewName("/admin/configuration");

        return model;
    }

    @PostMapping("/transport-data")
    public ModelAndView transportData(){

        List<StockData> stockDataList = modelService.transportDataFromCSVIntoDatabase();

        stockDataList.stream().forEach(stockData -> stockDataDao.save(stockData));
        System.out.println("Stock Data has been created!");
        ModelAndView model = new ModelAndView();
        model.setViewName("/admin/configuration");

        return model;
    }

}
