package de.fraunhofer.cortex.recommender.atn;

public class Record implements Comparable<Record> {
	
	private String userID;
	private String itemID;
	private String recordKey;
	
	public Record(String userID, String itemID) {
		if(userID == null || itemID == null)
			throw new NullPointerException();
		this.userID = userID;
		this.itemID = itemID;
		this.recordKey = userID + itemID;
	}
	
	public String getUserID() {
		return userID;
	}
	
	public String getItemID() {
		return itemID;
	}
	
	public String getRecordKey() {
		return recordKey;
	}
	
	/**
	 * Two records are equals when they have the same userID and itemID
	 */
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Record))
			return false;
		Record r = (Record)obj;
		return r.getUserID().equals(userID) && r.getItemID().equals(itemID);
	}
	
	public int hashCode() {
		return userID.hashCode() + itemID.hashCode();
	}
	
	@Override
	public String toString() {
		return userID + "_" + itemID;
	}

	/**
	 * Two records can be compared and sorted using the key
	 */
	@Override
	public int compareTo(Record r) {
		return recordKey.compareTo(r.getRecordKey());
	}
	

}
