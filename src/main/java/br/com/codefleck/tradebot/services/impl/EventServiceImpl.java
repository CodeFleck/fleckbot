package br.com.codefleck.tradebot.services.impl;

import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ta4j.core.BaseTimeSeries;
import org.ta4j.core.Order;
import org.ta4j.core.TimeSeries;
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
        //String amount = String.valueOf(order.getAmount());
        String amount = String.valueOf(0.00);

        if (order.isBuy()){
            event.setDate(timeStamp);
            event.setType("sign");
            event.setBackgroundColor("#FFFFFF");
            event.setBackgroundAlpha("0.5");
            event.setGraph("g1");
            event.setText("C");
            event.setDescription(timeStamp + " Compra: $" + " Preço: " + order.getPrice());
        } else if (order.isSell() && profitable){
            event.setDate(timeStamp);
            event.setType("flag");
            event.setBackgroundColor("#00CC00");
            event.setBackgroundAlpha("0.5");
            event.setGraph("g1");
            event.setText("V");
            event.setDescription(timeStamp + " Venda: $" + " Preço: " + order.getPrice());
        } else if (order.isSell() && !profitable){
            event.setDate(timeStamp);
            event.setType("flag");
            event.setBackgroundColor("#888888");
            event.setBackgroundAlpha("0.5");
            event.setGraph("g1");
            event.setText("V");
            event.setDescription(timeStamp + " Venda: $" + " Preço: " + order.getPrice());
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
