/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clusteringComparison;

/**
 * @author silvio
 */
public class ClusterResults {
    enum CRITERIA {CENTROID_CLASS, MAJORITARY_CLASS}

    public static CRITERIA anomalyCriteria = CRITERIA.MAJORITARY_CLASS;

    public double getCentroidClass() {
        return centroidClass;
    }

    public void setCentroidClass(double centroidClass) {
        this.centroidClass = centroidClass;
    }

    private double centroidClass;

    public boolean isAnomalous() {
        if (anomalyCriteria == CRITERIA.MAJORITARY_CLASS) {
            return attacks >= normals;
        } else {
            return centroidClass>0;
        }
    }

    int vp = 0;
    int vn = 0;
    int fp = 0;
    int fn = 0;
    int clusterNum;

    long attacks = 0;
    long normals = 0;


    public long getSize() {
        return attacks + normals;
    }


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

//    public double getClusterClass() {
//        return clusterClass;
//    }
//
//    public void setClusterClass(double clusterClass) {
//        this.clusterClass = clusterClass;
//    }

    public int getVp() {
        return vp;
    }

    public void setVp(int vp) {
        this.vp = vp;
    }

    public int getVn() {
        return vn;
    }

    public void setVn(int vn) {
        this.vn = vn;
    }

    public int getFp() {
        return fp;
    }

    public void setFp(int fp) {
        this.fp = fp;
    }

    public int getFn() {
        return fn;
    }

    public void setFn(int fn) {
        this.fn = fn;
    }

    public int getClusterNum() {
        return clusterNum;
    }

    public void setClusterNum(int clusterNum) {
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


    public void setClusterClass(double centroidClass) {
        this.centroidClass = centroidClass;
    }
}
