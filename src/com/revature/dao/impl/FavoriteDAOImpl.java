package com.revature.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.revature.dao.FavoriteDAO;
import com.revature.model.Favorite;
import com.revature.model.Product;
import com.revature.util.DBConnection;

public class FavoriteDAOImpl implements FavoriteDAO{

	
	@Override
    public boolean addFavorite(int buyerId, int productId){

        String checkStock =
            "SELECT stock_quantity FROM products WHERE product_id=?";

        String insert =
            "INSERT INTO favorites(favorite_id, buyer_id, product_id) " +
            "VALUES(favorites_seq.NEXTVAL, ?, ?)";

        try(Connection con = DBConnection.getConnection()){

            PreparedStatement stockPs =
                con.prepareStatement(checkStock);

            stockPs.setInt(1, productId);

            ResultSet rs = stockPs.executeQuery();

            if(rs.next()){

                if(rs.getInt("stock_quantity") <= 0){

                    System.out.println("❌ Out of stock — cannot favorite.");
                    return false;
                }
            }

            PreparedStatement ps =
                con.prepareStatement(insert);

            ps.setInt(1, buyerId);
            ps.setInt(2, productId);

            return ps.executeUpdate() > 0;

        }catch(Exception e){
            System.err.println(e.getMessage());


//            System.out.println("⚠ Already in favorites.");
        }

        return false;
    }

    @Override
    public boolean removeFavorite(int buyerId, int productId){

        String sql =
            "DELETE FROM favorites WHERE buyer_id=? AND product_id=?";

        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){

            ps.setInt(1, buyerId);
            ps.setInt(2, productId);

            return ps.executeUpdate() > 0;
        }
        catch(Exception e){
//            e.printStackTrace();
            System.err.println(e.getMessage());

        }

        return false;
    }

    @Override
    public boolean exists(int buyerId, int productId){

        String sql =
            "SELECT 1 FROM favorites WHERE buyer_id=? AND product_id=?";

        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){

            ps.setInt(1, buyerId);
            ps.setInt(2, productId);

            ResultSet rs = ps.executeQuery();

            return rs.next();
        }
        catch(Exception e){
//            e.printStackTrace();
            System.err.println(e.getMessage());

        }

        return false;
    }

    @Override
    public List<Product> getFavorites(int buyerId){

        List<Product> list = new ArrayList<>();

        String sql =
            "SELECT p.product_id, p.product_name, p.mrp, p.stock_quantity " +
            "FROM favorites f " +
            "JOIN products p ON f.product_id = p.product_id " +
            "WHERE f.buyer_id=? "+"AND NVL(p.is_active,1) = 1";

        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){

            ps.setInt(1, buyerId);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){

                Product f = new Product();

                f.setProductId(rs.getInt("product_id"));
                f.setProductName(rs.getString("product_name"));
                f.setMrp(rs.getDouble("mrp"));
//                f.setStock(rs.getInt("stock_quantity"));

                list.add(f);
            }

        }catch(Exception e){
//            e.printStackTrace();
            System.err.println(e.getMessage());

        }

        return list;
    }
	
}
