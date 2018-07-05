package photoBot.Imagen;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Imagen {
	private Date fecha;
	private List<Persona> lPersonas;
	private List<Evento> contexto;
	
	public Imagen() {
		this.fecha = null;
		this.lPersonas = new ArrayList<Persona>();
		this.contexto = new ArrayList<Evento>();
	}
	
	public Imagen(Date f) {		
		this.fecha = f;
		this.lPersonas = new ArrayList<Persona>();
		this.contexto = new ArrayList<Evento>();
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
