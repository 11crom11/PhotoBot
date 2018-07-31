package photoBot.Gate;

public class EtiquetaColor extends Etiqueta {

	private String nombrePersona;
	private String color;
	
	public EtiquetaColor(String t, String n, String np, String c) {
		super(t, n);

		this.nombrePersona = np;
		this.color = c;
	}

	public String getNombrePersona() {
		return nombrePersona;
	}

	public void setNombrePersona(String nombrePersona) {
		this.nombrePersona = nombrePersona;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
}
