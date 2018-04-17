package br.com.codefleck.tradebot.services.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ta4j.core.Order;
import org.ta4j.core.Trade;

import com.google.gson.Gson;

import br.com.codefleck.tradebot.exchanges.trading.api.impl.EventImpl;

@Service("eventService")
@Transactional
@ComponentScan(basePackages = {"br.com.codefleck.tradebot.repository"})
public class EventServiceImpl {

    List<String> stockEvents = new ArrayList<>();

    private String createEvent(Order order){

        EventImpl event = new EventImpl();

        event.setDate(String.valueOf(new Date().getTime()));

        String timeStamp = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new java.util.Date());
        //String amount = String.valueOf(order.getAmount());
        String amount = String.valueOf(0.00);

        if (order.isBuy()){
            event.setType("sign");
            event.setBackgroundColor("#FFFFFF");
            event.setBackgroundAlpha("0.5");
            event.setGraph("g1");
            event.setText("C");
            event.setDescription("Compra | " + timeStamp + " | Quantia $: ");
        } else if (order.isSell()){
            event.setType("flag");
            event.setBackgroundColor("#00CC00");
            event.setBackgroundAlpha("0.5");
            event.setGraph("g1");
            event.setText("V");
            event.setDescription("Vendra | " + timeStamp + " | Quantia $: ");
        }

        Gson gson = new Gson();
        String eventJSONString = gson.toJson(event);

        System.out.println(eventJSONString);

        stockEvents.add(eventJSONString);

        return eventJSONString;
    }

    public List<String> getStockEvents(List<Trade> tradeList) {

        if (tradeList != null && tradeList.size() > 0){

            for (Trade trade : tradeList) {

                createEvent(trade.getEntry());

                createEvent(trade.getExit());
            }
        }

        return stockEvents;
    }

    public void setStockEvents(List<String> stockEvents) {
        this.stockEvents = stockEvents;
    }

//            HashMap<String, String> eventForGraph = new HashMap<String, String>();
//
//            eventForGraph.put("date", orderDate);
//            eventForGraph.put("type", type);
//            eventForGraph.put("backgroundColor", name);
//            eventForGraph.put("backgroundAlpha", name);
//            eventForGraph.put("graph", name);
//            eventForGraph.put("text", name);
//            eventForGraph.put("description", name);

    //TODO create save event DAO and methods to persist in database. Create Gson of events for graph


}
