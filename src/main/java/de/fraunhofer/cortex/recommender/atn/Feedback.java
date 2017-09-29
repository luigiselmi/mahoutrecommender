package de.fraunhofer.cortex.recommender.atn;

public class Feedback {
	
	private String userId = "";
	private String itemId = "";
	private int feedbacks = 0;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public int getFeedbacks() {
		return feedbacks;
	}
	public void setFeedbacks(int feedbacks) {
		this.feedbacks = feedbacks;
	}

}
