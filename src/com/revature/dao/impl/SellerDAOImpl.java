package com.revature.dao.impl;

import java.sql.*;
import com.revature.dao.SellerDAO;
import com.revature.model.Seller;
import com.revature.util.DBConnection;

public class SellerDAOImpl implements SellerDAO {

    @Override
    public void createSeller(int userId, String business, String gst, String address) {
        String sql = "INSERT INTO sellers(user_id, business_name, gst_number, address) VALUES (?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, business);
            ps.setString(3, gst);
            ps.setString(4, address);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Seller getSellerIdByUserId(int userId) {
        String sql = "SELECT * FROM sellers WHERE user_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {

                Seller s = new Seller();

                s.setSellerId(rs.getInt("seller_id"));
                s.setUserId(rs.getInt("user_id"));
                s.setBusinessName(
                        rs.getString("business_name"));
                s.setGstNumber(
                        rs.getString("gst_number"));
                s.setAddress(
                        rs.getString("address"));

                return s;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
