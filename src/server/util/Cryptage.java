package server.util;

import java.util.Random;

public class Cryptage {
    public final int MAX_MSG_LENGTH = 100;

    private String before;
    private String after;
    private int shift;

    /**
     * Constructeur Cryptage().
     * @param messageUncoded message non codé.
     * @param shift pas de cryptage.
     */
    public Cryptage(String messageUncoded, int shift) {
        this.before = messageUncoded;
        this.shift = shift;
        this.crypt();
    }

    /**
     * Méthode cryptant le message contenu dans this.before.
     */
    private void crypt() {
        int max = Math.max(this.before.length(), MAX_MSG_LENGTH);
        this.after = "";
        char c;
        for(int index = 0; index < max; index++) {
            if(this.alphaChar(this.before.charAt(index))) {
                c = (char) (this.before.charAt(index) + this.shift);
                if (!(c <= 'z' && c >= 'a') && !(c <= 'Z' && c >= 'A')) {
                    if (c < 'A') {
                        c = (char) ('Z' + this.shift);
                    } else if (c > 'Z' && c < 'a') {
                        if (this.shift > 0) {
                            c = (char) ('A' + this.shift);
                        } else {
                            c = (char) ('z' + this.shift);
                        }
                    } else {
                        c = (char) ('a' + this.shift);
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
     * @param c caractère à tester
     * @return true si dans un des intervalles.
     */
    private boolean alphaChar(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    /**
     * Méthode permettant de changer le message à coder.
     * @param uncoded message non codé.
     */
    public void setMessage(String uncoded) {
        this.before = uncoded;
        this.crypt();
    }

    /**
     * Méthode permettant de récupérer le message codé.
     * @return String : msg codé
     */
    public String getCodedMessage() {
        return this.after;
    }

}
