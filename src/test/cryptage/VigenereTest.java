package test.cryptage;

import org.junit.Test;
import server.crypter.Vigenere;

import static org.junit.Assert.assertEquals;

public class VigenereTest {

    @Test
    public void testNotAlphaChar() {
        Vigenere crypt = new Vigenere(" .;?,/:§!0123456789", "lacle");
        assertEquals(" .;?,/:§!0123456789", crypt.getCodedMessage());
    }

    @Test
    public void testSujet1() {
        Vigenere crypt = new Vigenere("Bonjour le monde !", "lacle");
        assertEquals("Mopusfr np qznfp !", crypt.getCodedMessage());
    }

    @Test
    public void testSujet2() {
        Vigenere crypt = new Vigenere("j'ai un secret à te dire", "lacle");
        assertEquals("u'ak fr deecie à tg omce", crypt.getCodedMessage());
    }
}
