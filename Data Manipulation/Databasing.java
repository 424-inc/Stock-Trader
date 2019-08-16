import java.util.Map;
import yahoofinance.Stock;

public class Databasing {
	private static boolean recording;
	public static class Recording{
		public static void Stop() {
			recording=false;
		}
		public static void Start(){
			recording=true;
			Thread thread = new Thread() {
				public void run() {
					while(recording) {
						Map<String, Stock> stocks = Stock_Info.Current_Price_Array(Database.Stock_Names);
						for(int i=0;i<Database.Stock_Names.size();i++) {
							System.out.println(Database.Stock_Names.get(i)+": "+stocks.get(Database.Stock_Names.get(i)).getQuote().getPrice());
						}
						System.out.println("----------------------------\n----------------------------\n----------------------------\n");
						try {
							Thread.sleep(1000*60);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			};
			thread.start();
		}
	}
}
