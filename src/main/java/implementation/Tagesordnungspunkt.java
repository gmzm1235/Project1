package implementation;

import abstractclasses.Protokoll;

import java.util.ArrayList;

/***
 * In dieser Klasse werden die ID von Tagesordnungspunkt und deren Text-Liste mithilfe von getset Methoden definiert.
 */

public class Tagesordnungspunkt {
    private String topid;
    private ArrayList<Rede> reden;
    private Protokoll protokoll;

    public Tagesordnungspunkt() {
    }

    public Tagesordnungspunkt(String topid, ArrayList<Rede> reden) {
        this.topid = topid;
        this.reden = reden;
    }

    public String getTopid() {
        return topid;
    }

    public void setTopid(String topid) {
        this.topid = topid;
    }

    public ArrayList<Rede> getReden() {
        return reden;
    }

    public void setReden(ArrayList<Rede> reden) {
        this.reden = reden;
    }

    public Protokoll getProtokoll() {
        return protokoll;
    }

    public void setProtokoll(Protokoll protokoll) {
        this.protokoll = protokoll;
    }

    public void addRede(Rede rede) {
        reden.add(rede);
    }
}
