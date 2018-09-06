package photoBot.Agentes.Comportamiento;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import photoBot.BBDD.PhotoBotBBDD;
import photoBot.Imagen.Imagen;

/**
 * Esta clase implementa el comportamiento del agente Almacenar Imagen
 *
 */
public class ComportamientoAgenteAlmacenarImagen extends CyclicBehaviour {
	
	private Behaviour self;
	private PhotoBotBBDD bd;
	
	/**
	 * Constructor del agente Almacenar Imagen
	 * @param a Agente Almacenar Imagen
	 */
	public ComportamientoAgenteAlmacenarImagen(Agent a) {
		super(a);
		this.self = this;
		
		this.bd = new PhotoBotBBDD();
	}

	@Override
	public void action() {
		ACLMessage msj = this.self.getAgent().receive();

		if(msj != null){

			HashMap<String, Object> msjContent;
			int comando;	


			try {
				msjContent = (HashMap<String, Object>) msj.getContentObject();
				comando = (int) msjContent.get("COMANDO");

				if (comando == ConstantesComportamiento.ENVIAR_IMG_AGENTE_ALMACENAR){
					this.almacernarFicheroImagen(msjContent);
				}
				else if (comando == ConstantesComportamiento.ACTUALIZAR_IMAGEN_BBDD) {
					this.actualizarImagenBBDD(msjContent);
				}
				
			} catch (UnreadableException e) {
				e.printStackTrace();
			}

		}
		
	}
	
	/**
	 * Este método almacena la imagen recibida por el chat de Telegram en la carpeta galeria.
	 *  Este metodo es llamado a traves de un mensaje entre agentes
	 * @param msjContent Contenido del mensaje Agente
	 */
	private void almacernarFicheroImagen(HashMap<String, Object> msjContent) {

		String userID = (String) msjContent.get("ID");
		List<File> lFotos = (List<File>) msjContent.get("LISTA");
		
		java.io.File dir = new java.io.File("./galeria/" + userID);
		dir.mkdirs();
		
		for(int i = 0; i < lFotos.size(); i++){
			File imagen = lFotos.get(i);
			
			try {
				long fecha = new Timestamp(System.currentTimeMillis()).getTime();
				String url_img = dir.getPath() + "/" + fecha + ".jpeg";
				
				ImageIO.write(ImageIO.read(imagen), "jpeg", new java.io.File(url_img));
				System.out.println("He almacenado la imagen: " + fecha + ".jpeg");
				
				msjContent = new HashMap<String, Object>();
				
				msjContent.put("COMANDO", ConstantesComportamiento.RECONOCER_CARAS);
				msjContent.put("ID", userID);
				msjContent.put("URL_IMAGEN", url_img);
				msjContent.put("FECHA_SUBIDA", fecha);
								
				ACLMessage msj = new ACLMessage(ACLMessage.INFORM);
				msj.addReceiver(new AID(ConstantesComportamiento.AGENTE_GESTIONAR_CARAS, AID.ISLOCALNAME));
				
				try {
					msj.setContentObject((Serializable)msjContent);
					getAgent().send(msj);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				
				
			} catch (IOException e) {
				e.printStackTrace();
			}

		}	
	}
	
	/**
	 * Este metodo actualiza la informacion de una imagen en la base de datos. 
	 * Este metodo es llamado a travez de un mensaje entre agentes
	 * @param msjContent
	 */
	private void actualizarImagenBBDD(HashMap<String, Object> msjContent) {
		Integer userID = (Integer) msjContent.get("ID");
		Imagen i = (Imagen) msjContent.get("IMAGEN");
		
		this.bd.actualizarInfoImagen(i);
	}
}
