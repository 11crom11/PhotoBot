package photoBot.Gate;

/**
 * Clase que se utiliza para manejar las etiquetas que se obtienen con GATE
 *
 */
public class Etiqueta {

	private String tipo;
	private String nombre;
	private String color;
	
	/**
	 * Constructor de una etiqueta basica tipo-texto
	 * @param t Tipo de la etiqueta
	 * @param n Texto al que hace referencia la etiqueta
	 */
	public Etiqueta(String t, String n){
		this.tipo = t;
		this.nombre = n;
	}
	
	/**
	 * Constructor de una etiqueta tipo-texto-color
	 * @param t Tipo de la etiqueta
	 * @param n Texto al que hace referencia la etiqueta
	 * @param c Color que aparece en las etiquetas de tipo Nombre_Persona_Color o Persona_Color_Desconocida
	 */
	public Etiqueta(String t, String n, String c){
		this.tipo = t;
		this.nombre = n;
		this.color = c;
	}

	/**
	 * Este metodo devuelve el tipo de una etiqueta
	 * @return tipo de etiqueta
	 */
	public String getTipo() {
		return tipo;
	}

	/**
	 * Este metodo establece el tipo de un etiqueta
	 * @param tipo Tipo de etiqueta
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	/**
	 * Este metodo devuelve el texto al que hace referencia una etiqueta
	 * @return Texto al que hace referencia una etiqueta
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Este metodo establece el texto de una etiqueta
	 * @param nombre
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * Este metodo devuelve el color de una etiqueta de tipo Nombre_Persona_Color o Persona_Color_Desconocida
	 * @return Color al que hace referencia la etiqueta
	 */
	public String getColor() {
		return color;
	}

	/**
	 * Este metodo establece el color en una etiqueta de tipo Nombre_Persona_Color o Persona_Color_Desconocida
	 * @param color Color a establecer
	 */ 
	public void setColor(String color) {
		this.color = color;
	}
}
