package photoBot.Agentes.Comportamiento;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.tuple.Triple;
import org.joda.time.DateTime;

import gate.util.GateException;
import photoBot.Agentes.AgenteConversacionUsuario.PhotoBot;
import photoBot.BBDD.PhotoBotBBDD;
import photoBot.Drools.ProcesadorDeReglas;
import photoBot.Drools.Reglas.Conversacion;
import photoBot.Gate.Etiqueta;
import photoBot.Gate.ProcesadorLenguaje;
import photoBot.Imagen.Evento;
import photoBot.Imagen.Imagen;
import photoBot.Imagen.Persona;
import photoBot.Imagen.Usuario;
import photoBot.OpenCV.CarasDetectadas;
import photoBot.OpenCV.GestorDeCaras;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class ComportamientoAgenteConversacionUsuario extends CyclicBehaviour {

	private static final long serialVersionUID = 1L;
	private PhotoBot photoBot;
	private ComportamientoAgenteConversacionUsuario self;
	private ProcesadorDeReglas procReglas;
	private ProcesadorLenguaje procLenguaje;
	private GestorDeCaras gestorCaras;
	
	private Conversacion conversacion;
	private PhotoBotBBDD bd;
	

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
        
        this.gestorCaras = new GestorDeCaras();
        
        this.conversacion = new Conversacion();
        
        this.bd = new PhotoBotBBDD();
	}
	
	@Override
	public void action() {
		// TODO Auto-generated method stub
		
		HashMap<String, Object> msjContent;

		ACLMessage msj = this.self.getAgent().receive();
		
		
		//Si hay un mensaje por parte de un agente
		if(msj != null){
			
			try {
				msjContent = (HashMap<String, Object>) msj.getContentObject();
				
				int comando = (int) msjContent.get("COMANDO");
				
				if(comando == ConstantesComportamiento.ENTREGAR_IMG_ENCONTRADAS) {
					obtenerImagenesBusquedaAgente(msj, msjContent);
				}
				else if(comando ==  ConstantesComportamiento.RESULTADO_RECONOCIMIENTO_IMAGEN) {
					recibirRespuestaSubidaImagenes(msj, msjContent);
				}
				else if(comando == -1) {
					//........................
				}
				
			} catch (UnreadableException e) {
				e.printStackTrace();
			}				
		}
		
		//Si hay un mensaje por parte de usuario                                                
		if(photoBot.hayMensaje()) {
			
			//0- esta registrado en la base datos el usuario?
			this.conversacion.setUsuarioRegistrado(bd.existeUsuario(photoBot.getUserID()));
			
			//1- Recibir mensaje
			String mensaje = photoBot.getMensaje();

			try {
				
			//2- Analizar el texto
				if(photoBot.hayMensajeTexto()) {
					List<Etiqueta> lEtiquetas = procLenguaje.analizarTextoGate(mensaje);
					
			//3A- Ejecutar reglas que haran acciones sobre los agentes y bot
					this.conversacion = procReglas.ejecutarReglasEtiquetas(lEtiquetas, this.conversacion, self);
				}
			//3B- Procesar imagen recibida	
				else if(photoBot.hayMensajeFoto()) {
					bot_solicitudSubirImagenes();
					
				}
				
			} catch (GateException e) {
				e.printStackTrace();
			}
			
			//4- Marcar mensaje como leido
			photoBot.mensajeLeido();
		}
	}
	
	public void bot_saludar() {
		photoBot.enviarMensajeTextoAlUsuario(saludoRespectoHora() + ", ¿qué tal va todo?");
	}
	
	public void bot_solicitudBuscarTodasImagenes() {
		HashMap<String, Object> msjContent = new HashMap<String, Object>();
		
		msjContent.put("COMANDO", ConstantesComportamiento.SOLICITAR_IMG_AGENTE);
		msjContent.put("ID", photoBot.getUserID().toString());
		
		ACLMessage msj = new ACLMessage(ACLMessage.INFORM);
		self.getAgent().send(msj);
	}
	
	private void obtenerImagenesBusquedaAgente(ACLMessage msj, HashMap<String, Object> contenido) {
		List<String> list = (List<String>) contenido.get("LISTA");
		
		photoBot.enviarImagenes(list);

	}
	
	public void esperarFotoConversacion() {
		this.conversacion.solicitudSubirFotoRecibida();
		
		photoBot.enviarMensajeTextoAlUsuario("Muy bien, enviame la fotografía que quieres que almacene!");

	}
	
	public void bot_anularSubidaFotos() {
		this.conversacion.solicitudSubirFotoFinalizada();
		
		photoBot.enviarMensajeTextoAlUsuario("Parece ser que has cambiado de idea respecto a subir una nueva imagen. Lo dejaremos para más tarde.");
	}
	
	public void bot_solicitudSubirImagenes() {
		HashMap<String, Object> msjContent = new HashMap<String, Object>();
		
		List<File> lFotos = photoBot.obtenerImagenesMensaje();
		String userID = photoBot.getUserID().toString();
		
		msjContent.put("COMANDO", ConstantesComportamiento.ENVIAR_IMG_AGENTE);
		msjContent.put("ID", userID);
		msjContent.put("LISTA", lFotos);
						
		ACLMessage msj = new ACLMessage(ACLMessage.INFORM);
		msj.addReceiver(new AID(ConstantesComportamiento.AGENTE_ALMACENAR_IMAGEN, AID.ISLOCALNAME));
		
		try {
			msj.setContentObject((Serializable)msjContent);
			getAgent().send(msj);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void bot_pedirDatosUsuarioNuevo(){
		this.conversacion.setEsperarDatosDelUsuario(true);
		
		photoBot.enviarMensajeTextoAlUsuario("Hola! Encantado de conocerte! Me llamo PhotoBot!");
		photoBot.enviarMensajeTextoAlUsuario("Parece que es la primera vez que nos vemos, por favor, ¿podrías decirme tu nombre y tu edad?");
	}
	
	public void bot_registrarUsuarioNuevo(Etiqueta nombre, Etiqueta edad){
		boolean ok = this.bd.crearUsuario(new Usuario(photoBot.getUserID(), nombre.getNombre(), Integer.valueOf(edad.getNombre())));
		
		if(ok){
			photoBot.enviarMensajeTextoAlUsuario("Encantado " + nombre.getNombre() + "!");
			this.conversacion.registradaInfoDelUsuario();
		}
		else{
			photoBot.enviarMensajeTextoAlUsuario("Ups!!! Parece que ha ocurrido un error, ¿por favor, puedes repetirme tu nombre?");
		}
	}
	
	private void recibirRespuestaSubidaImagenes(ACLMessage msj, HashMap<String, Object> contenido) {
		String urlImagen = (String) contenido.get("URL_IMAGEN");
		long fechaSubida = (long) contenido.get("FECHA_SUBIDA");
		
		List<Triple<String, Integer, Double>> tripletaColorEtiquetaProbabilidad = (List<Triple<String, Integer, Double>>) contenido.get("RESULTADO_RECONOCIMIENTO");

		//Enviamos la foto recuadrada al usuario
		List<String> list = new ArrayList<String>();
		list.add(FilenameUtils.getPath(urlImagen) + FilenameUtils.getBaseName(urlImagen) + "_rec.jpeg");
		
		this.photoBot.enviarImagenes(list);
		
		String color;
		Integer persona;
		Double probabilidad;
		String mensajeTexto;
		
		List<String> listMensaje = new ArrayList<>();
		
		for (Triple<String, Integer, Double> triple : tripletaColorEtiquetaProbabilidad) {
			
			color = triple.getLeft();
			persona = triple.getMiddle();
			probabilidad = triple.getRight();
			
			//SEGUIR AQUIIIIIIIIIIIIIIII
			if (probabilidad <= 50.0){
				//listaMensaje.add("El de color: " + triple.getLeft() + " es: " + triple.getMiddle() + " con un porcentaje del: " + triple.getRight());
			}
			
			photoBot.enviarMensajeTextoAlUsuario("El de color: " + triple.getLeft() + " es: " + triple.getMiddle() + " con un porcentaje del: " + triple.getRight());
		}
		
		//obtener datos de la foto
		Imagen im = new Imagen(new Date(fechaSubida), new ArrayList<Persona>(),new ArrayList<Evento>(), this.photoBot.getUserID(), urlImagen);

		this.bd.registrarImagen(im);
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
