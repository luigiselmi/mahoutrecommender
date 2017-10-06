package de.fraunhofer.cortex.recommender.atn;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

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
	
		List<RecommendedItem> recommendations = recommender.recommend(20159, 2);
		for(RecommendedItem i: recommendations){
			long recommendedItemID = i.getItemID();
			float value = i.getValue();
			System.out.println("Recommended item: " + recommendedItemID + " value: " + value);
		}
	
	}
	
	@Test
	public void testJsonRecommendations() throws TasteException {
		List<RecommendedItem> recommendations = recommender.recommend(20159, 2);
		JsonArrayBuilder jrecommendations = Json.createArrayBuilder();
		for(RecommendedItem ri: recommendations){
			JsonObject jrecommendation = Json.createObjectBuilder()
		    		  .add("itemID", ri.getItemID())
		    		  .add("value", ri.getValue())
		    		  .build();
			jrecommendations.add(jrecommendation);	  	
		}
		JsonObject jrecommendedItems = Json.createObjectBuilder()
				.add("recommendedItems", jrecommendations).build();
		System.out.println(jrecommendedItems.toString());
		
	}
	
	@Test
	public void testItemIDs(){
		Map<Long, String> mapHashToItemID = ((AtnFileDataModel) recommender.getDataModel()).getMapItemID();
		String itemID = mapHashToItemID.get(new Long(67));
		Assert.assertTrue("40526f54-3c60-4d87-9628-368524cff90c".equals(itemID));
	}

}
