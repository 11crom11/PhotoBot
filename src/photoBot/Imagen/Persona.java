package photoBot.Imagen;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

import com.mongodb.DBRef;

//@Entity("Persona")
public class Persona {
	
	@Id
    private ObjectId id;
    
	@Reference(idOnly = true)
	private Usuario user;
	
	@Property
	private String nombre;
	
	@Property
	private int etiqueta;
	
	public Persona() {
		
	}
	
	public Persona(String nombre, int etiqueta, Usuario user) {
		this.id = new ObjectId();

		this.nombre = nombre;
		this.etiqueta = etiqueta;
		this.user = user;
	}
	
	public Persona(String nombre, int etiqueta, Usuario user, ObjectId id) {
		this.id = id;

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
