package server;

import com.sun.security.ntlm.Server;
import server.tcp.ServerTcp;
import server.udp.ServerUdp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class MainServer {
    private ServerSocket socket_server;
    private int port;

    private ServerUdp udp_server;
    private ServerTcp tcp_server;
    /**
     * Méthode permettant de créer un serveur TCP d'accueil au port indiqué.
     *
     * @param port port souhaité.
     * @throws IOException
     */
    public MainServer(int port) throws IOException {
        this.socket_server = new ServerSocket(port);
        this.port = port;
        this.udp_server = new ServerUdp();
        this.tcp_server = new ServerTcp();
    }

    /**
     * Méthode permettant de créer un serveur TCP d'accueil au port par défaut (12345).
     *
     * @throws IOException
     */
    public MainServer() throws IOException {
        this(12345);
    }

    /**
     * Méthode permettant de lancer le serveur.
     *
     * @throws IOException
     */
    public void launch() throws IOException {
        this.launchTCPServer();
        this.launchUdpServer();
        Socket socket_client = socket_server.accept();
        InputStreamReader isr = new InputStreamReader(socket_client.getInputStream());
        BufferedReader br = new BufferedReader(isr);
        String str;
        str = br.readLine();

        if (str.toLowerCase().equals("tcp")) {
            this.sendIdc(this.tcp_server.newIdc(), socket_client);
        } else if (str.toLowerCase().equals("udp")) {
            this.sendIdc(this.udp_server.newIdc(), socket_client);
        }
        socket_client.close();
    }

    /**
     * Méthode envoyant le message contenant l'idc au client.
     * @param init String "IDC:SHIFT:MSG"
     * @param socket_client Socket du client
     * @throws IOException Erreur
     */
    private void sendIdc(String init, Socket socket_client) throws IOException {
        OutputStream os = socket_client.getOutputStream();
        PrintWriter print = new PrintWriter(os, true);
        print.println(init);
    }

    /**
     * Méthode permettant de lancer le serveur de communication TCP.
     */
    private void launchTCPServer() {
        try {
            this.tcp_server.launch();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Méthode permettant de lancer le serveur de communication UDP.
     */
    private void launchUdpServer() {
        try {
            this.udp_server.launch();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Méthode permettant d'éteindre le serveur.
     *
     * @throws IOException
     */
    public void shutdown() throws IOException {
        this.socket_server.close();
        this.tcp_server.shutdown();
        this.udp_server.shutdown();
    }

    public static void main(String args[]) throws IOException {
        MainServer ss;
        if(args.length == 0 ) ss = new MainServer();
        else ss = new MainServer(Integer.parseInt(args[0]));
        ss.launch();
        ss.shutdown();
    }
}
