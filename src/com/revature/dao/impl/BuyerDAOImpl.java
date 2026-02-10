package com.revature.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.revature.dao.*;
import com.revature.model.Buyer;
import com.revature.util.DBConnection;

public class BuyerDAOImpl implements BuyerDAO{
	
//	@Override
//	public void createBuyer(int userId, String ship, String bill) {
//	    String sql = "INSERT INTO buyers VALUES (buyers_seq.NEXTVAL,?,?,?)";
//	    try (Connection con = DBConnection.getConnection();
//	         PreparedStatement ps = con.prepareStatement(sql)) {
//
//	        ps.setInt(1, userId);
//	        ps.setString(2, ship);
//	        ps.setString(3, bill);
//	        ps.executeUpdate();
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	    }
//	}
	
	@Override
    public void createBuyer(Buyer buyer) {

        String sql = "INSERT INTO buyers (buyer_id, user_id, shipping_address, billing_address) "
                   + "VALUES (buyers_seq.NEXTVAL, ?, ?, ?)";

        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, buyer.getUserId());
            ps.setString(2, buyer.getShippingAddress());
            ps.setString(3, buyer.getBillingAddress());

            ps.executeUpdate();

        } catch(Exception e){
        	System.out.println("Buyer creation failed: " + e.getMessage());        }
    }
	
	public Buyer getBuyerByUserId(int userId){

	    String sql = "SELECT * FROM buyers WHERE user_id=?";

	    try(Connection con = DBConnection.getConnection();
	        PreparedStatement ps = con.prepareStatement(sql)){

	        ps.setInt(1, userId);

	        ResultSet rs = ps.executeQuery();

	        if(rs.next()){

	            Buyer b = new Buyer();

	            b.setBuyerId(rs.getInt("buyer_id"));
	            b.setUserId(rs.getInt("user_id"));
	            b.setShippingAddress(rs.getString("shipping_address"));
	            b.setBillingAddress(rs.getString("billing_address"));

	            return b;
	        }

	    }catch(Exception e){
	    	System.out.println("Buyer creation failed: " + e.getMessage());
	    }

	    return null;
	}

	


//	@Override
//	public int getBuyerIdByUserId(int userId) {
//		// TODO Auto-generated method stub
//		String sql = "SELECT buyer_id FROM buyers WHERE user_id=?";
//        try (Connection con = DBConnection.getConnection();
//             PreparedStatement ps = con.prepareStatement(sql)) {
//
//            ps.setInt(1, userId);
//            ResultSet rs = ps.executeQuery();
//            if (rs.next()) return rs.getInt("buyer_id");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return 0;
//	}


}
