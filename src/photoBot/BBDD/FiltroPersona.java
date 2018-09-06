package photoBot.BBDD;

import org.apache.commons.lang3.tuple.Pair;

/**
 * Clase que hereda de Filtro, y se especializa en el nombre de personas
 *
 */
public class FiltroPersona extends Filtro {

	private String valor;
	
	/**
	 * Este constructor construye un filtro de tipo persona
	 * @param campo Campo de la bbdd al que se hace referencia
	 * @param nombrePersona Nombre de la persona sobre la que se quiere buscar
	 */
	public FiltroPersona(String campo, String nombrePersona) {
		super(campo);

		this.valor = nombrePersona;
	}
	
	@Override
	public Pair<String, String> getFilterValue() {
		return Pair.of(this.campoBBDD, this.valor);
	}

}
