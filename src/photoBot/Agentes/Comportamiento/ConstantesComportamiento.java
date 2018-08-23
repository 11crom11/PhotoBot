package photoBot.Agentes.Comportamiento;

public final class ConstantesComportamiento {
	public static final int OBTENER_IMG_AGENTE;
	public static final int ENVIAR_IMG_AGENTE;
	public static final int SOLICITAR_IMG_AGENTE;
	public static final int ENTREGAR_IMG_ENCONTRADAS;
	public static final int RESULTADO_RECONOCIMIENTO_IMAGEN;
	public static final int RECONOCER_CARAS;
	public static final int CLASIFICADOR_USUARIO_ACTUALIZADO;
	public static final int ACTUALIZAR_CAMPO_CARAS_DETECTADAS;

	public static final String AGENTE_ALMACENAR_IMAGEN;
	public static final String AGENTE_BUSCAR_IMAGEN;
	public static final String AGENTE_CONVERSACION_USUARIO;
	public static final String AGENTE_GESTIONAR_CARAS;

	static {
		OBTENER_IMG_AGENTE = 1;
		ENVIAR_IMG_AGENTE = 2;
		SOLICITAR_IMG_AGENTE = 3;
		ENTREGAR_IMG_ENCONTRADAS = 4;
		RESULTADO_RECONOCIMIENTO_IMAGEN = 5;
		RECONOCER_CARAS = 6;
		CLASIFICADOR_USUARIO_ACTUALIZADO = 7;
		ACTUALIZAR_CAMPO_CARAS_DETECTADAS = 8;
		
		AGENTE_ALMACENAR_IMAGEN = "AgenteAlmacenarImagenes";
		AGENTE_BUSCAR_IMAGEN = "AgenteBuscarImagen";
		AGENTE_CONVERSACION_USUARIO = "AgenteConversacional";
		AGENTE_GESTIONAR_CARAS = "AgenteGestionarCaras";
	}
	
	public ConstantesComportamiento() {
		
	}
}
