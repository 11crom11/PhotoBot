package photoBot.BBDD;

import org.apache.commons.lang3.tuple.Pair;

/**
 * Clase que hereda de Filtro, y se especializa en eventos/contexto
 *
 */
public class FiltroEvento extends Filtro {

	private String valor;
	
	/**
	 * Este constructor construye un filtro de tipo evento
	 * @param campo campo de la bbdd al que se hace referenca
	 * @param evento valor del evento por el que se desea buscar
	 */
	public FiltroEvento(String campo, String evento) {
		super(campo);
		
		this.valor = evento;
	}

	@Override
	public Pair<String, String> getFilterValue() {
		return Pair.of(this.campoBBDD, this.valor);
	}

}
