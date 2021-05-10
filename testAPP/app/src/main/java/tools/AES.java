package tools;


import android.annotation.SuppressLint;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

public class AES {
    public static class Encryption{
        private static SecretKeySpec secretKey;
        private static byte[] key;

        public static void setKey(String myKey, String algorithm)
        {
            MessageDigest sha = null;
            try {
                key = myKey.getBytes("UTF-8");
                // select the encryption method SHA-1, MD5 or SHA-256
                // AES 是 Advanced Encryption Standard 是一种加密标准  是一种体系，其中加密的算法 有多种
                sha = MessageDigest.getInstance(algorithm);
                key = sha.digest(key);
                key = Arrays.copyOf(key, 16);
                secretKey = new SecretKeySpec(key, "AES");
            }
            catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }


        @SuppressLint("NewApi")
        public static String encrypt(String strToEncrypt, String secret, String algorithm)
        {
            try
            {
                setKey(secret, algorithm);
                Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
                cipher.init(Cipher.ENCRYPT_MODE, secretKey);
                return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
            }
            catch (Exception e)
            {
                System.out.println("Error while encrypting: " + e.toString());
            }
            return null;
        }
    }


    public static class Decryption{

        private static SecretKeySpec secretKey;
        private static byte[] key;

        public static void setKey(String myKey, String algorithm)
        {
            MessageDigest sha = null;
            try {
                key = myKey.getBytes("UTF-8");
                // select the encryption method SHA-1, MD5 or SHA-256
                // AES 是 Advanced Encryption Standard 是一种加密标准  是一种体系，其中加密的算法 有多种
                sha = MessageDigest.getInstance(algorithm);
                key = sha.digest(key);
                key = Arrays.copyOf(key, 16);
                secretKey = new SecretKeySpec(key, "AES");
            }
            catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        @SuppressLint("NewApi")
        public static String decrypt(String strToDecrypt, String secret, String algorithm)
        {
            try
            {
                setKey(secret, algorithm);
                Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
                cipher.init(Cipher.DECRYPT_MODE, secretKey);
                return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
            }
            catch (Exception e)
            {
                System.out.println("Error while decrypting: " + e.toString());
            }
            return null;
        }
    }





    public static void main(String[] args)
    {
        final String secretKey = "ssshhhhhhhhhhh!!!!"; // key

        String originalString = "{\"data\":1}";
        // select the encryption method SHA-1, MD5 or SHA-256
        String encryptedString = AES.Encryption.encrypt(originalString, secretKey,"SHA-256") ;


        String decryptedString = AES.Decryption.decrypt(encryptedString, secretKey,"SHA-256") ;

        System.out.println(originalString);
        System.out.println(encryptedString);
        System.out.println(decryptedString);
    }

}

