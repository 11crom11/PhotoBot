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

	public Imagen() {
		this.id = new ObjectId();

		this.fecha = null;
		this.lPersonas = new ArrayList<Persona>();
		this.lEventos = new ArrayList<String>();
	}
	
	public Imagen(Date f) {		
		this.id = new ObjectId();

		this.fecha = f;
		this.lPersonas = new ArrayList<Persona>();
		this.lEventos = new ArrayList<String>();
	}
	
	public Imagen(Date fecha, List<Persona> lPersonas, List<String> contexto, Usuario propietario, String ubicacion) {
		this.id = new ObjectId();

		this.fecha = fecha;
		this.lPersonas = lPersonas;
		this.lEventos = contexto;
		this.propietario = propietario;
		this.ubicacion = ubicacion;
	}
	
	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public Usuario getPropietario() {
		return propietario;
	}

	public void setPropietario(Usuario propietario) {
		this.propietario = propietario;
	}

	public String getUbicacion() {
		return ubicacion;
	}

	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}

	public void setlPersonas(List<Persona> lPersonas) {
		this.lPersonas = lPersonas;
	}

	public void setContexto(List<String> contexto) {
		this.lEventos = contexto;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
	public void addPersonaImagen(Persona p) {
		this.lPersonas.add(p);
	}
	
	public void addPersonaImagen(List<Persona> lp) {
		for(Persona p : lp) {
			this.lPersonas.add(p);
		}
	}
	
	public void addEventoContextoImagen(String e) {
		this.lEventos.add(e);
	}
	
	public void addEventoContextoImagen(List<String> le) {
		for(String e : le) {
			this.lEventos.add(e);
		}
	}

	public List<Persona> getlPersonas() {
		return lPersonas;
	}

	public List<String> getContexto() {
		return lEventos;
	}
	
	
}
