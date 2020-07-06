package com.marck_devs.beans;

import com.marck_devs.engine.CipherUtils;

import java.io.Serializable;
import java.util.Objects;

public class Entry implements Serializable {

    protected String name;
    protected Icon icon;
    protected String desciption;
    protected String id = CipherUtils.genId(12);
    protected EntryDB db;
    protected GroupEntry group;

    public Entry() {
    }

    public Entry(String name, Icon icon, String desciption, String id, EntryDB db) {
        this.name = name;
        this.icon = icon;
        this.desciption = desciption;
        this.id = id;
        this.db = db;
    }
    protected void mod(){
        if(db!=null)
            db.mod();
    }
    /**
     * 
     * @return the data base
     */
    public EntryDB getDb() {
        return db;
    }

    /**
     * Set the data base
     * @param db
     */
    public void setDb(EntryDB db) {
        this.db = db;
    }
    
    /**
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
        mod();
    }

    /**
     * 
     * @return the icon
     */
    public Icon getIcon() {
        return icon;
    }

    /**
     * Set the icon
     * @param icon
     */
    public void setIcon(Icon icon) {
        this.icon = icon;
        mod();
    }

    /**
     * 
     * @return the description
     */
    public String getDesciption() {
        return desciption;
    }

    /**
     * Set the description
     * @param desciption
     */
    public void setDesciption(String desciption) {
        this.desciption = desciption;
        mod();
    }

    /**
     * 
     * @return the entry group
     */
    public GroupEntry getGroup() {
        return group;
    }

    /**
     * Set the entry group
     * @param group
     */
    public void setGroup(GroupEntry group) {
        this.group = group;
    }

    /**
     * 
     * @return the entry id
     */
    public String getId() {
        return id;
    }

    /**
     * 
     * @return if the entry is a {@link GroupEntry}
     */
    public boolean isGroup(){
        return getClass().getSimpleName().equals(GroupEntry.class.getSimpleName());
    }

    /**
     * 
     * @return if the entry is a {@link PassEntry}
     */
    public boolean isEntry(){
        return getClass().getSimpleName().equals(PassEntry.class.getSimpleName());
    }

    /**
     * Add to the set database  
     */
    public void add2DB(){
        db.addEntry(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entry entry = (Entry) o;
        return Objects.equals(name, entry.name) &&
                id.equals(entry.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id);
    }
}
