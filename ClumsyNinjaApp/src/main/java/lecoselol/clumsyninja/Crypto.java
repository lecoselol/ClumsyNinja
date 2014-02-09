package lecoselol.clumsyninja;

import android.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

public final class Crypto {
    private static final byte[] salt =
        {
            (byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32,
            (byte) 0x56, (byte) 0x34, (byte) 0xE3, (byte) 0x03
        };

    private static final int iterationCount = 19;

    private static final AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);

    private static SecretKey getSecretKey(String key) throws NoSuchAlgorithmException, InvalidKeySpecException {
        final KeySpec keySpec = new PBEKeySpec(key.toCharArray(), salt, iterationCount);
        return SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
    }

    /**
     * Encrypts a password. As I said!
     *
     * @param key  key for password encoding.
     * @param text body of password encoding.
     *
     * @return the encrypted password, if and only if it doesn't throw a bloody hell number of exceptions.
     * @throws UnsupportedEncodingException
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidAlgorithmParameterException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static String encrypt(String key, String text)
        throws UnsupportedEncodingException, InvalidKeySpecException, NoSuchAlgorithmException,
               NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException,
               IllegalBlockSizeException {
        final byte[] utf8_text = text.getBytes("UTF-8");

        final SecretKey secretKey = getSecretKey(key);

        Cipher cipher = Cipher.getInstance(secretKey.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, paramSpec);

        byte[] rawEncrypted = cipher.doFinal(utf8_text);

        return Base64.encodeToString(rawEncrypted, Base64.NO_WRAP);
    }

    /**
     * Decrypts a password. Maybe.
     *
     * @param key  key for password encoding.
     * @param text body of password encoding.
     *
     * @return the encrypted password, if and only if it doesn't throw a bloody hell number of exceptions.
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidAlgorithmParameterException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws UnsupportedEncodingException
     */
    public static String decrypt(String key, String text)
        throws InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException,
               InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException,
               UnsupportedEncodingException {
        final SecretKey secretKey = getSecretKey(key);
        final byte[] rawEncrypted = Base64.decode(text, Base64.NO_WRAP);

        Cipher cipher = Cipher.getInstance(secretKey.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, secretKey, paramSpec);

        final byte[] utf8_decrypted = cipher.doFinal(rawEncrypted);

        return new String(utf8_decrypted, "UTF-8");
    }

    /**
     * Calculates SHA256 of a string in input
     *
     * @param password something you want to be hashed
     *
     * @return let me guess... an hashed string, maybe. Not sure though. Check the code, you lazy dude.
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static String SHA256_orYouDie(String password) throws NoSuchAlgorithmException,
                                                                 UnsupportedEncodingException {
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.reset();

        final byte[] byteData = digest.digest(password.getBytes("UTF-8"));
        StringBuilder bufferedEncodedPassword = new StringBuilder();

        for (byte aByteData : byteData) {
            bufferedEncodedPassword.append(Integer.toString((aByteData & 0xff) + 0x100, 16).substring(1));
        }

        return bufferedEncodedPassword.toString().toLowerCase();
    }
}