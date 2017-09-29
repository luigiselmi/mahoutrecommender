package de.fraunhofer.cortex.recommender.atn;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.junit.Before;
import org.junit.Test;

import junit.framework.Assert;

public class AtnRecommenderTest {

	private File dataFile;
	private AtnFileDataModel dataModel;
	private AtnRecommender recommender;
	
	@Before
	public void setUp() throws Exception {
		dataFile = new File(this.getClass().getClassLoader().getResource("feedback.csv").getFile());
		dataModel = new AtnFileDataModel(dataFile);
		recommender = new AtnRecommender(dataModel);
	}

	@Test
	public void testRecommendations() throws TasteException {
		List<RecommendedItem> items = recommender.recommend(1, 1); //recommend to user 1 one item
		RecommendedItem recommendation = items.get(0);
		long recommendedItem = recommendation.getItemID();
		Assert.assertTrue(recommendedItem == 104);
	}

}
