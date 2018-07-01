package photoBot.Agentes.Comportamiento;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.OneShotBehaviour;

public class ComportamientoAgenteAlmacenarImagen extends FSMBehaviour {
	
	public ComportamientoAgenteAlmacenarImagen(Agent a) {
		super(a);
		
		registerFirstState(new OneShotBehaviour() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			private int estado = 0;

			@Override
			public void action() {
				
				
			}
			
			@Override
			public int onEnd() {
				return this.estado;
			}
			
		}, "ESTADO_0");
		
		
		registerState(new OneShotBehaviour() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			private int estado = 1;

			@Override
			public void action() {
				
				
			}
			
			@Override
			public int onEnd() {
				return estado;
			}
			
		}, "ESTADO_1");
		
		registerTransition("ESTADO_0", "ESTADO_1", 0);
	}
}
