import java.util.ArrayList;

public class Database {
	public static String basedir = System.getProperty("user.home")+"/Desktop/StockTrader";
	public static ArrayList<String> Stock_Names;
	public static Telegram telegram;
	public static Telegram Telegram;
	public class Telegram{
		String username;
		String password;
	}
}
