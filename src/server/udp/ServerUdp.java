package server.udp;

import server.util.AllowList;
import server.util.Idc;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class ServerUdp {
    private DatagramSocket server_socket;
    private int port;
    private AllowList allowList;

    /**
     * Constructeur créant un serveur UDP au port souhaité.
     * @param port int port souhaité
     */
    public ServerUdp(int port) throws SocketException {
        this.port = port;
        this.server_socket = new DatagramSocket(this.port);
        this.allowList = new AllowList();
    }

    /**
     * Méthode permettant de créer un serveur UDP au port par défaut 7777
     * @throws SocketException
     */
    public ServerUdp() throws SocketException {
        this(7777);
    }

    /**
     * Méthode permettant de créer, autoriser et récupérer l'idc d'un nouveau client.
     * @return String "IDC:PROTOCOL:PORT"
     */
    public String newIdc() {
        Idc nouveauClient = new Idc("udp", this.port);
        this.allowList.add(nouveauClient.getIdc());
        return nouveauClient.getServerResponse();
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

}
