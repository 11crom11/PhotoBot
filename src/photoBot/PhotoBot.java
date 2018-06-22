package photoBot;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;

import javax.imageio.ImageIO;

import org.telegram.telegrambots.api.methods.GetFile;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.File;
import org.telegram.telegrambots.api.objects.PhotoSize;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class PhotoBot extends TelegramLongPollingBot {

	@Override
	public String getBotUsername() {
		String nombreBot = "PhotosDASI_bot";
		
		return nombreBot;
	}

	@Override
	public void onUpdateReceived(Update update) {
		
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
		else if (update.getMessage().hasPhoto()){//Si hemos recibido una foto...
			boolean ok = false;
			List<PhotoSize> lFotos = update.getMessage().getPhoto();
			Long chatID = update.getMessage().getChatId();
			Integer userID = update.getMessage().getFrom().getId();
			Integer date = update.getMessage().getDate();
			
			ok = this.guardarFotoEnServidor(lFotos, chatID, userID, date);
			
			SendMessage message = new SendMessage().setChatId(chatID);
			
			if(ok){
				message.setText("Ya he recibido y almacenado correctamente tus imágenes.");
			}
			else{
				message.setText("Ha ocurrido un error :( ... ¿Puedes mandarme de nuevo las imágenes?");
			}
			
			try {
				execute(message);
			} catch (TelegramApiException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public String getBotToken() {
		String token = "530885872:AAHq86bNy6Yx08zpPuFXfTfBdE9YtlN90cI";
				
		return token;
	}
	
	
	private boolean guardarFotoEnServidor(List<PhotoSize> lFotos, Long idChat, Integer idUsuario, Integer fecha){
		boolean ok = true;
		int k = 2;
		PhotoSize ps = lFotos.stream()
				.sorted(Comparator.comparing(PhotoSize::getFileSize).reversed())
				.findFirst()
				.orElse(null);

		GetFile getFileMethod = new GetFile();
		getFileMethod.setFileId(ps.getFileId());

		try {
			File file = execute(getFileMethod);
			java.io.File fileJava = downloadFile(file.getFilePath());           
			//https://github.com/rubenlagus/TelegramBots/wiki/FAQ
			//https://www.mkyong.com/java/how-to-write-an-image-to-file-imageio/
			
			//bImg = ImageIO.read(fileJava);
			java.io.File dir = new java.io.File("./galeria/" + idUsuario);
			dir.mkdirs();
			ImageIO.write(ImageIO.read(fileJava), "jpeg", new java.io.File(dir.getPath() + "/" + fecha + ".jpeg"));
			
		} catch (TelegramApiException e) {
			e.printStackTrace();
			ok = false;
		} catch (IOException e) {
			e.printStackTrace();
			ok = false;
		}
		
		return ok;
	}
}
