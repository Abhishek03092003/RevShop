package com.revature.model;

public class Seller {

	 
	 
	private int sellerId;
    private int userId;
    private String businessName;
    private String gstNumber;
    private String address;
    
    
    
	public Seller() {
		super();
	}



	public Seller(int sellerId, int userId, String businessName, String gstNumber, String address) {
		super();
		this.sellerId = sellerId;
		this.userId = userId;
		this.businessName = businessName;
		this.gstNumber = gstNumber;
		this.address = address;
	}



	public int getSellerId() {
		return sellerId;
	}



	public void setSellerId(int sellerId) {
		this.sellerId = sellerId;
	}



	public int getUserId() {
		return userId;
	}



	public void setUserId(int userId) {
		this.userId = userId;
	}



	public String getBusinessName() {
		return businessName;
	}



	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}



	public String getGstNumber() {
		return gstNumber;
	}



	public void setGstNumber(String gstNumber) {
		this.gstNumber = gstNumber;
	}



	public String getAddress() {
		return address;
	}



	public void setAddress(String address) {
		this.address = address;
	}
	
	
	
    
    

}
