package photoBot.Agentes;

import java.util.Date;

import jade.core.Agent;
import photoBot.Agentes.Comportamiento.ComportamientoAgenteGestionarCaras;

/**
 * Agente JADE cuya funcion es gestionar el procesamiento de las imagenes para detectar caras,
 * clasificarlas y entrenar el clasificador.
 *
 */
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
		System.out.println("PARADA AGENTE GESTIONAR CARAS");
	}

}
