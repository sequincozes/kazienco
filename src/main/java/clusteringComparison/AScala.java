/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clusteringComparison;

import kazienko.Util;

/**
 *
 * @author silvio
 */
public class AScala {

    public static void main(String[] args) throws Exception {

         
        String ataques[] = {"flooding", "grayhole", "blackhole"};
        int ks[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30};
        int[][] fss = {Parameters.GR5WSN, Parameters.IG5WSN, Parameters.OneR5WSN};
        String[] fsNames = {"GR", "IG", "OneR"};      
        ClusteringEnum[] clusters = {ClusteringEnum.EM, ClusteringEnum.FARTEST, ClusteringEnum.KMEANS};                
        
/*
        ClusteringEnum[] clusters = {ClusteringEnum.EM};
        String ataques[] = {"flooding"};
        int ks[] = {5};
        int[][] fss = {Parameters.GR5WSN};
        String[] fsNames = {"GR"};
 */
        Parameters.NORMALIZE = true;
        Engine.balance = false;
        Engine.threshold = 1;
        Engine.centroidBased = false;
        Engine.init(true, Output.ACCURACY);
        Engine.run(clusters, ataques, ks, fss, fsNames);
    }
}
