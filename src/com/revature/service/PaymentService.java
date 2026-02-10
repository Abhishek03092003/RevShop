package com.revature.service;

import com.revature.dao.PaymentDAO;
import com.revature.dao.impl.PaymentDAOImpl;

public class PaymentService {
	
	private PaymentDAO paymentDAO = new PaymentDAOImpl();

    public boolean pay(int orderId, String method){

        if(!method.equals("UPI") &&
           !method.equals("CARD") &&
           !method.equals("COD")){

            System.out.println("Invalid payment method!");
            return false;
        }

        return paymentDAO.makePayment(orderId, method);
    }

}
