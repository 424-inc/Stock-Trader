import java.io.IOException;

public class Run {
	// Base directory so that all of the Information is stored in an organized manner
	// Desktop for now for simplicity
	public static String basedir = System.getProperty("user.home")+"/Desktop/StockTrader";
	public static void main(String[] args) throws IOException {
		Read_And_Write.Startup();
		Stock_Info.Current_Price();
	}

}
