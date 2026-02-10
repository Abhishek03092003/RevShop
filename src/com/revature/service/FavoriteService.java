package com.revature.service;

import java.util.List;

import com.revature.dao.FavoriteDAO;
import com.revature.dao.ProductDAO;
import com.revature.dao.impl.FavoriteDAOImpl;
import com.revature.dao.impl.ProductDAOImpl;
import com.revature.model.Favorite;
import com.revature.model.Product;
import com.revature.util.ConsoleUtil;

public class FavoriteService {
	
	private FavoriteDAO favoriteDAO = new FavoriteDAOImpl();
    private ProductDAO productDAO = new ProductDAOImpl();


    public void add(int buyerId, int productId){
    	
        Product product = productDAO.getProductById(productId);


//        if(dao.exists(buyerId, productId)){
//            System.out.println("Already in wishlist ❤️");
//            return;
//        }
//
//        if(dao.addFavorite(buyerId, productId)){
//            System.out.println("✅ Added to wishlist");
//        }
        
        if(product == null){
            ConsoleUtil.error("Product not found!");
            return;
        }

        if(product.getStockQuantity() == 0){
            ConsoleUtil.warn("Out of stock — cannot favorite.");
            return;
        }

        if(favoriteDAO.exists(buyerId, productId)){
            ConsoleUtil.warn("Already in wishlist.");
            return;
        }

        if(favoriteDAO.addFavorite(buyerId, productId))
            ConsoleUtil.success("Added to wishlist!");
    }

    public void remove(int buyerId, int productId){

        if(favoriteDAO.removeFavorite(buyerId, productId)){
            System.out.println("Removed from wishlist");
        }
    }

    public List<Product> getAll(int buyerId){
        return favoriteDAO.getFavorites(buyerId);
    }

}
