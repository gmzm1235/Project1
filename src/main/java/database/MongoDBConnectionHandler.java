package database;
import java.util.*;

import com.mongodb.*;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import implementation.*;


import java.util.Collections;

/***
 * In dieser KLasse MongoDBConnectionHandler wird die Verbindung zur Datenbank hergestellt.
 * Und in der CreateCollection werden dem Inhalt  der Redner und der  Protokolle Collection in der Databank erstellt.
 * in der DeleteCollection werden dem Inhalt der Collections aus der Databank gelöscht.
 * Ausserdem hat MongoDatabase mit Getter-Setter Methode definiert.
 */
public class MongoDBConnectionHandler {
    public Plenarsitzung plenarsitzung;
    private MongoDatabase database;
    public  MongoDBConnectionHandler(){

        connect();
    }


    public MongoDatabase getDatabase() {
        return database;
    }

    public void setDatabase(MongoDatabase database) {
        this.database = database;
    }

    public void connect(){
        Properties properties = new Properties();
        properties.setProperty("remote_user","PRG_WiSe21_226");
        properties.setProperty("remote_database","PRG_WiSe21_226");
        properties.setProperty("remote_password","E6g0Oa6p");
        properties.setProperty("remote_host","prg2021.texttechnologylab.org");
        properties.setProperty("remote_port","27020");
        properties.setProperty("remote_collections","protocol");

        MongoCredential credential = MongoCredential.createCredential(properties.getProperty("remote_user"),
                properties.getProperty("remote_database"), properties.getProperty("remote_password").toCharArray());
        MongoClientOptions options = MongoClientOptions.builder().sslEnabled(false).build();

        MongoClient mongoClient;
        mongoClient = new MongoClient(new ServerAddress(properties.getProperty("remote_host"),
                Integer.parseInt(properties.getProperty("remote_port"))), Collections.singletonList(credential), options);
        database = mongoClient.getDatabase(properties.getProperty("remote_database"));



    }
    public void CreateCollections () {
        try {
            database.createCollection("redners");
            database.createCollection("protocol");


        }
        catch (Exception exp){
            System.out.println("redners yaratilamadi");
        }

    }
    public void DeleteCollections () {
        database.getCollection("redners").deleteMany(new Document());
        database.getCollection("protocol").deleteMany(new Document());

    }

}



