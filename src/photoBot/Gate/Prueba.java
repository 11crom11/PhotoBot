package photoBot.Gate;

import gate.util.GateException;

import java.io.IOException;
import java.util.List;

public class Prueba {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			ProcesadorLenguaje pl = new ProcesadorLenguaje();
			
			
			List<Etiqueta> etiquetas = pl.analizarTextoGate("Alejandro Pidal Gallego es el chico recuadrado de color verde, Verónica Morante Pindado es la de color cian y por último, en el recuadro de color negro se encuentra Antonio García Pérez.");
		
			System.out.println("ETIQUETAS ENCONTRADAS:");
			for(Etiqueta aux : etiquetas){		
				if(aux.getTipo().equals("Nombre_persona_color")) {
					System.out.println(aux.getTipo() + " --> " + aux.getNombre() + " - " + aux.getColor());
				}
				else {
					System.out.println(aux.getTipo() + " --> " + aux.getNombre());					
				}

			}
			System.out.println("----------------------");
			
			
			
			
			etiquetas = pl.analizarTextoGate("quiero ayer, bot.");
		
			System.out.println("ETIQUETAS ENCONTRADAS:");
			for(Etiqueta aux : etiquetas){
				System.out.println(aux.getTipo() + " --> " + aux.getNombre());
			}
			System.out.println("----------------------");
			
		
			
			
			
			etiquetas = pl.analizarTextoGate("blo");
			
			System.out.println("ETIQUETAS ENCONTRADAS:");
			for(Etiqueta aux : etiquetas){
				System.out.println(aux.getTipo() + " --> " + aux.getNombre());
			}
			System.out.println("----------------------");
		
		} catch (GateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

}
