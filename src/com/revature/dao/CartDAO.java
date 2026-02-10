package com.revature.dao;

import java.util.List;

import com.revature.model.CartItem;

public interface CartDAO {
	int getCartIdByBuyerId(int buyerId);

    boolean productExistsInCart(int cartId, int productId);

    boolean addToCart(int cartId, int productId, int qty);

    boolean updateCartQuantity(int cartId, int productId, int qty);
    

    void createCart(int buyerId);

	List<CartItem> getCartItems(int buyerId);
	
	boolean removeItem(int cartId, int productId);

	boolean decreaseQty(int cartId, int productId);

	boolean increaseQty(int cartId, int productId, int stock);


}