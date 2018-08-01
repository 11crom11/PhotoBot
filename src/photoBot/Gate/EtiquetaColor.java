package photoBot.Gate;

public class EtiquetaColor extends Etiqueta {

	private String color;
	
	public EtiquetaColor(String t, String n, String c) {
		super(t, n);

		this.color = c;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
}
