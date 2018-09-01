package photoBot.BBDD;

import org.apache.commons.lang3.tuple.Pair;

public class FiltroPersona extends Filtro {

	private String valor;
	
	public FiltroPersona(String campo, String nombrePersona) {
		super(campo);

		this.valor = nombrePersona;
	}
	
	@Override
	public Pair<String, String> getFilterValue() {
		return Pair.of(this.campoBBDD, this.valor);
	}

}
