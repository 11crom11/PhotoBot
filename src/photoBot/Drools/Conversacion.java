package photoBot.Drools;

import photoBot.Imagen.Imagen;

/**
 * Esta clase contiene diversos atributos para controlar el estado de una conversacion
 *
 */
/**
 * @author d_dan
 *
 */
/**
 * @author d_dan
 *
 */
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
	private boolean fotoCompletamenteDescrita;                  // X
	private boolean personasNoReconocidasDescritas;             // X
	private boolean contextoDescrito;                           // X
	private boolean esperaConfirmacionFinalizarFoto;            // X   
	private boolean fotoSinPersonas;                            // X
	private boolean contextoEnUnaPalabra;                       // X
    ////////////////////////////////////////////////////////////////
	
	
	/**
	 * Construye un objeto Conversacion con todos los atributos indicializados
	 * como si una conversacion nueva se tratase
	 */
	public Conversacion() {
		this.saludo = false;
		this.subirFoto = false;
		this.buscarFoto = false;
		this.usuarioRegistrado = false;
		this.esperarDatosDelUsuario = false;
		this.fotoSinPersonas = true;
		
		this.fotosCargadasSubida = false;
		
		this.imagenPeticionInfo = null;
		this.pendienteActualizarClasificador = false;
		
		this.personasNoReconocidasDescritas = true;
		this.fotoCompletamenteDescrita = true;
		this.contextoDescrito = true;
		this.contextoEnUnaPalabra = false;
		this.esperaConfirmacionFinalizarFoto = false;
	}
	
	
	
	/**
	 * Comprobar si la ultima imagen subida contiene caras que pueden ser reconocidas
	 * @return True si en una imagen no han sido reconocidas ninguna cara
	 */
	public boolean isFotoSinPersonas() {
		return fotoSinPersonas;
	}



	/**
	 * @param fotoSinPersonas True si la foto no contiene personas que puedan ser reconocidas
	 */
	public void setFotoSinPersonas(boolean fotoSinPersonas) {
		this.fotoSinPersonas = fotoSinPersonas;
	}



	/**
	 * @return True si se espera recibir un SI o un NO para finalizar la decripcion de una imagen
	 * al haber sido subida al sistema
	 */
	public boolean isEsperaConfirmacionFinalizarFoto() {
		return esperaConfirmacionFinalizarFoto;
	}



	/**
	 * @param esperaConfirmacionFinalizarFoto True si se espera una confirmacion por el usuario
	 * para finalizar la descripcion de una imagen
	 */
	public void setEsperaConfirmacionFinalizarFoto(boolean esperaConfirmacionFinalizarFoto) {
		this.esperaConfirmacionFinalizarFoto = esperaConfirmacionFinalizarFoto;
	}



	/**
	 * @return True si el contexto ha sido descrito por parte del usuario
	 */
	public boolean isContextoDescrito() {
		return contextoDescrito;
	}



	/**
	 * @param contextoDescrito True si el contexto ha sido descrito por parte del usuario, False
	 * si no ha sido descrito
	 */
	public void setContextoDescrito(boolean contextoDescrito) {
		this.contextoDescrito = contextoDescrito;
	}



	/**
	 * Este metodo consulta si las personas que no habian sido reconocidas por el clasificador han sido
	 * descritas por el usuario
	 * @return True si las personas han sido descritas, False lo contrario
	 */
	public boolean isPersonasNoReconocidasDescritas() {
		return personasNoReconocidasDescritas;
	}



	/**
	 * @param personasNoReconocidasDescritas True si las personas no reconocidas han sido descritas,
	 *  False si no
	 */
	public void setPersonasNoReconocidasDescritas(boolean personasNoReconocidasDescritas) {
		this.personasNoReconocidasDescritas = personasNoReconocidasDescritas;
	}



	/**
	 * Este metodo devuelve un boolean en el caso de que una imagen este completamente descrita
	 * (personas y contexto)
	 * @return True si la imagen esta completamente descrita, False si faltan datos
	 */
	public boolean isFotoCompletamenteDescrita() {
		return fotoCompletamenteDescrita;
	}



	/**
	 * @param fotoCompletamenteDescrita True si una imagen esta completamente descrita, False si faltan datos
	 */
	public void setFotoCompletamenteDescrita(boolean fotoCompletamenteDescrita) {
		this.fotoCompletamenteDescrita = fotoCompletamenteDescrita;
	}



	/**
	 * Este metodo establece la imagen recibida por el bot por parte del usuario
	 * @param imagenPeticionInfo Imagen recibida
	 */
	public void setImagenPeticionInfo(Imagen imagenPeticionInfo){
		this.esperarDatosDelUsuario = true;
		this.imagenPeticionInfo = imagenPeticionInfo;
	}
	
	/**
	 * Este metodo establece valores a los atributos de la conversacion para que quede constancia
	 * de que una imagen ha sido registrada
	 */
	public void registradaInfoImagen(){
		this.esperarDatosDelUsuario = false;
		this.imagenPeticionInfo = null;
	}

	/**
	 * Este metodo establece en el atributo de espera de datos que ya se han recibido los datos
	 */
	public void infoRecibida(){
		this.esperarDatosDelUsuario = false;
	}
	
	/**
	 * @return ultima Imagen recibida por parte del Usuario
	 */
	public Imagen getImagenPeticionInfo(){
		return this.imagenPeticionInfo;
	}
	
	/**
	 * Este metodo establece en la conversacion cuando se esperan datos por parte del usuario
	 * @param esperarDatosDelUsuario True si se esperan datos, False si no se esperan datos
	 */
	public void setEsperarDatosDelUsuario(boolean esperarDatosDelUsuario) {
		this.esperarDatosDelUsuario = esperarDatosDelUsuario;
	}
	
	/**
	 * Este metodo comprueba si se esperan datos por parte del usuario
	 * @return True si se esperan datos por parte del usuario
	 */
	public boolean isEsperarDatosDelUsuario() {
		return esperarDatosDelUsuario;
	}
	
	/**
	 * Este metodo establece valores a los atributos de la conversacion para que quede constancia
	 * de que un usuario ha sido registrado
	 */
	public void registradaInfoDelUsuario(){
		this.esperarDatosDelUsuario = false;
		this.usuarioRegistrado = true;
	}

	/**
	 * Este metodo establece los atributos de la conversacion para indicar que se ha recibido un saludo
	 * por parte del usuario
	 */
	public void saludoRecibido() {
		this.saludo = true;
	}
	
	/**
	 * Este metodo establece los atributos de la conversacion para indicar que se ha recibido una imagen
	 */
	public void solicitudSubirFotoRecibida() {
		this.subirFoto = true;
	}
	
	
	/**
	 * Este metodo establece los atributos de la conversacion para indicar que se ha solicitado una busqueda
	 */
	public void solicitudBuscarFotoRecibida() {
		this.buscarFoto = true;
	}

	/**
	 * Este metodo comprueba si se ha recibido un saludo
	 * @return True si se ha recibido un saludo anteriormente
	 */
	public boolean isSaludo() {
		return saludo;
	}

	/**
	 * Este metodo comprueba si se ha recibido una foto para subir al sistema
	 * @return True si se ha recibido una imagen
	 */
	public boolean isSubirFoto() {
		return subirFoto;
	}

	/**
	 * Este metodo comprueba se se ha recibido una peticion de busqueda
	 * @return True si se ha recibido una peticion de busqueda
	 */
	public boolean isBuscarFoto() {
		return buscarFoto;
	}
	
	/**
	 * Este metodo comprueba si el usuario ha enviado una imagen via Telegram
	 * @return True si la imagen ha sido cargada
	 */
	public boolean isFotosCargadasSubida() {
		return fotosCargadasSubida;
	}
	
	/**
	 * Este metodo establce si la imagen enviada por el usuario ha sido cargada
	 * @param b
	 */
	public void setFotosCargadasSubida(boolean b) {
		this.fotosCargadasSubida = b;
	}
	
	/**
	 * Este metodo establece en la convseracion que el proceso de subir una imagen ha finalizado
	 */
	public void solicitudSubirFotoFinalizada() {
		this.subirFoto = false;
	}
	
	/**
	 * Este metodo establece en la conversacion que la imagen recibida por el usuario ha sido cargada en el sistema
	 */
	public void fotosCargadasUsuario() {
		this.fotosCargadasSubida = true;
	}
	
	/**
	 * Este metodo establece en la conversacion que la imagen recibida por el usuario ha sido almacenada en el sistema
	 */
	public void fotoAlmacenada() {
		this.fotosCargadasSubida = false;
	}
	
	/**
	 * Este metodo comprueba si el usuario de la conversacion esta registrado en el sistema
	 * @return True si el usuario esta registrado
	 */
	public boolean isUsuarioRegistrado() {
		return usuarioRegistrado;
	}

	/**
	 * Establece en la conversacion si un usuario esta registrado
	 * @param usuarioRegistrado True si el usuario esta registrado
	 */
	public void setUsuarioRegistrado(boolean usuarioRegistrado) {
		this.usuarioRegistrado = usuarioRegistrado;
	}
	
	/**
	 * Este metodo comprueba si el clasificador esta pendiente de ser actualizado
	 * @return True si el clasificador esta pendiente de ser actualizado
	 */
	public boolean isPendienteActualizarClasificador() {
		return pendienteActualizarClasificador;
	}

	/**
	 * Este metodo establece si el clasificador esta pendiente de ser actualizado
	 * @param pendienteActualizarClasificador True si el clasificador esta pendiente de ser actualizado
	 * @return 
	 */
	public void setPendienteActualizarClasificador(boolean pendienteActualizarClasificador) {
		this.pendienteActualizarClasificador = pendienteActualizarClasificador;
	}
	
	
	
	/**
	 * Este metodo comprueba si se va a recibir una palabra como contexto para añadirla al Gazettero
	 * @return True si se va a recibir una palabra como contexto
	 */
	public boolean isContextoEnUnaPalabra() {
		return contextoEnUnaPalabra;
	}



	/**
	 * Este metodo establece si se va a recibir una palabra como contexto
	 * @param contextoEnUnaPalabra
	 */
	public void setContextoEnUnaPalabra(boolean contextoEnUnaPalabra) {
		this.contextoEnUnaPalabra = contextoEnUnaPalabra;
	}



	/**
	 * Incicializa los atributos relacionados con la subida de una imagen
	 */
	public void procesoSubirImagenCompletado() {
		 setPendienteActualizarClasificador(false);
		 setContextoDescrito(true);
		 setEsperaConfirmacionFinalizarFoto(false);
		 setEsperarDatosDelUsuario(false);
		 setPersonasNoReconocidasDescritas(true);
		 //setImagenPeticionInfo(null);
		 this.subirFoto = false;
		 this.fotoCompletamenteDescrita = true;
		 this.fotoSinPersonas = true;
		 this.fotosCargadasSubida = false;
		 this.contextoEnUnaPalabra = false;
	}
}
