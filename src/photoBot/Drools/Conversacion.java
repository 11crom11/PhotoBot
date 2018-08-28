package photoBot.Drools;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import photoBot.Imagen.Imagen;
import photoBot.Imagen.Persona;
import photoBot.OpenCV.CarasDetectadas;

public class Conversacion {
	                                                        //EN USO
	//ESPERA DE DATOS POR PARTE DEL USUARIO/////////////////////////
	private boolean esperarDatosDelUsuario;                     // X
	////////////////////////////////////////////////////////////////
	
	//SALUDO & REGISTRO/////////////////////////////////////////////
	private boolean saludo;                                     // X
	private boolean usuarioRegistrado;                          // X
    ////////////////////////////////////////////////////////////////
	
	//BUSCAR IMAGENES///////////////////////////////////////////////
	private boolean buscarFoto;                                 //
    ////////////////////////////////////////////////////////////////
	
	//SUBIR IMAGEN//////////////////////////////////////////////////
	private boolean subirFoto;                                  // X
	private Imagen imagenPeticionInfo;                          // X
	private boolean fotosCargadasSubida;                        // X
	private boolean pendienteActualizarClasificador;            // X
	private boolean fotoCompletamenteDescrita;                  //
    ////////////////////////////////////////////////////////////////
	
	
	public Conversacion() {
		this.saludo = false;
		this.subirFoto = false;
		this.buscarFoto = false;
		this.usuarioRegistrado = false;
		this.esperarDatosDelUsuario = false;
		
		this.fotosCargadasSubida = false;
		
		this.imagenPeticionInfo = null;
		this.pendienteActualizarClasificador = false;
	}
	
	
	
	public boolean isFotoCompletamenteDescrita() {
		return fotoCompletamenteDescrita;
	}



	public void setFotoCompletamenteDescrita(boolean fotoCompletamenteDescrita) {
		this.fotoCompletamenteDescrita = fotoCompletamenteDescrita;
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
	
	public void fotosCargadasUsuario() {
		this.fotosCargadasSubida = true;
	}
	
	public void fotoAlmacenada() {
		this.fotosCargadasSubida = false;
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
