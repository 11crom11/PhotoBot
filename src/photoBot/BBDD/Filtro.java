package photoBot.BBDD;

import org.apache.commons.lang3.tuple.Pair;

public abstract class Filtro {
	protected String campoBBDD;
	
	public Filtro(String campo) {
		this.campoBBDD = campo;
	}
	
	public abstract Pair<String, ?> getFilterValue();
	
	public String getCampoBBDD() {
		return this.campoBBDD;
	}
}
