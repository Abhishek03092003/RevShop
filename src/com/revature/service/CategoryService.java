package com.revature.service;

import java.util.List;

import com.revature.dao.CategoryDAO;
import com.revature.dao.impl.CategoryDAOImpl;
import com.revature.model.Category;

public class CategoryService {

	
    private CategoryDAO categoryDAO = new CategoryDAOImpl();

    public List<Category> getAllCategories() {
        return categoryDAO.getAllCategories();
    }
    
    public boolean categoryExists(int id) {
        return categoryDAO.categoryExists(id);
    }

}


