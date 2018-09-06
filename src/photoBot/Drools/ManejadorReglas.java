package photoBot.Drools;

import java.io.IOException;
import java.util.List;

import gate.util.GateException;
import photoBot.Agentes.Comportamiento.ConstantesComportamiento;
import photoBot.Gate.Etiqueta;
import photoBot.Gate.ProcesadorLenguaje;

/**
 * Esta clase tiene como funcionalidad almacenar cual es grupo de reglas que actualmente
 * tiene le poder de ejecutarse
 *
 */
public class ManejadorReglas {
	
	private int grupoActivado;

	/**
	 * Este constructor construye un manejador de reglas inicializando el grupo de reglas
	 * que pueden activar al de GRUPO_EXISTENCIA_USUARIO
	 */
	public ManejadorReglas(){
		this.grupoActivado = ConstantesComportamiento.GRUPO_EXISTENCIA_USUARIO;
	}
	
	/**
	 * Este metodo comprueba cual es el grupo de reglas que puede activarse
	 * @return el grupo que actualmente puede activarse
	 */
	public int getGrupoActivado() {
		return grupoActivado;
	}

	/**
	 * Este metodo establece el grupo que podra activase en el procesador de reglas
	 * @param grupoActivado
	 */
	public void setGrupoActivado(int grupoActivado) {
		this.grupoActivado = grupoActivado;
	}
}
