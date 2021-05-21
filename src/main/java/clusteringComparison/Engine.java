/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clusteringComparison;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import kazienko.Util;
import weka.clusterers.AbstractClusterer;
import weka.clusterers.EM;
import weka.clusterers.FarthestFirst;
import weka.clusterers.SimpleKMeans;
import weka.core.EuclideanDistance;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Normalize;

/**
 *
 * @author silvio
 */
public class Engine {
    static Instances test;
    static Instances labeled;
    static double normalClass = -1;
    static ClusteringEnum[] clusters = {ClusteringEnum.KMEANS};
    static int ks[] = {2};
    static int[][] fss = {Parameters.GR5WSN};
    static Output output;
    static boolean printSelection = true;
    static boolean centroidBased = false;
    static double threshold = 0;
    static boolean balance = true;
        
    public static void init(boolean printSelectionP, Output outputType) throws IOException, Exception {
        output = outputType;
        printSelection = printSelectionP;
    }

    public static void loadData() throws IOException, Exception{
        Instances labeledNormal = new Instances(readDataFile(Parameters.NORMAL_FILE));
        Instances labeledAttack = new Instances(readDataFile(Parameters.TEST_FILE));
        if(balance){
            labeledNormal = new Instances(labeledNormal, 0, labeledAttack.numInstances());
        }
        
        labeled = new Instances(labeledNormal);
        labeled.addAll(labeledAttack);

        test = new Instances(labeled);
        test.deleteAttributeAt(test.numAttributes() - 1); // Remove classe   
        
     
        
        if (Parameters.FEATURE_SELECTION.length > 0) {
            test = ScalaUtils.applyFilterKeep(test);
            labeled = ScalaUtils.applyFilterKeep(labeled);
            if (printSelection) {
                System.out.println(Arrays.toString(Parameters.FEATURE_SELECTION) + " - ");
            }
        }
        
        labeled.setClassIndex(labeled.numAttributes() - 1);    
        normalClass = labeled.get(0).classValue();

        if(Parameters.NORMALIZE){
            Normalize filterNorm = new Normalize();
            filterNorm.setInputFormat(test);
            test = Filter.useFilter(test, filterNorm);
        }   
    }
 
    public static AbstractClusterer clusterData(Instances evaluation, ClusteringEnum algo, int k) throws Exception {
    AbstractClusterer clusteringAlgo;
    switch (algo) {
        case EM:
            EM em = new EM();
            em.setNumClusters(k);
            em.buildClusterer(evaluation);
            clusteringAlgo = em;
            break;
        case FARTEST:
            FarthestFirst ff = new FarthestFirst();
            ff.setNumClusters(k);
            ff.buildClusterer(evaluation);
            clusteringAlgo = ff;
            break;
        case KMEANS:
            SimpleKMeans sk = new SimpleKMeans();    
            sk.setPreserveInstancesOrder(true);
            sk.setNumClusters(k);
            sk.buildClusterer(evaluation);           
            clusteringAlgo = sk;
            break;
        default:
            clusteringAlgo = null;
            break;
        }
      
        return clusteringAlgo;

    }
    
    static void run(ClusteringEnum[] clusters, String[] ataques, int[] ks, int[][] fss, String fsDescription[]) throws Exception {
        if (output == Output.ACCURACY){          
            System.out.println("Clusterig;Seleção;Ataque;k;VP;VN;FP;FN");              
        } 
        for (String ataque : ataques) {
            int fsIndex= 0;
            for (int[] fs : fss) {
                Parameters.FEATURE_SELECTION = fs;        
                Parameters.changeAttack(ataque);
                loadData();
                    for (ClusteringEnum clusterAlgo : clusters) {
                        for (int parameterK : ks) {
                            evaluation(clusterAlgo.name()+";"+fsDescription[fsIndex]+";"+ataque, parameterK, clusterAlgo);
                        }
                    }
                    fsIndex = fsIndex +1;
            }
        }
    }
    

    static void evaluation(String description, int parameterK, ClusteringEnum clusterAlgo) throws Exception{
        /* Cria modelo */
        AbstractClusterer clustered = clusterData(getTest(), clusterAlgo, parameterK);
        ClusterResults[] clusterResults = new ClusterResults[parameterK];
        
        /* Identifica o Centroide por Cluster*/
        if(output == Output.DETAILED){          
            System.out.println("Classe;VP;VN;FP;FN");              
        } 
        
        if(clustered instanceof SimpleKMeans && centroidBased){
            handleKmeansByCentroid(description, parameterK, clustered, clusterResults);
        } else {
            handleAbstractClustering(description, parameterK, clustered, clusterResults, threshold);
        }                                 
        
    }
    
       
    private static double[] findCentroidClass(Instances centroids, boolean normalize) throws Exception {
       double[] centroidClasses = new double[centroids.size()];

       int numCluster = 0;
        for (Instance centroid : centroids){
            EuclideanDistance d = new EuclideanDistance(getTest());
            d.setDontNormalize(normalize);
            double minDistance = d.distance(centroid, getTest().get(0));
            double centroidClass = getLabeled().get(0).classValue();

            for (int i = 1; i <= getTest().size()-1;i++){
                Instance testingInstance = getTest().get(i);
                double testingDistane = d.distance(centroid, testingInstance);
                if (testingDistane < minDistance){
                    minDistance = testingDistane;
                    centroidClass = getLabeled().get(i).classValue();
                }
            }
            centroidClasses[numCluster] = centroidClass;
            numCluster++;
        }
        return centroidClasses;
    }
    
    public static BufferedReader readDataFile(String filename) {
        BufferedReader inputReader = null;

        try {
            inputReader = new BufferedReader(new FileReader(filename));
        } catch (FileNotFoundException ex) {
            System.err.println("File not found: " + filename);
        }

        return inputReader;
    }

    public static Instances getTest() {
        return test;
    }

    public static Instances getLabeled() {
        return labeled;
    }

    public Engine() {
    }

    private static void handleKmeansByCentroid(String description, int parameterK, AbstractClusterer clustered, ClusterResults[] clusterResults) throws Exception {
        SimpleKMeans kmeans = (SimpleKMeans) clustered;
          
        for (int eachK = 0; eachK < parameterK; eachK++) { 
            Instances centroid = kmeans.getClusterCentroids();

            /* Identifica a classe do centroide */
            double centroidClasses[] = findCentroidClass(centroid, Parameters.NORMALIZE);
            clusterResults[eachK] = new ClusterResults();
            clusterResults[eachK].setClusterNum(eachK);
            try{
                clusterResults[eachK].setCentroidClass(centroidClasses[eachK]);
            } catch (java.lang.ArrayIndexOutOfBoundsException e){
                break;
            }

              /* Calcula Acurácia do cluster */
            int[] assignments = kmeans.getAssignments();

            int i = 0;
            for(int clusterNum : assignments) {

                if(clusterNum == eachK){
                    if (centroidClasses[clusterNum] == labeled.get(i).classValue()){
                        if (centroidClasses[clusterNum] == normalClass){
                            clusterResults[clusterNum].addVn();
                        } else {
                            clusterResults[clusterNum].addVp();
                        }
                    } else {
                        if (centroidClasses[clusterNum] == normalClass){
                            clusterResults[clusterNum].addFn();
                        } else {
                             clusterResults[clusterNum].addFp();
                        }
                    }
                }
                i++;
            }

            if(output == Output.DETAILED){          
                System.out.println("Cluster "+clusterResults[eachK].getClusterNum()+";"
                    +clusterResults[eachK].getCentroidClass() + ";"
                    +clusterResults[eachK].getVp()+";"
                    +clusterResults[eachK].getVn()+";"
                    +clusterResults[eachK].getFp()+";"
                    +clusterResults[eachK].getFn()
                );              
            } 

        }
        
         if(output == Output.ACCURACY){          
             AlgoResults ag = new AlgoResults();
             for (ClusterResults c : clusterResults){
                 ag.addFn(c.getFn());
                 ag.addVn(c.getVn());
                 ag.addFp(c.getFp());
                 ag.addVp(c.getVp());
             }
                 
                System.out.println(
                    description+";"
                    + parameterK+";"
                    + ag.getVp()+";"
                    + ag.getVn()+";"
                    + ag.getFp()+";"
                    + ag.getFn()
                );              
            } 
    }

    private static void handleAbstractClustering(String description, int parameterK, AbstractClusterer clustered, ClusterResults[] clusterResults, double threshold) throws Exception {
        //FarthestFirst farthestFirst = (FarthestFirst) clustered;

        /* Inicializa */
        for (int i = 0; i < clusterResults.length; i++){
            clusterResults[i] = new ClusterResults();
            clusterResults[i].setClusterNum(i);
        }
        
        /* Contabiliza Ataques e Normais por Cluster */
        for (int i = 0; i < test.size(); i++){
            int k = clustered.clusterInstance(test.get(i));   

            if(labeled.get(i).classValue() == normalClass){
                clusterResults[k].addNormal();
            } else {
                clusterResults[k].addAttack();    
            }
        }
        
        /* Define classe do cluster */
        for (int i = 0; i < clusterResults.length; i++){
            double attackRate = (double) clusterResults[i].getAttacks()/ (double) clusterResults[i].getNormals();
            if(output == Output.DETAILED){          
               System.out.println("Cluster ["+i+"] - Attacks: "+clusterResults[i].getAttacks()+", Normals: "+clusterResults[i].getNormals()+", Rate: "+attackRate);
            }
            if(attackRate < threshold){
                clusterResults[i].setClusterClass(normalClass);
            } else {
                if (normalClass > 0){
                    clusterResults[i].setClusterClass(0);    
                } else {
                    clusterResults[i].setClusterClass(1);    
                }
            }
        }
        
        /* Computa Acurácia */
        for (int instanciaIndex = 0; instanciaIndex < test.size(); instanciaIndex++){
            int k = clustered.clusterInstance(test.get(instanciaIndex));   
            //System.out.println("Cluster "+k+", classe do cluster: "+clusterResults[k].clusterClass+", normal: "+normalClass+", classe da inst:"+labeled.get(instanciaIndex).classValue());
            if (clusterResults[k].getCentroidClass() == normalClass){
                if(labeled.get(instanciaIndex).classValue() == normalClass){            
                    clusterResults[k].addVn();
                } else{
                    clusterResults[k].addFn();
                }
            } else {
                if(labeled.get(instanciaIndex).classValue() == normalClass){            
                    clusterResults[k].addFp();
                } else{
                    clusterResults[k].addVp();
                }
            }
        }
        
         if(output == Output.DETAILED){          
                for (int eachK = 0; eachK < clusterResults.length; eachK++){
                    System.out.println("Cluster "+clusterResults[eachK].getClusterNum()+";"
                        +clusterResults[eachK].getCentroidClass() + ";"
                        +clusterResults[eachK].getVp()+";"
                        +clusterResults[eachK].getVn()+";"
                        +clusterResults[eachK].getFp()+";"
                        +clusterResults[eachK].getFn()
                    );              
                } 
            }
        
        if(output == Output.ACCURACY){          
            AlgoResults ag = new AlgoResults();
            for (ClusterResults c : clusterResults){
                ag.addFn(c.getFn());
                ag.addVn(c.getVn());
                ag.addFp(c.getFp());
                ag.addVp(c.getVp());
            }
                 
            System.out.println(
                description+";"
                + parameterK+";"
                + ag.getVp()+";"
                + ag.getVn()+";"
                + ag.getFp()+";"
                + ag.getFn()
            );              
        } 
    }
    
     public Instances[] splitInstance(Instances fullInstanceSet, double fator) {
        int trainSize = (int) Math.round(fullInstanceSet.numInstances() * fator / 100);
        int testSize = fullInstanceSet.numInstances() - trainSize;
        Instances train = new Instances(fullInstanceSet, 0, trainSize);
        Instances test = new Instances(fullInstanceSet, trainSize, testSize);
        Instances splitted[] = {train, test};
        return splitted;
    }

 }
