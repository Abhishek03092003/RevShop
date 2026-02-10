package com.revature.dao;

import java.util.List;

import com.revature.model.Review;

public interface ReviewDAO {
	
	boolean addReview(Review review);

    boolean hasPurchased(int buyerId, int productId);

    List<Review> getReviewsByProduct(int productId);
    
    List<String> getSellerReviews(int sellerId);


}
