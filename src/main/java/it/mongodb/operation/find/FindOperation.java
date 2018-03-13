package it.mongodb.operation.find;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.security.auth.login.Configuration;

import it.mongodb.manager.ConfigurationProperty;
import it.mongodb.manager.DBManager;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

public class FindOperation {
	
	private MongoCollection<Document> originCollection = null;
	
	public FindOperation(String nameCollection){
		originCollection = DBManager.INSTANCE.getDatabase(ConfigurationProperty.DATABASE_NAME)
                .getCollection(nameCollection);
	}

	/**
	 *  Metodo che esegue una count
	 * 
	 */
	public void count() {
		BasicDBObject dbObject = new BasicDBObject();
		dbObject.put("headers.From","andrew.fastow@enron.com");
		dbObject.put("headers.To","jeff.skilling@enron.com");
		
		long result = originCollection.count(dbObject);
		System.out.println("CONTO:"+result);
		
	}

	/**
	 *  Metodo che esegue una query con un $IN
	 * 
	 */
	public void findIn(){
		//-----------------------------
		//---ESEMPIO di CLAUSOLA IN
		//-----------------------------
		//QUERY:
		//db.getCollection('messages').find(
		//{body : "sluts?","headers.To": {$in: ["timothy.blanchard@enron.com","shanna.husser@enron.com"]}   })
		//-----------------------------
		
		BasicDBObject inClause = new BasicDBObject();
		
		//-- ArrayList
		List<String> recipers = new ArrayList<String>();
		recipers.add("timothy.blanchard@enron.com");
		recipers.add("shanna.husser@enron.com");
		
		//------------------------------------------
		// From ArrayList --> BasicDBList
		//------------------------------------------
		BasicDBList listaRecipers = new BasicDBList();
		listaRecipers.addAll(recipers);
		
		inClause.put("$in",listaRecipers);
		
		BasicDBObject query = new BasicDBObject();
		query.put("body","sluts?");
		query.put("headers.To", inClause);
		
		int count=0;
		int cycle =0;
		//------------------
		// Cursor
		//------------------
		FindIterable<Document> result = originCollection.find(query);
		MongoCursor<Document> iterator = result.iterator();
		while(iterator.hasNext()){
			cycle++;
			Document document = iterator.next();
//			Document headers = (Document) document.get("headers");
//			List<String> to = (List<String>) headers.get("To");
//			System.out.println("body:"+to);
			String body = (String) document.get("body");
			if (body.equals("sluts?")) count++;
				
		}
		System.out.println("CONTO:"+count);
		System.out.println("CONTO:"+cycle);
	}

	
	/**
	 * Find : ritorna solo elemento dell'array
	 * Si può usare la notazione .$ MA solo se nel filter la consdizione e anche sull'array
	 * 
	 * db.getCollection('messages').find({"_id" : ObjectId("4f16fc97d1e2d32371003f41"),"headers.To":"jim.schwieger@enron.com"},{"_id":0,"headers.To.$":1})
	 * in questo modo mi ritorna solo jim.schwieger@enron.com ma solo perchè lo avevo messo nel filtro
	 * 
	 * 
	 * 
	 */
	public void recuperaSoloElementiArray(){
		//-----------------------------
		//---ESEMPIO di CLAUSOLA IN
		//-----------------------------
		//QUERY:
		//db.getCollection('messages').find(
		//{body : "sluts?","headers.To": {$in: ["timothy.blanchard@enron.com","shanna.husser@enron.com"]}   })
		//-----------------------------
		
		BasicDBObject inClause = new BasicDBObject();
		
		//-- ArrayList
		List<String> recipers = new ArrayList<String>();
		recipers.add("timothy.blanchard@enron.com");
		recipers.add("shanna.husser@enron.com");
		
		//------------------------------------------
		// From ArrayList --> BasicDBList
		//------------------------------------------
		BasicDBList listaRecipers = new BasicDBList();
		listaRecipers.addAll(recipers);
		
		inClause.put("$in",listaRecipers);
		
		BasicDBObject query = new BasicDBObject();
		query.put("body","sluts?");
		query.put("headers.To", inClause);
		
		Document myProjection = new Document("_id",0).append("headers.To.$",1);
		
		
		int count=0;
		int cycle =0;
		//------------------
		// Cursor
		//------------------
		FindIterable<Document> result = originCollection.find(query).projection(myProjection);
		MongoCursor<Document> iterator = result.iterator();
		while(iterator.hasNext()){
			cycle++;
			Document document = iterator.next();
//			Document headers = (Document) document.get("headers");
//			List<String> to = (List<String>) headers.get("To");
			
			Document headers = (Document) document.get("headers");
			List<String> to = (List<String>) headers.get("To");
			System.out.println("document:"+to);
//			String body = (String) document.get("body");
//			if (body.equals("sluts?")) count++;
				
		}
		System.out.println("CONTO:"+count);
		System.out.println("CONTO:"+cycle);
	}
}
