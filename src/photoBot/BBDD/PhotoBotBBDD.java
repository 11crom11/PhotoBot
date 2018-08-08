package photoBot.BBDD;

import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import photoBot.Imagen.Persona;

public class PhotoBotBBDD {

	private MongoCollection<Persona> cPersonas;
	
	public PhotoBotBBDD(){
		CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(com.mongodb.MongoClient.getDefaultCodecRegistry(), 
				CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));
		MongoClient mongoClient = MongoClients.create();
		MongoDatabase database = mongoClient.getDatabase("photobot").withCodecRegistry(pojoCodecRegistry);
		
		
		cPersonas = database.getCollection("usuario_personas_imagenes", Persona.class);
	}
}
