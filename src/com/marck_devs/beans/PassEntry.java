package com.marck_devs.beans;

import com.marck_devs.engine.CipherUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

public class PassEntry extends Entry implements Serializable {

    private URL site;
    private String user;
    private String pass;
    private Key iv;
    private GroupEntry group;

    public PassEntry() throws NoSuchAlgorithmException {
        iv = CipherUtils.genKey();
    }

    /**
     *
     * @param name
     * @param icon
     * @param desciption
     * @param id
     * @param db
     * @param site
     * @param user
     * @param pass
     * @param group
     */
    public PassEntry(String name,
                     Icon icon,
                     String desciption,
                     String id,
                     EntryDB db,
                     URL site,
                     String user,
                     String pass,
                     GroupEntry group) {
        super(name, icon, desciption, id, db);
        this.site = site;
        this.user = user;
        this.pass = pass;
        this.group = group;
        group.addEntry(this);
    }

    /**
     * 
     * @return the {@link URL} site
     */
    public URL getSite() {
        return site;
    }

    /**
     * 
     * @param site
     */
    public void setSite(URL site) {
        this.site = site;
        mod();
    }

    /**
     * 
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * Set the user
     * @param user
     */
    public void setUser(String user) {
        this.user = user;
        mod();
    }

    /**
     * 
     * @return the pass
     * @throws NoSuchPaddingException
     * @throws BadPaddingException
     * @throws NoSuchAlgorithmException
     * @throws IllegalBlockSizeException
     * @throws UnsupportedEncodingException
     * @throws InvalidKeyException
     */
    public String getPass() throws NoSuchPaddingException,
            BadPaddingException,
            NoSuchAlgorithmException,
            IllegalBlockSizeException,
            UnsupportedEncodingException,
            InvalidKeyException {
        return CipherUtils.decipher(pass, iv);
    }

    /**
     * Set the password
     * @param pass
     * @throws NoSuchPaddingException
     * @throws BadPaddingException
     * @throws NoSuchAlgorithmException
     * @throws IllegalBlockSizeException
     * @throws UnsupportedEncodingException
     * @throws InvalidKeyException
     */
    public void setPass(String pass) throws NoSuchPaddingException,
            BadPaddingException,
            NoSuchAlgorithmException,
            IllegalBlockSizeException,
            UnsupportedEncodingException,
            InvalidKeyException {
        this.pass = CipherUtils.cipher(pass,iv);
        System.out.println(this.pass);
        mod();
    }

    /**
     * 
     * @return the iv
     */
    public Key getIv() {
        return iv;
    }

    /**
     * @return the group
     */
    public GroupEntry getGroup() {
        return group;
    }

    /**
     * Set the group
     * @param group
     */
    public void setGroup(GroupEntry group) {
        this.group = group;
        db = group.getDb();
        group.addEntry(this);
        mod();
    }
    
    /**
     * Move the current {@link Entry} to the destination {@link GroupEntry}
     * @param dest
     */
    public void moveGroup(GroupEntry dest){
        group.getEntries().remove(this);
        setGroup(dest);
    }

    @Override
    public String toString() {
        return "PassEntry{" +
                "name='" + name + '\'' +
                ", site=" + site +
                ", user='" + user + '\'' +
                ", group=" + group +
                '}';
    }
}
