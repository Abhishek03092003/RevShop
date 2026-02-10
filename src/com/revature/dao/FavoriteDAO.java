package com.revature.dao;

import java.util.List;

import com.revature.model.Favorite;
import com.revature.model.Product;

public interface FavoriteDAO {

	 boolean addFavorite(int buyerId, int productId);

	    boolean removeFavorite(int buyerId, int productId);

	    boolean exists(int buyerId, int productId);

	    List<Product> getFavorites(int buyerId);
}
