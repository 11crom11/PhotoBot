package photoBot.Agentes.Comportamiento;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.joda.time.DateTime;

import gate.util.GateException;
import photoBot.Agentes.AgenteConversacionUsuario.PhotoBot;
import photoBot.BBDD.PhotoBotBBDD;
import photoBot.Drools.Conversacion;
import photoBot.Drools.ProcesadorDeReglas;
import photoBot.Gate.Etiqueta;
import photoBot.Gate.ProcesadorFechas;
import photoBot.Gate.ProcesadorLenguaje;
import photoBot.Imagen.Imagen;
import photoBot.Imagen.Persona;
import photoBot.Imagen.Usuario;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

/**
 * Esta clase implementa el comportamiento del agente Conversacion Usuario
 *
 */
public class ComportamientoAgenteConversacionUsuario extends CyclicBehaviour {

	private static final long serialVersionUID = 1L;
	private PhotoBot photoBot;
	private ComportamientoAgenteConversacionUsuario self;
	private ProcesadorDeReglas procReglas;
	private ProcesadorLenguaje procLenguaje;
	
	private Conversacion conversacion;
	private PhotoBotBBDD bd;
	

	/**
	 * Este constructor construye al comportamiento del agente Conversacion Usuario iniciando
	 * todos sus procesadores.
	 * @param a Agente Conversacion Usuario
	 * @param pB Bot de Telegram
	 */
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
				else if(comando == ConstantesComportamiento.TODAS_PERSONAS_IMAGEN_DESCRITAS) {
					personasDesconocidasDescritas();
				}
				else if(comando == ConstantesComportamiento.LISTADO_PERSONAS_IMAGEN) {
					actualizarBBDDImagen(msjContent);
				}
				
			} catch (UnreadableException e) {
				e.printStackTrace();
			}				
		}
		
		//Si hay un mensaje por parte de usuario                                                
		if(photoBot.hayMensaje()) {
			
			//1- Recibir mensaje
			String mensaje = photoBot.getMensaje();
			List<Etiqueta> lEtiquetas = new ArrayList<>();

			try {
				
				//2A- Analizar el texto en caso de que sea un mensaje de texto
				if(photoBot.hayMensajeTexto()) {
					lEtiquetas = procLenguaje.analizarTextoGate(mensaje);
					
					if(conversacion.isContextoEnUnaPalabra() == true) {
						if(lEtiquetas.isEmpty() && mensaje.split(" ").length == 1) {
							this.procLenguaje.addPalabraContextoGazetero(mensaje);
							this.photoBot.enviarMensajeTextoAlUsuario("Muy bien. He añadido " + mensaje + " "
									+ "a mi vocabulario de palabras de eventos y contexto.");
							this.conversacion.setContextoDescrito(true);
							this.bot_actualizarInfoContextoImagen(this.conversacion.getImagenPeticionInfo(), new Etiqueta("Evento", mensaje));
							this.conversacion.setContextoEnUnaPalabra(false);
						}
					}
				}
				
				//2B- Indicar que se ha subido una imagen en el caso de que sea un mensaje con foto	
				else if(photoBot.hayMensajeFoto()) {					
					
					this.procReglas.seleccionarGrupoSubirImagenManejador();
					this.conversacion.fotosCargadasUsuario();
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
	

	/**
	 * Este metodo envia un saludo al usuario via chat de Telegram
	 * @param botSaludado Indica si el usuario ha saludado al bot o no
	 */
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
	
	/**
	 * Este metodo envia un mensaje para buscar todas las imagenes de un usuario
	 * @deprecated
	 */
	public void bot_solicitudBuscarTodasImagenes() {
		HashMap<String, Object> msjContent = new HashMap<String, Object>();
		
		msjContent.put("COMANDO", ConstantesComportamiento.SOLICITAR_IMG_AGENTE);
		msjContent.put("ID", photoBot.getUser().getIdUsuarioTelegram().toString());
		
		ACLMessage msj = new ACLMessage(ACLMessage.INFORM);
		self.getAgent().send(msj);
	}
	
	/**
	 * Este metodo se ejecuta cuando se recibe un mensaje de un agente adjuntando una lista de imagenes.
	 *  Se envia al usuario todas la imagenes encontradas en la busqueda de la base de datos.
	 * @param msj Mensaje de agente
	 * @param contenido Contenido del mensaje de agente.
	 */
	private void obtenerImagenesBusquedaAgente(ACLMessage msj, HashMap<String, Object> contenido) {
		List<String> list = (List<String>) contenido.get("LISTA");
		
		if(list.isEmpty() == false) {
			photoBot.enviarImagenes(list);
			photoBot.enviarMensajeTextoAlUsuario("Estas son las fotos que he encontrado teniendo en cuenta tus peticiones de búsqueda.");
		}
		else {
			photoBot.enviarMensajeTextoAlUsuario("No he encontrado ninguna foto que cumpla todo lo que me has dicho.");
		}

	}
	
	/**
	 * Este metodo establece en la coversacion que se espera la recepcion de una imagen y envia un mensaje al
	 *  usuario avisando de que se espera una imagen
	 */
	public void esperarFotoConversacion() {
		this.conversacion.solicitudSubirFotoRecibida();
		
		photoBot.enviarMensajeTextoAlUsuario("Muy bien, enviame la fotografía que quieres que almacene!");

	}
	
	/**
	 * Este metodo anula la espera de recepcion de una imagen
	 */
	public void bot_anularSubidaFotos() {
		this.conversacion.solicitudSubirFotoFinalizada();
		
		photoBot.enviarMensajeTextoAlUsuario("Parece ser que has cambiado de idea respecto a subir una nueva imagen. Lo dejaremos para más tarde.");
	}
	
	/**
	 * Este metodo envia un mensaje al agente ALMACENAR IMAGENES indicandole que se quiere subir una imagen nueva.
	 */
	public void bot_solicitudSubirImagenes() {
		HashMap<String, Object> msjContent = new HashMap<String, Object>();
		
		List<File> lFotos = photoBot.obtenerImagenesMensaje();
		String userID = photoBot.getUser().getIdUsuarioTelegram().toString();
		
		msjContent.put("COMANDO", ConstantesComportamiento.ENVIAR_IMG_AGENTE_ALMACENAR);
		msjContent.put("ID", userID);
		msjContent.put("LISTA", lFotos);
						
		ACLMessage msj = new ACLMessage(ACLMessage.INFORM);
		msj.addReceiver(new AID(ConstantesComportamiento.AGENTE_ALMACENAR_IMAGEN, AID.ISLOCALNAME));
		
		this.conversacion.setFotosCargadasSubida(false);
		
		try {
			msj.setContentObject((Serializable)msjContent);
			getAgent().send(msj);	
			conversacion.fotoAlmacenada();
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Este metodo envia un mensaje al usuario via Telegram solicitando el nombre al usuario
	 */
	public void bot_pedirDatosUsuarioNuevo(){
		this.conversacion.setEsperarDatosDelUsuario(true);
		this.conversacion.saludoRecibido();
		
		photoBot.enviarMensajeTextoAlUsuario("Encantado de conocerte! Me llamo PhotoBot!");
		photoBot.enviarMensajeTextoAlUsuario("Soy un asistente virtual y mi misión es ayudarte a organizar todas tus fotografías."
				+ " Las imágenes que reciba por tu parte las almacenaré en mi buena memoria fotográfica e intentaré aprenderme el"
				+ " nombre de tus amigos para luego poder ayudarte a recuperar tus fotos de manera cómoda, sin que tengas que"
				+ " malgastar tu tiempo buscando entre miles de fotos.");
		photoBot.enviarMensajeTextoAlUsuario("Para empezar, por favor, ¿podrías decirme tu nombre?");
	}
	
	/**
	 * Este metodo registra a un usuario en la base de datos y envía un mensaje al usuario
	 *  via Telegram explicando sus funciones.
	 * @param nombre Etiqueta con el nombre del usuario
	 */
	public void bot_registrarUsuarioNuevo(Etiqueta nombre){
		
		Usuario usuario = new Usuario(photoBot.getUser().getIdUsuarioTelegram(), nombre.getNombre(), 0);
		
		boolean ok = this.bd.crearUsuario(usuario);
		
		this.photoBot.setUser(usuario);
		
		if(ok){
			photoBot.enviarMensajeTextoAlUsuario("Encantado " + nombre.getNombre() + "!");
			photoBot.enviarMensajeTextoAlUsuario("A partir de aquí puedes enviarme las imágenes que quieras almacenar y yo"
					+ " te preguntaré al principio por las personas que aparecen en ellas, para así aprenderme sus nombres."
					+ " Con el tiempo te prometo que me los aprenderé :P");
			photoBot.enviarMensajeTextoAlUsuario("También te preguntaré por el contexto de la imagen (boda, cumpleaños...) para"
					+ " facilitarte luego la búsqueda de la imagen.");
			photoBot.enviarMensajeTextoAlUsuario("Eso es todo lo que tengo que contarte a modo de tutorial, creo que ya podemos empezar.");
			photoBot.enviarMensajeTextoAlUsuario("Si vas a subir una foto pueder decírmelo o bien enviame directamente la imagen de una"
					+ " en una, por favor."
					+ " Por el contrario, si quieres buscar una foto solo tendrás pedírmelo con las personas que quieres que aparezcan"
					+ ", así como si también quieres filtrar por el contexto de la foto y yo haré el resto.");
			photoBot.enviarMensajeTextoAlUsuario("Muy bien, ahora si he terminado de contarte el rollo :P. Estoy a tu disposición :)");
			this.conversacion.registradaInfoDelUsuario();
		}
		else{
			photoBot.enviarMensajeTextoAlUsuario("Ups!!! Parece que ha ocurrido un error, ¿por favor, puedes repetirme tu nombre?");
		}
	}
	
	/**
	 * Este metodo se ejecuta tras la recepcion de un mensaje por parte del agente GESTIONAR CARAS
	 * con los resultados del procesamiento de una imagen y envia al usuario via Telegram los
	 *  reconocimientos
	 * @param msj Mensaje de agente
	 * @param contenido Contenido del mensaje de agente
	 */
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
		
		List<Persona> lPerReconocidas = new ArrayList<Persona>();
		
		if(tripletaColorEtiquetaProbabilidad.isEmpty()) {
			photoBot.enviarMensajeTextoAlUsuario("No he conseguido reconocer a ninguna persona en esta fotografía."
					+ " En el caso de que me haya confundido, deberás volver a fotografíar el retrato."
					+ " Por otro lado, si no me he confundido y quieres guardar la imagen para luego recuperarla me tendrás que decir su contexto,"
					+ " por el contrario, solo podrás recuperarla indicandome la fecha de subida.");
			
			this.conversacion.setContextoDescrito(false);
			this.conversacion.setFotoCompletamenteDescrita(false);
			this.conversacion.setFotoSinPersonas(true);
			
			
		}
		else {
			for (Triple<String, Integer, Double> triple : tripletaColorEtiquetaProbabilidad) {
				
				color = triple.getLeft();
				persona = triple.getMiddle();
				
				Persona p = this.bd.obtenerPersonaApartirEtiqeuta(this.photoBot.getUser(), persona);
				
				probabilidad = triple.getRight();
				
				if (probabilidad > 50.00 || probabilidad == 0.0){
					String nombr = p.getNombre();
					if(nombr.toLowerCase().equals("yo")) {
						nombr = "tú";
					}
					
					grupoConocido.add(nombr + " con el color " + triple.getLeft());
					lPerReconocidas.add(p);
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
			
				conversacion.setPendienteActualizarClasificador(true);
				conversacion.setPersonasNoReconocidasDescritas(false);
			}
			
			if(!grupoConocido.isEmpty()){
				mensajeUsuario += !grupoDesconocido.isEmpty() ? "Por otra parte, he reconocido a " + String.join(", a ", grupoConocido) + ".": "En la imagen he reconocido a " + 
						String.join(", a ", grupoConocido) + ".";
			}

			
			photoBot.enviarMensajeTextoAlUsuario(mensajeUsuario);
			photoBot.enviarMensajeTextoAlUsuario("Corrígeme si me confundo, si todo está OK. Avísame cuando hayas acabado!");
			conversacion.setFotoCompletamenteDescrita(false);
			conversacion.setContextoDescrito(false);
			conversacion.setFotoSinPersonas(false);
		}
		
		File f = new File(FilenameUtils.getPath(urlImagen) + FilenameUtils.getBaseName(urlImagen) + "_rec.jpeg");
		String m = f.delete() ? FilenameUtils.getPath(urlImagen) + FilenameUtils.getBaseName(urlImagen) + "_rec.jpeg HA SIDO ELIMINADO" : "La eliminación ha fallado";
		System.out.println(m);
		
		//obtener datos de la foto
		Imagen im = new Imagen(new Date(fechaSubida), new ArrayList<Persona>(),new ArrayList<String>(), this.photoBot.getUser(), urlImagen);

		//this.bd.registrarImagen(im);
		this.conversacion.setEsperarDatosDelUsuario(true);
		this.conversacion.setImagenPeticionInfo(im);
		this.conversacion.setContextoDescrito(false);
		

		
}
	
	/**
	 * Este metodo envia un mensaje al agente GESTIONAR CARAS para actualizar los resultados
	 *  de deteccion de imagen. 
	 * @param imagen
	 * @param etiqueta
	 */
	public void bot_actualizarInfoPersonaImagen(Imagen imagen, Etiqueta etiqueta){
		String color = etiqueta.getColor();
		color = color.toLowerCase();
		HashMap<String, Object> msjContent = new HashMap<String, Object>();
		
		if(etiqueta.getTipo().equals("Persona_color_desconocida")) {
			//ADJUNTAR ELEMENTOS AL MENSAJE DE AGENTE
			msjContent.put("ETIQUETA", -2);
			msjContent.put("CONFIDENCE", -2.0);
			
			/////////////////////////////////////////////////////////////
		}
		else if (etiqueta.getTipo().equals("Nombre_persona_color")){
			
			//ACTUALIZACION
			String nombrePersona = etiqueta.getNombre();
			
			if(nombrePersona.toLowerCase().equals("yo")) {
				nombrePersona = "yo";
			}
			Persona p = this.bd.obtenerPersonaEtiqueta(this.photoBot.getUser(), nombrePersona);
			int etiquetaClasificador = p.getEtiqueta();
			
			if(etiquetaClasificador == this.photoBot.getUser().getEtiquetaMaxUsada() + 1) {
				this.bd.registrarPersonaUsuario(p);
				this.photoBot.getUser().setEtiquetaMaxUsada(etiquetaClasificador);
			}
			
			//imagen.addPersonaImagen(p); //PROBLEMA, SI ACTUALIZAO A ALGUIEN QUE HA RECONOCIDO MAL ME MANTIENE ESA PERSONA EN LA FOTO
			//this.bd.actualizarInfoImagen(imagen);
			
			////////////////////////////////////////////////////////////
			
			//ACTUALIZAR EL ATRIBUTO ETIQUETAMAXUTILIZADA DEL USUARIO
			this.photoBot.setUser(this.photoBot.getUser());
			this.bd.actualizarInfoUsuario(this.photoBot.getUser());
			
			////////////////////////////////////////////////////////////
			
			//ADJUNTAR ELEMENTOS AL MENSAJE DE AGENTE
			msjContent.put("ETIQUETA", etiquetaClasificador);
			msjContent.put("CONFIDENCE", 100.0);
			
			/////////////////////////////////////////////////////////////
		}
		
		msjContent.put("COMANDO", ConstantesComportamiento.ACTUALIZAR_CAMPO_CARAS_DETECTADAS);
		msjContent.put("ID", this.photoBot.getUser().getIdUsuarioTelegram());
		msjContent.put("COLOR", color);
		
		ACLMessage msjc = new ACLMessage(ACLMessage.INFORM);
		msjc.addReceiver(new AID(ConstantesComportamiento.AGENTE_GESTIONAR_CARAS, AID.ISLOCALNAME));
		
		try {
			msjc.setContentObject((Serializable)msjContent);
			getAgent().send(msjc);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Este metodo envia un mensaje al usuario indicando que va a tener en cuenta los nuevos
	 *  cambios que respectan a una persona.
	 */
	public void bot_avisarCambiosPersona() {
		this.photoBot.enviarMensajeTextoAlUsuario("Ok, tendré en cuenta este dato.");
	}
	
	/**
	 * Este metodo en via un mensaje al agente SUBIR IMAGEN para poder actualizar la BBDD
	 *  con la informacion del contexto de una imagen
	 * @param imagen
	 * @param etiqueta
	 */
	public void bot_actualizarInfoContextoImagen(Imagen imagen, Etiqueta etiqueta) {
		imagen.addEventoContextoImagen(etiqueta.getNombre());
		this.conversacion.setContextoDescrito(true);
		this.conversacion.setImagenPeticionInfo(imagen);
		
		HashMap<String, Object> msjContent = new HashMap<String, Object>();
		msjContent.put("COMANDO", ConstantesComportamiento.ACTUALIZAR_IMAGEN_BBDD);
		msjContent.put("ID", this.photoBot.getUser().getIdUsuarioTelegram());
		msjContent.put("IMAGEN", imagen);
		
		ACLMessage msjc = new ACLMessage(ACLMessage.INFORM);
		msjc.addReceiver(new AID(ConstantesComportamiento.AGENTE_ALMACENAR_IMAGEN, AID.ISLOCALNAME));
		
		try {
			msjc.setContentObject((Serializable)msjContent);
			getAgent().send(msjc);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.photoBot.enviarMensajeTextoAlUsuario("Comprendo, tendré en cuenta que durante la foto se estaba "
				+ "de " + etiqueta.getNombre() + ".");
	}
	
	/**
	 * Este metodo avisa al usuario via Telegram de las carencias de datos de una imagen o finaliza el proceso
	 *  de descripcion de una imagen.
	 */
	public void bot_finalizarDescripcionImagen() {
		if(conversacion.isPersonasNoReconocidasDescritas() == false) {
			this.photoBot.enviarMensajeTextoAlUsuario("Todavía faltan personas que no me has dichos quienes son.");
			
			if(conversacion.isContextoDescrito() == false) {
				this.photoBot.enviarMensajeTextoAlUsuario("Tampoco me has dicho el contexto de la imagen, aunque si quieres, esto último no es obligatorio.");
			}
		}
		else {
			if(conversacion.isContextoDescrito() == false) {
				this.photoBot.enviarMensajeTextoAlUsuario("No me has indicado ningún contexto que describa la imagen, ¿seguro que quieres continuar?");
				this.conversacion.setEsperaConfirmacionFinalizarFoto(true);
			}
			else {
				//FOTO COMPLETAMENTE DESCRITA
				if (conversacion.isFotoSinPersonas() == false) {
					actualizarClasificador();
					enviarMensajeSolicitudPersonaImagen();
				}
 
				this.conversacion.procesoSubirImagenCompletado();
				this.conversacion.setFotoCompletamenteDescrita(true);
				avisarUsuarioFinalizacionSubidaFoto();
			}
		}
	}
	
	/**
	 * Este metodo confirma la finalizacion de descripcion de una imagen
	 */
	public void bot_confirmadoFinalizacionDescripcionImagen() {
		actualizarClasificador();
		enviarMensajeSolicitudPersonaImagen();
		this.conversacion.procesoSubirImagenCompletado();
		avisarUsuarioFinalizacionSubidaFoto();
	}
	
	/**
	 * Este metodo anula la finalizacion de descripcion de imagen posibilitando
	 * el poder crear nuevas palbras de contexto en el gazettero
	 */
	public void bot_negacionFinalizacionDescripcionImagen() {
		this.conversacion.setEsperaConfirmacionFinalizarFoto(false);
		this.photoBot.enviarMensajeTextoAlUsuario("Vale, dime la información de la imagen que quieres que tenga en cuenta y luego avisame otra vez cuando hayas acabado."
				+ " Si anteriormente me has dicho el contexto y no lo he reconocido, dime ahora solamente la palabra, y así"
				+ " lo añado a mi vocabulario.");
		
		this.conversacion.setContextoEnUnaPalabra(true);
	}
	
	/**
	 * Este metodo envia un mensaje al usuario via Telegram avisando de que no se puede subir nuevas imagenes
	 *  hasta que no se termine de describir la anterior recibida.
	 */
	public void bot_rechazarImagenes() {
		this.photoBot.enviarMensajeTextoAlUsuario("No puedo procesar esta nueva imagen mientras tenemos pendiente "
				+ "la descripcion de otra imagen. Termina de contarme la imagen anterior y vuelve a enviarme esta foto.");
		
		this.conversacion.setFotosCargadasSubida(false);
	}
	
	/**
	 * Este metodo envia un mensaje al agente BUSCAR_IMAGENES con el objetivo de enviar 
	 *  un filtro de tipo fecha 
	 * @param etiqueta Etiqueta fecha
	 */
	public void bot_enviarFiltroFecha(Etiqueta etiqueta) {
		String userID = photoBot.getUser().getIdUsuarioTelegram().toString();
		Pair<Date, Date> filtroFecha = ProcesadorFechas.obtenerFormatoDate(etiqueta);
		
		HashMap<String, Object> msjContent = new HashMap<String, Object>();
		msjContent.put("COMANDO", ConstantesComportamiento.ANADIR_FILTRO_FECHA);
		msjContent.put("ID", userID);
		msjContent.put("FILTRO", filtroFecha);
						
		ACLMessage msj = new ACLMessage(ACLMessage.INFORM);
		msj.addReceiver(new AID(ConstantesComportamiento.AGENTE_BUSCAR_IMAGEN, AID.ISLOCALNAME));
		
		try {
			msj.setContentObject((Serializable)msjContent);
			getAgent().send(msj);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Este metodo envia un mensaje al agente BUSCAR_IMAGENES con el objetivo de enviar 
	 *  un filtro de tipo evento
	 * @param etiqueta Etiqueta evento
	 */
	public void bot_enviarFiltroEvento(Etiqueta etiqueta) {
		String userID = photoBot.getUser().getIdUsuarioTelegram().toString();
		
		HashMap<String, Object> msjContent = new HashMap<String, Object>();
		msjContent.put("COMANDO", ConstantesComportamiento.ANADIR_FILTRO_EVENTO);
		msjContent.put("ID", userID);
		msjContent.put("FILTRO", etiqueta.getNombre());
						
		ACLMessage msj = new ACLMessage(ACLMessage.INFORM);
		msj.addReceiver(new AID(ConstantesComportamiento.AGENTE_BUSCAR_IMAGEN, AID.ISLOCALNAME));
		
		try {
			msj.setContentObject((Serializable)msjContent);
			getAgent().send(msj);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Este metodo envia un mensaje al agente BUSCAR_IMAGENES con el objetivo de enviar 
	 *  un filtro de tipo persona 
	 * @param etiqueta Etiqueta persona
	 */
	public void bot_enviarFiltroPersona(Etiqueta etiqueta) {
		String userID = photoBot.getUser().getIdUsuarioTelegram().toString();
		
		HashMap<String, Object> msjContent = new HashMap<String, Object>();
		msjContent.put("COMANDO", ConstantesComportamiento.ANADIR_FILTRO_PERSONA);
		msjContent.put("ID", userID);
		msjContent.put("FILTRO", etiqueta.getNombre());
						
		ACLMessage msj = new ACLMessage(ACLMessage.INFORM);
		msj.addReceiver(new AID(ConstantesComportamiento.AGENTE_BUSCAR_IMAGEN, AID.ISLOCALNAME));
		
		try {
			msj.setContentObject((Serializable)msjContent);
			getAgent().send(msj);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Este metodo envia un mensaje al agente BUSCAR_IMAGENES con el objetivo de arrancar
	 *  el proceso de busqueda de imagenes
	 */
	public void bot_buscarImagenesFiltros() {
		String userID = photoBot.getUser().getIdUsuarioTelegram().toString();
		
		HashMap<String, Object> msjContent = new HashMap<String, Object>();
		msjContent.put("COMANDO", ConstantesComportamiento.BUSCAR_IMAGENES);
		msjContent.put("ID", userID);
						
		ACLMessage msj = new ACLMessage(ACLMessage.INFORM);
		msj.addReceiver(new AID(ConstantesComportamiento.AGENTE_BUSCAR_IMAGEN, AID.ISLOCALNAME));
		
		try {
			msj.setContentObject((Serializable)msjContent);
			getAgent().send(msj);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Este metodo envia un mensaje al agente BUSCAR_IMAGENES con el objetivo de 
	 *  inicializar la lista de filtros con la que se desea buscar en la base de datos
	 */
	public void solicitarInicializacionListaFiltros() {
		String userID = photoBot.getUser().getIdUsuarioTelegram().toString();
		
		HashMap<String, Object> msjContent = new HashMap<String, Object>();
		msjContent.put("COMANDO", ConstantesComportamiento.CREAR_LISTA_FILTROS);
		msjContent.put("ID", userID);
						
		ACLMessage msj = new ACLMessage(ACLMessage.INFORM);
		msj.addReceiver(new AID(ConstantesComportamiento.AGENTE_BUSCAR_IMAGEN, AID.ISLOCALNAME));
		
		try {
			msj.setContentObject((Serializable)msjContent);
			getAgent().send(msj);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Este metodo comprueba la existencia de la base de datos y modifica el estado del
	 *  objeto Conversacion
	 */
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
	
	/**
	 * Este metodo comprueba en el objeto conversacion si todos las  personas de una imagen
	 *  han sido descritas. En caso de haber sido todas descritas, avisa al usuario via Telegram
	 *  de que ha finalizado describir a todas las personas.
	 */
	private void personasDesconocidasDescritas() {
		if(this.conversacion.isPersonasNoReconocidasDescritas() == false) {
			this.photoBot.enviarMensajeTextoAlUsuario("Enhorabuena, acabas de desribirme a todas las personas "
					+ "que no he reconocido. Asegurate de que las personas que te reconozca "
					+ "son las que son. Las máquinas también nos podemos con confundir ;)");
			this.photoBot.enviarMensajeTextoAlUsuario("¿Alguna cosa más que añadir?");
		}
		
		conversacion.setPersonasNoReconocidasDescritas(true);
	}
	
	/**
	 * Este metodo envia un mensaje al agente GESTIONAR CARAS con el objetivo de que el agente
	 *  reentrene el clasificador de un usuario.
	 */
	private void actualizarClasificador() {
		HashMap<String, Object> msjContent = new HashMap<String, Object>();
		msjContent.put("COMANDO", ConstantesComportamiento.ACTUALIZAR_CLASIFICADOR);
		msjContent.put("ID", this.photoBot.getUser().getIdUsuarioTelegram());
		
		ACLMessage msjc = new ACLMessage(ACLMessage.INFORM);
		msjc.addReceiver(new AID(ConstantesComportamiento.AGENTE_GESTIONAR_CARAS, AID.ISLOCALNAME));
		
		try {
			msjc.setContentObject((Serializable)msjContent);
			getAgent().send(msjc);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Este método envia un mensaje al agente SUBIR IMAGENES indicando que se desea actualuar
	 *  la informacion de una imagen en la base de datos
	 * @param msjContent Contenido del mensaje de agente
	 */
	private void actualizarBBDDImagen(HashMap<String, Object> msjContent) {
		List<Integer> list = (List<Integer>) msjContent.get("LISTA");
		
		List<Persona> lPersonas = this.bd.listaPersonasPorEtiqueta(list, String.valueOf(photoBot.getUser().getIdUsuarioTelegram()));
		
		Imagen i = this.conversacion.getImagenPeticionInfo();
		i.setlPersonas(lPersonas);
		
		msjContent = new HashMap<String, Object>();
		msjContent.put("COMANDO", ConstantesComportamiento.ACTUALIZAR_IMAGEN_BBDD);
		msjContent.put("ID", this.photoBot.getUser().getIdUsuarioTelegram());
		msjContent.put("IMAGEN", i);
		
		ACLMessage msjc = new ACLMessage(ACLMessage.INFORM);
		msjc.addReceiver(new AID(ConstantesComportamiento.AGENTE_ALMACENAR_IMAGEN, AID.ISLOCALNAME));
		
		try {
			msjc.setContentObject((Serializable)msjContent);
			getAgent().send(msjc);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.conversacion.setImagenPeticionInfo(null);
		this.conversacion.procesoSubirImagenCompletado();
	}
	
	/**
	 * Este metodo envia un mensaje al usuario via Telegram avisando de que se ha finalizado
	 *  el proceso de subir de imagen al sistema
	 */
	private void avisarUsuarioFinalizacionSubidaFoto() {
		this.photoBot.enviarMensajeTextoAlUsuario("Muy bien. Me quedo con la información que me has"
				+ " descrito de esta imagen para que puedas recuperarla posteriormente.");
		this.photoBot.enviarMensajeTextoAlUsuario("¿Qué más puedo hacer por ti?");
		
		this.conversacion.setEsperarDatosDelUsuario(false);
	}
	
	/**
	 * Este metodo envia un mensaje al agente GESTIONAR CARAS con el objetivo de obtener el listado
	 *  de personas que aparecen en la ultima imagen procesada.
	 */
	private void enviarMensajeSolicitudPersonaImagen() {
		HashMap<String, Object> msjContent = new HashMap<String, Object>();
		msjContent.put("COMANDO", ConstantesComportamiento.SOLICITAR_LISTADO_PERSONAS_ACTUALIZAR_IMAGEN);
		msjContent.put("ID", this.photoBot.getUser().getIdUsuarioTelegram());
		
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
