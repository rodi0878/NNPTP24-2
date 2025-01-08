package cz.upce.fei.nnptp.zz.entity;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PasswordDatabaseTest {

    private File temporaryDirectory;

    @BeforeEach
    void setUp() {
        temporaryDirectory = new File(System.getProperty("java.io.tmpdir", "./"), "NNPTP24-2");
        temporaryDirectory.mkdirs();
        assertTrue(temporaryDirectory.exists(), "Temporary directory was not created");
    }

    @AfterEach
    void tearDown() throws Exception {
        FileUtils.deleteDirectory(temporaryDirectory);
    }

    @Test
    void testSaveEmpty() throws Exception {
        File testFile = new File(temporaryDirectory, "testEmpty.txt");

        PasswordDatabase passwordDatabase = new PasswordDatabase(testFile, "password");
        passwordDatabase.save();

        assertTrue(testFile.exists(), "DB file was not created");

        URL resource = PasswordDatabaseTest.class.getResource("testEmpty.txt");
        assertEquals(-1, Files.mismatch(testFile.toPath(), Paths.get(resource.toURI())), "Generated file content does not match the expected output");
    }

    @Test
    void testSaveFilled() throws Exception {
        File testFile = new File(temporaryDirectory, "testFilled.txt");

        PasswordDatabase passwordDatabase = new PasswordDatabase(testFile, "password");
        passwordDatabase.add(new Password(0, "pwd1"));
        passwordDatabase.add(new Password(1, "pwd2"));
        passwordDatabase.save();

        assertTrue(testFile.exists(), "DB file was not created");

        URL resource = PasswordDatabaseTest.class.getResource("testFilled.txt");
        assertEquals(-1, Files.mismatch(testFile.toPath(), Paths.get(resource.toURI())), "Generated file content does not match the expected output");
    }

    @Test
    void testSaveNullFile() {
        assertDoesNotThrow(() -> new PasswordDatabase(null, "password"));
    }

    @Test
    void testSaveNullPasswd() {
        File testFile = new File(temporaryDirectory, "test.txt");
        assertDoesNotThrow(() -> new PasswordDatabase(testFile, null));
        assertFalse(testFile.exists(), "DB file was created");
    }

    @Test
    void testLoadValidData() throws Exception {
        File testFile = new File(temporaryDirectory, "testLoadValid.txt");

        PasswordDatabase passwordDatabase = new PasswordDatabase(testFile, "password");
        List<Password> testPasswords = List.of(
                new Password(0, "pwd1"),
                new Password(1, "pwd2")
        );
        CryptoFile.writeFile(testFile, "password", new JsonConverter().toJson(testPasswords));

        passwordDatabase.load();
        assertEquals(2, passwordDatabase.getPasswords().size(), "Loaded passwords size does not match");
        assertEquals("pwd1", passwordDatabase.getPasswords().get(0).password(), "First password does not match expected value");
        assertEquals("pwd2", passwordDatabase.getPasswords().get(1).password(), "Second password does not match expected value");
    }

    @Test
    void testLoadEmptyFile() {
        File testFile = new File(temporaryDirectory, "testLoadEmpty.txt");

        CryptoFile.writeFile(testFile, "password", "");

        PasswordDatabase passwordDatabase = new PasswordDatabase(testFile, "password");

        PasswordDatabaseException exception = assertThrows(PasswordDatabaseException.class, passwordDatabase::load);
        assertEquals("Failed to load data: file content is empty or cannot be decrypted.", exception.getMessage(), "Exception message does not match");
    }

    @Test
    void testFindEntryByTitleFound() {
        File testFile = new File(temporaryDirectory, "findEntryByTitleFound.txt");

        PasswordDatabase passwordDatabase = new PasswordDatabase(testFile, "password");

        HashMap<String, Parameter> parameters = new HashMap<>();
        parameters.put(Parameter.StandardizedParameters.TITLE, new Parameter.TextParameter("test_value"));

        Password password = new Password(1, "testPassword", parameters);
        passwordDatabase.add(password);

        Password result = passwordDatabase.findEntryByTitle("test_value");

        assertNotNull(result, "Password should be found for the given title");
        assertEquals(password, result, "The found password should match the added password");
    }

    @Test
    void testFindEntryByTitleNotFound() {
        File testFile = new File(temporaryDirectory, "findEntryByTitleNotFound.txt");

        PasswordDatabase passwordDatabase = new PasswordDatabase(testFile, "password");

        HashMap<String, Parameter> parameters = new HashMap<>();
        parameters.put(Parameter.StandardizedParameters.TITLE, new Parameter.TextParameter("test_value"));
        Password password = new Password(1, "testPassword", parameters);
        passwordDatabase.add(password);

        Password result = passwordDatabase.findEntryByTitle("someValue");

        assertNull(result, "Password should not be found for a non-existent title");
    }
    @Test
    void testPasswordCollectionBehavior() {
        PasswordDatabase passwordDatabase = new PasswordDatabase(new File("dummyFile"), "dummyPassword");

        assertTrue(passwordDatabase.getPasswords().isEmpty(), "Password list should be initially empty");

        Password password1 = new Password(1, "testPassword1");
        passwordDatabase.add(password1);
        assertEquals(1, passwordDatabase.getPasswords().size(), "Password list size should be 1 after adding an element");
        assertEquals(password1, passwordDatabase.getPasswords().get(0), "First password should match the added password");

        Password password2 = new Password(2, "testPassword2");
        passwordDatabase.add(password2);
        assertEquals(2, passwordDatabase.getPasswords().size(), "Password list size should be 2 after adding another element");
        assertEquals(password2, passwordDatabase.getPasswords().get(1), "Second password should match the added password");

        List<Password> copiedPasswords = passwordDatabase.getPasswords();
        copiedPasswords.remove(0);
        assertEquals(2, passwordDatabase.getPasswords().size(), "Removing from the returned list should not affect the original list");

        Password modifiedPassword = new Password(password1.id(), "modifiedPassword", password1.parameters());
        passwordDatabase.add(modifiedPassword);

        assertEquals(3, passwordDatabase.getPasswords().size(), "Password list size should be 3 after adding the modified password");
        assertEquals("modifiedPassword", passwordDatabase.getPasswords().get(2).password(), "Modified password should be correctly added");
    }
}
