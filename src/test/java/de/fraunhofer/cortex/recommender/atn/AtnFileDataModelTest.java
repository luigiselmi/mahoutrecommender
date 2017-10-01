package de.fraunhofer.cortex.recommender.atn;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import junit.framework.Assert;

public class AtnFileDataModelTest {
	
	private File dataFile;
	private List<Record> records;

	@Before
	public void setUp() throws Exception {
		dataFile = new File(this.getClass().getClassLoader().getResource("feedback.csv").getFile());
		records = AtnFileDataModel.readRecords(dataFile);
	}

	@Test
	public void testReadRecords() throws IOException {
		Record record0 = records.get(0);
		Assert.assertEquals("21585", record0.getUserID());
		Assert.assertEquals("e5e35469-3e62-4677-a265-5307005c3c8c", record0.getItemID());
	}
	
	@Test
	public void testSortRecords() throws IOException {
		Collections.sort(records);
		Record record0 = records.get(0);
		Assert.assertEquals("20159", record0.getUserID());
		Assert.assertEquals("0ae61f0b-9303-4d7e-b778-4d37f86bc34d", record0.getItemID());
	}
	
	@Test
	public void testAggregateRecords() throws IOException {
		List<Feedback> feedbacks = AtnFileDataModel.groupRecordsByKey(records);
//		for (Feedback feedback: feedbacks) {
//			System.out.println(feedback.getUserId() + " " + feedback.getItemId() + " " + feedback.getFeedbacks());
//		}
		Assert.assertTrue(feedbacks.size() > 11);
	}
	
	@Test
	public void testHashItemID() throws IOException {
		List<Feedback> feedbacks = AtnFileDataModel.groupRecordsByKey(records);
		List<Feedback> hashfeedbacks = AtnFileDataModel.hashItemID(feedbacks);
//		for (Feedback feedback: hashfeedbacks) {
//			System.out.println(feedback.getUserId() + " " + feedback.getItemId() + " " + feedback.getFeedbacks());
//		}
		
	}
	
	@Test
	public void testHashCode() throws IOException {
		List<Feedback> feedbacks = AtnFileDataModel.groupRecordsByKey(records);
		int hashItemID = AtnFileDataModel.hashCode("51eb5154-f55f-4c4e-b8e8-0f8902084350", feedbacks.size());
		Assert.assertEquals(29, hashItemID);
	}
	
	
}
