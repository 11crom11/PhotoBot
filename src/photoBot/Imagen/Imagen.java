package photoBot.Imagen;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;
import org.mongodb.morphia.annotations.Reference;

//@Entity ("Imagen")
/**
 * Clase que actua como "mapper" o "Transfer" para interactuar con la base de datos, asi 
 * como para almacenar informacion en la aplicacion
 *
 */
public class Imagen implements Serializable{
	
	@Id
    private ObjectId id;
	
	@Property
	private Date fecha;
	
	@Property	
	private String ubicacion;
	
	@Property	
	private List<String> lEventos;
	
	@Reference(idOnly = true)
	private List<Persona> lPersonas;
	
	@Reference(idOnly = true)
	private Usuario propietario;

	/**
	 * Constructor de Imagen sin parametros
	 */
	public Imagen() {
		this.id = new ObjectId();

		this.fecha = null;
		this.lPersonas = new ArrayList<Persona>();
		this.lEventos = new ArrayList<String>();
	}
	
	/**
	 * Constructor de una imagen a partir de una fecha
	 * @param f Fecha de subida de una imagen
	 */
	public Imagen(Date f) {		
		this.id = new ObjectId();

		this.fecha = f;
		this.lPersonas = new ArrayList<Persona>();
		this.lEventos = new ArrayList<String>();
	}
	
	/**
	 * Constructor completo de una imagen
	 * @param fecha Fecha de subida de una imagen
	 * @param lPersonas Lista de personas que aparecen en una imagen
	 * @param contexto Contexto de una imagen
	 * @param propietario Propietario de la imagen
	 * @param ubicacion URL de almacenamiento de la imagen
	 */
	public Imagen(Date fecha, List<Persona> lPersonas, List<String> contexto, Usuario propietario, String ubicacion) {
		this.id = new ObjectId();

		this.fecha = fecha;
		this.lPersonas = lPersonas;
		this.lEventos = contexto;
		this.propietario = propietario;
		this.ubicacion = ubicacion;
	}
	
	/**
	 * @return Object ID de la imagen
	 */
	public ObjectId getId() {
		return id;
	}

	/**
	 * @param id Object ID de la imagen
	 */
	public void setId(ObjectId id) {
		this.id = id;
	}

	/**
	 * @return Usuario propietario de la imagen
	 */
	public Usuario getPropietario() {
		return propietario;
	}

	/**
	 * @param propietario Usuario propietario de la imagen
	 */
	public void setPropietario(Usuario propietario) {
		this.propietario = propietario;
	}

	/**
	 * @return Ubicacion de almacenamiento de la imagen
	 */
	public String getUbicacion() {
		return ubicacion;
	}

	/**
	 * @param ubicacion Ubicacion de almacenamiento de la imagen
	 */
	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}

	/**
	 * @param lPersonas Lista de personas de una imagen
	 */
	public void setlPersonas(List<Persona> lPersonas) {
		this.lPersonas = lPersonas;
	}

	/**
	 * @param lista de contexto Contexto/evento de la imagen
	 */
	public void setContexto(List<String> contexto) {
		this.lEventos = contexto;
	}

	/**
	 * @return Fecha de subida de la imagen
	 */
	public Date getFecha() {
		return fecha;
	}

	/**
	 * @param fecha Fecha de subida de la imagen
	 */
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
	/**
	 * @param p Una de las personas que aparece en la imagen
	 */
	public void addPersonaImagen(Persona p) {
		this.lPersonas.add(p);
	}
	
	/**
	 * @param lp Lista de personas que aparece en la imagen
	 */
	public void addPersonaImagen(List<Persona> lp) {
		for(Persona p : lp) {
			this.lPersonas.add(p);
		}
	}
	
	/**
	 * @param e Uno de los contextos de la imagen
	 */
	public void addEventoContextoImagen(String e) {
		this.lEventos.add(e);
	}
	
	/**
	 * @param le Lista de eventos de la imagen
	 */
	public void addEventoContextoImagen(List<String> le) {
		for(String e : le) {
			this.lEventos.add(e);
		}
	}

	/**
	 * @return Lista de personas que aparecen en la imagen
	 */
	public List<Persona> getlPersonas() {
		return lPersonas;
	}

	/**
	 * @return Lista de evetos/contextos de la imagen
	 */
	public List<String> getContexto() {
		return lEventos;
	}
	
	
}
