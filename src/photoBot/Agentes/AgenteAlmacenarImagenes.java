package photoBot.Agentes;

import java.util.Date;

import jade.core.Agent;
import photoBot.Agentes.Comportamiento.ComportamientoAgenteAlmacenarImagen;

public class AgenteAlmacenarImagenes extends Agent{
	
	private ComportamientoAgenteAlmacenarImagen comportamiento;
	
	
	@Override
	protected void setup() {
		super.setup();
		
        this.comportamiento = new ComportamientoAgenteAlmacenarImagen(this);
        
		addBehaviour(this.comportamiento);
	}
	
	@Override
	protected void takeDown(){
		System.out.println("PARADA AGENTE ALMACENAR IMAGEN");
	}

}
