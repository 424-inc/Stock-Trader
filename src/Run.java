import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

public class Run {
	public static void main(String[] args){
		Brain.build();
		/*
		Read_And_Write.Startup();
		Databasing.Recording.Start();
		
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
