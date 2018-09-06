package photoBot;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;
import photoBot.Agentes.AgenteConversacionUsuario;
import photoBot.Agentes.Comportamiento.ConstantesComportamiento;

/**
 * Clase principal de la aplicacion PhotoBot. Arranca los componentes del sistema
 *
 */
public class Main {
	
	
	
	public static void main(String[] args) {

		System.out.println("ARRANCANDO PHOTOBOT...");
		System.out.println("TECLEE \"STOP\" PARA PARAR EL SISTEMA (SIN COMILLAS)");

		Runtime rt = Runtime.instance();
		ProfileImpl p = new ProfileImpl(false);
		
		AgentContainer container = rt.createMainContainer(p);
		AgentController agentControllerCONV = null;
		AgentController agentControllerALM = null;
		AgentController agentControllerBUS = null;
		AgentController agentControllerGES = null;
				
		//AgentController agentController = container.createNewAgent("Agent1", "jade.Agent1", null);
		try {
			
			//AGENTE CONVERSACION
			agentControllerCONV = container.createNewAgent(ConstantesComportamiento.AGENTE_CONVERSACION_USUARIO, "photoBot.Agentes.AgenteConversacionUsuario", null);			
			agentControllerCONV.start();
			
			//AGENTE SUBIR iMAGEN
			agentControllerALM = container.createNewAgent(ConstantesComportamiento.AGENTE_ALMACENAR_IMAGEN, "photoBot.Agentes.AgenteAlmacenarImagenes", null);			
			agentControllerALM.start();
			
			//AGENTE BUSCAR iMAGEN
			agentControllerBUS = container.createNewAgent(ConstantesComportamiento.AGENTE_BUSCAR_IMAGEN, "photoBot.Agentes.AgenteBuscarImagen", null);			
			agentControllerBUS.start();
			
			//AGENTE GESTIONAR CARAS
			agentControllerGES = container.createNewAgent(ConstantesComportamiento.AGENTE_GESTIONAR_CARAS, "photoBot.Agentes.AgenteGestionarCaras", null);			
			agentControllerGES.start();
			
		} catch (StaleProxyException e) {
			e.printStackTrace();
		}
	
		Scanner in = new Scanner(System.in);
		while(in.nextLine().equals("STOP") == false) {
			
		}
		
		System.out.println("INICIANDO PARADA DEL SISTEMA...");
		
		AgenteConversacionUsuario.pararBotTelegram();
		
		try {
			agentControllerALM.kill();
			agentControllerBUS.kill();
			agentControllerCONV.kill();
			agentControllerGES.kill();
			container.kill();
			rt.shutDown();
		} catch (ControllerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("AGENTES FINALIZADOS CORRECTAMENTE");
		System.out.println("APLICACIÓN FINALIZADA CORRECTAMENTE");
		System.exit(0);
    }
}
