package com.revature.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.revature.dao.ReviewDAO;
import com.revature.model.Review;
import com.revature.util.DBConnection;

public class ReviewDAOImpl implements ReviewDAO{
	
	public boolean hasPurchased(int buyerId, int productId){

	    String sql =
	        "SELECT 1 FROM order_items oi " +
	        "JOIN orders o ON oi.order_id = o.order_id " +
	        "WHERE o.buyer_id=? AND oi.product_id=?";

	    try(
	        Connection con = DBConnection.getConnection();
	        PreparedStatement ps = con.prepareStatement(sql);
	    ){

	        ps.setInt(1, buyerId);
	        ps.setInt(2, productId);

	        ResultSet rs = ps.executeQuery();

	        return rs.next();

	    }catch(Exception e){
	        e.printStackTrace();
	    }

	    return false;
	}
	
	public boolean addReview(Review r){

	    String sql =
	        "INSERT INTO reviews(review_id, product_id, buyer_id, rating, review_comment) " +
	        "VALUES(reviews_seq.NEXTVAL, ?, ?, ?, ?)";

	    try(
	        Connection con = DBConnection.getConnection();
	        PreparedStatement ps = con.prepareStatement(sql);
	    ){

	        ps.setInt(1, r.getProductId());
	        ps.setInt(2, r.getBuyerId());
	        ps.setInt(3, r.getRating());
	        ps.setString(4, r.getComment());

	        return ps.executeUpdate() > 0;

	    }catch(Exception e){

	        // catches duplicate review too 🙂
	        System.out.println("⚠ You already reviewed this product.");
	    }

	    return false;
	}

	
	public List<Review> getReviewsByProduct(int productId){

	    List<Review> list = new ArrayList<>();

	    String sql =
	        "SELECT rating, review_comment " +
	        "FROM reviews WHERE product_id=?";

	    try(
	        Connection con = DBConnection.getConnection();
	        PreparedStatement ps = con.prepareStatement(sql);
	    ){

	        ps.setInt(1, productId);

	        ResultSet rs = ps.executeQuery();

	        while(rs.next()){

	            Review r = new Review();

	            r.setRating(rs.getInt("rating"));
	            r.setComment(rs.getString("review_comment"));

	            list.add(r);
	        }

	    }catch(Exception e){
	        e.printStackTrace();
	    }

	    return list;
	}

	
	public List<String> getSellerReviews(int sellerId){

	    List<String> reviews = new ArrayList<>();

	    String sql =
	        "SELECT p.product_name, r.rating, r.review_comment " +
	        "FROM reviews r " +
	        "JOIN products p ON r.product_id = p.product_id " +
	        "WHERE p.seller_id=?";

	    try(
	        Connection con = DBConnection.getConnection();
	        PreparedStatement ps = con.prepareStatement(sql);
	    ){

	        ps.setInt(1, sellerId);

	        ResultSet rs = ps.executeQuery();

	        while(rs.next()){

	            String row =
	                rs.getString("product_name") +
	                " | ⭐" + rs.getInt("rating") +
	                " | " + rs.getString("review_comment");

	            reviews.add(row);
	        }

	    }catch(Exception e){
	        e.printStackTrace();
	    }

	    return reviews;
	}



}
