package client.file;

import client.util.ServerInit;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

public class Communication {
    private SocketChannel socket_client;
    private DatagramChannel data_channel;
    private ByteBuffer buf;
    private ServerInit server;
    private String address;
    private int port;

    private String protocol;
    private ArrayList<MessageFile> crypt;
    private ArrayList<String> result;

    public Communication(String protocol, ArrayList<MessageFile> crypt) {
        this.protocol = protocol;
        this.port = 12345;
        this.address = "localhost";
        this.crypt = crypt;
        this.result = new ArrayList<>();
        this.resetBuffer();
        this.launch();
    }

    private void launch() {
        try {
            this.connexionServer();
            this.send(this.protocol, "tcp");
            this.server = new ServerInit(this.receive("tcp"));
            this.receive("tcp");
            this.disconnect();
            this.port = this.server.getPort();
            this.connexionServer();
            if (this.server.getKeepAlive()) {
                for (MessageFile m: this.crypt) {
                    if(m.isGood()) {
                        this.send(this.server.getIdc() + ":" + m.getFinal(), this.protocol);
                        this.result.add(this.receive(this.protocol));
                    }
                }
            }
        } catch(Exception e) {
            System.out.println(">> Erreur lors de la communication avec le serveur.");
        }
    }

    private void resetBuffer () {
        this.buf = ByteBuffer.allocate(2048);
    }

    private String receive(String protocol) throws IOException {
        this.resetBuffer();
        String res;
        if (protocol.toLowerCase().equals("tcp")) {
            this.socket_client.read(this.buf);
        } else if (protocol.toLowerCase().equals("udp")) {
            this.data_channel.receive(this.buf);
        }
        res = new String(this.buf.array()).trim();
        if (res.equals(">> Déconnexion du client.")
                || res.equals("Accès réfusé, cet IDC n'existe pas sur le serveur.")) {
            this.server.stop();
        }
        this.buf.flip();
        return res;
    }

    private void send(String str, String protocol) throws IOException {
        this.buf = ByteBuffer.allocate(str.length() * 8);
        this.buf = ByteBuffer.wrap(str.getBytes());
        if (protocol.toLowerCase().equals("tcp")) {
            this.socket_client.write(this.buf);
        } else if (protocol.toLowerCase().equals("udp")) {
            SocketAddress server = new InetSocketAddress(this.address, this.port);
            this.data_channel.send(this.buf, server);
        }
    }

    private void connexionServer() throws IOException {
        if (this.protocol.toLowerCase().equals("tcp")) {
            this.socket_client = SocketChannel.open();
            this.socket_client.connect(new InetSocketAddress(this.address, this.port));
        } else if (this.protocol.toLowerCase().equals("udp")) {
            this.data_channel = DatagramChannel.open();
            SocketAddress address = new InetSocketAddress(0);
            DatagramSocket socket = data_channel.socket();
            socket.bind(address);
        }
    }

    public void disconnect() throws IOException {
        this.socket_client.close();
    }

    public ArrayList<String> getResult() {
        return this.result;
    }
}
