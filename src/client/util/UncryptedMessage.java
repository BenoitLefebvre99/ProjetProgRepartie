package client.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Classe représentant un message non crypté.
 */
public class UncryptedMessage {
    private String message;
    private String shift;

    /**
     * Constructeur permettant de créer un message non crypté
     */
    public UncryptedMessage() {
        this("", "0");
    }

    /**
     * Constructeur permettant d'intégrer un certain message et un certain pas.
     *
     * @param message nouveau message
     * @param shift   pas à appliquer
     */
    public UncryptedMessage(String message, String shift) {
        this.message = message;
        this.shift = shift;
    }

    /**
     * Méthode permettant de changer le message non crypté.
     *
     * @param message nouveau message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Méthode permettant de changer le pas à appliquer au cryptage.
     *
     * @param shift pas à appliquer
     */
    public void setShift(String shift) {
        this.shift = shift;
    }

    /**
     * Méthode appliquant la saisie au client
     */
    public void saisie(int crypteur) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println(">> Quel est le message ?\n");
        String res;
        res = br.readLine();
        this.setMessage(res);
        this.choixKey(crypteur, br);
    }

    /**
     * Méthode privée appliquant la clé par rapport au crypteur choisi.
     *
     * @param crypteur id du crypteur
     * @param br       BufferedReader
     * @throws IOException erreur lors de la saisie de la clé.
     */
    private void choixKey(int crypteur, BufferedReader br) throws IOException {
        String res;
        if (crypteur == 0) {
            do {
                System.out.print("\n>> Quel est le pas à appliquer (shift) ? ");
                res = br.readLine();
            } while (!this.isInteger(res));
        } else {
            do {
                System.out.print("\n>> Quelle est la clé à appliquer ? ");
                res = br.readLine();
            } while (res.contains(" ") || res.isEmpty());
        }
        this.setShift(res);
    }

    /**
     * Méthode permettant de tester si la saisie est bel et bien un int
     *
     * @param test String à tester
     * @return true si int
     */
    private boolean isInteger(String test) {
        try {
            Integer.parseInt(test);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Méthode permettant de récupérer le message.
     *
     * @return String le message non crypté
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * Méthode permettant de récupérer le pas à appliquer.
     *
     * @return String le pas
     */
    public String getShift() {
        return this.shift;
    }
}
