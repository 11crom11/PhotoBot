package photoBot.Gate;

public class Etiqueta {

	private String tipo;
	private String nombre;
	private String color;
	
	public Etiqueta(String t, String n){
		this.tipo = t;
		this.nombre = n;
	}
	
	public Etiqueta(String t, String n, String c){
		this.tipo = t;
		this.nombre = n;
		this.color = c;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
}
