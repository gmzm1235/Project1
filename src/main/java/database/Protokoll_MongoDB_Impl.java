package database;

import abstractclasses.Protokoll;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import implementation.Protokoll_File_Impl;
import implementation.Rede;
import implementation.Tagesordnungspunkt;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/***
 * In dieser KLasse werden Prozesse(Insert, Delete, Read) in der Datenbank mit Protokollen durchgeführt.
 *
 */

public class Protokoll_MongoDB_Impl {
    public  Redner_MonngoDB_File_Impl rednerMongoDBFile;
    public MongoDBConnectionHandler mongoDBConnectionHandler;
    private MongoDatabase database;


    public Protokoll_MongoDB_Impl(MongoDBConnectionHandler connection) {
        this.mongoDBConnectionHandler = connection;
        this.database = connection.getDatabase();
        this.rednerMongoDBFile = new Redner_MonngoDB_File_Impl(connection);
    }

    /***
     * In dieser Methode wird das Protocol in dem MongoDB hinzugefügt.
     * @param protokoll
     */
    public void insertProtocol (Protokoll protokoll) {
            MongoCollection<Document> protocols = database.getCollection("protocol");
            Document document = new Document("_id",new ObjectId());
            document.append("titel", protokoll.getTitel());
            document.append("datum", protokoll.getDatum());
            document.append("sitzungsnumber", protokoll.getSitzungsnumber());
            List<Document> lst = new ArrayList<>();
            Document doc;
            if(protokoll.getTagesordnungspunkts() != null){
                for(Tagesordnungspunkt t:protokoll.getTagesordnungspunkts()) {
                    doc = new Document();
                    doc.append("topid", t.getTopid());
                    doc.append("reden", redelistdocument(t.getReden()));
                    lst.add(doc);
                }
            }

            document.append("tagesordnungspunkts",lst);
            protocols.insertOne(document);




    }

    /***
     * in dieser Methode wird das Protocol, das bestimmten Parameter (sitzungsnumber) hat, aus der MongoDB gelöscht.
     * @param sitzungsnumber
     * @return
     */
    public long deleteProtocol(String sitzungsnumber){
        MongoCollection<Document> collection = database.getCollection("protocol");
        DeleteResult result;
        result = collection.deleteOne(new Document("sitzungsnumber", sitzungsnumber));
        return result.getDeletedCount();



    }

    /***
     * in dieser Methode wird das Protocol, das bestimmten Parameter (sitzungsnumber) hat, aus der MongoDB gelesen.
     * @param sitzungsnumber
     * @return
     * @throws Exception
     */
    public Protokoll readProtocol(String sitzungsnumber) throws Exception{
        MongoCollection<Document> collection = database.getCollection("protocol");
        Document doc;
        FindIterable<Document> itr;
        itr = collection.find(new Document("sitzungsnumber", sitzungsnumber));
        MongoCursor<Document> cursor = itr.iterator();
        if(cursor.hasNext()){
            Protokoll p = new Protokoll_File_Impl();
            ArrayList<Tagesordnungspunkt> tlist = new ArrayList<>();
            List<Document> lstdocument;
            doc = cursor.next();
            String titel = (String) doc.get("titel");
            String datum = (String) doc.get("datum");
            if(doc.get("tagesordnungspunkts") instanceof List<?>) {
                lstdocument = (List<Document>) doc.get("tagesordnungspunkts");
                tlist = tagesordnungspunktList(lstdocument, p);
                //System.out.println("tagescount : "+ tlist.size());
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

    /***
     * in dieser Methode werden alle Protokolle aus der MongoDB gelesen.
     * @return
     */
    public ArrayList<Protokoll> readAllProtocolsfromMongo() {
        MongoCollection<Document> collection = database.getCollection("protocol");
        Document doc;
        FindIterable<Document> itr;
        itr = collection.find();
        MongoCursor<Document> cursor = itr.iterator();
        String sitzungsnumber;
        Protokoll protokoll;
        ArrayList<Protokoll> protokolls = new ArrayList<>();
        int i = 0;
        while (cursor.hasNext() && i <1 ) {
            doc = cursor.next();
            sitzungsnumber = doc.getString("sitzungsnumber");
            try {
                System.out.println("sitzungsnumber : " + sitzungsnumber);
                protokoll = readProtocol(sitzungsnumber);
                System.out.println(protokoll.getSitzungsnumber());
                protokolls.add(protokoll);

            } catch (Exception e) {
                e.printStackTrace();
            }
            i++;

        }
        return protokolls;
    }

    /***
     * In dieser Methode werden die Tagesordnungspunkten als Arraylist erstellt, um die Protokolle einfacher zu lesen.
     * @param tageslist
     * @param protokoll
     * @return
     */

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
            t.setReden(buildredelist(redendoclist , t));
            t.setProtokoll(protokoll);
            tlist.add(t);
        }
        return tlist;

    }

    /***
     * In dieser Methode werden die Redetext als Arraylist erstellt, um die Protokolle einfacher zu lesen.
     * @param rededoclist
     * @param tagesordnungspunkt
     * @return
     */
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
            redetextlist = d.getList("redetext",String.class);


            r = new Rede();
            r.setId(id);
            r.setRedner(rednerMongoDBFile.readRedner(rednerid));
            r.setRedetext(new ArrayList<>(redetextlist));
            r.setTagesordnungspunkt(tagesordnungspunkt);
            redelist.add(r);

        }
        return redelist;
    }

    /***
     * in dieser Methode wird jede Redetext als Document erstellt, um die Protokolle zu lesen.
     * @param reden
     * @return
     */
    private List<Document> redelistdocument(List<Rede> reden) {
        Document doc;
        List<Document> lst = new ArrayList<>();
        for(Rede r:reden) {
            doc = new Document();
            doc.append("id",r.getId());
            if(r.getRedner() != null){
                doc.append("rednerid", r.getRedner().getId());
            }
            else {
                System.out.println("Rede : "+ r.getId()+ " Tages : "+ r.getTagesordnungspunkt().getTopid()+ "Protocol : "+ r.getTagesordnungspunkt().getProtokoll().getSitzungsnumber());
                doc.append("rednerid", "");
            }

            doc.append("redetext", r.getRedetext());
            lst.add(doc);

        }
        return lst;

    }

}
