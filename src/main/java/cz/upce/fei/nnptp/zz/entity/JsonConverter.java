/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.upce.fei.nnptp.zz.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
/**
 *
 * @author Roman
 */
public class JsonConverter {
    
    
    public String toJson(List<Password> passwords)  {
        String output = "[";
        for (Password password : passwords) {
            if (!output.isEmpty() && !output.equals("["))
                output += ",";
            output += "{";
            output += "\"id\":" + password.id() + ",";
            output += "\"password\":\"" + password.password()+"\"";
            HashMap<String, Parameter> parameters = password.parameters();
            if (parameters != null && !parameters.isEmpty()) {
                output += ",\"parameters\":{";
                for (String key : parameters.keySet()) {
                    Parameter parameter = parameters.get(key);
                    output += "\"" + key + "\":" + parameter.toJson() + ",";
                }
                output = output.substring(0, output.length() - 1);
                output += "}";
            }
            output += "}";
        }
        output += "]";
        
        return output;
    }

    public List<Password> fromJson(String json) {
        if (json == null || json.isEmpty()) return new ArrayList<>();

        List<Password> passwords = new ArrayList<>();
        int i = 0;

        while (i < json.length()) {
            if (json.charAt(i) == '{') {
                Password password = parsePassword(json, i);
                passwords.add(password);
                // Move index past '}'
                while (i < json.length() && json.charAt(i) != '}') {
                    i++;
                }
            }
            i++; // Move past '}'
        }

        return passwords;
    }

    private Password parsePassword(String json, int startIndex) {
        int i = startIndex + 1; // Move past '{'
        int id = 0;
        String passwordValue = "";
        HashMap<String, Parameter> parameters = new HashMap<>();

        while (i < json.length() && json.charAt(i) != '}') {
            int keyStart = json.indexOf("\"", i) + 1;
            int keyEnd = json.indexOf("\"", keyStart);
            String key = json.substring(keyStart, keyEnd);
            i = keyEnd + 2; // Move past key and ':'

            switch (key) {
                case "id":
                    int idEnd = json.indexOf(",", i);
                    id = Integer.parseInt(json.substring(i, idEnd).trim());
                    i = idEnd + 1; // Move past id value and ','
                    break;

                case "password":
                    int passwordStart = json.indexOf("\"", i) + 1;
                    int passwordEnd = json.indexOf("\"", passwordStart);
                    passwordValue = json.substring(passwordStart, passwordEnd);
                    i = passwordEnd + 1; // Move past password value and ','
                    break;

                case "parameters":
                    i++; // Move past '{'
                    parameters = parseParameters(json, i);
                    // Move i to '}'
                    while (i < json.length() && json.charAt(i) != '}') {
                        i++;
                    }
                    i++; // Move past '}'
                    break;
            }
            if (i < json.length() && json.charAt(i) == ',') i++;
        }
        return new Password(id, passwordValue, parameters);
    }

    private HashMap<String, Parameter> parseParameters(String json, int startIndex) {
        HashMap<String, Parameter> parameters = new HashMap<>();
        int i = startIndex;

        while (i < json.length() && json.charAt(i) != '}') {
            int paramKeyStart = json.indexOf("\"", i) + 1;
            int paramKeyEnd = json.indexOf("\"", paramKeyStart);
            String paramKey = json.substring(paramKeyStart, paramKeyEnd);
            i = paramKeyEnd + 2; // Move past paramKey and ':'

            int paramValueStart = json.indexOf("\"", i) + 1;
            int paramValueEnd = json.indexOf("\"", paramValueStart);
            String paramValue = json.substring(paramValueStart, paramValueEnd);

            if (paramKey.equals(Parameter.StandardizedParameters.EXPIRATION_DATETIME)) {
                parameters.put(paramKey, new Parameter.DateTimeParameter(LocalDateTime.parse(paramValue)));
            } else {
                parameters.put(paramKey, new Parameter.TextParameter(paramValue));
            }

            i = paramValueEnd + 1; // Move past parameters value and ','

            if (i < json.length() && json.charAt(i) == ',') i++;
        }
        return parameters;
    }
}
