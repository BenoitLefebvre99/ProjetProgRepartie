package server.crypter;

/**
 * Interface définissant un crypteur.
 */
interface ICryptage {
    int MAX_MSG_LENGTH = 100;

    /**
     * Méthode renvoyant le message codé.
     */
    String getCodedMessage();

    /**
     * Méthode permettant de récupérer le message codé.
     *
     * @return String : msg codé
     */
    void crypt();
}
