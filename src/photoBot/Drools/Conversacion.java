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
	private boolean personasNoReconocidasDescritas;             // X
	private boolean contextoDescrito;                           // X
	private boolean esperaConfirmacionFinalizarFoto;            // X      
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
		
		this.personasNoReconocidasDescritas = true;
		this.fotoCompletamenteDescrita = true;
		this.contextoDescrito = true;
		this.esperaConfirmacionFinalizarFoto = false;
	}
	
	
	
	public boolean isEsperaConfirmacionFinalizarFoto() {
		return esperaConfirmacionFinalizarFoto;
	}



	public void setEsperaConfirmacionFinalizarFoto(boolean esperaConfirmacionFinalizarFoto) {
		this.esperaConfirmacionFinalizarFoto = esperaConfirmacionFinalizarFoto;
	}



	public boolean isContextoDescrito() {
		return contextoDescrito;
	}



	public void setContextoDescrito(boolean contextoDescrito) {
		this.contextoDescrito = contextoDescrito;
	}



	public boolean isPersonasNoReconocidasDescritas() {
		return personasNoReconocidasDescritas;
	}



	public void setPersonasNoReconocidasDescritas(boolean personasNoReconocidasDescritas) {
		this.personasNoReconocidasDescritas = personasNoReconocidasDescritas;
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
