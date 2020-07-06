package com.marck_devs.beans;

import com.marck_devs.engine.CipherUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.function.Consumer;

public class EntryDB  implements Serializable, Cloneable {
    private HashSet<Entry> entries;
    private Date creationDate;
    private Date modDate;
    private String id = CipherUtils.genId();

    public EntryDB() {
        creationDate = new Date();
        modDate = new Date();
        entries = new HashSet<Entry>();
    }

    /**
     * Set the modification date. Take the actual time to set the date.
     */
    public void mod(){
        modDate = new Date();
    }


    /**
     * 
     * @return the entries of the db
     * @see Entry
     */
    public HashSet<Entry> getEntries() {
        return entries;
    }

    /**
     * 
     * @return the creation date
     */
    public Date getCreationDate() {
        return creationDate;
    }

    private void setCreationDate(Date date){this.creationDate = date;}

    private void setModDate(Date d){this.modDate = d;}

    /**
     * 
     * @return the modification date
     */
    public Date getModDate() {
        return modDate;
    }

    /**
     * Set the entries of the data base
     * @param entries {@link HashSet}
     */
    private void setEntries(HashSet<Entry> entries){
        this.entries = entries;
    }

    /**
     * Set the entries of the data base
     * @param entries {@link ArrayList}
     */
    public void setEntries(ArrayList<Entry> entries) {
        mod();
        this.entries = new HashSet<Entry>();
        entries.forEach(new Consumer<Entry>() {
            @Override
            public void accept(Entry entry) {
                addEntry(entry);
            }
        });
    }

    /**
     * Add entry to the database
     * @param entry
     * @return {@link EntryDB} current object
     */
    public EntryDB addEntry(Entry entry){
        entries.add(entry);
        entry.setDb(this);
        return this;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        EntryDB out = new EntryDB();
        out.setEntries(this.getEntries());
        out.setModDate(this.getModDate());
        out.setCreationDate(this.getCreationDate());
        return out;
    }
}
