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

import jade.core.behaviours.Behaviour;
import photoBot.Agentes.Comportamiento.ConstantesComportamiento;
import photoBot.Gate.Etiqueta;

/**
 * Esta clase actua como el gestor de reglas de la aplicacion
 *
 */
/**
 * @author d_dan
 *
 */
public class ProcesadorDeReglas {

	private KnowledgeBase kbase;
	private StatefulKnowledgeSession ksession;
	private final KnowledgeBuilder kbuilder;
	private ManejadorReglas manejadorReglas;
	
	
	/**
	 * Este constructor construye un Procesador de reglas a partir de los ficheros drl contenidos 
	 * en los directorios especifios para tal fin (photoBot.Drools.Reglas). Utiliza la funcionalidad 
	 * de DROOLS
	 */
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
	
	/**
	 * Este metodo pone en marcha al procesador de reglas, evaluando las reglas y ejecutandose aquellas que cumplan con
	 * sus condiciones, teniendo en cuenta su base de conocimiento
	 * @param lEtiquetas Lista de etiquetas que se insertaran en la base de conocimiento del procesador de reglas
	 * @param conversacion Estado de la conversacion
	 * @param comportamientoAgente Comportamiento del agente del que se quiere ejecutar sus metodos
	 * @return
	 */
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
		
		this.ksession.fireAllRules();
		this.ksession = this.kbase.newStatefulKnowledgeSession();
		
		return conversacion;
	}
	
	/**
	 * Establece al grupo de reglas SUBIR_IMAGEN como grupo activo en el manejador de regla
	 */
	public void seleccionarGrupoSubirImagenManejador() {
		this.manejadorReglas.setGrupoActivado(ConstantesComportamiento.GRUPO_SUBIR_IMAGEN);
	}
}