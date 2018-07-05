package photoBot.Gate;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Corpus;
import gate.CorpusController;
import gate.Document;
import gate.Factory;
import gate.FeatureMap;
import gate.Gate;
import gate.corpora.DocumentContentImpl;
import gate.util.GateException;
import gate.util.persistence.PersistenceManager;

public class ProcesadorLenguaje {
	
	/** The Corpus Pipeline application to contain ANNIE */
	private CorpusController annieController;
	//private StandAloneAnnie annie;
	private Corpus corpus;
	
	
	/**
	 * Con este constructor obtenemos un procesador de lenguaje coonfigurado y arrancado para
	 * poder analizar texto en calquier momento.
	 * @throws GateException
	 * @throws IOException
	 */
	public ProcesadorLenguaje() throws GateException, IOException{
		
		Gate.init();
		this.configurarGate();
	}
	
	
	/**
	 * Este método se encarga de configurar los parámetros de GATE para poder 
	 * funcionar en cualquier momento
	 * @throws IOException
	 * @throws GateException
	 */
	private void configurarGate() throws IOException, GateException{
		
		// load the ANNIE application from the saved state in plugins/ANNIE
		File pluginsHome = Gate.getPluginsHome();
		File anniePlugin = new File(pluginsHome, "ANNIE");
		File annieGapp = new File(anniePlugin, "ANNIE_with_defaults.gapp");		
		
		this.annieController = (CorpusController) PersistenceManager.loadObjectFromFile(annieGapp);
		//this.annie = new StandAloneAnnie();
		//this.annie.initAnnie();
		this.corpus = Factory.newCorpus("StandAloneAnnie corpus");	
	}
	
	/**
	 * Este método se encarga de configurar el documento que se va a analizar a partir del texto recibido.
	 * Ejecuta ANNIE de GATE para analizar el texto, devolviendose una lista de Anotaciones que contienen
	 * el texto (Cada anotacion está construido sobre un objeto Etiqueta que contiene el tipo de la 
	 * anotación y el texto de la palabra cuya anotación hace referencia
	 * @param texto String con el texto del cual se quieren obtener anotaciones
	 * @return Lista de Anotaciones de tipo Etiqueta (contienen el tipo de la anotacion y el texto de la palabra que hace referencia
	 * @throws GateException
	 */
	public List<Etiqueta> analizarTextoGate(String texto) throws GateException{
		
		Document documento = new gate.corpora.DocumentImpl();
		DocumentContentImpl impl = new DocumentContentImpl(texto);
		documento.setContent(impl);
		corpus.add(documento);

		//this.annie.setCorpus(corpus);
		//this.annie.execute();
		this.annieController.setCorpus(corpus);
		this.annieController.execute();
		
		return obtenerEtiquetas();
	}
	
	/**
	 * Este método devuelve una lista de tipo Etiqueta, las cuales contienen el tipo de la etiqueta y el texto
	 * de la palabra que hace referencia.
	 * @return Lista de Etiquetas del texto analizado
	 */
	private List<Etiqueta> obtenerEtiquetas(){
		List<Etiqueta> etiquetas = new ArrayList<Etiqueta>();
		Iterator<Document> iter = this.corpus.iterator();
		
		while(iter.hasNext()){
			Document doc = (Document) iter.next();
			AnnotationSet defaultAnnotSet = doc.getAnnotations();
			Set<String> annotTypesRequired = new HashSet<String>();
			
			//////////////////////////////////////////////////////////////////////////////////////////////////////
			//ANOTACIONES TOKEN PARA OBTENER EL TEXTO DE LA PALABRA
			annotTypesRequired.add("Token");
			AnnotationSet etiquetasToken = defaultAnnotSet.get(annotTypesRequired);
			
			//////////////////////////////////////////////////////////////////////////////////////////////////////
			//SE INDICA LOS TIPOS DE ETIQUETAS QUE QUEREMOS RECUPERAR
			annotTypesRequired = new HashSet<String>();
			annotTypesRequired.add("Fecha");
			annotTypesRequired.add("Busqueda");
			annotTypesRequired.add("Nombre_persona_imagen");
			annotTypesRequired.add("Fecha_busqueda");
			annotTypesRequired.add("Saludo");
			annotTypesRequired.add("Cargar");
			
			Set<Annotation> etiquetasAnotacion = new HashSet<Annotation>(defaultAnnotSet.get(annotTypesRequired));
		
			for (Annotation s : etiquetasAnotacion) {		

				Etiqueta e = new Etiqueta(s.getType(), obtenerNombrePalabra(s, etiquetasToken));
				etiquetas.add(e);
			}
			
			//////////////////////////////////////////////////////////////////////////////////////////////////////
		}
		
		//Elimino el corpus para que en sucesivos analisis no aparezcan anotaciones de los anteriores
		this.corpus.clear();
		
		return etiquetas;
	}
	
	/**
	 * Este método obtiene el texto de la palabra que hace referencia la Anotación recibida como parametro
	 * localizado en un texto del cual se han obtenidos sus Anotaciones de tipo Token (los que contienen
	 * sus textos a partir de sus atributos String)
	 * @param a Anotacion de la cual se quiere obtener el texto de la palabra a la que hace referencia
	 * @param tokens Conjuntos de anotacion de tipo Token obtenidos del análisis del texto
	 * @return String con el texto de la palabra que hace referencia la etiqueta recibida
	 */
	private String obtenerNombrePalabra(Annotation a, AnnotationSet tokens) {
		
		/*se recupera las anotaciones de tipo Token para que, 
		a partir de una anotación de un tipo específico, 
		dados su StartNode y EndNode, y partir de aquí, el startOfsset y endOfsset, 
		obtener la anotación Token correspondiente, y de esta anotación Token, 
		sus Features, las cuales contienen una en concreto, que es "string", 
		que contienen el texto de la palabra.
		*/
		
		
		String nombre = "";
		
		AnnotationSet palabrasToken = tokens.get(a.getStartNode().getOffset(), a.getEndNode().getOffset());
		Annotation palabraToken = palabrasToken.iterator().next();
		
		FeatureMap features = palabraToken.getFeatures();
		nombre = (String) features.get("string");
		
		return nombre;
	}
}
