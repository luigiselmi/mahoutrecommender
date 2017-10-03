package de.fraunhofer.cortex.recommender.atn;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
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
	private static Map<Long, String> mapItemIDs = null;
	public AtnFileDataModel(File dataFile) throws IOException {
		super(transformFile(dataFile));
		
	}
	
	/**
	 * This method returns a file with records: userID, itemID, value
	 * 1) Read the records from the file and build a collection 
	 * 2) Group the records by a key (userID and itemID).
	 *    The number of records per key counts as feedbacks
	 * 3) Map the itemID, a string, to numeric id
	 *    Apache Mahout requires both userID and itemID as non-negative numeric values
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
		mapItemID(feedbacks);
		List<Feedback> hashFeedbacks = hashItemID(feedbacks);
		transformedFile = createTransformedFile(hashFeedbacks);
		return transformedFile;
	}
	/**
	 * Read the records from the file and build a collection
	 * @param dataFile
	 * @return
	 * @throws IOException
	 */
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
	/**
	 * Group the records by a key (userID and itemID). The number of records 
	 * per key counts as feedbacks.
	 * @param records
	 * @return
	 * @throws IOException
	 */
	public static List<Feedback> groupRecordsByKey(List<Record> records) throws IOException {
		List<Feedback> feedbacks = new ArrayList<Feedback>();
		Map<String, List<Record>> recordsByKey = records.stream()
										.collect(Collectors.groupingBy(Record::getRecordKey));
		for (String key: recordsByKey.keySet()) {
			List<Record> groupRecords = recordsByKey.get(key);
			Record record = groupRecords.get(0);
			Feedback feedback = new Feedback(record.getUserID(), record.getItemID());
			feedback.setFeedbacks(groupRecords.size());
			feedbacks.add(feedback);
		}
		
		return feedbacks;
		
	}
	/**
	 * Returns the feedbacks with the itemID hash coded
	 * @param feedbacks
	 * @return
	 */
	public static List<Feedback> hashItemID(List<Feedback> feedbacks) {
		List<Feedback> hashFeedbacks = new ArrayList<Feedback>();
		int listSize = feedbacks.size();
		for (Feedback f: feedbacks){
			int hashItemID = hashCode(f.getItemId(), listSize);
			Feedback hashFeedback = new Feedback(f.getUserId(), Integer.toString(hashItemID));
			hashFeedback.setFeedbacks(f.getFeedbacks());
			hashFeedbacks.add(hashFeedback);
		}
		return hashFeedbacks;
	}
	/**
	 * Compute a non-negative hash code
	 * @param idString
	 * @param size
	 * @return
	 */
	public static int hashCode(String idString, int size) {
		return (idString.hashCode() & 0x7FFFFFFF) % size;
	}
	/**
	 * Returns a map between the hash coded itemID and the original itemID
	 * @param feedbacks
	 * @return
	 */
	public static void mapItemID(List<Feedback> feedbacks) {
		mapItemIDs = new TreeMap<Long, String>();
		int listSize = feedbacks.size();
		for (Feedback f: feedbacks){
		  long hashItemIDStr = hashCode(f.getItemId(), listSize);
	      mapItemIDs.put(hashItemIDStr, f.getItemId());	
		}
		//Collectors.sort(mapItemIDs);
	}
	
	public static Map<Long, String> getMapItemID(){
		return mapItemIDs;
	}
	
	
	/**
	 * Create a file of feedbacks: userID, itemID, feedbacks value.
	 * @param feedbacks
	 * @return
	 * @throws FileNotFoundException
	 */
	public static File createTransformedFile(List<Feedback> feedbacks) throws FileNotFoundException {
		File resultFile = new File(new File(System.getProperty("java.io.tmpdir")), "feedbacks.txt");
	    if (resultFile.exists()) {
	      resultFile.delete();
	    }
	    PrintWriter writer = new PrintWriter(new FileOutputStream(resultFile));
	    for (Feedback f: feedbacks) {
	    	writer.println(f.getUserId() + "," + f.getItemId() + "," + f.getFeedbacks());
	    }
	    writer.flush();
	    writer.close();
	    
	    return resultFile;
	}

}
