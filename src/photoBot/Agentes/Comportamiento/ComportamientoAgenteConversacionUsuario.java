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
import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.opencv.core.MatOfRect;

import gate.util.GateException;
import photoBot.Agentes.AgenteConversacionUsuario.PhotoBot;
import photoBot.BBDD.PhotoBotBBDD;
import photoBot.Drools.ProcesadorDeReglas;
import photoBot.Drools.Reglas.Conversacion;
import photoBot.Gate.Etiqueta;
import photoBot.Gate.ProcesadorLenguaje;
import photoBot.Imagen.Imagen;
import photoBot.Imagen.Persona;
import photoBot.Imagen.Usuario;
import photoBot.OpenCV.CarasDetectadas;
import photoBot.OpenCV.GestorDeCaras;
import photoBot.Utilidades.SerializadorObjeto;
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
			Usuario usu = bd.existeUsuario(photoBot.getUser());
			
			if(usu == null){
				this.conversacion.setUsuarioRegistrado(false);
			}
			else{
				this.conversacion.setUsuarioRegistrado(true);
				this.photoBot.setUser(usu);
			}
			
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
		msjContent.put("ID", photoBot.getUser().getIdUsuarioTelegram().toString());
		
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
		String userID = photoBot.getUser().getIdUsuarioTelegram().toString();
		
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
		this.conversacion.saludoRecibido();
		
		photoBot.enviarMensajeTextoAlUsuario("Hola! Encantado de conocerte! Me llamo PhotoBot!");
		photoBot.enviarMensajeTextoAlUsuario("Parece que es la primera vez que nos vemos, por favor, ¿podrías decirme tu nombre y tu edad?");
	}
	
	public void bot_registrarUsuarioNuevo(Etiqueta nombre, Etiqueta edad){
		
		Usuario usuario = new Usuario(photoBot.getUser().getIdUsuarioTelegram(), nombre.getNombre(), Integer.valueOf(edad.getNombre()), 0);
		
		boolean ok = this.bd.crearUsuario(usuario);
		
		this.photoBot.setUser(usuario);
		
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
		String mensajeUsuario = "";
		List<Triple<String, Integer, Double>> tripletaColorEtiquetaProbabilidad = (List<Triple<String, Integer, Double>>) contenido.get("RESULTADO_RECONOCIMIENTO");
		CarasDetectadas carasDetectadas = (CarasDetectadas) contenido.get("OBJETO_CARAS_DETECTADAS");
		//long k = (long) contenido.get("OBJETO_CARAS_DETECTADAS");
		//MatOfRect mor = MatOfRect.fromNativeAddr(k);
		//CarasDetectadas carasDetectadas = (CarasDetectadas) SerializadorObjeto.deserialize(k);
		
		carasDetectadas.actualizaMatOfRect();
		
		//volver a DESCOMENTARRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR
		this.conversacion.setCarasDetectadas(carasDetectadas);
		
		//Enviamos la foto recuadrada al usuario
		List<String> list = new ArrayList<String>();
		list.add(FilenameUtils.getPath(urlImagen) + FilenameUtils.getBaseName(urlImagen) + "_rec.jpeg");
		
		this.photoBot.enviarImagenes(list);
		
		String color;
		Integer persona;
		Double probabilidad;
		String mensajeTexto;
		String nombre;
		
		List<String> grupoDesconocido = new ArrayList<>();
		List<String> grupoMedioConocido = new ArrayList<>();
		List<String> grupoConocido = new ArrayList<>();

		for (Triple<String, Integer, Double> triple : tripletaColorEtiquetaProbabilidad) {
			
			color = triple.getLeft();
			persona = triple.getMiddle();
			
			nombre = this.bd.obtenerPersonaEtiqueta(this.photoBot.getUser(), persona.toString()).getNombre();
			
			probabilidad = triple.getRight();
			
			if (probabilidad <= 50.0){
				grupoDesconocido.add(triple.getLeft());
				this.conversacion.getCarasDetectadas().actualizarPersonaHashMap(color, -1, 0.0);
			}
			else if (probabilidad > 50.00){
				grupoConocido.add(nombre + " con el color " + triple.getLeft());
			}
			
		}
		
		if(!grupoDesconocido.isEmpty()){
			mensajeUsuario += "No he reconocido a las personas recuadradas con los siguientes colores, "
					+ "por favor, ¿podrías indicarme para cada color, de qué persona se trata?: " + String.join(", ", grupoDesconocido) + ".\n\n";
		}
		
		if(!grupoConocido.isEmpty()){
			mensajeUsuario += !grupoDesconocido.isEmpty() ? "Por otra parte, he reconocido a " + String.join(", a ", grupoConocido) + ".": "En la imagen he reconocido a  \n" + 
					String.join(", a ", grupoConocido) + ".";
		}

		
		photoBot.enviarMensajeTextoAlUsuario(mensajeUsuario);
		
		File f = new File(FilenameUtils.getPath(urlImagen) + FilenameUtils.getBaseName(urlImagen) + "_rec.jpeg");
		String m = f.delete() ? FilenameUtils.getPath(urlImagen) + FilenameUtils.getBaseName(urlImagen) + "_rec.jpeg HA SIDO ELIMINADO" : "La eliminación ha fallado";
		System.out.println(m);
		
		//obtener datos de la foto
		Imagen im = new Imagen(new Date(fechaSubida), new ArrayList<Persona>(),new ArrayList<String>(), this.photoBot.getUser(), urlImagen);

		this.bd.registrarImagen(im);
		this.conversacion.setEsperarDatosDelUsuario(true);
		this.conversacion.setImagenPeticionInfo(im);
}
	
	public void bot_actualizarInfoImagen(Imagen imagen, Etiqueta etiqueta){
		String color = etiqueta.getColor();
		String nombrePersona = etiqueta.getNombre();
		Persona p = this.bd.obtenerPersonaEtiqueta(this.photoBot.getUser(), nombrePersona);
		int etiquetaClasificador = p.getEtiqueta();
		
		
		if(etiquetaClasificador == this.photoBot.getUser().getEtiquetaMaxUsada() + 1) {
			this.bd.registrarPersonaUsuario(p);
			this.photoBot.getUser().setEtiquetaMaxUsada(etiquetaClasificador);
		}
		
		imagen.addPersonaImagen(p);
		
		//ACTUALIZAR BBDD Y EL OBJETO CARASDETECTADAS
		this.conversacion.getCarasDetectadas().actualizarPersonaHashMap(color, etiquetaClasificador, 100.0);
		this.bd.actualizarInfoImagen(imagen);
		
		//ACTUALIZAR EL ATRIBUTO ETIQUETAMAXUTILIZADA DEL USUARIO
		this.photoBot.setUser(this.photoBot.getUser());
		this.bd.actualizarInfoUsuario(this.photoBot.getUser());
		
		this.procReglas.ejecutarReglas(this.conversacion, this.self);
	}

	public void bot_actualizarClasificador(){
		System.out.println("Estoy actualizando el clasificador");
		
		this.gestorCaras.actualizarClasificadorPersonalizado(this.photoBot.getUser().getIdUsuarioTelegram().toString(), this.conversacion.getCarasDetectadas());
		//this.conversacion.setCarasDetectadas(null);
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
