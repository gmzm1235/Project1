package implementation;

/***
 * Die ID und Vorname und Nachname und Titel von den Sprechern werden in dieser Klasse als Objekt deklariert
 * und mit getter setter Methoden werden die Eigenschaften von den Variablen definiert.
 */

public class Redner {
    private String id;
    private String titel;
    private String vorname;
    private String nachname;


    public Redner() {

    }

    public Redner(String id, String titel, String vorname, String nachname) {
        this.id = id;
        this.titel = titel;
        this.vorname = vorname;
        this.nachname = nachname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }
}
