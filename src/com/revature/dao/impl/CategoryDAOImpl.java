package com.revature.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
//import java.util.Locale.Category;

import com.revature.dao.*;
import com.revature.model.Category;
import com.revature.util.DBConnection;

public class CategoryDAOImpl implements CategoryDAO{
	
	@Override
	public List<Category> getAllCategories() {

	    List<Category> list = new ArrayList<>();

	    String sql = "SELECT category_id, category_name, description " +
	    	    "FROM categories ORDER BY category_id";

	    try (Connection con = DBConnection.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql);
	         ResultSet rs = ps.executeQuery()) {

	        while (rs.next()) {

	            Category c = new Category();
	            c.setCategoryId(rs.getInt("category_id"));
	            c.setCategoryName(rs.getString("category_name"));

	            list.add(c);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return list;
	}

	@Override
	public boolean categoryExists(int categoryId) {
		// TODO Auto-generated method stub
		
		String sql = "SELECT 1 FROM categories WHERE category_id=?";

	    try (Connection con = DBConnection.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setInt(1, categoryId);

	        ResultSet rs = ps.executeQuery();
	        return rs.next();

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		return false;
	}


}
