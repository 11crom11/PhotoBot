package photoBot.Agentes.Comportamiento;

import java.io.File;
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
import photoBot.Agentes.AgenteConversacionUsuario.PhotoBot;
import photoBot.OpenCV.CarasDetectadas;
import photoBot.OpenCV.GestorDeCaras;

public class ComportamientoAgenteGestionarCaras extends CyclicBehaviour{

	private ComportamientoAgenteGestionarCaras self;
	private GestorDeCaras gestorCaras;

	public ComportamientoAgenteGestionarCaras(Agent a) {
		super(a);
		this.self = this;
		this.gestorCaras = new GestorDeCaras();


	}

	@Override
	public void action() {
		ACLMessage msj = this.self.getAgent().receive();
		List<String> list = new ArrayList<>();

		if(msj != null){

			HashMap<String, Object> msjContent;
			int comando;	


			try {
				msjContent = (HashMap<String, Object>) msj.getContentObject();
				comando = (int) msjContent.get("COMANDO");

				if (comando == ConstantesComportamiento.RECONOCER_CARAS){
					enviarResultadosClasificacionAgenteConversacional(msjContent);
				}
			} catch (UnreadableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}



	}

	private void enviarResultadosClasificacionAgenteConversacional(HashMap<String, Object> msjContent){
		ACLMessage msj = this.self.getAgent().receive();
		msj = new ACLMessage(ACLMessage.INFORM);

		String idUsuario = (String) msjContent.get("ID");
		String urlImagen = (String) msjContent.get("URL_IMAGEN");

		CarasDetectadas carasDetectadas = this.gestorCaras.obtenerCarasImagen(urlImagen, idUsuario);
		List<Triple<String, Integer, Double>> tripletaColorEtiquetaProbabilidad = carasDetectadas.getListOfColorTagProbability();


		msjContent = new HashMap<String, Object>();

		msj.addReceiver(new AID(ConstantesComportamiento.AGENTE_CONVERSACION_USUARIO, AID.ISLOCALNAME));

		msjContent.put("COMANDO", ConstantesComportamiento.RESULTADO_RECONOCIMIENTO_IMAGEN);
		msjContent.put("RESULTADO_RECONOCIMIENTO", tripletaColorEtiquetaProbabilidad);

		try {
			msj.setContentObject(msjContent);
			getAgent().send(msj);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
