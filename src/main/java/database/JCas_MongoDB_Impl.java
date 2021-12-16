package database;

import abstractclasses.Protokoll;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.constituent.S;
import implementation.Protokoll_File_Impl;
import implementation.Rede;
import implementation.Tagesordnungspunkt;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/***
 * In dieser KLasse wird das JcasXml, das die Informationen über eigene protokolid, topid , redeid hat, in der MongoDB eingefügt.
 */

public class JCas_MongoDB_Impl {
    public MongoDBConnectionHandler mongoDBConnectionHandler;
    private MongoDatabase database;

    public  JCas_MongoDB_Impl(MongoDBConnectionHandler mongoDBConnectionHandler) {
        this.mongoDBConnectionHandler = mongoDBConnectionHandler;
        this.database = mongoDBConnectionHandler.getDatabase();
    }
    public void InsertJcasXml (String jcasxml, Rede rede) {
        MongoCollection<Document> collection = database.getCollection("uima");
        Document document = new Document("_id", new ObjectId());
        document.append("sitzungsnumber", rede.getTagesordnungspunkt().getProtokoll().getSitzungsnumber());
        document.append("tagesordnungspunkttopid", rede.getTagesordnungspunkt().getTopid());
        document.append("redeid", rede.getId());
        document.append("xml", jcasxml);
        collection.insertOne(document);
    }
}
