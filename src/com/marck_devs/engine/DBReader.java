package com.marck_devs.engine;

import com.marck_devs.beans.EntryDB;
import com.marck_devs.engine.exception.DataUnloadException;
import com.marck_devs.engine.exception.EmptyKeyException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

/**
 * Class that read a encrypted database
 * @author Marck C. Guzmán
 *
 */
public class DBReader {
	
    private static DBReader instance = new DBReader();

    /**
     * 
     * @return the instance of the class
     */
    public static DBReader getInstance() {
        return instance;
    }



    private File file;
    private byte[] crypto;
    private byte[] key;
    private EntryDB db = null;
    private byte[] data;

    private DBReader(){}

    /**
     * Set the file
     * @param file
     * @return
     */
    public DBReader setFile(File file) {
        this.file = file;
        return this;
    }

    /**
     * Set the file
     * @param file
     * @return
     */
    public DBReader setFile(String file) {
        return setFile(new File(file));
    }

    /**
     * Set the key
     * @param key
     * @return
     */
    public DBReader setKey(String key) {
        this.key = Arrays.copyOf(DigestUtils.sha1(key), 16);
        return this;
    }


    private void loadFile() throws IOException {
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
        byte[] d = IOUtils.toByteArray(in);
        crypto = Base64.getDecoder().decode(d);
        in.close();
    }

    /**
     * Decipher the file
     * @return {@link DBReader} the current object
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws EmptyKeyException
     * @throws IOException
     * @throws DataUnloadException
     */
    public DBReader decript() throws NoSuchPaddingException,
            NoSuchAlgorithmException,
            InvalidKeyException,
            BadPaddingException,
            IllegalBlockSizeException,
            EmptyKeyException, IOException, DataUnloadException {
        if(this.key == null || this.key.length == 0)
            throw new EmptyKeyException();
        loadFile();
        if(crypto == null)
            throw new DataUnloadException();
        SecretKeySpec sks = new SecretKeySpec(this.key, "AES");
        Cipher c = Cipher.getInstance("AES");
        c.init(Cipher.DECRYPT_MODE, sks);
        this.data = c.doFinal(crypto);
        return this;
    }

    /**
     * Load the {@link EntryDB} from the file
     * @return {@link EntryDB}
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public EntryDB loadDB() throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream oin = new ObjectInputStream(in);
        db = (EntryDB) oin.readObject();
        oin.close();
        return db;
    }

    /**
     * 
     * @return {@link EntryDB}
     * @throws DataUnloadException
     */
    public EntryDB getDb() throws DataUnloadException {
        if(db == null)
            throw new DataUnloadException();
        return db;
    }
}
