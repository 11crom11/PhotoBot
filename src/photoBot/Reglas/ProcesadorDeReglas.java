package photoBot.Reglas;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;

public class ProcesadorDeReglas {

	private KnowledgeBase kbase;
	private StatefulKnowledgeSession ksession;
	
	public ProcesadorDeReglas(){
		//CONTINUAR!!!!!!!!!!!!!!!!!!!!!
		//Inicializar atributos, arrancar DROOLS, configurar
	}
	
	/*public static final void main(String[] args) {
		final KnowledgeBase kbase = readKnowledgeBase();
		final StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();

		for (Order order : orders) {
			ksession.insert(order);
		}
		
		
		ksession.fireAllRules();


	}
*/
	private static KnowledgeBase readKnowledgeBase() {
		final KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		kbuilder.add(ResourceFactory.newClassPathResource("photoBot/Reglas/Order.drl"), ResourceType.DRL);
		if (kbuilder.hasErrors()) {
			for (KnowledgeBuilderError error : kbuilder.getErrors()) {
				System.err.println(error);
			}
			throw new IllegalArgumentException("Imposible crear knowledge con Order.drl");
		}
		final KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
		kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
		return kbase;
	}
}