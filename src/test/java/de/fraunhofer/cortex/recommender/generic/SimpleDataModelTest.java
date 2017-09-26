package de.fraunhofer.cortex.recommender.generic;

import java.io.File;

import org.apache.mahout.cf.taste.common.TasteException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SimpleDataModelTest {
	
	private SimpleDataModel dataModel;
	private File dataFile;
	

	@Before
	public void setUp() throws Exception {
		dataFile = new File(this.getClass().getClassLoader().getResource("intro.csv").getFile());
		dataModel = new SimpleDataModel(dataFile);
	}

	@Test
	public void testNumberOfUsers() throws TasteException {
		int numberOfUsers = dataModel.getNumUsers();
		Assert.assertTrue(numberOfUsers == 5);
	}
	
	@Test
	public void testNumberOfItems() throws TasteException {
		int numberOfItems = dataModel.getNumItems();
		Assert.assertTrue(numberOfItems == 7);
	}

}
