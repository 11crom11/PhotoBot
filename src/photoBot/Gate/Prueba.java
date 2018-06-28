package photoBot.Gate;

import gate.util.GateException;

import java.io.IOException;
import java.util.Set;

public class Prueba {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			ProcesadorLenguaje pl = new ProcesadorLenguaje();
			Set<String> etiquetas = pl.analizarTextoGate("hola, quiero las fotos de ayer en Madrid");
		
			for(String aux : etiquetas){
				System.out.println(aux);
			}
		
		} catch (GateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

}
