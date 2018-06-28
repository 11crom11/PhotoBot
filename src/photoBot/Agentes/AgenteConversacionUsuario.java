package photoBot.Agentes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import org.drools.lang.dsl.DSLMapParser.value_chunk_return;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.GetFile;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.File;
import org.telegram.telegrambots.api.objects.PhotoSize;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;

public class AgenteConversacionUsuario extends Agent {
	
	private PhotoBot photoBot;
	private TelegramBotsApi apiTelegram;
	
	private ComportamientoAgenteConversacionUsuario comportamiento;
	
	@Override
	protected void setup() {
		super.setup();
		
        ApiContextInitializer.init();

        this.apiTelegram = new TelegramBotsApi();
        this.photoBot = new PhotoBot();
        this.comportamiento = new ComportamientoAgenteConversacionUsuario(this, photoBot);
        
        try {
            this.apiTelegram.registerBot(this.photoBot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        
		addBehaviour(this.comportamiento);
	}
	
	@Override
	protected void takeDown(){
		System.out.println(new Date());
	}
	
	public class PhotoBot extends TelegramLongPollingBot {

		private Long chatID;
		private Integer userID;
		private Integer date;
		private Update update;

		public Integer getUserID(){
			return userID;
		}
		
		@Override
		public String getBotUsername() {
			String nombreBot = "PhotosDASI_bot";
			return nombreBot;
		}

			
		@Override
		public void onUpdateReceived(Update update) {
			this.update = update;
			
			this.chatID = update.getMessage().getChatId();
			this.userID = update.getMessage().getFrom().getId();
			this.date = update.getMessage().getDate();
			Behaviour estado = comportamiento.getEstado();
			String estadoActualString = estado.getBehaviourName();
			

			/**
			if (update.hasMessage() && update.getMessage().hasText() && update.getMessage().getText().equalsIgnoreCase("/dame")) {
				devolverTodasLasImagenesDelUsuario(userID, chatID);
		    }
			else if (update.hasMessage() && update.getMessage().hasText()) {
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
				String msj = "";
				List<PhotoSize> lFotos = update.getMessage().getPhoto();
				
				ok = this.guardarFotoEnServidor(lFotos, chatID, userID, date);
				
				if(ok){
					msj = "Ya he recibido y almacenado correctamente tus imágenes.";
				}
				else{
					msj = "Ha ocurrido un error :( ... ¿Puedes mandarme de nuevo las imágenes?";
				}
				
				enviarMensajeTextoAlUsuario(userID, chatID, msj);
				
			}
			*/

		}

		@Override
		public String getBotToken() {
			String token = "530885872:AAHq86bNy6Yx08zpPuFXfTfBdE9YtlN90cI";
					
			return token;
		}
		
		
		public boolean guardarFotoEnServidor(List<PhotoSize> lFotos, Long idChat, Integer idUsuario, Integer fecha){
			boolean ok = true;
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
		
		
		public void devolverTodasLasImagenesDelUsuario(){
	        SendPhoto sendPhotoRequest = new SendPhoto();
	        
	        sendPhotoRequest.setChatId(chatID);
	        
	        try {
				Files.list(Paths.get("./galeria/" + userID)).forEach((imagen)->{
					sendPhotoRequest.setNewPhoto(new java.io.File(imagen.toString()));
			        try {
			            // Execute the method
			            sendPhoto(sendPhotoRequest);
			        } catch (TelegramApiException e) {
			            e.printStackTrace();
			        }
			        
				});
				enviarMensajeTextoAlUsuario("Estas son todas las imágenes que tengo de ti :)");

			} catch (IOException e) {
				e.printStackTrace();
				enviarMensajeTextoAlUsuario("No tengo ninguna foto tuya :( ... Envíame una foto :)");
			}	
		}
		
		/**
		 * Esta función se encarga de mandar mensajes de texto a la conversación del
		 * usuario
		 * @param userID Identificador de Usuario
		 * @param chatID Identificador de Chat
		 * @param cadena Mensaje que se quiere transmitir del Bot al usuario
		 */
		public void enviarMensajeTextoAlUsuario(String cadena){
			SendMessage message = new SendMessage().setChatId(chatID);
			
			message.setText(cadena);
			
			try {
				execute(message);
			} catch (TelegramApiException e) {
				e.printStackTrace();
			}
		}
		
		public String getMensaje(){
			return this.update.getMessage().getText();
		}
		
		public void mensajeLeido(){
			this.update = null;
		}
		
		public boolean hayMensaje(){
			return this.update != null && this.update.getMessage().hasText();
		}
		
		public boolean hayMensajeFoto(){
			return this.update != null && this.update.getMessage().hasPhoto();
		}
	}

}