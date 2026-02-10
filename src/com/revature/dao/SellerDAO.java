package com.revature.dao;

import com.revature.model.Seller;

public interface SellerDAO {
    void createSeller(int userId, String business, String gst, String address);
    Seller getSellerIdByUserId(int userId);

}
