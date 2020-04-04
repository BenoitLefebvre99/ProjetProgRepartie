package client.util;

import java.util.ArrayList;

/**
 * Classe représentant la liste de crypteurs disponibles sur le serveur.
 */
public class CrypteursAvailable {
    private ArrayList<String> listCrypteurs;
    private boolean bug;
    private String message;

    /**
     * Constructeur permettant de créer la liste de crypteurs.
     *
     * @param tableauServeur liste reçue du serveur.
     */
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
            toAdd = this.noBlanc(toAdd);
            this.listCrypteurs.add(toAdd);
            this.message = this.message.substring(this.message.indexOf(',') + 1);
            return true;
        } else if (this.message.length() > 0) {
            if (this.listCrypteurs.isEmpty()) toAdd = this.message;
            else toAdd = this.message.substring(1);
            this.listCrypteurs.add(toAdd);
        }
        return false;
    }

    /**
     * Méthode privée passant un espace s'il y en a un.
     *
     * @param toAdd le message à traiter
     * @return message traîté
     */
    private String noBlanc(String toAdd) {
        if (toAdd.charAt(0) == ' ') {
            toAdd = toAdd.substring(1);
        }
        return toAdd;
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

    /**
     * Méthode vérifiant que la chaîne de caractères correspond à un crypteur.
     *
     * @param res chaîne de caractère à tester
     * @return true si correspond
     */
    public boolean contains(String res) {
        try {
            int tmp = Integer.parseInt(res);
            if (!this.listCrypteurs.get(tmp).isEmpty()) return true;
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
