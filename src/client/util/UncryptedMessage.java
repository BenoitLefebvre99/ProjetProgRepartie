package client.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class UncryptedMessage {
    private String message;
    private int shift;

    /**
     * Constructeur permettant de créer un message non crypté
     */
    public UncryptedMessage() {
        this("", 0);
    }

    /**
     * Constructeur permettant d'intégrer un certain message et un certain pas.
     *
     * @param message nouveau message
     * @param shift   pas à appliquer
     */
    public UncryptedMessage(String message, int shift) {
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
    public void setShift(int shift) {
        this.shift = shift;
    }

    /**
     * Méthode appliquant la saisie au client
     */
    public void saisie() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println(">> Quel est le message ?\n");
        String res = "";
        do {
            res += br.readLine();
        } while (!res.contains("\r"));
        this.setMessage(res);
        do {
            System.out.print(">> Quel est le pas à appliquer (shift) ? ");
            res = br.readLine();
        } while (this.isInteger(res));
        this.setShift(Integer.parseInt(res));
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
     * @return int le pas
     */
    public int getShift() {
        return this.shift;
    }
}
