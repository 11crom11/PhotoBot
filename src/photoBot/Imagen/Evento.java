package photoBot.Imagen;

import org.bson.types.ObjectId;

public class Evento {
	private String nombre;
	private ObjectId id;
	
	public Evento(String n) {
		this.nombre = n;
		this.id = new ObjectId();
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}
	
	
}
