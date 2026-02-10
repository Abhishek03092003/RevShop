package com.revature.dao;

import java.util.List;

import com.revature.model.Product;

public interface ProductDAO {
	void addProduct(Product p);
    List<Product> getAllProducts();
	Product getProductById(int productId);
	List<Product> getLowStockProducts(int sellerId);
	List<Product> searchProducts(String keyword);
	
	boolean deleteProduct(int productId);
	boolean updateProductStock(int productId, int stock);


	boolean adjustStock(int productId, int change);
	boolean setStock(int productId, int newStock);
	boolean updatePrice(int productId, double mrp, double discount);

	
	
	List<Product> getProductsBySeller(int sellerId);

	}

