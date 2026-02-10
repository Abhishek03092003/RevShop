package com.revature.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.revature.dao.OrderDAO;
import com.revature.model.Order;
import com.revature.model.Product;
import com.revature.util.DBConnection;

public class OrderDAOImpl implements OrderDAO{
	
    @Override
	public int checkout(int buyerId){

	    String createOrder =
	        "INSERT INTO orders(order_id, buyer_id, total_amount, order_status) " +
	        "VALUES(orders_seq.NEXTVAL, ?, ?, 'PLACED')";

	    String getCart =
	    		"SELECT ci.product_id, ci.quantity, p.price, p.seller_id " +
	    			    "FROM cart_items ci " +
	    			    "JOIN cart c ON ci.cart_id = c.cart_id " +
	    			    "JOIN products p ON ci.product_id = p.product_id " +
	    			    "WHERE c.buyer_id=?";
//	        "SELECT ci.product_id, ci.quantity, p.price " +
//	        "FROM cart_items ci " +
//	        "JOIN cart c ON ci.cart_id = c.cart_id " +
//	        "JOIN products p ON ci.product_id = p.product_id " +
//	        "WHERE c.buyer_id=?";

	    String insertOrderItem =
	        "INSERT INTO order_items(order_item_id, order_id, product_id, seller_id, quantity, price) " +
	        "VALUES(order_items_seq.NEXTVAL, ?, ?, ?, ?, ?)";

	    String updateStock =
	        "UPDATE products SET stock_quantity = stock_quantity - ? WHERE product_id=?";

	    String clearCart =
	        "DELETE FROM cart_items WHERE cart_id = (SELECT cart_id FROM cart WHERE buyer_id=?)";

	    try(Connection con = DBConnection.getConnection()){

	        con.setAutoCommit(false); //  START TRANSACTION

	        // 1 calculate total
	        double total = 0;

	        PreparedStatement cartPs = con.prepareStatement(getCart);
	        cartPs.setInt(1, buyerId);

	        ResultSet rs = cartPs.executeQuery();

	        List<Object[]> items = new ArrayList<>();

	        while(rs.next()){

//	            int productId = rs.getInt("product_id");
//	            int qty = rs.getInt("quantity");
//	            double price = rs.getDouble("price");

	        	int productId = rs.getInt("product_id");
	        	int qty = rs.getInt("quantity");
	        	double price = rs.getDouble("price");
	        	int sellerId = rs.getInt("seller_id");
	        	if(qty > getCurrentStock(con, productId)){
                    throw new SQLException("Insufficient stock for product ID: " + productId);
                }

//                total += price * qty;
	            total += price * qty;

	            items.add(new Object[]{productId, qty, price,sellerId});
	        }

	        if(items.isEmpty()){
	            System.out.println("Cart empty!");
	            return 0;
	        }

	        // 2 create order
	        PreparedStatement orderPs =
	            con.prepareStatement(createOrder, new String[]{"order_id"});

	        orderPs.setInt(1, buyerId);
	        orderPs.setDouble(2, total);

	        orderPs.executeUpdate();

	        ResultSet orderKey = orderPs.getGeneratedKeys();
	        orderKey.next();

	        int orderId = orderKey.getInt(1);
	        

	        // 3 insert order items + reduce stock
	        for(Object[] item : items){

	            int productId =(int) item[0];
	            int qty = (int)item[1];
	            double price =(double) item[2];
	            int sellerId =(int) item[3];

	            PreparedStatement itemPs =
	                con.prepareStatement(insertOrderItem);

	            itemPs.setInt(1, orderId);
	            itemPs.setInt(2, productId);
//	            itemPs.setInt(3, 1); // sellerId (simplify for now)
	            itemPs.setInt(3, sellerId);

	            itemPs.setInt(4, qty);
	            itemPs.setDouble(5, price);

	            itemPs.executeUpdate();

	            PreparedStatement stockPs =
	                con.prepareStatement(updateStock);

	            stockPs.setInt(1, qty);
	            stockPs.setInt(2, productId);

	            stockPs.executeUpdate();
	        }

	        // 4 clear cart
	        PreparedStatement clearPs =
	            con.prepareStatement(clearCart);

	        clearPs.setInt(1, buyerId);
	        clearPs.executeUpdate();

	        con.commit(); //  SUCCESS

	        return orderId;

	    }catch(Exception e){
//	        e.printStackTrace();
            System.out.println("Checkout failed: " + e.getMessage());

	        try(Connection con = DBConnection.getConnection()){
	            if(con != null){
	                con.rollback();
	            }
	        }catch(Exception ex){
//	            ex.printStackTrace();
                System.out.println("Rollback failed.");

	        }
	    
	    }

	    return 0;
	}

    private int getCurrentStock(Connection con, int productId) throws Exception{

        String sql =
            "SELECT stock_quantity FROM products WHERE product_id=?";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, productId);

        ResultSet rs = ps.executeQuery();
        rs.next();

        return rs.getInt(1);
    }
    
	@Override
	public List<Order> getOrdersByBuyer(int buyerId) {
		// TODO Auto-generated method stub
		List<Order> orders = new ArrayList<>();

	    String sql =
	        "SELECT order_id, total_amount, order_status, order_date " +
	        "FROM orders " +
	        "WHERE buyer_id=? " +
	        "ORDER BY order_date DESC";

	    try(
	        Connection con = DBConnection.getConnection();
	        PreparedStatement ps = con.prepareStatement(sql);
	    ){

	        ps.setInt(1, buyerId);

	        try(ResultSet rs = ps.executeQuery()){

	            while(rs.next()){

	                Order order = new Order();

	                order.setOrderId(
	                        rs.getInt("order_id"));

	                order.setTotalAmount(
	                        rs.getDouble("total_amount"));

	                order.setStatus(
	                        rs.getString("order_status"));

	                order.setOrderDate(
	                        rs.getTimestamp("order_date"));

	                orders.add(order);
	            }
	        }

	    }catch(Exception e){
	        e.printStackTrace();
	    }

	    return orders;
	}
	
	
	public List<Product> getPurchasedProducts(int buyerId){

	    List<Product> list = new ArrayList<>();

	    String sql =
	        "SELECT DISTINCT p.product_id, p.product_name " +
	        "FROM products p " +
	        "JOIN order_items oi ON p.product_id = oi.product_id " +
	        "JOIN orders o ON oi.order_id = o.order_id " +
	        "WHERE o.buyer_id=?";

	    try(
	        Connection con = DBConnection.getConnection();
	        PreparedStatement ps = con.prepareStatement(sql);
	    ){

	        ps.setInt(1, buyerId);

	        ResultSet rs = ps.executeQuery();

	        while(rs.next()){

	            Product p = new Product();

	            p.setProductId(rs.getInt("product_id"));
	            p.setProductName(rs.getString("product_name"));

	            list.add(p);
	        }

	    }catch(Exception e){
	        e.printStackTrace();
	    }

	    return list;
	}
	
	public List<String> getSellerOrders(int sellerId){

	    List<String> orders = new ArrayList<>();

	    String sql =
	        "SELECT o.order_id, p.product_name, u.name, " +
	        "oi.quantity, oi.price, o.order_date " +
	        "FROM orders o " +
	        "JOIN order_items oi ON o.order_id = oi.order_id " +
	        "JOIN products p ON oi.product_id = p.product_id " +
	        "JOIN buyers b ON o.buyer_id = b.buyer_id "+ 
	        "JOIN users u ON b.user_id = u.user_id "+
	        "WHERE p.seller_id=? " +
	        "ORDER BY o.order_date DESC";

	    try(
	        Connection con = DBConnection.getConnection();
	        PreparedStatement ps = con.prepareStatement(sql);
	    ){

	        ps.setInt(1, sellerId);

	        ResultSet rs = ps.executeQuery();

	        while(rs.next()){

	            String row =
	                    "Order#" + rs.getInt("order_id") +
	                    " | " + rs.getString("product_name") +
	                    " | Buyer: " + rs.getString("name") +
	                    " | Qty: " + rs.getInt("quantity") +
	                    " | Ru:" + rs.getDouble("price") +
	                    " | " + rs.getTimestamp("order_date");

	            orders.add(row);
	        }

	    }catch(Exception e){
	        e.printStackTrace();
	    }

	    return orders;
	}




}
