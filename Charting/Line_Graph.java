import java.awt.Color;
import java.util.ArrayList;
import java.util.Date;

import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

public class Line_Graph {
	static class Scheme{
		public static Color line = new Color(225,0,56);
		public static Color backround = new Color(0,0,0);
		public static Color text = new Color(240,200,8);
		public static Color random = new Color(234,99,140);
	}
	static class Viewer{
		
		private static ArrayList<Integer> x = new ArrayList<Integer>();
		private static ArrayList<Double> y = new ArrayList<Double>();
		
		private static XYChart chart;
		private static SwingWrapper<XYChart> sw;
		
		public static void update(Double update) {
			if(x.size()>Database.chartTime-1) {
				y.remove(0);
				y.add(update);
			}else {
				x.add(x.size());
				y.add(update);
			}
				chart.updateXYSeries("Current", x, y,null);
				sw.repaintChart();
		}
		public static void makeChart(Double initial) {
			x.add(0);
			y.add(initial);
			
			chart = QuickChart.getChart("Stock", "Time", "Price", "Current", x, y);
			chart.getSeriesMap().get("Current").setLineColor(Scheme.line);
			chart.getStyler().setPlotBorderColor(Scheme.random);
			chart.getStyler().setPlotBackgroundColor(Scheme.backround);
			chart.getStyler().setPlotGridLinesVisible(false);
			chart.getStyler().setChartBackgroundColor(Scheme.backround);
			chart.getStyler().setChartFontColor(Scheme.text);
			sw = new SwingWrapper<XYChart>(chart);
			sw.displayChart();
			}
	}
}
