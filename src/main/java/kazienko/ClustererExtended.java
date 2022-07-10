package kazienko;

import weka.clusterers.EM;
import weka.clusterers.FarthestFirst;
import weka.clusterers.SimpleKMeans;
import weka.core.Instance;
import weka.core.Instances;

public class ClustererExtended {

    EM em;
    SimpleKMeans kMeans;
    FarthestFirst ff;

    public ALGORITHMS ALGORITHM = null;

    enum ALGORITHMS {EM, KMEANS, FF}

    public void build(Instances train, int k, ALGORITHMS ALGORITHM) throws Exception {
        this.ALGORITHM = ALGORITHM;
        if (ALGORITHM.equals(ALGORITHMS.FF)) {
            ff = Util.clusterDataFarthest(train, k);
        } else if (ALGORITHM.equals(ALGORITHMS.KMEANS)) {
            kMeans = Util.clusterData(train, k);
        } else if (ALGORITHM.equals(ALGORITHMS.EM)) {
            em = Util.clusterDataEM(train, k);
        }
    }

    public int clusterInstance(Instance instance) throws Exception {
        if (this.ALGORITHM.equals(ALGORITHMS.FF)) {
            return ff.clusterInstance(instance);
        } else if (this.ALGORITHM.equals(ALGORITHMS.KMEANS)) {
            return kMeans.clusterInstance(instance);
        } else if (this.ALGORITHM.equals(ALGORITHMS.EM)) {
            return em.clusterInstance(instance);
        }
        System.err.println("No clustering algorithm defined.");
        System.exit(1);
        return -17;
    }


}
