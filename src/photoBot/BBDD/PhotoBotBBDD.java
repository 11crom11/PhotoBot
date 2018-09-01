package photoBot.BBDD;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.CriteriaContainerImpl;
import org.mongodb.morphia.query.FieldEnd;
import org.mongodb.morphia.query.Query;

import photoBot.Imagen.Imagen;
import photoBot.Imagen.Persona;
import photoBot.Imagen.Usuario;

public class PhotoBotBBDD {

	
	private final Morphia morphia;
	private final Datastore dataStore;
	
	public PhotoBotBBDD(){
		
		this.morphia = new Morphia();
		
		this.morphia.mapPackage("photoBot.Imagen");
		
		//172.29.129.141
		this.dataStore = morphia.createDatastore(new com.mongodb.MongoClient("localhost", 27017), "photobot");
		
	}
	
	public Usuario existeUsuario(Usuario usuario){
		
		Query<Usuario> q = this.dataStore.createQuery(Usuario.class);
		
		//return q.field("idUsuarioTelegram").equal(usuario.getIdUsuarioTelegram()).asList();
		return q.field("idUsuarioTelegram").equal(usuario.getIdUsuarioTelegram()).get();
	}
	
	
	
	
	public boolean crearUsuario(Usuario usuario){
		boolean ok = false;
		
		//boolean existeUsuario = this.existeUsuario(usuario).isEmpty() ? false : true;
		boolean existeUsuario = this.existeUsuario(usuario) == null ? false : true;
		System.out.print("ESTO REGISTRANDO AL USUARIO...");
		
		if(!existeUsuario){
			//this.cUsuarios.insertOne(usuario);
			System.out.println("USUARIO REGISTRADO CORRECTAMENTE");
			this.dataStore.save(usuario);
			
			ok = true;
		}
		
		return ok;
	}
	
	public boolean registrarImagen(Imagen imagen){
		boolean ok = true;
		
		System.out.println("NUEVA IMAGEN EN LA BBDD");
		this.dataStore.save(imagen);
		
		return ok;
	}
	
	public boolean actualizarInfoImagen(Imagen imagen){
		boolean ok = true;
		System.out.println("ACTUALIZANDO INFORMACIÓN DE LA IMAGEN");

		this.dataStore.save(imagen);
		
		
		return ok;
		
	}
	
	public boolean registrarPersonaUsuario(Persona p){
		boolean ok = true;
		
		System.out.println("INSERCION DE UNA NUEVA PERSONA EN LA BBDD");
		this.dataStore.save(p);
		
		return ok;
	}
	
	/**
	 * Este metodo devuelve un Objeto Persona de la base de datos con su respectiva etiqueta.
	 * Si la persona no existe en la base de datos, se crea un objeto persona con un numero un
	 * etiqueta correspondiente (el siguiente valor libre del usuario)
	 * @param u Usuario registrado
	 * @param p Nombre de la persona a buscar
	 * @return Objeto Persona con el valor etiqueta correspondiente
	 */
	public Persona obtenerPersonaEtiqueta(Usuario u, String p) {
		Persona ret = null;
		int etiqueta = 0;
		
		Query<Persona> q = this.dataStore.createQuery(Persona.class);
		q.and(
				  q.criteria("user").equal(u),
				  q.criteria("nombre").equal(p));
		
		//1º Miro a ver si existe una persona dentro de la lista de personas del usuario en la BBDD
		Persona persona = q.get();
		
		//2A Si ya tengo alguna foto en la que salga esta persona: cojo su etiqueta
		if (persona != null) {
			etiqueta = persona.getEtiqueta();
			ret = persona;
		}
		//2B Si no tengo ninguna foto con esta persona, cojo la siguiente etiqueta libre (si es la primer persona 0 -> 1, eoc, n -> n+1)
		else {
			
			etiqueta = u.getEtiquetaMaxUsada() + 1; 
			
			ret = new Persona(p, etiqueta, u);
		}
		
		
		return ret;
	}
	
	public Persona obtenerPersonaApartirEtiqeuta(Usuario u, int etiqueta) {
		Query<Persona> q = this.dataStore.createQuery(Persona.class);
		q.and(
				  q.criteria("user").equal(u),
				  q.criteria("etiqueta").equal(etiqueta));
		
		Persona persona = q.get();
		
		if(persona != null) {
			return persona;
		}
		else {
			return null;
		}
	}
	
	public void actualizarInfoUsuario(Usuario u) {
		this.dataStore.save(u);
		System.out.println("INFORMACIÓN DEL USUARIO ACTUALIZADA EN LA BBDD");
	}
	
	public List<Imagen> buscarImagenesFiltros(List<Filtro> lFiltros, String idU){
		
		Usuario u = this.dataStore.createQuery(Usuario.class).field("idUsuarioTelegram").equal(Integer.parseInt(idU)).get();
		
		List<FiltroFecha> filtrosFecha = new ArrayList<FiltroFecha>();
		List<FiltroEvento> filtrosEvento = new ArrayList<FiltroEvento>();
		List<FiltroPersona> filtrosPersonas = new ArrayList<FiltroPersona>();
		
		for(Filtro f : lFiltros) {
			if(f.getClass() == FiltroFecha.class) {
				filtrosFecha.add((FiltroFecha) f);
			}
			else if(f.getClass() == FiltroEvento.class) {
				filtrosEvento.add((FiltroEvento) f);
			}
			else if(f.getClass() == FiltroPersona.class) {
				filtrosPersonas.add((FiltroPersona) f);
			}
		}
		
		Query<Imagen> q = this.dataStore.createQuery(Imagen.class);
		
		//ANADIR FILTROS DE FECHA
		for(FiltroFecha f : filtrosFecha) {
			Date d1 = f.getFilterValue().getRight().getLeft();
			Date d2 = f.getFilterValue().getRight().getRight();
			
			q.field(f.getCampoBBDD()).greaterThanOrEq(d1);
			q.field(f.getCampoBBDD()).lessThanOrEq(d2);
		}
		
		//ANADIR FILTROS DE EVENTO
		if(filtrosEvento.isEmpty() == false) {
			List<String> aux = new ArrayList<>();
			
			for(FiltroEvento e : filtrosEvento) {
				aux.add(e.getFilterValue().getRight());
			}
			
			q.field("lEventos").hasAnyOf(aux);
		}
		
		//ANADIR FILTROS DE PERSONAS
		if(filtrosPersonas.isEmpty() == false) {
			
			List<Persona> aux = new ArrayList<Persona>();
			
			for(FiltroPersona f : filtrosPersonas) {
				Query<Persona> p = this.dataStore.createQuery(Persona.class);
				p.field("nombre").equal(f.getFilterValue().getRight());
				p.field("user").equal(u);
				
				aux.add(p.get());
			}
			
			q.field("lPersonas").hasAllOf(aux);
			
		}
		
		//POR ULTIMO, QUE LAS IMAGENES ENCONTRADAS SEA DEL USUARIO QUE MANDO LA PETICION DE BUSCAR
		q.field("propietario").equal(u);
		
		return q.asList();
	}
	
	public List<Persona> prueba() {
		Query<Persona> s = this.dataStore.createQuery(Persona.class);
		
		List<Integer> l = new ArrayList<>();
		l.add(1);
		l.add(2);
		
		s.field("etiqueta").hasAnyOf(l);
		
		return s.asList();
	}

	public List<Persona> listaPersonasPorEtiqueta(List<Integer> list) {
		
		List<Persona> ret = new ArrayList<Persona>();
		
		ret = this.dataStore.createQuery(Persona.class).field("etiqueta").hasAnyOf(list).asList();
		
		return ret;
	}
}
