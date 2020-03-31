package server.util;

import server.crypter.Cesar;
import server.crypter.Vigenere;

public class ClientResponse {
    private long idc;
    private String shift;
    private String before;
    private int crypteur;
    private Cesar cesar;
    private Vigenere vigenere;
    private boolean keepAlive;

    /**
     * Constructeur ClientResponse() traitant la réponse du client.
     *
     * @param reponseBrut "IDC:SHIFT:MSG"
     */
    public ClientResponse(String reponseBrut) {
        this.keepAlive = true;
        this.cut(reponseBrut);
        this.initSelectedCrypt();
    }

    /**
     * Méthode initialisant le crypteur choisi.
     */
    private void initSelectedCrypt() {
        if(this.crypteur == 0) {
            this.cesar = new Cesar(this.before, Integer.parseInt(this.shift));
        } else {
            this.vigenere = new Vigenere(this.before, this.shift);
        }
    }
    /**
     * Méthode permettant de découper la réponse brute.
     *
     * @param brut String réponse brute.
     */
    private void cut(String brut) {
        try {
            String tmp;
            tmp = brut.substring(0, brut.indexOf(':'));
            this.idc = Long.parseLong(tmp);
            tmp = brut.substring(brut.indexOf(':') + 1);
            tmp = tmp.substring(0 , tmp.indexOf(':'));
            this.crypteur = Integer.parseInt(tmp);
            tmp = brut.substring(brut.indexOf(':') + 1);
            tmp = tmp.substring(tmp.indexOf(':') + 1, tmp.lastIndexOf(":"));
            this.shift = tmp;
            tmp = brut.substring(brut.lastIndexOf(':') + 1);
            this.before = tmp;
            if(this.before.equals("")) this.keepAlive = false;
        } catch (Exception e) {
            System.out.println(">>> Erreur lors de la découpe du message du client /!\\ .");
            this.keepAlive = false;
        }
    }

    /**
     * Méthode renvoyant le message crypté demandé par le client.
     *
     * @return String message crypté
     */
    public String getCryptedMessage() {
        if(this.crypteur == 0) {
            return this.cesar.getCodedMessage();
        } else {
            return this.vigenere.getCodedMessage();
        }
    }

    /**
     * Méthode renvoyant l'idc contenu dans le message du client.
     *
     * @return long idc.
     */
    public long getIdc() {
        return this.idc;
    }

    /**
     * Méthode permettant de savoir si le message est valide et que le serveur doit rester actif.
     *
     * @return boolean true : doit rester ouvert.
     */
    public boolean isKeepAlive() {
        return this.keepAlive;
    }
}
