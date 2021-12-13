package implementation;

/***
 * Die Abgeordnete Klasse erbt von Redner
 * mit Getter-Setter Methode wird die Fraktion Information zu erhalten
 */

public class Abgeordnete extends Redner{
    private String fraktion;

    public String getFraktion() {
        return fraktion;
    }

    public void setFraktion(String fraktion) {
        this.fraktion = fraktion;
    }
}
