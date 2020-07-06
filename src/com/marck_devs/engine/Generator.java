package com.marck_devs.engine;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * Generate a random password using same chars pools.<br/>
 * This class has a builder to instance the object.
 * @author Marck C. Guzmán
 * @version 1.0.0
 */
public final class Generator {
    //Constants
    public static final String MAIN_POOL = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";
    public static final String SPECIAL_CHAR_POOL = "ñáéóúí~çäüöëïªºàèùìòÑÁÉÓÍÚÀÈÌÒÙÜÄËÏÖ";
    public static final String SIMBOLS_POOL = "+_?-!¿.,¡[]{}()<>";
    public static final String SPECIAL_SIMBOOLS_POOL = "$=%^&*@/\\¬#";
    public static final String NUMBER_POOL = "1234567890";

    private Integer length ;
    private ArrayList<String>  pools;
    private Long vector;
    private SecureRandom sr;

    /**
     * 
     * @return the {@link Builder} of the generator
     */
    public static Builder builder(){
        return new Builder();
    }

    private Generator(Integer l, ArrayList<String> pools, Long vector, SecureRandom sr){
        this.pools = pools;
        this.vector = vector;
        this.length = l;
        this.sr = sr;
    }

    private Integer genPoolIndex(int weight){
        double a = Math.sqrt(new Date().getTime() / vector);
        a = Math.sin(Math.pow(a, weight)) * pools.size();
        a = a % (pools.size()-1);
        return Math.toIntExact(Math.abs(Math.round(a)));
    }

    /**
     * Generate the password with all the data
     * @return {@link String} the password
     */
    public String gen(){
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int poolIndex = genPoolIndex(i);
            String pool = pools.get(poolIndex);
            int index = sr.nextInt(pool.length());
            double tmp = Math.pow(index,index) / (i*(Math.pow(index,poolIndex)));
            tmp = tmp % (pool.length()-1);
            index = new Double(tmp).intValue();
            out.append(pool.charAt(index));
        }
        return out.toString();
    }

    /**
     * Builder for the {@link Generator} class
     * @author Marck C. Guzmán
     *
     */
    public static final class Builder implements Cloneable{
        private Integer length = 8;
        private ArrayList<String> pools = new ArrayList<String>();
        private Long vector = 18795314L;

        private Builder(){}
        
        /**
         * Set the pass length
         * @param length
         * @return Builder
         */
        public Builder setLength(Integer length) {
            this.length = length;
            return this;
        }
        
        /**
         * Set the passgenerator pools
         * @param pools
         * @return Builder
         */
        public Builder setPools(String[] pools) {
            this.pools = new ArrayList<String>(Arrays.asList(pools));
            return this;
        }
        
        /**
         * Set the passgenerator pools
         * @param pools
         * @return Builder
         */
        public Builder setPools(ArrayList<String> pools){
            this.pools = pools;
            return this;
        }

        /**
         * Add one pool to the rest of the pools
         * @param pool
         * @return Builder
         */
        public Builder addPool(String pool){
            this.pools.add(pool);
            return this;
        }

        /**
         * Set the span vector for the generator
         * @param vector
         * @return
         */
        public Builder setVector(Long vector) {
            this.vector = vector;
            return this;
        }
        
        /**
         * Build the generator
         * @return {@link Generator} the instance
         */
        public Generator build(){
            try {
                return new Generator(length, pools,vector, SecureRandom.getInstance("SHA1PRNG"));
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected Object clone() throws CloneNotSupportedException {
            return new Builder().setLength(this.length).setPools(this.pools).setVector(this.vector);
        }
    }
}
