package client;

import client.util.ServerInit;
import client.util.UncryptedMessage;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

public class MainClient {
    private SocketChannel socket_client;
    private String address;
    private int port;
    private String protocol;
    private ServerInit server;
    private ByteBuffer buf;
    private UncryptedMessage noncrypte;

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
        this.resetBuffer();
        this.noncrypte = new UncryptedMessage();
    }

    /**
     * Methode connectant le client au server.
     */
    public void launch() {
        try {
            System.out.println(">> Connexion au serveur principal : '" + this.address + ":" + this.port + "'.");
            this.chooseProtocol();
            this.connexionServer();
            this.send(this.protocol);
            this.server = new ServerInit(this.receive());
            this.disconnect();
            this.port = this.server.getPort();
            this.connexionServer();
            while(this.server.getKeepAlive()) {
                this.send(this.getMessage());
                System.out.println(this.receive());
            }
        } catch (Exception e) {

            System.out.println("Accès refusé. " + e.getMessage());
        }
    }

    /**
     * Méthode permettant la connexion au serveur désiré.
     * @throws IOException
     */
    private void connexionServer() throws IOException {
        this.socket_client = SocketChannel.open();
        this.socket_client.connect(new InetSocketAddress(this.address, this.port));
        System.out.println(">> Connecté au serveur '" + this.address + ":" + this.port + "'.\n");
    }

    /**
     * Méthode permettant de récupérer la réponse du client sous forme de String.
     * @return String réponse du Serveur
     * @throws IOException
     */
    private String receive() throws IOException {
        this.resetBuffer();
        this.socket_client.read(this.buf);
        return new String(this.buf.array()).trim();
    }

    /**
     * Méthode permettant d'obtenir l'accès au serveur de cryptage en demandant au serveur d'accueil.
     *
     * @throws IOException
     */
    private void send(String str) throws IOException {
        this.buf = ByteBuffer.allocate(str.length() * 8);
        this.buf = ByteBuffer.wrap(str.getBytes());
        this.socket_client.write(this.buf);
    }

    private void resetBuffer() {
        this.buf = ByteBuffer.allocate(2048);
    }
    /**
     * Méthode demandant au client le protocol à utiliser.
     *
     * @throws IOException
     */
    private void chooseProtocol() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println(">> Veuillez choisir le protocole à utiliser pour se connecter au serveur de cryptage.");
        do {
            System.out.print("> udp ou tcp ? ");
            this.protocol = br.readLine();
            System.out.print("\n");
        } while (!this.protocol.toLowerCase().equals("udp")
                && !this.protocol.toLowerCase().equals("tcp"));
    }

    /**
     * Méthode lisant le message à envoyer au client.
     *
     * @return String message à envoyer
     * @throws IOException
     */
    private String getMessage() throws IOException {
        this.noncrypte.saisie();
        return this.server.getIdc() + ":" + this.noncrypte.getShift() + ":" + this.noncrypte.getMessage();
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
        mc.launch();
        mc.disconnect();
    }
}
