package server;

import server.udp.ServerUdp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerTcp {
    private ServerSocket socket_server;
    private int port;

    /**
     * Méthode permettant de créer un serveur TCP au port indiqué.
     *
     * @param port port souhaité.
     * @throws IOException
     */
    public ServerTcp(int port) throws IOException {
        this.socket_server = new ServerSocket(port);
        this.port = port;
    }

    /**
     * Méthode permettant de créer un serveur TCP au port par défaut (12345).
     *
     * @throws IOException
     */
    public ServerTcp() throws IOException {
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
            this.communicationClientTcp();
        } else if (str.toLowerCase().equals("udp")) {
            this.launchUdpServer();
        }
        socket_client.close();
    }

    /**
     * Méthode permettant de communiquer avec le client sur le serveur TCP.
     */
    private void communicationClientTcp() {

    }

    /**
     * Méthode permettant de lancer le serveur de communication UDP.
     */
    private void launchUdpServer() {
        try {
            ServerUdp udp_server = new ServerUdp(this.port);
            udp_server.launch();
            udp_server.shutdown();
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
    }

    public static void main(String args[]) throws IOException {
        ServerTcp ss = new ServerTcp();
        ss.launch();
        ss.shutdown();
    }
}
