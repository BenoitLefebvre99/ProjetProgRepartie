package server.udp;

import server.util.AllowList;
import server.util.ClientResponse;
import server.util.Idc;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.*;

/**
 * Classe représentant un serveur UDP.
 */
public class ServerUdp {

    private int port;
    private AllowList allowList;
    private DatagramChannel datagram_server;
    private ByteBuffer buf;

    /**
     * Constructeur créant un serveur UDP au port souhaité.
     *
     * @param port int port souhaité
     */
    public ServerUdp(int port) {
        this.port = port;
        this.allowList = new AllowList();
    }

    /**
     * Méthode permettant de créer un serveur UDP au port par défaut 12347
     */
    public ServerUdp() {
        this(12347);
    }

    /**
     * Méthode permettant de créer, autoriser et récupérer l'idc d'un nouveau client.
     *
     * @return String "IDC:PROTOCOL:PORT"
     */
    public String newIdc() {
        Idc nouveauClient = new Idc("udp", this.port);
        this.allowList.add(nouveauClient.getIdc());
        return nouveauClient.getServerResponse();
    }

    /**
     * Méthode renvoyant le port du serveur de cryptage UDP.
     *
     * @return int port
     */
    public int getPort() {
        return this.port;
    }

    /**
     * Méthode permettant de lancer le serveur UDP.
     *
     * @param selector Selecteur à utiliser
     * @throws IOException erreur lors du lancement du serveur UDP
     */
    public void launch(Selector selector) throws IOException {
        this.datagram_server = DatagramChannel.open();
        datagram_server.configureBlocking(false);
        datagram_server.socket().bind(new InetSocketAddress(this.port));
        datagram_server.register(selector, SelectionKey.OP_READ);
        System.out.println("> Serveur de cryptage UDP ouvert sur le port : '" + this.port + "'.");
    }

    /**
     * Méthode permettant de traiter la demande du client
     *
     * @param key SelectionKey
     * @throws IOException erreur lors du traitement de la réponse au client.
     */
    public void answer(SelectionKey key) throws IOException {
        this.datagram_server = (DatagramChannel) key.channel();
        this.buf = ByteBuffer.allocate(2048);
        SocketAddress add = this.datagram_server.receive(this.buf);
        ClientResponse work = new ClientResponse(new String(this.buf.array()).trim());
        if (this.allowList.contains(work.getIdc())) {
            if (work.isKeepAlive()) {
                this.datagram_server.send(ByteBuffer.wrap(work.getCryptedMessage().getBytes()), add);
            } else {
                System.out.println(">> Déconnexion du client.");
                this.datagram_server.send(ByteBuffer.wrap((">> Déconnexion du client.").getBytes()), add);
                this.allowList.remove(work.getIdc());
            }
        } else {
            this.datagram_server.send(ByteBuffer.wrap(("Accès réfusé, cet IDC n'existe pas sur le serveur.").getBytes()), add);
        }
    }

    /**
     * Méthode permettant une clé UDP.
     *
     * @param selector Selector
     * @throws ClosedChannelException erreur lors de l'enregistrement
     */
    public void register_udp(Selector selector) throws ClosedChannelException {
        this.datagram_server.register(selector, SelectionKey.OP_READ);
    }
}
