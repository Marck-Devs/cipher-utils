package com.marck_devs.engine;

import com.marck_devs.beans.EntryDB;
import com.marck_devs.engine.exception.DataUnloadException;
import com.marck_devs.engine.exception.EmptyKeyException;
import com.marck_devs.engine.exception.UnencriptedDataExection;
import com.marck_devs.engine.exception.UnsetFileException;
import org.apache.commons.codec.digest.DigestUtils;

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
 * Class that write the {@link EntryDB} object into a file,
 * all infomation will be encripted.
 * @author Marck C. Guzmán
 * @version 1.0.0
 *
 */
public class DBWriter {
    private static DBWriter instance = new DBWriter();
    
    /**
     * 
     * @return the unique instance of the class
     */
    public static DBWriter getInstance() {
        return instance;
    }

    private File file;
    private byte[] crypto;
    private byte[] key;
    private EntryDB db;
    private byte[] data;

    private DBWriter() {
    }
    
    /**
     * set the output file
     * @param file
     * @return {@link DBWriter}
     */
    public DBWriter setFile(File file) {
        this.file = file;
        return this;
    }
    
    /**
     * set the output file
     * @param file
     * @return {@link DBWriter}
     */
    public DBWriter setFile(String file) {
        return setFile(new File(file));
    }
    
    /**
     * Set the entry database
     * @param db
     * @return {@link DBWriter}
     */
    public DBWriter setEntryDB(EntryDB db) {
        this.db = db;
        return this;
    }
    
    /**
     * set the key
     * @param key
     * @return {@link DBWriter}
     */
    public DBWriter setKey(String key) {
        this.key = Arrays.copyOf(DigestUtils.sha1(key), 16);
        return this;
    }

    
    private void loadDB() throws IOException {
        ByteArrayOutputStream o = new ByteArrayOutputStream();
        ObjectOutputStream obs = new ObjectOutputStream(o);
        obs.writeObject(this.db);
        obs.close();
        this.data = o.toByteArray();
        o.close();
    }
    
    /**
     * Encrypt the database
     * @return {@link DBWriter}
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws EmptyKeyException
     * @throws DataUnloadException
     * @throws IOException
     */
    public DBWriter encript() throws NoSuchPaddingException,
            NoSuchAlgorithmException,
            InvalidKeyException,
            BadPaddingException,
            IllegalBlockSizeException,
            EmptyKeyException,
            DataUnloadException, IOException {
        if(this.key == null || this.key.length == 0)
            throw new EmptyKeyException();
        loadDB();
        if(data == null || data.length == 0)
            throw new DataUnloadException();
        SecretKeySpec sks = new SecretKeySpec(this.key, "AES");
        Cipher c = Cipher.getInstance("AES");
        c.init(Cipher.ENCRYPT_MODE, sks);
        this.crypto = c.doFinal(data);
        return this;
    }
    
    /**
     * Save the data into the file.<br/>
     * First must encrypt the file
     * @throws UnencriptedDataExection
     * @throws UnsetFileException
     * @throws IOException
     */
    public void save() throws UnencriptedDataExection,
            UnsetFileException,
            IOException {
        if(crypto == null || crypto.length == 0)
            throw new UnencriptedDataExection();
        if(file == null)
            throw new UnsetFileException();
        OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(file),"UTF8");
        out.write(Base64.getEncoder().encodeToString(crypto));
        out.close();
    }


}
