package client.file;

import client.util.ServerInit;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

/**
 * Classe permettant d'éxécuter la communication entre le serveur et le client de lecture de fichier.
 */
public class Communication {
    private SocketChannel socket_client;
    private DatagramChannel data_channel;
    private ByteBuffer buf;
    private ServerInit server;
    private String address;
    private int port;

    private String protocol;
    private ArrayList<MessageFile> crypt;
    private ArrayList<String> result;

    /**
     * Constructeur de la classe Communication.
     *
     * @param protocol TCP / UDP
     * @param crypt    Liste des messages à crypter
     */
    public Communication(String protocol, ArrayList<MessageFile> crypt) {
        this.protocol = protocol;
        this.port = 12345;
        this.address = "localhost";
        this.crypt = crypt;
        this.result = new ArrayList<>();
        this.resetBuffer();
        this.launch();
    }

    /**
     * Méthode privée lançant la connexion et orchestrant les différentes communications.
     */
    private void launch() {
        try {
            this.initialisation();
            if (this.server.getKeepAlive()) {
                for (MessageFile m : this.crypt) {
                    if (m.isGood()) {
                        this.send(this.server.getIdc() + ":" + m.getFinal(), this.protocol);
                        this.result.add(this.receive(this.protocol));
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(">> Erreur lors de la communication avec le serveur.");
        }
    }

    /**
     * Méthode privée éxécutant l'initialisation de la connexion.
     *
     * @throws IOException erreur lors de l'initialisation de la connexion.
     */
    private void initialisation() throws IOException {
        this.connexionServer();
        this.send(this.protocol, "tcp");
        this.server = new ServerInit(this.receive("tcp"));
        this.receive("tcp");
        this.disconnect();
        this.port = this.server.getPort();
        this.connexionServer();
    }

    /**
     * Méthode privée réinitialisant le buffer.
     */
    private void resetBuffer() {
        this.buf = ByteBuffer.allocate(2048);
    }

    /**
     * Méthode privée permettant la réception des éléments venus du serveur.
     *
     * @param protocol protocole à utiliser.
     * @return Message reçu
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
        this.closedByServer(res);
        return res;
    }

    /**
     * Méthode privée permettant de vérifier si le serveur coupe la connexion.
     *
     * @param res Message du serveur
     */
    private void closedByServer(String res) {
        if (res.equals(">> Déconnexion du client.")
                || res.equals("Accès réfusé, cet IDC n'existe pas sur le serveur.")) {
            this.server.stop();
        }
        this.buf.flip();
    }

    /**
     * Méthode privée permettant l'envoi d'un message au serveur.
     *
     * @param str      message à envoyer
     * @param protocol protocole à utiliser
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

    /**
     * Méthode privée connectant le client au serveur.
     *
     * @throws IOException erreur lors de la connexion
     */
    private void connexionServer() throws IOException {
        if (this.protocol.toLowerCase().equals("tcp")) {
            this.socket_client = SocketChannel.open();
            this.socket_client.connect(new InetSocketAddress(this.address, this.port));
        } else if (this.protocol.toLowerCase().equals("udp")) {
            this.data_channel = DatagramChannel.open();
            SocketAddress address = new InetSocketAddress(0);
            DatagramSocket socket = data_channel.socket();
            socket.bind(address);
        }
    }

    /**
     * Méthode déconnectant le client du client.
     *
     * @throws IOException erreur lors de la déconnexion
     */
    public void disconnect() throws IOException {
        this.socket_client.close();
    }

    /**
     * Méthode permettant de récupérer la liste des messages cryptés.
     *
     * @return Arraylist des messages cryptés.
     */
    public ArrayList<String> getResult() {
        return this.result;
    }
}
