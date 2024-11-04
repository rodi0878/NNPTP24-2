package cz.upce.fei.nnptp.zz.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class JSONTest {

    @Test
    public void testToJsonEmptyPasswordList() {
        JsonConverter json = new JsonConverter();
        List<Password> passwords = new ArrayList<>();
        String result = json.toJson(passwords);
        assertEquals("[]", result, "Expected empty JSON array for empty password list.");
    }
    @Test
    public void testToJsonSinglePasswordNoParameters() {
        JsonConverter json = new JsonConverter();
        List<Password> passwords = new ArrayList<>();
        passwords.add(new Password(1, "password123"));
        String result = json.toJson(passwords);
        assertEquals("[{id:1,password:\"password123\"}]", result, "Expected JSON for a single password without parameters.");
    }
    @Test
    public void testToJsonMultiplePasswordsWithParameters() {
        JsonConverter json = new JsonConverter();
        HashMap<String, Parameter> parameters1 = new HashMap<>();
        parameters1.put(Parameter.StandardizedParameters.TITLE, new Parameter.TextParameter("Title"));
        LinkedHashMap<String, Parameter> parameters2 = new LinkedHashMap<>();
        parameters2.put(Parameter.StandardizedParameters.EXPIRATION_DATETIME, new Parameter.DateTimeParameter(LocalDateTime.of(2024, 11, 10, 23, 50)));
        parameters2.put(Parameter.StandardizedParameters.WEBSITE, new Parameter.TextParameter("https://www.upce.cz/"));

        List<Password> passwords = new ArrayList<>();
        passwords.add(new Password(1, "password123", parameters1));
        passwords.add(new Password(2, "password456", parameters2));

        String result = json.toJson(passwords);
        assertEquals("[{id:1,password:\"password123\",parameters:{title:\"Title\"}},"
                        + "{id:2,password:\"password456\",parameters:{expiration-datetime:\"2024-11-10T23:50\",website:\"https://www.upce.cz/\"}}]",
                result, "Expected JSON for multiple passwords with parameters.");
    }
    @Test
    public void testFromJsonEmptyString() {
        JsonConverter json = new JsonConverter();
        String input = "";
        List<Password> result = json.fromJson(input);

        assertTrue(result.isEmpty());
    }
    @Test
    public void testFromJsonNullString() {
        JsonConverter json = new JsonConverter();
        String input = null;
        List<Password> result = json.fromJson(input);

        assertTrue(result.isEmpty());
    }
    @Test
    public void testFromJsonEmptyJson() {
        JsonConverter json = new JsonConverter();
        String input = "[]";
        List<Password> result = json.fromJson(input);

        assertTrue(result.isEmpty());
    }
    @Test
    public void testFromJsonSinglePasswordNoParameters() {
        JsonConverter json = new JsonConverter();
        String input = "[{\"id\":1,\"password\":\"password123\"}]";
        List<Password> result = json.fromJson(input);

        Password expectedPassword = new Password(1, "password123");
        Password actualPassword = result.get(0);

        assertEquals(1, result.size());
        assertEquals(expectedPassword.password(), actualPassword.password());
        assertTrue(actualPassword.parameters().isEmpty());
    }
    @Test
    public void testFromJsonSinglePasswordWithParameters() {
        JsonConverter json = new JsonConverter();
        String input = "{\"id\":1,\"password\":\"password123\",\"parameters\":{\"title\":\"title1\",\"expiration-datetime\":\"2024-10-26T15:50\"}}";
        List<Password> result = json.fromJson(input);

        String expectedTitleValue = "title1";
        LocalDateTime expectedDateTime = LocalDateTime.of(2024, 10, 26, 15, 50);

        HashMap<String, Parameter> params = new HashMap<>();
        params.put(Parameter.StandardizedParameters.TITLE, new Parameter.TextParameter(expectedTitleValue));
        params.put(Parameter.StandardizedParameters.EXPIRATION_DATETIME, new Parameter.DateTimeParameter(expectedDateTime));

        Password expectedPassword = new Password(1, "password123", params);
        Password actualPassword = result.get(0);

        assertEquals(1, result.size());
        assertEquals(expectedPassword.password(), actualPassword.password());

        assertTrue(actualPassword.hasParameter(Parameter.StandardizedParameters.TITLE));
        assertTrue(actualPassword.hasParameter(Parameter.StandardizedParameters.EXPIRATION_DATETIME));

        Parameter actualTitleParam = actualPassword.getParameter(Parameter.StandardizedParameters.TITLE);
        assertInstanceOf(Parameter.TextParameter.class, actualTitleParam);
        assertEquals(expectedTitleValue, ((Parameter.TextParameter) actualTitleParam).getValue());

        Parameter actualExpirationParam = actualPassword.getParameter(Parameter.StandardizedParameters.EXPIRATION_DATETIME);
        assertInstanceOf(Parameter.DateTimeParameter.class, actualExpirationParam);
        assertEquals(expectedDateTime, ((Parameter.DateTimeParameter) actualExpirationParam).getValue());
    }
    @Test
    public void testFromJsonMultiplePasswordsWithParameters() {
        JsonConverter json = new JsonConverter();

        String input = "["
                + "{\"id\":1,\"password\":\"password123\",\"parameters\":{\"title\":\"title1\",\"expiration-datetime\":\"2024-10-26T15:50\"}},"
                + "{\"id\":2,\"password\":\"password456\",\"parameters\":{\"description\":\"desc2\"}}"
                + "]";

        List<Password> result = json.fromJson(input);

        String expectedTitleValue1 = "title1";
        LocalDateTime expectedDateTime1 = LocalDateTime.of(2024, 10, 26, 15, 50);

        String expectedDescription2 = "desc2";

        HashMap<String, Parameter> params1 = new HashMap<>();
        params1.put(Parameter.StandardizedParameters.TITLE, new Parameter.TextParameter(expectedTitleValue1));
        params1.put(Parameter.StandardizedParameters.EXPIRATION_DATETIME, new Parameter.DateTimeParameter(expectedDateTime1));

        Password expectedPassword1 = new Password(1, "password123", params1);

        HashMap<String, Parameter> params2 = new HashMap<>();
        params2.put(Parameter.StandardizedParameters.DESCRIPTION, new Parameter.TextParameter(expectedDescription2));

        Password expectedPassword2 = new Password(2, "password456", params2);

        assertEquals(2, result.size());

        Password actualPassword1 = result.get(0);
        assertEquals(expectedPassword1.password(), actualPassword1.password());
        assertTrue(actualPassword1.hasParameter(Parameter.StandardizedParameters.TITLE));
        assertTrue(actualPassword1.hasParameter(Parameter.StandardizedParameters.EXPIRATION_DATETIME));

        Parameter actualTitleParam1 = actualPassword1.getParameter(Parameter.StandardizedParameters.TITLE);
        assertInstanceOf(Parameter.TextParameter.class, actualTitleParam1);
        assertEquals(expectedTitleValue1, ((Parameter.TextParameter) actualTitleParam1).getValue());

        Parameter actualExpirationParam1 = actualPassword1.getParameter(Parameter.StandardizedParameters.EXPIRATION_DATETIME);
        assertInstanceOf(Parameter.DateTimeParameter.class, actualExpirationParam1);
        assertEquals(expectedDateTime1, ((Parameter.DateTimeParameter) actualExpirationParam1).getValue());

        Password actualPassword2 = result.get(1);
        assertEquals(expectedPassword2.password(), actualPassword2.password());
        assertTrue(actualPassword2.hasParameter(Parameter.StandardizedParameters.DESCRIPTION));

        Parameter actualDescriptionParam2 = actualPassword2.getParameter(Parameter.StandardizedParameters.DESCRIPTION);
        assertInstanceOf(Parameter.TextParameter.class, actualDescriptionParam2);
        assertEquals(expectedDescription2, ((Parameter.TextParameter) actualDescriptionParam2).getValue());
    }
}