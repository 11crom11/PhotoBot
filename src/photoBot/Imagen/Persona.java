package photoBot.Imagen;

import java.io.Serializable;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;
import org.mongodb.morphia.annotations.Reference;

//@Entity("Persona")
/**
 * Clase que actua como "Mapper" o "Transer" que interacciona para interactuar con la base de datos, asi 
 * como para almacenar informacion en la aplicacion sobre personas que aparecen en una imagen
 *
 */
public class Persona implements Serializable{
	
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
	
	/**
	 * Constructor de una Persona a partir de su nombre, etiqueta y usuario
	 * @param nombre Nombre de la persona
	 * @param etiqueta Etiqueta con la que sera entrenado el clasificador
	 * @param user Usuario al que pertenece la persona
	 */
	public Persona(String nombre, int etiqueta, Usuario user) {
		this.id = new ObjectId();

		this.nombre = nombre;
		this.etiqueta = etiqueta;
		this.user = user;
	}
	
	/**
	 * Constructor de una persona a partir de su nombre, etiqueta, usuario y object id
	 * @param nombre Nombre de la persona
	 * @param etiqueta Etiqueta con la que sera entrenado el clasificador
	 * @param user Usuario al que pertenece la persona
	 * @param id ID en la bbdd
	 */
	public Persona(String nombre, int etiqueta, Usuario user, ObjectId id) {
		this.id = id;

		this.nombre = nombre;
		this.etiqueta = etiqueta;
		this.user = user;
	}

	/**
	 * @return Object ID
	 */
	public ObjectId getId() {
		return id;
	}

	/**
	 * @param id Object id
	 */
	public void setId(ObjectId id) {
		this.id = id;
	}

	/**
	 * @return Nombre de la persona
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre Nombre de la persona
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * @return Etiqueta con la que el usuario sera entrenado en el clasidicador
	 */
	public int getEtiqueta() {
		return etiqueta;
	}

	/**
	 * @param etiqueta Etiqueta con la que el usuario sera entrenado en el clasidicador
	 */
	public void setEtiqueta(int etiqueta) {
		this.etiqueta = etiqueta;
	}

	/**
	 * @return Usuario al que pertenece la persona
	 */
	public Usuario getUser() {
		return user;
	}

	/**
	 * @param user Usuario al que pertenece la persona
	 */
	public void setUserID(Usuario user) {
		this.user = user;
	}
	
	
}
