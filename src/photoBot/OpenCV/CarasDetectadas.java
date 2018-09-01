package photoBot.OpenCV;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import jade.util.leap.Serializable;

public class CarasDetectadas {

	private HashMap<String, Pair<Integer, Double>> lCarasEtiquetadas;
	
	private MatOfRect carasDetectadas;
	private long natObjAddCarasDetectadas;
	
	
	private String urlImagen;
	
	public CarasDetectadas(){
		
	}
	
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
		this.natObjAddCarasDetectadas = this.carasDetectadas.getNativeObjAddr();
		
		this.urlImagen = url;
		this.lCarasEtiquetadas = new HashMap<String, Pair<Integer, Double>>();
		
		this.rellenarHashMapPersonas(personas);
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
	public void actualizarPersonaHashMap(String color, int persona, double prob) {

		Pair<Integer, Double> p = Pair.of(persona, prob);
		
		if(this.lCarasEtiquetadas.get(color) != null) {
			this.lCarasEtiquetadas.put(color, p);
		}	
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
		int i = 0;
		
		for(Rect r : this.carasDetectadas.toArray()) {
			int labelAux = this.lCarasEtiquetadas.get((ColoresCaras.getColor(i))).getLeft();
			
			if(labelAux != -2) {
				lMat.add(new Mat(imagen, r));
			}
			
			i++;
		}
		
		return lMat;
	}
	
	/**
	 * Este metodo devuelve un Mat con todas las etiquetas de todas las caras almacenadas en lCaras,
	 * en el mismo orden en el que se encuentran en carasDetectadas. Obvia las personas desconocidas.
	 * 
	 * @return Devuelve una Mat con las etiquetas Label de todas las caras contenidas en este objeto, en el mismo orden.
	 */
	public Mat getListOfLabels(int size) {
		Mat labels = new Mat(size, 1, CvType.CV_32SC1);
				
		for (int i = 0; i < this.carasDetectadas.toArray().length; i++) {
			int labelAux = this.lCarasEtiquetadas.get((ColoresCaras.getColor(i))).getLeft();
			
			if(labelAux != -2) {
				labels.put(i, 0, labelAux);
			}
		}		
		
		return labels;
	}
	
	public List<Triple<String, Integer, Double>> getListOfColorTagProbability(){
		List<Triple<String, Integer, Double>> ret = new ArrayList<>();
		
		lCarasEtiquetadas.forEach((key, value) -> {
			ret.add(Triple.of(key, value.getLeft(), value.getRight()));
		});
		
		return ret;
	}

	public boolean carasReconocidas(){
		boolean ok = this.lCarasEtiquetadas.containsValue(Pair.of(-1, 0.0));
		
		return ok;
	}
	
	public void actualizaMatOfRect(){
		this.carasDetectadas = MatOfRect.fromNativeAddr(this.natObjAddCarasDetectadas);
	}
	
	public HashMap<String, Pair<Integer, Double>> getlCarasEtiquetadas() {
		return lCarasEtiquetadas;
	}

	public void setlCarasEtiquetadas(HashMap<String, Pair<Integer, Double>> lCarasEtiquetadas) {
		this.lCarasEtiquetadas = lCarasEtiquetadas;
	}

	public MatOfRect getCarasDetectadas() {
		return carasDetectadas;
	}

	public void setCarasDetectadas(MatOfRect carasDetectadas) {
		this.carasDetectadas = carasDetectadas;
	}
	
	public List<Integer> getEtiquetas(){
		List<Integer> ret = new ArrayList<Integer>();
		
		for(Pair<Integer, Double> p : lCarasEtiquetadas.values()) {
			ret.add(p.getLeft());
		}
		
		return ret;
	}
}
