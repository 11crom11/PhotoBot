package photoBot.Agentes.Comportamiento;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.tuple.Triple;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import photoBot.OpenCV.CarasDetectadas;
import photoBot.OpenCV.GestorDeCaras;

public class ComportamientoAgenteGestionarCaras extends CyclicBehaviour{

	private ComportamientoAgenteGestionarCaras self;
	private GestorDeCaras gestorCaras;
	private HashMap<Integer, CarasDetectadas> resultadosDeteccion;

	public ComportamientoAgenteGestionarCaras(Agent a) {
		super(a);
		this.self = this;
		this.gestorCaras = new GestorDeCaras();
		this.resultadosDeteccion = new HashMap<Integer, CarasDetectadas>();
	}

	@Override
	public void action() {
		ACLMessage msj = this.self.getAgent().receive();

		if(msj != null){

			HashMap<String, Object> msjContent;
			int comando;	


			try {
				msjContent = (HashMap<String, Object>) msj.getContentObject();
				comando = (int) msjContent.get("COMANDO");

				if (comando == ConstantesComportamiento.RECONOCER_CARAS){
					this.reconocerCarasYenviarResultadosClasificacionAgenteConversacional(msjContent);
				}
				else if (comando == ConstantesComportamiento.ACTUALIZAR_CAMPO_CARAS_DETECTADAS) {
					this.actualizarCampoCarasDetectadas(msjContent);
				}
				else if (comando == ConstantesComportamiento.ACTUALIZAR_CLASIFICADOR) {
					this.actualizarClasificador(msjContent);
				}
				else if (comando == ConstantesComportamiento.SOLICITAR_LISTADO_PERSONAS_ACTUALIZAR_IMAGEN) {
					this.generarListadoPersonasEtiqueta(msjContent);
				}
			} catch (UnreadableException e) {
				e.printStackTrace();
			}

		}



	}

	private void generarListadoPersonasEtiqueta(HashMap<String, Object> msjContent) {
		Integer idUsuario = (Integer) msjContent.get("ID");
		
		List<Integer> lEtiquetas = this.resultadosDeteccion.get(idUsuario).getEtiquetas();
		
		ACLMessage msj = this.self.getAgent().receive();
		msj = new ACLMessage(ACLMessage.INFORM);

		msjContent = new HashMap<String, Object>();
		
		msj.addReceiver(new AID(ConstantesComportamiento.AGENTE_CONVERSACION_USUARIO, AID.ISLOCALNAME));
		msjContent.put("COMANDO", ConstantesComportamiento.LISTADO_PERSONAS_IMAGEN);
		msjContent.put("LISTA", lEtiquetas);
		
		try {
			msj.setContentObject(msjContent);
			getAgent().send(msj);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void reconocerCarasYenviarResultadosClasificacionAgenteConversacional(HashMap<String, Object> msjContent){
		ACLMessage msj = this.self.getAgent().receive();
		msj = new ACLMessage(ACLMessage.INFORM);

		String idUsuario = (String) msjContent.get("ID");
		String urlImagen = (String) msjContent.get("URL_IMAGEN");
		long fechaSubida = (long) msjContent.get("FECHA_SUBIDA");

		
		//1- RECONOCER CARAS
		CarasDetectadas carasDetectadas = this.gestorCaras.obtenerCarasImagen(urlImagen, idUsuario);
		List<Triple<String, Integer, Double>> tripletaColorEtiquetaProbabilidad = carasDetectadas.getListOfColorTagProbability();
		
		//2- ALMACENAR EL RESULTADO DE LA DETECCION PARA UNA FUTURA ACTUALIZACION DEL ALGUN CLASIFICADOR
		//msjContent.put("OBJETO_CARAS_DETECTADAS", carasDetectadas);
		System.out.println("HE ALMACENADO EL RESULTADO DE RECONOCIMIENTO EN LA MEMORIA DEL AGENTE GESTOR DE CARAS");
		this.resultadosDeteccion.put(Integer.valueOf(idUsuario), carasDetectadas);

		//3- ENVIAR AL AGENTE CONVERSACIONAL EL RESULTADO DEL RECONOCIMIENTO
		msjContent = new HashMap<String, Object>();
		
		msj.addReceiver(new AID(ConstantesComportamiento.AGENTE_CONVERSACION_USUARIO, AID.ISLOCALNAME));
		msjContent.put("COMANDO", ConstantesComportamiento.RESULTADO_RECONOCIMIENTO_IMAGEN);
		msjContent.put("URL_IMAGEN", urlImagen);
		msjContent.put("FECHA_SUBIDA", fechaSubida);
		msjContent.put("RESULTADO_RECONOCIMIENTO", tripletaColorEtiquetaProbabilidad);
		
		try {
			msj.setContentObject(msjContent);
			getAgent().send(msj);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void actualizarCampoCarasDetectadas(HashMap<String, Object> msjContent) {
		int idUsuario = (int) msjContent.get("ID");
		String color = (String) msjContent.get("COLOR");
		int etiqueta = (int) msjContent.get("ETIQUETA");
		double confidence = (double) msjContent.get("CONFIDENCE");
		
		this.resultadosDeteccion.get(idUsuario).actualizarPersonaHashMap(color, etiqueta, confidence);
		System.out.println("HE MODIFICADO EL VALOR DE LA PERSONA DE COLOR " + color + " a " + etiqueta);
		
		comprobarTotalidadDescripcionPersonasYactualizar(idUsuario);
	}
	
	private void comprobarTotalidadDescripcionPersonasYactualizar(int idUsuario) {
		boolean carasNoReconocidas = this.resultadosDeteccion.get(idUsuario).carasReconocidas();
		
		if (carasNoReconocidas == false) {		
			//enviar mensaje al agente conversacional indicandole que modifique el objeto conversacion
			HashMap<String, Object> msjContent = new HashMap<String, Object>();
			
			msjContent.put("COMANDO", ConstantesComportamiento.TODAS_PERSONAS_IMAGEN_DESCRITAS);
							
			ACLMessage msj = new ACLMessage(ACLMessage.INFORM);
			msj.addReceiver(new AID(ConstantesComportamiento.AGENTE_CONVERSACION_USUARIO, AID.ISLOCALNAME));
			
			try {
				msj.setContentObject((Serializable)msjContent);
				getAgent().send(msj);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void actualizarClasificador(HashMap<String, Object> msjContent) {
		int idUsuario = (int) msjContent.get("ID");
		
		CarasDetectadas caras = this.resultadosDeteccion.get(idUsuario);
		
		this.gestorCaras.actualizarClasificadorPersonalizado(idUsuario, caras);
		System.out.println("HE ACTUALIZADO EL CLASIFICADOR");
		//this.resultadosDeteccion.remove(idUsuario); //a veces falla carasReconocidas()
	}
}
