package database;

import abstractclasses.Protokoll;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import implementation.Abgeordnete;
import implementation.Redner;
import org.bson.Document;
import org.bson.types.ObjectId;
import database.MongoDBConnectionHandler;

import java.util.ArrayList;

/***
 * In dieser KLasse werden Prozesse(Insert, Delete, Read, Update) in der Datenbank mit Redner durchgeführt.
 */

public class Redner_MonngoDB_File_Impl {
    public static MongoDBConnectionHandler mongoDBConnectionHandler;
    private MongoDatabase database;

    public Redner_MonngoDB_File_Impl (MongoDBConnectionHandler connection) {
        this.mongoDBConnectionHandler = connection;
        this.database = connection.getDatabase();
    }

    /***
     * In dieser Methode werden der Redner in dem MongoDB hinzugefügt.
     * @param redner
     */

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

    /***
     * In dieser Methode werden der Redner in dem MongoDB aktualisiert.
     * @param redner
     * @return
     */
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

    /***
     * in dieser Methode wird der Redner, der bestimmten Parameter (rednerid) hat, aus der MongoDB gelöscht.
     * @param id
     * @return long result.getDeletedCount()
     */
    public long deleteRedner(String id){
        MongoCollection<Document> collection = database.getCollection("redners");
        DeleteResult result;
        result = collection.deleteOne(new Document("rednerid", id));
        return result.getDeletedCount();



    }

    /***
     * in dieser Methode wird der Redner, der bestimmten Parameter (rednerid) hat, aus der MongoDB gelesen.
     * @param id
     * @return redner
     */
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

    /***
     * in dieser Methode werden alle Redner aus der MongoDB gelesen.
     * @return ArrayList<Redner> rednerArrayList
     */
    public ArrayList<Redner> readAllRednersfromMongo() {
        MongoCollection<Document> collection = database.getCollection("redners");
        Document doc;
        FindIterable<Document> itr;
        itr = collection.find();
        MongoCursor<Document> cursor = itr.iterator();
        String rednerid;
        ArrayList<Redner> rednerArrayList = new ArrayList<>();
        while (cursor.hasNext()) {
            doc = cursor.next();
            rednerid = doc.getString("rednerid");
            try {
                System.out.println("rednerid : " + rednerid);
                //strid = readRedner(r);
                //System.out.println(protokoll.getSitzungsnumber());
                //protokolls.add(protokoll);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        cursor.close();
        return rednerArrayList;
    }
}
