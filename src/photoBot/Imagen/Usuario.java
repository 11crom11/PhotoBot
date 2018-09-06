package photoBot.Imagen;

import java.io.Serializable;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

//@Entity("Usuario")
/**
 * Clase que actua como "Mapper" o "Transer" que interacciona para interactuar con la base de datos, asi 
 * como para almacenar informacion en la aplicacion sobre un usuario
 *
 */
/**
 * @author d_dan
 *
 */
/**
 * @author d_dan
 *
 */
public class Usuario implements Serializable{
	
	@Id
    private ObjectId id;
	
	@Property
	private Integer idUsuarioTelegram;
	
	@Property
	private String nombre;
	
	@Property
	private int etiquetaMaxUsada;
	
	
	/**
	 * Constructor de usuario vacio
	 */
	public Usuario() {

	}

	/**
	 * Constructor de usuario a partir del id de telegram
	 * @param idUsuarioTelegram ID de telegram
	 */
	public Usuario(Integer idUsuarioTelegram){
		this.idUsuarioTelegram = idUsuarioTelegram;
	}
	
	/**
	 * Constructor de un usuario a partir del id de telegram, su nombre y etiqueta maxima utilizada
	 * @param idUsuarioTelegram
	 * @param nombre
	 * @param etiqueta
	 */
	public Usuario(Integer idUsuarioTelegram, String nombre, int etiqueta) {
		this.idUsuarioTelegram = idUsuarioTelegram;
		this.nombre = nombre;
		this.id = new ObjectId();
		this.etiquetaMaxUsada = etiqueta;
		
	}
	
	/**
	 * @return Id de telegram
	 */
	public Integer getIdUsuarioTelegram() {
		return idUsuarioTelegram;
	}
	
	/**
	 * @param idUsuarioTelegram Id telegram
	 */
	public void setIdUsuarioTelegram(Integer idUsuarioTelegram) {
		this.idUsuarioTelegram = idUsuarioTelegram;
	}
	
	/**
	 * @return Nombre del usuario
	 */
	public String getNombre() {
		return nombre;
	}
	
	/**
	 * @param nombre Nombre del usuario
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
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
	 * @return Etiqueta maxima utilizada en el clasificador del usuario
	 */
	public int getEtiquetaMaxUsada() {
		return etiquetaMaxUsada;
	}

	/**
	 * @param etiquetaMaxUsada Etiqueta maxima utilizada en el clasificador del usuario
	 */
	public void setEtiquetaMaxUsada(int etiquetaMaxUsada) {
		this.etiquetaMaxUsada = etiquetaMaxUsada;
	}
	
	
}
