package test.client;

import client.util.CrypteursAvailable;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CrypteursAvailableTest {

    @Test
    public void toStringTestMultiple() {
        CrypteursAvailable ca = new CrypteursAvailable("[CESAR, VIGENERE]");
        assertEquals("0. CESAR\n1. VIGENERE\n", ca.toString());
    }

    @Test
    public void toStringTestAlone() {
        CrypteursAvailable ca = new CrypteursAvailable("[CESAR]");
        assertEquals("0. CESAR\n", ca.toString());
    }

    @Test
    public void toStringTestEmpty() {
        CrypteursAvailable ca = new CrypteursAvailable("[]");
        assertEquals("", ca.toString());
    }

}
