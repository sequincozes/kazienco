/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kazienko;

import java.util.ArrayList;

import clusteringComparison.ClusterResults;
import weka.classifiers.Classifier;
import weka.clusterers.EM;
import weka.clusterers.FarthestFirst;
import weka.clusterers.SimpleKMeans;
;

import weka.core.Instance;
import weka.core.Instances;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Instance;
import weka.core.Instances;

/**
 * @author silvio'
 */


public class BemSimples_Kmeans {

    static Instances[] allInstances;
    static Classifier selectedClassifier = Parameters.NAIVE_BAYES.getClassifier();
    static boolean rawOutput = false;
    static boolean debug = false;

    public static void main(String[] args) throws Exception {
//        oldMethod2019();
        Parameters.TRAIN_FILE = Parameters.WSN_CLUSTERING_BLACKHOLE_TRAIN;
        Parameters.TEST_FILE = Parameters.WSN_CLUSTERING_BLACKHOLE_TEST;
        allInstances = Util.loadAndFilterUnsupervised2021(false);
//        Instances train, Instances test, Instances trainLabeled, Instances testLabeled
        System.out.println("KMeans");
        System.out.println("vp" + ";" + "vn" + ";" + "fp" + ";" + "fn");
        for (int k = 2; k < 30; k++) {
            ads2021(allInstances[0], allInstances[1], allInstances[2], allInstances[3], k);
        }
    }

    private static void oldMethod2019() {
//                allInstances = Util.loadAndFilterUnsupervised(false);
//        Instances[]{trainInstances, normalInstances, testInstances, trainInstancesLabeled};
//        ads(allInstances[0], allInstances[1], allInstances[2], allInstances[3]);
    }


    public static void ads2021(Instances train, Instances test, Instances trainLabeled, Instances testLabeled, int k) throws Exception {

        if (false) {
            System.out.println("Train: " + train.size() + " instances.");
            System.out.println("Test: " + test.size() + " instances.");
            System.out.println("Train Labeled: " + trainLabeled.size() + " instances.");
            System.out.println("Test Labeled: " + testLabeled.size() + " instances.");
        }

        // Criação do cluster (treino)
        ClusterResults[] results = new ClusterResults[k];
        SimpleKMeans kmeas = Util.clusterData(train, k); // Trocar
//        FarthestFirst kmeas = Util.clusterDataFarthest(train, k); // Trocar
//        EM kmeas = Util.clusterDataEM(train, k); // Trocar

        /* TREINO */
        for (int i = 0; i < k; i++) {
            results[i] = new ClusterResults();
        }

//        System.out.println(" ** TRAIN ** ");
        for (int i = 0; i < train.numInstances(); i++) {
//            System.out.println(i + "-" + labeled.instance(i).classValue() + " = " + labeled.instance(i).toString());
            int clusterNum = kmeas.clusterInstance(train.instance(i));
            if (trainLabeled.instance(i).classValue() > 0) {
                results[clusterNum].addAttack(); // Suponha que >0 é ataque (1)
            } else {
                results[clusterNum].addNormal(); // Suponha que zero é normal
            }
        }

//        System.out.println(" ** TEST ** ");
        for (int i = 0; i < test.numInstances(); i++) {
//            System.out.println(i + "-" + labeled.instance(i).classValue() + " = " + labeled.instance(i).toString());
            int clusterNum = kmeas.clusterInstance(train.instance(i));
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
//        System.out.println("K (VP: " + vp + "|" + " VN: " + vn + "|" + " FP: " + fp + "|" + " FN: " + fn + ")");
        System.out.println(k + ";"+vp + ";" + vn + ";" + fp + ";" + fn);
    }


    public static void ads(Instances treino, Instances testeNormal, Instances teste, Instances labeled) throws Exception {
        System.out.println("Treino: " + treino.size() + " instâncias.");
        System.out.println("Teste Normal: " + testeNormal.size() + " instâncias.");
        System.out.println("Teste Ataque: " + teste.size() + " instâncias.");
        System.out.println("Treino Label: " + labeled.size() + " instâncias.");

        // Criação do cluster (treino)
        int k = 5;
        int[][] results = new int[k][3];
        SimpleKMeans farthest = Util.clusterData(treino, k);

        /* TREINO */
        for (int i = 0; i < k; i++) {
            results[i][0] = 0; // Total
            results[i][1] = 0; // Normal (Supondo que 0 é normal)
            results[i][2] = 0; // Attacks (Supondo que 1 é ataque)
        }

        System.out.println(" ** TREINO ** ");
        for (int i = 0; i < treino.numInstances(); i++) {
//            System.out.println(i + "-" + labeled.instance(i).classValue() + " = " + labeled.instance(i).toString());
            int clusterNum = farthest.clusterInstance(treino.instance(i));
            results[clusterNum][0] = results[clusterNum][0] + 1;
            if (labeled.instance(i).classValue() > 0) {
                results[clusterNum][2] = results[clusterNum][2] + 1; // Suponha que >0 é ataque (1)
//                System.out.println(i+"-"+labeled.instance(i).classValue()+" = "+labeled.instance(i).toString());
            } else {
                results[clusterNum][1] = results[clusterNum][1] + 1; // Suponha que zero é normal
            }
        }
        for (int i = 0; i < k; i++) {
            System.out.println("Cluster " + i + "; " + results[i][0] + ";" + results[i][1] + ";" + results[i][2]);
        }

        /* TESTE */
        for (int i = 0; i < k; i++) {
            results[i][0] = 0; // Total
            results[i][1] = 0; // Normal
            results[i][2] = 0; // Attacks
        }

        if (true) {
            System.out.println(" ** TESTE ** ");

            //Remover esse for até 3 (ERA SÓ PRA CALCULAR TEMPO)
//            for (int temp = 0; temp < 3; temp++) {

            for (int i = 0; i < teste.numInstances(); i++) {
                long timeBefore = System.nanoTime();
                int clusterNum = farthest.clusterInstance(teste.instance(i));
                long timeAfter = System.nanoTime();
                System.out.println(timeAfter - timeBefore);
                results[clusterNum][0] = results[clusterNum][0] + 1;
            }
//            }
            for (int i = 0; i < k; i++) {
                System.out.println("Cluster " + i + "; " + results[i][0]);// ";" + results[i][1] + ";" + results[i][2]);
            }
            /* TESTE NORMAL*/
            for (int i = 0; i < k; i++) {
                results[i][0] = 0; // Total
                results[i][1] = 0; // Normal
                results[i][2] = 0; // Attacks
            }
            System.out.println(" ** TESTE NORMAL ** ");
            for (int i = 0; i < testeNormal.numInstances(); i++) {
                int clusterNum = farthest.clusterInstance(testeNormal.instance(i));
                results[clusterNum][0] = results[clusterNum][0] + 1;
            }
            for (int i = 0; i < k; i++) {
                System.out.println("Cluster " + i + "; " + results[i][0]);//+ ";" + results[i][1] + ";" + results[i][2]);
            }
        }
    }

    public static void testeComClustering(SimpleKMeans kmeans, Instances instancias) throws Exception {
        Resultado finalResult = new Resultado("FinalResult", 0, 0, 0, 0);

        /* Prepare Header Conflicting Results */
        System.out.print("Instância,Cluster,Classe,");
        for (ClassifierExtended conflicting : Parameters.CLASSIFIERS_FOREACH) {
            if (conflicting.equals(Parameters.CLASSIFIERS_FOREACH[Parameters.CLASSIFIERS_FOREACH.length - 1])) {
                System.out.print(conflicting.getClassifierName());
            } else {
                System.out.print(conflicting.getClassifierName() + ",");
            }
        }
        System.out.println("");
        for (int i = 0; i < instancias.numInstances(); i++) {
            String outputConflitos = "";
            Instance testando = instancias.instance(i);

            /* Prepare Conflicting Results */
            Instance tempInstance = instancias.instance(i);
//            tempInstance.isMissing(tempInstance.numAttributes() - 1);

            int clusterNum = kmeans.clusterInstance(tempInstance);
//            System.out.println("Cluster:" + clusterNum + "tempInstance: " + tempInstance);
            outputConflitos = outputConflitos + i + "," + clusterNum + "," + testando.classValue() + ",";

            ArrayList<ClassifierExtended> errados = new ArrayList();
            ArrayList<ClassifierExtended> certos = new ArrayList();
            for (ClassifierExtended tempClassifier : Parameters.CLASSIFIERS_FOREACH) {
                selectedClassifier = tempClassifier.getClassifier();
                double res1 = selectedClassifier.classifyInstance(testando);
                tempClassifier.setTempDecision(res1);
                if (res1 != testando.classValue()) {
                    errados.add(tempClassifier);
                } else {
                    certos.add(tempClassifier);
                }

                /* Prepare Conflicting Results */
                if (tempClassifier.equals(Parameters.CLASSIFIERS_FOREACH[Parameters.CLASSIFIERS_FOREACH.length - 1])) {
                    outputConflitos = outputConflitos + tempClassifier.getTempDecision();
                } else {
                    outputConflitos = outputConflitos + tempClassifier.getTempDecision() + ",";
                }
            }

            boolean existemErros = (certos.size() < Parameters.CLASSIFIERS_FOREACH.length);
            boolean existemAcertos = (errados.size() < Parameters.CLASSIFIERS_FOREACH.length);
            if (existemAcertos && existemErros) {
                System.out.println(outputConflitos);
//                System.out.println("Total: " + Parameters.CLASSIFIERS_FOREACH.length + ", Certos: " + certos.size() + "/" + Parameters.CLASSIFIERS_FOREACH.length + ", Errados: " + errados.size());
                if (debug) {
                    for (ClassifierExtended conflicting : certos) {
                        System.out.println(conflicting.getClassifierName() + " [CERTO] = " + conflicting.getTempDecision());
                    }
                }
                if (debug) {
                    for (ClassifierExtended conflicting : errados) {
                        System.out.println(conflicting.getClassifierName() + " [ERRADO] = " + conflicting.getTempDecision());
                    }
                }
            }

        }

    }

    public static void updateClusterResult(Resultado[] clustersResults, int cluster, Instance instance, double resultingClasss) {
        Resultado r = clustersResults[cluster];
        if (resultingClasss == instance.classValue() && instance.classValue() == 0) {
            r.setVN(r.getVN() + 1);
        } else if (resultingClasss == instance.classValue() && instance.classValue() == 1) {
            r.setVP(r.getVP() + 1);
        } else if (resultingClasss != instance.classValue() && instance.classValue() == 0) {
            r.setFP(r.getFP() + 1);
        } else if (resultingClasss != instance.classValue() && instance.classValue() == 1) {
            r.setFN(r.getFN() + 1);
        } else {
            System.err.println("Classe estranha: " + resultingClasss);
        }

    }

    public static void semClustering() throws Exception {
        /* SEM CLUSTERING */
        if (true) {
            for (ClassifierExtended CLASSIFIERS_FOREACH : Parameters.CLASSIFIERS_FOREACH) {
                selectedClassifier = CLASSIFIERS_FOREACH.getClassifier();
                Resultado rs = avaliaEssaGalera(CLASSIFIERS_FOREACH.getClassifierName(), allInstances[0], allInstances[1], allInstances[2]);
//            System.out.println(String.valueOf(rs.getCx() + " => " + " | Acurácia: " + rs.getAcuracia() + "Alarme Falso: " + rs.getTaxaAlarmeFalsos() + "Detecção: " + rs.getTaxaDeteccao() + "VP: " + rs.getVP() + ", VN: " + rs.getVN() + ", FN: " + rs.getFN() + ", FP: " + rs.getFP()));
//                System.out.println(String.valueOf(rs.getCx() + ";" + String.valueOf(rs.getAcuracia()).replace(".", ",") + "%" + ";" + String.valueOf(rs.getTaxaAlarmeFalsos()).replace(".", ",") + "%" + ";" + String.valueOf(rs.getTaxaDeteccao()).replace(".", ",") + "%" + ";" + rs.getVP() + ";" + rs.getVN() + ";" + rs.getFN() + ";" + rs.getFP()));
                System.out.println(String.valueOf(rs.getCx() + ";" + String.valueOf(rs.getAcuracia()).replace(".", ",") + ";" + String.valueOf(rs.getTime()).replace(".", ",") + ";" + String.valueOf(rs.getTaxaAlarmeFalsos()).replace(".", ",") + "%" + ";" + String.valueOf(rs.getTaxaDeteccao()).replace(".", ",") + "%" + ";" + rs.getVP() + ";" + rs.getVN() + ";" + rs.getFN() + ";" + rs.getFP()));

            }
        } else {
            int i = 2;
            selectedClassifier = Parameters.CLASSIFIERS_FOREACH[i].getClassifier();
            Resultado rs = avaliaEssaGalera(Parameters.CLASSIFIERS_FOREACH[i].getClassifierName(), allInstances[0], allInstances[1], allInstances[2]);
            System.out.println(String.valueOf(rs.getCx() + ";" + String.valueOf(rs.getAcuracia()).replace(".", ",") + ";" + String.valueOf(rs.getTime()).replace(".", ",") + ";" + String.valueOf(rs.getTaxaAlarmeFalsos()).replace(".", ",") + "%" + ";" + String.valueOf(rs.getTaxaDeteccao()).replace(".", ",") + "%" + ";" + rs.getVP() + ";" + rs.getVN() + ";" + rs.getFN() + ";" + rs.getFP()));

        }

    }

    public static Resultado avaliaEssaGalera(String descricao, Instances treino, Instances normal, Instances teste) throws Exception {
        selectedClassifier.buildClassifier(treino);
        long tempoTotal = 0;
        // Resultados
        double acuracia = 0;
        double txDec = 0;
        double txAFal = 0;
        int VP = 0;
        int VN = 0;
        int FP = 0;
        int FN = 0;
        float txTempo = 0;

        for (int i = 0; i < normal.size(); i++) {
            teste.add(normal.get(i));
        }

        // Validação de ataques
//        System.out.println(" *** Ataques *** ");
        for (int i = 0; i < teste.numInstances() - 1; i++) {
            Instance testando = teste.instance(i);
            long timeAntes = System.currentTimeMillis();
            double res1 = selectedClassifier.classifyInstance(testando);
            long timeDepois = System.currentTimeMillis();
            tempoTotal = tempoTotal + timeDepois - timeAntes;
            if (res1 == testando.classValue() && res1 < 1) {
                VN = VN + 1;
            } else if (res1 != testando.classValue() && res1 < 1) {
                FN = FN + 1;
                if (debug) {
                    System.out.println("Errou: " + "[" + i + " = " + testando.classValue() + "] res: " + res1);
                }
            } else if (res1 == testando.classValue() && res1 > 0) {
                VP = VP + 1;
            } else {
                FP = FP + 1;
                if (debug) {
                    System.out.println("Errou: " + "[" + i + " = " + testando.classValue() + "] res: " + res1);
                }
            }
            try {

                acuracia = Float.valueOf(((VP + VN)) * 100) / Float.valueOf((VP + VN + FP + FN));
                txDec = Float.valueOf((VP * 100)) / Float.valueOf((VP + FN));  // Sensitividade ou Taxa de Detecção
                txAFal = Float.valueOf((FP * 100)) / Float.valueOf((VN + FP)); // Especificade ou Taxa de Alarmes Falsos  
                txTempo = Float.valueOf(tempoTotal) * 1000 / Float.valueOf(VP + VN + FP + FN);
            } catch (java.lang.ArithmeticException e) {
                System.out.println("Divisão por zero ((" + VP + " + " + VN + ") * 100) / (" + VP + " + " + VN + "+ " + FP + "+" + FN + "))");
            }
        }
        Resultado r = new Resultado(descricao, VP, FN, VN, FP, acuracia, txDec, txAFal, txTempo);
        return r;

    }

    public static Resultado testaEssaGalera(String descricao, int begin, int end) throws Exception {
        selectedClassifier.buildClassifier(allInstances[0]);

        // Resultados
        double acuracia = 0;
        double txDec = 0;
        double txAFal = 0;
        int VP = 0;
        int VN = 0;
        int FP = 0;
        int FN = 0;
        long time = System.nanoTime();
        // Validação de ataques
//        System.out.println(" *** Ataques *** ");
        for (int i = begin; i < end; i++) {
            Instance testando = allInstances[1].instance(i);
            double res1 = selectedClassifier.classifyInstance(testando);
            if (res1 == testando.classValue()) {
                VP = VP + 1;
            } else {
                FN = FN + 1;
            }
        }

        // Validação de normais
//        System.out.println(" *** Normais *** ");
        for (int i = begin; i < end; i++) {
            Instance testando2 = allInstances[2].instance(i);
            double res2 = selectedClassifier.classifyInstance(testando2);
            if (res2 == testando2.classValue()) {
                VN = VN + 1;
            } else {
                FP = FP + 1;
            }
        }
        long timeEnd = (System.nanoTime() - time) / 1000;

        try {

            acuracia = Float.valueOf(((VP + VN)) * 100) / Float.valueOf((VP + VN + FP + FN));
            txDec = Float.valueOf((VP * 100)) / Float.valueOf((VP + FN));  // Sensitividade ou Taxa de Detecção
            txAFal = Float.valueOf((FP * 100)) / Float.valueOf((VN + FP)); // Especificade ou Taxa de Alarmes Falsos    
        } catch (java.lang.ArithmeticException e) {
            System.out.println("Divisão por zero ((" + VP + " + " + VN + ") * 100) / (" + VP + " + " + VN + "+ " + FP + "+" + FN + "))");
        }
        Resultado r = new Resultado(descricao, VP, FN, VN, FP, acuracia, txDec, txAFal, timeEnd);
        return r;

    }

    //Instances[]{trainInstances, testAttackInstances, testNormalInstances};
    public static Resultado testaEssaGaleraRetroalimentando(String descricao, int begin, int end) throws Exception {
        selectedClassifier.buildClassifier(allInstances[0]);

        // Resultados
        double acuracia = 0;
        double txDec = 0;
        double txAFal = 0;
        int VP = 0;
        int VN = 0;
        int FP = 0;
        int FN = 0;

        // Validação de ataques
//        System.out.println(" *** Ataques *** ");
        for (int i = begin; i < end; i++) {
            Instance testando = allInstances[1].instance(i);
            double res1 = selectedClassifier.classifyInstance(testando);
            if (res1 == testando.classValue()) {
                VP = VP + 1;
            } else {
                FN = FN + 1;
            }
            testando.setClassValue(res1);
            allInstances[0].add(testando);
        }

        // Validação de normais
//        System.out.println(" *** Normais *** ");
        for (int i = begin; i < end; i++) {
            Instance testando2 = allInstances[2].instance(i);
            double res2 = selectedClassifier.classifyInstance(testando2);
            if (res2 == testando2.classValue()) {
                VN = VN + 1;
            } else {
                FP = FP + 1;
            }

            //retroalimentação
            testando2.setClassValue(res2);
            allInstances[0].add(testando2);

        }

        try {

            acuracia = Float.valueOf(((VP + VN)) * 100) / Float.valueOf((VP + VN + FP + FN));
            txDec = Float.valueOf((VP * 100)) / Float.valueOf((VP + FN));  // Sensitividade ou Taxa de Detecção
            txAFal = Float.valueOf((FP * 100)) / Float.valueOf((VN + FP)); // Especificade ou Taxa de Alarmes Falsos    
        } catch (java.lang.ArithmeticException e) {
            System.out.println("Divisão por zero ((" + VP + " + " + VN + ") * 100) / (" + VP + " + " + VN + "+ " + FP + "+" + FN + "))");
        }
        Resultado r = new Resultado(descricao, VP, FN, VN, FP, acuracia, txDec, txAFal);
        return r;

    }

    //Instances[]{trainInstances, testAttackInstances, testNormalInstances};
    public static Resultado testaEssaGaleraRetroalimentandoPerfeitamente(String descricao, int begin, int end) throws Exception {
        selectedClassifier.buildClassifier(allInstances[0]);

        // Resultados
        double acuracia = 0;
        double txDec = 0;
        double txAFal = 0;
        int VP = 0;
        int VN = 0;
        int FP = 0;
        int FN = 0;

        // Validação de ataques
//        System.out.println(" *** Ataques *** ");
        for (int i = begin; i < end; i++) {
            Instance testando = allInstances[1].instance(i);
            double res1 = selectedClassifier.classifyInstance(testando);
            if (res1 == testando.classValue()) {
                VP = VP + 1;
            } else {
                FN = FN + 1;
            }
//            testando.setClassValue(res1);
            allInstances[0].add(testando);
        }

        // Validação de normais
//        System.out.println(" *** Normais *** ");
        for (int i = begin; i < end; i++) {
            Instance testando2 = allInstances[2].instance(i);
            double res2 = selectedClassifier.classifyInstance(testando2);
            if (res2 == testando2.classValue()) {
                VN = VN + 1;
            } else {
                FP = FP + 1;
            }

            //retroalimentação
//            testando2.setClassValue(res2);
            allInstances[0].add(testando2);

        }

        try {

            acuracia = Float.valueOf(((VP + VN)) * 100) / Float.valueOf((VP + VN + FP + FN));
            txDec = Float.valueOf((VP * 100)) / Float.valueOf((VP + FN));  // Sensitividade ou Taxa de Detecção
            txAFal = Float.valueOf((FP * 100)) / Float.valueOf((VN + FP)); // Especificade ou Taxa de Alarmes Falsos    
        } catch (java.lang.ArithmeticException e) {
            System.out.println("Divisão por zero ((" + VP + " + " + VN + ") * 100) / (" + VP + " + " + VN + "+ " + FP + "+" + FN + "))");
        }
        Resultado r = new Resultado(descricao, VP, FN, VN, FP, acuracia, txDec, txAFal);
        return r;

    }

}
