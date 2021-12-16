import abstractclasses.Protokoll;
import database.MongoDBConnectionHandler;
import de.tudarmstadt.ukp.dkpro.core.api.ner.type.NamedEntity;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import implementation.*;
import database.*;
import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.impl.XCASSerializer;
import org.apache.uima.fit.factory.AggregateBuilder;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.bson.Document;
import org.hucompute.textimager.uima.gervader.GerVaderSentiment;
import org.hucompute.textimager.uima.spacy.SpaCyMultiTagger3;
import org.hucompute.textimager.uima.type.Sentiment;
import org.xml.sax.SAXException;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;


/***

 * die Location Path von den xml files wird von dem User in dem Class Main angefordert und
 * um dieser Folder zu lesen wird Class Main mithilfe der implementation.Plenarsitzung Class gearbeitet.
 * Die Methode, die den angeforderten Job ausführt, wird basierend auf der bereitgestellten Eingabe aufgerufen.
 * Nach Eingabe des Benutzers kann Main auch mit MongoDB verbunden und
 * Operationen(Delete, Insert, Update, Read) mit MongoDB ausgeführt werden.
 * Wenn der User einen falschen Input eingibt, dann wird von dem User noch Mal neue Input angefordert
 *
 *
 */
public class Main {
    public static Plenarsitzung plenarsitzung;
    public static Scanner input;
    public static MongoDBConnectionHandler db;

    public static void main(String[] args) {
        try {
            db = new MongoDBConnectionHandler();
            Thread.sleep(500);
            input = new Scanner(System.in);
            int choice;
            while (true) {
                System.out.println("Enter 0 : Read all Xml Files");
                System.out.println("Enter 1 : Redners");
                System.out.println("Enter 2 : Abgeordnete");
                System.out.println("Enter 3 : Fraktion Redners");
                System.out.println("Enter 4 : Protokoll Tagesordunungpunkttexts");
                System.out.println("Enter 5 : MongoDB test");
                System.out.println("Enter 6 : MongoDB insert all collection");
                System.out.println("Enter 7 : MongoDB remove all collection");
                System.out.println("Enter 8 : Show MongoDB Protocol");
                System.out.println("Enter 9 : CasObjeckt + Pipeline");
                System.out.println("Enter 10 : Exit");
                System.out.println();
                System.out.print("Enter your choice : ");
                choice = input.nextInt();
                input.nextLine();
                if (choice == 0) {
                    String folderlocation = "xmlfiles";
                    System.out.println("Enter xmlfilespath : ");
                    folderlocation = input.nextLine();
                    plenarsitzung = new Plenarsitzung(folderlocation);
                    //plenarsitzung = new Plenarsitzung("/home/gbatil/Downloads/xmlfiles");

                    //plenarsitzung = new Plenarsitzung(folderlocation);
                    System.out.println("reading from Files...");
                    plenarsitzung.build();
                }
                else if (choice == 1) {
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
                else if (choice == 8) {
                    MongoShowProtocol();
                    //MongoShowRedner();
                }
                else if (choice == 9) {
                    MyJCas();
                }

                else if (choice == 10){
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
     * die Methode wird Informationen darüber, welcher Sprecher zu welcher Partei gehört geprint
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

    /***
     * die Methode wird  das Erstellen,
     * Lesen, Updaten und Löschen von Dokumenten in der MongoDB getestet.
     */
    public static void MongodbTest(){

        Redner_MonngoDB_File_Impl rmongo = new Redner_MonngoDB_File_Impl(db);
        Protokoll_MongoDB_Impl pmongo = new Protokoll_MongoDB_Impl(db);
        System.out.println("Test über readRedner :");
        System.out.println("Redner ( 11000616 redenid ) Informationen :");
        Redner r = rmongo.readRedner("11000616");
        if (r!=null){
            System.out.println(r.getId()+" "+r.getTitel()+" "+r.getVorname()+" "+ r.getNachname());
        }
        else {
            System.out.println("der Redner existiert nicht");
        }
        System.out.println("Test über deleteRedner :");
        System.out.println("Löscht Redner rednerid : 11000616");
        long DeleteCount = rmongo.deleteRedner("11000616");
        if(DeleteCount > 0) {
            System.out.println(DeleteCount + " der Eintrag Redner wird gelöscht..........");
        }
        else {
            System.out.println(DeleteCount + " 11000616 id Eintrag existiert nicht");
        }
        System.out.println("Test über Update redner : ");
        System.out.println(" Bsp : Wir aktualisieren nur die Titel ");


        r= new Redner();
        r.setId("11001938");
        r.setTitel("Dr.");
        r.setVorname("Wolfgang");
        r.setNachname("Schäuble");
        rmongo.updateRedner(r);
        Redner r2 = rmongo.readRedner("11001938");
        if (r2!=null){
            System.out.println(r2.getId()+" "+r2.getTitel()+" "+r2.getVorname()+" "+ r2.getNachname());
        }
        else {
            System.out.println("der Eintrag existiert nicht.........");
        }

        System.out.println("Test über Delete Protocol");
        System.out.println("Löscht 221 Protocol");
        DeleteCount = pmongo.deleteProtocol("221");
        if(DeleteCount > 0) {
            System.out.println(DeleteCount + " 221 Protokoll wird gelöscht......");
        }
        else {
            System.out.println(DeleteCount + " 221 Protokoll existiert nicht....");
        }
        try {
            System.out.println("Test über Read Protocol ");
            System.out.println("Read 197 Protokoll");
            Protokoll prt = pmongo.readProtocol("197");
            if (prt!=null){
                PrintProtocol(prt);
            }
            else {
                System.out.println("197 Protokoll existiert nicht....");
            }


        }
        catch (Exception exp) {
            exp.printStackTrace();
        }








    }


    /***
     * Diese Methode wird die Redner aus der MongoDB Databank gelesen und rufen wir PrintRedner Methode auf ,
     * um die Redner auszudrucken.
     *
     * @throws Exception
     */
    public static void MongoShowRedner() throws Exception {
        ArrayList<Redner> redners = new ArrayList<>();

        Redner_MonngoDB_File_Impl rmongo = new Redner_MonngoDB_File_Impl(db);
        redners = rmongo.readAllRednersfromMongo();
        for(Redner r: redners){
            PrintRedner(r);
        }

    }

    /***
     * Diese Methode wird die Protocols aus der MongoDB Databank gelesen und ausgedruckt.
     * @throws Exception
     */
    public static void MongoShowProtocol() throws Exception {
        System.out.println("Enter sitzungsnumber : ");
        String sitzungsnumber = input.nextLine();
        Protokoll_MongoDB_Impl pmongo = new Protokoll_MongoDB_Impl(db);
        Protokoll p = pmongo.readProtocol(sitzungsnumber);
        PrintProtocol(p);
    }

    /***
     * Diese Methode ist eine  HilfsMethode. Wir verwenden sie, um die Redner auszudrucken.
     * @param r
     */
    public static void PrintRedner(Redner r) {
        if(r!=null) {
            System.out.println(r.getId()+ " "+ r.getTitel()+ " "+ r.getVorname()+" "+ r.getNachname());
        }
    }

    /***
     * Diese Methode ist eine HilfsMethode. Wir verwenden sie, um die Protocols auszudrucken.
     * @param p
     */
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

    /***
     * diese Methode wird sich von CLass Redner_MonngoDB_File_Impl und CLass Protokoll_MongoDB_Impl zu Nutze gemacht.
     * Sie wird die Collections (Redner und Protokolle) zu MongoDB Databank eingefügt.
     */

    public static void MongodbInsertAllCollections(){
        Redner_MonngoDB_File_Impl rmongo = new Redner_MonngoDB_File_Impl(db);
        Protokoll_MongoDB_Impl pmongo = new Protokoll_MongoDB_Impl(db);
        db.CreateCollections();
        HashMap<String, Redner> rednerMap = plenarsitzung.getRednerMap();
        Iterator<String> itr = rednerMap.keySet().iterator();
        String rednerid;
        Redner redner;
        while (itr.hasNext()){
            rednerid = itr.next();
            redner = rednerMap.get(rednerid);
            rmongo.insertRedner(redner);
        }
        System.out.println("die Redner werden hinzugefügt......");
        ArrayList<Protokoll> plist = plenarsitzung.getProtokollList();
        for (int i = 0;i<plist.size();i++){
            try {
                System.out.println((i+1)+ " Protocol : "+ plist.get(i).getSitzungsnumber());
                pmongo.insertProtocol(plist.get(i));
            }
           catch (Exception exp){
               System.out.println(" Error in Protocol : "+ plist.get(i).getSitzungsnumber());
                exp.printStackTrace();
           }

        }
        System.out.println("die Protokolle werden hinzugefügt......");






    }

    /***
     * diese Methode löscht die Collections aus der Databank.
     */
    public static void MongodbDeleteAllCollections(){
        db.DeleteCollections();



    }

    /***
     * In der Methode werden die Protocols mithilfe der Methode readAllProtocolsfromMongo aus der KLasse Protokoll_MongoDB_Impl gelesen.
     * Dann entsteht eine CAS Objects aus jeder Redetext aus der Protokolle, danach werden Sie in der jCaslist eingefügt.
     *  Mit der for-Schleife können Inhalte der jCaslist durchlaufen. Jede CAS Objects aus der Liste wird mit Pipline ausgepackt.
     * @throws UIMAException
     */
    public static void MyJCas( ) throws UIMAException {
        Protokoll_MongoDB_Impl pmongo = new Protokoll_MongoDB_Impl(db);
        System.out.println("Reading sitzungsnumbers.......");
        ArrayList<String> sitzungsnumbers = pmongo.readAllProtocolssitzungsnumberfromMongo();
        System.out.println("die Protokolle werden hinzugefügt......");
        StringBuilder sb;
        JCas jCas;
        int count = 0;
        boolean exit = false;
        ArrayList<RedeJcas> redejcaslist = new ArrayList<>();
        RedeJcas rj;

        AggregateBuilder builder = new AggregateBuilder();
        builder.add(createEngineDescription(SpaCyMultiTagger3.class,
                SpaCyMultiTagger3.PARAM_REST_ENDPOINT, "http://spacy.prg2021.texttechnologylab.org"));
        builder.add(createEngineDescription(GerVaderSentiment.class,
                GerVaderSentiment.PARAM_REST_ENDPOINT, "http://gervader.prg2021.texttechnologylab.org",
                GerVaderSentiment.PARAM_SELECTION , "text,de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence"));
        AnalysisEngine pAE = builder.createAggregate();

        JCas_MongoDB_Impl jmongo = new JCas_MongoDB_Impl(db);
        Protokoll p;
        ArrayList<String> errorlist = new ArrayList<>();

        int j = 0;
        for(String sitzungsnumber: sitzungsnumbers){
            j++;
            try {
                System.out.println(j+"  Reading Protocol sitzungsnumber : " + sitzungsnumber);
                p = pmongo.readProtocol(sitzungsnumber);
                System.out.println(j +"  Success Protocol sitzungsnumber : " + sitzungsnumber);
                for (Tagesordnungspunkt t: p.getTagesordnungspunkts()){
                    for(Rede r : t.getReden()){

                        count++;
                        sb = new StringBuilder();
                        for(String s: r.getRedetext()){
                            sb.append(s);
                        }
                        jCas = toCas(sb.toString());
                        //System.out.println(sb.toString());


                        System.out.println(j +"  Reading Protocol sitzungsnumber : " + sitzungsnumber);
                        System.out.println("######################################    "+ count + "   ###########################################");
                        SimplePipeline.runPipeline(jCas, pAE);
                        //PrintJCasInfo(j);
                        //System.out.println(getXml(j));
                        jmongo.InsertJcasXml(getXml(jCas),r);
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
                errorlist.add(sitzungsnumber);
            }

        }
        if(errorlist.size() > 0) {
            System.out.println("************************* Error Protocol sitzungsnummber *************************************");
            for (String s: errorlist){
                System.out.println(s);
            }
        }
        System.out.println("redecount : "+count);


        int i = 1;
        for(RedeJcas rjs : redejcaslist) {
            System.out.println("######################################    "+ i + "   ###########################################");
            SimplePipeline.runPipeline(rjs.getJcas(), pAE);
            //PrintJCasInfo(j);
            //System.out.println(getXml(j));
            jmongo.InsertJcasXml(getXml(rjs.getJcas()),rjs.getRede());
            i++;
            //break;
        }




    }


    /***
     * Dank des Pipelines wird jede CAS Obeckt dem Xml-String serialisiert.
     * @param jCas
     * @return
     */
    public static String getXml(JCas jCas){
        CAS cas = jCas.getCas();
        ByteArrayOutputStream outTmp = new ByteArrayOutputStream();
        try {
            XCASSerializer.serialize(cas, outTmp);
        } catch (IOException |SAXException e) {
            e.printStackTrace();
        }
        String xml = outTmp.toString();
        return xml;

    }

    /***
     * die Methode JCAS wird toCas Methode erweitert.
     * Hier erstellen wir also der Text aus Redetext aus der Databank.
     * @param text
     * @return
     * @throws UIMAException
     */
    public static JCas toCas(String text) throws UIMAException{
        return JCasFactory.createText(text, "de");
    }

    /***
     * Diese Methode erstellt aus der JCas Object Sentence, Tokens, NameEntity und Sentiment.
     * @param jCas
     */
    public static void PrintJCasInfo(JCas jCas) {
        Collection<Sentence> sentences = JCasUtil.select(jCas, Sentence.class);

        for (Sentence sentence: sentences) {
            System.out.println("Sentence :" + sentence.getCoveredText());

            Collection<Token> tokens = JCasUtil.selectCovered(Token.class, sentence);

            for(Token token : JCasUtil.select(jCas, Token.class)) {
                System.out.println("Tokens : " + token.getCoveredText());
                System.out.println(token.getPosValue());
            }

            for(NamedEntity entity : JCasUtil.select(jCas, NamedEntity.class)) {
                System.out.println("Entity:  "+ entity.getCoveredText()+ " Value : "+ entity.getValue());
                if(entity.getValue().equals("PER")) {
                    System.out.println("Person" + entity.getCoveredText() );
                }
                else {
                    System.out.println("Location : "+ entity.getCoveredText());
                }
            }
        }
        for(Sentence sentence : JCasUtil.select(jCas, Sentence.class)) {
            System.out.println(sentence.getCoveredText());
            for (Sentiment sentiment :  JCasUtil.selectCovered(Sentiment.class, sentence)) {
                System.out.println("Sentiment : "+ sentiment.getSentiment());
            }
        }

    }





}
