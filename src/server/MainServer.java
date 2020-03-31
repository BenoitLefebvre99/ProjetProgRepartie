package server;

import server.crypter.Crypteurs;
import server.tcp.ServerTcp;
import server.udp.ServerUdp;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Set;

public class MainServer {
    private Selector selector;
    private ServerSocketChannel socket_server;
    private int port;
    private ByteBuffer buf;

    private ServerUdp udp_server;
    private ServerTcp tcp_server;

    /**
     * Méthode permettant de créer le serveur TCP d'accueil au port indiqué.
     *
     * @param port port souhaité.
     * @throws IOException
     */
    public MainServer(int port) throws IOException {
        this.selector = Selector.open();
        this.port = port;
        this.tcp_server = new ServerTcp();
        this.udp_server = new ServerUdp();
        this.resetBuffer();
    }

    /**
     * Méthode permettant de créer le serveur TCP d'accueil au port par défaut (12345).
     *
     * @throws IOException
     */
    public MainServer() throws IOException {
        this(12345);
    }

    /**
     * Méthode privée configurant le serveur principal.
     *
     * @throws IOException
     */
    private void configureMainServer() throws IOException {
        this.socket_server = ServerSocketChannel.open();
        this.socket_server.configureBlocking(false);
        this.socket_server.socket().bind(new InetSocketAddress(this.port));
        this.socket_server.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("> Serveur d'accueil ouvert sur le port : '" + this.port + "'.");
    }

    /**
     * Méthode permettant de lancer le serveur.
     *
     * @throws IOException
     */
    public void launch() {
        try {
            this.configureMainServer();
            this.launchTCPServer();
            this.launchUDPServer();
            System.out.println(">> Tous les serveurs sont démarrés.");
            this.gestionConnexions();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Méthode gérant les connexions aux différentes 'channel'.
     *
     * @throws IOException
     */
    private void gestionConnexions() throws IOException {
        while (true) {
            selector.select();
            Set selectedKeys = selector.selectedKeys();
            Iterator iter = selectedKeys.iterator();
            while (iter.hasNext()) {
                SelectionKey key = (SelectionKey) iter.next();
                if (key.isAcceptable()) {
                    this.registerKey(key);
                }
                if (key.isReadable()) {
                    if (key.channel() instanceof SocketChannel) {
                        SocketChannel client = (SocketChannel) key.channel();
                        int portSelected = client.socket().getLocalPort();
                        if (this.port == portSelected) {
                            this.redirection(client);
                        } else if (this.tcp_server.getPort() == portSelected) {
                            this.resetBuffer();
                            client.read(this.buf);
                            this.tcp_server.answer(new String(this.buf.array()).trim(), client);
                            this.buf.clear();
                        }
                    } else {
                        this.udp_server.answer(key);
                    }
                }
                iter.remove();
            }
        }
    }

    /**
     * Méthode permettant de remettre à zéro le buffer.
     */
    private void resetBuffer(){
        this.buf = ByteBuffer.allocate(2048);
    }
    /**
     * Méthode permettant de rediriger le client vers un serveur de cryptage.
     *
     * @param client client à rediriger
     * @throws IOException
     */
    private void redirection(SocketChannel client) throws IOException {
        this.resetBuffer();
        client.read(this.buf);
        String res = this.traitementRead();
        this.buf.clear();
        client.write(ByteBuffer.wrap(res.getBytes()));
        EnumSet<Crypteurs> crypteurs = EnumSet.allOf(Crypteurs.class);
        client.write(ByteBuffer.wrap(crypteurs.toString().getBytes()));
        client.close();
        System.out.println(">> Client déconnecté du serveur d'accueil.");
        this.buf.clear();
    }

    /**
     * Méhode permettant d'identifier et d'enregistrer une connexion.
     *
     * @param key connexion
     * @throws IOException
     */
    private void registerKey(SelectionKey key) throws IOException {
        SocketChannel socketChannel = ((ServerSocketChannel) key.channel()).accept();
        socketChannel.configureBlocking(false);
        int portSelected = socketChannel.socket().getLocalPort();
        if (this.port == portSelected) {
            System.out.println(">> Client sur le serveur d'accueil");
            this.register(socketChannel);
        } else if (this.tcp_server.getPort() == portSelected) {
            System.out.println(">> Client sur le serveur de cryptage TCP");
            this.register(socketChannel);
        } else if (this.udp_server.getPort() == portSelected) {
            System.out.println(">> Client sur le serveur de cryptage UDP");
            this.udp_server.register_udp(this.selector);
        }
    }

    /**
     * Traitement de la demande d'autorisation de connexion auprès du serveur de cryptage.
     *
     * @return Message à envoyer au client.
     */
    private String traitementRead() {
        String reception = new String(this.buf.array()).trim().toLowerCase();
        if (reception.equals("tcp")) {
            System.out.println(">> Demande d'accès au serveur de cryptage TCP ...");
            return this.tcp_server.newIdc();
        } else if (reception.equals("udp")) {
            System.out.println(">> Demande d'accès au serveur de cryptage UDP ...");
            return this.udp_server.newIdc();
        } else {
            System.out.println(">> Erreur de demande du client ...");
            return "> ACCES REFUSE";
        }
    }

    /**
     * Méthode permettant de lancer le serveur de communication TCP.
     */
    private void launchTCPServer() throws IOException {
        this.tcp_server.launch(this.selector);
    }

    /**
     * Méthode permettant de lancer le serveur de communication UDP.
     */
    private void launchUDPServer() throws IOException {
        this.udp_server.launch(this.selector);
    }

    /**
     * Méthode permettant d'enregistrer le client tcp.
     *
     * @param socketChannel
     * @throws IOException
     */
    private void register(SocketChannel socketChannel) throws IOException {
        socketChannel.register(selector, SelectionKey.OP_READ);
    }

    /**
     * Méthode permettant d'éteindre le serveur.
     *
     * @throws IOException
     */
    public void shutdown() throws IOException {
        this.socket_server.close();
    }

    public static void main(String args[]) throws IOException {
        MainServer ss;
        if (args.length == 0) ss = new MainServer();
        else ss = new MainServer(Integer.parseInt(args[0]));
        ss.launch();
        ss.shutdown();
    }
}
