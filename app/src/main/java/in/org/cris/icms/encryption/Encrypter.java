package in.org.cris.icms.encryption;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Random;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Created by anurag on 10/7/17.
 */
public class Encrypter {
    private static final int ITERATIONS = 1000;
    private static final int KEY_LENGTH = 256;
    private static final Random RANDOM = new SecureRandom();

    public static String generateHash(String password, byte[] salt){
        return hashPassword(password.toCharArray(), salt);
    }

    private static String hashPassword(char[] password, byte[] salt){
        PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);
        try{
            SecretKeyFactory key = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hashedPassword = key.generateSecret(spec).getEncoded();
            return String.format("%x", new BigInteger(hashedPassword));
        } catch(NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }finally{
            spec.clearPassword();
        }
    }

    public static byte[] getNextSalt() {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        return salt;
    }

    public static String generateFileName(String username){
        if(username != null){
            try {
                //MD5 Algorithm
                MessageDigest digest = MessageDigest.getInstance("MD5");
                digest.update(username.getBytes(), 0, username.length());
                return new BigInteger(1, digest.digest()).toString(16);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        return username;
    }
}