package com.revature.ui;


import java.util.Collection;
import java.util.List;
//import java.util.Locale.Category;
import java.util.Scanner;

import com.revature.model.Category;
import com.revature.model.Product;
import com.revature.model.Seller;
import com.revature.model.User;
import com.revature.service.CategoryService;
import com.revature.service.OrderService;
import com.revature.service.ProductService;
import com.revature.service.ReviewService;
import com.revature.service.SellerService;
import com.revature.service.UserService;
import com.revature.util.ConsoleUtil;
import com.revature.dao.*;
import com.revature.dao.impl.*;

public class SellerMenu {

    private User user;
    
    private Seller seller;
//    int categoryId;

	private CategoryService categoryService = new CategoryService();

    private ProductService productService = new ProductService();

    private Scanner sc = new Scanner(System.in);
    private OrderService orderService = new OrderService();
    

    private UserService userService = new UserService();

    
    private ReviewService reviewService =new ReviewService();
    private SellerService sellerService =new SellerService();


    public SellerMenu(User user,Seller seller) {
        this.user = user;

    	this.seller=seller;
    }

//    public void show() {
//        System.out.println("Welcome Seller " + user.getName());
//        System.out.println("1. Add Product");
//        System.out.println("2. View Orders");
//        System.out.println("3. Stock Alerts");
//    }
    
    
    

    private void addProduct() {
    	
    	ConsoleUtil.header("ADD NEW PRODUCT");

        System.out.print("Product name: ");
        String name = sc.nextLine();
        
        
        showCategories();
        
        int categoryId;

        while (true) {

            System.out.print("Enter Category ID: ");
            categoryId = sc.nextInt();

            if (categoryService.categoryExists(categoryId)) {
                break; // ✅ valid category
            }

            ConsoleUtil.error("Invalid category!");
        }

        System.out.print("Enter MRP: ");
        double mrp = sc.nextDouble();
        
        System.out.print("Enter Discount Price: ");
        double discountPrice = sc.nextDouble();

        if (discountPrice > mrp) {
        	ConsoleUtil.error("Discount must be less than MRP.");
            sc.nextLine();
            return;
        }

        System.out.print("Stock quantity: ");
        int stock = sc.nextInt();
//        sc.nextLine(); // buffer clear
        
        System.out.print("Low stock threshold: ");
        int threshold = sc.nextInt();
        sc.nextLine();

//        Seller sellerId = sellerService.getSellerIdByUserId(user.getUserId());
//
//        System.out.println("DEBUG sellerId = " + sellerId);
        Product p = new Product();
        p.setProductName(name);
        p.setCategoryId(categoryId);
        p.setMrp(mrp);
        p.setDiscountPrice(discountPrice);
        p.setStockQuantity(stock);
        p.setLowStockThreshold(threshold);
        p.setSellerId(seller.getSellerId());

        productService.addProduct(p);
        
        

        ConsoleUtil.success("Product added successfully!");

        pause();
    }

    
    
    public void show() {

        while (true) {
//            System.out.println("\nWelcome Seller " + user.getName());
            ConsoleUtil.header("SELLER DASHBOARD");

        	System.out.println("1. Add Product");
            System.out.println("2. Manage Product");
            System.out.println("3. View Orders");
            System.out.println("4. View Reviews");
            System.out.println("5. Stock Alerts");
            System.out.println("6. Change Password");
            System.out.println("7. Logout");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();
            sc.nextLine(); //  buffer clear

            switch (choice) {
                case 1:
                     addProduct();
                    break;
                    
                case 2:
                  manageProducts();
                  break;

                case 3:
                    viewSellerOrders();

                	break;
                	
                case 4:
                	viewSellerReviews();
                	break;

                case 5:
                	showStockAlerts();
                	break;
                	
                case 6:
                	changePassword();
                	return; 

                case 7:
                    ConsoleUtil.success("Logged out!");
                    return; //  ONLY place we return

                default:
                	ConsoleUtil.error("Invalid choice");            }
        }
    }
    
    private void showCategories() {
    	
    	ConsoleUtil.header("AVAILABLE CATEGORIES");
    	
        List<Category> categories = categoryService.getAllCategories();


     
        System.out.printf("%-5s %-20s\n",
                "ID","Category");

        ConsoleUtil.separator();
        for (Category c : categories) {
        	System.out.printf("%-5d %-20s\n",
                    c.getCategoryId(),
                    c.getCategoryName());
        }
        
        ConsoleUtil.separator();
    }
    
    private void viewSellerOrders(){
    	
    	ConsoleUtil.header("YOUR ORDERS");

        List<String> orders =
            orderService.getSellerOrders(
                seller.getSellerId()
            );

        if(orders.isEmpty()){
        	ConsoleUtil.warn("No orders yet.");
        	return;
        }

        ConsoleUtil.separator();

        for(String o : orders){
            System.out.println(o);
        }
        
        ConsoleUtil.separator();

        pause();
    }
    
    private void viewSellerReviews(){
    	
    	ConsoleUtil.header("PRODUCT REVIEWS");

        List<String> reviews =
            reviewService.getSellerReviews(
                seller.getSellerId()
            );

        if(reviews.isEmpty()){
        	ConsoleUtil.warn("No reviews yet.");
        	return;
        }

        ConsoleUtil.separator();
        
        
        for(String r : reviews){
            System.out.println(r);
        }
        
        ConsoleUtil.separator();

        pause();
    }
    
    
    private void showStockAlerts(){
    	
    	ConsoleUtil.header("LOW STOCK ALERT");

        List<Product> lowStock =
                productService.getLowStockProducts(
                        seller.getSellerId());

        if(lowStock.isEmpty()){
        	ConsoleUtil.success("All products sufficiently stocked.");
        	return;
        }


        for(Product p : lowStock){

            System.out.println(
                p.getProductName() +
                " — Only " +
                p.getStockQuantity() +
                " left!"
            );
        }
        
        pause();
    }
    
    private void pause(){
        System.out.println("\nPress ENTER to continue...");
        sc.nextLine();
    }

    
    private void changePassword(){

        System.out.print("Enter new password: ");
        String pass = sc.nextLine();

        if(userService.changePassword(user.getUserId(), pass))
            ConsoleUtil.success("Password updated!");
    }
    
    

    
    private void manageProducts(){

        while(true){
        	
            ConsoleUtil.header("YOUR PRODUCTS");


            List<Product> products =
                    productService.getSellerProducts(
                            seller.getSellerId());

            if(products.isEmpty()){
            	ConsoleUtil.warn("No products found.");
            	return;
            }

//            System.out.println("\n====== YOUR PRODUCTS ======");

            System.out.printf("%-5s %-22s %-8s %-10s %-10s %-10s\n",
                    "ID","Product","Stock","MRP","Discount","Final");

            ConsoleUtil.separator();
            
            for(Product p : products){

            	double finalPrice =
                        p.getMrp() - p.getDiscountPrice();

                System.out.printf("%-5d %-22s %-8d ₹%-9.2f ₹%-9.2f ₹%-9.2f\n",
                        p.getProductId(),
                        p.getProductName(),
                        p.getStockQuantity(),
                        p.getMrp(),
                        p.getDiscountPrice(),
                        finalPrice);
            }
            
            ConsoleUtil.separator();

            System.out.println("\n1. Add Stock");
            System.out.println("2. Reduce Stock");
            System.out.println("3. Update Price");
            System.out.println("4. Delete Product");
            System.out.println("5. Back");
            System.out.println("Enter Choice: ");


            int choice = sc.nextInt();

            if(choice == 5)
                return; // ONLY place return is allowed


            System.out.print("Enter Product ID: ");
            int pid = sc.nextInt();

            switch(choice){

                case 1:

                    System.out.print("Enter quantity to add: ");
                    
                    int stock = sc.nextInt();

                    if(stock <= 0){
                        ConsoleUtil.warn("Enter a positive number.");
                        break;
                    }

                   
                    if(productService.adjustStock(pid, stock))
                        ConsoleUtil.success("Stock updated!");
                    break;
                    
//                    productService.adjustStock(pid, stock);
//                    ConsoleUtil.success("Stock increased!");

                case 2:

                    System.out.print("Enter quantity to REDUCE: ");
                    int reduce = sc.nextInt();
                    
                    if(reduce <= 0){
                        ConsoleUtil.warn("Enter a positive number.");
                        break;
                    }

                    if(productService.adjustStock(pid, -reduce))
                    	ConsoleUtil.success("Stock reduced!");
                    break;
                    
                    
                case 3:

                    System.out.print("Enter new MRP: ");
                    double mrp = sc.nextDouble();

                    System.out.print("Enter discount: ");
                    double discount = sc.nextDouble();

                    if(mrp <= 0){
                        ConsoleUtil.error("MRP must be greater than zero.");
                        break;
                    }

                    if(discount < 0){
                        ConsoleUtil.error("Discount cannot be negative.");
                        break;
                    }

                    if(discount >= mrp){
                        ConsoleUtil.error("Discount must be LESS than MRP.");
                        break;
                    }

                    
                    if(productService.updatePrice(pid, mrp, discount))
                    	ConsoleUtil.success("MRP and Discount Updated!");;
                    break;
                    
                case 4:

                	System.out.print("Confirm delete? (Y/N): ");
                    sc.nextLine();
                    String confirm = sc.nextLine();

                    if(confirm.equalsIgnoreCase("Y")){

                        if(productService.deleteProduct(pid))
                            ConsoleUtil.success("Product deleted!");
                        else
                            ConsoleUtil.error("Cannot delete — product exists in orders.");
                    }
                	
                	break;
                
                default:
                    ConsoleUtil.warn("Invalid option.");


            }
            
            pause();
        }
    }






    
}

