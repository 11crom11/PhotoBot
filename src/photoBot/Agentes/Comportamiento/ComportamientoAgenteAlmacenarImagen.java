package photoBot.Agentes.Comportamiento;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class ComportamientoAgenteAlmacenarImagen extends FSMBehaviour {
	
	private Behaviour self;
	
	private List<File> lFotos;
	private String userID;
	
	public ComportamientoAgenteAlmacenarImagen(Agent a) {
		super(a);
		this.self = this;
		
		registerFirstState(new OneShotBehaviour() {

			private static final long serialVersionUID = 1L;
			private int estado = 0;

			@Override
			public void action() {
				this.estado = 0;
				
			}
			
			@Override
			public int onEnd() {
				return this.estado;
			}
			
		}, "ESTADO_INICIAL");
		
		
		registerState(new OneShotBehaviour() {

			private static final long serialVersionUID = 1L;
			private int estado = 1;

			@Override
			public void action() {
				
				this.estado = 1;
				
				ACLMessage msj = self.getAgent().receive();
								
				if(msj != null){
					
					HashMap<String, Object> msjContent;
					try {
						
						msjContent = (HashMap<String, Object>) msj.getContentObject();
						userID = (String) msjContent.get("ID");
						lFotos = (List<File>) msjContent.get("LISTA");
						this.estado = 2;
						
					} catch (UnreadableException e) {
						
						this.estado = 1;
						e.printStackTrace();
					}
				}
				
			}
			
			@Override
			public int onEnd() {
				return estado;
			}
			
		}, "ESTADO_ESPERA_RECIBIR");
		
		registerState(new OneShotBehaviour() {

			private static final long serialVersionUID = 1L;
			private int estado = 3;

			@Override
			public void action() {
				this.estado = 3;
				
				java.io.File dir = new java.io.File("./galeria/" + userID);
				dir.mkdirs();
				
				for(int i = 0; i < lFotos.size(); i++){
					File imagen = lFotos.get(i);
					
					try {
						long fecha = new Timestamp(System.currentTimeMillis()).getTime();
						String url_img = dir.getPath() + "/" + fecha + ".jpeg";
						
						ImageIO.write(ImageIO.read(imagen), "jpeg", new java.io.File(url_img));
						
						HashMap<String, Object> msjContent = new HashMap<String, Object>();
						
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
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}	
				
	
				this.estado = 4;
			}
			
			@Override
			public int onEnd() {
				return estado;
			}
			
		}, "ESTADO_ALMACENAR_FOTO");
		
		registerLastState(new OneShotBehaviour() {

			private static final long serialVersionUID = 1L;
			

			@Override
			public void action() {
				//estado final				
			}
			
		}, "ESTADO_FINAL");
		
		registerTransition("ESTADO_INICIAL", "ESTADO_ESPERA_RECIBIR", 0);
		registerTransition("ESTADO_ESPERA_RECIBIR", "ESTADO_ESPERA_RECIBIR", 1);
		registerTransition("ESTADO_ESPERA_RECIBIR", "ESTADO_ALMACENAR_FOTO", 2);
		registerTransition("ESTADO_ALMACENAR_FOTO", "ESTADO_ALMACENAR_FOTO", 3);
		registerTransition("ESTADO_ALMACENAR_FOTO", "ESTADO_ESPERA_RECIBIR", 4);
	}
}
