package de.fraunhofer.cortex.recommender.cf;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import org.apache.mahout.cf.taste.common.Refreshable;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.IDRescorer;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.fraunhofer.cortex.recommender.model.SignalsDataModel;

public class AtnUserBasedRecommender implements Recommender {
	
	static final Logger LOG = LoggerFactory.getLogger(AtnUserBasedRecommender.class);
	private final Recommender recommender;
	private SignalsDataModel dataModel;
	private long minReloadIntervalMillis = 60000L;
	
	public AtnUserBasedRecommender() throws IOException, TasteException {
	  ApplicationConfig config = readConfiguration();
	  File dataFile = new File(config.getSignalsFile());
		dataModel = new SignalsDataModel(dataFile, minReloadIntervalMillis);
		UserSimilarity similarity = new PearsonCorrelationSimilarity(dataModel);
		UserNeighborhood neighborhood = new NearestNUserNeighborhood(3, similarity, dataModel);
		recommender = new GenericUserBasedRecommender(dataModel, neighborhood, similarity);
		LOG.info("User Based Recommender ready");
	}
	// Used for testing
	public AtnUserBasedRecommender(File dataFile) throws IOException, TasteException {
    dataModel = new SignalsDataModel(dataFile, minReloadIntervalMillis);
    UserSimilarity similarity = new PearsonCorrelationSimilarity(dataModel);
    UserNeighborhood neighborhood = new NearestNUserNeighborhood(3, similarity, dataModel);
    recommender = new GenericUserBasedRecommender(dataModel, neighborhood, similarity);
    LOG.info("User Based Recommender ready");
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
    InputStream configIs = AtnUserBasedRecommender.class.getClassLoader().getResourceAsStream("config.properties");
    prop.load(configIs);
    config.setSignalsFile(prop.getProperty("signals.file"));
    return config;
  }

}
