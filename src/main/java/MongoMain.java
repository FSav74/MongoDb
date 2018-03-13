import it.mongodb.operation.find.FindOperation;
import it.mongodb.operation.find.InsertOperation;
import it.mongodb.operation.find.RemoveOperation;
import it.mongodb.operation.find.UpdateOperation;

import java.net.UnknownHostException;
import java.util.Date;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;



public class MongoMain {

	/**
	 * 	The general advice is to use :
	 *  Document/MongoCollection/MongoDatabase 
	 *  for new application development.

		The DBObject/DBCollection/DB classes, however, remain in the 3.0.0 driver so that many existing applications 
		that currently use the 2.x driver series have an upgrade path to the 3.0 driver. 
		The driver authors (I'm one of them) tried to make this clear by deprecating the MongoClient.getDB method,
		which returns an instance of the DB class, in order to encourage use of the new MongoClient.getDatabase method,
		which returns an instance of MongoDatabase.
	 * 
	 * 
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		System.out.println("Starting ......");

		FindOperation findOperation = new FindOperation("messages");
		//findOperation.count();
		
		//findOperation.findIn();
		
//		InsertOperation myInsert = new InsertOperation("messages");
//		myInsert.insertOneElementInArray();	
		//myInsert.scorriCursore();
		
		
//		RemoveOperation op = new RemoveOperation("messages");
//		op.removeElementInArray2();
		
		
//  ** UPDATE element dell'array **//		
//		UpdateOperation op = new UpdateOperation("messages");
//		op.updateOneElementInArray();
		
		findOperation.recuperaSoloElementiArray();
		
		
		/**
		 * esempio di autenticazione - START
		 * 
		 */
//		Mongo mongo = new Mongo("localhost", 27017);
//		DB db = mongo.getDB("testdb");
//
//		boolean auth = db.authenticate("testdb", "password".toCharArray());
//		if (auth) {
		/**
		 * esempio di autenticazione - END
		 * 
		 */
		
		
		
		
		
		

//		/**** Get database ****/
//		// if database doesn't exists, MongoDB will create it for you
//		DB db = mongo.getDB("saveriodb");
//
//		/**** Get collection / table from 'testdb' ****/
//		// if collection doesn't exists, MongoDB will create it for you
//		DBCollection table = db.getCollection("users");

		
		
		/**** Insert ****/
		// create a document to store key and value
//		BasicDBObject document = new BasicDBObject();
//		document.put("name", "Pippo");
//		document.put("age", 30);
//		document.put("createdDate", new Date());
//		table.insert(document);
		
		
		

//		/**** Find and display ****/
//		BasicDBObject searchQuery = new BasicDBObject();
//		searchQuery.put("username", "saverio");
//
//		DBCursor cursor = table.find(searchQuery);
//
//		while (cursor.hasNext()) {
//			System.out.println(cursor.next());
//		}
//
//		System.out.println("Done");
//		
//		
//		/*** Update ***/
//		BasicDBObject newDocument = new BasicDBObject();
//
//		/*** NON CORRETTO: La riga sotto sostituisce completamente il documento ***/
////		newDocument.put("clients", 110);
//		
//		/*** Questa riga invece si comporta bene e aggiunge il documento ***/
//		newDocument.append("$set", new BasicDBObject().append("clients", 110));
//
//		BasicDBObject searchQuery2 = new BasicDBObject().append("name", "Pippo");
//
//		table.update(searchQuery2, newDocument);
		
		
		
		
	}

}
