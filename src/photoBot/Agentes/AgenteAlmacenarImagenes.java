package photoBot.Agentes;

import java.util.Date;

import jade.core.Agent;
import photoBot.Agentes.Comportamiento.ComportamientoAgenteAlmacenarImagen;



/**
 * Agente JADE cuya funcion es almacenar los archivos de imagen que se recibe del usuario
 * y actualizar la base de datos con la información de las mismas.
 *
 */
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
