package photoBot.Utilidades;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;


//Sacado de aqui: https://examples.javacodegeeks.com/java-basics/exceptions/java-io-notserializableexception-how-to-solve-not-serializable-exception/

public class SerializadorObjeto {
	/**
	 * Converts an Object to a byte array.
	 * @param <T>
	 * 
	 * @param object, the Object to serialize.
	 * @return, the byte array that stores the serialized object.
	 */
	public static  <T> byte[] serialize(T object) {

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = null;
		try {
			out = new ObjectOutputStream(bos);
			out.writeObject(object);

			byte[] byteArray = bos.toByteArray();
			return byteArray;

		} catch (IOException e) {
			e.printStackTrace();
			return null;

		} finally {
			try {
				if (out != null)
					out.close();
			} catch (IOException ex) {
			}
			try {
				bos.close();
			} catch (IOException ex) {
			}
		}

	}

	/**
	 * Converts a byte array to an Object.
	 * 
	 * @param byteArray, a byte array that represents a serialized Object.
	 * @return, an instance of the Object class.
	 */
	public static Object deserialize(byte[] byteArray) {
		ByteArrayInputStream bis = new ByteArrayInputStream(byteArray);
		ObjectInput in = null;
		try {
			in = new ObjectInputStream(bis);
			Object o = in.readObject();
			return o;

		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				bis.close();
			} catch (IOException ex) {
			}
			try {
				if (in != null)
					in.close();
			} catch (IOException ex) {
			}
		}
	}
	
}
