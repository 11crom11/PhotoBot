package photoBot.Agentes;

import java.util.Date;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import photoBot.PhotoBot;
import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;

public class AgenteConversacionUsuario extends Agent {
	
	private PhotoBot photoBot;
	private TelegramBotsApi apiTelegram;
	
	private ComportamientoAgenteConversacionUsuario comportamiento;
	
	@Override
	protected void setup() {
		super.setup();
		
        ApiContextInitializer.init();

        this.apiTelegram = new TelegramBotsApi();
        this.photoBot = new PhotoBot();
        this.comportamiento = new ComportamientoAgenteConversacionUsuario(this, this.photoBot);
        
        try {
            this.apiTelegram.registerBot(this.photoBot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        
		addBehaviour(this.comportamiento);
	}
	
	@Override
	protected void takeDown(){
		System.out.println(new Date());
	}

}