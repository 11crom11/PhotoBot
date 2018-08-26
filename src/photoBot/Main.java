package photoBot;

import java.io.File;
import java.io.IOException;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import photoBot.Agentes.Comportamiento.ConstantesComportamiento;
public class Main {
	
	public static void main(String[] args) {


		Runtime rt = Runtime.instance();
		ProfileImpl p = new ProfileImpl(false);
		
		AgentContainer container = rt.createMainContainer(p);
				
		//AgentController agentController = container.createNewAgent("Agent1", "jade.Agent1", null);
		try {
			
			//AGENTE CONVERSACION
			AgentController agentController = container.createNewAgent(ConstantesComportamiento.AGENTE_CONVERSACION_USUARIO, "photoBot.Agentes.AgenteConversacionUsuario", null);			
			agentController.start();
			
			//AGENTE SUBIR iMAGEN
			agentController = container.createNewAgent(ConstantesComportamiento.AGENTE_ALMACENAR_IMAGEN, "photoBot.Agentes.AgenteAlmacenarImagenes", null);			
			agentController.start();
			
			//AGENTE BUSCAR iMAGEN
			agentController = container.createNewAgent(ConstantesComportamiento.AGENTE_BUSCAR_IMAGEN, "photoBot.Agentes.AgenteBuscarImagen", null);			
			agentController.start();
			
			//AGENTE GESTIONAR CARAS
			agentController = container.createNewAgent(ConstantesComportamiento.AGENTE_GESTIONAR_CARAS, "photoBot.Agentes.AgenteGestionarCaras", null);			
			agentController.start();			
			
		} catch (StaleProxyException e) {
			e.printStackTrace();
		}             
    }
}
