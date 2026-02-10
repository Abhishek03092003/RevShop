package com.revature.model;

public class Review {
	
	private int productId;
    private int buyerId;
    private int rating;
    private String comment;
    
    
    
    
    
	public Review() {
		super();
	}





	public Review(int productId, int buyerId, int rating, String comment) {
		super();
		this.productId = productId;
		this.buyerId = buyerId;
		this.rating = rating;
		this.comment = comment;
	}





	public int getProductId() {
		return productId;
	}





	public void setProductId(int productId) {
		this.productId = productId;
	}





	public int getBuyerId() {
		return buyerId;
	}





	public void setBuyerId(int buyerId) {
		this.buyerId = buyerId;
	}





	public int getRating() {
		return rating;
	}





	public void setRating(int rating) {
		this.rating = rating;
	}





	public String getComment() {
		return comment;
	}





	public void setComment(String comment) {
		this.comment = comment;
	}
	
	
	
	
    
    

}
