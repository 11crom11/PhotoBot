package photoBot.BBDD;

import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import photoBot.Imagen.Imagen;
import photoBot.Imagen.Persona;
import photoBot.Imagen.Usuario;

public class PhotoBotBBDD {

	private MongoCollection<Usuario> cUsuarios;
	private MongoCollection<Persona> cPersonas;
	private MongoCollection<Imagen> cImagenes;

	
	public PhotoBotBBDD(){
		CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(com.mongodb.MongoClient.getDefaultCodecRegistry(), 
				CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));
		MongoClient mongoClient = MongoClients.create();
		MongoDatabase database = mongoClient.getDatabase("photobot").withCodecRegistry(pojoCodecRegistry);
		
		this.cUsuarios = database.getCollection("usuarios", Usuario.class);
		this.cPersonas = database.getCollection("personas_usuarios", Persona.class);
		this.cImagenes = database.getCollection("imagenes", Imagen.class);
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
}
