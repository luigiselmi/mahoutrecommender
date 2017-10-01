package de.fraunhofer.cortex.recommender.generic;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.junit.Before;
import org.junit.Test;

import junit.framework.Assert;

public class GenericRecommenderTest {
	
	private DataModel dataModel;
	private File dataFile;
	private Recommender recommender;

	@Before
	public void setUp() throws Exception {
		dataFile = new File(this.getClass().getClassLoader().getResource("intro.csv").getFile());
		dataModel = new SimpleDataModel(dataFile);
		recommender = new GenericRecommender(dataModel);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testRecommendations() throws TasteException {
		List<RecommendedItem> items = recommender.recommend(1, 1); //recommend to user 1 one item
		RecommendedItem recommendation = items.get(0);
		long recommendedItem = recommendation.getItemID();
		Assert.assertTrue(recommendedItem == 104);
	}
	
//	@Test
//	public void testNumberOfUserIDs() throws TasteException {
//		while(dataModel.getUserIDs().hasNext()){
//			Long userID = recommender.getDataModel().getUserIDs().next();
//			List<RecommendedItem> recommendations = recommender.recommend(userID, 1);
//			if(!recommendations.isEmpty())
//				System.out.println("User " + userID + " has recommendations");
//		}
//	}
}