package de.fraunhofer.cortex.recommender.model;

import java.io.File;
import java.io.IOException;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;


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
