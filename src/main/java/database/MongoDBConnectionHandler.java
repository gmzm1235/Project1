package database;
import java.io.InvalidClassException;
import java.util.*;

import abstractclasses.Protokoll;
import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import jdk.nashorn.internal.ir.AccessNode;
import org.bson.Document;
import implementation.*;
import org.bson.types.ObjectId;


import java.util.Arrays;
import java.util.Collections;


public class MongoDBConnectionHandler {
    public Plenarsitzung plenarsitzung;
    private MongoDatabase database;
    public MongoDBConnectionHandler(){

        connect();
    }

    private void connect(){
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
    public void insertProtocol (Protokoll protokoll) {
        MongoCollection<Document> protocols = database.getCollection("protocol");
        Document document = new Document("_id",new ObjectId());
        document.append("titel", protokoll.getTitel());
        document.append("datum", protokoll.getDatum());
        document.append("sitzungsnumber", protokoll.getSitzungsnumber());
        List<Document> lst = new ArrayList<>();
        Document doc;
        for(Tagesordnungspunkt t:protokoll.getTagesordnungspunkts()) {
            doc = new Document();
            doc.append("topid", t.getTopid());
            doc.append("reden", redelistdocument(t.getReden()));
            lst.add(doc);
        }
        document.append("tagesordnungspunkts",lst);
        protocols.insertOne(document);

    }
    public long deleteProtocol(String sitzungsnumber){
        MongoCollection<Document> collection = database.getCollection("protocol");
        DeleteResult  result;
        result = collection.deleteOne(new Document("sitzungsnumber", sitzungsnumber));
        return result.getDeletedCount();



    }
    public Protokoll readProtocol(String sitzungsnumber) throws Exception{
        MongoCollection<Document> collection = database.getCollection("protocol");
        Document doc;
        FindIterable<Document> itr;
        itr = collection.find(new Document("sitzungsnumber", sitzungsnumber));
        if(itr.cursor().hasNext()){
            Protokoll p = new Protokoll_File_Impl();
            ArrayList<Tagesordnungspunkt> tlist = new ArrayList<>();
            List<Document> lstdocument;
            doc = itr.first();
            String titel = (String) doc.get("titel");
            String datum = (String) doc.get("datum");
            if(doc.get("tagesordnungspunkts") instanceof List<?>) {
                lstdocument = (List<Document>) doc.get("tagesordnungspunkts");
                tlist = tagesordnungspunktList(lstdocument, p);
                System.out.println("tagescount : "+ tlist.size());
            }
            else {
                throw new Exception("error");
            }
            p.setSitzungsnumber(sitzungsnumber);
            p.setTagesordnungspunkts(tlist);
            p.setTitel(titel);
            p.setDatum(datum);
            return p;

        }
        else {
            return null;
        }

    }
    private ArrayList<Tagesordnungspunkt> tagesordnungspunktList(List<Document> tageslist, Protokoll protokoll) {
        ArrayList<Tagesordnungspunkt> tlist = new ArrayList<>();
        String topid;
        List<Document> redendoclist;
        Tagesordnungspunkt t;
        for(Document d : tageslist){
            topid = (String) d.get("topid");
            redendoclist = (List<Document>) d.get("reden");
            t = new Tagesordnungspunkt();
            t.setTopid(topid);
            //t.setReden(buildredelist(redendoclist , t));
            t.setProtokoll(protokoll);
            tlist.add(t);
        }
        return tlist;

    }
    private ArrayList<Rede> buildredelist (List<Document> rededoclist, Tagesordnungspunkt tagesordnungspunkt) {
        ArrayList<Rede> redelist = new ArrayList<>();
        String id;
        String rednerid;
        List<Document> redetextdoclist;
        List<String> redetextlist;
        Rede r;
        for(Document d : rededoclist) {
            id = (String) d.get("id");
            rednerid = d.getString("rednerid");
            //redetextdoclist = (List<Document>) d.get("redetext");
            redetextlist = (List<String>) d.get("redetext");

            r = new Rede();
            r.setId(id);
            r.setRedner(readRedner("rednerid"));
            //r.setRedetext(buildredetextlist(redetextdoclist));
            r.setRedetext(new ArrayList<>(redetextlist));
            r.setTagesordnungspunkt(tagesordnungspunkt);
            redelist.add(r);

        }
        return redelist;
    }
    private ArrayList<String> buildredetextlist (List<Document> redetextdoclist) {
        throw new UnsupportedOperationException();
    }

    private List<Document> redelistdocument(List<Rede> reden) {
        Document doc;
        List<Document> lst = new ArrayList<>();
        for(Rede r:reden) {
            doc = new Document();
            doc.append("id",r.getId());
            doc.append("rednerid", r.getRedner().getId());
            doc.append("redetext", r.getRedetext());
            lst.add(doc);

        }
        return lst;

    }
    public void insertRedner(Redner redner){

        MongoCollection<Document> redners = database.getCollection("redners");
        Document document = new Document("_id",new ObjectId());
        document.append("rednerid", redner.getId());
        document.append("titel", redner.getTitel());
        document.append("vorname", redner.getVorname());
        document.append("nachname", redner.getNachname());
        if (redner instanceof Abgeordnete) {
            Abgeordnete abg = (Abgeordnete) redner;
            document.append("fraktion", abg.getFraktion());
        }
        else {
            document.append("fraktion", "");
        }
        redners.insertOne(document);



    }
    public long updateRedner(Redner redner){
        MongoCollection<Document> redners = database.getCollection("redners");

        Document query;
        FindIterable<Document> itr;
        itr = redners.find(new Document("rednerid", redner.getId()));
        if(itr.cursor().hasNext()) {
            query = itr.first();
            Document newdoc = new Document("_id", query.get("_id"));

            newdoc.append("rednerid", redner.getId());
            newdoc.append("titel", redner.getTitel());
            newdoc.append("vorname", redner.getVorname());
            newdoc.append("nachname", redner.getNachname());
            if (redner instanceof Abgeordnete) {
                Abgeordnete abg = (Abgeordnete) redner;
                newdoc.append("fraktion", abg.getFraktion());
            } else {
                newdoc.append("fraktion", "");
            }
            UpdateResult result = redners.replaceOne(query, newdoc);
            return result.getModifiedCount();
        }

        return 0;
    }
    public long deleteRedner(String id){
        MongoCollection<Document> collection = database.getCollection("redners");
        DeleteResult  result;
        result = collection.deleteOne(new Document("rednerid", id));
        return result.getDeletedCount();



    }
    public Redner readRedner(String id){
        MongoCollection<Document> collection = database.getCollection("redners");
        Document doc;
        FindIterable<Document> itr;
        itr = collection.find(new Document("rednerid", id));
        if(itr.cursor().hasNext()){
            doc = itr.first();
            String strid = (String) doc.get("rednerid");
            String titel = (String) doc.get("titel");
            String vorname = (String) doc.get("vorname");
            String nachname = (String) doc.get("nachname");
            String fraktion = (String) doc.get("fraktion");
            if (fraktion.equals("")) {
                Redner redner = new Redner();
                redner.setId(id);
                redner.setTitel(titel);
                redner.setVorname(vorname);
                redner.setNachname(nachname);
                return redner;

            }
            else {
                Abgeordnete abg = new Abgeordnete();
                abg.setId(id);
                abg.setTitel(titel);
                abg.setVorname(vorname);
                abg.setNachname(nachname);
                abg.setFraktion(fraktion);
                return abg;
            }

        }
        else {
            return null;
        }

    }
    ///////////////////////////////////////////////////////////////





    public static void main(String[] args) {

        /*MongoCredential credential = MongoCredential.createCredential("PRG_WiSe21_226", "PRG_WiSe21_226", "E6g0Oa6p".toCharArray());
        MongoClientOptions options = MongoClientOptions.builder().sslEnabled(false).build();
        MongoClient mongoClient = new MongoClient(new ServerAddress("prg2021.texttechnologylab.org", 27020), Collections.singletonList(credential),options);
        MongoDatabase database = mongoClient.getDatabase("PRG_WiSe21_226");
        System.out.print("Check");
        MongoCollection<Document> protocolCollection = database.getCollection("protocol");
        Document document = new Document("name", "Skywalker").append("age", 60);
        protocolCollection.insertOne(document);*/
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
        MongoDatabase database = mongoClient.getDatabase(properties.getProperty("remote_database"));
        MongoCollection<Document> collection = database.getCollection(properties.getProperty("remote_collections"));







        //MongoCollection<Document> protocolCollection = (MongoCollection<Document>) database.getCollection(properties.getProperty("remote_collections"));
        //Document document = new Document("name", "Skywalker").append("age", 60);
        //protocolCollection.insertOne(document);






    }

}



