package br.com.codefleck.tradebot.services.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ta4j.core.BaseTimeSeries;
import org.ta4j.core.Order;
import org.ta4j.core.Trade;

import com.google.gson.Gson;

import br.com.codefleck.tradebot.exchanges.trading.api.impl.EventImpl;

@Service("eventService")
@Transactional
@ComponentScan(basePackages = {"br.com.codefleck.tradebot.repository"})
public class EventServiceImpl {

    List<String> stockEvents = new ArrayList<>();

    private String createEvent(Order order, BaseTimeSeries series, boolean profitable){

        EventImpl event = new EventImpl();

        Date d = Date.from(series.getBar(order.getIndex()).getEndTime().toInstant());
        String timeStamp = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(d.getTime());
        String amount = String.valueOf(order.getAmount());

        if (order.isBuy()){
            event.setDate(timeStamp);
            event.setDateInMilis(String.valueOf(d.getTime()));
            event.setType("sign");
            event.setBackgroundColor("#FFFFFF");
            event.setBackgroundAlpha("0.5");
            event.setGraph("g1");
            event.setText("C");
            event.setDescription(timeStamp +" Preço: " + order.getPrice() + " Quantia: " + amount);
        } else if (order.isSell() && profitable){
            event.setDate(timeStamp);
            event.setDateInMilis(String.valueOf(d.getTime()));
            event.setType("flag");
            event.setBackgroundColor("#00CC00");
            event.setBackgroundAlpha("0.5");
            event.setGraph("g1");
            event.setText("V");
            event.setDescription(timeStamp + " Preço: " + order.getPrice() + " Quantia: " + amount);
        } else if (order.isSell() && !profitable){
            event.setDate(timeStamp);
            event.setDateInMilis(String.valueOf(d.getTime()));
            event.setType("flag");
            event.setBackgroundColor("#f24b4b");
            event.setBackgroundAlpha("0.5");
            event.setGraph("g1");
            event.setText("V");
            event.setDescription(timeStamp + " Preço: " + order.getPrice() + " Quantia: " + amount);
        }

        Gson gson = new Gson();
        String eventJSONString = gson.toJson(event);

        stockEvents.add(eventJSONString);

        return eventJSONString;
    }

    public List<String> getStockEvents(List<Trade> tradeList, BaseTimeSeries series) {

        if (tradeList != null && tradeList.size() > 0){

            for (Trade trade : tradeList) {

                boolean profitable = true;


                if (trade.getExit().getPrice().isLessThan(trade.getEntry().getPrice())){

                    profitable = false;

                }
                createEvent(trade.getEntry(), series, profitable);

                createEvent(trade.getExit(), series, profitable);

            }
        }

        return stockEvents;
    }
}
