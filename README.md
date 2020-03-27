# progrep_lefebvre_benoit

Projet de Programmation Répartie, Lefebvre Benoit Alternant

Socket socket_client = socket_server.accept();
        System.out.println("> Connexion d'un client ...");
        InputStreamReader isr = new InputStreamReader(socket_client.getInputStream());
        BufferedReader br = new BufferedReader(isr);
        String str;
        str = br.readLine();
        if (str.toLowerCase().equals("tcp")) {
            System.out.println("> Demande d'accès au serveur de cryptage TCP...");
            this.sendIdc(this.tcp_server.newIdc(), socket_client);
            socket_client.close();
            this.launchTCPServer();
        } else if (str.toLowerCase().equals("udp")) {
            System.out.println("> Demande d'accès au serveur de cryptage UDP...");
            this.sendIdc(this.udp_server.newIdc(), socket_client);
            socket_client.close();
            this.launchUdpServer();
        }
        socket_client.close();