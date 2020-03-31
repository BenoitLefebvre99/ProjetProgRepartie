package client.util;


import java.io.IOException;
import java.util.ArrayList;

public class CrypteursAvailable {
    private ArrayList<String> listCrypteurs;
    private boolean bug;
    private String message;

    public CrypteursAvailable(String tableauServeur) {
        this.listCrypteurs = new ArrayList<>();
        this.message = tableauServeur;
        this.bug = this.lecture();
    }

    /**
     * Méthode lisant le message reçu du serveur et découpant les différents crypteurs pour les ajouter dans la liste.
     *
     * @return false si la liste est vide
     */
    private boolean lecture() {
        if (this.message.length() > 2) {
            this.message = this.supprimerCrochets(this.message);
            boolean tmp = true;
            while (tmp) {
                tmp = this.addNext();
            }
            return true;
        }
        return false;
    }

    /**
     * Ajoute le prochain crypteur dans la liste des crypteurs.
     *
     * @return false si fin de liste.
     */
    private boolean addNext() {
        String toAdd;
        if (this.message.contains(",")) {
            toAdd = this.message.substring(0, this.message.indexOf(','));
            if (toAdd.charAt(0) == ' ') {
                toAdd = toAdd.substring(1);
            }
            this.listCrypteurs.add(toAdd);
            this.message = this.message.substring(this.message.indexOf(',') + 1);
            return true;
        } else if (this.message.length() > 0) {
            if(this.listCrypteurs.isEmpty()) toAdd = this.message.substring(0);
            else toAdd = this.message.substring(1);
            this.listCrypteurs.add(toAdd);
            return false;
        }
        return false;
    }

    /**
     * Méthode supprimant les "[]" du message reçu.
     *
     * @param message message reçu du client
     * @return le même message mais sans les "[]"
     */
    private String supprimerCrochets(String message) {
        return message.substring(1, message.length() - 1);
    }

    /**
     * Méthode renvoyant la liste des crypteurs disponibles sous forme de liste.
     *
     * @return liste des crypteurs disponibles.
     */
    public String toString() {
        String res = "";
        for (int i = 0; i < this.listCrypteurs.size(); i++) {
            res += i + ". " + this.listCrypteurs.get(i) + "\n";
        }
        return res;
    }

    public static void main(String args[]) throws IOException {
        CrypteursAvailable ca = new CrypteursAvailable("[CESAR, VIGENERE]");
        System.out.print(ca.toString());
    }
}
