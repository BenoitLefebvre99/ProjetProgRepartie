package server;

import server.tcp.ServerTcp;
import server.udp.ServerUdp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    private ServerSocket socket_server;

    /**
     * Méthode permettant de créer un serveur TCP au port indiqué.
     *
     * @param port port souhaité.
     * @throws IOException
     */
    public Main(int port) throws IOException {
        this.socket_server = new ServerSocket(port);
    }

    /**
     * Méthode permettant de créer un serveur TCP au port par défaut (12345).
     *
     * @throws IOException
     */
    public Main() throws IOException {
        this(12345);
    }

    /**
     * Méthode permettant de lancer le serveur.
     *
     * @throws IOException
     */
    public void launch() throws IOException {
        Socket socket_client = socket_server.accept();
        InputStreamReader isr = new InputStreamReader(socket_client.getInputStream());
        BufferedReader br = new BufferedReader(isr);
        String str;
        str = br.readLine();

        if (str.toLowerCase().equals("tcp")) {
            this.launchTcpServer();
        } else if (str.toLowerCase().equals("udp")) {
            this.launchUdpServer();
        }
        socket_client.close();
    }

    /**
     * Méthode permettant de lancer le serveur de communication TCP.
     */
    private void launchTcpServer() {
        ServerTcp tcp_server = new ServerTcp();
    }

    /**
     * Méthode permettant de lancer le serveur de communication UDP.
     */
    private void launchUdpServer() {
        ServerUdp udp_server = new ServerUdp();
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
        Main ss = new Main();
        ss.launch();
        ss.shutdown();
    }
}
