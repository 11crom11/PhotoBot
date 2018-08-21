package photoBot.OpenCV;

import org.opencv.core.MatOfRect;

public class PruebaOpenCV {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		detectarCaras();

	}
	
	private static void detectarCaras() {
		
		//String url = "./galeria/573730609/1529661117.jpeg";
		String url = "./galeria/test_vicente.jpeg";
		
		GestorDeCaras gestorCaras = new GestorDeCaras();
		
		//MatOfRect caras = gestorCaras.detectarCarasImagen(url);
		
		//gestorCaras.generarImagenCarasRecuadradas(url, caras);
		
		//gestorCaras.entrenaClasificadorr(url, caras);
	}

}
