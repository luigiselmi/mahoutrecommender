package de.fraunhofer.cortex.recommender.atn;

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

public class AtnRecommenderTest {

	private File dataFile;
	private DataModel dataModel;
	private Recommender recommender;
	
	@Before
	public void setUp() throws Exception {
		dataFile = new File(this.getClass().getClassLoader().getResource("feedback.csv").getFile());
		dataModel = new AtnFileDataModel(dataFile);
		recommender = new AtnRecommender(dataModel);
	}

	@Test
	public void testRecommendations() throws TasteException {
		System.out.println("Number of users: " + dataModel.getNumUsers());
		
		System.out.println("Number of items: " + dataModel.getNumItems());
		
		System.out.println("Number of users with preferences for 29: " + dataModel.getNumUsersWithPreferenceFor(29));
		System.out.println("Number of preferences for 29: " + dataModel.getPreferencesForItem(29));
		
//		int userCount = 0;
//		while(dataModel.getUserIDs().hasNext()){
//			Long userID = recommender.getDataModel().getUserIDs().next();
//			userCount += 1;
//			System.out.println(userCount + " userID: " + userID);
//		}
//		System.out.println("Number of users: " + userCount);
	
		List<RecommendedItem> recommendations = recommender.recommend(21819, 1);
		for(RecommendedItem i: recommendations){
			long recommendedItemID = i.getItemID();
			float value = i.getValue();
			System.out.println("Recommended item: " + recommendedItemID + " value: " + value);
		}
	
	}

}
