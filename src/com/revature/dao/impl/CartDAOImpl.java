package com.revature.dao.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.revature.dao.CartDAO;
import com.revature.model.CartItem;
import com.revature.util.DBConnection;

public class CartDAOImpl implements CartDAO{

//	@Override
//	public void addToCart(int buyerId, int productId, int qty) {
//	    String sql = "INSERT INTO cart_items VALUES (cart_seq.NEXTVAL,?,?,?)";
//	    try (Connection con = DBConnection.getConnection();
//	         PreparedStatement ps = con.prepareStatement(sql)) {
//
//	        ps.setInt(1, buyerId);
//	        ps.setInt(2, productId);
//	        ps.setInt(3, qty);
//	        ps.executeUpdate();
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	    }
//	}

	@Override
	public int getCartIdByBuyerId(int buyerId) {
		// TODO Auto-generated method stub
		String sql = "SELECT cart_id FROM cart WHERE buyer_id=?";

	    try(Connection con = DBConnection.getConnection();
	        PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setInt(1, buyerId);

	        ResultSet rs = ps.executeQuery();

	        if(rs.next())
	            return rs.getInt("cart_id");

	    } catch(Exception e){
//	        e.printStackTrace();
	    	System.out.println("Cart Id didnt get fetchet by buyed id: " + e.getMessage());
	    }

	    return 0;
	}

	@Override
	public boolean productExistsInCart(int cartId, int productId) {
		// TODO Auto-generated method stub
		String sql =
		        "SELECT 1 FROM cart_items WHERE cart_id=? AND product_id=?";

		    try(Connection con = DBConnection.getConnection();
		        PreparedStatement ps = con.prepareStatement(sql)) {

		        ps.setInt(1, cartId);
		        ps.setInt(2, productId);

//		        ResultSet rs = ps.executeQuery();
//
//		        return rs.next();
		        try(ResultSet rs = ps.executeQuery()){
		            return rs.next();
		        }

		    } catch(Exception e){
		        e.printStackTrace();
		    }
		return false;
	}

	@Override
	public boolean addToCart(int cartId, int productId, int qty) {
		// TODO Auto-generated method stub
		String sql =
		        "INSERT INTO cart_items(cart_id, product_id, quantity) VALUES(?,?,?)";

		    try(Connection con = DBConnection.getConnection();
		        PreparedStatement ps = con.prepareStatement(sql)) {

		        ps.setInt(1, cartId);
		        ps.setInt(2, productId);
		        ps.setInt(3, qty);

//		        ps.executeUpdate();
//		        return true;
		        return ps.executeUpdate() > 0;


		    } catch(Exception e){
		        e.printStackTrace();
		    }
		return false;
	}

	@Override
	public boolean updateCartQuantity(int cartId, int productId, int qty) {
		// TODO Auto-generated method stub
		String sql =
		        "UPDATE cart_items SET quantity = quantity + ? " +
		        "WHERE cart_id=? AND product_id=?";

		    try(Connection con = DBConnection.getConnection();
		        PreparedStatement ps = con.prepareStatement(sql)) {

		        ps.setInt(1, qty);
		        ps.setInt(2, cartId);
		        ps.setInt(3, productId);

//		        ps.executeUpdate();
		        return ps.executeUpdate()>0;

		    } catch(Exception e){
		        e.printStackTrace();
		    }
		return false;
	}
	
	@Override
	public void createCart(int buyerId) {

	    String sql = "INSERT INTO cart(buyer_id) VALUES(?)";

	    try(Connection con = DBConnection.getConnection();
	        PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setInt(1, buyerId);
	        ps.executeUpdate();

	    } catch(Exception e){
	        e.printStackTrace();
	    }
	}
	
	public List<CartItem> getCartItems(int buyerId){

	    List<CartItem> list = new ArrayList<>();

	    String sql ="SELECT p.product_id, " +
	            "p.product_name, " +
	            "(p.mrp - p.discount_price) AS final_price, " +
	            "ci.quantity, " +
	            "((p.mrp - p.discount_price) * ci.quantity) AS subtotal " +
	            "FROM cart_items ci " +
	            "JOIN cart c ON ci.cart_id = c.cart_id " +
	            "JOIN products p ON ci.product_id = p.product_id " +
	            "WHERE c.buyer_id = ?";
//	        "SELECT p.product_id, p.product_name, p.mrp, ci.quantity, " +
//	        "(p.mrp * ci.quantity) AS subtotal " +
//	        "FROM cart_items ci " +
//	        "JOIN cart c ON ci.cart_id = c.cart_id " +
//	        "JOIN products p ON ci.product_id = p.product_id " +
//	        "WHERE c.buyer_id = ?";

	    try(
	        Connection con = DBConnection.getConnection();
	        PreparedStatement ps = con.prepareStatement(sql);
	    ){

	        ps.setInt(1, buyerId);

	        try(ResultSet rs = ps.executeQuery()){

	            while(rs.next()){

	                CartItem item = new CartItem();
	                
	                item.setProductId(
	                	    rs.getInt("product_id"));


	                item.setProductName(
	                        rs.getString("product_name"));

	                item.setPrice(
	                        rs.getDouble("final_price"));

	                item.setQuantity(
	                        rs.getInt("quantity"));

	                item.setSubtotal(
	                        rs.getDouble("subtotal"));

	                list.add(item);
	            }
	        }

	    }catch(Exception e){
//	        e.printStackTrace();
	    	System.out.println("Error loading cart items.");
	    }

	    return list;
	}
	
	public boolean removeItem(int cartId, int productId){

	    String sql =
	        "DELETE FROM cart_items WHERE cart_id=? AND product_id=?";

	    try(Connection con = DBConnection.getConnection();
	        PreparedStatement ps = con.prepareStatement(sql)){

	        ps.setInt(1, cartId);
	        ps.setInt(2, productId);

	        return ps.executeUpdate()>0;
	    }
	    catch(Exception e){
//	        e.printStackTrace();
	        System.err.println(e.getMessage());

	    }

	    return false;
	}


	public boolean decreaseQty(int cartId, int productId){

	    String check =
	        "SELECT quantity FROM cart_items WHERE cart_id=? AND product_id=?";

	    String update =
	        "UPDATE cart_items SET quantity = quantity-1 WHERE cart_id=? AND product_id=?";

	    try(Connection con = DBConnection.getConnection()){

	        PreparedStatement cps = con.prepareStatement(check);
	        cps.setInt(1, cartId);
	        cps.setInt(2, productId);

	        ResultSet rs = cps.executeQuery();

	        if(rs.next()){

	            if(rs.getInt("quantity") <= 1){
	                return removeItem(cartId, productId);
	            }
	        }

	        PreparedStatement ups = con.prepareStatement(update);

	        ups.setInt(1, cartId);
	        ups.setInt(2, productId);

	        return ups.executeUpdate()>0;

	    }catch(Exception e){
	        e.printStackTrace();
	    }

	    return false;
	}

	public boolean increaseQty(int cartId, int productId, int stock){

	    String current ="SELECT ci.quantity "
	    		+ "FROM cart_items ci "
	    		+ "JOIN products p ON ci.product_id = p.product_id "
	    		+ "WHERE ci.cart_id=? "
	    		+ "AND ci.product_id=? "
	    		+ "AND NVL(p.is_active,1)=1";
//	        "SELECT quantity FROM cart_items WHERE cart_id=? AND product_id=?";

	    String update =
	        "UPDATE cart_items SET quantity = quantity+1 WHERE cart_id=? AND product_id=?";

	    try(Connection con = DBConnection.getConnection()){

	        PreparedStatement ps = con.prepareStatement(current);
	        ps.setInt(1, cartId);
	        ps.setInt(2, productId);

	        ResultSet rs = ps.executeQuery();

	        if(rs.next()){

	            if(rs.getInt("quantity") >= stock){

	                System.out.println("❌ Only " + stock + " left!");
	                return false;
	            }
	        }

	        PreparedStatement ups = con.prepareStatement(update);

	        ups.setInt(1, cartId);
	        ups.setInt(2, productId);

	        return ups.executeUpdate()>0;

	    }catch(Exception e){
	        e.printStackTrace();
	    }

	    return false;
	}



}
