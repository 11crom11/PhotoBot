package photoBot.Agentes.Comportamiento;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.lowagie.text.Image;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import photoBot.BBDD.Filtro;
import photoBot.BBDD.FiltroEvento;
import photoBot.BBDD.FiltroFecha;
import photoBot.BBDD.FiltroPersona;
import photoBot.BBDD.PhotoBotBBDD;
import photoBot.Imagen.Imagen;

public class ComportamientoAgenteBuscarImagenes extends CyclicBehaviour {

	private Behaviour self;
	private PhotoBotBBDD bd;
	private HashMap<String, List<Filtro>> filtros;
	
	public ComportamientoAgenteBuscarImagenes(Agent a) {
		super(a);
		this.self = this;
		
		this.bd = new PhotoBotBBDD();
		this.filtros = new HashMap<String, List<Filtro>>();
		
	}

	@Override
	public void action() {
		ACLMessage msj = this.self.getAgent().receive();

		if(msj != null){

			HashMap<String, Object> msjContent;
			int comando;	


			try {
				msjContent = (HashMap<String, Object>) msj.getContentObject();
				comando = (int) msjContent.get("COMANDO");

				if (comando == ConstantesComportamiento.BUSCAR_IMAGENES){
					buscarImagenes(msj, msjContent);
				}
				else if(comando == ConstantesComportamiento.ANADIR_FILTRO_FECHA) {
					anadirFiltroFecha(msj, msjContent);
				}
				else if(comando == ConstantesComportamiento.ANADIR_FILTRO_EVENTO) {
					anadirFiltroEvento(msj, msjContent);
				}
				else if(comando == ConstantesComportamiento.ANADIR_FILTRO_PERSONA) {
					anadirFiltroPersona(msj, msjContent);
				}
				else if(comando == ConstantesComportamiento.CREAR_LISTA_FILTROS) {
					inicializarListaFiltrosUsuario(msjContent);
				}
				
			} catch (UnreadableException e) {
				e.printStackTrace();
			}

		}
		
	}
	
	private void inicializarListaFiltrosUsuario(HashMap<String, Object> msjContent) {
		String idUsuario = (String) msjContent.get("ID");
		
		List<Filtro> lFiltros = new ArrayList<Filtro>();
		this.filtros.put(idUsuario, lFiltros);
	}
	
	private void buscarImagenes(ACLMessage msj, HashMap<String, Object> msjContent) {
		String idUsuario = (String) msjContent.get("ID");
		
		List<Imagen> lImagenes = this.bd.buscarImagenesFiltros(this.filtros.get(idUsuario), idUsuario);
		
		List<String> ficherosImagen = new ArrayList<String>();
		
		for(Imagen i : lImagenes) {
			ficherosImagen.add(i.getUbicacion());
		}
		
		msj = new ACLMessage(ACLMessage.INFORM);
		msj.addReceiver(new AID(ConstantesComportamiento.AGENTE_CONVERSACION_USUARIO, AID.ISLOCALNAME));
		msjContent = new HashMap<String, Object>();
		
		msjContent.put("COMANDO", ConstantesComportamiento.ENTREGAR_IMG_ENCONTRADAS);
		msjContent.put("LISTA", ficherosImagen);
		
		try {
			msj.setContentObject((Serializable)msjContent);
			getAgent().send(msj);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void anadirFiltroFecha(ACLMessage msj, HashMap<String, Object> msjContent) {
		String idUsuario = (String) msjContent.get("ID");
		Pair<Date, Date> filtro = (Pair<Date, Date>) msjContent.get("FILTRO");
		
		List<Filtro> aux = this.filtros.get(idUsuario);
		aux.add(new FiltroFecha("fecha", filtro));
		
		this.filtros.put(idUsuario, aux);
	}
	
	private void anadirFiltroEvento(ACLMessage msj, HashMap<String, Object> msjContent) {
		String idUsuario = (String) msjContent.get("ID");
		String filtro = (String) msjContent.get("FILTRO");
		
		List<Filtro> aux = this.filtros.get(idUsuario);
		aux.add(new FiltroEvento("lEventos", filtro));
		
		this.filtros.put(idUsuario, aux);
	}
	
	private void anadirFiltroPersona(ACLMessage msj, HashMap<String, Object> msjContent) {
		String idUsuario = (String) msjContent.get("ID");
		String filtro = (String) msjContent.get("FILTRO");
		
		List<Filtro> aux = this.filtros.get(idUsuario);
		aux.add(new FiltroPersona("lPersonas", filtro));
		
		this.filtros.put(idUsuario, aux);
	}
	
	/*VIEJO ... BORRAR CUANDO SE TENGAN IMPLEMENTADAS LAS FUNCIONES DE BUSQUEDA
	private void buscarTodasImagenes(ACLMessage msj) {
		List<String> list = new ArrayList<>();
		
		String userID = msj.getContent();
		System.out.print("AgenteBuscarImagenes ha recibido el siguiente mensaje: " + userID);
		
		try {
			Files.list(Paths.get("./galeria/" + userID)).forEach((imagen)->{
				System.out.println(imagen.toString());
				list.add(imagen.toString());
				
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		HashMap<String, Object> msjContent = new HashMap<String, Object>();
		
		msjContent.put("COMANDO", ConstantesComportamiento.ENTREGAR_IMG_ENCONTRADAS);
		msjContent.put("LISTA", list);
		
		ACLMessage reply = msj.createReply();
		getAgent().send(reply);
			//sendPhotoRequest.setNewPhoto(new java.io.File(imagen.toString()));
	}
	
	*/
}
