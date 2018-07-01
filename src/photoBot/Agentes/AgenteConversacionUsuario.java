package photoBot.Agentes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

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
import photoBot.Agentes.Comportamiento.ComportamientoAgenteConversacionUsuario;

public class AgenteConversacionUsuario extends Agent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
			//Behaviour estado = comportamiento.getEstado();
			//String estadoActualString = estado.getBehaviourName();
			

			/**		
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
			*/

		}

		@Override
		public String getBotToken() {
			String token = "530885872:AAHq86bNy6Yx08zpPuFXfTfBdE9YtlN90cI";
					
			return token;
		}
		
		
		/**
		 * Esta funcion recoge la imagen recibido por el chat y la almacena en la carpeta 
		 * del usuario correspondiente (su userID).
		 * @return Devuelve True en caso de que la imagen haya sido almacenado correctamente
		 */
		public boolean guardarFotoEnServidor(){
			boolean ok = true;
			
			List<PhotoSize> lFotos = update.getMessage().getPhoto();
			PhotoSize ps = lFotos.stream()
					.sorted(Comparator.comparing(PhotoSize::getFileSize).reversed())
					.findFirst()
					.orElse(null);

			GetFile getFileMethod = new GetFile();
			getFileMethod.setFileId(ps.getFileId());

			try {
				File file = execute(getFileMethod);
				java.io.File fileJava = downloadFile(file.getFilePath());           
				
				//bImg = ImageIO.read(fileJava);
				
				java.io.File dir = new java.io.File("./galeria/" + this.userID);
				dir.mkdirs();
				ImageIO.write(ImageIO.read(fileJava), "jpeg", new java.io.File(dir.getPath() + "/" + this.date + ".jpeg"));
				
			} catch (TelegramApiException e) {
				e.printStackTrace();
				ok = false;
			} catch (IOException e) {
				e.printStackTrace();
				ok = false;
			}
			
			return ok;
		}
		
		
		/**
		 * Esta funci�n devuelve todas las imagenes contenidas en la carpeta de un usuario
		 * concreto a partir del userID de la conversaci�n concreta
		 */
		public void devolverTodasLasImagenesDelUsuario(List<String> listaDeImagenes){
	        SendPhoto sendPhotoRequest = new SendPhoto();
	        
	        sendPhotoRequest.setChatId(chatID);
	        
	        	for (String imagen : listaDeImagenes) {
					sendPhotoRequest.setNewPhoto(new java.io.File(imagen));
		            try {
						sendPhoto(sendPhotoRequest);
					} catch (TelegramApiException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}


				}
			       
				enviarMensajeTextoAlUsuario("Estas son todas las imágenes que tengo de ti :)");


		}
		
		/**
		 * Esta funci�n se encarga de mandar mensajes de texto a la conversaci�n del
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
		
		public int getFecha(){
			if(this.date != null){
				return this.date;
			}
			else{
				LocalTime now = LocalTime.now();
				return now.getHour();
			}
		}
	}

}