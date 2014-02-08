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
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

public final class Crypto
{
    private static final byte[] salt =
            {
                    (byte)0xA9, (byte)0x9B, (byte)0xC8, (byte)0x32,
                    (byte)0x56, (byte)0x34, (byte)0xE3, (byte)0x03
            };

    private static final int iterationCount = 19;

    private static final AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);

    private static SecretKey getSecretKey(String key) throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        final KeySpec keySpec = new PBEKeySpec(key.toCharArray(), salt, iterationCount);
        return SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
    }

    public static String encrypt(String key, String text)
            throws UnsupportedEncodingException, InvalidKeySpecException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException,
            IllegalBlockSizeException
    {
        final byte[] utf8_text = text.getBytes("UTF-8");

        final SecretKey secretKey = getSecretKey(key);

        Cipher cipher = Cipher.getInstance(secretKey.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, paramSpec);

        byte[] rawEncrypted = cipher.doFinal(utf8_text);

        return Base64.encodeToString(rawEncrypted, Base64.NO_PADDING);
    }

    public static String decrypt(String key, String text)
            throws InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException,
            UnsupportedEncodingException
    {
        final SecretKey secretKey = getSecretKey(key);
        final byte[] rawEncrypted = Base64.decode(text, Base64.NO_PADDING);

        Cipher cipher = Cipher.getInstance(secretKey.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, secretKey, paramSpec);

        final byte[] utf8_decrypted = cipher.doFinal(rawEncrypted);

        return new String(utf8_decrypted, "UTF-8");
    }
}