package cz.upce.fei.nnptp.zz.entity;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Roman
 */
public class PasswordDatabase {
    private final File file;
    private final String password;
    
    private List<Password> passwords;

    public PasswordDatabase(File file, String password) {
        this.file = file;
        this.password = password;

        this.passwords = new LinkedList<>();
    }

    public List<Password> getPasswords() {
        return passwords;
    }
    
    public void load() throws PasswordDatabaseException {
        String encryptedJson = CryptoFile.readFile(file, password);
        
        if (encryptedJson == null || encryptedJson.isEmpty()) {
            throw new PasswordDatabaseException("Failed to load data: file content is empty or cannot be decrypted.");
        }
        
        passwords = new JsonConverter().fromJson(encryptedJson);
    }
    
    public void save() {
        String contents = new JsonConverter().toJson(passwords);
        CryptoFile.writeFile(file, password, contents);
    }
    
    public void add(Password password) {
        passwords.add(password);
    }
    
    public Password findEntryByTitle(String title) {
        for (Password password : passwords) {
            
            if (password.hasParameter(Parameter.StandardizedParameters.TITLE)) {
                Parameter.TextParameter titleParam;
                titleParam = (Parameter.TextParameter)password.getParameter(Parameter.StandardizedParameters.TITLE);
                if (titleParam.getValue().equals(title)) {
                    return password;
                }
            }
        }
        return null;
    }
    
}
