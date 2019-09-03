import java.io.IOException;

public class Trading {
	public static Bot bot = new Bot();
	public static class Simulation{
		private static boolean run = false;
		private static double sellbuffer = 10;
		private static double buybuffer = 1;
		private static double bank = 100;
		private static double stock = 0;
		private static double lastBuy;
		private static double lastSell;
		private static boolean sell = true;
		private static boolean buy = false;
		public static void Start() {
			Thread threadsim = new Thread() {
				public void run() {
					run = true;
					buy();
					double HighestPoint = 0;
					double LowestPoint = 0;
					try {
						Line_Graph.Viewer.makeChart(Stock_Info.Current_Price("BTC-USD"));
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					while(run) {
						try {
							double startingPoint = Stock_Info.Current_Price("BTC-USD");
							HighestPoint = startingPoint;
							LowestPoint = startingPoint;
						} catch (IOException e) {
							e.printStackTrace();
						}
						while(sell&&run) {
							double price = 0;
							try {
								price = Stock_Info.Current_Price("BTC-USD");
								Line_Graph.Viewer.update(price);
								System.out.println("Current Price: "+price);
							} catch (IOException e) {
								e.printStackTrace();
							}
							if(price>HighestPoint) {
								HighestPoint=price;
							}
							if(price>lastBuy+sellbuffer&&price<HighestPoint) {
								sell();
							}
							try {
								Thread.sleep(Database.timeInterval);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						while(buy&&run) {
							double price = 0;
							try {
								price = Stock_Info.Current_Price("BTC-USD");
								Line_Graph.Viewer.update(price);
								System.out.println("Current Price: "+price);
							} catch (IOException e) {
								e.printStackTrace();
							}
							if(price<LowestPoint) {
								LowestPoint=price;
							}
							if(price<lastSell-buybuffer&&price>LowestPoint) {
								buy();
							}
							try {
								Thread.sleep(Database.timeInterval);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				}
			};
			if(!run) {
				run=true;
				threadsim.start();
			}else {
				bot.sendMessage(796720243, "Simulation is already running");
			}
		}
		public static void Stop() {
			run=false;
		}
		public static void Reset() {
			run=false;
			bank=100;
			stock=0;
			sell=true;
			buy=false;
		}
		private static void buy() {
			double price = 0;
			try {
				price = Stock_Info.Current_Price("BTC-USD");
				stock = bank/price;
				bank = 0;
				lastBuy = price;
			} catch (IOException e) {
				e.printStackTrace();
			}
			sell=true;
			buy=false;
			bot.sendMessage(796720243, "Bought: "+price);
			System.out.println("Bought: "+price);
		}
		private static void sell() {
			double price = 0;
			try {
				price = Stock_Info.Current_Price("BTC-USD");
				bank = stock*price;
				stock = 0;
				lastSell = price;
			} catch (IOException e) {
				e.printStackTrace();
			}
			buy=true;
			sell=false;
			bot.sendMessage(796720243, "Sold: "+bank+" ----- "+price);
			System.out.println("Sold: "+bank+" ----- "+price);
		}
	}
}
