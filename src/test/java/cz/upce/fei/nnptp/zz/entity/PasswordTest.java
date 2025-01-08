package cz.upce.fei.nnptp.zz.entity;

import cz.upce.fei.nnptp.zz.entity.Parameter.DateTimeParameter;
import cz.upce.fei.nnptp.zz.entity.Parameter.PasswordParameter;
import cz.upce.fei.nnptp.zz.entity.Parameter.TextParameter;
import java.time.LocalDateTime;
import java.util.HashMap;
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
        assertEquals("textParamTest", retrievedParam.getValue(), "Parameter 'textKey' should return 'exampleText' as its value");
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

    @Test
    public void testAddParameter() {
        Password password = new Password(1, "testPass");
        TextParameter newParam = new TextParameter("newValue");

        password.addParameter("newKey", newParam);

        assertTrue(password.hasParameter("newKey"), "Newly added parameter 'newKey' should exist");
        assertEquals("newValue", ((TextParameter) password.getParameter("newKey")).getValue(), "Newly added parameter 'newKey' should return 'newValue'");
    }

    @Test
    public void testOverrideParameter() {
        HashMap<String, Parameter> parameters = new HashMap<>();
        TextParameter initialParam = new TextParameter("initialValue");
        parameters.put("key", initialParam);

        Password password = new Password(1, "testPass", parameters);
        TextParameter overriddenParam = new TextParameter("overriddenValue");

        password.addParameter("key", overriddenParam);

        assertTrue(password.hasParameter("key"), "Parameter 'key' should still exist after override");
        assertEquals("overriddenValue", ((TextParameter) password.getParameter("key")).getValue(), "Parameter 'key' should return the overridden value");
    }

    @Test
    public void testGetMultipleParametersValue() {
        HashMap<String, Parameter> parameters = new HashMap<>();

        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeParameter dateTimeParam = new DateTimeParameter(currentTime);
        parameters.put("dateKey", dateTimeParam);

        TextParameter textParam = new TextParameter("textParamTest");
        parameters.put("textKey", textParam);

        PasswordParameter pwdParam = new PasswordParameter("secret123");
        parameters.put("key", pwdParam);

        Password password = new Password(0, "mainPass", parameters);

        DateTimeParameter retrievedParamDate = (DateTimeParameter) password.getParameter("dateKey");
        assertEquals(currentTime, retrievedParamDate.getValue(), "Parameter 'dateKey' should return the correct LocalDateTime value");

        TextParameter retrievedParamText = (TextParameter) password.getParameter("textKey");
        assertEquals("textParamTest", retrievedParamText.getValue(), "Parameter 'textKey' should return 'exampleText' as its value");

        PasswordParameter retrievedParamPassword = (PasswordParameter) password.getParameter("key");
        assertEquals("secret123", retrievedParamPassword.getPassword(), "Parameter 'key' should return 'secret123' as its value");
    }

}
