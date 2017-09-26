package de.fraunhofer.cortex.recommender.generic;

import java.io.File;
import java.io.IOException;

import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;

public class SimpleDataModel extends FileDataModel {

	public SimpleDataModel(File dataFile) throws IOException {
		super(transformFile(dataFile));
		// TODO Auto-generated constructor stub
	}
	
	private static File transformFile(File dataFile) {
		return dataFile; //no transformation needed
	}

}
