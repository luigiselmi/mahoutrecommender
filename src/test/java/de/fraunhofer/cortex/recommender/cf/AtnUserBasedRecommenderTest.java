package de.fraunhofer.cortex.recommender.cf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.junit.Before;
import org.junit.Test;

import de.fraunhofer.cortex.recommender.cf.AtnUserBasedRecommender;
import de.fraunhofer.cortex.recommender.model.SignalsDataModel;

/**
 * The tests use the data in src/test/resources.
 * The application must be configured in config.properties to 
 * be under test.
 */
public class AtnUserBasedRecommenderTest {

  private File dataFile;
	private Recommender recommender;
	private SignalsDataModel dataModel;
	
	@Before
	public void setUp() throws Exception {
	  dataFile = new File(this.getClass().getClassLoader().getResource("signals_test.csv").getFile());
		recommender = new AtnUserBasedRecommender(dataFile);
		dataModel = (SignalsDataModel)recommender.getDataModel();
	}

	@Test
	public void testRecommendedItem() throws TasteException {
	  
  	List<RecommendedItem> recommendations = recommender.recommend(35410, 2);
  	long recommendedItemID = recommendations.get(0).getItemID();
  	String stringRecommendedItemID = dataModel.getItemIDAsString(recommendedItemID);
  	assertEquals("69ebc042-341a-4776-9d07-8a54c46176d1", stringRecommendedItemID);		
	}
	
	@Test
	public void testRecommendations() throws TasteException {
	  LongPrimitiveIterator userIDs = dataModel.getUserIDs();
	  int usersWithRecommendations = 0;
    while(userIDs.hasNext()) {
      long usedID = userIDs.next();
      List<RecommendedItem> recommendations = recommender.recommend(usedID, 1);
      if( ! recommendations.isEmpty() )
        usersWithRecommendations ++;
    }
    // 5 users have got recommendations when neighborhood is 3
    assertEquals(5, usersWithRecommendations);
	  
	}
	
	@Test
	public void testRecommendationsStatistics()throws TasteException {
	  
	  assertEquals(14, dataModel.getNumUsers());
	  assertEquals(285, dataModel.getNumItems());
    
  }
	
}
