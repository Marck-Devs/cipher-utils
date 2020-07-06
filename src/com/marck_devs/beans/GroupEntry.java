package com.marck_devs.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.function.Consumer;

/**
 * Represent a group of pass entry, useful to organized in folders
 */
public class GroupEntry extends Entry implements Serializable {

    private HashSet<Entry> entries;

    public GroupEntry() {
        entries = new HashSet<Entry>();
    }

    public GroupEntry(String name, Icon icon, String desciption, String id, EntryDB db, HashSet<Entry> entries) {
        super(name, icon, desciption, id, db);
        this.entries = entries;
    }

    /**
     * 
     * @return the content entries {@link Entry}
     */
    public HashSet<Entry> getEntries() {
        return entries;
    }
    
    /**
     * Set the entries
     * @param entries {@link Entry}
     */
    public void setEntries(ArrayList<Entry> entries) {
        this.entries = new HashSet<Entry>();
        entries.forEach(new Consumer<Entry>() {
            @Override
            public void accept(Entry entry) {
                addEntry(entry);
            }
        });
        mod();
    }


    /**
     * Add one pass entry to the group
     * @param entry
     * @return
     */
    public GroupEntry addEntry(Entry entry){
        entries.add(entry);
        entry.setGroup(this);
        mod();
        return this;
    }

    /**
     * add all entries to the group
     * @param entries
     * @return
     */
    public GroupEntry addEntries(Entry[] entries){
        this.entries.addAll(Arrays.asList(entries));
        mod();
        return this;
    }

    /**
     * add all entries to the group
     * @param entries
     * @return
     */
    public GroupEntry addEntries(ArrayList<Entry> entries){
        this.entries.addAll(entries);
        mod();
        return this;
    }



    public String toString(){
        return this.name;
    }
}
