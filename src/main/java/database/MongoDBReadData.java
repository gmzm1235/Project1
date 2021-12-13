package database;
import com.mongodb.*;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;


public class MongoDBReadData {

    public static void data(String[] args) {
        //MongoClient mongoClient = (MongoClient) MongoClients.create();
        //MongoDatabase database = mongoClient.getDatabase("PRG_WiSe21_226");
        //MongoCollection<Document> collection = database.getCollection("employees");


        /*MongoCredential credential = MongoCredential.createCredential(properties.getProperty("PRG_WiSe21_226"),
                properties.getProperty("PRG_WiSe21_226"), properties.getProperty("E6g0Oa6p").toCharArray());

        MongoClientOptions options = MongoClientOptions.builder().sslEnabled(true).build();

        MongoClient mongoClient;
        mongoClient = new MongoClient(new ServerAddress(properties.getProperty("prg2021.texttechnologylab.org"),
                Integer.parseInt(properties.getProperty("27020"))), Collections.singletonList(credential), options);*/


    }
}
