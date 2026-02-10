//package com.revature.service;
//
//import java.sql.*;
//
//import com.revature.util.DBConnection;
//
//public class OrderService {
//	
//	public void placeOrder(int buyerId, double total) {
//
//	    String orderSql = "INSERT INTO orders VALUES (orders_seq.NEXTVAL,?,?,SYSDATE)";
//
//	    try (Connection con = DBConnection.getConnection()) {
//
//	        con.setAutoCommit(false);
//
//	        PreparedStatement ps = con.prepareStatement(orderSql);
//	        ps.setInt(1, buyerId);
//	        ps.setDouble(2, total);
//	        ps.executeUpdate();
//
//	        // order_items + stock update logic here
//
//	        con.commit();
//	        System.out.println("Order placed successfully!");
//
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	    }
//	}
//
//
//}

package com.revature.service;

import java.sql.*;
import java.util.List;
import java.util.Map;

import com.revature.dao.OrderDAO;
import com.revature.dao.impl.OrderDAOImpl;
import com.revature.model.Order;
import com.revature.model.Product;
import com.revature.util.DBConnection;

public class OrderService {
	
    private OrderDAO orderDAO = new OrderDAOImpl();


    public int checkout(int buyerId){
        return orderDAO.checkout(buyerId);
    }
    
//    public void placeOrder(int buyerId, Map<Integer, Integer> cart) {
//
//        Connection con = null;
//
//        try {
//            con = DBConnection.getConnection();
//            con.setAutoCommit(false); //  START TRANSACTION
//
//            //  INSERT INTO ORDERS
//            String orderSql =
//                "INSERT INTO orders(order_id, buyer_id, total_amount, order_status) " +
//                "VALUES (orders_seq.NEXTVAL, ?, ?, ?)";
//
//            double totalAmount = 0;
//
//            // calculate total
//            for (int qty : cart.values()) {
//                totalAmount += qty * 100; // dummy price for now
//            }
//
//            PreparedStatement orderPs =
//                con.prepareStatement(orderSql, new String[]{"order_id"});
//
//            orderPs.setInt(1, buyerId);
//            orderPs.setDouble(2, totalAmount);
//            orderPs.setString(3, "PLACED");
//            orderPs.executeUpdate();
//
//            // get generated ORDER_ID
//            ResultSet rs = orderPs.getGeneratedKeys();
//            rs.next();
//            int orderId = rs.getInt(1);
//
//            //  INSERT INTO ORDER_ITEMS
//            String itemSql =
//                "INSERT INTO order_items " +
//                "(order_item_id, order_id, product_id, seller_id, quantity, price) " +
//                "VALUES (order_items_seq.NEXTVAL, ?, ?, ?, ?, ?)";
//
//            PreparedStatement itemPs = con.prepareStatement(itemSql);
//
//            //  UPDATE PRODUCT STOCK
//            String stockSql =
//                "UPDATE products SET stock_quantity = stock_quantity - ? " +
//                "WHERE product_id = ?";
//
//            PreparedStatement stockPs = con.prepareStatement(stockSql);
//
//            for (Map.Entry<Integer, Integer> entry : cart.entrySet()) {
//
//                int productId = entry.getKey();
//                int quantity = entry.getValue();
//
//                int sellerId = getSellerIdByProduct(con, productId);
//                double price = getProductPrice(con, productId);
//
//                // insert order item
//                itemPs.setInt(1, orderId);
//                itemPs.setInt(2, productId);
//                itemPs.setInt(3, sellerId);
//                itemPs.setInt(4, quantity);
//                itemPs.setDouble(5, price);
//                itemPs.executeUpdate();
//
//                // update stock
//                stockPs.setInt(1, quantity);
//                stockPs.setInt(2, productId);
//                stockPs.executeUpdate();
//            }
//
//            con.commit(); //  COMMIT TRANSACTION
//            System.out.println(" Order placed successfully!");
//
//        } catch (Exception e) {
//            try {
//                if (con != null) con.rollback(); // ROLLBACK
//            } catch (SQLException ex) {
//                ex.printStackTrace();
//            }
//            e.printStackTrace();
//        }
//    }

    // helper methods
    private int getSellerIdByProduct(Connection con, int productId) throws Exception {
        String sql = "SELECT seller_id FROM products WHERE product_id=?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, productId);
        ResultSet rs = ps.executeQuery();
        rs.next();
        return rs.getInt("seller_id");
    }

    private double getProductPrice(Connection con, int productId) throws Exception {
        String sql = "SELECT price FROM products WHERE product_id=?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, productId);
        ResultSet rs = ps.executeQuery();
        rs.next();
        return rs.getDouble("price");
    }
    
    public List<Order> getOrdersByBuyer(int buyerId){
        return orderDAO.getOrdersByBuyer(buyerId);
    }
    
    public List<Product> getPurchasedProducts(int buyerId){
        return orderDAO.getPurchasedProducts(buyerId);
    }
    
    
    public List<String> getSellerOrders(int sellerId){
        return orderDAO.getSellerOrders(sellerId);
    }



    
    
    
    

}

