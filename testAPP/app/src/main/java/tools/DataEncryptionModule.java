package tools;
import javax.crypto.*;
import android.util.Log;
import java.security.*;

public class DataEncryptionModule {
//    public static void main(String[] args) {
//
//    }
    /**
     * Encrypt the bytes using given password.
     * @param input the bytes that are intended to be encrypted
     * @param pwd the password
     * @return
     */
    public byte[] cryptEncrypt(byte[] input, String pwd) {
        Cipher cipher = cryptGetCipher(pwd, false);
        byte[] result = null;
        try {
            result = cipher.doFinal(input);
        } catch (IllegalBlockSizeException ex) {
            printErr(ex);
        } catch (BadPaddingException ex) {
            printErr(ex);
        }
        return result;
    }

    /**
     * Decrypt the bytes using given password.
     * @param cipherText bytes that are intended to be decrypted
     * @param pwd the password
     * @return
     */
    public byte[] cryptDecrypt(byte[] cipherText, String pwd) {
        Cipher cipher = cryptGetCipher(pwd, true);
        byte[] result = null;
        try {
            result = cipher.doFinal(cipherText);
        } catch (IllegalBlockSizeException ex) {
            printErr(ex);
        } catch (BadPaddingException ex) {
            printErr(ex);
        }
        return result;
    }

    /**
     * Common part for encryption and decryption.
     * @param pwd the password
     * @param isDecryption <tt>true</tt> if this method is for decryption and <tt>false</tt> if this is for encryption
     * @return
     */
    private Cipher cryptGetCipher(String pwd, boolean isDecryption) {
        //--- Get the hash algorithm
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            printErr(ex);
        }
        //--- Hash the pwd to make a 128bit key
        byte[] key = md5.digest(pwd.getBytes());
        //--- Create a key suitable for AES
        javax.crypto.spec.SecretKeySpec skey = new javax.crypto.spec.SecretKeySpec(key, "AES");
        javax.crypto.spec.IvParameterSpec ivSpec = new javax.crypto.spec.IvParameterSpec(md5.digest(key));

        Cipher cipher = null;
        try {
            cipher = javax.crypto.Cipher.getInstance("AES/CTR/NoPadding", "SunJCE");
        } catch (NoSuchAlgorithmException ex) {
            printErr(ex);
        } catch (NoSuchProviderException ex) {
            printErr(ex);
        } catch (NoSuchPaddingException ex) {
            printErr(ex);
        }
        try {
            if (isDecryption) {
                cipher.init(Cipher.ENCRYPT_MODE, skey, ivSpec);
            } else {
                cipher.init(Cipher.DECRYPT_MODE, skey, ivSpec);
            }
        } catch (InvalidKeyException ex) {
            printErr(ex);
        } catch (InvalidAlgorithmParameterException ex) {
            printErr(ex);
        }
        return cipher;
    }

    /**
     * It is for validating whether the encryption and decryption work well.
     * It is also the example about how to encrypt and decrypt.
     * @param size the number of bytes that is encrypted and decrypted
     * @param password the password
     * @return <tt>true</tt> if the original bytes are equal to the bytes that is the result of encryption and decryption
     */
    public boolean cryptValidate(int size, String password) {
        byte[] bytes = new byte[size];
//        r.nextBytes(bytes);
        byte[] byte_en = cryptEncrypt(bytes, password);
        byte[] byte_de = cryptDecrypt(byte_en, password);
        int diff = arrayCompare(bytes, byte_de, false);
        int diff2 = arrayCompare(bytes, byte_en, false);
        if (diff == 0 && diff2 != 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Compare two byte arrays.
     * @param orig
     * @param recon
     * @param isPrint
     * @return the number of bytes that are not equal.
     */
    public int arrayCompare(byte[] orig, byte[] recon, boolean isPrint) {
        int numError = 0;
        for (int i = 0; i < orig.length; i++) {
            if (orig[i] != recon[i]) {
                if (isPrint) {
                    Log.d("encryption","error on " + i + " :: " + orig[i] + " :: " + recon[i]);
                }
                numError++;
            }
        }
        if (isPrint) {
            Log.d("number or error :: ", numError+"");
        }
        return numError;
    }
    public static void printErr(java.lang.Exception ex) {
        Log.d("encyrption", ex.getMessage());
    }
}
