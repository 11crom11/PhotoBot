package photoBot.Agentes;

import java.util.Date;

import jade.core.Agent;
import photoBot.Agentes.Comportamiento.ComportamientoAgenteBuscarImagenes;

public class AgenteBuscarImagen extends Agent {
	
	private ComportamientoAgenteBuscarImagenes comportamiento;
	
	@Override
	protected void setup() {
		super.setup();
		
        this.comportamiento = new ComportamientoAgenteBuscarImagenes(this);
        
                
		addBehaviour(this.comportamiento);
	}
	
	@Override
	protected void takeDown(){
		System.out.println(new Date());
	}
}
