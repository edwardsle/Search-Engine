package dao.mongo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class BasicDAO {
	
	protected MongoClient mongoClient;
	protected MongoDatabase mongoDB;
	
	public BasicDAO(String database) {
		mongoClient = MongoClients.create();   
		mongoDB = mongoClient.getDatabase(database);
	}
	
	public void close() { 
		mongoClient.close();
		mongoDB = null;
		mongoClient = null;
	}
	
}
