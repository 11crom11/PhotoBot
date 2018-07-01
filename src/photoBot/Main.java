package photoBot;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.*;
public class Main {
	
	public static void main(String[] args) {
		Runtime rt = Runtime.instance();
		ProfileImpl p = new ProfileImpl(false);
		
		AgentContainer container = rt.createMainContainer(p);
				
		//AgentController agentController = container.createNewAgent("Agent1", "jade.Agent1", null);
		try {
			
			//AGENTE CONVERSACION
			AgentController agentController = container.createNewAgent("AgenteConversacional", "photoBot.Agentes.AgenteConversacionUsuario", null);			
			agentController.start();
			
			//AGENTE SUBIR iMAGEN
			//agentController = container.createNewAgent("AgenteSubirImagen", "photoBot.Agentes.AgenteAlmacenarImagenes", null);			
			//agentController.start();
			
			//AGENTE BUSCAR iMAGEN
			agentController = container.createNewAgent("AgenteBuscarImagen", "photoBot.Agentes.AgenteBuscarImagen", null);			
			agentController.start();
			
			
		} catch (StaleProxyException e) {
			e.printStackTrace();
		}             
    }
}
