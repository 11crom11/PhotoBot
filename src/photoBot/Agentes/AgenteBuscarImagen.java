package photoBot.Agentes;

import jade.core.Agent;
import photoBot.Agentes.Comportamiento.ComportamientoAgenteBuscarImagenes;

/**
 * Agente JADE cuya funcion es buscar imágenes en la base de datos para entregarselas 
 * al usuario a partir de unos criterios de búsqueda.
 *
 */
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
		System.out.println("PARADA DE AGENTE BUSCAR IMAGEN");
	}
}
