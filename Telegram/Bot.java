import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Bot extends TelegramLongPollingBot {
    public void onUpdateReceived(Update update) {
    		System.out.println(update.getMessage().getFrom().getUserName()+": "+update.getMessage().getFrom().getId()+": "+update.getMessage().getText());
    }
    public void sendMessage(Integer ChatId, String text) {
    	 	SendMessage message = new SendMessage()
	    .setChatId(ChatId.toString())
	    .setText(text);
	    try {
	    		execute(message);
	    	}catch (TelegramApiException e) {
	    	}
    }
    public String getBotUsername() {
        // TODO
        return Database.telegram.username;
    }
    public String getBotToken() {
        // TODO
        return Database.telegram.password;
    }
}
