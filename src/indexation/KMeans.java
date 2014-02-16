package indexation;

import java.util.Collections;
import java.util.List;
import org.ejml.data.DenseMatrix64F;

public class KMeans {

    private int numCluster = 2;
    private Cluster[] clusters;
    private int maxIteration = 200;
    private double epsilon = 0;

    public void computeCluster(Indexer indexer, List<Index> indexs) {
     
        int iter = 0;
        initializeCentroids(indexer, indexs);
        double cdist = 9999;
        while (iter < maxIteration && cdist > epsilon) {
            System.out.println("ITERATION : " + iter);
            reorganizeIndexs(indexs);
            cdist = calculateCentroids();
            System.out.println("cdist = " + cdist);
            iter++;
        }

    }

    private void initializeCentroids(Indexer indexer, List<Index> indexs) {
        Index selectedIdx = null;
        clusters = new Cluster[numCluster];
        Collections.shuffle(indexs);
        System.out.println("debut de l'initialisation");
        clusters[0] = new Cluster(indexer, indexs.get(1));
        System.out.println("debut boucle");
        for (int c = 1; c < numCluster; c++) {
            int numClusterHaveCentroid = c;
            System.out.println(c);
            double sumd = 0;
            double prevsumd = 0;

            for (Index idx : indexs) {
                System.out.println(idx.getBHLhaarvariance());
                for (int j = 0; j < numClusterHaveCentroid; j++) {
                    System.out.println(sumd);
                    sumd += (clusters[j].getDistance(idx));
                    System.out.println("for cluster"+j);
                }
                if (sumd > prevsumd) {
                    selectedIdx = idx;
                }
                prevsumd = sumd;
            }
            
            clusters[c] = new Cluster(indexer, selectedIdx);
            System.out.println("========= INIT ====== " + c);
            printCentroid(clusters[c].getCentroid());
        }
    }

    private double calculateCentroids() {
        DenseMatrix64F[] prevCentroid;
        double cdist = 0;
        for (Cluster c : clusters) {
            prevCentroid = c.getPrevCentroid();
            //System.out.println("========= PREV ====== " + c);
            //printCentroid(prevCentroid);
            cdist += c.getDistance(prevCentroid[0], prevCentroid[1], prevCentroid[2]);
            c.calculateMean();
            //System.out.println("========= NOW ====== " + c);
            //printCentroid(c.getCentroid());
        }

        return cdist / numCluster;
    }

    private void printCentroid(DenseMatrix64F[] centroid) {
        for (DenseMatrix64F c : centroid) {
            System.out.println("c-" + c);
            for (int i = 0; i < c.numRows; i++) {
                for (int j = 0; j < c.numCols; j++) {
                    System.out.print(c.get(i, j) + " ");
                }
                System.out.println();
            }
        }

    }

    private void reorganizeIndexs(List<Index> indexs) {
        for (Cluster c : clusters) {
            c.getMembers().clear();
        }
        for (Index index : indexs) {
            // select best cluster
            double dist = 0, prevDist = 65280;
            int cid = -1;
            for (int c = 0; c < numCluster; c++) {
                dist = clusters[c].getDistance(index);

                if (dist < prevDist) {
                    cid = c;
                    prevDist = dist;
                }
            }

            // add index to cluster
            if (cid > -1) {
                clusters[cid].addMember(index);
            }
        }
    }

    public int getNumCluster() {
        return numCluster;


    }

    /**
     * @param numCluster the numCluster to set
     */
    public void setNumCluster(int numCluster) {
        this.numCluster = numCluster;


    }

    /**
     * @return the maxIteration
     */
    public int getMaxIteration() {
        return maxIteration;






    }

    /**
     * @param maxIteration the maxIteration to set
     */
    public void setMaxIteration(int maxIteration) {
        this.maxIteration = maxIteration;






    }

    /**
     * @return the epsilon
     */
    public double getEpsilon() {
        return epsilon;






    }

    /**
     * @param epsilon the epsilon to set
     */
    public void setEpsilon(double epsilon) {
        this.epsilon = epsilon;






    }

    /**
     * @return the clusters
     */
    public Cluster[] getClusters() {
        return clusters;



    }
}
