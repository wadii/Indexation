package indexation;

import java.util.ArrayList;
import java.util.List;
import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.CommonOps;


public class Cluster {

    private List<Index> members = new ArrayList<Index>();
    private Indexer indexer;
    private DenseMatrix64F rCentroid;
    private DenseMatrix64F gCentroid;
    private DenseMatrix64F bCentroid;
    private DenseMatrix64F prevRCentroid;
    private DenseMatrix64F prevGCentroid;
    private DenseMatrix64F prevBCentroid;
    private int numMembers = 0;

    public Cluster(Indexer indexer, DenseMatrix64F[] centroid) {
        rCentroid = centroid[0];
        gCentroid = centroid[1];
        bCentroid = centroid[2];
        //downScaleFactorPoint(rCentroid, gCentroid, bCentroid);
        prevRCentroid = new DenseMatrix64F(rCentroid.numRows, rCentroid.numCols);
        prevGCentroid = new DenseMatrix64F(gCentroid.numRows, gCentroid.numCols);
        prevBCentroid = new DenseMatrix64F(bCentroid.numRows, bCentroid.numCols);
        this.indexer = indexer;
    }

    
    public Cluster(Indexer indexer, Index index) {
        this.indexer = indexer;
        rCentroid = indexer.toMatrix(index.getRHaarFeature());
        gCentroid = indexer.toMatrix(index.getGHaarFeature());
        bCentroid = indexer.toMatrix(index.getBHaarFeature());
        
                System.out.println(index.getBHLhaarvariance());

        prevRCentroid = new DenseMatrix64F(rCentroid.numRows, rCentroid.numCols);
        prevGCentroid = new DenseMatrix64F(gCentroid.numRows, gCentroid.numCols);
        prevBCentroid = new DenseMatrix64F(bCentroid.numRows, bCentroid.numCols);
}

    public void addMember(Index index) {
        getMembers().add(index);
        numMembers = members.size();
    }

    public void calculateMean() {
        if (numMembers == 0) {
            return;
        }

        prevRCentroid = rCentroid.copy();
        prevGCentroid = gCentroid.copy();
        prevBCentroid = bCentroid.copy();

        DenseMatrix64F sumr = new DenseMatrix64F(1, rCentroid.getNumCols());
        DenseMatrix64F sumg = new DenseMatrix64F(1, gCentroid.getNumCols());
        DenseMatrix64F sumb = new DenseMatrix64F(1, bCentroid.getNumCols());

        for (Index index : getMembers()) {
            DenseMatrix64F r = indexer.toMatrix(index.getRHaarFeature());
            DenseMatrix64F g = indexer.toMatrix(index.getGHaarFeature());
            DenseMatrix64F b = indexer.toMatrix(index.getBHaarFeature());

            CommonOps.add(sumr, r, sumr);
            CommonOps.add(sumg, g, sumg);
            CommonOps.add(sumb, b, sumb);
        }

        CommonOps.divide(numMembers, sumr, rCentroid);
        CommonOps.divide(numMembers, sumg, gCentroid);
        CommonOps.divide(numMembers, sumb, bCentroid);

       
    }

    public double getDistance(Index idx) {
        System.out.println(idx.getRHaarFeature());
        DenseMatrix64F r = indexer.toMatrix(idx.getRHaarFeature());
        DenseMatrix64F g = indexer.toMatrix(idx.getGHaarFeature());
        DenseMatrix64F b = indexer.toMatrix(idx.getBHaarFeature());
        return getDistance(r, g, b);
    }

    public double getDistance(DenseMatrix64F r, DenseMatrix64F g, DenseMatrix64F b) {

        double rsum = 0;
        double gsum = 0;
        double bsum = 0;

        for (int i = 0; i < r.getNumCols(); i++) {
            double rdist = rCentroid.get(0, i) - r.get(0, i);
            double gdist = gCentroid.get(0, i) - g.get(0, i);
            double bdist = bCentroid.get(0, i) - b.get(0, i);
            rsum += rdist * rdist;
            gsum += gdist * gdist;
            bsum += bdist * bdist;
        }

        double dist = Math.sqrt(rsum) + Math.sqrt(gsum) + Math.sqrt(bsum);
        return dist / 3;

       }

    /**
     * @return the members
     */
    public List<Index> getMembers() {
        return members;
    }

    public DenseMatrix64F[] getCentroid() {

        return new DenseMatrix64F[]{rCentroid, gCentroid, bCentroid};
    }

    public DenseMatrix64F[] getPrevCentroid() {
        return new DenseMatrix64F[]{prevRCentroid, prevGCentroid,
                    prevBCentroid};
    }

    /**
     * @param members the members to set
     */
    public void setMembers(List<Index> members) {
        this.members = members;
    }

    public Indexer getIndexer() {
        return indexer;
    }

    public void setIndexer(Indexer indexer) {
        this.indexer = indexer;
    }

    public DenseMatrix64F getrCentroid() {
        return rCentroid;
    }

    public void setrCentroid(DenseMatrix64F rCentroid) {
        this.rCentroid = rCentroid;
    }

    public DenseMatrix64F getgCentroid() {
        return gCentroid;
    }

    public void setgCentroid(DenseMatrix64F gCentroid) {
        this.gCentroid = gCentroid;
    }

    public DenseMatrix64F getbCentroid() {
        return bCentroid;
    }

    public void setbCentroid(DenseMatrix64F bCentroid) {
        this.bCentroid = bCentroid;
    }

    public DenseMatrix64F getPrevRCentroid() {
        return prevRCentroid;
    }

    public void setPrevRCentroid(DenseMatrix64F prevRCentroid) {
        this.prevRCentroid = prevRCentroid;
    }

    public DenseMatrix64F getPrevGCentroid() {
        return prevGCentroid;
    }

    public void setPrevGCentroid(DenseMatrix64F prevGCentroid) {
        this.prevGCentroid = prevGCentroid;
    }

    public DenseMatrix64F getPrevBCentroid() {
        return prevBCentroid;
    }

    public void setPrevBCentroid(DenseMatrix64F prevBCentroid) {
        this.prevBCentroid = prevBCentroid;
    }

    public int getNumMembers() {
        return numMembers;
    }

    public void setNumMembers(int numMembers) {
        this.numMembers = numMembers;
    }
    
    
    
}
