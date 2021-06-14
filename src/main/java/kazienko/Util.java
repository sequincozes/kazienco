/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kazienko;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;

import weka.clusterers.EM;
import weka.clusterers.FarthestFirst;
import weka.clusterers.SimpleKMeans;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

/**
 * @author silvio
 */
public class Util {

    public static void quickSort(FeatureAvaliada[] vetor, int inicio, int fim) {
        if (inicio < fim) {
            int posicaoPivo = separar(vetor, inicio, fim);
            quickSort(vetor, inicio, posicaoPivo - 1);
            quickSort(vetor, posicaoPivo + 1, fim);
        }
    }

    private static int separar(FeatureAvaliada[] vetor, int inicio, int fim) {
        FeatureAvaliada pivo = vetor[inicio];
        int i = inicio + 1, f = fim;
        while (i <= f) {
            if (vetor[i].getValorFeature() <= pivo.getValorFeature()) {
                i++;
            } else if (pivo.getValorFeature() < vetor[f].getValorFeature()) {
                f--;
            } else {
                FeatureAvaliada troca = vetor[i];
                vetor[i] = vetor[f];
                vetor[f] = troca;
                i++;
                f--;
            }
        }
        vetor[inicio] = vetor[f];
        vetor[f] = pivo;
        return f;
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

    public static Instances applyFilterKeep(Instances instances) {
        int[] fs = Parameters.FEATURE_SELECTION;
        Arrays.sort(fs);
        int deletadas = 0;
        for (int i = instances.numAttributes() - 1; i > 0; i--) {
            if (instances.numAttributes() <= fs.length) {
                System.err.println("O número de features (" + instances.numAttributes() + ") precisa ser maior que o filtro (" + fs.length + ").");
                return instances;
            }
            boolean deletar = true;
            for (int j : fs) {
                if (i == j) {
                    deletar = false;
//                    System.out.println("Manter [" + i + "]:" + instances.attribute(i));;
                }
            }
            if (deletar) {
                instances.deleteAttributeAt(i - 1);
            }
        }
        return instances;
    }

    public static Instances[] loadAndFilter(boolean printSelection) throws Exception {

        Instances trainInstances = new Instances(Util.readDataFile(Parameters.TRAIN_FILE));
        Instances normalInstances = new Instances(Util.readDataFile(Parameters.NORMAL_FILE));
        Instances testInstances = new Instances(Util.readDataFile(Parameters.TEST_FILE));

        /* Não-Supervisionado: K-Means */
        Instances evaluationInstancesNoLabel = new Instances(Util.readDataFile(Parameters.TRAIN_FILE));
        evaluationInstancesNoLabel.deleteAttributeAt(evaluationInstancesNoLabel.numAttributes() - 1); // Remove classe

        if (Parameters.FEATURE_SELECTION.length > 0) {
            trainInstances = Util.applyFilterKeep(trainInstances);
            normalInstances = Util.applyFilterKeep(normalInstances);
            testInstances = Util.applyFilterKeep(testInstances);
            if (printSelection) {
                System.out.print(Arrays.toString(Parameters.FEATURE_SELECTION) + " - ");
                System.out.print("trainInstances: " + trainInstances.size() + "; ");
                System.out.print("Normal: " + normalInstances.size() + "; ");
                System.out.println("testInstances: " + testInstances.size());
            }
            trainInstances.setClassIndex(trainInstances.numAttributes() - 1);
            normalInstances.setClassIndex(normalInstances.numAttributes() - 1);
            testInstances.setClassIndex(testInstances.numAttributes() - 1);

        }

        return new Instances[]{trainInstances, normalInstances, testInstances, evaluationInstancesNoLabel};

    }

    public static Instances[] loadAndFilterUnsupervised(boolean printSelection) throws Exception {

        Instances trainInstances = new Instances(Util.readDataFile(Parameters.TRAIN_FILE));
//        Instances trainInstancesLabeled = new Instances(Util.readDataFile(Parameters.TRAIN_FILE));
        Instances trainInstancesLabeled = new Instances(Util.readDataFile(Parameters.TEST_FILE));
        Instances normalInstances = new Instances(Util.readDataFile(Parameters.NORMAL_FILE));
        Instances testInstances = new Instances(Util.readDataFile(Parameters.TEST_FILE));


        if (Parameters.FEATURE_SELECTION.length > 0) {
            trainInstances = Util.applyFilterKeep(trainInstances);
            normalInstances = Util.applyFilterKeep(normalInstances);
            testInstances = Util.applyFilterKeep(testInstances);
            if (printSelection) {
                System.out.print(Arrays.toString(Parameters.FEATURE_SELECTION) + " - ");
                System.out.println("trainInstances: " + trainInstances.numAttributes());
                System.out.print("evaluationInstances: " + normalInstances.numAttributes());
                System.out.print("testInstances: " + testInstances.numAttributes());
            }
//            trainInstances.setClassIndex(trainInstances.numAttributes() - 1);
//            normalInstances.setClassIndex(normalInstances.numAttributes() - 1);
//            testInstances.setClassIndex(testInstances.numAttributes() - 1);
            trainInstancesLabeled.setClassIndex(trainInstancesLabeled.numAttributes() - 1);

        }

        /* Não-Supervisionado: K-Means */
        trainInstances.deleteAttributeAt(trainInstances.numAttributes() - 1); // Remove classe
        normalInstances.deleteAttributeAt(normalInstances.numAttributes() - 1); // Remove classe
        testInstances.deleteAttributeAt(testInstances.numAttributes() - 1); // Remove classe

        return new Instances[]{trainInstances, normalInstances, testInstances, trainInstancesLabeled};

    }

    public static Instances[] loadAndFilterUnsupervised2021Test(boolean printSelection) throws Exception {

        Instances trainInstances = new Instances(Util.readDataFile(Parameters.DATASET));
        Instances trainInstancesLabeled = new Instances(trainInstances);
        Instances testInstances = new Instances(Util.readDataFile(Parameters.DATASET));
        Instances testInstancesLabeled = new Instances(testInstances);


        if (Parameters.FEATURE_SELECTION.length > 0) {
            trainInstances = Util.applyFilterKeep(trainInstances);
            testInstances = Util.applyFilterKeep(testInstances);
            if (printSelection) {
                System.out.print(Arrays.toString(Parameters.FEATURE_SELECTION) + " - ");
                System.out.println("trainInstances: " + trainInstances.numAttributes());
                System.out.print("testInstances: " + testInstances.numAttributes());
            }
            trainInstancesLabeled.setClassIndex(trainInstancesLabeled.numAttributes() - 1);
            testInstancesLabeled.setClassIndex(trainInstancesLabeled.numAttributes() - 1);

        }

        /* Não-Supervisionado: K-Means */
        trainInstances.deleteAttributeAt(trainInstances.numAttributes() - 1); // Remove classe
        testInstances.deleteAttributeAt(testInstances.numAttributes() - 1); // Remove classe

        return new Instances[]{trainInstances, testInstances, trainInstancesLabeled, testInstancesLabeled};

    }


    public static Instances[] loadAndFilterUnsupervised2021(boolean printSelection) throws Exception {

        Instances trainInstances = new Instances(Util.readDataFile(Parameters.TRAIN_FILE));
        Instances trainInstancesLabeled = new Instances(trainInstances);
        Instances testInstances = new Instances(Util.readDataFile(Parameters.TEST_FILE));
        Instances testInstancesLabeled = new Instances(testInstances);


        if (Parameters.FEATURE_SELECTION.length > 0) {
            trainInstances = Util.applyFilterKeep(trainInstances);
            testInstances = Util.applyFilterKeep(testInstances);
            if (printSelection) {
                System.out.print(Arrays.toString(Parameters.FEATURE_SELECTION) + " - ");
                System.out.println("trainInstances: " + trainInstances.numAttributes());
                System.out.print("testInstances: " + testInstances.numAttributes());
            }
            trainInstancesLabeled.setClassIndex(trainInstancesLabeled.numAttributes() - 1);
            testInstancesLabeled.setClassIndex(trainInstancesLabeled.numAttributes() - 1);

        }

        /* Não-Supervisionado: K-Means */
        trainInstances.deleteAttributeAt(trainInstances.numAttributes() - 1); // Remove classe
        testInstances.deleteAttributeAt(testInstances.numAttributes() - 1); // Remove classe

        return new Instances[]{trainInstances, testInstances, trainInstancesLabeled, testInstancesLabeled};

    }

    public static Instances[] readUnsupervisedAndLabels(boolean printSelection) throws Exception {
        Instances instances = new Instances(Util.readDataFile(Parameters.DATASET));
        Instances instancesLabeled = new Instances(instances);

        if (Parameters.FEATURE_SELECTION.length > 0) {
            instances = Util.applyFilterKeep(instances);
            if (printSelection) {
                System.out.print(Arrays.toString(Parameters.FEATURE_SELECTION) + " - ");
                System.out.println("trainInstances: " + instances.numAttributes());
            }
        }

        instancesLabeled.setClassIndex(instancesLabeled.numAttributes() - 1);
        instances.deleteAttributeAt(instances.numAttributes() - 1); // Remove classe

        return new Instances[]{instances, instancesLabeled};

    }

    public static Instances[] readUnsupervisedAndLabelsMultiple(boolean printSelection) throws Exception {
        Instances train = new Instances(Util.readDataFile(Parameters.FOLD1));
        Instances fold2 = new Instances(Util.readDataFile(Parameters.FOLD2));
        Instances fold3 = new Instances(Util.readDataFile(Parameters.FOLD3));
        Instances fold4 = new Instances(Util.readDataFile(Parameters.FOLD4));
        Instances folds1to4 = new Instances(train);
        folds1to4.addAll(fold2);
        folds1to4.addAll(fold3);
        folds1to4.addAll(fold4);
        Instances test = new Instances(Util.readDataFile(Parameters.FOLD5));

        if (Parameters.FEATURE_SELECTION.length > 0) {
            folds1to4 = Util.applyFilterKeep(folds1to4);
            test = Util.applyFilterKeep(test);
            if (printSelection) {
                System.out.print(Arrays.toString(Parameters.FEATURE_SELECTION) + " - ");
                System.out.println("trainInstances: " + test.numAttributes());
            }
        }
        Instances labeledTrain = new Instances(folds1to4);
        train = new Instances(folds1to4);
        Instances labeledTest = new Instances(test);

        labeledTrain.setClassIndex(test.numAttributes() - 1);
        labeledTest.setClassIndex(test.numAttributes() - 1);
        train.deleteAttributeAt(train.numAttributes() - 1); // Remove classe
        test.deleteAttributeAt(test.numAttributes() - 1); // Remove classe

        if (Parameters.BALANCE) {

            /* Get Attributes */
            ArrayList<Attribute> attrs = new ArrayList<>();
            for (int k = 0; k < train.numAttributes(); k++) {
                attrs.add(train.attribute(k));
            }

            /* Save attacks only */
            Instances attacksOnlyLabeled = new Instances("attacksOnlyLabeled", attrs, train.size());
            Instances attacksOnly = new Instances("attacksOnly", attrs, train.size());
            for (int i = 0; i < train.size(); i++) {
                Instance instance = train.get(i);
                Instance instanceLabeled = labeledTrain.get(i);
                if (instanceLabeled.classValue() > 0) {
                    attacksOnly.add(instance);
                    attacksOnlyLabeled.add(instanceLabeled);
                }
            }

            Instances trainLabeledBalanced = new Instances("trainLabeledBalanced", attrs, train.size());
            trainLabeledBalanced.setClassIndex(trainLabeledBalanced.numAttributes()-1);
            Instances trainBalanced = new Instances("trainLabeledBalanced", attrs, train.size());

            for (int i = 0; i < train.size(); i++) {
                Instance instance = train.get(i);
                Instance instanceLabeled = labeledTrain.get(i);
                if (instanceLabeled.classValue() == 0) {
                    if (trainLabeledBalanced.size() < attacksOnly.size()) {
                        trainLabeledBalanced.add(instanceLabeled);
                        trainBalanced.add(instance);
                    } else {
                        break;
                    }
                }
            }
            trainLabeledBalanced.addAll(attacksOnlyLabeled);
            trainBalanced.addAll(attacksOnly);
            System.out.println("Size balanced: " + trainBalanced.size() + "(" + attacksOnly.size() + " attacks)");
            return new Instances[]{trainBalanced, test, trainLabeledBalanced, labeledTest};
        }

        //Instances train, Instances test, Instances trainLabeled, Instances testLabeled
        if (false) {
            System.out.println("READING...");
            System.out.println("Train: " + train.size() + " instances.");
            System.out.println("Test: " + test.size() + " instances.");
            System.out.println("Train Labeled: " + labeledTrain.size() + " instances.");
            System.out.println("Test Labeled: " + labeledTest.size() + " instances.");
        }
        return new Instances[]{train, test, labeledTrain, labeledTest};


    }

    public static SimpleKMeans clusterData(Instances evaluation, int k) throws Exception {
        SimpleKMeans kmeans = new SimpleKMeans();
        kmeans.setSeed(k);
        kmeans.setPreserveInstancesOrder(true);
        kmeans.setNumClusters(k);
        kmeans.buildClusterer(evaluation);
        return kmeans;

    }

    public static FarthestFirst clusterDataFarthest(Instances evaluation, int k) throws Exception {
        FarthestFirst kmeans = new FarthestFirst();
        kmeans.setSeed(k);
//        kmeans.setPreserveInstancesOrder(true);
        kmeans.setNumClusters(k);
        kmeans.buildClusterer(evaluation);
        return kmeans;

    }

    public static EM clusterDataEM(Instances evaluation, int k) throws Exception {
        EM kmeans = new EM();
        kmeans.setSeed(k);
//        kmeans.setPreserveInstancesOrder(true);
        kmeans.setNumClusters(k);
        kmeans.buildClusterer(evaluation);
        return kmeans;

    }
}
