import abstractclasses.Protokoll;
import database.MongoDBConnectionHandler;
import implementation.*;
import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.fit.factory.AggregateBuilder;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.hucompute.textimager.uima.gervader.GerVaderSentiment;
import org.hucompute.textimager.uima.spacy.SpaCyMultiTagger3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;

/***

 * die Location Path von den xml files wird von dem User in dem Class Main angefordert und
 * um dieser Folder zu lesen wird Class Main mithilfe der implementation.Plenarsitzung Class gearbeitet.
 * Die Methode, die den angeforderten Job ausführt, wird basierend auf der bereitgestellten Eingabe aufgerufen.
 * Wenn der User einen falschen Input eingibt, dann wird von dem User noch Mal neue Input angefordert
 *
 *
 */
public class Main {
    public static Plenarsitzung plenarsitzung;
    public static Scanner input;

    public static void main(String[] args) {
        try {
            JCasDeneme();
            System.exit(0);
            input = new Scanner(System.in);
            String folderlocation = "xmlfiles";
            System.out.println("Enter xmlfilespath : ");
            //folderlocation = input.nextLine();
            plenarsitzung = new Plenarsitzung("/home/gbatil/Downloads/xmlfiles");

            //plenarsitzung = new Plenarsitzung(folderlocation);
            System.out.println("reading from Files...");
            plenarsitzung.build();
            int choice;
            while (true) {
                System.out.println("Enter 1 : Redners");
                System.out.println("Enter 2 : Abgeordnete");
                System.out.println("Enter 3 : Fraktion Redners");
                System.out.println("Enter 4 : Protokoll Tagesordunungpunkttexts");
                System.out.println("Enter 5 : MongoDB test");
                System.out.println("Enter 6 : MongoDB insert all collection");
                System.out.println("Enter 7 : MongoDBremove all collection");
                System.out.println("Enter 9 : Exit");
                System.out.println();
                System.out.print("Enter your choice : ");
                choice = input.nextInt();
                input.nextLine();
                if (choice == 1) {
                    printRedners();
                }
                else if (choice == 2) {
                    printAbgeordnete();
                }
                else if (choice == 3) {
                    printFraktionRedners();
                }
                else if (choice == 4) {
                    printProtokollTagesordunungpunkttexts();
                }
                else if (choice == 5) {
                    MongodbTest();
                }
                else if (choice == 6) {
                    MongodbInsertAllCollections();
                }
                else if (choice == 7) {
                    MongodbDeleteAllCollections();
                }

                else if (choice == 9){
                    break;
                }
                else {
                    System.out.println("InValid Choice");
                }

            }

        } catch (Exception exp) {
            //System.out.println(exp.getClass().getName() +" : "+ exp.getMessage());
            exp.printStackTrace();
        }
    }

    /***
     * dieser Methode wird darstellt, um die Redner auszudrucken
     */
    public static void printRedners() {
        plenarsitzung.printAllRedners();

    }

    /***
     * dieser Methode wird darstellt, um die Abgeordnete auszudrucken
     */
    public static void printAbgeordnete() {
        plenarsitzung.printAllAbgeordnete();

    }

    /***
     * der Methode wird Informationen darüber, welcher Sprecher zu welcher Partei gehört geprint
     */
    public static void printFraktionRedners() {
        plenarsitzung.printAllFraktionRedners();

    }

    /***
     * der Methode, der der Text von einem gewählten Tagesordnungspunkt ausgedruckt wird.
     */
    public static void printProtokollTagesordunungpunkttexts() {
        String situngsnumber;
        String tagesornungspunktnumber;
        System.out.println("Enter situngsnumber : ");
        situngsnumber = input.nextLine();
        System.out.println("Enter tagesornungspunktnumber : ");
        tagesornungspunktnumber = input.nextLine();
        plenarsitzung.printProtokollTagesordnungspunkt(situngsnumber, tagesornungspunktnumber.trim());

    }
    public static void MongodbTest(){
        MongoDBConnectionHandler db = new MongoDBConnectionHandler();

        System.out.println("11000616 idli redner bilgileri :");
        Redner r = db.readRedner("11000616");
        if (r!=null){
            System.out.println(r.getId()+" "+r.getTitel()+" "+r.getVorname()+" "+ r.getNachname());
        }
        else {
            System.out.println("bulunamadi");
        }
        long DeleteCount = db.deleteRedner("11000616");
        if(DeleteCount > 0) {
            System.out.println(DeleteCount + " Kayit silindi .....");
        }
        else {
            System.out.println(DeleteCount + " 11000616 Kayit yok .....");
        }

        r= new Redner();
        r.setId("11001938");
        r.setTitel("Dr.");
        r.setVorname("Wolfgang");
        r.setNachname("Schäuble");
        db.updateRedner(r);
        Redner r2 = db.readRedner("11001938");
        if (r2!=null){
            System.out.println(r2.getId()+" "+r2.getTitel()+" "+r2.getVorname()+" "+ r2.getNachname());
        }
        else {
            System.out.println("bulunamadi");
        }


        DeleteCount = db.deleteProtocol("221");
        if(DeleteCount > 0) {
            System.out.println(DeleteCount + " 221 nolu Protokol silindi .....");
        }
        else {
            System.out.println(DeleteCount + " 221 nolu Protokol yok .....");
        }
        try {

            Protokoll prt = db.readProtocol("197");
            if (prt!=null){
                PrintProtocol(prt);
            }
            else {
                System.out.println(" 197 nolu Protokol yok .....");
            }


        }
        catch (Exception exp) {
            exp.printStackTrace();
        }





        //db.updateRedner() test edilicek***********************************



    }
    public static void PrintProtocol(Protokoll p) {
        if(p!=null) {
            System.out.println(p.getSitzungsnumber()+ " "+p.getTitel()+ " "+ p.getDatum());
            for (Tagesordnungspunkt t : p.getTagesordnungspunkts()) {
                System.out.println(t.getTopid());
                if (t.getReden() != null) {
                    for(Rede r : t.getReden()) {
                        if(r.getRedner() != null){
                            System.out.println(r.getId()+ " Redner : "+ r.getRedner().getId()+ " "+ r.getRedner().getTitel()+ " "+ r.getRedner().getVorname()+ " "+ r.getRedner().getNachname());
                        }
                        else {
                            System.out.println(r.getId());
                        }

                        for(String s: r.getRedetext()){
                            System.out.println(s);
                        }
                        System.out.println("---------------------------------------");
                    }
                }

                System.out.println("+++++++++++++++++++++++++++++++++++++++++++");
            }
        }

    }

    public static void MongodbInsertAllCollections(){
        MongoDBConnectionHandler db = new MongoDBConnectionHandler();
        db.CreateCollections();
        HashMap<String, Redner> rednerMap = plenarsitzung.getRednerMap();
        Iterator<String> itr = rednerMap.keySet().iterator();
        String rednerid;
        Redner redner;
        while (itr.hasNext()){
            rednerid = itr.next();
            redner = rednerMap.get(rednerid);
            db.insertRedner(redner);
        }
        System.out.println("rednerlar eklendi......");
        ArrayList<Protokoll> plist = plenarsitzung.getProtokollList();
        for (int i = 0;i<2;i++){
            db.insertProtocol(plist.get(i));

        }
        System.out.println("protocoller eklendi .....");






    }
    public static void MongodbDeleteAllCollections(){
        MongoDBConnectionHandler db = new MongoDBConnectionHandler();
        db.DeleteCollections();


    }
    public static void JCasDeneme() throws UIMAException {
        JCas jCas = JCasFactory.createJCas();
        jCas.setDocumentText("Dies ist ein Text.");
        jCas.setDocumentLanguage("de");

        AggregateBuilder builder = new AggregateBuilder();
        builder.add(createEngineDescription(SpaCyMultiTagger3.class,
                SpaCyMultiTagger3.PARAM_REST_ENDPOINT, "http://spacy.prg2021.texttechnologylab.org"));
        builder.add(createEngineDescription(GerVaderSentiment.class,
                GerVaderSentiment.PARAM_REST_ENDPOINT, "http://gervader.prg2021.texttechnologylab.org",
                GerVaderSentiment.PARAM_SELECTION , "text,de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence"));
        AnalysisEngine pAE = builder.createAggregate();
        SimplePipeline.runPipeline(jCas, pAE);


    }



}
