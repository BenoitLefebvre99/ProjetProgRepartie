package client.file;

public class MessageFile {
    private String shift;
    private String crypteur;
    private String message;
    private String brut;
    private boolean good;

    public MessageFile(String brut) {
        this.brut = brut;
        this.good = true;
        this.cut();
    }

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

    private void next() {
        this.brut = this.brut.substring(this.brut.indexOf('\n') + 1);
    }

    public String getFinal() {
        return this.crypteur + ":" + this.shift + ":" + this.message;
    }

    public boolean isGood() {
        return this.good;
    }
}
