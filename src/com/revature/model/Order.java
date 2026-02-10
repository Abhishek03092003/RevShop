package com.revature.model;

import java.sql.Timestamp;

public class Order {
	
	private int orderId;
    private int buyerId;
    private double totalAmount;
//    private double totalAmount;
    private String status;
    
    
    


	private Timestamp orderDate;
    
    
	public Order() {
		super();
	}


	public Order(int orderId, int buyerId, double totalAmount) {
		super();
		this.orderId = orderId;
		this.buyerId = buyerId;
		this.totalAmount = totalAmount;
	}


	public int getOrderId() {
		return orderId;
	}


	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}


	public int getBuyerId() {
		return buyerId;
	}


	public void setBuyerId(int buyerId) {
		this.buyerId = buyerId;
	}


	public double getTotalAmount() {
		return totalAmount;
	}


	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}
	
	
	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public Timestamp getOrderDate() {
		return orderDate;
	}


	public void setOrderDate(Timestamp orderDate) {
		this.orderDate = orderDate;
	}
	
    
    

}
