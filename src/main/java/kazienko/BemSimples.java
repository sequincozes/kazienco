/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kazienko;

import java.util.ArrayList;
import weka.classifiers.Classifier;
import weka.clusterers.SimpleKMeans;
import weka.clusterers.FarthestFirst;

import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author silvio
 */
public class BemSimples {

    static Instances[] allInstances;
    static Classifier selectedClassifier = Parameters.NAIVE_BAYES.getClassifier();
    static boolean rawOutput = false;
    static boolean debug = false;

    public static void main(String[] args) throws Exception {
        // TESTES DE CLASSIFICADORES
        allInstances = Util.loadAndFilter(true);
        ids(allInstances[0], allInstances[1], allInstances[2]);

// TESTES DE CLUSTERINGs (trainInstances, normalInstances, testInstances)
//        allInstances = Util.loadAndFilter(false);
//        
//        SimpleKMeans kmeans = avaliacaoComClustering(6, allInstances[0], allInstances[1], allInstances[3]);
//        testeComClustering(kmeans, allInstances[1]);
//        System.out.println(kmeans.clusterInstance(kmeans.getClusterCentroids().instance(0)));
//        System.out.println(kmeans.clusterInstance(kmeans.getClusterCentroids().instance(1)));
//        System.out.println(kmeans.clusterInstance(kmeans.getClusterCentroids().instance(2)));
//        System.out.println(kmeans.clusterInstance(kmeans.getClusterCentroids().instance(3)));
//        System.out.println(kmeans.clusterInstance(kmeans.getClusterCentroids().instance(4)));
//        System.out.println(kmeans.clusterInstance(kmeans.getClusterCentroids().instance(5)));
//        allInstances = Util.loadAndFilterUnsupervised(false);
//        ads(allInstances[0], allInstances[1], allInstances[2], allInstances[3]);
    }

    public static void ids(Instances treino, Instances testeNormal, Instances teste) throws Exception {
//        System.out.println("Treino: " + treino.size() + " instâncias.");
//        System.out.println("Teste Normal: " + testeNormal.size() + " instâncias.");
//        System.out.println("Teste Ataque: " + teste.size() + " instâncias.");
        for (ClassifierExtended cE : Parameters.CLASSIFIERS_FOREACH) {
            Classifier c = cE.getClassifier();
            // Train
            c.buildClassifier(treino);

            // Testes
            int vp = 0, vn = 0, fp = 0, fn = 0;
            for (Instance i : testeNormal) {
                if (c.classifyInstance(i) == i.classValue()) {
                    vn++;
                } else {
                    fp++;
                }
            }

            for (Instance i : teste) {
                if (c.classifyInstance(i) == i.classValue()) {
                    vp++;
                } else {
                    fn++;
                }
            }
//            double acuracia = Float.valueOf(((vp + vn) * 100) / (vp + vn + fp + fn));;
//            double taxaDeteccao = Float.valueOf((vp * 100) / (vp + fn));
//            double taxaAlarmeFalsos = Float.valueOf((fp * 100) / (vn + fp));
            System.out.println(cE.getClassifierName() + ";" + vp + ";" + vn + ";" + fp + ";" + fn);
        }
    }

    public static void ads(Instances treino, Instances testeNormal, Instances teste, Instances labeled) throws Exception {
        System.out.println("Treino: " + treino.size() + " instâncias.");
        System.out.println("Teste Normal: " + testeNormal.size() + " instâncias.");
        System.out.println("Teste Ataque: " + teste.size() + " instâncias.");
        System.out.println("Treino Label: " + labeled.size() + " instâncias.");
// Criação do cluster (treino)
        int k = 5;
        int[][] results = new int[k][3];
        SimpleKMeans kmeans = Util.clusterData(treino, k);

        /* TREINO */
        for (int i = 0; i < k; i++) {
            results[i][0] = 0; // Total
            results[i][1] = 0; // Normal (Supondo que 0 é normal)
            results[i][2] = 0; // Attacks (Supondo que 1 é ataque)
        }

        System.out.println(" ** TREINO ** ");
        for (int i = 0; i < treino.numInstances(); i++) {
//            System.out.println(i + "-" + labeled.instance(i).classValue() + " = " + labeled.instance(i).toString());
            int clusterNum = kmeans.clusterInstance(treino.instance(i));
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
        for (int i = 0;
                i < k;
                i++) {
            results[i][0] = 0; // Total
            results[i][1] = 0; // Normal
            results[i][2] = 0; // Attacks
        }

        if (true) {
            System.out.println(" ** TESTE ** ");

            for (int i = 0; i < teste.numInstances(); i++) {
                int clusterNum = kmeans.clusterInstance(teste.instance(i));
                results[clusterNum][0] = results[clusterNum][0] + 1;
            }
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
                int clusterNum = kmeans.clusterInstance(testeNormal.instance(i));
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

    public static SimpleKMeans avaliacaoComClustering(int k, Instances treino, Instances avaliacao, Instances avaliacaoLimpa) throws Exception {
        if (avaliacao.numInstances() != avaliacaoLimpa.numInstances()) {
            System.out.println("Bases de avaliação com tamanhos diferentes.");
            return null;
        }
        SimpleKMeans kmeans = Util.clusterData(avaliacaoLimpa, k);
        for (ClassifierExtended CLASSIFIERS_FOREACH : Parameters.CLASSIFIERS_FOREACH) {
            selectedClassifier = CLASSIFIERS_FOREACH.getClassifier();
            selectedClassifier.buildClassifier(treino);
            Resultado[] clustersResults = new Resultado[k];
            int numCluster = 0;

            for (int i = 0; i < k; i++) {
                clustersResults[i] = new Resultado(CLASSIFIERS_FOREACH.getClassifierName() + " - Cluster: " + String.valueOf(numCluster), 0, 0, 0, 0);
                numCluster++;
            }

            int[] assignments = kmeans.getAssignments(); // Avaliação No-Label
            for (int i = 0; i < assignments.length; i++) {
                int cluster = assignments[i];
//                System.out.println("Cluster " + cluster + ": " + i);
                // Classificação
                Instance testando = avaliacao.instance(i); // Avaliação com Label
                double res1 = selectedClassifier.classifyInstance(testando);
                updateClusterResult(clustersResults, cluster, testando, res1);
            }
            for (Resultado r : clustersResults) {
                r.recalcular();
                System.out.println(String.valueOf(r.getCx() + ";" + String.valueOf(r.getAcuracia()).replace(".", ",") + "%" + ";" + String.valueOf(r.getTaxaAlarmeFalsos()).replace(".", ",") + "%" + ";" + String.valueOf(r.getTaxaDeteccao()).replace(".", ",") + "%" + ";" + r.getVP() + ";" + r.getVN() + ";" + r.getFN() + ";" + r.getFP()));
            }
        }

        return kmeans;
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

    
}
