package com.revature.service;

import com.revature.dao.BuyerDAO;
import com.revature.dao.impl.BuyerDAOImpl;
import com.revature.model.Buyer;

public class BuyerService {
	private BuyerDAO buyerDAO = new BuyerDAOImpl();

    public void createBuyer(Buyer buyer){
        buyerDAO.createBuyer(buyer);
    }

    public Buyer getBuyerByUserId(int userId){
        return buyerDAO.getBuyerByUserId(userId);
    }


}
