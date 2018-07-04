package photoBot.Drools;

import java.util.List;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;

import photoBot.Drools.Reglas.ConclusionReglas;
import photoBot.Gate.Etiqueta;

public class ProcesadorDeReglas {

	private KnowledgeBase kbase;
	private StatefulKnowledgeSession ksession;
	private final KnowledgeBuilder kbuilder;
	
	public ProcesadorDeReglas(){
		
		//kbuilder/////////////////////////////////////////////////////////////////////////////////////////
		this.kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		kbuilder.add(ResourceFactory.newClassPathResource("photoBot/Drools/Reglas/ReglasConversacionales.drl"), ResourceType.DRL);
		if (kbuilder.hasErrors()) {
			for (KnowledgeBuilderError error : kbuilder.getErrors()) {
				System.err.println(error);
			}
			throw new IllegalArgumentException("Imposible crear knowledge con el drl");
		}
		
		//kbase////////////////////////////////////////////////////////////////////////////////////////////
		this.kbase = KnowledgeBaseFactory.newKnowledgeBase();
		kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
		
		//ksession/////////////////////////////////////////////////////////////////////////////////////////
		this.ksession = kbase.newStatefulKnowledgeSession();

	}
	
	public ConclusionReglas ejecutarReglasEtiquetas(List<Etiqueta> lEtiquetas){
		
		ConclusionReglas conclusiones = new ConclusionReglas();
		
		ksession.insert(conclusiones);
		
		for (Etiqueta e : lEtiquetas) {
			ksession.insert(e);
		}
		
		this.ksession.fireAllRules();
		
		return conclusiones;
	}
}