package client;

import client.file.Communication;
import client.file.LectureFichier;
import client.file.MessageFile;

import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Classe principale gérant le traitement du cryptage du fichier config.txt.
 */
public class MainLectureFichier {
    private String nomFichier;
    private ArrayList<MessageFile> toCrypt;
    private ArrayList<String> toWriteInFile;
    private LectureFichier lecture;

    /**
     * Constructeur initialisant et lançant le traitement.
     */
    public MainLectureFichier() {
        this.nomFichier = this.newName();
        this.lecture = new LectureFichier();
        this.toCrypt = this.lecture.toList();
        this.toWriteInFile = new ArrayList<>();
        this.crypter();
        this.createFinalFile();
    }

    /**
     * Méthode privée permettant de faire crypter les messages.
     */
    private void crypter() {
        Communication c = new Communication(this.lecture.getProtocol(), this.toCrypt);
        this.toWriteInFile = c.getResult();
    }

    /**
     * Méthode privée permettant de générer un nouveau nom de fichier.
     *
     * @return nom sous la forme "traite_yyyyMMdd-HHMMSS.txt"
     */
    private String newName() {
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("yyyyMMdd-HHMMSS");
        return "traite_" + df.format(date) + ".txt";
    }

    /**
     * Méthode privée permettant de générer le fichier final.
     */
    private void createFinalFile() {
        if (this.lecture.isGood()) {
            try {
                FileWriter writer = new FileWriter(this.nomFichier);
                for (String res : this.toWriteInFile) {
                    writer.write(res);
                }
                writer.close();
                System.out.println(">> Tout a bien été traité !");
            } catch (Exception e) {
                System.out.println(">> Erreur lors de la création du fichier : " + this.nomFichier);
            }
        }
    }

    public static void main(String[] args) {
        MainLectureFichier mlf = new MainLectureFichier();
        System.out.println("\n>> JUST FINISHED !");
    }
}
