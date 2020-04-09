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
public class ClusterResults {

    double clusterClass;
    long vp = 0;
    long vn = 0;
    long fp = 0;
    long fn = 0;
    long clusterNum;

    long attacks = 0;
    long normals = 0;


    public void addAttack() {
        setAttacks(getAttacks() + 1);
    }

    public void addNormal() {
        setNormals(getNormals() + 1);
    }

    public void addVn() {
        setVn(getVn() + 1);
    }
    
    public void addVp() {
        setVp(getVp() + 1);
    }

    public void addFn() {
        setFn(getFn() + 1);
    }

    public void addFp() {
        setFp(getFp() + 1);
    }

    public double getClusterClass() {
        return clusterClass;
    }

    public void setClusterClass(double clusterClass) {
        this.clusterClass = clusterClass;
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

    public long getClusterNum() {
        return clusterNum;
    }

    public void setClusterNum(long clusterNum) {
        this.clusterNum = clusterNum;
    }

    public long getAttacks() {
        return attacks;
    }

    public void setAttacks(long attacks) {
        this.attacks = attacks;
    }

    public long getNormals() {
        return normals;
    }

    public void setNormals(long normals) {
        this.normals = normals;
    }

    
}
