package implementation;

import abstractclasses.Protokoll;

import java.util.ArrayList;


/***
 * In dieser Klasse die ID, der Redner und der Text von Rede als Objekt definiert
 * und noch mit Getter-Setter Methode wird die Variablen definiert
 */

public class Rede {
    private String id;
    private Redner redner;
    private ArrayList<String> redetext;
    private Tagesordnungspunkt tagesordnungspunkt;

    public Rede() {
    }

    public Rede(String id, Redner redner) {
        this.id = id;
        this.redner = redner;
    }

    public Rede(String id, Redner redner, ArrayList<String> redetext) {
        this.id = id;
        this.redner = redner;
        this.redetext = redetext;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Redner getRedner() {
        return redner;
    }

    public void setRedner(Redner redner) {
        this.redner = redner;
    }

    public ArrayList<String> getRedetext() {
        return redetext;
    }

    public void setRedetext(ArrayList<String> redetext) {
        this.redetext = redetext;
    }


    public Tagesordnungspunkt getTagesordnungspunkt() {
        return tagesordnungspunkt;
    }

    public void setTagesordnungspunkt(Tagesordnungspunkt tagesordnungspunkt) {
        this.tagesordnungspunkt = tagesordnungspunkt;
    }

    public void addRedeParagraph(String paragraph) {
        redetext.add(paragraph);
    }

}
