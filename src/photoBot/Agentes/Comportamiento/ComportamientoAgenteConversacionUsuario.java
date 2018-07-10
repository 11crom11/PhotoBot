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
import photoBot.Drools.Reglas.Conversacion;
import photoBot.Gate.Etiqueta;
import photoBot.Gate.ProcesadorLenguaje;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class ComportamientoAgenteConversacionUsuario extends CyclicBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PhotoBot photoBot;
	private ComportamientoAgenteConversacionUsuario self;
	private ProcesadorDeReglas procReglas;
	private ProcesadorLenguaje procLenguaje;
	
	private Conversacion conversacion;
	
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
        
        this.conversacion = new Conversacion();
	}
	
	@Override
	public void action() {
		// TODO Auto-generated method stub
		if(photoBot.hayMensaje()) {
			
			//1- Recibir mensaje
			String mensaje = photoBot.getMensaje();

			try {
				
			//2- Analizar el texto
				if(photoBot.hayMensajeTexto()) {
					List<Etiqueta> lEtiquetas = procLenguaje.analizarTextoGate(mensaje);
					
			//3- Ejecutar reglas que haran acciones sobre los agentes y bot
					this.conversacion = procReglas.ejecutarReglasEtiquetas(lEtiquetas, this.conversacion, self);
				}
				
			
			} catch (GateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//4- Marcar mensaje como leido
			photoBot.mensajeLeido();
		}
	}
	
	public void bot_saludar() {
		photoBot.enviarMensajeTextoAlUsuario(saludoRespectoHora() + ", ¿qué tal va todo?");
	}
	
	public void bot_buscarTodasImagenes() {
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
			photoBot.enviarMensajeTextoAlUsuario("Estas son todas las imágenes que tengo de ti :)");

			
		} catch (UnreadableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void esperarFotoConversacion() {
		this.conversacion.solicitudSubirFotoRecibida();
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
