package de.fraunhofer.cortex.recommender.grouplens;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.junit.Before;
import org.junit.Test;

import junit.framework.Assert;

public class GroupLensItemBasedRecommenderTest {
	
	private GroupLensDataModel dataModel;
	private File dataFile;
	private GroupLensItemBasedRecommender recommender;

	@Before
	public void setUp() throws Exception {
		dataFile = new File(this.getClass().getClassLoader().getResource("ratings.dat").getFile());
		dataModel = new GroupLensDataModel(dataFile);
		recommender = new GroupLensItemBasedRecommender(dataModel);
	}

	@Test
	public void testRecommendation() throws TasteException {
		List<RecommendedItem> recommendations = recommender.recommend(1, 10);
		Iterator<RecommendedItem> irec = recommendations.iterator();
		while(irec.hasNext()) {
			RecommendedItem recItem = irec.next();
			System.out.println(recItem.toString());
		}
		
		Assert.assertTrue(recommendations.get(0).getValue() > 4.677 );
	}

}
