package de.fraunhofer.cortex.recommender.cf;

import java.io.File;

public class ApplicationConfig {
  
  private File signalsFile = null;
  
  
  public File getSignalsFile() {
    return signalsFile;
  }
  public void setSignalsFile(File signalsFile) {
    this.signalsFile = signalsFile;
  }
  
}
