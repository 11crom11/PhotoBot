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
		else if (update.getMessage().hasPhoto()){//Si hemos recibido una foto...
			List<PhotoSize> lFotos = update.getMessage().getPhoto();
			
			PhotoSize ps = lFotos.stream()
						.sorted(Comparator.comparing(PhotoSize::getFileSize).reversed())
						.findFirst()
						.orElse(null);
			
	        GetFile getFileMethod = new GetFile();
	        getFileMethod.setFileId(ps.getFileId());
	  
	        try {
	            // We execute the method using AbsSender::execute method.
	            File file = execute(getFileMethod);
	            
	            java.io.File fileJava = downloadFile(file.getFilePath());           
	            //BufferedImage bImg = new BufferedImage(830, 467, BufferedImage.TYPE_INT_ARGB);
	            
	            //https://github.com/rubenlagus/TelegramBots/wiki/FAQ
	            //https://www.mkyong.com/java/how-to-write-an-image-to-file-imageio/
	            
	            try {
					//bImg = ImageIO.read(fileJava);
	            	java.io.File dir = new java.io.File("./galeria/" + update.getMessage().getFrom().getId());
	            	dir.mkdirs();
	            	
	            	ImageIO.write(ImageIO.read(fileJava), "jpeg", new java.io.File(dir.getPath() + "/" + update.getMessage().getDate() + ".jpeg"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


	        } catch (TelegramApiException e) {
	            e.printStackTrace();
	        }
	        
			SendPhoto message = new SendPhoto()
								.setChatId(update.getMessage().getChatId())
								.setPhoto(lFotos.get(0).getFileId());
			
			try {
				sendPhoto(message);
			} catch (TelegramApiException e) {
				// TODO Auto-generated catch block
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
