package photoBot.Agentes;

import photoBot.Agentes.AgenteConversacionUsuario.PhotoBot;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.OneShotBehaviour;

public class ComportamientoAgenteConversacionUsuario extends FSMBehaviour {

	private PhotoBot photoBot;
	
	public ComportamientoAgenteConversacionUsuario(Agent a, PhotoBot photoBot) {
		super(a);
		this.photoBot = photoBot;
		
		registerFirstState(new OneShotBehaviour() {
			private int estado = 0;

			@Override
			public void action() {
				if(photoBot.getUserID() != null){ //Si hay usuario hablando al bot
					photoBot.enviarMensajeTextoAlUsuario("Estoy en el Estado_Inicial");
					this.estado = 4;
				}
				
			}
			
			@Override
			public int onEnd() {
				return this.estado;
			}
			
		}, "ESTADO_INICIAL");
		
		
		registerState(new OneShotBehaviour() {
			private int estado = 1;

			@Override
			public void action() {
				System.out.println("Entro en el estado " + String.valueOf(estado));
				
				
				//SI QUIERO BUSCAR FOTOS
				if(photoBot.hayMensaje() && photoBot.getMensaje().equalsIgnoreCase("/dame")){
					photoBot.devolverTodasLasImagenesDelUsuario();
					
					photoBot.mensajeLeido();
					this.estado = 5;
				}
				else if(photoBot.hayMensajeFoto()){
					//SI QUIERO CARGAR/SUBIR FOTOS
					boolean ok = false;
					String msj = "";
					List<PhotoSize> lFotos = update.getMessage().getPhoto();
					
					ok = this.guardarFotoEnServidor(lFotos);
					
					if(ok){
						msj = "Ya he recibido y almacenado correctamente tus imágenes.";
					}
					else{
						msj = "Ha ocurrido un error :( ... ¿Puedes mandarme de nuevo las imágenes?";
					}
					
					enviarMensajeTextoAlUsuario(msj);
					
					photoBot.mensajeLeido();
					this.estado = 6;
				}
				

			
			}
			
			@Override
			public int onEnd() {
				return estado;
			}
			
		}, "ESTADO_ESPERANDO_PETICICION");
		
		registerState(new OneShotBehaviour() {
			private int estado = 2;
	
			
			@Override
			public void action() {
				System.out.println("Entro en el estado " + String.valueOf(estado));
				
				//SI HE FINALIZADO BUSQUEDA, VUELVO AL PRINCIPIO
				this.estado = 7;

				
			}
			
			@Override
			public int onEnd() {
				return estado;
			}
		}, "ESTADO_BUSCAR");
		
		registerLastState(new OneShotBehaviour() {
			private int estado = 3;

			@Override
			public void action() {
				System.out.println("Entro en el estado " + String.valueOf(estado));
				
				//SI HE FINALIZADO SUBIR IMAGENES, VUELVO AL PRINCIPIO
				this.estado = 8;
			}
			
		}, "ESTADO_SUBIR");
		
		//registerDefaultTransition("ESTADO_INICIAL", "ESTADO_ESPERANDO_PETICICION");
		registerTransition("ESTADO_INICIAL", "ESTADO_INICIAL", 0);
		registerTransition("ESTADO_ESPERANDO_PETICICION", "ESTADO_ESPERANDO_PETICICION", 1); //SE QUEDA AQUI HASTA QUE RECIBA MENSAJE
		registerTransition("ESTADO_BUSCAR", "ESTADO_BUSCAR", 2); //VUELTA ESTADO INICIAL
		registerTransition("ESTADO_SUBIR", "ESTADO_SUBIR", 3); //VUELTA ESTADO INICIAL

		
		registerTransition("ESTADO_INICIAL", "ESTADO_ESPERANDO_PETICICION", 4);
		registerTransition("ESTADO_ESPERANDO_PETICICION", "ESTADO_BUSCAR", 5); //SI EL USUARIO QUIERE BUSCAR IMAGENES
		registerTransition("ESTADO_ESPERANDO_PETICICION", "ESTADO_SUBIR", 6); //SI EL USUARIO QUIERE SUBIR IMAGENES
		registerTransition("ESTADO_BUSCAR", "ESTADO_ESPERANDO_PETICICION", 7); //VUELTA ESTADO INICIAL
		registerTransition("ESTADO_SUBIR", "ESTADO_ESPERANDO_PETICICION", 8); //VUELTA ESTADO INICIAL
		

	}
	
	@Override
	public int onEnd() {
		System.out.println("FSM behaviour completed.");
		myAgent.doDelete();
		return super.onEnd();
	}
	
	public Behaviour getEstado() {
		return getCurrent();
	}
}
