import org.telegram.telegrambots.meta.api.objects.Update;

public class Commands {
	private static Integer ID;

	public static void main(Update input){
		ID = input.getMessage().getFrom().getId();
		Bot bot = new Bot();
		if(input.getMessage().getText().startsWith("/")) {
		String[] formatted = format(input);
			switch(formatted[0].toLowerCase()) {
			case "/startsim": Trading.Simulation.Start();
			break;
			case "/stopsim": Trading.Simulation.Stop();
			break;
			case "/resetsim": Trading.Simulation.Reset();
			break;
			default: bot.sendMessage(ID, "Error 404: Command Not Found");
			}
		}else {
			bot.sendMessage(ID, "010010010010000001100001011011010010000001000111011001010110111001100101011100110110100101110011");
		}
	}

	private static String[] format(Update input) {
		if(input.getMessage().getText().contains(" ")) {
			String[] output = input.getMessage().getText().split(" ");
			return output;
		}else {
			String[] output = {input.getMessage().getText()};
			return output;
		}
	}
}
