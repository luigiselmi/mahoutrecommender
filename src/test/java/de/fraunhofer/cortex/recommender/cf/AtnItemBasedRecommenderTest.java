package de.fraunhofer.cortex.recommender.cf;

import static org.junit.Assert.*;

import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.junit.Before;
import org.junit.Test;

import de.fraunhofer.cortex.recommender.model.SignalsDataModel;

public class AtnItemBasedRecommenderTest {

  private Recommender recommender;
  private SignalsDataModel dataModel;
  
  @Before
  public void setUp() throws Exception {
    recommender = new AtnItemBasedRecommender();
    dataModel = (SignalsDataModel)recommender.getDataModel();
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
    // 3 users have got recommendations
    assertEquals(3, usersWithRecommendations);
    
  }

  @Test
  public void testRecommendationsStatistics()throws TasteException {
    
    assertEquals(14, dataModel.getNumUsers());
    assertEquals(285, dataModel.getNumItems());
    
  }

}
