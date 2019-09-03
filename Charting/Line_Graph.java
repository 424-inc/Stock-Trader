import java.awt.Color;
import java.util.ArrayList;
import java.util.Date;

import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

public class Line_Graph {
	static class Viewer{
		
		private static ArrayList<Integer> x = new ArrayList<Integer>();
		private static ArrayList<Double> y = new ArrayList<Double>();
		
		private static XYChart chart;
		private static SwingWrapper<XYChart> sw;
		
		public static void update(Double update) {
				x.add(x.size());
				y.add(update);
				chart.updateXYSeries("Current", x, y,null);
				sw.repaintChart();
		}
		public static void makeChart(Double initial) {
			x.add(0);
			y.add(initial);
			
			chart = QuickChart.getChart("Stock", "Time", "Price", "Current", x, y);
			chart.getSeriesMap().get("Current").setLineColor(Color.RED);
			chart.getStyler().setPlotBorderColor(Color.BLUE);
			chart.getStyler().setPlotBackgroundColor(Color.BLACK);
			chart.getStyler().setPlotGridLinesVisible(false);
			sw = new SwingWrapper<XYChart>(chart);
			//sw.getXChartPanel().setBackground(Color.BLACK);
			sw.displayChart();
			}
	}
}
