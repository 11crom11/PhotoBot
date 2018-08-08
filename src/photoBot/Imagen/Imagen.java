package photoBot.Imagen;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;

public class Imagen {
	
    private ObjectId id;

	private Date fecha;
	private List<Persona> lPersonas;
	private List<Evento> contexto;
	private Usuario propietario;
	private String ubicacion;
	
	


	public Imagen() {
		this.id = new ObjectId();

		this.fecha = null;
		this.lPersonas = new ArrayList<Persona>();
		this.contexto = new ArrayList<Evento>();
	}
	
	public Imagen(Date f) {		
		this.id = new ObjectId();

		this.fecha = f;
		this.lPersonas = new ArrayList<Persona>();
		this.contexto = new ArrayList<Evento>();
	}
	
	public Imagen(Date fecha, List<Persona> lPersonas, List<Evento> contexto, Usuario propietario, String ubicacion) {
		this.id = new ObjectId();

		this.fecha = fecha;
		this.lPersonas = lPersonas;
		this.contexto = contexto;
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

	public void setContexto(List<Evento> contexto) {
		this.contexto = contexto;
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
		this.contexto.add(e);
	}
	
	public void addEventoContextoImagen(List<Evento> le) {
		for(Evento e : le) {
			this.contexto.add(e);
		}
	}

	public List<Persona> getlPersonas() {
		return lPersonas;
	}

	public List<Evento> getContexto() {
		return contexto;
	}
	
	
}
