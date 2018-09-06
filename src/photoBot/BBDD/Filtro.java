package photoBot.BBDD;

import org.apache.commons.lang3.tuple.Pair;

/**
 * Esta clase abstracta almacena el campo al que hace referencia en la base de datos el filtro creado
 *
 */
public abstract class Filtro {
	protected String campoBBDD;
	
	/**
	 * @param campo campo de la bbdd a la que hace referencia el filtro
	 */
	public Filtro(String campo) {
		this.campoBBDD = campo;
	}
	
	/**
	 * Este metodo devuelve un par campo-valor donde el campo es el atributo de la bbdd al que se hace referencia
	 * y el valor es por lo que se desea buscar
	 * @return
	 */
	public abstract Pair<String, ?> getFilterValue();
	
	/**
	 * @return El campo de la bbdd
	 */
	public String getCampoBBDD() {
		return this.campoBBDD;
	}
}
