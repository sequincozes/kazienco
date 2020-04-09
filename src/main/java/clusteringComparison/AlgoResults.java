/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clusteringComparison;

/**
 *
 * @author silvio
 */
public class AlgoResults {
    String description;
    long vp = 0;
    long vn = 0;
    long fp = 0;
    long fn = 0;

    public void addVn(long qt) {
        setVn(getVn() + qt);
    }

    public void addVp(long qt) {
        setVp(getVp() + qt);
    }

    public void addFn(long qt) {
        setFn(getFn() + qt);
    }

    public void addFp(long qt) {
        setFp(getFp() + qt);
    }
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getVp() {
        return vp;
    }

    public void setVp(long vp) {
        this.vp = vp;
    }

    public long getVn() {
        return vn;
    }

    public void setVn(long vn) {
        this.vn = vn;
    }

    public long getFp() {
        return fp;
    }

    public void setFp(long fp) {
        this.fp = fp;
    }

    public long getFn() {
        return fn;
    }

    public void setFn(long fn) {
        this.fn = fn;
    }
    
}
