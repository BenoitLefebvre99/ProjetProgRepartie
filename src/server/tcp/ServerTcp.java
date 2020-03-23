package server.tcp;

import server.util.AllowList;
import server.util.ClientResponse;
import server.util.Idc;

import java.io.*;
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
        InputStreamReader isr = new InputStreamReader(socket_client.getInputStream());
        BufferedReader br = new BufferedReader(isr);
        String str;
        str = br.readLine();

        ClientResponse cr = new ClientResponse(str);
        if(this.allowList.contains(cr.getIdc())) {
            if(cr.isKeepAlive()){
                this.send(cr.getCryptedMessage(), socket_client);
            } else {
                this.send("Déconnexion.", socket_client);
            }
        } else {
            this.send("Accès réfusé, cet IDC n'existe pas.", socket_client);
        }
        socket_client.close();
    }

    /**
     * Méthode permettant d'envoyant une string au client.
     * @param str string a envoyer
     * @param socket_client Socket du client
     * @throws IOException
     */
    private void send(String str, Socket socket_client) throws IOException {
        OutputStream os = socket_client.getOutputStream();
        PrintWriter print = new PrintWriter(os, true);
        print.println(str);
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
