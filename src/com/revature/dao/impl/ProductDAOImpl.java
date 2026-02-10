package com.revature.dao.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.revature.dao.ProductDAO;
import com.revature.model.Product;
import com.revature.util.ConsoleUtil;
import com.revature.util.DBConnection;

public class ProductDAOImpl implements ProductDAO{
	
	

	@Override
	public Product getProductById(int productId) {

	    String sql = "SELECT * FROM products WHERE product_id = ? "+"AND NVL(is_active,1) = 1";

	    try(Connection con = DBConnection.getConnection();
	        PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setInt(1, productId);

	        ResultSet rs = ps.executeQuery();

	        if(rs.next()) {

	            Product p = new Product();

	            p.setProductId(rs.getInt("product_id"));
	            p.setProductName(rs.getString("product_name"));
	            p.setPrice(rs.getDouble("price"));
	            p.setMrp(rs.getDouble("mrp"));
	            p.setDiscountPrice(rs.getDouble("discount_price"));
	            p.setStockQuantity(rs.getInt("stock_quantity"));
	            p.setCategoryId(rs.getInt("category_id"));
	            p.setSellerId(rs.getInt("seller_id"));

	            return p;
	        }

	    } catch(Exception e){
//	        e.printStackTrace();
	        System.out.println("Error fetching product: " + e.getMessage());

	    }

	    return null; // VERY IMPORTANT
	}

	
	@Override
	public void addProduct(Product p) {
//        String sql = "INSERT INTO products(product_id, seller_id, category_id, product_name, price, stock_quantity) "
//                   + "VALUES (products_seq.NEXTVAL,?,?,?,?,?)";
//        try (Connection con = DBConnection.getConnection();
//             PreparedStatement ps = con.prepareStatement(sql)) {
//
//            ps.setInt(1, p.getSellerId());
//            ps.setInt(2, p.getCategoryId());
//            ps.setString(3, p.getProductName());
//            ps.setDouble(4, p.getPrice());
//            ps.setInt(5, p.getStockQuantity());
//            ps.executeUpdate();
		String sql =
		        "INSERT INTO products " +
		        "(seller_id, category_id, product_name, price, mrp, discount_price, " +
		        " stock_quantity, low_stock_threshold) " +
		        "VALUES (?,?,?,?,?,?,?,?)";

		    try (Connection con = DBConnection.getConnection();
		         PreparedStatement ps = con.prepareStatement(sql)) {

		        ps.setInt(1, p.getSellerId());
		        ps.setInt(2, p.getCategoryId());
		        ps.setString(3, p.getProductName());
//		        ps.setDouble(4, p.getPrice());
		        double finalPrice = p.getMrp() - p.getDiscountPrice();
		        ps.setDouble(4, finalPrice);
		        ps.setDouble(5, p.getMrp());            // nullable
		        ps.setDouble(6, p.getDiscountPrice());  // nullable
		        ps.setInt(7, p.getStockQuantity());     // nullable
		        ps.setInt(8, p.getLowStockThreshold()); // nullable

		        ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Product> getAllProducts() {
        List<Product> list = new ArrayList<>();
//        String sql = "SELECT * FROM products";

//        String sql =
//                "SELECT product_id, product_name, price, mrp, discount_price, stock_quantity " +
//                "FROM products ORDER BY product_id";
        String sql=
//                "SELECT p.product_id, " +
//                        "p.product_name, " +
//                        "c.category_name, " +
//                        "p.price, " +
//                        "p.mrp, "+
//                        "p.discount_price, "+
//                        "p.stock_quantity " +
        		"SELECT p.product_id, p.product_name, p.price, p.mrp, " +
                "p.discount_price, p.stock_quantity, c.category_name " +
                "FROM products p " +
                "JOIN categories c ON p.category_id = c.category_id " +
                "WHERE NVL(p.is_active,1) = 1 " +   // ⭐ VERY IMPORTANT
                "ORDER BY p.product_id";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Product p = new Product();
//                p.setProductId(rs.getInt("product_id"));
//                p.setProductName(rs.getString("product_name"));
//                p.setPrice(rs.getDouble("price"));
//                p.setStockQuantity(rs.getInt("stock_quantity"));
//                list.add(p);
                
                p.setProductId(rs.getInt("product_id"));
                p.setProductName(rs.getString("product_name"));
                p.setCategoryName(rs.getString("category_name"));
                p.setPrice(rs.getDouble("price"));
                p.setMrp(rs.getDouble("mrp"));
                p.setDiscountPrice(rs.getDouble("discount_price"));
                p.setStockQuantity(rs.getInt("stock_quantity"));

                list.add(p);
            }
        } catch (Exception e) {
//        	System.out.println("Error in getallproducts");
//            e.printStackTrace();
            System.out.println("Error fetching products: " + e.getMessage());

        }
        return list;
    }
    
    public List<Product> getLowStockProducts(int sellerId){

        List<Product> list = new ArrayList<>();

        String sql =
            "SELECT product_name, stock_quantity " +
            "FROM products " +
            "WHERE seller_id=? " +
            "AND stock_quantity <= low_stock_threshold";

        try(
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
        ){

            ps.setInt(1, sellerId);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){

                Product p = new Product();

                p.setProductName(rs.getString("product_name"));
                p.setStockQuantity(rs.getInt("stock_quantity"));

                list.add(p);
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return list;
    }
    
    @Override
    public List<Product> searchProducts(String keyword){

        List<Product> list = new ArrayList<>();

        String sql ="SELECT product_id, product_name, category_id, " +
                "mrp, discount_price, stock_quantity " +
                "FROM products " +
                "WHERE LOWER(product_name) LIKE LOWER(?) " +
                "AND is_active = 1";
//            "SELECT * FROM products WHERE LOWER(product_name) LIKE LOWER(?)";

        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){

            ps.setString(1, "%" + keyword + "%");

            ResultSet rs = ps.executeQuery();

            while(rs.next()){

                Product p = new Product();

                p.setProductId(rs.getInt("product_id"));
                p.setProductName(rs.getString("product_name"));
                p.setCategoryId(rs.getInt("category_id"));
                p.setMrp(rs.getDouble("mrp"));
                p.setDiscountPrice(rs.getDouble("discount_price"));
                p.setStockQuantity(rs.getInt("stock_quantity"));

                list.add(p);
            }

        }catch(Exception e){
//            ConsoleUtil.error(e.getMessage());
            System.out.println("Error Searching failed: " );

        }

        return list;
    }
    
    
    public boolean deleteProduct(int id){

        String sql = "UPDATE products "
        		+ "SET is_active = 0 "
        		+ "WHERE product_id = ?";

        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){

            ps.setInt(1, id);

//            return ps.executeUpdate()>0;
//            ps.executeUpdate();
            int rows = ps.executeUpdate();

            return rows > 0;


        }catch(Exception e){
            System.out.println(" Failed to delete product");
        }

        return false;
    }


//	@Override
//	public boolean updateProductStock(int productId, int newStock) {
//		// TODO Auto-generated method stub
//		if(newStock < 0){
//	        System.out.println("Stock cannot be negative!");
//	        return false;
//	    }
//
//	    String sql =
//	        "UPDATE products SET stock_quantity=? WHERE product_id=?";
//
//	    try(Connection con = DBConnection.getConnection();
//	        PreparedStatement ps = con.prepareStatement(sql)){
//
//	        ps.setInt(1, newStock);
//	        ps.setInt(2, productId);
//
//	        return ps.executeUpdate() > 0;
//
//	    }catch(Exception e){
//	        System.out.println("Stock update failed: " + e.getMessage());
//	    }
//
//	    return false;
//	}
    @Override
    public boolean updateProductStock(int productId, int newStock){

        String update =
            "UPDATE products SET stock_quantity=? WHERE product_id=?";

        String delete =
            "UPDATE products SET is_active=0 WHERE product_id=?";

        try(Connection con = DBConnection.getConnection()){

            if(newStock <= 0){

                PreparedStatement ps =
                    con.prepareStatement(delete);

                ps.setInt(1, productId);

                ps.executeUpdate();

                System.out.println("⚠ Product removed due to zero stock.");
                return true;
            }

            PreparedStatement ps =
                con.prepareStatement(update);

            ps.setInt(1, newStock);
            ps.setInt(2, productId);

            return ps.executeUpdate()>0;

        }catch(Exception e){
            System.out.println("Stock update failed.");
        }

        return false;
    }

	
	@Override
	public List<Product> getProductsBySeller(int sellerId){

	    List<Product> list = new ArrayList<>();

	    String sql =
	        "SELECT product_id, product_name, price, stock_quantity,mrp " +
	        "FROM products WHERE seller_id=?"+"AND NVL(is_active,1)=1 " +
	        "ORDER BY product_id";

	    try(Connection con = DBConnection.getConnection();
	        PreparedStatement ps = con.prepareStatement(sql)){

	        ps.setInt(1, sellerId);

	        ResultSet rs = ps.executeQuery();

	        while(rs.next()){

	            Product p = new Product();

	            p.setProductId(rs.getInt("product_id"));
	            p.setProductName(rs.getString("product_name"));
	            p.setStockQuantity(rs.getInt("stock_quantity"));
	            p.setMrp(rs.getDouble("mrp"));
	            p.setPrice(rs.getDouble("price"));
	            list.add(p);
	        }

	    }catch(Exception e){
	        System.out.println("Error to load seller products");
	    }

	    return list;
	}
	
	
	@Override
	public boolean adjustStock(int productId, int change){

	    String getStock =
	        "SELECT stock_quantity FROM products WHERE product_id=?";

	    String update =
	        "UPDATE products SET stock_quantity=stock_quantity + ? WHERE product_id=?";

//	    try(Connection con = DBConnection.getConnection()){
//
//	        PreparedStatement gps =
//	            con.prepareStatement(getStock);
//
//	        gps.setInt(1, productId);
//
//	        ResultSet rs = gps.executeQuery();
//
//	        if(!rs.next()){
//	            System.out.println("Product not found.");
//	            return false;
//	        }
//
//	        int currentStock = rs.getInt("stock_quantity");
//
//	        int newStock = currentStock + change;
//
//	        if(newStock < 0){
//	            System.out.println("❌ Cannot reduce below 0.");
//	            return false;
//	        }
//
//	        PreparedStatement ups =
//	            con.prepareStatement(update);
//
//	        ups.setInt(1, newStock);
//	        ups.setInt(2, productId);
//
//	        ups.executeUpdate();
//
//	        System.out.println("✅ Stock updated: " + currentStock + " → " + newStock);
//
//	        return true;
//
//	    }catch(Exception e){
//	        System.out.println("Stock adjustment failed.");
//	    }
//
//	    return false;
	    try(Connection con = DBConnection.getConnection()){

	        // ✅ Get current stock
	        PreparedStatement ps1 = con.prepareStatement(getStock);
	        ps1.setInt(1, productId);

	        ResultSet rs = ps1.executeQuery();

	        if(!rs.next()){
	            System.out.println("❌ Product not found.");
	            return false;
	        }

	        int currentStock = rs.getInt("stock_quantity");
	        int newStock = currentStock + change;

	        // ✅ Prevent negative stock
	        if(newStock < 0){
	            System.out.println("❌ Cannot reduce below 0.");
	            return false;
	        }

	        // ✅ Update stock
	        PreparedStatement ps2 = con.prepareStatement(update);
	        ps2.setInt(1, change);
	        ps2.setInt(2, productId);

	        ps2.executeUpdate();

	        // ⭐⭐⭐ WRITE THIS HERE ⭐⭐⭐
	        if(newStock == 0){
	            System.out.println("⚠ Product is now OUT OF STOCK.");
	        }

	        return true;

	    }catch(Exception e){
	        System.out.println("Stock update failed: " + e.getMessage());
	    }

	    return false;
	}


	@Override
	public boolean setStock(int productId, int newStock) {

	    if(newStock < 0){
	        System.out.println("❌ Stock cannot be negative!");
	        return false;
	    }

	    String sql =
	        "UPDATE products SET stock_quantity=? WHERE product_id=?";

	    try(Connection con = DBConnection.getConnection();
	        PreparedStatement ps = con.prepareStatement(sql)){

	        ps.setInt(1, newStock);
	        ps.setInt(2, productId);

	        int rows = ps.executeUpdate();

	        if(rows == 0){
	            System.out.println("❌ Product not found.");
	            return false;
	        }

	        // ⭐ Out of stock warning
	        if(newStock == 0){
	            System.out.println("⚠ Product is now OUT OF STOCK.");
	        }

	        return true;

	    }catch(Exception e){
	        System.out.println("Stock update failed: " + e.getMessage());
	    }

	    return false;
	}

	@Override
	public boolean updatePrice(int productId, double mrp, double discount){

	    if(discount > mrp){
	        System.out.println("Discount cannot exceed MRP.");
	        return false;
	    }

	    double finalPrice = mrp - discount;

	    String sql =
	        "UPDATE products " +
	        "SET mrp=?, discount_price=?, price=? " +
	        "WHERE product_id=?";

	    try(Connection con = DBConnection.getConnection();
	        PreparedStatement ps = con.prepareStatement(sql)){

	        ps.setDouble(1, mrp);
	        ps.setDouble(2, discount);
	        ps.setDouble(3, finalPrice);
	        ps.setInt(4, productId);

//	        ps.executeUpdate();
	        
	        return ps.executeUpdate() > 0;

//	        System.out.println("✅ Price updated successfully!");

//	        return true;

	    }catch(Exception e){
	        System.out.println("Price update failed."+ e.getMessage());
	    }

	    return false;
	}






}
