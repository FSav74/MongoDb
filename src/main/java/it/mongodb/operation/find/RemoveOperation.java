package it.mongodb.operation.find;

import it.mongodb.manager.ConfigurationProperty;
import it.mongodb.manager.DBManager;

import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.UpdateResult;

public class RemoveOperation {

	private MongoCollection<Document> originCollection =null;
	public RemoveOperation(String nameCollection) {
		originCollection = DBManager.INSTANCE.getDatabase(ConfigurationProperty.DATABASE_NAME)
                .getCollection(nameCollection);
	}

	/**
	 * Elimino un elemento nell'array 'headers.To' 
	 * per le collection che il filter ha ritornato
	 * 
	 * - prendo il documento ritornato, lo modifico in memoria (eliminando l'elemento dall'array)  e lo salvo.
	 * 
	 * utilizzo del metodo replace (save non utilizzabile : vedi sotto) 
	 */
	public void removeElementInArray(){
		
		
		System.out.println("Operazione di Remove (su un array) effettuata sull'oggetto java in MEMORIA....");
		
		//-------------------------------
		// Query di ricerca
		//-------------------------------
		BasicDBObject query = new BasicDBObject();
		query.put("body","sluts?");
		
		//-----------------------------------------
		// Dichiaro variabili usate nel ciclo
		//----------------------------------------
		Document filter = new Document();
		BasicDBObject updateQuery = new BasicDBObject();
		BasicDBObject updateQueryParam = new BasicDBObject();
		ObjectId myObjectId = null;
		
		//------------------
		// Cursor
		//------------------
		FindIterable<Document> result = originCollection.find(query);
		MongoCursor<Document> iterator = result.iterator();
		
		//------------------
		// Loop sul Cursor
		//------------------
		while(iterator.hasNext()){
			Document document = iterator.next();
//			ObjectId myObjectId = (ObjectId) document.get("_id");  //alternativa
			myObjectId =document.getObjectId("_id");
			
			//List<Document> headerTo = (List<Document>) document.get("headers.To");   <<--------la dot notation qui non funziona
			Document doc = (Document)document.get("headers");
			List<String> headerTo = (List<String>)doc.get("To");
			
			//elimino il nuovo elemento sulla lista
			headerTo.remove("martina.letterese@gmail.com");
			
			//----------------------------------------------------------------------------------------------------------------------------
			//purtroppo il metodo 'save'
			// è solo delle DBCollection e non di MongoCollection : le prime (anche se funzionati) sono deprecate!!
			
			//originCollection.save(document);   <<<<<<<<< 
			
			//quindi niente metodo save ma dalla documentazione:
			//If the document contains an _id field, 
			//then the save() method is equivalent to an update with the upsert option set to true and the query predicate on the _id field.
			//----------------------------------------------------------------------------------------------------------------------------
			
			//Bson filter2 = Filters.eq("_id", document.getObjectId("_id"));  <<<<<alternativa per il filtro
			filter.put("_id",myObjectId);
			UpdateOptions options = new UpdateOptions();
			options.upsert(true);
			//-------------------------------------------------------------------------------
			//fondamentale usare il metodo replaca: updateOne andava in errore sul campo _id
			//-------------------------------------------------------------------------------
			UpdateResult resultUpdate = originCollection.replaceOne(filter, document,options);
			
			
			
			System.out.println("Result update:"+resultUpdate.getMatchedCount());
			
		}
	
	}
	
	/**
	 * Elimino un elemento nell'array 'headers.To' 
	 * per le collection che il filter ha ritornato
	 * 
	 * - voglio eseguire la query con $pull
	 * 
	 */
	public void removeElementInArray2(){
		
		System.out.println("Operazione di Remove (su un array) effettuata con QUERY $PULL....");
		
		//-------------------------------
		// Query di ricerca
		//-------------------------------
		BasicDBObject filter = new BasicDBObject();
		filter.put("body","sluts?");
		
		//-----------------------------------------
		// Dichiaro variabili usate nel ciclo
		//----------------------------------------
		BasicDBObject updateQuery = new BasicDBObject();
		BasicDBObject updateQueryParam = new BasicDBObject();
					
		//-----------------
		// Query di update 
		//-----------------

		//update
		updateQueryParam.put("headers.To", "martina.letterese@gmail.com");
		//updateQueryParam.put("headers.To", "aletterese@gmail.com");
		//addToSet non inserisce se già presente
		updateQuery.put("$pull", updateQueryParam);
		
//				UpdateResult resultUpdate =originCollection.updateOne(filter,updateQuery);
		UpdateResult resultUpdate =originCollection.updateMany(filter,updateQuery);
		
		System.out.println("Result update:"+resultUpdate.getMatchedCount());
		
	}
}
