package photoBot.OpenCV;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class CarasDetectadas {

	private HashMap<String, Integer> lCaras;
	private MatOfRect carasDetectadas;
	
	public CarasDetectadas(MatOfRect caras, List<Integer> personas) {
		this.carasDetectadas = caras;
		this.rellenarHashMapPersonas(personas);
	}
	
	private void rellenarHashMapPersonas(List<Integer> personas) {
		
		int i = 0;
		
		for(Rect r : this.carasDetectadas.toArray()) {
			lCaras.put(ColoresCaras.getColor(i), personas.get(i));
			i++;
		}
	}
	
	public void actualizarPersonaHashMap(String color, String persona) {
		this.lCaras.put(color, persona);
	}
	
	public List<Mat> getListOfMat(String urlImagen){
		List<Mat> lMat = new ArrayList<Mat>();
		Mat imagen = Imgcodecs.imread(urlImagen);
		Imgproc.cvtColor(imagen, imagen, Imgproc.COLOR_BGR2GRAY);
		
		for(Rect r : this.carasDetectadas.toArray()) {
			lMat.add(new Mat(imagen, r));
		}
		
		return lMat;
	}
	
	public Mat getListOfLabels() {
		Mat labels = new Mat(carasDetectadas.toArray().length, 1, CvType.CV_32SC1);
		
		int i = 0;
		
		for(Rect r : this.carasDetectadas.toArray()) {
			labels.put(i, 0, lCaras.); //CAMBIAR -> PENSAR COMO USAR LOS NUMEROS QUE CORRESPONDEN A PERSONAS
		}
		
		return labels;
	}
}
