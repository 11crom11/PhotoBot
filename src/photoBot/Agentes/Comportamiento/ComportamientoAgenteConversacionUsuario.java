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
import photoBot.Drools.Conversacion;
import photoBot.Drools.ProcesadorDeReglas;
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
				else if(comando == ConstantesComportamiento.CLASIFICADOR_USUARIO_ACTUALIZADO) {
					clasificadorUsuarioActualizado();
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
			//MOVIDO A UN METODO QUE ES EJECUTADO POR UNA REGLA DEL GRUPO EXISTENCIA_USUARIO
			/*Usuario usu = bd.existeUsuario(photoBot.getUser());
			
			if(usu == null){
				this.conversacion.setUsuarioRegistrado(false);
			}
			else{
				this.conversacion.setUsuarioRegistrado(true);
				this.photoBot.setUser(usu);
			}*/
			
			//this.comprobarExistenciayObtenerUsuario();
			
			//1- Recibir mensaje
			String mensaje = photoBot.getMensaje();
			List<Etiqueta> lEtiquetas = new ArrayList<>();

			try {
				
				//2A- Analizar el texto en caso de que sea um mensaje de texto
				if(photoBot.hayMensajeTexto()) {
					lEtiquetas = procLenguaje.analizarTextoGate(mensaje);
				}
				
				//2B- Indicar que se ha subido una imagen en el caso de que sea un mensaje con foto	
				else if(photoBot.hayMensajeFoto()) {					
					this.procReglas.seleccionarGrupoSubirImagenManejador();
					this.conversacion.fotosCargadasUsuario();
					//bot_solicitudSubirImagenes();					
				}
				//3- Ejecutar reglas que haran acciones sobre los agentes y bot
				this.conversacion = procReglas.ejecutarReglasEtiquetas(lEtiquetas, this.conversacion, self);
				
				
				
			} catch (GateException e) {
				e.printStackTrace();
			}
			
			//4- Marcar mensaje como leido
			photoBot.mensajeLeido();
		}
	}
	
	public void bot_saludar(boolean botSaludado) {
		
		
		if(botSaludado) {
			if(this.conversacion.isUsuarioRegistrado()) {
				photoBot.enviarMensajeTextoAlUsuario(saludoRespectoHora() + " " + this.photoBot.getUser().getNombre() + ", ¿qué tal va todo?");				
			}
			else {
				photoBot.enviarMensajeTextoAlUsuario(saludoRespectoHora() + ", veo que es la primera vez que hablamos.");

			}
		}
		else {
			if(this.conversacion.isUsuarioRegistrado()) {
				photoBot.enviarMensajeTextoAlUsuario(saludoRespectoHora() + " " + this.photoBot.getUser().getNombre() + ", es de mala educación no saludarme. Es broma, jajaja. ¿Qué puedo hacer por tí?");				
			}
			else {
				photoBot.enviarMensajeTextoAlUsuario(saludoRespectoHora() + ", creo que todavía no nos han presentado.");
			}
		}
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
			conversacion.fotoAlmacenada();
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void bot_pedirDatosUsuarioNuevo(){
		this.conversacion.setEsperarDatosDelUsuario(true);
		this.conversacion.saludoRecibido();
		
		photoBot.enviarMensajeTextoAlUsuario("Encantado de conocerte! Me llamo PhotoBot!");
		photoBot.enviarMensajeTextoAlUsuario("Soy un asistente virtual y mi misión es ayudarte a organizar todas tus fotografías."
				+ " Las imaganes que reciba por tu parte las almacenaré en mi buena memoria fotográfica e intentaré aprenderme el"
				+ " nombre de tus amigos para luego poder ayudarte a recuperar tus fotos de manera cómoda, sin que tengas que"
				+ " malgastar tu tiempo buscando entre miles de fotos.");
		photoBot.enviarMensajeTextoAlUsuario("Para empezar, por favor, ¿podrías decirme tu nombre?");
	}
	
	public void bot_registrarUsuarioNuevo(Etiqueta nombre){
		
		Usuario usuario = new Usuario(photoBot.getUser().getIdUsuarioTelegram(), nombre.getNombre(), 0);
		
		boolean ok = this.bd.crearUsuario(usuario);
		
		this.photoBot.setUser(usuario);
		
		if(ok){
			photoBot.enviarMensajeTextoAlUsuario("Encantado " + nombre.getNombre() + "!");
			photoBot.enviarMensajeTextoAlUsuario("A partir de aquí puedes enviarme las imágenes que quieras que almacene y yo"
					+ " te preguntare al principio por las personas que aparecen en ellas, para así aprenderme sus nombres."
					+ " Con el tiempo te prometo que me los aprenderé :P");
			photoBot.enviarMensajeTextoAlUsuario("También te preguntaré por el contexto de la imagen (boda, cumpleaños...) para"
					+ " facilitarte luego la búsqueda de la imagen.");
			photoBot.enviarMensajeTextoAlUsuario("Eso es todo lo que tengo que contarte a modo de tutorial, creo que ya podemos empezar.");
			photoBot.enviarMensajeTextoAlUsuario("Si vas a subir una foto pueder decirmelo o bien enviame directamente la imagen de una"
					+ " en una, por favor."
					+ " Por el contrario, si quieres buscar una foto solo tendrás pedirmelo con las personas que quieres que aparezcan"
					+ ", así como si también quieres filtrar por el contexto de la foto y yo haré el resto.");
			photoBot.enviarMensajeTextoAlUsuario("Muy bien, ahora si he terminado de contarte el rollo :P. Empecemos :)");
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

		this.conversacion.setPendienteActualizarClasificador(true);
		
		//Enviamos la foto recuadrada al usuario
		List<String> list = new ArrayList<String>();
		list.add(FilenameUtils.getPath(urlImagen) + FilenameUtils.getBaseName(urlImagen) + "_rec.jpeg");
		
		this.photoBot.enviarImagenes(list);
		
		String color;
		Integer persona;
		Double probabilidad;
		String nombre;
		
		List<String> grupoDesconocido = new ArrayList<>();
		List<String> grupoMedioConocido = new ArrayList<>();
		List<String> grupoConocido = new ArrayList<>();
		
		if(tripletaColorEtiquetaProbabilidad.isEmpty()) {
			photoBot.enviarMensajeTextoAlUsuario("No he conseguido reconocer a ninguna persona en esta fotografía."
					+ " En el caso de que me haya confundido, deberás volver a fotografíar el retrato."
					+ " Por otro lado, si no me he confundido y quieres guardar la imagen para luego recuperarla me tendrás que decir su contexto,"
					+ " por el contrario, solo podrás recuperarla indicandome la fecha de subida.");
		}
		else {
			for (Triple<String, Integer, Double> triple : tripletaColorEtiquetaProbabilidad) {
				
				color = triple.getLeft();
				persona = triple.getMiddle();
				
				nombre = this.bd.obtenerNombrePersona(this.photoBot.getUser(), persona);
				
				probabilidad = triple.getRight();
				
				if (probabilidad > 50.00 || probabilidad == 0.0){
					grupoConocido.add(nombre + " con el color " + triple.getLeft());
				}
				else if (probabilidad <= 50.0){
					grupoDesconocido.add(triple.getLeft());
					
					//this.conversacion.getCarasDetectadas().actualizarPersonaHashMap(color, -1, 0.0);
					HashMap<String, Object> msjContent = new HashMap<String, Object>();
					
					msjContent.put("COMANDO", ConstantesComportamiento.ACTUALIZAR_CAMPO_CARAS_DETECTADAS);
					msjContent.put("ID", this.photoBot.getUser().getIdUsuarioTelegram());
					msjContent.put("COLOR", color);
					msjContent.put("ETIQUETA", -1);
					msjContent.put("CONFIDENCE", 0.0);
					
					ACLMessage msjc = new ACLMessage(ACLMessage.INFORM);
					msjc.addReceiver(new AID(ConstantesComportamiento.AGENTE_GESTIONAR_CARAS, AID.ISLOCALNAME));
					
					try {
						msjc.setContentObject((Serializable)msjContent);
						getAgent().send(msjc);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
			}
			
			if(!grupoDesconocido.isEmpty()){
				mensajeUsuario += "No he reconocido a las personas recuadradas con los siguientes colores, "
						+ "por favor, ¿podrías indicarme para cada color, de qué persona se trata?: " + String.join(", ", grupoDesconocido) + ".\n\n";
			}
			
			if(!grupoConocido.isEmpty()){
				mensajeUsuario += !grupoDesconocido.isEmpty() ? "Por otra parte, he reconocido a " + String.join(", a ", grupoConocido) + ".": "En la imagen he reconocido a " + 
						String.join(", a ", grupoConocido) + ".";
			}

			
			photoBot.enviarMensajeTextoAlUsuario(mensajeUsuario);
			
			
		}
		
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
		//this.conversacion.getCarasDetectadas().actualizarPersonaHashMap(color, etiquetaClasificador, 100.0);
		HashMap<String, Object> msjContent = new HashMap<String, Object>();
		msjContent.put("COMANDO", ConstantesComportamiento.ACTUALIZAR_CAMPO_CARAS_DETECTADAS);
		msjContent.put("ID", this.photoBot.getUser().getIdUsuarioTelegram());
		msjContent.put("COLOR", color);
		msjContent.put("ETIQUETA", etiquetaClasificador);
		msjContent.put("CONFIDENCE", 100.0);
		ACLMessage msjc = new ACLMessage(ACLMessage.INFORM);
		msjc.addReceiver(new AID(ConstantesComportamiento.AGENTE_GESTIONAR_CARAS, AID.ISLOCALNAME));
		
		try {
			msjc.setContentObject((Serializable)msjContent);
			getAgent().send(msjc);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.bd.actualizarInfoImagen(imagen);
		
		//ACTUALIZAR EL ATRIBUTO ETIQUETAMAXUTILIZADA DEL USUARIO
		this.photoBot.setUser(this.photoBot.getUser());
		this.bd.actualizarInfoUsuario(this.photoBot.getUser());
		
		//this.procReglas.ejecutarReglas(this.conversacion, this.self);
	}
	
	public void comprobarExistenciayObtenerUsuario() {
		Usuario usu = bd.existeUsuario(photoBot.getUser());
		
		if(usu == null){
			this.conversacion.setUsuarioRegistrado(false);
		}
		else{
			this.conversacion.setUsuarioRegistrado(true);
			this.photoBot.setUser(usu);
		}
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
	
	private void clasificadorUsuarioActualizado() {
		this.conversacion.setPendienteActualizarClasificador(false);
		this.conversacion.setEsperarDatosDelUsuario(false);
		
		//POSIBLEMENTE AQUI HAYA QUE VOLVER A EJECUTAR REGLAS, DEPENDE DE COMO
		//QUEDE ORGANIZADO EL NUEVO SISTEMA DE REGLAS
	}
	
	
}
