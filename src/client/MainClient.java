package client;

import client.util.ServerInit;

import java.io.*;
import java.net.Socket;

public class MainClient {
    private Socket socket_client;
    private String address;
    private int port;
    private String protocol;
    private ServerInit server;

    /**
     * Constructeur permettant de créer le client pour une connexion par défaut (localhost:12345)
     */
    public MainClient() {
        this("localhost", 12345);
    }

    /**
     * Constructeur permettant de créer le client pour une connexion à l'adresse et au port donné.
     *
     * @param address adresse hôte
     * @param port    port hôte
     */
    public MainClient(String address, int port) {
        this.address = address;
        this.port = port;
        this.socket_client = null;
        this.protocol = "";
    }

    /**
     * Methode connectant le client au server.
     */
    public void connect() {
        String tmp;
        try {
            this.chooseProtocol();
            this.socket_client = new Socket(this.address, this.port);
            this.send(this.protocol);
            tmp = this.receive();
            this.server = new ServerInit(tmp);
            this.socket_client = new Socket(this.address, this.server.getPort());
        } catch (Exception e) {
            System.out.println("Accès refusé.");
        }
    }

    /**
     * Méthode renvoyant la réponse brute du serveur.
     * @return String Réponse du serveur.
     * @throws IOException
     */
    private String receive() throws IOException {
        InputStream in = this.socket_client.getInputStream();
        String res="";
        int c;
        while((c = in.read()) != -1)
            res += (char) c;
        return res;
    }

    /**
     * Méthode permettant d'envoyer un message au serveur.
     * @throws IOException
     */
    private void send(String str) throws IOException {
        OutputStream os = this.socket_client.getOutputStream();
        PrintWriter pw = new PrintWriter(os, true);
        pw.println(str);
    }

    /**
     * Méthode demandant au client le protocol à utiliser.
     * @throws IOException
     */
    private void chooseProtocol() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Veuillez choisir le protocole à utiliser pour se connecter au '"
                + this.address + ':' + this.port + "'.");
        do {
            System.out.print(">> udp ou tcp ?");
            this.protocol = br.readLine();
            System.out.print("\n");
        } while(!this.protocol.toLowerCase().equals("udp")
                && !this.protocol.toLowerCase().equals("tcp"));
    }

    /**
     * Méthode lisant le message à envoyer au client.
     * @return String message à envoyer
     * @throws IOException
     */
    private String getMessage() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Quel est le message ?");
        String res = "";
        do {
            res += br.readLine();
        } while(!res.contains("\r"));
        return res;
    }

    /**
     * Méthode gérant un échange de la discussion.
     */
    public void conversation() {
        try {
            this.send(this.getMessage());
            System.out.println(">> crypted : " + this.getMessage());
        } catch(Exception e) {
            System.out.print(e.getMessage());
        }
    }

    /**
     * Méthode déconnectant le client du serveur.
     */
    public void disconnect() throws IOException {
        this.socket_client.close();
    }

    public static void main(String args[]) throws IOException {
        MainClient mc;
        if (args.length == 0) mc = new MainClient();
        else mc = new MainClient(args[0], Integer.parseInt(args[1]));
        mc.connect();
        while(mc.server.getKeepAlive()) {
            mc.conversation();
        }
        mc.disconnect();
    }
}
