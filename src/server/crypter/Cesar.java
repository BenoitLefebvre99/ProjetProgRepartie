package server.crypter;

public class Cesar implements ICryptage {
    private String before;
    private String after;
    private int shift;

    /**
     * Constructeur Cryptage().
     *
     * @param messageUncoded message non codé.
     * @param shift          pas de cryptage.
     */
    public Cesar(String messageUncoded, int shift) {
        this.before = messageUncoded;
        this.shift = shift;
        this.crypt();
    }

    @Override
    public void crypt() {
        int max = Math.min(this.before.length(), MAX_MSG_LENGTH);
        this.after = "";
        char c;
        for (int index = 0; index < max; index++) {
            if (this.alphaChar(this.before.charAt(index))) {
                c = (char) (this.before.charAt(index) + this.shift);
                if (!(c <= 'z' && c >= 'a') && !(c <= 'Z' && c >= 'A')) {
                    if (c < 'A') {
                        c = (char) ('Z' - ('A' - c - 1));
                    } else if (c > 'Z' && c < 'a') {
                        if (this.shift > 0) {
                            c = (char) ('A' + (c - 'Z' - 1));
                        } else {
                            c = (char) ('z' - ('a' - c - 1));
                        }
                    } else {
                        c = (char) ('a' + (c - 'z' - 1));
                    }
                }
                this.after += c;
            } else {
                this.after += this.before.charAt(index);
            }
        }
    }

    /**
     * Méthode privée permettant de savoir si le caractère est compris dans ['a','z'] ou ['A','Z'].
     *
     * @param c caractère à tester
     * @return true si dans un des intervalles.
     */
    private boolean alphaChar(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    @Override
    public String getCodedMessage() {
        return this.after;
    }

}
