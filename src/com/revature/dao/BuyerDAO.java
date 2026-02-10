package com.revature.dao;

import com.revature.model.Buyer;

public interface BuyerDAO {
//	void createBuyer(int userId, String shipping, String billing);
    void createBuyer(Buyer buyer);

//	Buyer getBuyerIdByUserId(int userId);
    
    Buyer getBuyerByUserId(int userId);

}

