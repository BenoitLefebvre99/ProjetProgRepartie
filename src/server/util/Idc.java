package server.util;

import java.util.Random;

/**
 * Classe représentant un IDC.
 */
public class Idc {
    private long idc;
    private String protocol;
    private int port;
    private Random alea;

    /**
     * Contructeur Idc() permettant de créer un nouvel idc et d'obtenir le message à envoyer au client.
     *
     * @param protocol "tcp" ou "udp"
     * @param port     int port du serveur
     */
    public Idc(String protocol, int port) {
        this.protocol = protocol.toUpperCase();
        this.port = port;
        this.alea = new Random();
        getRandomizedIdc();
    }

    /**
     * Méthode permettant de récupérer la réponse complète du server.
     *
     * @return IDC:PROTOCOL:PORT
     */
    public String getServerResponse() {
        return this.idc + ":" + this.protocol + ":" + this.port;
    }

    /**
     * Methode permettant de générer un IDC Aléatoire.
     */
    private void getRandomizedIdc() {
        this.idc = this.alea.nextLong();
    }

    /**
     * Méthode renvoyant l'idc créé.
     *
     * @return long idc
     */
    public long getIdc() {
        return this.idc;
    }

}
