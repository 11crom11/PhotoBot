package photoBot.Agentes;

import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
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
import org.telegram.telegrambots.generics.BotSession;

import jade.core.Agent;
import photoBot.Agentes.Comportamiento.ComportamientoAgenteConversacionUsuario;
import photoBot.Imagen.Usuario;

/**
 * Agente JADE cuya funcion es analizar la conversacion que se mantiene con el usuario para
 * dirigir el funcionamiento de la aplicacion.
 *
 */
public class AgenteConversacionUsuario extends Agent {
	
	private static final long serialVersionUID = 1L;
	private PhotoBot photoBot;
	private TelegramBotsApi apiTelegram;
	private static BotSession botSession;
		
	private ComportamientoAgenteConversacionUsuario comportamiento;
	
	@Override
	protected void setup() {
		super.setup();
		
        ApiContextInitializer.init();

        this.apiTelegram = new TelegramBotsApi();
        this.photoBot = new PhotoBot();
        this.comportamiento = new ComportamientoAgenteConversacionUsuario(this, photoBot);
        
        try {
            this.botSession = this.apiTelegram.registerBot(this.photoBot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        
		addBehaviour(this.comportamiento);
	}
	
	@Override
	protected void takeDown(){		
		System.out.println("PARADA DE AGENTE CONVERSACION USUARIO");
	}
	
	/**
	 * Este metodo finaliza la sesion del bot de Telegram.
	 */
	public static void pararBotTelegram() {
		botSession.stop();
		System.out.println("BOT TELEGRAM PHOTOBOT PARADO CORRECTAMENTE");
	}
	
	/**
	 * Clase que controla el funcionamiento del chat de Telegram
	 *
	 */
	/**
	 * @author d_dan
	 *
	 */
	public class PhotoBot extends TelegramLongPollingBot {

		private Long chatID;
		private Usuario user;
		private Integer date;
		private Update update;

		/**
		 * Este getter devuelve un objeto Usuario que contiene solamente el ID de telegram relleno
		 * @return Usuario con el que se está conversando
		 */
		public Usuario getUser(){
			return user;
		}
		
		/**
		 * @param Usuario al que asignar a la conversacion de Telegram
		 */
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
		 * @param listaImagenes lista con las ubicaciones de las imagenes que se desea enviar al chat del usuario activo
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
		
		/**
		 * Este metodo obtiene las imagenes que el usuario envia al bot por el chat de Telegram
		 * @return Lista de archivos (imagenes) que el bot recibe via Telegram
		 */
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
		
		
		
		/**
		 * @return Texto del ultimo mensaje que se ha recibido de la conversacion de Telegram
		 */
		public String getMensaje(){
			return this.update.getMessage().getText();
		}
		
		/**
		 * Cuando un mensaja es leido se actualiza el atributo update.
		 */
		public void mensajeLeido(){
			this.update = null;
		}
		
		/**
		 * Este metodo comprueba si se ha recibido un mensaje que esta sin leer
		 * @return True cuando hay un nuevo mensaje sin leer, False cuando no hay mensaje
		 */
		public boolean hayMensaje() {
			return this.update != null;
		}
		
		/**
		 * Este metodo comprueba si hay un mensaje de texto sin leer
		 * @return True si hay un mensaje sin leer y es de tipo texto, False si no hay mensaje de texto
		 */
		public boolean hayMensajeTexto(){
			return this.update != null && this.update.getMessage().hasText();
		}
		
		/**
		 * Este metodo comprueba si hay una imagen sin ver
		 * @return True si se ha recibido un mensaje de tipo fotografia, False si no hay mensaje
		 */
		public boolean hayMensajeFoto(){
			return this.update != null && this.update.getMessage().hasPhoto();
		}
		
		/**
		 * @return Fecha del ultimo mensaje recibido
		 */
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
	
	/**
	 * Este metodo devuelve el comportamiento del agente Conversacion Usuario
	 * @return Comportamiento del agente Conversacion Usuario
	 */
	public ComportamientoAgenteConversacionUsuario getComportamiento() {
		return this.comportamiento;
	}

}