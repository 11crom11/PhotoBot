package photoBot.Drools;

import java.io.IOException;
import java.util.List;

import gate.util.GateException;
import photoBot.Agentes.Comportamiento.ConstantesComportamiento;
import photoBot.Gate.Etiqueta;
import photoBot.Gate.ProcesadorLenguaje;

public class ManejadorReglas {
	
	private int grupoActivado;

	public ManejadorReglas(){
		this.grupoActivado = ConstantesComportamiento.GRUPO_SALUDO;
	}
	
	public int getGrupoActivado() {
		return grupoActivado;
	}

	public void setGrupoActivado(int grupoActivado) {
		this.grupoActivado = grupoActivado;
	}
}
