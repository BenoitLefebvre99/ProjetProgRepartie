package client;

public class ClientResponse {
    public final int MAX_MSG_LENGTH = 100;

    private long idc;
    private int shift;
    private String beforeCrypt;
    private String afterCrypt;

    public ClientResponse(long idc, int shift, String msg) {
        this.idc = idc;
        this.shift = shift;
        this.beforeCrypt = msg;
        this.crypt();
    }

    /**
     * Méthode cryptant le message contenu dans this.beforeCrypt.
     */
    private void crypt() {
        int max = Math.max(this.beforeCrypt.length(), MAX_MSG_LENGTH);
        this.afterCrypt = "";
        char c;
        for(int index = 0; index < max; index++) {
            c = (char) (this.beforeCrypt.charAt(index) + this.shift);
            if((c != ' ') && !(c <= 'z' && c >= 'a') && !(c <= 'Z' && c >= 'A')) {
                if(c < 'A') {
                    c = (char) ('Z' + this.shift);
                } else if ( c > 'Z' && c < 'a' ) {
                    if( this.shift > 0) {
                        c = (char) ('A' + this.shift);
                    } else {
                        c = (char) ('z' + this.shift );
                    }
                } else {
                    c = (char) ('a' + this.shift);
                }
            }
            this.afterCrypt += c;
        }
    }

    /**
     * Méthode permettant de changer le message de réponse.
     * @param msg message non crypté.
     */
    public void setMessage(String msg) {
        this.beforeCrypt = msg;
        this.crypt();
    }

    /**
     * Méthode permettant de récupérer la réponse complète du client.
     * @return IDC:SHIFT:MSG
     */
    public String getClientResponse() {
        return this.idc + ":" + this.shift + ":" + this.afterCrypt;
    }
}
