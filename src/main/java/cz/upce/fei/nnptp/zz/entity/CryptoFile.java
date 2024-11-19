package cz.upce.fei.nnptp.zz.entity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Roman
 */
public class CryptoFile {
    private static final int DES_KEY_LENGTH = 8;
    private static Cipher initializeCipher(String password, int mode) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        byte[] keyBytes = Arrays.copyOf(password.getBytes(StandardCharsets.UTF_8), DES_KEY_LENGTH);
        SecretKey secretKey = new SecretKeySpec(keyBytes, "DES");
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(mode, secretKey);
        return cipher;
    }
    
    public static String readFile(File file, String password) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            Cipher c = initializeCipher(password, Cipher.DECRYPT_MODE);
            CipherInputStream cis = new CipherInputStream(fis, c);
            
            DataInputStream dis = new DataInputStream(cis);
            String r = dis.readUTF();
            dis.close();
            c.doFinal();
            
            return r;        
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | IOException | InvalidKeyException |
                 IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(CryptoFile.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fis.close();
            } catch (IOException ex) {
                Logger.getLogger(CryptoFile.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return null;
    }
    
    public static void writeFile(File file, String password, String cnt) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            Cipher c = initializeCipher(password, Cipher.ENCRYPT_MODE);
            CipherOutputStream cis = new CipherOutputStream(fos, c);

            DataOutputStream dos = new DataOutputStream(cis);
            dos.writeUTF(cnt);
            dos.close();
            c.doFinal();
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IOException |
                 IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(CryptoFile.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fos.close();
            } catch (IOException ex) {
                Logger.getLogger(CryptoFile.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
