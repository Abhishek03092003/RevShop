package com.revature.service;

import com.revature.dao.SellerDAO;
import com.revature.dao.impl.SellerDAOImpl;
import com.revature.model.Seller;

public class SellerService {

    private SellerDAO sellerDAO = new SellerDAOImpl();

    public void createSeller(int userId, String business, String gst, String address) {
        sellerDAO.createSeller(userId, business, gst, address);
    }

    public Seller getSellerIdByUserId(int userId) {
        return sellerDAO.getSellerIdByUserId(userId);
    }
}
