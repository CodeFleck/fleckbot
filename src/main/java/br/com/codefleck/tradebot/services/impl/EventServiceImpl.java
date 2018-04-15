package br.com.codefleck.tradebot.services.impl;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;

import org.ta4j.core.Order;

import br.com.codefleck.tradebot.exchanges.trading.api.impl.EventImpl;

public class EventServiceImpl {

    public EventImpl createEvent(Order order){

        EventImpl event = new EventImpl();

        event.setDate(String.valueOf(new Date().getTime()));

        String timeStamp = new SimpleDateFormat("dd-MM-yyyy HH.mm.ss").format(new java.util.Date());
        String amount = String.valueOf(order.getAmount());

        if (order.isBuy()){
            event.setType("sign");
            event.setBackgroundColor("#FFFFFF");
            event.setGraph("g1");
            event.setText("C");
            event.setDescription("Compra\n " + timeStamp + "\nQuantia $: " + amount);
        } else if (order.isSell()){
            event.setType("flag");
            event.setBackgroundColor("#00CC00");
            event.setBackgroundAlpha("0.5");
            event.setGraph("g1");
            event.setText("V");
            event.setDescription("Venda\n " + timeStamp + "\nQuantia $: " + amount);
        }

        return event;
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

}
