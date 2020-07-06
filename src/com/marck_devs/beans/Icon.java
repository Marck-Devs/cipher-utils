package com.marck_devs.beans;

import com.marck_devs.engine.CipherUtils;

import java.awt.image.BufferedImage;
import java.io.Serializable;

public class Icon implements Serializable {
    private String name;
    private BufferedImage image;

    public Icon() {
        name = CipherUtils.genId();
    }

    public Icon(String name, BufferedImage image) {
        this.name = name;
        this.image = image;
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
    }

    /**
     * 
     * @return the image {@link BufferedImage}
     */
    public BufferedImage getImage() {
        return image;
    }

    /**
     * Set the image
     * @param image {@link BufferedImage}
     */
    public void setImage(BufferedImage image) {
        this.image = image;
    }

}
