package com.revature.dao;

import java.util.List;

import com.revature.model.Category;
//import java.util.Locale.Category;

public interface CategoryDAO {
    List<Category> getAllCategories();
    boolean categoryExists(int categoryId);

}
