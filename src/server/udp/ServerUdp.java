package server.udp;

import server.util.AllowList;
import server.util.ClientResponse;
import server.util.Idc;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class ServerUdp {
    public final static int MAX = 100;

    //private DatagramSocket server_socket;
    private int port;
    private AllowList allowList;

    private InetAddress addressClient;
    private int portClient;

    /**
     * Constructeur créant un serveur UDP au port souhaité.
     * @param port int port souhaité
     */
    public ServerUdp(int port) throws SocketException {
        this.port = port;
        //this.server_socket = new DatagramSocket(this.port);
        this.allowList = new AllowList();
        this.addressClient = null;
        this.portClient = 0;
    }

    /**
     * Méthode permettant de créer un serveur UDP au port par défaut 7777
     * @throws SocketException
     */
    public ServerUdp() throws SocketException {
        this(12347);
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
     * Méthode renvoyant le port du serveur de cryptage UDP.
     * @return int port
     */
    public int getPort() {
        return this.port;
    }

    /**
     * Méthode permettant de lancer le serveur UDP.
     */
   /* public void launch() {
        try {
            ClientResponse cr = new ClientResponse(this.receive());
            if(this.allowList.contains(cr.getIdc())){
                if(cr.isKeepAlive()) {
                    this.send(cr.getCryptedMessage());
                } else {
                    this.send("Déconnexion.");
                }
            } else {
                this.send("Accès réfusé, cet IDC n'existe pas.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    /**
     * Méthode permettant de recevoir un message de la part du client.
     * @return String message reçu.
     */
    /*private String receive() throws IOException {
        DatagramPacket dgp = new DatagramPacket(new byte[MAX], MAX);
        this.server_socket.receive(dgp);
        this.addressClient = dgp.getAddress();
        this.portClient = dgp.getPort();
        return new String(dgp.getData(), 0, dgp.getLength());
    }*/

    /**
     * Méthode envoyant un message au client.
     * @param str String
     * @throws IOException
     */
   /* private void send(String str) throws IOException {
        byte[] buf = new byte[1];
        int taille;
        DatagramPacket dgp = new DatagramPacket(buf,1, this.addressClient, this.portClient);
        buf = str.getBytes();
        for (int pas = 0; pas < buf.length; pas += MAX) {
            taille = Math.min(buf.length - pas, MAX);
            dgp.setData(buf, pas, taille);
            this.server_socket.send(dgp);
        }
        this.server_socket.send(dgp);
    }*/

    /**
     * Méthode permettant d'éteindre le serveur UDP.
     */
   /* public void shutdown() {
        this.server_socket.close();
    }*/

}
