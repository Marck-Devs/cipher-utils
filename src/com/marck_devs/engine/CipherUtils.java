package com.marck_devs.engine;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public final class CipherUtils {

	/**
	 * Generate random id
	 * @param length of the id
	 * @return id
	 */
    public static String genId(int length){
        return Generator.builder().setLength(length)
                .addPool(Generator.MAIN_POOL)
                .addPool(Generator.NUMBER_POOL)
                .build().gen();
    }
    
    /**
     * Generate random id with 16 chars
     * @return id
     */
    public static String genId(){
        return genId(16);
    }

    /**
     * Generate random key to DESede algorithm
     * @return {@link Key}
     * @throws NoSuchAlgorithmException
     */
    public static Key genKey() throws NoSuchAlgorithmException {
        return KeyGenerator.getInstance("DESede").generateKey();
    }
    
    /**
     * Generate AES key from the password
     * @param pass
     * @return {@link Key}
     * @throws NoSuchAlgorithmException
     */
    public static Key genAESKey(String pass) throws NoSuchAlgorithmException {
    	byte[] data = Arrays.copyOf(DigestUtils.sha1(pass), 16);
    	SecretKeySpec a = new SecretKeySpec(data, "AES");
        return a;
    }

    /**
     * Cipher a string
     * @param text
     * @param key
     * @return string cipher
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws UnsupportedEncodingException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static String cipher(String text, Key key) throws NoSuchPaddingException,
            NoSuchAlgorithmException,
            InvalidKeyException,
            UnsupportedEncodingException,
            BadPaddingException,
            IllegalBlockSizeException {
        Cipher c = Cipher.getInstance("DESede");
        c.init(Cipher.ENCRYPT_MODE, key);
        String out = new String(c.doFinal(text.getBytes("UTF8")), "UTF8");
        return out.toString();
    }

    /**
     * Decipher the cipher string
     * @param text cipher text
     * @param key
     * @return original string
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws UnsupportedEncodingException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static String decipher(String text, Key key) throws NoSuchPaddingException,
            NoSuchAlgorithmException,
            InvalidKeyException,
            UnsupportedEncodingException,
            BadPaddingException,
            IllegalBlockSizeException {
        Cipher c = Cipher.getInstance("DESede");
        c.init(Cipher.DECRYPT_MODE, key);
        System.out.println(text);
        String out = new String(c.doFinal(text.getBytes("UTF8")), "UTF8");
        return out;
    }
    
    /**
     * Cipher the file
     * @param f file to cipher
     * @param key
     * @return byte[] cipher file
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws FileNotFoundException
     * @throws IOException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public static byte[] cipherFile(File f, Key key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, FileNotFoundException, IOException, IllegalBlockSizeException, BadPaddingException {
    	 Cipher c = Cipher.getInstance("AES");
         c.init(Cipher.ENCRYPT_MODE, key);
         byte[] data = IOUtils.readFully(new BufferedInputStream(new FileInputStream(f)), Math.toIntExact(f.length()));
    	return c.doFinal(data);
    }
    
    /**
     * Decipher a file
     * @param f cipher file
     * @param key
     * @return byte[] original file
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws FileNotFoundException
     * @throws IOException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public static byte[] decipherFile(File f, Key key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, FileNotFoundException, IOException, IllegalBlockSizeException, BadPaddingException {
   	 Cipher c = Cipher.getInstance("AES");
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] data = IOUtils.readFully(new BufferedInputStream(new FileInputStream(f)), Math.toIntExact(f.length()));
   	return c.doFinal(data);
   }

   
}
