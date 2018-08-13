package photoBot.Imagen;

import java.util.List;

import org.bson.types.ObjectId;

public class Persona {
	
    private ObjectId id;
    
	private String nombre;
	private int etiqueta;
	
	
	public Persona(){

	}

	public Persona(String nombre, int etiqueta) {
		this.id = new ObjectId();

		this.nombre = nombre;
		this.etiqueta = etiqueta;
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	
}
