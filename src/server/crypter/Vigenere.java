package server.crypter;

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
        this.key = this.generateNewKey(key);
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
                    if (keyPosition == this.key.length()) keyPosition = 0;
                } else if (this.isLowerChar(this.before.charAt(i))) {
                    first = (int) this.key.toLowerCase().charAt(keyPosition) - 'a';
                    second = (int) this.before.charAt(i) - 'a';
                    this.after += (char) this.vigenereTabL[first][second];
                    keyPosition++;
                    if (keyPosition == this.key.length()) keyPosition = 0;
                } else {
                    this.after += this.before.charAt(i);
                }
            }
        }
    }

    /**
     * Méthode générant une clé adaptée au message, en la répétant autant de fois qu'il y a de caractères.
     *
     * @param oldKey la clé du client
     * @return nouvelle clé adaptée
     */
    public String generateNewKey(String oldKey) {
        String res = "";
        int keyPos = 0;
        for (int idx = 0; idx < this.before.length(); idx++) {
            if (this.isUpperChar(this.before.charAt(idx)) || this.isLowerChar(this.before.charAt(idx))) {
                res += oldKey.charAt(keyPos);
                keyPos++;
                if (keyPos == oldKey.length()) {
                    keyPos = 0;
                }
            }
        }
        return res;
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
}
