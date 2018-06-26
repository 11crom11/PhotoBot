package photoBot.Agentes;

import photoBot.PhotoBot;
import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.OneShotBehaviour;

public class ComportamientoAgenteConversacionUsuario extends FSMBehaviour {

	private int estado;
	private PhotoBot photoBot;
	
	public ComportamientoAgenteConversacionUsuario(Agent a, PhotoBot photoBot) {
		super(a);
		
		this.photoBot = photoBot;
		this.estado = 0;
		
		registerFirstState(new OneShotBehaviour() {
			
			@Override
			public void action() {
				
				estado++;	
			}
			
			@Override
			public int onEnd() {
				return estado;
			}
			
		}, "ESTADO_0");
		
		
		registerState(new OneShotBehaviour() {
			
			@Override
			public void action() {
				System.out.println("Entro en el estado " + String.valueOf(estado));
				
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				long timestamp = System.currentTimeMillis();
				
				if(timestamp % 2 == 0){
					estado++;
				}
				
				System.out.println(String.valueOf(timestamp));
			}
			
			@Override
			public int onEnd() {
				return estado;
			}
		}, "ESTADO_1");
		
		registerState(new OneShotBehaviour() {
					
			
			@Override
			public void action() {
				System.out.println("Entro en el estado " + String.valueOf(estado));
				
				try {
					Thread.sleep(5000);
					estado++;
					
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
			@Override
			public int onEnd() {
				return estado;
			}
		}, "ESTADO_2");
		
		registerLastState(new OneShotBehaviour() {
			
			@Override
			public void action() {
				System.out.println("Entro en el estado " + String.valueOf(estado));
				
				try {
					Thread.sleep(1000);
					
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		}, "ESTADO_3");
		
		registerDefaultTransition("ESTADO_0", "ESTADO_1");
		registerTransition("ESTADO_1", "ESTADO_1", 1);
		registerTransition("ESTADO_1", "ESTADO_2", 2);
		registerTransition("ESTADO_2", "ESTADO_3", 3);

	}
	
	@Override
	public int onEnd() {
		System.out.println("FSM behaviour completed.");
		myAgent.doDelete();
		return super.onEnd();
	}

}
