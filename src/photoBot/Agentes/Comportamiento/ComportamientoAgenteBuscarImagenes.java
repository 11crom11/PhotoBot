package photoBot.Agentes.Comportamiento;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import photoBot.BBDD.PhotoBotBBDD;

public class ComportamientoAgenteBuscarImagenes extends CyclicBehaviour {

	private Behaviour self;
	private PhotoBotBBDD bd;
	
	public ComportamientoAgenteBuscarImagenes(Agent a) {
		super(a);
		this.self = this;
		
		this.bd = new PhotoBotBBDD();
		
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

				if (comando == ConstantesComportamiento.BUSCAR_IMAGENES){
					//
				}
				else if(comando == ConstantesComportamiento.ANADIR_FILTRO_BUSQUEDA_ANTERIOR) {
					
				}
				
			} catch (UnreadableException e) {
				e.printStackTrace();
			}

		}
		
	}
	
	private void buscarImagenes(ACLMessage msj, HashMap<String, Object> contenido) {
		
	}
	
	/*VIEJO ... BORRAR CUANDO SE TENGAN IMPLEMENTADAS LAS FUNCIONES DE BUSQUEDA
	private void buscarTodasImagenes(ACLMessage msj) {
		List<String> list = new ArrayList<>();
		
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
	
	*/
}
