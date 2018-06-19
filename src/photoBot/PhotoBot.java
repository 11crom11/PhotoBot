package photoBot;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class PhotoBot extends TelegramLongPollingBot {

	@Override
	public String getBotUsername() {
		// TODO Auto-generated method stub
		
		String nombreBot = "PhotosDASI_bot";
		
		
		return nombreBot;
	}

	@Override
	public void onUpdateReceived(Update update) {
		// TODO Auto-generated method stub
		
		if (update.hasMessage() && update.getMessage().hasText()) {
	        SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
	                .setChatId(update.getMessage().getChatId())
	                .setText(update.getMessage().getText());
	        try {
	            execute(message); // Call method to send the message
	        } catch (TelegramApiException e) {
	            e.printStackTrace();
	        }
	    }

	}

	@Override
	public String getBotToken() {
		// TODO Auto-generated method stub
		
		String token = "530885872:AAHq86bNy6Yx08zpPuFXfTfBdE9YtlN90cI";
				
		return token;
	}

}
