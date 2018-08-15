package photoBot.BBDD;


import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;

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

	private MongoCollection<Usuario> cUsuarios;
	private MongoCollection<Persona> cPersonas;
	private MongoCollection<Imagen> cImagenes;
	private DBCollection cImagenesDos;

	
	public PhotoBotBBDD(){
		CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(com.mongodb.MongoClient.getDefaultCodecRegistry(), 
				CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));
		MongoClient mongoClient = MongoClients.create();
		MongoDatabase database = mongoClient.getDatabase("photobot").withCodecRegistry(pojoCodecRegistry);
		
		this.cUsuarios = database.getCollection("usuarios", Usuario.class);
		this.cPersonas = database.getCollection("personas_usuarios", Persona.class);
		this.cImagenes = database.getCollection("imagenes", Imagen.class);
		
		//this.cImagenesDos = database.getCollection("imagenes", Imagen.class);
	}
	
	public boolean existeUsuario(Integer idUsuarioTelegram){
		FindIterable<Usuario> cursor  = this.cUsuarios.find(Filters.and(Filters.exists("idUsuarioTelegram"), Filters.eq("idUsuarioTelegram", idUsuarioTelegram)));
		
		
		return cursor.first() != null ? true : false;
		
	}
	
	public boolean crearUsuario(Usuario usuario){
		boolean ok = false;
		
		boolean existeUsuario = this.existeUsuario(usuario.getIdUsuarioTelegram());
		
		if(!existeUsuario){
			this.cUsuarios.insertOne(usuario);
			
			ok = true;
		}
		
		return ok;
	}
	
	public boolean registrarImagen(Imagen imagen){
		boolean ok = true;
		
		this.cImagenes.insertOne(imagen);
		
		return ok;
	}
	
	public boolean actualizarInfoImagen(Imagen imagen){
		boolean ok = true;
		System.out.println("Actualizando informacion de la imagen");
		imagen.addEventoContextoImagen(new Evento("Mierda"));
		
		this.cImagenes.replaceOne(Filters.eq("_id", imagen.getId()), imagen);
		
		return ok;
		
	}
}
