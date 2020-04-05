package server.crypter;

/**
 * Interface définissant un crypteur.
 */
interface ICryptage {
    int MAX_MSG_LENGTH = 100;

    /**
     * Méthode renvoyant le message codé.
     *
     * @return message codé.
     */
    String getCodedMessage();

    /**
     * Méthode permettant de récupérer le message codé.
     */
    void crypt();
}
