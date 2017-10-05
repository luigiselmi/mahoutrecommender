package de.fraunhofer.cortex.recommender.atn;

public class Feedback implements Comparable<Feedback> {
	
	private String userID;
	private String itemID;
	private long hashItemID;
	private int feedbacks = 0;
	private String key;

	public Feedback(String userID, String itemID) {
		this.userID = userID;
		this.itemID = itemID;
		this.key = itemID + userID;
	}
	
	public String getUserID() {
		return userID;
	}

	public String getItemID() {
		return itemID;
	}

	public long getHashItemID() {
		return hashItemID;
	}
	
	public void setHashItemID(int hashItemID) {
		this.hashItemID = hashItemID;
	}

	public int getFeedbacks() {
		return feedbacks;
	}

	public void setFeedbacks(int feedbacks) {
		this.feedbacks = feedbacks;
	}

	public String getKey() {
		return key;
	}
	
	
	@Override
	public int compareTo(Feedback f) {
		return key.compareTo(f.getKey());
	}

}
