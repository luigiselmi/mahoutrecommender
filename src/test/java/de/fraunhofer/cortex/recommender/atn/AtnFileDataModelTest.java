package de.fraunhofer.cortex.recommender.atn;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

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
		Assert.assertEquals("41026238-b547-4b1c-90c0-c1401c50a507", record0.getItemID());
	}
	
	@Test
	public void testAggregateRecords() throws IOException {
		List<Feedback> feedbacks = AtnFileDataModel.groupRecordsByKey(records);
		for (Feedback feedback: feedbacks) {
			System.out.println(feedback.getUserId() + " " + feedback.getItemId() + " " + feedback.getFeedbacks());
		}
		Assert.assertTrue(feedbacks.size() == 7);
	}

}
