package de.fraunhofer.cortex.recommender.cf;

public class ApplicationConfig {
  
  private String signalsFile;
  private String minReloadIntervalMillis;
  
  
  public String getMinReloadIntervalMillis() {
	return minReloadIntervalMillis;
  }
  
  public void setMinReloadIntervalMillis(String minReloadIntervalMillis) {
	this.minReloadIntervalMillis = minReloadIntervalMillis;
  }
  
  public String getSignalsFile() {
    return signalsFile;
  }
  
  public void setSignalsFile(String signalsFile) {
    this.signalsFile = signalsFile;
  }
  
}
