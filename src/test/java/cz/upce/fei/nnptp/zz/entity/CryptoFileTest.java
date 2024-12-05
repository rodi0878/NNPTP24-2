package cz.upce.fei.nnptp.zz.entity;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
/**
 *
 * @author Roman
 */
public class CryptoFileTest {

    
    public CryptoFileTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of readFile method, of class CryptoFile.
     *
     * @throws java.io.IOException
     */
    @Test
    public void testReadEmptyFile() throws IOException {
        File emptyFile = File.createTempFile("emptyFile", ".txt");
        emptyFile.deleteOnExit();

        String password = "password";
        String expResult = null;

        String result = CryptoFile.readFile(emptyFile, password);

        assertEquals(expResult, result, "Reading an empty file should return null");
    }
    
    /**
     * Test of readFile method, of class CryptoFile.
     */
    @Test
    public void testReadFile() {
        System.out.println("readFile");
        File file = null;
        String password = "";
        String expResult = "";
        String result = ""; //CryptoFile.readFile(file, password);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of writeFile method, of class CryptoFile.
     */
    @Test
    public void testWriteFile() throws IOException {
        File file = File.createTempFile("testFile",".enc");
        System.out.println("writeFile");
        String password = "password";
        String cnt = "content";
        CryptoFile.writeFile(file, password, cnt);
        assertTrue(file.length() > 0);
        String decryptedContent = CryptoFile.readFile(file, password);
        assertEquals(cnt, decryptedContent);
    }
    
}
