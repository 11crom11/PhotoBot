package photoBot.OpenCV;

import java.util.HashMap;
import org.opencv.core.Scalar;

/**
 * El objetivo de esta clase estática es tener un listado de colores ordenados no modificable
 * que facilite la tarea de procesamiento de imagenes, de tal modo que si se analiza las caras
 * de una imagen y se recuadran, sabremos el orden en las que han sido recuadradas, y por lo tanto
 * facilitará la tarea de etiquetado de personas. (ej, la cara ROJA es la primera cara detectada, por
 * lo tanto, si etiqueto esta cara como valor "1", tengo que tener en cuenta que la cara se encuenta
 * almacenada en la primera posicion de la lista de caras obtenidas.
 *
 */
public final class ColoresCaras {
	
	private static String[] lColores;
	private static HashMap<String, Scalar> hColores;
	private static int numColores;

	static {
		lColores = new String[14];
		
		lColores[0] = "rojo";
		lColores[1] = "naranja";
		lColores[2] = "amarillo";
		lColores[3] = "verde";
		lColores[4] = "azul";
		lColores[5] = "cian";
		lColores[6] = "violeta";
		lColores[7] = "magenta";
		lColores[8] = "rosa";
		lColores[9] = "marron";
		lColores[10] = "negro";
		lColores[11] = "blanco";
		lColores[12] = "gris";
		lColores[13] = "oro";
		
		numColores = lColores.length;
		
		
		HashMap<String, Scalar> mapAux = new HashMap<>();
		mapAux.put(lColores[0], new Scalar(0, 0, 255));
		mapAux.put(lColores[0], new Scalar(0, 0, 255)); //rojo
		mapAux.put(lColores[1], new Scalar(0, 165, 255)); //naranja
		mapAux.put(lColores[2], new Scalar(0, 255, 255)); //amarillo
		mapAux.put(lColores[3], new Scalar(0, 255, 0)); //verde
		mapAux.put(lColores[4], new Scalar(255, 0, 0)); //azul
		mapAux.put(lColores[5], new Scalar(255, 255, 0)); //cian
		mapAux.put(lColores[6], new Scalar(211, 85, 168)); //violeta
		mapAux.put(lColores[7], new Scalar(255, 0, 255)); //magenta
		mapAux.put(lColores[8], new Scalar(203, 192, 255)); //rosa
		mapAux.put(lColores[9], new Scalar(19, 69, 139)); //marron
		mapAux.put(lColores[10], new Scalar(0, 0, 0)); //negro
		mapAux.put(lColores[11], new Scalar(255, 255, 255)); //blanco
		mapAux.put(lColores[12], new Scalar(128, 128, 128)); //gris
		mapAux.put(lColores[13], new Scalar(32, 165, 218)); //oro
		
		hColores = mapAux;
	}
	
	
	public ColoresCaras() {
		hColores = new HashMap<String, Scalar>();

	}
	
	/**
	 * Este metodo devuelve el nombre del color en la posicion i-esima de la lista de colores.
	 * 
	 * @param i Posicion del color que se desea obtener
	 * @return String con el color en la posicion i-esima.
	 */
	public static String getColor(int i) {
		return lColores[i % lColores.length];
	}
	
	/** 
	 * Este metodo devuelve un color en formato Scalar almacenado en la posicion i-esima de la lista de colores.
	 * 
	 * @param i Posicion del color que se desea obtener
	 * @return Scalar del color en la posicion i-esima.
	 */
	public static Scalar getScalarColor(int i) {
		return hColores.get(lColores[i % lColores.length]);
	}
	
	/**
	 * Este metodo devuelve el numero de colores que contiene esta clase
	 * 
	 * @return Valor numerico que indica en numero de colores.
	 */
	public static int getNumColores() {
		return numColores;
	}
	
}
