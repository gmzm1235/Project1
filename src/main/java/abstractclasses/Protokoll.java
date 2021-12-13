package abstractclasses;

import implementation.Tagesordnungspunkt;

import java.util.Date;
import java.util.List;

/***
 * In dieser Klasse der situngnumber, das Datum und der Titel von Protokoll als Objekt mit Getter-Setter Methode definiert
 * und auch für tagesornungspunkt wird ein ähnlicher prozess durchgeführt.
 */

public abstract class Protokoll {
    private String sitzungsnumber;
    private String datum ;
    private String titel;

    public String getSitzungsnumber() {
        return sitzungsnumber;
    }

    public void setSitzungsnumber(String sitzungsnumber) {
        this.sitzungsnumber = sitzungsnumber;
    }



    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }


    public abstract List<Tagesordnungspunkt> getTagesordnungspunkts();

    public abstract void setTagesordnungspunkts(List<Tagesordnungspunkt> tagesordnungspunkts);

    public abstract void addTagesordnungspunkt(Tagesordnungspunkt tagesordnungspunkt);
}
