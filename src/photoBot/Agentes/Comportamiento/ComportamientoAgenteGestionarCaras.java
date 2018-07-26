package photoBot.Agentes.Comportamiento;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
		// TODO Auto-generated method stub
		ACLMessage msj = this.self.getAgent().receive();
		List<String> list = new ArrayList<>();
		
		if(msj != null){
			
			HashMap<String, Object> msjContent;
			String comando;	
			
			try {
				
				msjContent = (HashMap<String, Object>) msj.getContentObject();
								
				switch ((String) msjContent.get("COMANDO")) {
				case "DETECTAR_CARAS":
					
					String idUsuario = (String) msjContent.get("ID");
					String urlImagen = (String) msjContent.get("URL_IMAGEN");
					
					CarasDetectadas carasDetectadas = this.gestorCaras.obtenerCarasImagen(urlImagen, idUsuario);
					
					
					
					break;
				
				case "...":
					
					
					break;
				default:
					break;
				}		
				
				
			} catch (UnreadableException e) {
				e.printStackTrace();
			}
			
		}
	}

}
