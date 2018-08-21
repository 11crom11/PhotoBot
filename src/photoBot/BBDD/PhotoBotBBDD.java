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
		Query<Usuario> user = this.dataStore.createQuery(Usuario.class);
		
		//Como una imagen tiene propiedades referenciadas, si quiero hacer una consulta por de un valor de un atributo de una propiedad referenciada
		//lo tengo que hacer en varias consultas
		
		
		//1º Por seguridad, recojo lo ultimo del objeto referenciado de la BBDD
		Usuario aux = user.field("idUsuarioTelegram").equal(u.getIdUsuarioTelegram())
								.get();
		//2º Miro a ver si el usuario ya tiene a esa persona en alguna foto
		q.and(
				  q.criteria("user").equal(aux),
				  q.criteria("nombre").equal(p));
		
		List<Persona> persona = q.asList();
		
		//3ºA Si ya tengo alguna foto en la que salga esta persona: cojo su etiqueta
		if (persona.size() != 0) {
			etiqueta = persona.get(0).getEtiqueta();
			ret = persona.get(0);
		}
		//3ºB Si no tengo ninguna foto con esta persona, voy a ver cual es la primera etiqueta libre
		else {
			
			//4ºB Miro a ver si tiene alguna persona ya registrada
			q = this.dataStore.createQuery(Persona.class); //reiniciar la query
			
			persona = q.field("user").equal(aux)
					.order("-etiqueta")
					.asList();
			
			//5ºB.A No tengo a personas. Es la primera vez que subo una fotografia, la primera etiqueta que utilice para clasificar sera el 1
			if(persona.size() == 0) {
				etiqueta = 1;
			}
			//5ºB.B Si ya tengo a mas personas, cojo la etiqueta mas alta libre
			else {
				etiqueta = aux.getEtiquetaMaxUsada() + 1;
			}
			
			ret = new Persona(p, etiqueta, u);
		}
		
		
		return ret;
	}
	
	public void actualizarInfoUsuario(Usuario u) {
		this.dataStore.save(u);
	}
}
