package de.fraunhofer.cortex.recommender.generic;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

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

public class GenericRecommender implements Recommender {
	
	private final Recommender recommender;
	
	public GenericRecommender() throws IOException, TasteException {
		File dataFile = new File(this.getClass().getClassLoader().getResource("intro.csv").getFile());
		SimpleDataModel dataModel = new SimpleDataModel(dataFile);
		UserSimilarity similarity = new PearsonCorrelationSimilarity(dataModel);
		UserNeighborhood neighborhood = new NearestNUserNeighborhood(2, similarity, dataModel);
		recommender = new GenericUserBasedRecommender(dataModel, neighborhood, similarity);
		
	}
	
	public GenericRecommender(DataModel dataModel) throws TasteException {
		  UserSimilarity similarity = new PearsonCorrelationSimilarity(dataModel);
		  UserNeighborhood neighborhood = new NearestNUserNeighborhood(2, similarity, dataModel);
		  recommender = new GenericUserBasedRecommender(dataModel, neighborhood, similarity);
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
		return recommender.getDataModel();
	}

}