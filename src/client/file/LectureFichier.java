package client.file;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Classe LectureFichier permettant la lecture du contenu du fichier config.txt (racine du projet).
 */
public class LectureFichier {
    public final String PATH_LECTURE = "config.txt";

    private File fichierLecture;
    private String protocol;
    private ArrayList<MessageFile> listMessage;
    private boolean good;

    /**
     * Constructeur permettant d'initialiser et de lancer la lecture du fichier.
     */
    public LectureFichier() {
        this.protocol = "";
        this.listMessage = new ArrayList<>();
        this.good = true;
        this.lecture();
    }

    /**
     * Méthode privée permettant la lecture complète du fichier.
     */
    private void lecture() {
        try {
            this.fichierLecture = new File(PATH_LECTURE);
            Scanner reader = new Scanner(this.fichierLecture);
            this.protocol = reader.nextLine();
            this.reading(reader);
            reader.close();
        } catch (Exception e) {
            System.out.println(">> Il y a eu un problème lors de la lecture du fichier : " + PATH_LECTURE);
            this.good = false;
        }
    }

    /**
     * Méthode privée éxécutant la lecture par le scanner.
     *
     * @param reader Scanner à utiliser.
     */
    private void reading(Scanner reader) {
        while (reader.hasNextLine()) {
            String res = "";
            for (int i = 0; i < 3; i++) {
                res += reader.nextLine();
                if (i < 2) res += "\n";
            }
            this.listMessage.add(new MessageFile(res));
        }
    }

    /**
     * Méthode permettant de récupérer la liste des messages contenus dans le fichier config.txt.
     *
     * @return Arraylist des messages.
     */
    public ArrayList<MessageFile> toList() {
        return this.listMessage;
    }

    /**
     * Méthode permettant de confirmer que tout s'est bien déroulé.
     *
     * @return
     */
    public boolean isGood() {
        return this.good;
    }

    /**
     * Méthode permettant de récupérer le protocol à utiliser.
     *
     * @return protocole.
     */
    public String getProtocol() {
        return this.protocol.toUpperCase();
    }
}
