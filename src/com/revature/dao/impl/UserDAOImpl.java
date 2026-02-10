package com.revature.dao.impl;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.revature.dao.UserDAO;
import com.revature.model.User;
import com.revature.util.ConsoleUtil;
import com.revature.util.DBConnection;

public class UserDAOImpl implements UserDAO {

//    private Connection con = DBConnection.getConnection();

    @Override
    public boolean registerUser(User user) {
    	String sql = "INSERT INTO users(name,email,password,role) VALUES(?,?,?,?)";

        try(
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
        ){
//        	PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getRole());
            return ps.executeUpdate() > 0;
//        } catch (Exception e) {
//            e.printStackTrace();
//            if(e.getMessage().contains("UNIQUE")){
//        	    System.out.println("❌ Email already exists.");
//        	}else{
//        	    System.out.println("Registration failed: " + e.getMessage());
//        	}
//            
//
//        }
        }catch(SQLException e){

                if(e.getErrorCode() == 1){ // ORA-00001
                    ConsoleUtil.error("Email already exists!");
                }else{
                    ConsoleUtil.error("Database error during registration.");
                }
            }

        return false;
    }
    
    public int getUserIdByEmail(String email) {

        String sql = "SELECT user_id FROM users WHERE email=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("user_id");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    @Override
    public User login(String email, String password) {
    	String sql = "SELECT * FROM users WHERE email=? AND password=?";

        try(
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
        ){
        	ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User u = new User();
                u.setUserId(rs.getInt("user_id"));
                u.setName(rs.getString("name"));
                u.setRole(rs.getString("role"));
                return u;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public boolean changePassword(int userId, String newPassword){

        String sql =
            "UPDATE users SET password=? WHERE user_id=?";

        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){

            ps.setString(1, newPassword);
            ps.setInt(2, userId);

            return ps.executeUpdate()>0;

        }catch(Exception e){
            ConsoleUtil.error(e.getMessage());
        }

        return false;
    }

    @Override
    public boolean resetPassword(String email, String newPassword){

        String sql =
            "UPDATE users SET password=? WHERE email=?";

        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){

            ps.setString(1, newPassword);
            ps.setString(2, email);

            return ps.executeUpdate()>0;

        }catch(Exception e){
            ConsoleUtil.error(e.getMessage());
        }

        return false;
    }
    
    
    @Override
    public boolean emailExists(String email){

        String sql =
            "SELECT 1 FROM users WHERE LOWER(email) = LOWER(?)";

        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){

            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();

            return rs.next();

        }catch(Exception e){
            System.out.println("Email check failed: " + e.getMessage());
        }

        return false;
    }


}

