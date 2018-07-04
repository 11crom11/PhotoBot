package photoBot.Agentes.Comportamiento;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import org.joda.time.DateTime;
import org.telegram.telegrambots.api.methods.send.SendAudio;

import gate.util.GateException;
import photoBot.Agentes.AgenteConversacionUsuario;
import photoBot.Agentes.AgenteConversacionUsuario.PhotoBot;
import photoBot.Drools.ProcesadorDeReglas;
import photoBot.Drools.Reglas.ConclusionReglas;
import photoBot.Gate.Etiqueta;
import photoBot.Gate.ProcesadorLenguaje;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class ComportamientoAgenteConversacionUsuario extends FSMBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PhotoBot photoBot;
	private Behaviour self;
	private ProcesadorDeReglas procReglas;
	private ProcesadorLenguaje procLenguaje;
	
	public ComportamientoAgenteConversacionUsuario(Agent a, PhotoBot pB) {
		super(a);
		this.photoBot = pB;
		this.self = this;
		
        try {
			this.procLenguaje = new ProcesadorLenguaje();
		} catch (GateException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        this.procReglas = new ProcesadorDeReglas();
		
		registerFirstState(new OneShotBehaviour() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			private int estado = 0;

			@Override
			public void action() {
				if(photoBot.getUserID() != null){ //Si hay usuario hablando al bot
					
					/*String saludo = saludoRespectoHora();
					
					photoBot.enviarMensajeTextoAlUsuario(saludo + ", ¿en qué puedo ayudarte?");
					*/
					
					ConclusionReglas c;
					
					try {
						
						List<Etiqueta> l = procLenguaje.analizarTextoGate(photoBot.getMensaje());
						
						c = procReglas.ejecutarReglasEtiquetas(l);
						
						if(c.existeTipo("Saludo")) {
							String saludo = saludoRespectoHora();
							photoBot.enviarMensajeTextoAlUsuario(saludo + ", ¿en qué puedo ayudarte?");
						}
					
					
					} catch (GateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					this.estado = 4;
				}
				
			}
			
			@Override
			public int onEnd() {
				return this.estado;
			}
			
		}, "ESTADO_INICIAL");
		
		
		registerState(new OneShotBehaviour() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			private int estado = 1;

			@Override
			public void action() {
				
				//System.out.println("Entro en el estado ESPERANDO_PETICION con la variable estado igual a " + String.valueOf(estado));
				this.estado = 1;
						
				//############SI QUIERO BUSCAR FOTOS
				if(photoBot.hayMensaje() && photoBot.getMensaje().equalsIgnoreCase("/dame")){
					this.estado = 5;
				}
				//############SI QUIERO CARGAR/SUBIR FOTOS
				else if(photoBot.hayMensajeFoto()){
					this.estado = 6;
				}	
			}
			
			@Override
			public int onEnd() {
				return estado;
			}
			
		}, "ESTADO_ESPERANDO_PETICICION");
		
		registerState(new OneShotBehaviour() {
	
			private static final long serialVersionUID = 1L;
			private int estado = 2;
	
			
			@Override
			public void action() {			
				//System.out.println("Entro en el estado BUSCAR con la variable estado igual a " + String.valueOf(estado));
				this.estado = 2;
				
				ACLMessage msj = new ACLMessage(ACLMessage.INFORM);
				msj.addReceiver(new AID("AgenteBuscarImagen", AID.ISLOCALNAME));
				msj.setContent(photoBot.getUserID().toString());
				//ID usuario
				
				self.getAgent().send(msj);
				
				ACLMessage respuesta = self.getAgent().receive();
				
				while(respuesta == null) {
					respuesta = self.getAgent().receive();
				}
				
				try {
					List<String> list = (List<String>) respuesta.getContentObject();
					
					photoBot.devolverTodasLasImagenesDelUsuario(list);
					photoBot.mensajeLeido();
					
				} catch (UnreadableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				

				
				//SI HE FINALIZADO BUSQUEDA, VUELVO AL ESTADO ESPERANDO_PETICION
				this.estado = 7;
			}
			
			@Override
			public int onEnd() {
				return estado;
			}
		}, "ESTADO_BUSCAR");
		
		registerState(new OneShotBehaviour() {

			private static final long serialVersionUID = 1L;
			private int estado = 3;

			@Override
			public void action() {
				//System.out.println("Entro en el estado SUBIR_FOTO con la variable estado igual a " + String.valueOf(estado));
				this.estado = 3;
				
				HashMap<String, Object> msjContent = new HashMap<String, Object>();
				
				List<File> lFotos = photoBot.obtenerImagenesMensaje();
				String userID = photoBot.getUserID().toString();
				
				msjContent.put("ID", userID);
				msjContent.put("LISTA", lFotos);
								
				ACLMessage msj = new ACLMessage(ACLMessage.INFORM);
				msj.addReceiver(new AID("AgenteAlmacenarImagenes", AID.ISLOCALNAME));
				
				try {
					msj.setContentObject((Serializable)msjContent);
					getAgent().send(msj);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				ACLMessage respuesta = self.getAgent().receive();
				
				while(respuesta == null) {
					respuesta = self.getAgent().receive();
				}
				
				String ret = "";
				
				if(respuesta.getContent().equals("OK")){
					ret = "Ya he recibido y almacenado correctamente tus imágenes.";
				}
				else{
					ret = "Ha ocurrido un error :( ... ¿Puedes mandarme de nuevo las imágenes?";
				}
				
				photoBot.enviarMensajeTextoAlUsuario(ret);
				photoBot.mensajeLeido();
				
				
				
				//SI HE FINALIZADO SUBIR IMAGENES, VUELVO AL PRINCIPIO
				this.estado = 8;
				
			}
			
			@Override
			public int onEnd() {
				return estado;
			}
			
		}, "ESTADO_SUBIR");
		
		registerLastState(new OneShotBehaviour() {

			private static final long serialVersionUID = 1L;
			

			@Override
			public void action() {
				//estado final				
			}
			
		}, "ESTADO_FINAL");
		
		//registerDefaultTransition("ESTADO_INICIAL", "ESTADO_ESPERANDO_PETICICION");
		registerTransition("ESTADO_INICIAL", "ESTADO_INICIAL", 0);
		registerTransition("ESTADO_ESPERANDO_PETICICION", "ESTADO_ESPERANDO_PETICICION", 1); //SE QUEDA AQUI HASTA QUE RECIBA MENSAJE
		registerTransition("ESTADO_BUSCAR", "ESTADO_BUSCAR", 2); //VUELTA ESTADO INICIAL
		registerTransition("ESTADO_SUBIR", "ESTADO_SUBIR", 3); //VUELTA ESTADO INICIAL

		
		registerTransition("ESTADO_INICIAL", "ESTADO_ESPERANDO_PETICICION", 4);
		registerTransition("ESTADO_ESPERANDO_PETICICION", "ESTADO_BUSCAR", 5); //SI EL USUARIO QUIERE BUSCAR IMAGENES
		registerTransition("ESTADO_ESPERANDO_PETICICION", "ESTADO_SUBIR", 6); //SI EL USUARIO QUIERE SUBIR IMAGENES
		registerTransition("ESTADO_BUSCAR", "ESTADO_ESPERANDO_PETICICION", 7); //VUELTA ESTADO INICIAL
		registerTransition("ESTADO_SUBIR", "ESTADO_ESPERANDO_PETICICION", 8); //VUELTA ESTADO INICIAL
		

	}
	
	@Override
	public int onEnd() {
		System.out.println("FSM behaviour completed.");
		myAgent.doDelete();
		return super.onEnd();
	}
	
	public Behaviour getEstado() {
		return getCurrent();
	}
	
	
	/**
	 * Esta funcion devuelve un saludo dependiendo de la hora recibida como parametro
	 * @param hora El numero de la hora (0-23)
	 * @return String con un saludo
	 */
	private String saludoRespectoHora(){
		String ret = "";
			
		DateTime dt = new DateTime();
		int hora = dt.getHourOfDay();
		
				
		if(hora >= 21 || (hora >= 0 && hora < 7)){
			ret = "Buenas noches";
		}
		else if(hora >= 7 && hora < 12){
			ret = "Buenos días";
		}
		else if(hora >= 12 && hora < 21){
			ret = "Buenas tardes";
		}
		
		return ret;
	}
}
