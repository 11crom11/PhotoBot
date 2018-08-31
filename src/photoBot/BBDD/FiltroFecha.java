package photoBot.BBDD;

import java.util.Date;

import org.apache.commons.lang3.tuple.Pair;

public class FiltroFecha extends Filtro {
	private Pair<Date, Date> valor;
	
	public FiltroFecha(String campo, Pair<Date, Date> valor) {
		super(campo);

		this.valor = valor;
	}

	@Override
	public Pair<String, Pair<Date, Date>> getFilterValue() {

		return Pair.of(this.campoBBDD, this.valor);
	}
}
