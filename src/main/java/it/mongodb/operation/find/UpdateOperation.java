package it.mongodb.operation.find;

import it.mongodb.manager.ConfigurationProperty;
import it.mongodb.manager.DBManager;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

public class UpdateOperation {
	
	private MongoCollection<Document> originCollection = null;

	public UpdateOperation(String collectionName) {
		originCollection = DBManager.INSTANCE.getDatabase(ConfigurationProperty.DATABASE_NAME).getCollection(collectionName);
	}
	
	/**
	 *  Metodo che esegue l'update di un elemento dell'array (non avendo la posizione):
	 *  How can i do?
	 *   - unwind and filter and then update.
	 *   - modify the first element of array which is returned by the filter (with operator $)  
	 *   		db.collection.update(
				   { <array>: value ... },
				   { <update operator>: { "<array>.$" : value } }
				)
	 *   -db.collection.updateMany(
			   <filter>,                  <------- la condizione messa qui mi ritorna l'intero document che contiene almeno un elemento dell
			                                        array che risponde al filtro
			   <update>,
			   {
			     .....
			     arrayFilters: [ <filterdocument1>, ... ]    <----------- la condizione messa qui invece filtra sugli elementi dell'array
			   }
		)
	 */
	public void updateOneElementInArray(){
		
		
		System.out.println("Operazione di Update (su un array) effettuata con query $ ....");
		
		Document query = new Document();
		query.put("body","sluts?");
		query.put("headers.To", "fsletterese@yahoo.it");
		

		Document queryUpdateParam = new Document();
		queryUpdateParam.put("headers.To.$", "fsletterese@gmail.com");
		Document queryUpdate = new Document();
		queryUpdate.put("$set", queryUpdateParam);
		
		originCollection.updateMany(query, queryUpdate);
		
		
	
	}

}
