package photoBot.BBDD;

import org.apache.commons.lang3.tuple.Pair;

public class FiltroEvento extends Filtro {

	private String valor;
	
	public FiltroEvento(String campo, String evento) {
		super(campo);
		
		this.valor = evento;
	}

	@Override
	public Pair<String, String> getFilterValue() {
		return Pair.of(this.campoBBDD, this.valor);
	}

}
