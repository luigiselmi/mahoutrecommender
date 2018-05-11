package de.fraunhofer.cortex.recommender.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.MemoryIDMigrator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.model.UpdatableIDMigrator;


/**
 * The signals file contains the following records
 * 
 * userID (long), itemID (string), value (double) 
 * 
 * The itemID must be transformed from string to long
 * in order to be used for the recommendations.
 * @author luigi
 *
 */
public class SignalsDataModel extends FileDataModel {
  public static final String COLON_DELIMTER = ",";
  public ItemMemIDMigrator itemIdMigrator;
  
  public SignalsDataModel(File dataFile, long reloadIntervalMillis) throws IOException, TasteException {
    
    super(dataFile, false, reloadIntervalMillis, COLON_DELIMTER); 
    
  }
  
  @Override
  protected long readItemIDFromString(String stringID) {
    
    if(itemIdMigrator == null) {
      itemIdMigrator = new ItemMemIDMigrator();
    }
    
    long longID = itemIdMigrator.toLongID(stringID);
    
    if(null == itemIdMigrator.toStringID(longID)) {
      try {
        itemIdMigrator.singleInit(stringID);
      }
      catch(TasteException te) {
        te.printStackTrace();
      }
    }
    
    return longID;
    
  }
  
  public String getItemIDAsString(long longID) throws TasteException {
    return itemIdMigrator.toStringID(longID);
  }
 
}
