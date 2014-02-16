/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package indexation;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.store.LockObtainFailedException;

/**
 *
 * @author Administrator
 */
public class Indexation {

    public static File folder = new File("./images");
    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) throws CorruptIndexException, LockObtainFailedException, IOException, Exception {
    int numIndex;
                File[] files = folder.listFiles(new ImageFileFilter(false));
                System.out.println(files.length);
                if (!folder.isDirectory() || folder == null || files.length == 0) {
                System.out.println("error loading images");

                }
                IndexWriter indexWriter = new IndexWriter();
                List<Index> indexes = new ArrayList<Index>();
                // feature extraction
                numIndex = 1;
                for (File f : files) {
                    try {
                        BufferedImage bufferedImage = ImageIO.read(f);
                        if (bufferedImage.getHeight(null) != Constants.IMAGE_HEIGHT
                                || bufferedImage.getWidth(null) != Constants.IMAGE_WIDTH) {
                            bufferedImage = GraphicsUtilities.resizeImage(bufferedImage,
                                    Constants.IMAGE_WIDTH, Constants.IMAGE_HEIGHT);
                        }
                        Index index = indexWriter.getIndex(bufferedImage);
                        index.setFilePath(f.getAbsolutePath());
                        indexes.add(index);
                        numIndex++;
                        System.out.println(indexes.size());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                // k-means clustering
                KMeans kMeans = new KMeans();
                int maxIter = 200;
                kMeans.setNumCluster(2);
                kMeans.setMaxIteration(maxIter);
                kMeans.computeCluster(indexWriter, indexes);
                // indexing ...
                Cluster[] clusters = kMeans.getClusters();
                // create root index
                indexWriter.open();
                for (Cluster c : clusters) {
                    indexWriter.addCluster(c);
                }
                indexWriter.close();
                //create clusters index
                numIndex = 1;
                ClusterWriter clusterWriter = new ClusterWriter();
                for (int idx = 0; idx < kMeans.getNumCluster(); idx++) {
                    clusterWriter.open(idx);
                    for (Index ix : clusters[idx].getMembers()) {
                        clusterWriter.addIndex(ix);
                        numIndex++;
                    }
                    clusterWriter.close();
                }

               
            }


    
}
