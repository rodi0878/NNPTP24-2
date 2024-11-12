/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.upce.fei.nnptp.zz.entity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
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
    
    private static Cipher initializeCipher(String password, int mode) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        SecretKey secretKey = new SecretKeySpec(password.getBytes(), "DES");
        cipher.init(mode, secretKey);
        return cipher;
    }
    
    public static String readFile(File file, String password) {
        FileInputStream fis = null;
        String result = null;
        
        try {
            fis = new FileInputStream(file);
            Cipher cipher  = initializeCipher(password, Cipher.DECRYPT_MODE);
            CipherInputStream cis = new CipherInputStream(fis, c);
            
            try (DataInputStream dis = new DataInputStream(cis)) {
                result = dis.readUTF();
            } catch (IOException ex) {
                Logger.getLogger(CryptoFile.class.getName()).log(Level.SEVERE, null, ex);
            }
            cipher.doFinal();
            
            return result;        
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
        
        return result;
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
