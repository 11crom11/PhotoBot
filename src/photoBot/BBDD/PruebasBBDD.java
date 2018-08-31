package photoBot.BBDD;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import photoBot.Gate.Etiqueta;
import photoBot.Gate.ProcesadorFechas;
import photoBot.Imagen.Imagen;

public class PruebasBBDD {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		PhotoBotBBDD bd = new PhotoBotBBDD();
		
		Pair<Date, Date> pd= ProcesadorFechas.obtenerFormatoDate(
				new Etiqueta("FechaCompuestaTipo7", "abril del año 2018 a septiembre del 2018"));
		
		List<Imagen> lIm = bd.buscarImagenesPorRangoFecha(pd);
		
		int k = 0;
		int p = 6 + k;
		int g = 9;
		
		//Usuario u = new Usuario(598312, "Daniel", 23);
		//bd.crearUsuario(u);
		
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
