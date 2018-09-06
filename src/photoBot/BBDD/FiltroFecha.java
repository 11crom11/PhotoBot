package photoBot.BBDD;

import java.util.Date;

import org.apache.commons.lang3.tuple.Pair;

/**
 * Clase que hereda de Filtro, y se especializa en fechas
 *
 */
public class FiltroFecha extends Filtro {
	private Pair<Date, Date> valor;
	
	/**
	 * Este constructor construye un filtro de tipo fecha
	 * @param campo Campo de la bbdd al que se hace referencia
	 * @param valor Fecha sobre la que se quiere buscar en la bbdd
	 */
	public FiltroFecha(String campo, Pair<Date, Date> valor) {
		super(campo);

		this.valor = valor;
	}

	@Override
	public Pair<String, Pair<Date, Date>> getFilterValue() {

		return Pair.of(this.campoBBDD, this.valor);
	}
}
