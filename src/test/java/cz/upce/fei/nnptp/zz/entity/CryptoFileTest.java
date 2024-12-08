package cz.upce.fei.nnptp.zz.entity;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Roman
 */

public class CryptoFileTest {

    private File testFile;
    private final String password = "securePassword";
    private final String content = "Testovací obsah";

    @BeforeEach
    public void setUp() throws IOException {
        // Vytvoření dočasného souboru pro testování
        testFile = File.createTempFile("cryptoFileTest", ".enc");
    }

    @AfterEach
    public void tearDown() {
        // Odstranění dočasného souboru
        if (testFile.exists()) {
            testFile.delete();
        }
    }

    /**
     * Testuje šifrování a dešifrování obsahu souboru.
     */
    @Test
    public void testWriteAndReadFile() {
        // Zápis do souboru
        CryptoFile.writeFile(testFile, password, content);
        assertTrue(testFile.length() > 0, "Soubor by měl obsahovat data po šifrování");

        // Čtení a dešifrování obsahu
        String decryptedContent = CryptoFile.readFile(testFile, password);
        assertEquals(content, decryptedContent, "Dešifrovaný obsah by měl odpovídat původnímu");
    }

    /**
     * Testuje, že čtení souboru se špatným heslem způsobí chybu.
     */
    @Test
    public void testReadFileWithWrongPassword() {
        // Zápis do souboru
        CryptoFile.writeFile(testFile, password, content);

        // Pokus o čtení s nesprávným heslem
        String wrongPassword = "wrongPassword";
        String result = CryptoFile.readFile(testFile, wrongPassword);
        assertNull(result, "Čtení souboru se špatným heslem by mělo vrátit null");
    }

    /**
     * Testuje chování při pokusu o čtení neexistujícího souboru.
     */
    @Test
    public void testReadNonExistentFile() {
        // Pokus o čtení neexistujícího souboru
        File nonExistentFile = new File("nonexistent.enc");
        String result = CryptoFile.readFile(nonExistentFile, password);
        assertNull(result, "Čtení neexistujícího souboru by mělo vrátit null");
    }

    /**
     * Testuje chování při pokusu o zápis do neplatného souboru.
     */
    @Test
    public void testWriteToInvalidFile() {
        // Vytvoření souboru s neplatnou cestou
        File invalidFile = new File("/invalid/path/test.enc");

        // Pokus o zápis by měl selhat, ale neměl by způsobit výjimku
        assertDoesNotThrow(() -> CryptoFile.writeFile(invalidFile, password, content),
                "Zápis do neplatného souboru by neměl způsobit výjimku");
        assertFalse(invalidFile.exists(), "Neplatný soubor by neměl být vytvořen");
    }
}
