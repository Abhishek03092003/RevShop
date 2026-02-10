package com.revature.dao;

import java.util.List;

import com.revature.model.Order;
import com.revature.model.Product;

public interface OrderDAO {
    int checkout(int buyerId);
    public List<Order> getOrdersByBuyer(int buyerId);

    public List<Product> getPurchasedProducts(int buyerId);

    List<String> getSellerOrders(int sellerId);


}
