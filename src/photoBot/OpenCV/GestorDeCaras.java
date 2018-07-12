package photoBot.OpenCV;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import jade.util.leap.ArrayList;

public class GestorDeCaras {
	
	private CascadeClassifier clasificador;
	private String[] lColores;
	
	private final HashMap<String, Scalar> hColores;
	
	public GestorDeCaras() {

		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		String urlXml = "C:\\hlocal\\opencv\\sources\\data\\lbpcascades\\lbpcascade_frontalface.xml";
		this.clasificador = new CascadeClassifier(urlXml);
		
		this.hColores = new HashMap<String, Scalar>();
		this.lColores = new String[14];
		this.rellenarColores();
		
		
	}
	
	private void rellenarColores() {
		this.lColores[0] = "rojo";
		this.lColores[1] = "naranja";
		this.lColores[2] = "amarillo";
		this.lColores[3] = "verde";
		this.lColores[4] = "azul";
		this.lColores[5] = "cian";
		this.lColores[6] = "violeta";
		this.lColores[7] = "magenta";
		this.lColores[8] = "rosa";
		this.lColores[9] = "marron";
		this.lColores[10] = "negro";
		this.lColores[11] = "blanco";
		this.lColores[12] = "gris";
		this.lColores[13] = "oro";
		
		this.hColores.put(lColores[0], new Scalar(0, 0, 255)); //rojo
		this.hColores.put(lColores[1], new Scalar(0, 165, 255)); //naranja
		this.hColores.put(lColores[2], new Scalar(0, 255, 255)); //amarillo
		this.hColores.put(lColores[3], new Scalar(0, 255, 0)); //verde
		this.hColores.put(lColores[4], new Scalar(255, 0, 0)); //azul
		this.hColores.put(lColores[5], new Scalar(255, 255, 0)); //cian
		this.hColores.put(lColores[6], new Scalar(211, 85, 168)); //violeta
		this.hColores.put(lColores[7], new Scalar(255, 0, 255)); //magenta
		this.hColores.put(lColores[8], new Scalar(203, 192, 255)); //rosa
		this.hColores.put(lColores[9], new Scalar(19, 69, 139)); //marron
		this.hColores.put(lColores[10], new Scalar(0, 0, 0)); //negro
		this.hColores.put(lColores[11], new Scalar(255, 255, 255)); //blanco
		this.hColores.put(lColores[12], new Scalar(128, 128, 128)); //gris
		this.hColores.put(lColores[13], new Scalar(32, 165, 218)); //oro
	}
	
	public MatOfRect detectarCarasImagen(String urlImagen) {
		
		MatOfRect carasDetectadas = new MatOfRect();
		Mat imagen = Imgcodecs.imread(urlImagen);
		
		this.clasificador.detectMultiScale(imagen, carasDetectadas);
		
		return carasDetectadas;
	}
	
	public void generarImagenCarasRecuadradas(String urlImagen, MatOfRect carasDetectadas) {
		
		Mat imagen = Imgcodecs.imread(urlImagen);
		int i = 0;
		
		for(Rect rect : carasDetectadas.toArray()) {
			
			Imgproc.rectangle(imagen, 
					new Point(rect.x, rect.y), 
					new Point(rect.x + rect.width, rect.y + rect.height), 
					hColores.get(lColores[i]),
					8);
			
			i = (i + 1) % (lColores.length - 1);
		}
		
		Imgcodecs.imwrite("./galeria/prueba.jpeg", imagen);
		
	}
	
	
	
	
}
