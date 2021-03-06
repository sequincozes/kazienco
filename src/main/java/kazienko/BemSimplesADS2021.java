/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kazienko;

import clusteringComparison.ClusterResults;
import weka.clusterers.EM;
import weka.clusterers.FarthestFirst;
import weka.clusterers.SimpleKMeans;
import weka.core.Instances;

/**
 * @author silvio
 */


public class BemSimplesADS2021 {

    public static void main(String[] args) throws Exception {
        System.out.println("algorithm;fs;attack;k;vp" + ";" + "vn" + ";" + "fp" + ";" + "fn;sensibility");
        Parameters.ATAQUE = "BLACKHOLE";
        Parameters.FS_NAME = "GR"; run30K(Parameters.CLUSTER_KMEANS, Parameters.GR5WSN);
        Parameters.FS_NAME = "IG"; run30K(Parameters.CLUSTER_KMEANS, Parameters.IG5WSN);
        Parameters.FS_NAME = "OneR"; run30K(Parameters.CLUSTER_KMEANS, Parameters.OneR5WSN);

        Parameters.FS_NAME = "GR"; run30K(Parameters.CLUSTER_FF, Parameters.GR5WSN);
        Parameters.FS_NAME = "IG"; run30K(Parameters.CLUSTER_FF, Parameters.IG5WSN);
        Parameters.FS_NAME = "OneR"; run30K(Parameters.CLUSTER_FF, Parameters.OneR5WSN);

        Parameters.FS_NAME = "GR"; run30K(Parameters.CLUSTER_EM, Parameters.GR5WSN);
        Parameters.FS_NAME = "IG"; run30K(Parameters.CLUSTER_EM, Parameters.IG5WSN);
        Parameters.FS_NAME = "OneR"; run30K(Parameters.CLUSTER_EM, Parameters.OneR5WSN);

    }

    private static void run30K(String clusteringAlgo, int[] fs) throws Exception {
        Parameters.CLUSTER_ALGORITHM = clusteringAlgo;
        Parameters.FEATURE_SELECTION = fs;

        for (int k = 2; k <= 30; k++) {
            for (int i = 1; i <= 5; i++) {
                setupFolds(i);
//        int k = 10;
                Instances[] folds = Util.readUnsupervisedAndLabelsMultiple(false);
                if (Parameters.CLUSTER_ALGORITHM.equalsIgnoreCase(Parameters.CLUSTER_EM)) {
                    ads2021EM(folds[0], folds[1], folds[2], folds[3], k);
                } else if (Parameters.CLUSTER_ALGORITHM.equalsIgnoreCase(Parameters.CLUSTER_KMEANS)) {
                    ads2021Kmeans(folds[0], folds[1], folds[2], folds[3], k);
                } else if (Parameters.CLUSTER_ALGORITHM.equalsIgnoreCase(Parameters.CLUSTER_FF)) {
                    ads2021FF(folds[0], folds[1], folds[2], folds[3], k);
                }

            }
        }
    }

    private static void setupFolds(int i) {
        String f1aux = Parameters.FOLD1;
        String f2aux = Parameters.FOLD2;
        String f3aux = Parameters.FOLD3;
        String f4aux = Parameters.FOLD4;
        String f5aux = Parameters.FOLD5;

        switch (i) {
            case 1:
                Parameters.FOLD5 = f1aux;
                Parameters.FOLD1 = f5aux;
                break;
            case 2:
                Parameters.FOLD5 = f2aux;
                Parameters.FOLD2 = f5aux;
                break;
            case 3:
                Parameters.FOLD5 = f3aux;
                Parameters.FOLD3 = f5aux;
                break;
            case 4:
                Parameters.FOLD5 = f4aux;
                Parameters.FOLD4 = f5aux;
                break;
            case 5:
                break;
        }
    }

    public static void ads2021EM(Instances train, Instances test, Instances trainLabeled, Instances testLabeled, int k) throws Exception {

        if (false) {
            System.out.println("Train: " + train.size() + " instances.");
            System.out.println("Test: " + test.size() + " instances.");
            System.out.println("Train Labeled: " + trainLabeled.size() + " instances.");
            System.out.println("Test Labeled: " + testLabeled.size() + " instances.");
        }

        ClusterResults[] results = new ClusterResults[k];
//        SimpleKMeans kmeas = Util.clusterData(train, k);
//        FarthestFirst kmeas = Util.clusterDataFarthest(train, k); // Trocar
        EM kmeas = Util.clusterDataEM(train, k); // Trocar

        for (int i = 0; i < k; i++) {
            results[i] = new ClusterResults();
        }

        for (int i = 0; i < train.numInstances(); i++) {
//            System.out.println(i + "-" + labeled.instance(i).classValue() + " = " + labeled.instance(i).toString());
            int clusterNum = kmeas.clusterInstance(train.instance(i));
            if (trainLabeled.instance(i).classValue() > 0) {
                results[clusterNum].addAttack(); // Suponha que >0 é ataque (1)
            } else {
                results[clusterNum].addNormal(); // Suponha que zero é normal
            }
        }
//
//        int[] assignments = kmeas.getAssignments();
//        System.out.println(assignments[4]);
//        System.exit(0);
//        for (int i = 0; i < train.numInstances(); i++) {
//
//        }
        for (int i = 0; i < test.numInstances(); i++) {
//            System.out.println(i + "-" + labeled.instance(i).classValue() + " = " + labeled.instance(i).toString());
            int clusterNum = kmeas.clusterInstance(test.instance(i));
            if (results[clusterNum].isAnomalous()) {
                if (testLabeled.instance(i).classValue() > 0) {
                    results[clusterNum].addVp(); // Attack clustered as anomaly
                } else {
                    results[clusterNum].addFp(); // Normal clustered as anomaly
                }
            } else {
                if (testLabeled.instance(i).classValue() > 0) {
                    results[clusterNum].addFn(); // Attack clustered as Normal
                } else {
                    results[clusterNum].addVn(); // Normal clustered as Normal
                }
            }
        }

        // Print
        int vp = 0, vn = 0, fp = 0, fn = 0;
        for (int i = 0; i < k; i++) {
            if (false) {
                System.out.println("Cluster " + results[i].getClusterNum() + ": " + results[i].getSize() +
                        " (Atk: " + results[i].getAttacks() + ", Nor:" + results[i].getNormals() + ")"
                        + " (Centroid: " + results[i].getCentroidClass() + " / " + "isAnomalous?" + results[i].isAnomalous() + ")"
                        + " (VP: " + results[i].getVp() + "|"
                        + " VN: " + results[i].getVn() + "|"
                        + " FP: " + results[i].getFp() + "|"
                        + " FN: " + results[i].getFn() + ")");
            }

            vp = vp + results[i].getVp();
            vn = vn + results[i].getVn();
            fp = fp + results[i].getFp();
            fn = fn + results[i].getFn();
        }
        Float sensibility = Float.valueOf((Float.valueOf(vp)) / (Float.valueOf(vp) + fn));
        System.out.println(Parameters.CLUSTER_ALGORITHM+";"+Parameters.FS_NAME+";"+Parameters.ATAQUE+";"+k + ";" + vp + ";" + vn + ";" + fp + ";" + fn  );//";" + sensibility);
    }

    public static void ads2021FF(Instances train, Instances test, Instances trainLabeled, Instances testLabeled, int k) throws Exception {

        if (false) {
            System.out.println("Train: " + train.size() + " instances.");
            System.out.println("Test: " + test.size() + " instances.");
            System.out.println("Train Labeled: " + trainLabeled.size() + " instances.");
            System.out.println("Test Labeled: " + testLabeled.size() + " instances.");
        }

        ClusterResults[] results = new ClusterResults[k];
//        SimpleKMeans kmeas = Util.clusterData(train, k);
        FarthestFirst kmeas = Util.clusterDataFarthest(train, k); // Trocar
//        EM kmeas = Util.clusterDataEM(train, k); // Trocar

        for (int i = 0; i < k; i++) {
            results[i] = new ClusterResults();
        }

        for (int i = 0; i < train.numInstances(); i++) {
//            System.out.println(i + "-" + labeled.instance(i).classValue() + " = " + labeled.instance(i).toString());
            int clusterNum = kmeas.clusterInstance(train.instance(i));
            if (trainLabeled.instance(i).classValue() > 0) {
                results[clusterNum].addAttack(); // Suponha que >0 é ataque (1)
            } else {
                results[clusterNum].addNormal(); // Suponha que zero é normal
            }
        }
//
//        int[] assignments = kmeas.getAssignments();
//        System.out.println(assignments[4]);
//        System.exit(0);
//        for (int i = 0; i < train.numInstances(); i++) {
//
//        }
        for (int i = 0; i < test.numInstances(); i++) {
//            System.out.println(i + "-" + labeled.instance(i).classValue() + " = " + labeled.instance(i).toString());
            int clusterNum = kmeas.clusterInstance(test.instance(i));
            if (results[clusterNum].isAnomalous()) {
                if (testLabeled.instance(i).classValue() > 0) {
                    results[clusterNum].addVp(); // Attack clustered as anomaly
                } else {
                    results[clusterNum].addFp(); // Normal clustered as anomaly
                }
            } else {
                if (testLabeled.instance(i).classValue() > 0) {
                    results[clusterNum].addFn(); // Attack clustered as Normal
                } else {
                    results[clusterNum].addVn(); // Normal clustered as Normal
                }
            }
        }

        // Print
        int vp = 0, vn = 0, fp = 0, fn = 0;
        for (int i = 0; i < k; i++) {
            if (false) {
                System.out.println("Cluster " + results[i].getClusterNum() + ": " + results[i].getSize() +
                        " (Atk: " + results[i].getAttacks() + ", Nor:" + results[i].getNormals() + ")"
                        + " (Centroid: " + results[i].getCentroidClass() + " / " + "isAnomalous?" + results[i].isAnomalous() + ")"
                        + " (VP: " + results[i].getVp() + "|"
                        + " VN: " + results[i].getVn() + "|"
                        + " FP: " + results[i].getFp() + "|"
                        + " FN: " + results[i].getFn() + ")");
            }

            vp = vp + results[i].getVp();
            vn = vn + results[i].getVn();
            fp = fp + results[i].getFp();
            fn = fn + results[i].getFn();
        }
        Float sensibility = Float.valueOf((Float.valueOf(vp)) / (Float.valueOf(vp) + fn));
        System.out.println(Parameters.CLUSTER_ALGORITHM+";"+Parameters.FS_NAME+";"+Parameters.ATAQUE+";"+k + ";" + vp + ";" + vn + ";" + fp + ";" + fn  );//";" + sensibility);
    }

    public static void ads2021Kmeans(Instances train, Instances test, Instances trainLabeled, Instances testLabeled, int k) throws Exception {

        if (false) {
            System.out.println("Train: " + train.size() + " instances.");
            System.out.println("Test: " + test.size() + " instances.");
            System.out.println("Train Labeled: " + trainLabeled.size() + " instances.");
            System.out.println("Test Labeled: " + testLabeled.size() + " instances.");
        }

        ClusterResults[] results = new ClusterResults[k];
        SimpleKMeans kmeas = Util.clusterData(train, k);
//        FarthestFirst kmeas = Util.clusterDataFarthest(train, k); // Trocar
//        EM kmeas = Util.clusterDataEM(train, k); // Trocar

        for (int i = 0; i < k; i++) {
            results[i] = new ClusterResults();
        }

        for (int i = 0; i < train.numInstances(); i++) {
//            System.out.println(i + "-" + labeled.instance(i).classValue() + " = " + labeled.instance(i).toString());
            int clusterNum = kmeas.clusterInstance(train.instance(i));
            if (trainLabeled.instance(i).classValue() > 0) {
                results[clusterNum].addAttack(); // Suponha que >0 é ataque (1)
            } else {
                results[clusterNum].addNormal(); // Suponha que zero é normal
            }
        }
//
//        int[] assignments = kmeas.getAssignments();
//        System.out.println(assignments[4]);
//        System.exit(0);
//        for (int i = 0; i < train.numInstances(); i++) {
//
//        }
        for (int i = 0; i < test.numInstances(); i++) {
//            System.out.println(i + "-" + labeled.instance(i).classValue() + " = " + labeled.instance(i).toString());
            int clusterNum = kmeas.clusterInstance(test.instance(i));
            if (results[clusterNum].isAnomalous()) {
                if (testLabeled.instance(i).classValue() > 0) {
                    results[clusterNum].addVp(); // Attack clustered as anomaly
                } else {
                    results[clusterNum].addFp(); // Normal clustered as anomaly
                }
            } else {
                if (testLabeled.instance(i).classValue() > 0) {
                    results[clusterNum].addFn(); // Attack clustered as Normal
                } else {
                    results[clusterNum].addVn(); // Normal clustered as Normal
                }
            }
        }

        // Print
        int vp = 0, vn = 0, fp = 0, fn = 0;
        for (int i = 0; i < k; i++) {
            if (false) {
                System.out.println("Cluster " + results[i].getClusterNum() + ": " + results[i].getSize() +
                        " (Atk: " + results[i].getAttacks() + ", Nor:" + results[i].getNormals() + ")"
                        + " (Centroid: " + results[i].getCentroidClass() + " / " + "isAnomalous?" + results[i].isAnomalous() + ")"
                        + " (VP: " + results[i].getVp() + "|"
                        + " VN: " + results[i].getVn() + "|"
                        + " FP: " + results[i].getFp() + "|"
                        + " FN: " + results[i].getFn() + ")");
            }

            vp = vp + results[i].getVp();
            vn = vn + results[i].getVn();
            fp = fp + results[i].getFp();
            fn = fn + results[i].getFn();
        }
        Float sensibility = Float.valueOf((Float.valueOf(vp)) / (Float.valueOf(vp) + fn));
        System.out.println(Parameters.CLUSTER_ALGORITHM+";"+Parameters.FS_NAME+";"+Parameters.ATAQUE+";"+k + ";" + vp + ";" + vn + ";" + fp + ";" + fn  );//";" + sensibility);
    }


}
