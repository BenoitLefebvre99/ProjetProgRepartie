package server.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class ServerUdp {
    private DatagramSocket server_socket;
    private int port;

    /**
     * Constructeur créant un serveur UDP au port souhaité.
     * @param port
     */
    public ServerUdp(int port) throws SocketException {
        this.port = port;
        this.server_socket = new DatagramSocket(this.port);
    }

    /**
     * Méthode permettant de lancer le serveur UDP.
     */
    public void launch() {
        DatagramPacket dgp = new DatagramPacket(new byte[0], 0);

    }

    /**
     * Méthode permettant d'éteindre le serveur UDP.
     */
    public void shutdown() {
        this.server_socket.close();
    }

    public static void main(String[] args) throws SocketException {
        ServerUdp server = new ServerUdp(12345);
        server.launch();
        server.shutdown();
    }
}
