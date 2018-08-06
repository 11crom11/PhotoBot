package photoBot.Agentes.Comportamiento;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class ComportamientoAgenteBuscarImagenes extends CyclicBehaviour {

	private Behaviour self;
	
	public ComportamientoAgenteBuscarImagenes(Agent a) {
		super(a);
		this.self = this;
		
		
	}

	@Override
	public void action() {
		ACLMessage msj = this.self.getAgent().receive();
		List<String> list = new ArrayList<>();
		
		if(msj != null){
			String userID = msj.getContent();
			System.out.print("AgenteBuscarImagenes ha recibido el siguiente mensaje: " + userID);
			
			try {
				Files.list(Paths.get("./galeria/" + userID)).forEach((imagen)->{
					System.out.println(imagen.toString());
					list.add(imagen.toString());
					
				});
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			HashMap<String, Object> msjContent = new HashMap<String, Object>();
			
			msjContent.put("COMANDO", ConstantesComportamiento.ENTREGAR_IMG_ENCONTRADAS);
			msjContent.put("LISTA", list);
			
			ACLMessage reply = msj.createReply();
			getAgent().send(reply);
				//sendPhotoRequest.setNewPhoto(new java.io.File(imagen.toString()));
		}
		
	}
}
