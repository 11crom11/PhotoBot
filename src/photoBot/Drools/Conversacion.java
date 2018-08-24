package photoBot.Drools;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import photoBot.Imagen.Imagen;
import photoBot.Imagen.Persona;
import photoBot.OpenCV.CarasDetectadas;

public class Conversacion {
	
	private boolean saludo;
	private boolean subirFoto;
	private boolean buscarFoto;
	private boolean fotosCargadasSubida;
	private List<String> lEventos;
	private List<Persona> lPersonas;
	private Date fechaFoto;
	private boolean usuarioRegistrado;
	private boolean esperarDatosDelUsuario;
	
	private Imagen imagenPeticionInfo;
	
	private boolean pendienteActualizarClasificador;
	
	
	public Conversacion() {
		this.saludo = false;
		this.subirFoto = false;
		this.buscarFoto = false;
		this.usuarioRegistrado = false;
		this.esperarDatosDelUsuario = false;
		
		this.lEventos = new ArrayList<String>();
		this.lPersonas = new ArrayList<Persona>();
		
		this.fotosCargadasSubida = false;
		
		this.fechaFoto = null;
		this.imagenPeticionInfo = null;
		this.pendienteActualizarClasificador = false;
	}
	
	public void setImagenPeticionInfo(Imagen imagenPeticionInfo){
		this.esperarDatosDelUsuario = true;
		this.imagenPeticionInfo = imagenPeticionInfo;
	}
	
	public void registradaInfoImagen(){
		this.esperarDatosDelUsuario = false;
		this.imagenPeticionInfo = null;
	}

	public void infoRecibida(){
		this.esperarDatosDelUsuario = false;
	}
	
	public Imagen getImagenPeticionInfo(){
		return this.imagenPeticionInfo;
	}
	
	public void setEsperarDatosDelUsuario(boolean esperarDatosDelUsuario) {
		this.esperarDatosDelUsuario = esperarDatosDelUsuario;
	}
	
	public boolean isEsperarDatosDelUsuario() {
		return esperarDatosDelUsuario;
	}
	
	public void registradaInfoDelUsuario(){
		this.esperarDatosDelUsuario = false;
		this.usuarioRegistrado = true;
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
		
		this.fotosCargadasSubida = false;
		
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
	
	public boolean isFotosCargadasSubida() {
		return fotosCargadasSubida;
	}
	
	public void solicitudSubirFotoFinalizada() {
		this.subirFoto = false;
	}
	
	public void fotosCargadasCorrectamente() {
		this.fotosCargadasSubida = true;
	}
	
	public boolean isUsuarioRegistrado() {
		return usuarioRegistrado;
	}

	public void setUsuarioRegistrado(boolean usuarioRegistrado) {
		this.usuarioRegistrado = usuarioRegistrado;
	}

	public boolean isPendienteActualizarClasificador() {
		return pendienteActualizarClasificador;
	}

	public void setPendienteActualizarClasificador(boolean pendienteActualizarClasificador) {
		this.pendienteActualizarClasificador = pendienteActualizarClasificador;
	}
}
