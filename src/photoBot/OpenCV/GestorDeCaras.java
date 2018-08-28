package photoBot.OpenCV;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.face.FaceRecognizer;
import org.opencv.face.LBPHFaceRecognizer;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.osgi.OpenCVNativeLoader;

public class GestorDeCaras {
   
	private CascadeClassifier clasificadorCaras;
	
	public GestorDeCaras() {
		String path =  System.getProperty("user.dir");
		System.out.println(path);

		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		//System.load(Core.NATIVE_LIBRARY_NAME);
		
		String urlXml = path + "\\opencv\\sources\\data\\lbpcascades\\lbpcascade_frontalface_improved.xml";
		System.out.println(urlXml);

		this.clasificadorCaras = new CascadeClassifier(urlXml);
		
	}
	
	private MatOfRect detectarCarasImagen(String urlImagen) {
		
		MatOfRect carasDetectadas = new MatOfRect();
		Mat imagen = Imgcodecs.imread(urlImagen);
		
		this.clasificadorCaras.detectMultiScale(imagen, carasDetectadas);
		
		return carasDetectadas;
	}
	
	private void generarImagenCarasRecuadradas(String urlImagen, MatOfRect carasDetectadas) {
		
		Mat imagen = Imgcodecs.imread(urlImagen);
		int i = 0;
		
		for(Rect rect : carasDetectadas.toArray()) {
			
			Imgproc.rectangle(imagen, 
					new Point(rect.x, rect.y), 
					new Point(rect.x + rect.width, rect.y + rect.height), 
					ColoresCaras.getScalarColor(i),
					8);
			
			i = (i + 1) % (ColoresCaras.getNumColores() - 1);
			
		}
		
		Imgcodecs.imwrite(FilenameUtils.getPath(urlImagen) + FilenameUtils.getBaseName(urlImagen) + "_rec.jpeg", imagen);

	}
	
	public void actualizarClasificadorPersonalizado(int idUsuario, CarasDetectadas carasDetectadas) {
		
		FaceRecognizer lbphClasificador = LBPHFaceRecognizer.create();
		lbphClasificador.read("./clasificadores/" + idUsuario + ".xml");
		
		lbphClasificador.update(carasDetectadas.getListOfMat(), carasDetectadas.getListOfLabels());
		
		lbphClasificador.save("./clasificadores/" + idUsuario + ".xml");
		
	}
	
	private void crearClasificadorPersonalizado(String idUsuario) {
		FaceRecognizer lbphClasificador = LBPHFaceRecognizer.create();
		
		lbphClasificador.save("./clasificadores/" + idUsuario + ".xml");
	}
	
	private List<Pair<Integer, Double>> dameEtiquetasPorcentajes(MatOfRect carasDetectadas, String urlImagen, String idUsuario){
		List<Pair<Integer, Double>> ret = new ArrayList<>();
		
		
		Mat imagen = Imgcodecs.imread(urlImagen);
		Imgproc.cvtColor(imagen, imagen, Imgproc.COLOR_BGR2GRAY);
		
		FaceRecognizer lbphClasificador = LBPHFaceRecognizer.create();
		
		//1. Ver si existe el clasificador
		File f = new File("./clasificadores/" + idUsuario + ".xml");
		
		//2. Si el clasificador existe
		if(f.exists()){
			lbphClasificador.read("./clasificadores/" + idUsuario + ".xml");
			
			for(Rect rect : carasDetectadas.toArray()) {
				Mat cara = new Mat(imagen, rect);
				int[] personasPredichas = {-1};
				double[] confidence = {1.0};
				
				lbphClasificador.predict(cara, personasPredichas, confidence);
				ret.add(Pair.of(personasPredichas[0], confidence[0]));
				int k = 0;k++;
				
			}			
		}
		//3. Si el clasificador no existe, no puede detectar personas, así que ponemos todo -1 y probabilidad 0
		else{
			crearClasificadorPersonalizado(idUsuario);
			
			for(int i = 0; i < carasDetectadas.toArray().length; i++) {
				int[] personasPredichas = {-1};
				double[] confidence = {1.0};
				
				ret.add(Pair.of(personasPredichas[0], confidence[0]));
			}
		}

		return ret; 
	}
	
	/**
	 * Este metodo se encarga de analizar una imagen para detectar las caras que aparecen en ella
	 * para, posteriormente, clasificarlas y predecir el nombre de la persona a la que pertenece.
	 * Ademas, se genera una imagen igual a la analizada con las caras detectadas recuadradas por
	 * colores.
	 *  
	 * @param urlImagen URL de la imagen que se quiere analizar
	 * @return Objeto CarasDetectadas que almacena la caras detectadas en una imagen, y un numero
	 * por persona, a la que corresponde la cara (este valor es unico por persona).
	 */
	public CarasDetectadas obtenerCarasImagen(String urlImagen, String idUsuario) {
		
		CarasDetectadas ret = null;
		
		//1- Detectar caras de una imagen
		MatOfRect caras = detectarCarasImagen(urlImagen);
		
		//2- Identificar la persona a la que pertenece esa cara
		List<Pair<Integer, Double>> personas = dameEtiquetasPorcentajes(caras, urlImagen, idUsuario);
				
		//3- Construir el objeto que almacene la informacion anterior
		ret = new CarasDetectadas(caras, personas, urlImagen);
		
		//4- Generar una imagen con las caras detectadas
		this.generarImagenCarasRecuadradas(urlImagen, caras);
		
		return ret;
	}
	
	
	
}
