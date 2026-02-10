package com.revature.model;

public class Category {

    private int categoryId;
    private String categoryName;
    private String description;
    
    
    public Category() {
    	
    }
    
	public Category(int categoryId, String categoryName, String description) {
		super();
		this.categoryId = categoryId;
		this.categoryName = categoryName;
		this.description = description;
	}
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

//    // ✅ Getter methods
//    public int getCategoryId() {
//        return categoryId;
//    }
//
//    public String getCategoryName() {   // <-- you asked for this
//        return categoryName;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    // ✅ Setter methods
//    public void setCategoryId(int categoryId) {
//        this.categoryId = categoryId;
//    }
//
//    public void setCategoryName(String categoryName) { // <-- setter
//        this.categoryName = categoryName;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
    
}
