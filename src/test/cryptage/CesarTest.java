package test.cryptage;

import org.junit.Test;
import server.crypter.Cesar;

import static org.junit.Assert.assertEquals;

public class CesarTest {

    @Test
    public void testNotAlphaChar() {
        Cesar crypt = new Cesar(" .;?,/:§!0123456789", 1);
        assertEquals(" .;?,/:§!0123456789", crypt.getCodedMessage());
    }

    @Test
    public void testSujet1() {
        Cesar crypt = new Cesar("Bonjour le monde !", 6);
        assertEquals("Hutpuax rk sutjk !", crypt.getCodedMessage());
    }

    @Test
    public void testSujet2() {
        Cesar crypt = new Cesar("j'ai un secret à te dire", -3);
        assertEquals("g'xf rk pbzobq à qb afob", crypt.getCodedMessage());
    }
}
