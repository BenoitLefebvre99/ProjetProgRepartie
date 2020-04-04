package client.file;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class LectureFichier {
    public final String PATH_LECTURE = "config.txt";

    private File fichierLecture;
    private String protocol;
    private ArrayList<MessageFile> listMessage;
    private boolean good;

    public LectureFichier() {
        this.protocol = "";
        this.listMessage = new ArrayList<>();
        this.good = true;
        this.lecture();
    }

    private void lecture() {
        try {
            this.fichierLecture = new File(PATH_LECTURE);
            Scanner reader = new Scanner(this.fichierLecture);
            this.protocol = reader.nextLine();
            while (reader.hasNextLine()) {
                String res = "";
                for (int i = 0; i < 3; i++) {
                    res += reader.nextLine();
                    if (i < 2) res += "\n";
                }
                this.listMessage.add(new MessageFile(res));
            }
            reader.close();
        } catch (Exception e) {
            System.out.println(">> Il y a eu un problème lors de la lecture du fichier : " + PATH_LECTURE);
            this.good = false;
        }
    }

    public ArrayList<MessageFile> toList() {
        return this.listMessage;
    }

    public boolean isGood() {
        return this.good;
    }
    public String getProtocol() {
        return this.protocol.toUpperCase();
    }
}
