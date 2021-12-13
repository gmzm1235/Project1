package implementation;

import abstractclasses.Protokoll;

import java.util.ArrayList;
import java.util.List;

/***
 * Diese Klasse ist die Unterklasse der Protokoll-Klasse. Sie beinhaltet die Liste von Tagesordungspunkt.
 * Mit den get/set/add Methoden werden die gewünschten Eigenschaften definiert, die man in die Liste hinzufügen will.
 */

public class Protokoll_File_Impl extends Protokoll {
    private List<Tagesordnungspunkt> tagesordnungspunkts;
    public Protokoll_File_Impl() {
        tagesordnungspunkts = new ArrayList<Tagesordnungspunkt>();
    }

    @Override
    public List<Tagesordnungspunkt> getTagesordnungspunkts() {
        return tagesordnungspunkts;
    }

    @Override
    public void setTagesordnungspunkts(List<Tagesordnungspunkt> tagesordnungspunkts) {
        this.tagesordnungspunkts = tagesordnungspunkts;
    }

    @Override
    public void addTagesordnungspunkt(Tagesordnungspunkt tagesordnungspunkt) {
        tagesordnungspunkts.add(tagesordnungspunkt);
    }
}
