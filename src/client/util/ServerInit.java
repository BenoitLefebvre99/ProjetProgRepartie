package client.util;

/**
 * Classe traîtant le message contenant les informations de connexion de la part du serveur.
 */
public class ServerInit {
    private long idc;
    private String protocole;
    private int port;
    private boolean keepAlive;

    /**
     * Constructeur découpant la réponse initiale du serveur.
     *
     * @param serverResponse Réponse brute du serveur.
     */
    public ServerInit(String serverResponse) {
        this.keepAlive = true;
        this.cut(serverResponse);
    }

    /**
     * Méthode découpant la réponse.
     *
     * @param brut réponse brute.
     */
    private void cut(String brut) {
        try {
            String tmp;
            tmp = brut.substring(0, brut.indexOf(':'));
            this.idc = Long.parseLong(tmp);
            tmp = brut.substring(brut.indexOf(':') + 1, brut.lastIndexOf(':'));
            this.protocole = tmp;
            tmp = brut.substring(brut.lastIndexOf(':') + 1);
            this.port = Integer.parseInt(tmp);
        } catch (Exception e) {
            this.keepAlive = false;
        }
    }

    /**
     * Méthode permettant de savoir si la connexion est maintenue par le serveur.
     *
     * @return true keep alive
     */
    public boolean getKeepAlive() {
        return this.keepAlive;
    }

    /**
     * Méthode permettant de récupérer l'IDC.
     *
     * @return idc
     */
    public long getIdc() {
        return this.idc;
    }

    /**
     * Méthode permettant de récupérer le port du serveur.
     *
     * @return port
     */
    public int getPort() {
        return this.port;
    }

    /**
     * Méthode stoppant le serveur.
     */
    public void stop() {
        this.keepAlive = !this.keepAlive;
    }
}
