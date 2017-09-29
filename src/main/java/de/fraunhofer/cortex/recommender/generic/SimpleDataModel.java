package de.fraunhofer.cortex.recommender.generic;

import java.io.File;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;

public class SimpleDataModel extends FileDataModel {
	
	static final Logger LOG = LoggerFactory.getLogger(SimpleDataModel.class);

	public SimpleDataModel(File dataFile) throws IOException {
		super(transformFile(dataFile));
		
	}
	/**
	 * This method returns a file with records: userID, itemID, value  
	 * @param dataFile
	 * @return
	 */
	private static File transformFile(File dataFile) {
		LOG.info("Source data file transformed.");
		return dataFile; //no transformation needed
	}

}
