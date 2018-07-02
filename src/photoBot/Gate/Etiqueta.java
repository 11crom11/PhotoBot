package photoBot.Gate;

public class Etiqueta {

	private String tipo;
	private String nombre;
	
	public Etiqueta(String t, String n){
		this.tipo = t;
		this.nombre = n;
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
}
