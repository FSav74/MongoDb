package it.mongodb.operation.find;

import java.util.List;

import it.mongodb.manager.ConfigurationProperty;
import it.mongodb.manager.DBManager;

import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.UpdateResult;

public class InsertOperation {
	
	private MongoCollection<Document> originCollection = null;

	public InsertOperation(String nameCollection) {

		originCollection = DBManager.INSTANCE.getDatabase(ConfigurationProperty.DATABASE_NAME)
                .getCollection(nameCollection);
	}

	public void scorriCursore(){
		//-------------------------------
		// Query di ricerca
		//-------------------------------
		BasicDBObject query = new BasicDBObject();
		query.put("body","sluts?");
		
		//------------------
		// Cursor
		//------------------
		FindIterable<Document> result = originCollection.find(query);
		MongoCursor<Document> iterator = result.iterator();
		
		ObjectId myObjectId = null;
		
		while(iterator.hasNext()){
			Document document = iterator.next();
//			ObjectId myObjectId = (ObjectId) document.get("_id");  //alternativa
			myObjectId =document.getObjectId("_id");
			
			Document doc = (Document)document.get("headers");
			List<String> headerTo = (List<String>)doc.get("To");
			
			System.out.println("_id:"+myObjectId);
			System.out.println("TO:" +headerTo);
			
		}
		
		
	}
	
	/**
	 * Aggiunge nell'array 'headers.To' due elementi (se non esistono)
	 * per le collection che il filter ha ritornato
	 * 
	 *  - faccio update sul db : $addToSet
	 *  
	 *  N.B. per fare l'update non avevo bisogno di fare un cursore e fare l'update su ogni elemento:
	 *  bastava fare direttamente l'update con il filtro query.put("body","sluts?");
	 */
	public void insertOneElementInArray(){
		
		System.out.println("Operazione di insert (su un array) effettuata con QUERY $addToSet....");
		
		
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
		updateQuery.put("$addToSet", updateQueryParam);
		
//		UpdateResult resultUpdate =originCollection.updateOne(filter,updateQuery);
		UpdateResult resultUpdate =originCollection.updateMany(filter,updateQuery);
		
		System.out.println("Result update:"+resultUpdate.getMatchedCount());
			

	
	}

	/**
	 * Aggiunge nell'array 'headers.To' due elementi (se non esistono)
	 * per le collection che il filter ha ritornato
	 * 
	 * - prendo il documento ritornato, lo modifico in MEMORIA e lo salvo.
	 * 
	 * utilizzo del metodo replace (save non utilizzabile : vedi sotto) 
	 */
	public void insertOneElementInArray2(){
		
		System.out.println("Operazione di insert (su un array) effettuata sull'oggetto java in MEMORIA....");
		
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
			
			//List<Document> headerTo = (List<Document>) document.get("headers.To");
			//List<String> headerTo = (List<String>) 
			Document doc = (Document)document.get("headers");
			List<String> headerTo = (List<String>)doc.get("To");
			
			//aggiungo il nuovo elemento sulla lista
			headerTo.add("martina.letterese@gmail.com");
			
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
}
