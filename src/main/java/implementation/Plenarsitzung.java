package implementation;

import abstractclasses.Protokoll;
import implementation.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/***
 * Die Informationen, die in main abgefragten werden in dieser Klasse ausgeführt.

 */

public class Plenarsitzung {
    private   HashMap<String , Redner> RednerMap;
    private   ArrayList<Protokoll> ProtokollList;
    private   DocumentBuilderFactory factory;
    private   DocumentBuilder builder;
    private String xmlFoldername;



    public  Plenarsitzung() {

    }

    /***
     * Bei dieser Methode wird die Objekt-variable xmlFoldername definiert, die aus der Klasse erstellt wird
     * @param xmlFoldername
     */
    public  Plenarsitzung(String xmlFoldername) {
        this.xmlFoldername = xmlFoldername;


    }

    public ArrayList<Protokoll> getProtokollList() {
        return ProtokollList;
    }

    public HashMap<String, Redner> getRednerMap(){
        return RednerMap;
    }

    /***
     * das xmlFoldername Objeckt, das oben erstellt wird, wird in Zeile 46-50 mit Getter-Setter Zugriffs Methoden definiert.
     * @return xmlFoldername
     */
    public String getXmlFoldername() {
        return xmlFoldername;
    }

    public void setXmlFoldername(String xmlFoldername) {
        this.xmlFoldername = xmlFoldername;
    }


    public void printAllProtokolls(){
        for (Protokoll p : ProtokollList) {
            System.out.println(p.getTitel() + " " + p.getDatum());
        }

    }

    /***
     * Bei dieser Methode werden für die unter der Klasse plenarsitung definierten Variablen neue Objekte erstellt.
     * RednerMap wird Ids von Redner (key) und die Informationen wie der Vorname, Nachname, Titel von Redner (Value)
     * mithilfe dem HashMap(key, value) zusammengepasst.
     * Protokolllist ist eine Liste, die erstellt wurde, um jede Protokolle aufzunehmen.
     * factory/builder sind ein Hilfeobjekt um xmlfiles zu lesen.
     * und zuletzt wird es ReadAllRedner und ReadAllProtokolls Methods aufgerufen, um die Protokolls und die Redner von xmlfiles zu lesen
     *
     */
    public void build(){
        try {
            RednerMap = new HashMap<String, Redner>();
            ProtokollList = new ArrayList<Protokoll>();
            factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder();
            readAllRedner(xmlFoldername);
            readAllProtokolls(xmlFoldername);


        } catch (Exception exp) {
            System.out.println(exp.getMessage());
        }

    }

    /**
     *Bei dieser Methode sollen alle Sprecher im XML ausgegeben werden.
     * Mit dem keySet in RednerMap erstellte Schlüssel werden mit der hasNext-Methode des Iterators gescannt.
     * Wenn ein Redner mit derselben ID vorhanden ist, wird dieser nicht in RednerMap gespeichert.
     * Fall er neue Redner ist, wird der ID von Redner in RednerMap hinzugefügt.
     * und die Redner eins zu eins mit ihrem Vornamen, Nachnamen und Titel wird geprint.
     * man prüft am Ende stehende if conditions mit dem operator instanceof, ob der Redner ein abgeordeneter ist
     * Falls er ein Abgeordneter ist, wird zusätzlich auch der Fraktionname ausgedruckt.
     *
     */
    public void printAllRedners(){
        String titel;
        String id;
        Redner redner;
        Iterator<String> itr = RednerMap.keySet().iterator();
        Abgeordnete abgeordnete;
        while (itr.hasNext()) {
            id = itr.next();
            redner = RednerMap.get(id);
            titel = redner.getTitel() != null ? redner.getTitel() : "";
            System.out.print(titel + " " + redner.getVorname()+" " + redner.getNachname());
            if (redner instanceof Abgeordnete) {
                abgeordnete = (Abgeordnete) redner;
                System.out.println("  Fraktion : " + abgeordnete.getFraktion() );


            }



        }

    }

    /***
     * Bei diesem Methode werden nur die Redner, die der Partei angehören, also Abgeordnete, zum Ausdruck aufgefordert.
     * Genau wie bei der printAllRedner Methode werden IDs und Informationen über Redner ausgefüllt
     * aber in diesem Fall wird nur die Redner, die eine Fraktion haben, ausgedruckt
     */

    public void printAllAbgeordnete(){
        String titel;
        String id;
        Redner redner;
        Iterator<String> itr = RednerMap.keySet().iterator();
        Abgeordnete abgeordnete;
        while (itr.hasNext()) {
            id = itr.next();
            redner = RednerMap.get(id);
            if (redner instanceof Abgeordnete) {
                abgeordnete = (Abgeordnete) redner;
                titel = abgeordnete.getTitel() != null ? abgeordnete.getTitel() : "";
                System.out.println(titel + " " + abgeordnete.getVorname()+" " + abgeordnete.getNachname() + "  Fraktion : " + abgeordnete.getFraktion() );

            }



        }

    }

    /***
     * Bei dieser Methode werden ebenso wie bei der printAllAbgeordnete werden die erhaltenen Abgeordneten
     * unter dem Namen der Partei aufgelistet, der sie angehören.
     * Nachdem Abgeordnete gefunden wurden, werden sie in die Liste abglist (abgeordnetelist) übernommen.
     * in FrMap(FRaktionMap) wird die Fraktionen (key) und die Mitglieder (value) mit Hashmap organisiert
     * Mit dem containskey wird geprüft, ob die Fraktion bereits registriert ist oder nicht, falls die Fraktion nicht in FrMap vorhanden ist, wird es der Liste hinzugefügt.
     * die schließlich erhaltene abglist List öffnet sich mit einer for-Schleife und druckt die Parteien und ihre Mitglieder aus.
     */
    public void printAllFraktionRedners() {
        HashMap<String , ArrayList<Abgeordnete>> FrMap = new HashMap<String , ArrayList<Abgeordnete>>();
        String titel;
        String id;
        String fraktion;
        Redner redner;
        Iterator<String> itr = RednerMap.keySet().iterator(); //bir collec üzerinde
        Abgeordnete abg;
        ArrayList<Abgeordnete> abglist;
        while (itr.hasNext()) {
            id = itr.next();
            redner = RednerMap.get(id);
            if (redner instanceof Abgeordnete) {
                abg = (Abgeordnete) redner;
                if (FrMap.containsKey(abg.getFraktion())){
                    abglist = FrMap.get(abg.getFraktion());
                    abglist.add(abg);
                }
                else {
                    abglist = new ArrayList<Abgeordnete>();
                    abglist.add(abg);
                    FrMap.put(abg.getFraktion(), abglist);
                }
            }
        }
        Iterator<String> itr2 = FrMap.keySet().iterator();
        while (itr2.hasNext()) {
            fraktion = itr2.next();
            System.out.println(fraktion + "  Mitglied");
            System.out.println("-----------------------------------------------------");
            abglist = FrMap.get(fraktion);
            for (Abgeordnete a :abglist) {
                titel = a.getTitel() != null ? a.getTitel() : "";
                System.out.println(a.getId() + " " + titel + " " + a.getVorname()+" " + a.getNachname() );

            }
            System.out.println();

        }





    }

    /***
     * Bei dieser Methode werden alle Protokolle in der Protokollliste
     * Mit Hilfe der for-Schleife, Sitzungsnummer, also der gewünschten Protokollnummer und
     * Die Tagesordnungspunktnummer im Protokoll, also top-id, wird eingeholt und ausgedruckt
     * @param sitzungsnumber
     */
    public void printProtokollTagesordnungspunktnumbers(String sitzungsnumber) {
        for(Protokoll p:ProtokollList) {
            if (p.getSitzungsnumber().trim().equals(sitzungsnumber.trim())) {
                for(Tagesordnungspunkt t : p.getTagesordnungspunkts()) {
                    System.out.println(t.getTopid());
                }
            }
        }

    }

    /***
     * Bei dieser Methode mit Protokollnummer (situngsnumber) und tagesordnungspunktid (top-id) ,
     * der Redner im gewünschten tagesordnungspunkt und seine Rede werden ausgedruckt.
     * jeder Redner hat ein eigene Id und jede Rede hat auch ein eigene Id
     * Die Informationen des Redners werden mit der Getter-Methode aus der Redner-Klasse abgerufen.
     * Die Reden(text) werden mit Hilfe der Rede-Klasse ausgedruckt.
     * @param sitzungsnumber
     * @param tagesordungspunktid
     */
    public void printProtokollTagesordnungspunkt(String sitzungsnumber, String tagesordungspunktid) {
        String titel;
        for(Protokoll p:ProtokollList) {
            if (p.getSitzungsnumber().trim().equals(sitzungsnumber.trim())) {
                for(Tagesordnungspunkt t : p.getTagesordnungspunkts()) {
                    if(t.getTopid().trim().equals(tagesordungspunktid.trim())) {
                        for (Rede r: t.getReden()){
                            System.out.println("redeid: " + r.getId());
                            titel = r.getRedner().getTitel() != null ? r.getRedner().getTitel() : ""; //conditional operator (ternary)
                            System.out.println("redner : " + titel + " "+ r.getRedner().getVorname() +" " + r.getRedner().getNachname());
                            for (String line : r.getRedetext()) {
                                System.out.println(line);
                            }
                            System.out.println("-------------------------------------------------------------");
                        }
                    }
                }
            }
        }

    }

    /***
     * Diese Methode hat die Sitzungstitelnummer im Protokoll
     * @param e
     * @return number
     */
    private  String FindTitel(Element e) {
        NodeList titellist = e.getElementsByTagName("sitzungstitel");
        Element sitzungstitel = (Element) (titellist.item(0));
        String number = sitzungstitel.getChildNodes().item(1).getTextContent();
        return number;
    }

    /***
     * Bei dieser Methode werden IDs von Redner aus der RedeMap (mit HashMap) mit der return-Anweisung ausgeführt.
     * @param id
     * @return
     */



    private  Redner FindRednerFromMap(String id) {
        if (RednerMap.containsKey(id)) {
            return RednerMap.get(id);

        }
        return null;
    }

    /***
     *
     * Bei dieser Methode wird der Txt, der kein Kommentar enthielt, mithilfe des Rede Ids gefunden.
     * jedes Gespräch(Rede) hat auch einen bestimmten Sprecher(Redner),
     * paragraphlist node listiyle  xml datei da <p> attributunde yer alan konusmalar bulunur ve en son strlist e atanir.
     * Mit dem paragraphlist (Node List) wird die Reden in <p> Attribute aus Xml Files gefunden und am Ende in strlist List definiert.
     * @param e
     * @return
     */

    private  Rede FindRede(Element e) {
        Rede rede = new Rede();
        String id = e.getAttribute("id");
        rede.setId(id);
        NodeList list = e.getElementsByTagName("redner");
        Element er = (Element) (list.item(0));
        String rednerid = er.getAttribute("id");
        Redner redner = FindRednerFromMap(rednerid);
        rede.setRedner(redner);
        NodeList paragraphlist = e.getElementsByTagName("p");
        NodeList kommentarlist = e.getElementsByTagName("kommentar");
        String klasse;
        Element ep;
        ArrayList<String> strlist = new ArrayList<String>();
        for (int i = 0; i<paragraphlist.getLength(); i++) {
            ep = (Element) paragraphlist.item(i);
            klasse = ep.getAttribute("klasse");
            if (!klasse.equals("redner")) {
                strlist.add(ep.getTextContent());

            }

        }
        Element k;
        for (int i = 0; i<kommentarlist.getLength(); i++){
            k = (Element) kommentarlist.item(i);
            strlist.add(k.getTextContent());

        }
        rede.setRedetext(strlist);
        return rede;
    }

    /***
     * Bei dieser Methode werden die Texte unter der Top-ID, also dem Tagesordnungspunkt in der xml-Datei,
     * mit Hilfe von for zum Redelist-Text hinzugefügt
     * so wird der text unter dem gewünschten tagesordnungspunkt gefunden
     * @param e
     * @return
     */

    private  Tagesordnungspunkt FindTagesordnungspunkt(Element e, Protokoll protokoll) {
        ArrayList<Rede> reden = new ArrayList<Rede>();
        Rede rede;
        Tagesordnungspunkt t = new Tagesordnungspunkt();
        t.setProtokoll(protokoll);
        String id = e.getAttribute("top-id");
        String [] items = id.split(" ");
        if (items.length > 1){
            id = items[1].trim();
        }
        else {
            id = items[0].trim();
        }
        t.setTopid(id);
        NodeList redelist = e.getElementsByTagName("rede");
        for (int i = 0; i < redelist.getLength(); i++) {
            rede = FindRede((Element) redelist.item(i));
            rede.setTagesordnungspunkt(t);
            reden.add(rede);

        }
        t.setReden(reden);
        return t;
    }

    /***
     * diese Methode listet alle Tagesordnungspunkte auf
     * @param e
     * @return
     */

    private  ArrayList<Tagesordnungspunkt> FindTagesordnungspunktslist(Element e, Protokoll protokoll) {
        ArrayList<Tagesordnungspunkt> tagesordnungspunkts = new ArrayList<Tagesordnungspunkt>();
        NodeList list = e.getElementsByTagName("tagesordnungspunkt");
        Tagesordnungspunkt t;
        for (int i = 0; i <list.getLength(); i++ ) {
            t = FindTagesordnungspunkt((Element) list.item(i), protokoll);
            //System.out.println(((Element) list.item(i)).getAttribute("top-id"));
            tagesordnungspunkts.add(t);

        }
        return tagesordnungspunkts;
    }

    /***
     * Bei dieser Methode wird das angegebene Protokoll gelesen und Attribute wie situngsdatum oder sitzungsnumber aus dem Protokoll gelesen und an die Objekte übertragen.
     * @param f
     * @return
     */

    private  Protokoll readProtokoll(File f) {
        try {
            if (f.isFile()) {
                Protokoll protokoll = new Protokoll_File_Impl();
                Document document = builder.parse(f);
                Element root = document.getDocumentElement();
                String strdate = root.getAttribute("sitzung-datum");
                String sitzungsnumber;
                NodeList list = root.getElementsByTagName("sitzungsnr");
                Element e = (Element) list.item(0);
                sitzungsnumber = e.getTextContent();
                protokoll.setSitzungsnumber(sitzungsnumber);
                protokoll.setDatum(strdate);
                protokoll.setTitel(FindTitel(root));
                protokoll.setTagesordnungspunkts(FindTagesordnungspunktslist(root, protokoll));

                return protokoll;
            } else {
                return null;
            }
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return null;

    }

    /***
     * Bei dieser Methode werden die Protokolle in allen xml-Dateien gelesen und der Protokollliste hinzugefügt.
     * @param foldername
     */

    private  void readAllProtokolls(String foldername) {
        Protokoll protokoll;
        File xmlfiles = new File(foldername);
        if (xmlfiles.isDirectory()) {
            File[] files = xmlfiles.listFiles();
            for (File f : files) {
                if (f.getName().endsWith(".xml")) {
                    protokoll = readProtokoll(f);
                    if (protokoll != null) {
                        ProtokollList.add(protokoll);

                    }
                }

            }
        }

    }

    /***
     * Bei dieser Methode werden die Merkmale oder Informationen der Redner im Protokoll gelesen.
     * Falls ein Redner zu einer Fraktion beitrat, dann wird extra noch das Abgeordnete Objekte zugeordnet
     * alle anderen Redner werden dem von der Redner-Klasse erhaltenen Redner-Objekt zugeordnet.
     * * @param e
     * @return
     */
    private  Redner FindRedner (Element e){
        String id;
        String titel = null;
        String vorname = null;
        String nachname = null;
        String fraktion = null;
        id = e.getAttribute("id");
        NodeList list1 = e.getElementsByTagName("titel");
        if (list1.getLength() == 1) {
            Element etitel = (Element) list1.item(0);
            titel = etitel.getTextContent();
        }

        NodeList list2 = e.getElementsByTagName("vorname");
        if (list2.getLength() == 1) {
            Element evorname = (Element) list2.item(0);
            vorname = evorname.getTextContent();
        }

        NodeList list3 = e.getElementsByTagName("nachname");
        if (list3.getLength() == 1) {
            Element enachname = (Element) list3.item(0);
            nachname = enachname.getTextContent();
        }

        NodeList list4 = e.getElementsByTagName("fraktion");
        if (list4.getLength() == 1) {
            Element efraktion = (Element) list4.item(0);
            fraktion = efraktion.getTextContent();
        }
        if (fraktion == null) {
            Redner redner = new Redner();
            redner.setId(id);
            redner.setTitel(titel);
            redner.setVorname(vorname);
            redner.setNachname(nachname);
            return redner;
        }
        else {
            Abgeordnete abgeordnete = new Abgeordnete();
            abgeordnete.setId(id);
            abgeordnete.setTitel(titel);
            abgeordnete.setVorname(vorname);
            abgeordnete.setNachname(nachname);
            abgeordnete.setFraktion(fraktion);
            return abgeordnete;

        }

    }

    /***
     * Wenn bei dieser Methode die Informationen einer zuvor hinzugefügten Person
     * in einem neu gelesenen Protokoll gefunden werden, wird diese Rednerliste nicht übertragen.
     * Wenn der Redner zum ersten Mal anwesend ist, wird er der RednerMap hinzugefügt.
     * Jeder Redner hat eine ID, die ihn von anderen Rednern unterscheidet.
     *
     * @param f
     */

    private  void addNewRednersToMap(File f) {
        try {
            if (f.isFile()) {
                Document document = builder.parse(f);
                Element root = document.getDocumentElement();
                NodeList list = root.getElementsByTagName("rednerliste");
                Element rednerlist = (Element) list.item(0);
                //NodeList list2 = rednerlist.getElementsByTagName("redner");
                NodeList list2 = root.getElementsByTagName("redner");
                Redner redner;
                for(int i = 0; i < list2.getLength(); i++) {
                    redner = FindRedner((Element) list2.item(i));
                    if (!RednerMap.containsKey(redner.getId())) {
                        RednerMap.put(redner.getId(), redner);
                    }

                }
            }

        } catch (Exception exp) {
            //System.out.println(exp.getMessage());
            System.out.println(f.getAbsolutePath());
            exp.printStackTrace(); //problrmin bulundugu satiri gösterir
        }


    }

    /***
     * Bei dieser Methode werden alle Redner erfasst und
     * mit Hilfe der Methode addNewRednersToMap gibt es keine Duplikate in den Namen in der Liste.
     * @param foldername
     */

    private  void readAllRedner(String foldername) {
        File xmlfiles = new File(foldername);
        if (xmlfiles.isDirectory()) {
            File[] files = xmlfiles.listFiles();
            for (File f : files) {
                if (f.getName().endsWith(".xml")) {
                    addNewRednersToMap(f);
                }
            }
        }

    }
}
