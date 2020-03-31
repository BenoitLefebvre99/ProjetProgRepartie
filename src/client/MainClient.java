package client;

import client.util.ServerInit;
import client.util.UncryptedMessage;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SocketChannel;

public class MainClient {
    private SocketChannel socket_client;
    private DatagramChannel data_channel;
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
        this.data_channel = null;
        this.protocol = "tcp";
        this.resetBuffer();
        this.noncrypte = new UncryptedMessage();
    }

    /**
     * Methode connectant le client au server.
     */
    public void launch() {
        try {
            System.out.println(">> Connexion au serveur principal : '" + this.address + ":" + this.port + "'.");
            String prot = this.chooseProtocole();
            this.connexionServer();
            this.protocol = prot;
            this.send(this.protocol, "tcp");
            this.server = new ServerInit(this.receive("tcp"));
            this.disconnect();
            this.port = this.server.getPort();
            this.connexionServer();
            while (this.server.getKeepAlive()) {
                this.barpresentation();
                this.send(this.getMessage(), this.protocol);
                System.out.println("\n>>> Message crypté : " + this.receive(this.protocol) + "\n");
                this.barpresentation();
            }
        } catch (Exception e) {
            System.out.println("Le serveur a coupé la communication. ");
        }
    }

    /**
     * Méthode permettant la connexion au serveur désiré.
     *
     * @throws IOException
     */
    private void connexionServer() throws IOException {
        if (this.protocol.toLowerCase().equals("tcp")) {
            this.socket_client = SocketChannel.open();
            this.socket_client.connect(new InetSocketAddress(this.address, this.port));
            System.out.println(">> Connecté au serveur '" + this.address + ":" + this.port + "'.\n");
        } else if (this.protocol.toLowerCase().equals("udp")) {
            this.data_channel = DatagramChannel.open();
            SocketAddress address = new InetSocketAddress(0);
            DatagramSocket socket = data_channel.socket();
            socket.bind(address);
        }
    }

    /**
     * Méthode permettant de récupérer la réponse du client sous forme de String.
     *
     * @return String réponse du Serveur
     * @throws IOException
     */
    private String receive(String str) throws IOException {
        this.resetBuffer();
        String res;
        if (str.toLowerCase().equals("tcp")) {
            this.socket_client.read(this.buf);
        } else if (str.toLowerCase().equals("udp")) {
            this.data_channel.receive(this.buf);
        }
        res = new String(this.buf.array()).trim();
        if(res.equals(">> Déconnexion du client.")
                || res.equals("Accès réfusé, cet IDC n'existe pas sur le serveur.")) {
            this.server.stop();
        }
        this.buf.flip();
        return res;
    }

    /**
     * Méthode permettant d'afficher une bar de présentation.
     */
    private void barpresentation() {
        System.out.println("\n*******************************************************\n");
    }

    /**
     * Méthode permettant d'obtenir l'accès au serveur de cryptage en demandant au serveur d'accueil.
     *
     * @throws IOException
     */
    private void send(String str, String protocol) throws IOException {
        this.buf = ByteBuffer.allocate(str.length() * 8);
        this.buf = ByteBuffer.wrap(str.getBytes());
        if (protocol.toLowerCase().equals("tcp")) {
            this.socket_client.write(this.buf);
        } else if (protocol.toLowerCase().equals("udp")) {
            SocketAddress server = new InetSocketAddress(this.address, this.port);
            this.data_channel.send(this.buf, server);
        }
    }

    private void resetBuffer() {
        this.buf = ByteBuffer.allocate(2048);
    }

    /**
     * Méthode demandant au client le protocol à utiliser.
     *
     * @throws IOException
     */
    private String chooseProtocole() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println(">> Veuillez choisir le protocole à utiliser pour se connecter au serveur de cryptage.");
        String res = "";
        do {
            System.out.print("> udp ou tcp ? ");
            res = br.readLine();
            System.out.print("\n");
        } while (!res.toLowerCase().equals("udp")
                && !res.toLowerCase().equals("tcp"));
        return res;
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
        System.out.println(">> Déconnecté du serveur '" + this.address + ":" + this.port + "'.\n");
    }

    public static void main(String args[]) throws IOException {
        MainClient mc;
        if (args.length == 0) mc = new MainClient();
        else mc = new MainClient(args[0], Integer.parseInt(args[1]));
        mc.launch();
        mc.disconnect();
    }
}
