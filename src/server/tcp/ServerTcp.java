package server.tcp;

import server.util.AllowList;
import server.util.Idc;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerTcp {
    private ServerSocket socket_server;
    private int port;
    private AllowList allowList;

    /**
     * Méthode permettant de créer un serveur TCP au port indiqué.
     *
     * @param port port souhaité.
     * @throws IOException
     */
    public ServerTcp(int port) throws IOException {
        this.socket_server = new ServerSocket(port);
        this.port = port;
        this.allowList = new AllowList();
    }

    /**
     * Méthode permettant de créer un serveur TCP au port par défaut (12345).
     *
     * @throws IOException
     */
    public ServerTcp() throws IOException {
        this(6666);
    }

    /**
     * Méthode permettant de lancer le serveur TCP.
     *
     * @throws IOException
     */
    public void launch() throws IOException {
        Socket socket_client = socket_server.accept();

        socket_client.close();
    }

    /**
     * Méthode permettant de générer un nouvel idc.
     * @return String "IDC:PROTOCOLE:PORT"
     */
    public String newIdc() {
        Idc nouveauClient = new Idc("tcp", this.port);
        this.allowList.add(nouveauClient.getIdc());
        return nouveauClient.getServerResponse();
    }

    /**
     * Méthode permettant d'éteindre le serveur.
     *
     * @throws IOException
     */
    public void shutdown() throws IOException {
        this.socket_server.close();
    }
}
