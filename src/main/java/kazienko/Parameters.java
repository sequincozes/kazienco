/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kazienko;

import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.lazy.IBk;
import weka.classifiers.trees.J48;
//import weka.classifiers.trees.NBTree;
import weka.classifiers.trees.REPTree;
import weka.classifiers.trees.RandomForest;
import weka.classifiers.trees.RandomTree;

/**
 *
 * @author silvio
 */
public class Parameters {

    public static int K = 0;
    public static String FS_NAME = "";
    public static  String FOLD1 = "/home/silvio/IdeaProjects/kazienco/datasets/clustering2021/folds/normal_blackhole_5folds-0.csv";
    public static  String FOLD2 = "/home/silvio/IdeaProjects/kazienco/datasets/clustering2021/folds/normal_blackhole_5folds-1.csv";
    public static  String FOLD3 = "/home/silvio/IdeaProjects/kazienco/datasets/clustering2021/folds/normal_blackhole_5folds-2.csv";

    public static  String FOLD4 = "/home/silvio/IdeaProjects/kazienco/datasets/clustering2021/folds/normal_blackhole_5folds-3.csv";
    public static  String FOLD5 = "/home/silvio/IdeaProjects/kazienco/datasets/clustering2021/folds/normal_blackhole_5folds-4.csv";
    public static  String DIRETORIO = "/home/silvio/IdeaProjects/kazienco/datasets/";


    public static String CLUSTER_KMEANS = "KMEANS";
    public static String CLUSTER_FF = "FF";
    public static String CLUSTER_EM = "EM";

    public static String CLUSTER_ALGORITHM = "KMEANS";
    public static boolean BALANCE = false;
    public static String DATASET = "";
    public static String ATAQUE = "flooding";

    public static String ataquePath = DIRETORIO + ATAQUE + "/";

    public static String WSN_CLUSTERING_FLOODING_TEST = "/home/silvio/datasets/wsn-ds/clustering2021/normal_flooding_20percent_test.csv";
    public static String WSN_CLUSTERING_GRAYHOLE_TEST = "/home/silvio/datasets/wsn-ds/clustering2021/normal_grayhole_20percent_test.csv";
    public static String WSN_CLUSTERING_BLACKHOLE_TEST = "/home/silvio/datasets/wsn-ds/clustering2021/normal_blackhole_20percent_test.csv";
    public static String WSN_CLUSTERING_FLOODING_TRAIN = "/home/silvio/datasets/wsn-ds/clustering2021/normal_flooding_80percent_train.csv";
    public static String WSN_CLUSTERING_GRAYHOLE_TRAIN = "/home/silvio/datasets/wsn-ds/clustering2021/normal_grayhole_80percent_train.csv";
    public static String WSN_CLUSTERING_BLACKHOLE_TRAIN = "/home/silvio/datasets/wsn-ds/clustering2021/normal_blackhole_80percent_train.csv";


    public static final String SEPARATOR = "/";
//    public static final String FILE_TRAIN = DIRETORIO + SEPARATOR + "10_train_files" + SEPARATOR + "compilado_train.csv"; //treino_binario_1000_1000
//    public static final String FILE_EVALUATION = DIRETORIO + SEPARATOR + "10_evaluation_files" + SEPARATOR + "compilado_evaluation.csv"; //ataque_binario_10k
//    public static final String FILE_TEST = DIRETORIO + SEPARATOR + "80_test_files" + SEPARATOR + "compilado_test.csv"; //ataque_binario_10k
    static String TRAIN_FILE = ataquePath + "train_5.csv";
    static String TEST_FILE = ataquePath + "test_95.csv";
    static String NORMAL_FILE = ataquePath + "normal_test_95.csv";
    public static final ClassifierExtended RANDOM_TREE = new ClassifierExtended(true, new RandomTree(), "RandomTree");
    public static final ClassifierExtended RANDOM_FOREST = new ClassifierExtended(true, new RandomForest(), "RandomForest");
    public static final ClassifierExtended NAIVE_BAYES = new ClassifierExtended(true, new NaiveBayes(), "NaiveBayes");
    public static final ClassifierExtended REP_TREE = new ClassifierExtended(true, new REPTree(), "REPTree");
    public static final ClassifierExtended J48 = new ClassifierExtended(true, new J48(), "J48");
    public static final ClassifierExtended KNN = new ClassifierExtended(true, new IBk(), "KNN");
//    public static final ClassifierExtended NBTREE = new ClassifierExtended(true, new NBTree(), "NBTree");

    // Run Settings
    public static final ClassifierExtended[] CLASSIFIERS_FOREACH = {RANDOM_TREE, REP_TREE, NAIVE_BAYES, RANDOM_FOREST, J48};// RANDOM_FOREST, J48 está fora
    public static final boolean TEST_NORMALS = true;
    public static final boolean TEST_ATTACKS = true;
    public static final int TOTAL_FEATURES = 19;
    public static  boolean NORMALIZE = false;

    //Selected by GR
//    static int[] FEATURE_SELECTION = new int[]{53, 5, 64, 40, 7, 70, 9, 54, 41, 42, 43, 67, 35, 49, 6, 66, 13, 55, 11, 1};
    //Selected by OneR
    static int[] OneR5WSN = new int[]{18, 7, 6, 1, 4};
    static int[] IG5WSN = new int[]{18, 6, 3, 7, 13};
    static int[] GR5WSN = new int[]{3, 6, 10, 9, 13};
    static int[] OneR18WSN = new int[]{18, 7, 6, 1, 4, 2, 3, 5, 10, 11, 12, 13, 15, 16, 17, 14, 9, 8};//
    static int[] IG18WSN = new int[]{18, 6, 3, 7, 13, 12, 8, 17, 5, 10, 9, 14, 4, 1, 11, 15, 16, 2};
    static int[] GR18WSN = new int[]{3, 6, 10, 9, 13, 18, 7, 12, 8, 17, 5, 11, 15, 16, 14, 4, 1, 2};
    static int[] OneR10WSN = new int[]{18, 7, 6, 1, 4, 2, 3, 5, 10, 11};//, 12, 13, 15, 16, 17, 14, 9, 8};//
    static int[] IG10WSN = new int[]{18, 6, 3, 7, 13, 12, 8, 17, 5, 10};//, 9, 14, 4, 1, 11, 15, 16, 2};
    static int[] GR10WSN = new int[]{3, 6, 10, 9, 13, 18, 7, 12, 8, 17};//, 5, 11, 15, 16, 14, 4, 1, 2};

    static int[] FEATURE_SELECTION = GR5WSN;

    static void changeAttack(String ataque) {
        ATAQUE = ataque;
        ataquePath = DIRETORIO + ATAQUE + "/";
        TRAIN_FILE = ataquePath + "train_5.csv";
        TEST_FILE = ataquePath + "test_95.csv";
        NORMAL_FILE = ataquePath + "normal_test_95.csv";
    }

}
