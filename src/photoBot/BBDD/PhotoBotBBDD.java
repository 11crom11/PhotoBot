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

/**
 * Esta clase constiene la instancia a la base de datos. Utiliza el framework Morphia para
 * interactuar con la base de datos MongoDB
 *
 */
public class PhotoBotBBDD {

	
	private final Morphia morphia;
	private final Datastore dataStore;
	
	/**
	 * Este constructor devuelve una conexion a la base de datos de PhotoBot
	 */
	public PhotoBotBBDD(){
		
		this.morphia = new Morphia();
		
		this.morphia.mapPackage("photoBot.Imagen");
		
		//172.29.129.141
		this.dataStore = morphia.createDatastore(new com.mongodb.MongoClient("localhost", 27017), "photobot");
		
	}
	
	/**
	 * Este metodo devuelve un objeto de tip Usuario en el caso de que exista aquel con ID de telegram proporcionado
	 * a traves de un objeto Usuario.
	 * @param usuario Usuario del que se quiere comprobar la existencia en la base de datos
	 * @return Objeto Usuario con todos sus atributos completos con la informacion de la base de datos
	 */
	public Usuario existeUsuario(Usuario usuario){
		
		Query<Usuario> q = this.dataStore.createQuery(Usuario.class);
		
		//return q.field("idUsuarioTelegram").equal(usuario.getIdUsuarioTelegram()).asList();
		return q.field("idUsuarioTelegram").equal(usuario.getIdUsuarioTelegram()).get();
	}
	
	
	
	
	/**
	 * Este metodo registra a un usuario en la base de datos
	 * @param usuario Usuario que se quiere registrar en la base de datos. Debe tener todos sus atributos con valores.
	 * @return
	 */
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
	
	/**
	 * Registrar una nueva imagen en la bbdd
	 * @param imagen Imagen que se desea registrar en la base de datos. Puede contener atributos vacios.
	 * @return
	 */
	public boolean registrarImagen(Imagen imagen){
		boolean ok = true;
		
		System.out.println("NUEVA IMAGEN EN LA BBDD");
		this.dataStore.save(imagen);
		
		return ok;
	}
	
	/**
	 * Este metodo actualiza la informacion de una imagen en la base de datos
	 * @param imagen Imagen que se quiere actualizar en la bbdd
	 * @return True si todo ha sido correcto
	 */
	public boolean actualizarInfoImagen(Imagen imagen){
		boolean ok = true;
		System.out.println("ACTUALIZANDO INFORMACIÓN DE LA IMAGEN");

		this.dataStore.save(imagen);
		
		
		return ok;
		
	}
	
	/**
	 * Este metodo registra una nueva persona en la base de datos con el usuario correspondiente
	 * @param p Persona que se desea registrar en la base de datos
	 * @return True si todo ha funcionado correctamente
	 */
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
	
	/**
	 * Este metodo devuelve una Persona a partir de la etiqueta que se utiliza para su clasificacion
	 * @param u Usuario al que pertenece la persona
	 * @param etiqueta Etiqueta que se utiliza para clasificar su cara
	 * @return Persona que cumple con los criterios de busqueda en la base de datos
	 */
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
	
	/**
	 * Este metodo actualiza la informacion de un usuario en la base de datos
	 * @param u Objeto Usuario con la informacion del usuario que va a ser actualizado
	 */
	public void actualizarInfoUsuario(Usuario u) {
		this.dataStore.save(u);
		System.out.println("INFORMACIÓN DEL USUARIO ACTUALIZADA EN LA BBDD");
	}
	
	/**
	 * Este metodo busca en la base de datos aquellas imagenes que cumplan con todos los filtros
	 * recibidos por parametro
	 * @param lFiltros Lista de filtros (de tipo persona, evento o fecha)
	 * @param idU Id del usuario sobre el que se quiere buscar sus imagenes
	 * @return
	 */
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

	/**
	 * Este metodo obtiene una lista de personas de un usuari a partir de las etiquetas proporcionadas
	 * @param list Lista de etiquetas de las personas que se desea obtener
	 * @param idU Usuario sobre el que se quiere obtener las personas
	 * @return
	 */
	public List<Persona> listaPersonasPorEtiqueta(List<Integer> list, String idU) {
		
		List<Persona> ret = new ArrayList<Persona>();
		
		Usuario u = this.dataStore.createQuery(Usuario.class).field("idUsuarioTelegram").equal(Integer.parseInt(idU)).get();
		
		ret = this.dataStore.createQuery(Persona.class).field("etiqueta").hasAnyOf(list)
				.field("user").equal(u).asList();
		
		return ret;
	}
}
