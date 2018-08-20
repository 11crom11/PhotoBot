package photoBot.BBDD;


import java.util.List;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.QueryResults;

import com.mongodb.DBCollection;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;

import jade.util.leap.Collection;
import photoBot.Imagen.Evento;
import photoBot.Imagen.Imagen;
import photoBot.Imagen.Persona;
import photoBot.Imagen.Usuario;

public class PhotoBotBBDD {

	
	private final Morphia morphia;
	private final Datastore dataStore;
	/*
	private MongoCollection<Usuario> cUsuarios;
	private MongoCollection<Persona> cPersonas;
	private MongoCollection<Imagen> cImagenes;
	private DBCollection cImagenesDos;
*/
	
	public PhotoBotBBDD(){
		
		this.morphia = new Morphia();
		
		this.morphia.mapPackage("photoBot.Imagen");
		
		this.dataStore = morphia.createDatastore(new com.mongodb.MongoClient(), "photobot");
		/*
		CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(com.mongodb.MongoClient.getDefaultCodecRegistry(), 
				CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));
		MongoDatabase database = mongoClient.getDatabase("photobot").withCodecRegistry(pojoCodecRegistry);
		
		this.cUsuarios = database.getCollection("usuarios", Usuario.class);
		this.cPersonas = database.getCollection("personas_usuarios", Persona.class);
		this.cImagenes = database.getCollection("imagenes", Imagen.class);
		*/
		//this.cImagenesDos = database.getCollection("imagenes", Imagen.class);
	}
	
	public List<Usuario> existeUsuario(Usuario usuario){
		
		/**
		FindIterable<Usuario> cursor  = this.cUsuarios.find(Filters.and(Filters.exists("idUsuarioTelegram"), Filters.eq("idUsuarioTelegram", idUsuarioTelegram)));
		
		
		return cursor.first() != null ? true : false;
		*/
		Query<Usuario> q = this.dataStore.createQuery(Usuario.class);
		
		
		return q.field("idUsuarioTelegram").equal(usuario.getIdUsuarioTelegram()).asList();
	}
	
	
	
	
	public boolean crearUsuario(Usuario usuario){
		boolean ok = false;
		
		boolean existeUsuario = this.existeUsuario(usuario).isEmpty() ? false : true;
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
		
		//this.cImagenes.insertOne(imagen);
		
		return ok;
	}
	
	public boolean actualizarInfoImagen(Imagen imagen){
		boolean ok = true;
		System.out.println("Actualizando informacion de la imagen");
		
		//imagen.addEventoContextoImagen(new Evento("Mierda"));
		//imagen.addPersonaImagen(imagen.getlPersonas().get(0));
		
		//this.cImagenes.replaceOne(Filters.eq("_id", imagen.getId()), imagen);
		this.dataStore.save(imagen);
		
		
		return ok;
		
	}
	
	public boolean registrarPersonaUsuario(Persona p){
		boolean ok = true;
		
		System.out.println("Inserción de persona en la base de datos");
		this.dataStore.save(p);
		
		return ok;
	}
	
	public int obtenerEtiquetaPersona(Usuario u, String p) {
		int etiqueta = 0;
		
		Query<Persona> q = this.dataStore.createQuery(Persona.class);
		
		Query<Usuario> user = this.dataStore.createQuery(Usuario.class);
		
		Usuario aux = user.field("idUsuarioTelegram").equal(u.getIdUsuarioTelegram())
								.get();
		
		//Como una imagen tiene propiedades referenciadas, si quiero hacer una consulta por de un valor de un atributo de una propiedad
		//lo tengo que hacer en varias consultas
		List<Persona> persona = q.field("nombre").equal(p)
								.field("user").equal(aux)
								.asList();
		
		//Si ya tengo alguna foto en la que salga esta persona: cojo su etiqueta
		if (persona.size() != 0) {
			etiqueta = persona.get(0).getEtiqueta();
		}
		//Si no tengo ninguna foto con esta persona, voy a ver cual es la primera etiqueta libre
		else {
			persona = q.field("user").equal(aux)
					.order("-etiqueta")
					.asList();
			
			//Si es la primera vez que subo una fotografia, la primera etiqueta que utilice para clasificar sera el 1
			if(persona.size() == 0) {
				etiqueta = 1;
			}
			//Si ya tengo a mas personas, cojo la etiqueta mas alta libre
			else {
				etiqueta = persona.get(0).getEtiqueta() + 1;
			}
		}
		
		
		return etiqueta;
	}
}
