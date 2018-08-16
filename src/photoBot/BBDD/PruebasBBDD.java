package photoBot.BBDD;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import photoBot.Imagen.Persona;
import photoBot.Imagen.Usuario;

public class PruebasBBDD {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		PhotoBotBBDD bd = new PhotoBotBBDD();
		
		Usuario u = new Usuario(598312, "Daniel", 23);
		bd.crearUsuario(u);
		
				//collection.insertOne(p);
		//boolean existe = bd.existeUsuario(598313);
		//System.out.print(existe == true ? "existe" : "no existe");
		
		/*
		CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(com.mongodb.MongoClient.getDefaultCodecRegistry(), 
				CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));
		
		MongoClient mongoClient = MongoClients.create();
		MongoDatabase database = mongoClient.getDatabase("photobot").withCodecRegistry(pojoCodecRegistry);
		MongoCollection<Persona> collection = database.getCollection("usuarios", Persona.class);
		
		List<Persona> lPersonas = new ArrayList<>();
		lPersonas.add(new Persona("Ivan", 23));
		
		//Persona p = new Persona("Daniel", 23, lPersonas);
		
		//collection.insertOne(p);
		*/
		/**
		Document doc = new Document("name", "MongoDB")
	                .append("type", "database")
	                .append("count", 1)
	                .append("versions", Arrays.asList("v3.2", "v3.0", "v2.6"))
	                .append("info", new Document("x", 203).append("y", 102));
		 
		collection.insertOne(doc);
		*/
		
		/*
		MongoCursor<Persona> cursor = collection.find(Filters.eq("nombre", "Daniel")).iterator();
		
		try {
		    while (cursor.hasNext()) {
		        System.out.println(cursor.next().getNombre() + " " + cursor.next().getEdad());
		    }
		    
		} finally {
		    cursor.close();
		}
		*/
		
	}

}
