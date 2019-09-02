import java.io.IOException;

public class Trading {
	public static Bot bot = new Bot();
	public static class Simulation{
		private static double bumper = 1;
		private static double bank = 10;
		private static double stock = 0;
		private static double lastBuy;
		private static double lastSell;
		private static boolean sell = true;
		private static boolean buy = false;
		public static void Start() {
			buy();
			double HighestPoint = 0;
			double LowestPoint = 0;
			while(true) {
				try {
					double startingPoint = (double) Stock_Info.Current_Price("BTC-USD");
					HighestPoint = startingPoint;
					LowestPoint = startingPoint;
				} catch (IOException e) {
					e.printStackTrace();
				}
				while(sell) {
					double price = 0;
					try {
						price = Stock_Info.Current_Price("BTC-USD");
						System.out.println("Current Price: "+price);
					} catch (IOException e) {
						e.printStackTrace();
					}
					if(price>HighestPoint) {
						HighestPoint=price;
					}
					if(price>lastBuy+bumper&&price<HighestPoint) {
						sell();
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				while(buy) {
					double price = 0;
					try {
						price = Stock_Info.Current_Price("BTC-USD");
						System.out.println("Current Price: "+price);
					} catch (IOException e) {
						e.printStackTrace();
					}
					if(price<LowestPoint) {
						LowestPoint=price;
					}
					if(price<lastSell-bumper&&price>LowestPoint) {
						buy();
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
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
