package cz.upce.fei.nnptp.zz.entity;

import cz.upce.fei.nnptp.zz.entity.Parameter.DateTimeParameter;
import cz.upce.fei.nnptp.zz.entity.Parameter.PasswordParameter;
import cz.upce.fei.nnptp.zz.entity.Parameter.TextParameter;
import java.time.LocalDateTime;
import java.util.HashMap;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Roman
 */
public class PasswordTest {

    @Test
    public void testPasswordValue() {
        Password password = new Password(0, "pass");
        assertEquals("pass", password.password(), "Password should match 'pass'");
    }

    @Test
    public void testHasParameter() {
        HashMap<String, Parameter> parameters = new HashMap<>();
        PasswordParameter pwdParam = new PasswordParameter("secret123");
        parameters.put("key", pwdParam);

        Password password = new Password(0, "pass", parameters);

        assertTrue(password.hasParameter("key"), "Parameter 'key' should exist");
        assertFalse(password.hasParameter("nonexistentKey"), "Parameter 'nonexistentKey' should not exist");
    }

    @Test
    public void testGetPasswordParameterValue() {
        HashMap<String, Parameter> parameters = new HashMap<>();
        PasswordParameter pwdParam = new PasswordParameter("secret123");
        parameters.put("key", pwdParam);

        Password password = new Password(0, "pass", parameters);

        PasswordParameter retrievedParam = (PasswordParameter) password.getParameter("key");
        assertEquals("secret123", retrievedParam.getPassword(), "Parameter 'key' should return 'secret123' as its value");
    }

    @Test
    public void testGetTextParameterValue() {
        HashMap<String, Parameter> parameters = new HashMap<>();
        TextParameter textParam = new TextParameter("textParamTest");
        parameters.put("textKey", textParam);

        Password password = new Password(0, "mainPass", parameters);

        TextParameter retrievedParam = (TextParameter) password.getParameter("textKey");
        assertEquals("textParamTest", retrievedParam.getValue(), "Parameter 'textKey' should return 'textParamTest' as its value");
    }

    @Test
    public void testGetDateTimeParameterValue() {
        HashMap<String, Parameter> parameters = new HashMap<>();
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeParameter dateTimeParam = new DateTimeParameter(currentTime);
        parameters.put("dateKey", dateTimeParam);

        Password password = new Password(0, "mainPass", parameters);

        DateTimeParameter retrievedParam = (DateTimeParameter) password.getParameter("dateKey");
        assertEquals(currentTime, retrievedParam.getValue(), "Parameter 'dateKey' should return the correct LocalDateTime value");
    }
}
