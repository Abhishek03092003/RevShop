package com.revature.model;


public class Product {

    private int productId;
    private int sellerId;
    private int categoryId;
    private String productName;
    private double price;
    private double mrp;
    private double discountPrice;
    private int stockQuantity;
    private int lowStockThreshold;
    private String categoryName;

    
    
    
	public Product() {
		super();
	}



	public Product(int productId, int sellerId, int categoryId, String productName, double price, double mrp,
			double discountPrice, int stockQuantity, int lowStockThreshold) {
		super();
		this.productId = productId;
		this.sellerId = sellerId;
		this.categoryId = categoryId;
		this.productName = productName;
		this.price = price;
		this.mrp = mrp;
		this.discountPrice = discountPrice;
		this.stockQuantity = stockQuantity;
		this.lowStockThreshold = lowStockThreshold;
	}

	
	public String getCategoryName() {
	    return categoryName;
	}
	
	public void setCategoryName(String categoryName) {
	    this.categoryName = categoryName;
	}




	public int getProductId() {
		return productId;
	}



	public void setProductId(int productId) {
		this.productId = productId;
	}



	public int getSellerId() {
		return sellerId;
	}



	public void setSellerId(int sellerId) {
		this.sellerId = sellerId;
	}



	public int getCategoryId() {
		return categoryId;
	}



	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}



	public String getProductName() {
		return productName;
	}



	public void setProductName(String productName) {
		this.productName = productName;
	}



	public double getPrice() {
		return price;
	}



	public void setPrice(double price) {
		this.price = price;
	}



	public double getMrp() {
		return mrp;
	}



	public void setMrp(double mrp) {
		this.mrp = mrp;
	}



	public double getDiscountPrice() {
		return discountPrice;
	}



	public void setDiscountPrice(double discountPrice) {
		this.discountPrice = discountPrice;
	}



	public int getStockQuantity() {
		return stockQuantity;
	}



	public void setStockQuantity(int stockQuantity) {
		this.stockQuantity = stockQuantity;
	}



	public int getLowStockThreshold() {
		return lowStockThreshold;
	}



	public void setLowStockThreshold(int lowStockThreshold) {
		this.lowStockThreshold = lowStockThreshold;
	}



	@Override
	public String toString() {
		return "Product [productId=" + productId + ", sellerId=" + sellerId + ", categoryId=" + categoryId
				+ ", productName=" + productName + ", price=" + price + ", mrp=" + mrp + ", discountPrice="
				+ discountPrice + ", stockQuantity=" + stockQuantity + ", lowStockThreshold=" + lowStockThreshold + "]";
	}
	
	

    // Getters & Setters
    
}

