package server;

import java.util.Random;

public class ServerResponse {
    private long idc;
    private String protocol_name;
    private int port;
    private Random alea;

    /**
     * Constructeur ServerResponse().
     * @param protocol_name nom de protocole.
     * @param port port.
     */
    public ServerResponse(String protocol_name, int port) {
        this.getRandomizedIdc();
        this.protocol_name = protocol_name;
        this.port = port;
    }

    /**
     * Méthode permettant de récupérer le port du serveur.
     * @return int port.
     */
    public int getPort() {
        return this.port;
    }

    /**
     * Méthode permettant de récupérer l'identifiant de communication (IDC).
     * @return long IDC
     */
    public long getIdc() {
        return this.idc;
    }

    /**
     * Méthode permettant de récupérer le nom du protocole du serveur.
     * @return String protocol name
     */
    public String getProtocol_name() {
        return this.protocol_name;
    }

    /**
     * Méthode permettant de récupérer la réponse complète du server.
     * @return IDC:PROTOCOL:PORT
     */
    public String getServerResponse() {
        return this.idc + ":" + this.protocol_name + ":" + this.port;
    }

    /**
     * Methode permettant de générer un IDC Aléatoire.
     */
    private void getRandomizedIdc() {
        this.idc = this.alea.nextLong();
    }
}
