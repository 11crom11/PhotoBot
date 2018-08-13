package photoBot.Imagen;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;

public class Imagen {
	
    private ObjectId id;

	private Date fecha;
	private List<Persona> lPersonas;
	private List<Evento> lEventos;
	private Integer propietario;
	private String ubicacion;
	
	


	public Imagen() {
		this.id = new ObjectId();

		this.fecha = null;
		this.lPersonas = new ArrayList<Persona>();
		this.lEventos = new ArrayList<Evento>();
	}
	
	public Imagen(Date f) {		
		this.id = new ObjectId();

		this.fecha = f;
		this.lPersonas = new ArrayList<Persona>();
		this.lEventos = new ArrayList<Evento>();
	}
	
	public Imagen(Date fecha, List<Persona> lPersonas, List<Evento> contexto, Integer propietario, String ubicacion) {
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

	public Integer getPropietario() {
		return propietario;
	}

	public void setPropietario(Integer propietario) {
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

	public void setContexto(List<Evento> contexto) {
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
	
	public void addEventoContextoImagen(Evento e) {
		this.lEventos.add(e);
	}
	
	public void addEventoContextoImagen(List<Evento> le) {
		for(Evento e : le) {
			this.lEventos.add(e);
		}
	}

	public List<Persona> getlPersonas() {
		return lPersonas;
	}

	public List<Evento> getContexto() {
		return lEventos;
	}
	
	
}
