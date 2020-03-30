package server.crypter;


import java.io.IOException;

public class Vigenere implements ICryptage {
    private String before;
    private String after;
    private String key;
    private int[][] vigenereTabU;
    public int[][] vigenereTabL;

    /**
     * Constructeur Cryptage().
     *
     * @param messageUncoded message non codé.
     * @param key            clé de cryptage.
     */
    public Vigenere(String messageUncoded, String key) {
        this.before = messageUncoded;
        this.key = key;
        this.vigenereTabU = this.initialiseVigenereTab((int) 'A', (int) 'Z');
        this.vigenereTabL = this.initialiseVigenereTab((int) 'a', (int) 'z');
        this.crypt();
    }

    @Override
    public String getCodedMessage() {
        this.crypt();
        return this.after;
    }

    @Override
    public void crypt() {
        this.after = "";
        if (this.key.isEmpty()) {
            this.after = this.before;
        } else {
            int keyPosition = 0, first, second;
            for (int i = 0; i < this.before.length(); i++) {
                if (this.isUpperChar(this.before.charAt(i))) {
                    first = (int) this.key.toUpperCase().charAt(keyPosition) - 'A';
                    second = (int) this.before.charAt(i) - 'A';
                    this.after += (char) this.vigenereTabU[first][second];
                    keyPosition++;
                    if (keyPosition > this.key.length()) keyPosition = 0;
                } else if (this.isLowerChar(this.before.charAt(i))) {
                    first = (int) this.key.toLowerCase().charAt(keyPosition) - 'a';
                    second = (int) this.before.charAt(i) - 'a';
                    this.after += (char) this.vigenereTabL[first][second];
                    keyPosition++;
                    if (keyPosition > this.key.length()) keyPosition = 0;
                } else {
                    this.after += this.before.charAt(i);
                }
                System.out.print(this.after.charAt(this.after.length()-1));
            }
        }
    }

    /**
     * Méthode privée permettant de savoir si le caractère une lettre majuscule.
     *
     * @param c caractère à tester
     * @return boolean
     */
    private boolean isUpperChar(char c) {
        return (c >= 'A' && c <= 'Z');
    }

    /**
     * Méthode privée permettant de savoir si le caractère une lettre minuscule.
     *
     * @param c caractère à tester
     * @return boolean
     */
    private boolean isLowerChar(char c) {
        return (c >= 'a' && c <= 'z');
    }

    /**
     * Méthode permettant d'intialiser un tableau de Vigénère entre les begin et end.
     *
     * @param begin A ou a
     * @param end   Z ou z
     * @return int[][] tableau de vigénère
     */
    private int[][] initialiseVigenereTab(int begin, int end) {
        int[][] tab = new int[end - begin + 1][end - begin + 1];
        int position = begin;
        for (int i = 0; i < tab.length; i++) {
            for (int j = 0; j < tab[0].length; j++) {
                tab[i][j] = position;
                position++;
                if (position > end) {
                    position = begin;
                }
            }
            position = begin + i + 1;
        }
        return tab;
    }

    public static void main(String args[]) throws IOException {
        Vigenere v = new Vigenere("BoNjOuR MeC", "lacle");
        System.out.print(v.getCodedMessage());
    }
}
