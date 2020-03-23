package server.util;

import java.util.ArrayList;

public class AllowList {
    private ArrayList<Long> allowList;

    /**
     * Constructeur AllowList() permettant de créer une liste d'IDC autorisés.
     */
    public AllowList() {
        this.allowList = new ArrayList<>();
    }

    /**
     * Méthode permettant d'ajouter un nouvel idc à la liste autorisée.
     * @param idc identifiant à ajouter.
     */
    public void add(long idc) {
        this.allowList.add(idc);
    }

    /**
     * Méhode permettant de savoir si la liste contient un idc.
     * @param idc identifiant à tester.
     * @return true si la liste contient l'idc
     */
    public boolean contains(long idc) {
        return this.allowList.contains(idc);
    }

    /**
     * Methode qui retire un idc de la liste.
     * @param idc identifiant à supprimer
     * @return true si l'idc a été supprimé.
     */
    public boolean remove(long idc) {
        return this.allowList.remove(idc);
    }
}
