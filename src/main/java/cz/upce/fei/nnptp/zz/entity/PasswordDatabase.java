package cz.upce.fei.nnptp.zz.entity;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Roman
 */
public class PasswordDatabase {
    private File file;
    private String password;

    private List<Password> passwords;

    public PasswordDatabase(File file, String password) {
        this.file = file;
        this.password = password;

        this.passwords = new LinkedList<>();
    }

    public List<Password> getPasswords() {
        return new LinkedList<>(passwords);
    }

    public void load() throws PasswordDatabaseException {
        String encryptedJson = CryptoFile.readFile(file, password);

        if (isInvalidEncryptedData(encryptedJson)) {
            throw new PasswordDatabaseException("Failed to load data: file content is empty or cannot be decrypted.");
        }

        passwords = new JsonConverter().fromJson(encryptedJson);
    }

    private boolean isInvalidEncryptedData(String encryptedJson) {
        return encryptedJson == null || encryptedJson.isEmpty();
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
