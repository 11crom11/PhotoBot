package photoBot.OpenCV;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class CarasDetectadas {

	private HashMap<String, Pair<Integer, Double>> lCarasEtiquetadas;
	private MatOfRect carasDetectadas;
	private String urlImagen;
	/**
	 * Este constructor se encarga de crear un objeto CarasDetectadas. El objetivo de este objeto
	 * es almacer las caras detectadas en el detector de caras, así como el valor numérico que 
	 * corresponde a cada persona.
	 * 
	 * @param caras Caras obtenidas en el detector de caras
	 * @param personas Lista numerica que indica el valor numerico que corresponde a cada persona.
	 * @param url URL de la imagen que contiene las caras.
	 */
	public CarasDetectadas(MatOfRect caras, List<Pair<Integer, Double>> personas, String url) {
		this.carasDetectadas = caras;
		this.rellenarHashMapPersonas(personas);
		this.urlImagen = url;
	}
	
	private void rellenarHashMapPersonas(List<Pair<Integer, Double>> personas) {
		for (int i = 0; i < this.carasDetectadas.toArray().length; i++) {
			lCarasEtiquetadas.put(ColoresCaras.getColor(i), personas.get(i));
		}
	}
		
	/**
	 * Este metodo permite modificar el valor numerico de una persona indentificada
	 * por un color.
	 * 
	 * @param color Color con la que una cara ha sido detectada por el detector de caras.
	 * @param persona Nuevo valor de etiqueta que se quiere establecer a una persona.
	 */
	public void actualizarPersonaHashMap(String color, int persona) {

		Pair<Integer, Double> p = Pair.of(persona, 100.0);
		this.lCarasEtiquetadas.put(color, p);
	}
	
	/**
	 * Este metodo devuelve una lista de Mat. Cada Mat corresponde a una cara almacenada en
	 * carasDetectadas, y se devuelven en el mismo orden en el que están guardadas.
	 * 
	 * @return Devuelve una List<Mat>
	 */
	public List<Mat> getListOfMat(){
		List<Mat> lMat = new ArrayList<Mat>();
		Mat imagen = Imgcodecs.imread(this.urlImagen);
		Imgproc.cvtColor(imagen, imagen, Imgproc.COLOR_BGR2GRAY);
		
		for(Rect r : this.carasDetectadas.toArray()) {
			lMat.add(new Mat(imagen, r));
		}
		
		return lMat;
	}
	
	/**
	 * Este metodo devuelve un Mat con todas las etiquetas de todas las caras almacenadas en lCaras,
	 * en el mismo orden en el que se encuentran en carasDetectadas.
	 * 
	 * @return Devuelve una Mat con las etiquetas Label de todas las caras contenidas en este objeto, en el mismo orden.
	 */
	public Mat getListOfLabels() {
		Mat labels = new Mat(carasDetectadas.toArray().length, 1, CvType.CV_32SC1);
				
		for (int i = 0; i < this.carasDetectadas.toArray().length; i++) {
			labels.put(i, 0, this.lCarasEtiquetadas.get((ColoresCaras.getColor(i))).getLeft());
		}		
		
		return labels;
	}
}
