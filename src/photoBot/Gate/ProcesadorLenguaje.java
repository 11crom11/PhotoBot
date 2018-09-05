package photoBot.Gate;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
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
import gate.Node;
import gate.ProcessingResource;
import gate.corpora.DocumentContentImpl;
import gate.creole.ResourceInstantiationException;
import gate.creole.gazetteer.DefaultGazetteer;
import gate.util.GateException;
import gate.util.persistence.PersistenceManager;

public class ProcesadorLenguaje {
	
	/** The Corpus Pipeline application to contain ANNIE */
	private CorpusController annieController;
	private Corpus corpus;
	private DefaultGazetteer gazettero;
	
	
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
		this.corpus = Factory.newCorpus("PhotoBot corpus");
		
		this.gazettero = this.obtenerGazettero();
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
			annotTypesRequired.add("Nombre_persona");
			annotTypesRequired.add("Fecha_busqueda");
			annotTypesRequired.add("Saludo");
			annotTypesRequired.add("Cargar");
			annotTypesRequired.add("Nombre_persona_color");
			annotTypesRequired.add("Numero");
			annotTypesRequired.add("Evento");
			annotTypesRequired.add("Persona_color_desconocida");
			annotTypesRequired.add("Finalizar");
			annotTypesRequired.add("Confirmacion");
			annotTypesRequired.add("Negacion");
			//annotTypesRequired.add("Adicion");
			
			annotTypesRequired.add("FechaTipo1");
			annotTypesRequired.add("FechaCompuestaTipo1");
			annotTypesRequired.add("FechaTipo2");
			annotTypesRequired.add("FechaCompuestaTipo2");
			annotTypesRequired.add("FechaTipo3");
			annotTypesRequired.add("FechaCompuestaTipo3");
			annotTypesRequired.add("FechaTipo4");
			annotTypesRequired.add("FechaCompuestaTipo4");
			annotTypesRequired.add("FechaTipo5");
			annotTypesRequired.add("FechaTipo6");
			annotTypesRequired.add("FechaTipo7");
			annotTypesRequired.add("FechaCompuestaTipo7");
			annotTypesRequired.add("FechaTipo8");
			
			Set<Annotation> etiquetasAnotacion = new HashSet<Annotation>(defaultAnnotSet.get(annotTypesRequired));
		
			for (Annotation s : etiquetasAnotacion) {		

				if(s.getType().equals("Nombre_persona_color")) {
					annotTypesRequired = new HashSet<String>();
					annotTypesRequired.add("Color");
					annotTypesRequired.add("Nombre_persona");
					AnnotationSet colsNoms =defaultAnnotSet.get(annotTypesRequired);
					
					Etiqueta e = this.obtenerParNombreColorPersona(s, etiquetasToken, colsNoms);
					etiquetas.add(e);
				}
				else if(s.getType().equals("Persona_color_desconocida")) {
					annotTypesRequired = new HashSet<String>();
					annotTypesRequired.add("Color");
					annotTypesRequired.add("Nombre_persona");
					AnnotationSet colsNoms =defaultAnnotSet.get(annotTypesRequired);
					
					Etiqueta e = this.obtenerParNombreColorPersona(s, etiquetasToken, colsNoms);
					e.setTipo("Persona_color_desconocida");
					etiquetas.add(e);
				}
				else {
					Etiqueta e = new Etiqueta(s.getType(), obtenerNombrePalabra(s, etiquetasToken));
					etiquetas.add(e);					
				}

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
		//Necesito ordenar los token para que en etiquetas cuyo texto este formado por varias palabras
		//se devuelve de manera ordenada
		List<Annotation> nombreOrdenado = new ArrayList<>(palabrasToken);
		nombreOrdenado.sort(new Comparator<Annotation>() {

			@Override
			public int compare(Annotation o1, Annotation o2) {

				if(o1.getStartNode().getOffset() < o2.getStartNode().getOffset()) {
					return -1;
				}
				else if(o1.getStartNode().getOffset() > o2.getStartNode().getOffset())
					return 1;

				return 0;
			}
		});
		
		for (Annotation annotation : nombreOrdenado) {
			FeatureMap features = annotation.getFeatures();
			
			if(nombre.equals("")) {
				nombre = (String) features.get("string");
			}
			else {
				nombre += " " + (String) features.get("string");
			}
		}
		
		return nombre;
	}
	
	/**
	 * Este metodo devuelve una etiqueta con el par Nombre de una persona y color a partir de le etiqueta original
	 * (que contiene otras palabras sin uso)
	 * @param a Etiqueta cuyo texto contiene, ademas del nombre de la persona y el color, otras palabras sin uso
	 * @param tokens Conjunto de token que contienen el texto de las palabras
	 * @param nombresYcolores Etiquetas con nombres y colores por separado
	 * @return Devuelve una etiqueta que contiene el par Nombre de persona y color.
	 */
	private Etiqueta obtenerParNombreColorPersona(Annotation a, AnnotationSet tokens, AnnotationSet nombresYcolores) {
		
		String nombre = "", color = "";
		
		Node ini = a.getStartNode();
		Node fin = a.getEndNode();
		
		AnnotationSet nyc = nombresYcolores.getContained(ini.getOffset(), fin.getOffset());
		
		for (Annotation an : nyc) {
			if(an.getType().equals("Color")) {
				color = obtenerNombrePalabra(an, tokens);
			}
			else if(an.getType().equals("Nombre_persona")) {
				nombre = obtenerNombrePalabra(an, tokens);
			}
		}
				
		return new Etiqueta("Nombre_persona_color", nombre, color);
	}
	
	private DefaultGazetteer obtenerGazettero() {
		
		DefaultGazetteer g = null;
		
		Collection<ProcessingResource>  prs = annieController.getPRs();
		
		for (ProcessingResource processingResource : prs) {
			if (processingResource.getClass() == DefaultGazetteer.class){
				g = (DefaultGazetteer) processingResource;
				break;
			}
		}
		
		
		return g;
	}
	
	public void addPalabraContextoGazetero(String palabra) {

		
		/*this.gazettero.addLookup(palabra, new Lookup("eventos.lst", "evento_imagen", "evento_imagen", "spanish"));
		//LinearNode m = new LinearNode("eventos.lst", "evento_imagen", "evento_imagen", "spanish");
		LinearDefinition ld = this.gazettero.getLinearDefinition();
		Map<String, LinearNode> k = ld.getNodesByListNames();
		LinearNode ln = k.get("eventos.lst");
		GazetteerList eventos = ld.getListsByNode().get(ln);
		eventos.add(new GazetteerNode("blo", ":"));*/
		
		

		int p = 0;
		int s = p + 1;

		try {
			BufferedWriter output = new BufferedWriter(new FileWriter("GATE\\plugins\\ANNIE\\resources\\gazetteer\\eventos.lst", true));
			output.write(palabra);
			output.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			this.gazettero.reInit();
		} catch (ResourceInstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
