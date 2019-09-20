import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

public class Run {
	public static void main(String[] args){
		Read_And_Write.Startup();
		Brain.startup();
		
		//Databasing.Recording.Start();
		//Brain.Training.load();
		Brain.Training.load();
		/*
		ApiContextInitializer.init();
		TelegramBotsApi botsApi = new TelegramBotsApi();
        try {
			botsApi.registerBot(new Bot());
			System.out.println("Messaging Bot is up and running!");
		} catch (TelegramApiRequestException e) {
			e.printStackTrace();
		} 
		*/
	}

}
