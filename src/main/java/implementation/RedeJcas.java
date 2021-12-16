package implementation;

import database.JCas_MongoDB_Impl;
import org.apache.uima.jcas.JCas;

/***
 * Diese Klasse wurde erstellt, um die Verbindung zwischen Rede und JCas Objeckt nicht zu unterbrechen.
 * und die KLasse enthält die Getter-Setter Methode von Rede und JCas Objeckt.
 */

public class RedeJcas {
    private Rede rede;
    private JCas jcas;

    public RedeJcas() {
    }

    public RedeJcas(Rede rede, JCas jcas) {
        this.rede = rede;
        this.jcas = jcas;
    }

    public Rede getRede() {
        return rede;
    }

    public void setRede(Rede rede) {
        this.rede = rede;
    }

    public JCas getJcas() {
        return jcas;
    }

    public void setJcas(JCas jcas) {
        this.jcas = jcas;
    }
}
