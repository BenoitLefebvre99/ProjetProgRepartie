package server.util;

import server.crypter.Cesar;

public class ClientResponse {
    private long idc;
    private int shift;
    private String before;
    private Cesar crypteur;
    private boolean keepAlive;

    /**
     * Constructeur ClientResponse() traitant la réponse du client.
     *
     * @param reponseBrut "IDC:SHIFT:MSG"
     */
    public ClientResponse(String reponseBrut) {
        this.keepAlive = true;
        this.cut(reponseBrut);
        this.crypteur = new Cesar(this.before, this.shift);
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
            tmp = brut.substring(brut.indexOf(':') + 1, brut.lastIndexOf(':'));
            this.shift = Integer.parseInt(tmp);
            tmp = brut.substring(brut.lastIndexOf(':') + 1);
            this.before = tmp;
            if(this.before.equals("")) this.keepAlive = false;
        } catch (Exception e) {
            this.keepAlive = false;
        }
    }

    /**
     * Méthode renvoyant le message crypté demandé par le client.
     *
     * @return String message crypté
     */
    public String getCryptedMessage() {
        return this.crypteur.getCodedMessage();
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
