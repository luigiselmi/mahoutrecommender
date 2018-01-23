package de.fraunhofer.cortex.recommender.cf;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import org.apache.mahout.cf.taste.common.Refreshable;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.CachingItemSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.IDRescorer;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fraunhofer.cortex.recommender.model.SignalsDataModel;

public class AtnItemBasedRecommender implements Recommender {
  
  static final Logger LOG = LoggerFactory.getLogger(AtnItemBasedRecommender.class);
  private final Recommender recommender;
  private SignalsDataModel dataModel;
  
  public AtnItemBasedRecommender() throws TasteException, IOException {
    ApplicationConfig config = readConfiguration();
    File dataFile = new File(config.getSignalsFile());
    dataModel = new SignalsDataModel(dataFile);
    ItemSimilarity similarity = new CachingItemSimilarity(new PearsonCorrelationSimilarity(dataModel), dataModel);
    recommender = new GenericItemBasedRecommender(dataModel, similarity);
    LOG.info("Item Based Recommender ready");
  }
  // used for testing
  public AtnItemBasedRecommender(File dataFile) throws TasteException, IOException {
    dataModel = new SignalsDataModel(dataFile);
    ItemSimilarity similarity = new CachingItemSimilarity(new PearsonCorrelationSimilarity(dataModel), dataModel);
    recommender = new GenericItemBasedRecommender(dataModel, similarity);
    LOG.info("Item Based Recommender ready");
  }

  @Override
  public void refresh(Collection<Refreshable> alreadyRefreshed) {
    // TODO Auto-generated method stub

  }

  @Override
  public List<RecommendedItem> recommend(long userID, int howMany) throws TasteException {
    return recommender.recommend(userID, howMany);
  }

  @Override
  public List<RecommendedItem> recommend(long userID, int howMany, boolean includeKnownItems) throws TasteException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<RecommendedItem> recommend(long userID, int howMany, IDRescorer rescorer) throws TasteException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<RecommendedItem> recommend(long userID, int howMany, IDRescorer rescorer, boolean includeKnownItems)
      throws TasteException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public float estimatePreference(long userID, long itemID) throws TasteException {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public void setPreference(long userID, long itemID, float value) throws TasteException {
    // TODO Auto-generated method stub

  }

  @Override
  public void removePreference(long userID, long itemID) throws TasteException {
    // TODO Auto-generated method stub

  }

  @Override
  public DataModel getDataModel() {
    return this.dataModel;
  }

  /**
   * Reads the configuration file
   * @return
   * @throws IOException
   */
  private ApplicationConfig readConfiguration() throws IOException {
    ApplicationConfig config = new ApplicationConfig();
    Properties prop = new Properties();
    InputStream configIs = AtnItemBasedRecommender.class.getClassLoader().getResourceAsStream("config.properties");
    prop.load(configIs);
    config.setSignalsFile(prop.getProperty("signals.file"));
    return config;
  }
}
