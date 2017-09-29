package de.fraunhofer.cortex.recommender.atn;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.common.iterator.FileLineIterable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;

public class AtnFileDataModel extends FileDataModel {
	
	private static final String COLON_DELIMTER = ",";
	private static final Pattern COLON_DELIMITER_PATTERN = Pattern.compile(COLON_DELIMTER);
	static final Logger LOG = LoggerFactory.getLogger(AtnFileDataModel.class);

	public AtnFileDataModel(File dataFile) throws IOException {
		super(transformFile(dataFile));
		
	}
	
	/**
	 * This method returns a file with records: userID, itemID, value
	 * 1) Read the records from the file and build a collectiont 
	 * 2) Group the records by a key (userID and itemID).
	 *    The number of records per key counts as feedbacks
	 * 3) Optional - Map the source_itemID (string) to a target item id (numeric)
	 * 4) Create a file of feedbacks: userID, itemID, feedbacks value
	 * 5) Return the aggregated set of records 
	 * @param dataFile
	 * @throws IOException
	 */
	private static File transformFile(File dataFile) throws IOException {
		File transformedFile = null;
		LOG.info("Transforming source data file.");
		List<Record> records = readRecords(dataFile);
		List<Feedback> feedbacks = groupRecordsByKey(records);
		transformedFile = createTransformedFile(feedbacks);
		return transformedFile;
	}
	
	public static List<Record> readRecords(File dataFile) throws IOException {
		List<Record> records = new ArrayList<Record>();
		FileLineIterable fileIterable = new FileLineIterable(dataFile, false);
		for (String line : fileIterable) {
			int lastDelimiterStart = line.lastIndexOf(COLON_DELIMTER);
	        String itemID = line.substring(lastDelimiterStart + 1);
	        String subRecord = line.substring(0, lastDelimiterStart);
	        int subRecordLastDelimiterStart = subRecord.lastIndexOf(COLON_DELIMTER);
	        String userID = subRecord.substring(subRecordLastDelimiterStart + 1);
	        Record record = new Record(userID, itemID);
	        records.add(record);    
	    }
		
		return records;
	}
	
	public static List<Feedback> groupRecordsByKey(List<Record> records) throws IOException {
		List<Feedback> feedbacks = new ArrayList<Feedback>();
		Map<String, List<Record>> recordsByKey = records.stream()
										.collect(Collectors.groupingBy(Record::getRecordKey));
		for (String key: recordsByKey.keySet()) {
			Feedback feedback = new Feedback();
			List<Record> groupRecords = recordsByKey.get(key);
			Record record = groupRecords.get(0);
			feedback.setUserId(record.getUserID());
			feedback.setItemId(record.getItemID());
			feedback.setFeedbacks(groupRecords.size());
			feedbacks.add(feedback);
		}
		
		return feedbacks;
		
	}
	
	public static File createTransformedFile(List<Feedback> feedbacks) throws FileNotFoundException {
		File resultFile = new File(new File(System.getProperty("java.io.tmpdir")), "feedbacks.txt");
	    if (resultFile.exists()) {
	      resultFile.delete();
	    }
	    PrintWriter writer = new PrintWriter(new FileOutputStream(resultFile));
	    for (Feedback f: feedbacks) {
	    	writer.write(f.getUserId() + "," + f.getItemId() + "," + f.getFeedbacks());
	    }
	    writer.flush();
	    writer.close();
	    
	    return resultFile;
	}

}
