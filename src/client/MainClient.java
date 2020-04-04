package client;

import client.util.CrypteursAvailable;
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
    private CrypteursAvailable crypteurs;
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
     * Methode exécutant toute le programme.
     */
    public void launch() {
        try {
            this.initialisation();
            while (this.server.getKeepAlive()) {
                this.barpresentation();
                int tmp = this.chooseCrypteur();
                this.send(this.getMessage(tmp), this.protocol);
                System.out.println("\n>>> Message crypté : " + this.receive(this.protocol) + "\n");
                this.barpresentation();
            }
        } catch (Exception e) {
            System.out.println("Le serveur a coupé la communication. ");
        }
    }

    /**
     * Méthode privée initialisant la connexion du client au serveur.
     *
     * @throws IOException erreur lors de l'initialisation.
     */
    private void initialisation() throws IOException {
        System.out.println(">> Connexion au serveur principal : '" + this.address + ":" + this.port + "'.");
        String prot = this.chooseProtocole();
        this.connexionServer();
        this.protocol = prot;
        this.send(this.protocol, "tcp");
        this.server = new ServerInit(this.receive("tcp"));
        this.crypteurs = new CrypteursAvailable(this.receive("tcp"));
        this.disconnect();
        this.port = this.server.getPort();
        this.connexionServer();
    }

    /**
     * Méthode permettant de faire choisir un crypteur au client.
     *
     * @return int du crypteur
     * @throws IOException erreur lors de la sélection du crypteur.
     */
    private int chooseCrypteur() throws IOException {
        System.out.println(this.crypteurs.toString());
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String res = "";
        do {
            System.out.print("\n>> Quel crypteur désirez-vous ? ");
            res = br.readLine();
            System.out.print("\n");
        } while (!this.crypteurs.contains(res));
        return Integer.parseInt(res);
    }

    /**
     * Méthode permettant la connexion au serveur désiré.
     *
     * @throws IOException erreur lors de la connexion.
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
     * @throws IOException erreur lors de la réception
     */
    private String receive(String protocol) throws IOException {
        this.resetBuffer();
        String res;
        if (protocol.toLowerCase().equals("tcp")) {
            this.socket_client.read(this.buf);
        } else if (protocol.toLowerCase().equals("udp")) {
            this.data_channel.receive(this.buf);
        }
        res = new String(this.buf.array()).trim();
        if (res.equals(">> Déconnexion du client.")
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
     * @throws IOException erreur lors de l'envoi
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
     * @throws IOException erreur lors de la sélection du protocole
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
     * @throws IOException erreur lors de la génération du message crypté.
     */
    private String getMessage(int crypteur) throws IOException {
        this.noncrypte.saisie(crypteur);
        return this.server.getIdc() + ":" + crypteur + ":" + this.noncrypte.getShift() + ":" + this.noncrypte.getMessage();
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
