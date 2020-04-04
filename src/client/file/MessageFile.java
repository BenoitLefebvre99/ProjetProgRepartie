package client.file;

/**
 * Classe représentant un message à crypter dans le fichier config.txt.
 */
public class MessageFile {
    private String shift;
    private String crypteur;
    private String message;
    private String brut;
    private boolean good;

    /**
     * Constructeur créant un Message.
     *
     * @param brut message brut
     */
    public MessageFile(String brut) {
        this.brut = brut;
        this.good = true;
        this.cut();
    }

    /**
     * Méthode privée découpant le message brut.
     */
    private void cut() {
        try {
            this.crypteur = this.brut.substring(0, this.brut.indexOf('\n'));
            this.next();
            this.shift = this.brut.substring(0, this.brut.indexOf('\n'));
            this.next();
            this.message = this.brut;
        } catch (Exception e) {
            System.out.print(">> Le format n'est pas bon !");
            this.good = false;
        }
    }

    /**
     * Méthode privée permettant de passer une ligne.
     */
    private void next() {
        this.brut = this.brut.substring(this.brut.indexOf('\n') + 1);
    }

    /**
     * Méthode permettant de récupérer le message à envoyer.
     *
     * @return message sous la forme "IDCRYPTEUR:IDSHIFT:MESSAGE"
     */
    public String getFinal() {
        return this.crypteur + ":" + this.shift + ":" + this.message;
    }

    /**
     * Méthode permettant de vérifier que tout s'est bien déroulé.
     *
     * @return true si tout est ok.
     */
    public boolean isGood() {
        return this.good;
    }
}
