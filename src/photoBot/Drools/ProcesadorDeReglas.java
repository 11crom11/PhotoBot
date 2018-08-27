package photoBot.Drools;

import java.util.List;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.common.InternalAgenda;
import org.drools.io.Resource;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;

import jade.core.behaviours.Behaviour;
import photoBot.Agentes.Comportamiento.ConstantesComportamiento;
import photoBot.Gate.Etiqueta;

public class ProcesadorDeReglas {

	private KnowledgeBase kbase;
	private StatefulKnowledgeSession ksession;
	private final KnowledgeBuilder kbuilder;
	private ManejadorReglas manejadorReglas;
	
	
	public ProcesadorDeReglas(){
		
		//kbuilder/////////////////////////////////////////////////////////////////////////////////////////
		this.kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		
		kbuilder.add(ResourceFactory.newClassPathResource("photoBot/Drools/Reglas/Reglas_buscarImagen.drl"), ResourceType.DRL);
		kbuilder.add(ResourceFactory.newClassPathResource("photoBot/Drools/Reglas/Reglas_comprobarExistencia.drl"), ResourceType.DRL);
		kbuilder.add(ResourceFactory.newClassPathResource("photoBot/Drools/Reglas/Reglas_registroUsuario.drl"), ResourceType.DRL);
		kbuilder.add(ResourceFactory.newClassPathResource("photoBot/Drools/Reglas/Reglas_saludo.drl"), ResourceType.DRL);
		kbuilder.add(ResourceFactory.newClassPathResource("photoBot/Drools/Reglas/Reglas_subirImagen.drl"), ResourceType.DRL);
		
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
		
		this.manejadorReglas = new ManejadorReglas();

	}
	
	public Conversacion ejecutarReglasEtiquetas(List<Etiqueta> lEtiquetas, Conversacion conversacion, Behaviour comportamientoAgente){
		
		//this.ksession = this.kbase.newStatefulKnowledgeSession();
		
		this.ksession.setGlobal("GR_BI", ConstantesComportamiento.GRUPO_BUSCAR_IMAGEN);
		this.ksession.setGlobal("GR_EX", ConstantesComportamiento.GRUPO_EXISTENCIA_USUARIO);
		this.ksession.setGlobal("GR_RE", ConstantesComportamiento.GRUPO_REGISTRO_USUARIO);
		this.ksession.setGlobal("GR_SA", ConstantesComportamiento.GRUPO_SALUDO);
		this.ksession.setGlobal("GR_SI", ConstantesComportamiento.GRUPO_SUBIR_IMAGEN);
		this.ksession.setGlobal("GR_PU", ConstantesComportamiento.GRUPO_ESPERA_PETICION_USUARIO);

		
		ksession.insert(conversacion);
		ksession.insert(comportamientoAgente);
		ksession.insert(this.manejadorReglas);
		
		for (Etiqueta e : lEtiquetas) {
			ksession.insert(e);
		}
		
		//this.ksession.fireAllRules(); //Se ejecutan todas las reglas 

		//this.ksession.getAgenda().getAgendaGroup("dog").setFocus();
		
		this.ksession.fireAllRules(); //Se ejecutan solamente el numero de reglas pasado por parametro
		this.ksession = this.kbase.newStatefulKnowledgeSession();
		
		return conversacion;
	}
	
	public Conversacion ejecutarReglas(Conversacion conversacion, Behaviour comportamientoAgente){
		this.ksession = this.kbase.newStatefulKnowledgeSession();
		
		this.ksession.setGlobal("GR_BI", ConstantesComportamiento.GRUPO_BUSCAR_IMAGEN);
		this.ksession.setGlobal("GR_EX", ConstantesComportamiento.GRUPO_EXISTENCIA_USUARIO);
		this.ksession.setGlobal("GR_RE", ConstantesComportamiento.GRUPO_REGISTRO_USUARIO);
		this.ksession.setGlobal("GR_SA", ConstantesComportamiento.GRUPO_SALUDO);
		this.ksession.setGlobal("GR_SI", ConstantesComportamiento.GRUPO_SUBIR_IMAGEN);
		this.ksession.setGlobal("GR_PU", ConstantesComportamiento.GRUPO_ESPERA_PETICION_USUARIO);


		ksession.insert(conversacion);
		ksession.insert(comportamientoAgente);
		ksession.insert(this.manejadorReglas);

			
		//this.ksession.fireAllRules(); //Se ejecutan todas las reglas 

		this.ksession.fireAllRules(); //Se ejecutan solamente el numero de reglas pasado por parametro
		this.ksession = this.kbase.newStatefulKnowledgeSession();
		
		return conversacion;
	}
}