package photoBot.Gate;

import gate.util.GateException;

import java.io.IOException;
import java.util.List;

public class Prueba {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			ProcesadorLenguaje pl = new ProcesadorLenguaje();
			
			
			List<Etiqueta> etiquetas = pl.analizarTextoGate("hola, quiero subir esta foto!");
		
			System.out.println("ETIQUETAS ENCONTRADAS:");
			for(Etiqueta aux : etiquetas){		
				System.out.println(aux.getTipo() + "--> " + aux.getNombre());
			}
			System.out.println("----------------------");
			
			etiquetas = pl.analizarTextoGate("quiero ayer, bot.");
		
			System.out.println("ETIQUETAS ENCONTRADAS:");
			for(Etiqueta aux : etiquetas){
				System.out.println(aux.getTipo() + "--> " + aux.getNombre());
			}
			System.out.println("----------------------");
		
		} catch (GateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

}
