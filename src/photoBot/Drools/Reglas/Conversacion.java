package photoBot.Drools.Reglas;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import photoBot.Imagen.Persona;

public class Conversacion {
	
	private boolean saludo;
	private boolean subirFoto;
	private boolean buscarFoto;
	private List<String> lEventos;
	private List<Persona> lPersonas;
	private Date fechaFoto;
	
	public Conversacion() {
		this.saludo = false;
		this.subirFoto = false;
		this.buscarFoto = false;
		
		this.lEventos = new ArrayList<String>();
		this.lPersonas = new ArrayList<Persona>();
		
		this.fechaFoto = null;
	}
	
	public void saludoRecibido() {
		this.saludo = true;
	}
	
	public void solicitudSubirFotoRecibida() {
		this.subirFoto = true;
	}
	
	public void solicitudBuscarFotoRecibida() {
		this.buscarFoto = true;
	}
	
	public void insertarPersonaImagen(Persona p) {
		this.lPersonas.add(p);
	}
	
	public void insertarEventoContextoImagen(String e) {
		this.lEventos.add(e);
	}
	
	public void indicarFechaImagen(Date d) {
		this.fechaFoto = d;
	}
	
	public void limpiarConversacion() {
		this.saludo = false;
		this.subirFoto = false;
		this.buscarFoto = false;
		
		this.lEventos = new ArrayList<String>();
		this.lPersonas = new ArrayList<Persona>();
		
		this.fechaFoto = null;
	}

	public boolean isSaludo() {
		return saludo;
	}

	public boolean isSubirFoto() {
		return subirFoto;
	}

	public boolean isBuscarFoto() {
		return buscarFoto;
	}
	
	
}
