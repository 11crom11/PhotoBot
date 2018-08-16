package photoBot.Agentes;

import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
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
import photoBot.BBDD.PhotoBotBBDD;
import photoBot.Imagen.Usuario;

public class AgenteConversacionUsuario extends Agent {
	
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
		private Usuario user;
		private Integer date;
		private Update update;

		public Usuario getUser(){
			return user;
		}
		
		public void setUser(Usuario user) {
			this.user = user;
		}
		
		@Override
		public String getBotUsername() {
			String nombreBot = "PhotosDASI_bot";
			return nombreBot;
		}

		@Override
		public void onUpdateReceived(Update update) {
			
			this.chatID = update.getMessage().getChatId();
			
			//Consultamos si existe el usuario, 
			this.user = new Usuario(update.getMessage().getFrom().getId());
			
			//this.userID = update.getMessage().getFrom().getId();
			
			this.date = update.getMessage().getDate();
			this.update = update;
			
			
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
				
				java.io.File dir = new java.io.File("./galeria/" + this.user.getIdUsuarioTelegram());
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
		 * Esta función devuelve todas las imagenes contenidas en la carpeta de un usuario
		 * concreto a partir del userID de la conversación concreta
		 */
		public void enviarImagenes(List<String> listaDeImagenes){
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
		
		public List<java.io.File> obtenerImagenesMensaje(){
			List<java.io.File> lFotosRet = new ArrayList<java.io.File>();			
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
				lFotosRet.add(fileJava);
				
			} catch (TelegramApiException e) {
				e.printStackTrace();
			}
			
			return lFotosRet;
		}
		
		
		
		public String getMensaje(){
			return this.update.getMessage().getText();
		}
		
		public void mensajeLeido(){
			this.update = null;
		}
		
		public boolean hayMensaje() {
			return this.update != null;
		}
		
		public boolean hayMensajeTexto(){
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
	
	public ComportamientoAgenteConversacionUsuario getComportamiento() {
		return this.comportamiento;
	}

}