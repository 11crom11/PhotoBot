package photoBot.BBDD;


import java.util.List;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import com.mongodb.DBRef;

import photoBot.Imagen.Imagen;
import photoBot.Imagen.Persona;
import photoBot.Imagen.Usuario;

public class PhotoBotBBDD {

	
	private final Morphia morphia;
	private final Datastore dataStore;
	
	public PhotoBotBBDD(){
		
		this.morphia = new Morphia();
		
		this.morphia.mapPackage("photoBot.Imagen");
		
		this.dataStore = morphia.createDatastore(new com.mongodb.MongoClient(), "photobot");
		
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
		System.out.println("Estoy registrando al usuario");
		
		if(!existeUsuario){
			//this.cUsuarios.insertOne(usuario);
			this.dataStore.save(usuario);
			
			ok = true;
		}
		
		return ok;
	}
	
	public boolean registrarImagen(Imagen imagen){
		boolean ok = true;
		
		this.dataStore.save(imagen);
		
		return ok;
	}
	
	public boolean actualizarInfoImagen(Imagen imagen){
		boolean ok = true;
		System.out.println("Actualizando informacion de la imagen");

		this.dataStore.save(imagen);
		
		
		return ok;
		
	}
	
	public boolean registrarPersonaUsuario(Persona p){
		boolean ok = true;
		
		System.out.println("Inserción de persona en la base de datos");
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
		
		//2ºA Si ya tengo alguna foto en la que salga esta persona: cojo su etiqueta
		if (persona != null) {
			etiqueta = persona.getEtiqueta();
			ret = persona;
		}
		//2ºB Si no tengo ninguna foto con esta persona, cojo la siguiente etiqueta libre (si es la primer persona 0 -> 1, eoc, n -> n+1)
		else {
			
			etiqueta = u.getEtiquetaMaxUsada() + 1; 
			
			ret = new Persona(p, etiqueta, u);
		}
		
		
		return ret;
	}
	
	public void actualizarInfoUsuario(Usuario u) {
		this.dataStore.save(u);
	}
}
