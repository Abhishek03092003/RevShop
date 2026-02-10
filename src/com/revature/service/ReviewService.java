package com.revature.service;

import java.util.List;

import com.revature.dao.ReviewDAO;
import com.revature.dao.impl.ReviewDAOImpl;
import com.revature.model.Review;

public class ReviewService {
	
	private ReviewDAO reviewDAO = new ReviewDAOImpl();

    public boolean addReview(int buyerId, int productId,
                             int rating, String comment){

        if(!reviewDAO.hasPurchased(buyerId, productId)){
            System.out.println("❌ You can review only purchased products.");
            return false;
        }

        Review r = new Review();
        r.setBuyerId(buyerId);
        r.setProductId(productId);
        r.setRating(rating);
        r.setComment(comment);

        return reviewDAO.addReview(r);
    }

    public List<Review> getReviews(int productId){
  
        return reviewDAO.getReviewsByProduct(productId);
    }
    
    public List<String> getSellerReviews(int sellerId){
        return reviewDAO.getSellerReviews(sellerId);
    }


}
