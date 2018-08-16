package photoBot.Imagen;

import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

//@Entity("persona")
public class Persona {
	
	@Id
    private ObjectId id;
    
	@Reference
	private Usuario user;
	
	@Property
	private String nombre;
	
	@Property
	private int etiqueta;
	
	
	public Persona(String nombre, int etiqueta, Usuario user) {
		this.id = new ObjectId();

		this.nombre = nombre;
		this.etiqueta = etiqueta;
		this.user = user;
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

	public int getEtiqueta() {
		return etiqueta;
	}

	public void setEtiqueta(int etiqueta) {
		this.etiqueta = etiqueta;
	}

	public Usuario getUser() {
		return user;
	}

	public void setUserID(Usuario user) {
		this.user = user;
	}
	
	
}
