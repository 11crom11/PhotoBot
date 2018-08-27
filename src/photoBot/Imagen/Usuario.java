package photoBot.Imagen;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

//@Entity("Usuario")
public class Usuario {
	
	@Id
    private ObjectId id;
	
	@Property
	private Integer idUsuarioTelegram;
	
	@Property
	private String nombre;
	
	@Property
	private int etiquetaMaxUsada;
	
	
	public Usuario() {

	}

	public Usuario(Integer idUsuarioTelegram){
		this.idUsuarioTelegram = idUsuarioTelegram;
	}
	
	public Usuario(Integer idUsuarioTelegram, String nombre, int etiqueta) {
		this.idUsuarioTelegram = idUsuarioTelegram;
		this.nombre = nombre;
		this.id = new ObjectId();
		this.etiquetaMaxUsada = etiqueta;
		
	}
	
	public Integer getIdUsuarioTelegram() {
		return idUsuarioTelegram;
	}
	
	public void setIdUsuarioTelegram(Integer idUsuarioTelegram) {
		this.idUsuarioTelegram = idUsuarioTelegram;
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

	public int getEtiquetaMaxUsada() {
		return etiquetaMaxUsada;
	}

	public void setEtiquetaMaxUsada(int etiquetaMaxUsada) {
		this.etiquetaMaxUsada = etiquetaMaxUsada;
	}
	
	
}
