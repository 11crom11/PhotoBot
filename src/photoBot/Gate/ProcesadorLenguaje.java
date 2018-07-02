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
	
	public ProcesadorLenguaje() throws GateException, IOException{
		
		Gate.init();
		this.configurarGate();
	}
	
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
	
	private List<Etiqueta> obtenerEtiquetas(){
		List<Etiqueta> etiquetas = new ArrayList<Etiqueta>();
		Iterator<Document> iter = this.corpus.iterator();
		
		while(iter.hasNext()){
			Document doc = (Document) iter.next();
			AnnotationSet defaultAnnotSet = doc.getAnnotations();
			Set<String> annotTypesRequired = new HashSet<String>();
			
			//SE INDICA LOS TIPOS DE ETIQUETAS QUE QUEREMOS RECUPERAR
			annotTypesRequired.add("Fecha");
			annotTypesRequired.add("Busqueda");
			annotTypesRequired.add("Nombre_persona_imagen");
			annotTypesRequired.add("Fecha_busqueda");
			
			Set<Annotation> etiquetasAnotacion = new HashSet<Annotation>(defaultAnnotSet.get(annotTypesRequired));
		
			for (Annotation s : etiquetasAnotacion) {		
				//GETFEATURES ARREGLAR INVESTIGAR!!!!!!!!!!!!!
				Etiqueta e = new Etiqueta(s.getType(), (String) s.getFeatures().get("texto"));
				etiquetas.add(e);
			}
		}
		
		//Elimino el corpus para que en sucesivos analisis no aparezcan anotaciones de los anteriores
		this.corpus.clear();
		
		return etiquetas;
	}
}
