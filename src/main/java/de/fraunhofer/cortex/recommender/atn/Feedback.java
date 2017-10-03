package de.fraunhofer.cortex.recommender.atn;

public class Feedback implements Comparable<Feedback> {
	
	private String userId;
	private String itemId;
	private int feedbacks = 0;
	private String key;
	
	public Feedback(String userID, String itemID) {
		this.userId = userID;
		this.itemId = itemID;
		this.key = itemID + userID;
	}
	
	public String getUserId() {
		return userId;
	}
	
	public String getItemId() {
		return itemId;
	}
	
	public String getKey() {
		return key;
	}
	
	public int getFeedbacks() {
		return feedbacks;
	}
	public void setFeedbacks(int feedbacks) {
		this.feedbacks = feedbacks;
	}
	@Override
	public int compareTo(Feedback f) {
		return key.compareTo(f.getKey());
	}

}
