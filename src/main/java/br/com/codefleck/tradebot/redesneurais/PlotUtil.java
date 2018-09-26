package br.com.codefleck.tradebot.redesneurais;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.ta4j.core.TimeSeries;

import com.google.gson.Gson;

public class PlotUtil {

    public static List<String> plot(double[] predicts, double[] actuals, String name, TimeSeries testTimeSeries) {
        double[] index = new double[predicts.length];
        for (int i = 0; i < predicts.length; i++)
            index[i] = i;
        int min = minValue(predicts, actuals);
        int max = maxValue(predicts, actuals);
        final XYSeriesCollection dataSet = new XYSeriesCollection();

        String predictsDataPoints = addSeries(dataSet, index, predicts, "Predicts", testTimeSeries);
        String actualsDataPoints = addSeries(dataSet, index, actuals, "Actuals", testTimeSeries);

        List<String> dataPointsList = new ArrayList<>();
        dataPointsList.add(predictsDataPoints);
        dataPointsList.add(actualsDataPoints);

//        final JFreeChart chart = ChartFactory.createXYLineChart(
//            "Prediction Result", // chart title
//            "Index", // x axis label
//            name, // y axis label
//            dataSet, // data
//            PlotOrientation.VERTICAL,
//            true, // include legend
//            true, // tooltips
//            false // urls
//        );
//        XYPlot xyPlot = chart.getXYPlot();
//        // X-axis
//        final NumberAxis domainAxis = (NumberAxis) xyPlot.getDomainAxis();
//        domainAxis.setRange((int) index[0], (int) (index[index.length - 1] + 2));
//        domainAxis.setTickUnit(new NumberTickUnit(20));
//        domainAxis.setVerticalTickLabels(true);
//        // Y-axis
//        final NumberAxis rangeAxis = (NumberAxis) xyPlot.getRangeAxis();
//        rangeAxis.setRange(min, max);
//        rangeAxis.setTickUnit(new NumberTickUnit(50));
//        final ChartPanel panel = new ChartPanel(chart);
//        final JFrame f = new JFrame();
//        f.add(panel);
//        f.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
//        f.pack();
//        f.setVisible(false);

        return dataPointsList;
    }

    private static String addSeries (final XYSeriesCollection dataSet, double[] x, double[] y, String label, TimeSeries testTimeSeries){

        final XYSeries s = new XYSeries(label);
        Gson gsonObj = new Gson();

        Map<Object,Object> map = null;
        List<Map<Object,Object>> list = new ArrayList<Map<Object,Object>>();

        System.out.println("************" + label + "**************");

        for( int j = 0; j < x.length; j++ ){
            s.add(x[j], y[j]);
            map = new HashMap<Object,Object>();
            map.put("x", x[j]);  map.put("y", y[j]);
            System.out.println(x[j] + ", " + y[j] + ", " + testTimeSeries.getBar(j).getSimpleDateName());
            list.add(map);
        }

        System.out.println("**************************");

        dataSet.addSeries(s);
        String dataPoints = gsonObj.toJson(list);
        return dataPoints;
    }

    private static int minValue (double[] predicts, double[] actuals) {
        double min = Integer.MAX_VALUE;
        for (int i = 0; i < predicts.length; i++) {
            if (min > predicts[i]) min = predicts[i];
            if (min > actuals[i]) min = actuals[i];
        }
        return (int) (min * 0.98);
    }

    private static int maxValue (double[] predicts, double[] actuals) {
        double max = Integer.MIN_VALUE;
        for (int i = 0; i < predicts.length; i++) {
            if (max < predicts[i]) max = predicts[i];
            if (max < actuals[i]) max = actuals[i];
        }
        return (int) (max * 1.02);
    }

}