package cz.upce.fei.nnptp.zz.entity;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Roman
 */
public class PasswordDatabase {
    private File file;
    private String password;
    
    private List<Password> passwords;

    public PasswordDatabase(File file, String password) {
        this.file = file;
        this.password = password;

        this.passwords = new ArrayList<>();
    }

    public List<Password> getPasswords() {
        return new ArrayList<>(passwords);
    }
    
    public void load() throws PasswordDatabaseException {
        String encryptedJson = CryptoFile.readFile(file, password);
        
        if (encryptedJson == null || encryptedJson.isEmpty()) {
            throw new PasswordDatabaseException("Failed to load data: file content is empty or cannot be decrypted.");
        }
        
        passwords = new JsonConverter().fromJson(encryptedJson);
    }
    
    public void save() {
        for (Password p : passwords) {
            for (Parameter param : p.parameters().values()) {
                param.validate();
            }
        }
        String contents = new JsonConverter().toJson(passwords);
        CryptoFile.writeFile(file, password, contents);
    }
    
    public void add(Password password) {
        passwords.add(password);
    }
    
    public Password findEntryByTitle(String title) {
        return passwords.stream()
                .filter(password -> password.hasParameter(Parameter.StandardizedParameters.TITLE))
                .filter(password -> {
                    Parameter.TextParameter titleParam =
                            (Parameter.TextParameter) password.getParameter(Parameter.StandardizedParameters.TITLE);
                    return title.equals(titleParam.getValue());
                })
                .findFirst()
                .orElse(null);
    }
    
}
