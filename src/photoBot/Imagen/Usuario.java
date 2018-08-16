package photoBot.Imagen;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

//@Entity("usuarios")
public class Usuario {
	
	@Id
    private ObjectId id;
	
	@Property
	private Integer idUsuarioTelegram;
	
	@Property
	private String nombre;
	
	@Property
	private int edad;
	
	
	public Usuario() {

	}

	public Usuario(Integer idUsuarioTelegram){
		this.idUsuarioTelegram = idUsuarioTelegram;
	}
	
	public Usuario(Integer idUsuarioTelegram, String nombre, int edad) {
		this.idUsuarioTelegram = idUsuarioTelegram;
		this.nombre = nombre;
		this.edad = edad;
		this.id = new ObjectId();
		
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
	
	public int getEdad() {
		return edad;
	}
	
	public void setEdad(int edad) {
		this.edad = edad;
	}
	
	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}
}
