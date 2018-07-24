package photoBot.OpenCV;

import java.awt.Color;
import java.io.File;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.MatOfRect2d;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.face.FaceRecognizer;
import org.opencv.face.LBPHFaceRecognizer;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import jade.util.leap.ArrayList;

public class GestorDeCaras {
	
	private CascadeClassifier clasificadorCaras;
	
	public GestorDeCaras() {

		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		String urlXml = "C:\\hlocal\\opencv\\sources\\data\\lbpcascades\\lbpcascade_frontalface_improved.xml";
		this.clasificadorCaras = new CascadeClassifier(urlXml);
		
	}
	
	
	public MatOfRect detectarCarasImagen(String urlImagen) {
		
		MatOfRect carasDetectadas = new MatOfRect();
		Mat imagen = Imgcodecs.imread(urlImagen);
		
		this.clasificadorCaras.detectMultiScale(imagen, carasDetectadas);
		
		return carasDetectadas;
	}
	
	public void generarImagenCarasRecuadradas(String urlImagen, MatOfRect carasDetectadas) {
		
		Mat imagen = Imgcodecs.imread(urlImagen);
		int i = 0;
		
		for(Rect rect : carasDetectadas.toArray()) {
			
			/*Imgproc.rectangle(imagen, 
					new Point(rect.x, rect.y), 
					new Point(rect.x + rect.width, rect.y + rect.height), 
					hColores.get(lColores[i]),
					8);
					
			*/
			Imgproc.rectangle(imagen, 
					new Point(rect.x, rect.y), 
					new Point(rect.x + rect.width, rect.y + rect.height), 
					ColoresCaras.getScalarColor(i),
					8);
			
			i = (i + 1) % (ColoresCaras.getNumColores() - 1);
			
			
		}
		
		Imgcodecs.imwrite("./galeria/prueba.jpeg", imagen);
		
	}
	
	private void actualizarClasificadorPersonalizado(int idClasificador, List<Mat> lMat, Mat labels) {
	
		FaceRecognizer lbphClasificador = LBPHFaceRecognizer.create();
		lbphClasificador.read("./clasificadores/" + idClasificador);
		
		lbphClasificador.update(lMat, labels);
		
		lbphClasificador.save("./clasificadores/" + idClasificador);
	}
	
	private void crearClasificadorPersonalizado(int idUsuario, List<Mat> lMat, Mat labels) {
		FaceRecognizer lbphClasificador = LBPHFaceRecognizer.create();
		
		lbphClasificador.train(lMat, labels);
		
		lbphClasificador.save("./clasificadores/" + idUsuario);
	}
	
	/*public void entrenaClasificador(CarasDetectadas caras, int idUsuario) {
		File clasificadorTmp = new File("./clasificadores/" + idUsuario);
		
		if(clasificadorTmp.exists()){
			this.actualizarClasificadorPersonalizado(idUsuario, lMat, labels);
		}
	}*/
	
	
	public void entrenaClasificadorr(String urlImagen, MatOfRect carasDetectadas) {
		
		Mat imagen = Imgcodecs.imread(urlImagen);
		Imgproc.cvtColor(imagen, imagen, Imgproc.COLOR_BGR2GRAY);
		
		FaceRecognizer lbphRecognizer = LBPHFaceRecognizer.create();
		
		List<Mat> lMat = new java.util.ArrayList<Mat>();
		Mat labels = new Mat(carasDetectadas.toArray().length, 1, CvType.CV_32SC1);

		int i = 0;
		for(Rect r: carasDetectadas.toArray()) {
			lMat.add(new Mat(imagen, r));
			//labels.put(i, i); //La segunda i es la clase (lo que viene a ser la persona)
			//i++;
		}
		
		labels.put(0, 0, 1); //Monica rojo
		labels.put(1, 0, 1); //Monica naranja
		labels.put(2, 0, 1); //Monica amarillo
		labels.put(3, 0, 1); //Monica verde
		labels.put(4, 0, 2); //Matias azul
		labels.put(5, 0, 2); //Matias cian
		labels.put(6, 0, 2); //Matias violeta
		labels.put(7, 0, 1); //Monica magenta
		labels.put(8, 0, 2); //Matias rosa
		labels.put(9, 0, 2); //Matias marron
		labels.put(10, 0, 2); //Matias negro
			
		lbphRecognizer.train(lMat, labels);
		lbphRecognizer.save("MiModelo.xml");
		
		int[] personasPredichas = {-1};
		double[] confidence = {0.0};
		
		Mat cosa = Imgcodecs.imread("./galeria/test_matias2.jpeg");
		Imgproc.cvtColor(cosa, cosa, Imgproc.COLOR_BGR2GRAY);
		
		MatOfRect lili = new MatOfRect();
		
		clasificadorCaras.detectMultiScale(cosa, lili);
				
		
		lbphRecognizer.predict(new Mat(cosa, lili.toArray()[0]), personasPredichas, confidence);
		
		//System.out.println("Predicho: " + lbphRecognizer.predict_label(new Mat(cosa, lili.toArray()[0])));
		
		int k = 0;
		k = 5;
		k = k+ 2;
	}
	
}
