package de.fraunhofer.cortex.recommender.grouplens;

import java.io.File;
import java.io.InputStream;

import org.apache.mahout.cf.taste.common.TasteException;
import org.junit.Test;

import junit.framework.Assert;
import junit.framework.TestCase;

public class GroupLensDataModelTest extends TestCase {
	
	private GroupLensDataModel dataModel;
	private File dataFile;

	protected void setUp() throws Exception {
		super.setUp();
		dataFile = new File(this.getClass().getClassLoader().getResource("sample_ratings.dat").getFile());
		dataModel = new GroupLensDataModel(dataFile);
		
	}
	
	@Test
	public void testNumberOfItems() throws TasteException {
		int numberOfItems = dataModel.getNumItems();
		Assert.assertTrue(numberOfItems == 99);
	}

}
