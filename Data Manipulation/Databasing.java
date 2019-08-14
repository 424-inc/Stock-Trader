import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

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
					HashMap<String,ArrayList<BigDecimal>> data = new HashMap<String, ArrayList<BigDecimal>>();
					for(int i=0;i<Database.Stock_Names.size();i++) {
						data.put(Database.Stock_Names.get(i), new ArrayList<BigDecimal>());
					}
					while(recording) {
						for(int i=0;i<Database.Stock_Names.size();i++) {
							try {
								BigDecimal value = Stock_Info.Current_Price(Database.Stock_Names.get(i));
								data.get(Database.Stock_Names.get(i)).add(value);
								System.out.println(Database.Stock_Names.get(i)+": "+value);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
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
