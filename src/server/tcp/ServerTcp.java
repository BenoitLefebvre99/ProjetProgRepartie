package server.tcp;

import server.util.AllowList;
import server.util.ClientResponse;
import server.util.Idc;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class ServerTcp {
    private int port;
    private AllowList allowList;

    /**
     * Méthode permettant de créer un serveur TCP au port indiqué.
     *
     * @param port port souhaité.
     * @throws IOException
     */
    public ServerTcp(int port) throws IOException {
        this.port = port;
        this.allowList = new AllowList();
    }

    /**
     * Méthode permettant de créer un serveur TCP au port par défaut (12345).
     *
     * @throws IOException
     */
    public ServerTcp() throws IOException {
        this(12346);
    }

    /**
     * Méthode permettant de lancer le serveur TCP.
     *
     * @throws IOException
     */
    public void launch(Selector selector) throws IOException {
        ServerSocketChannel socket_server = ServerSocketChannel.open();
        socket_server.configureBlocking(false);
        socket_server.socket().bind(new InetSocketAddress(this.port));
        socket_server.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("> Serveur de cryptage TCP ouvert sur le port : '" + this.port + "'.");
    }
    /*public void launch(Selector selector, ServerSocketChannel socket_server) throws IOException {
        socket_server = ServerSocketChannel.open();
        socket_server.configureBlocking(false);
        socket_server.socket().bind(new InetSocketAddress(this.port));
        socket_server.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("> Serveur de cryptage TCP ouvert sur le port : '" + this.port + "'.");
    }*/

    /**
     * Méthode permettant de traiter la demande du client
     *
     * @param client_message message brut du client
     * @param client         socket du client
     * @throws IOException
     */
    public void answer(String client_message, SocketChannel client) throws IOException {
        ClientResponse work = new ClientResponse(client_message);
        if (this.allowList.contains(work.getIdc())) {
            if (work.isKeepAlive()) {
                client.write(ByteBuffer.wrap(work.getCryptedMessage().getBytes()));
            } else {
                System.out.println(">> Déconnexion du client.");
                client.write(ByteBuffer.wrap((">> Déconnexion du client.").getBytes()));
                this.allowList.remove(work.getIdc());
            }
        } else {
            client.write(ByteBuffer.wrap(("Accès réfusé, cet IDC n'existe pas sur le serveur.").getBytes()));
        }
    }

    /**
     * Méthode permettant de générer un nouvel idc.
     *
     * @return String "IDC:PROTOCOLE:PORT"
     */
    public String newIdc() {
        Idc nouveauClient = new Idc("tcp", this.port);
        this.allowList.add(nouveauClient.getIdc());
        return nouveauClient.getServerResponse();
    }

    /**
     * Méthode renvoyant le port du serveur de cryptage TCP.
     *
     * @return int port
     */
    public int getPort() {
        return this.port;
    }
}
