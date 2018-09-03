package photoBot.Agentes.Comportamiento;

public final class ConstantesComportamiento {
	public static final int OBTENER_IMG_AGENTE;
	public static final int ENVIAR_IMG_AGENTE_ALMACENAR;
	public static final int SOLICITAR_IMG_AGENTE;
	public static final int ENTREGAR_IMG_ENCONTRADAS;
	public static final int RESULTADO_RECONOCIMIENTO_IMAGEN;
	public static final int RECONOCER_CARAS;
	public static final int CLASIFICADOR_USUARIO_ACTUALIZADO;
	public static final int ACTUALIZAR_CAMPO_CARAS_DETECTADAS;
	public static final int ACTUALIZAR_CLASIFICADOR;
	public static final int TODAS_PERSONAS_IMAGEN_DESCRITAS;
	public static final int BUSCAR_IMAGENES;
	public static final int ANADIR_FILTRO_FECHA;
	public static final int ANADIR_FILTRO_PERSONA;
	public static final int ANADIR_FILTRO_EVENTO;
	public static final int CREAR_LISTA_FILTROS;
	public static final int SOLICITAR_LISTADO_PERSONAS_ACTUALIZAR_IMAGEN;
	public static final int LISTADO_PERSONAS_IMAGEN;
	public static final int ACTUALIZAR_IMAGEN_BBDD;

	public static final String AGENTE_ALMACENAR_IMAGEN;
	public static final String AGENTE_BUSCAR_IMAGEN;
	public static final String AGENTE_CONVERSACION_USUARIO;
	public static final String AGENTE_GESTIONAR_CARAS;
	
	public static final int GRUPO_BUSCAR_IMAGEN;
	public static final int GRUPO_SUBIR_IMAGEN;
	public static final int GRUPO_REGISTRO_USUARIO;
	public static final int GRUPO_SALUDO;
	public static final int GRUPO_EXISTENCIA_USUARIO;
	public static final int GRUPO_ESPERA_PETICION_USUARIO;

	static {
		OBTENER_IMG_AGENTE = 1;
		ENVIAR_IMG_AGENTE_ALMACENAR = 2;
		SOLICITAR_IMG_AGENTE = 3;
		ENTREGAR_IMG_ENCONTRADAS = 4;
		RESULTADO_RECONOCIMIENTO_IMAGEN = 5;
		RECONOCER_CARAS = 6;
		CLASIFICADOR_USUARIO_ACTUALIZADO = 7;
		ACTUALIZAR_CAMPO_CARAS_DETECTADAS = 8;
		ACTUALIZAR_CLASIFICADOR = 9;
		TODAS_PERSONAS_IMAGEN_DESCRITAS = 10;
		BUSCAR_IMAGENES = 11;
		ANADIR_FILTRO_FECHA = 12;
		ANADIR_FILTRO_PERSONA = 13;
		ANADIR_FILTRO_EVENTO = 14;
		CREAR_LISTA_FILTROS = 15;
		SOLICITAR_LISTADO_PERSONAS_ACTUALIZAR_IMAGEN = 16;
		LISTADO_PERSONAS_IMAGEN = 17;
		ACTUALIZAR_IMAGEN_BBDD = 18;
		
		AGENTE_ALMACENAR_IMAGEN = "AgenteAlmacenarImagenes";
		AGENTE_BUSCAR_IMAGEN = "AgenteBuscarImagen";
		AGENTE_CONVERSACION_USUARIO = "AgenteConversacional";
		AGENTE_GESTIONAR_CARAS = "AgenteGestionarCaras";
		
		GRUPO_ESPERA_PETICION_USUARIO = 6;
		GRUPO_BUSCAR_IMAGEN = 5;
		GRUPO_SUBIR_IMAGEN = 4;
		GRUPO_REGISTRO_USUARIO = 3;
		GRUPO_SALUDO = 2;
		GRUPO_EXISTENCIA_USUARIO = 1;
	}
	
	public ConstantesComportamiento() {
		
	}
}
