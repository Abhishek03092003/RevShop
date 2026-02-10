package com.revature.service;

import java.util.List;

import com.revature.dao.CartDAO;
import com.revature.dao.ProductDAO;
import com.revature.dao.impl.CartDAOImpl;
import com.revature.dao.impl.ProductDAOImpl;
import com.revature.model.CartItem;
import com.revature.model.Product;

public class CartService {
	
	private CartDAO cartDAO = new CartDAOImpl();
	private ProductDAO productDAO =
	        new ProductDAOImpl();


//    public void addToCart(int buyerId, int productId, int qty) {
//
//        int cartId = cartDAO.getCartIdByBuyerId(buyerId);
//
//        if(cartDAO.productExistsInCart(cartId, productId)) {
//
//            cartDAO.updateCartQuantity(cartId, productId, qty);
//
//        } else {
//
//            cartDAO.addToCart(cartId, productId, qty);
//        }
//    }
	
	public boolean addToCart(int buyerId, int productId, int qty) {

	    int cartId = cartDAO.getCartIdByBuyerId(buyerId);

	    // Safety check (VERY IMPORTANT)
	    if(cartId == 0){
	        System.out.println(" Cart not found!");
	        return false;
	    }

	    boolean result;

	    if(cartDAO.productExistsInCart(cartId, productId)) {

	        result = cartDAO.updateCartQuantity(cartId, productId, qty);

	    } else {

	        result = cartDAO.addToCart(cartId, productId, qty);
	    }

	    return result;
	}

    
    public void createCart(int buyerId){
        cartDAO.createCart(buyerId);
    }
    
    public List<CartItem> getCartItems(int buyerId){
        return cartDAO.getCartItems(buyerId);
    }

    
    public List<CartItem> viewCart(int buyerId){
        return cartDAO.getCartItems(buyerId);
    }
    
    public void increaseQty(int buyerId, int productId){

        int cartId = cartDAO.getCartIdByBuyerId(buyerId);

        Product product =
                productDAO.getProductById(productId);

        if(product == null){
            System.out.println("Product not found!");
            return;
        }

        boolean success =
            cartDAO.increaseQty(
                    cartId,
                    productId,
                    product.getStockQuantity());

        if(success)
            System.out.println("Quantity increased!");
    }
    
    
    public void decreaseQty(int buyerId, int productId){

        int cartId =
            cartDAO.getCartIdByBuyerId(buyerId);

        if(cartDAO.decreaseQty(cartId, productId))
            System.out.println("Quantity decreased!");
    }
    
    public void removeItem(int buyerId, int productId){

        int cartId =
            cartDAO.getCartIdByBuyerId(buyerId);

        if(cartDAO.removeItem(cartId, productId))
            System.out.println("Item removed from cart!");
    }



    



}
