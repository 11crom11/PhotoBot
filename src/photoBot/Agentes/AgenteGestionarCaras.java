package photoBot.Agentes;

import java.util.Date;

import jade.core.Agent;
import photoBot.Agentes.Comportamiento.ComportamientoAgenteGestionarCaras;

public class AgenteGestionarCaras extends Agent{
	
	private ComportamientoAgenteGestionarCaras comportamiento;
	
	
	@Override
	protected void setup() {
		super.setup();
		
        this.comportamiento = new ComportamientoAgenteGestionarCaras(this);
        
		addBehaviour(this.comportamiento);
	}
	
	@Override
	protected void takeDown(){
		System.out.println(new Date());
	}

}
