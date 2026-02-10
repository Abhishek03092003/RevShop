package com.revature.dao;

public interface PaymentDAO {

    boolean makePayment(int orderId, String method);

}
