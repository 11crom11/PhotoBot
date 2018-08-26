package photoBot.Drools;

import photoBot.Agentes.Comportamiento.ConstantesComportamiento;

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
