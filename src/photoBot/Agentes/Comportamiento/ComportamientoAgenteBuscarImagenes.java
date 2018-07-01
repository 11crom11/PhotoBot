package photoBot.Agentes.Comportamiento;

import jade.core.behaviours.FSMBehaviour;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.xhtmlrenderer.swing.ImageReplacedElement;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
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
			System.out.print("AgenteBUscarImagenes ha recibido el siguiente mensaje: " + userID);
			
			try {
				Files.list(Paths.get("./galeria/" + userID)).forEach((imagen)->{
					System.out.println(imagen.toString());
					list.add(imagen.toString());
					
				});
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			ACLMessage reply = msj.createReply();
			try {
				reply.setContentObject((Serializable) list);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			getAgent().send(reply);
				//sendPhotoRequest.setNewPhoto(new java.io.File(imagen.toString()));
		}
		
	}
}
