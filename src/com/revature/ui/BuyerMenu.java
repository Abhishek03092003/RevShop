package com.revature.ui;


import java.util.List;
import java.util.Scanner;

import com.revature.dao.CartDAO;
import com.revature.dao.impl.CartDAOImpl;
import com.revature.model.Buyer;
import com.revature.model.CartItem;
import com.revature.model.Favorite;
import com.revature.model.Order;
import com.revature.model.Product;
import com.revature.model.Review;
import com.revature.model.User;
import com.revature.service.CartService;
import com.revature.service.FavoriteService;
import com.revature.service.OrderService;
import com.revature.service.PaymentService;
import com.revature.service.ProductService;
import com.revature.service.ReviewService;
import com.revature.service.UserService;
import com.revature.util.ConsoleUtil;

public class BuyerMenu {

    private User user;
    private Buyer buyer;
    
    private CartDAO cartDAO = new CartDAOImpl();


    private Scanner sc = new Scanner(System.in);

    private ProductService productService = new ProductService();
    
    private OrderService orderService = new OrderService();
    private ReviewService reviewService = new ReviewService();
    private PaymentService paymentService = new PaymentService();

    private UserService userService = new UserService();

    private FavoriteService favService = new FavoriteService();

    
    private CartService cartService = new CartService();

    
    public BuyerMenu(User user,Buyer buyer) {
    	
    	if(buyer == null || user == null){
            throw new IllegalArgumentException(
                "BuyerMenu cannot start — user or buyer is null"
            );
        }
    	this.user=user;
        this.buyer = buyer;
    }

    public void show() {

        while (true) {
//            System.out.println("\nWelcome Buyer " + user.getName());
//        	System.out.println("\nWelcome Buyer ID: " + buyer.getBuyerId());

            ConsoleUtil.header("BUYER DASHBOARD");

        	System.out.println("1. Browse Products");
            System.out.println("2. Search Products");

            System.out.println("3. View Cart");
            System.out.println("4. Order History");
            System.out.println("5. Checkout");
            System.out.println("6. Review Purchased Products");

            System.out.println("7. Wishlist");
            System.out.println("8. Change Password");
            System.out.println("9. Logout");
//            System.out.print("Enter choice: ");

            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine(); //  VERY IMPORTANT

            switch (choice) {
                case 1:
//                    System.out.println("Browsing products...");
//                    break;
                    browseProducts();

                	
                    break;
                    
                case 2:
//                  System.out.println("Viewing cart...");
                  searchProducts();
              	break;

                case 3:
//                    System.out.println("Viewing cart...");
                    viewCart();
                	break;

                case 4:
//                    System.out.println("Showing order history...");
                    viewOrderHistory();
                	break;
                    
                case 5:
//                    System.out.println("  Checkout...");
//                	boolean success =
//                    orderService.checkout(buyer.getBuyerId());
//
//                		if(success) {
//                			System.out.println(buyer.getBuyerId());
//                			System.out.println("✅ Order placed successfully!");
//                		}
//                		else {
//                			System.out.println("❌ Checkout failed.");
//                		}
                	int orderId = orderService.checkout(buyer.getBuyerId());

                	if(orderId == 0){
                	    System.out.println("Checkout failed.");
                	    return;
                	}

                	System.out.println("Order created! Proceed to payment."+orderId);

                	System.out.println("Select Payment Method:");
                	System.out.println("1. UPI");
                	System.out.println("2. CARD");
                	System.out.println("3. COD");

                	int option = sc.nextInt();

                	String method = "";

                	switch(option){
                	    case 1:
                	        method = "UPI";
                	        break;

                	    case 2:
                	        method = "CARD";
                	        break;

                	    case 3:
                	        method = "COD";
                	        break;

                	    default:
                	        System.out.println("Invalid option");
                	        return;
                	}


                	if(paymentService.pay(orderId, method)){

                	    System.out.println("✅ Payment successful!");
                	    System.out.println("✅ Order confirmed!");

                	}else{

                	    System.out.println("❌ Payment failed!");
                	}
                	
                	break;
                	
                case 6:
//                  System.out.println("Showing order history...");
                	reviewPurchasedProducts();
              	break;

                case 7:
//                    System.out.println("Logging out...");
                	viewFavorites();
                	break;
//                	return; // exit menu
                	
                case 8:
//                    System.out.println("Logging out...");
                	changePassword();
                	break;
//                	return;
                case 9:
                    System.out.println("Logging out...");
                    return; // exit menu

                default:
                    System.out.println("Invalid choice");
            }
        }
    }
    
//    private void browseProducts() {
//        List<Product> products = productService.getAllProducts();
//
//        if (products.isEmpty()) {
//            System.out.println("No products available.");
//        } else {
//            for (Product p : products) {
//                System.out.println(
//                    p.getProductId() + " | " +
//                    p.getProductName() + " | Rupee" +
//                    p.getPrice()
//                );
//            }
//        }
//    }
    
    private void browseProducts() {

        List<Product> products = productService.getAllProducts();

        
        
        if (products.isEmpty()) {
            System.out.println("No products available.");
            return;
        }

//        System.out.println("\n=========== AVAILABLE PRODUCTS ===========");

//        System.out.printf("%-6s %-15s %-15s %-10s %-10s %-8s\n",
//                "ID", "Product", "Category", "Price","Discounted Price", "Stock");
//        for (Product p : products) {
//        	
//        	System.out.printf("%-6d %-15s %-15s %-10.2f %-10d %-8s\n",
//                    p.getProductId(),
//                    p.getProductName(),
//                    p.getCategoryName(),
//                    p.getMrp(),
//                    p.getPrice(),
//                    p.getStockQuantity());
//
//        }
        System.out.println("\n=========== AVAILABLE PRODUCTS ===========");

        System.out.printf("%-6s %-15s %-15s %-10s %-18s %-10s %-8s\n",
                "ID", "Product", "Category", "Price", "Discounted Price","final", "Stock");

        for (Product p : products) {

        	double discount =
        	        p.getMrp() - p.getPrice();

        	System.out.printf("%-6d %-15s %-15s ₹%-8.2f  (-₹%.2f)  ₹%-9.2f %-8d\n",
        	        p.getProductId(),
        	        p.getProductName(),
        	        p.getCategoryName(),
        	        p.getMrp(),
        	        discount,
        	        p.getPrice(),
        	        p.getStockQuantity());

        }
        
        ConsoleUtil.separator();


        
//        System.out.println("=========================================");
        
//        System.out.print("Enter Product ID to add to cart (0 to cancel): ");
        System.out.print("Enter Product ID (0 to go back): ");

        int productId = sc.nextInt();

        if(productId == 0) return;

        Product product = productService.getProductById(productId);

        if(product == null){
            System.out.println("❌ Product not found!");
            return;
        }else {
            System.out.println("FOUND: " + product.getProductName());

        }
        
        List<Review> reviews =
                reviewService.getReviews(productId);

        ConsoleUtil.header("CUSTOMER REVIEWS");

        if(reviews.isEmpty()){

            ConsoleUtil.warn("No reviews yet.");

        }else{

            System.out.printf("%-10s %-50s\n",
                    "Rating", "Comment");

            ConsoleUtil.separator();

            for(Review r : reviews){

                System.out.printf("%-10s %-50s\n",
                        "* " + r.getRating(),
                        r.getComment());
            }

            ConsoleUtil.separator();
        }


        // ⭐ ACTION MENU
        System.out.println("\n1. Add to Cart");
        System.out.println("2. Add to Wishlist");
        System.out.println("3. Back");

        int choice = sc.nextInt();

        switch(choice){

            case 1:

                if(product.getStockQuantity() <= 0){
                    System.out.println("❌ Out of stock!");
                    return;
                }

                System.out.print("Enter quantity: ");
                int qty = sc.nextInt();

                if(qty > product.getStockQuantity()){

                    System.out.println(
                        "❌ Only " +
                        product.getStockQuantity() +
                        " left!"
                    );

//                    return;
                    break;
                }

                cartService.addToCart(
                        buyer.getBuyerId(),
                        productId,
                        qty);

                System.out.println("✅ Added to cart!");
                break;

            case 2:

                favService.add(
                        buyer.getBuyerId(),
                        productId);

                break;

            default:
                return;
        }
        
        
        product.getStockQuantity();


//        System.out.print("Enter quantity: ");
//        int qty = sc.nextInt();
//
//        if(qty > product.getStockQuantity()){
//            System.out.println("❌ Not enough stock!");
//            return;
//        }
//
////        cartService.addToCart(buyer.getBuyerId(), productId, qty);
//
//        boolean added = cartService.addToCart(
//                buyer.getBuyerId(),
//                productId,
//                qty
//        );
        
        

//        if(added)
//            System.out.println("✅ Added to cart!");
//        else
//            System.out.println("❌ Failed to add.");
//        
//        System.out.println("✅ Added to cart successfully!");

//        List<Review> reviews = reviewService.getReviews(productId);
//        if(!reviews.isEmpty()){
//
//            System.out.println("\n⭐ CUSTOMER REVIEWS:");
//
//            for(Review r : reviews){
//                System.out.println(
//                    r.getRating() + "⭐ - " + r.getComment());
//            }
//        }else{
//            System.out.println("No reviews yet.");
//        }
    
    }
    
    private void searchProducts(){

//        sc.nextLine();
        
        ConsoleUtil.header("SEARCH PRODUCTS");

        System.out.print("Enter Keyword: ");
        String key = sc.nextLine();

        List<Product> results = productService.search(key);

        if(results.isEmpty()){
//            System.out.println("No products found.");
            ConsoleUtil.warn("No products found.");

        	return;
        }
        
        ConsoleUtil.header("Search results");
        
        System.out.printf("%-5s %-22s %-10s %-10s\n",
                "ID","Product","Final","Stock");

        ConsoleUtil.separator();

        for(Product p: results){
        	
        	System.out.printf("%-5d %-22s ₹%-9.2f %-10d\n",
                    p.getProductId(),
                    p.getProductName(),
                    p.getPrice(),
                    p.getStockQuantity());
        }
        
        ConsoleUtil.separator();
        
//        System.out.println("\nPress ENTER to return...");
//        sc.nextLine();
        pause();
    }

    
    
//    private void viewCart(){
//
//        List<CartItem> items =
//                cartService.viewCart(buyer.getBuyerId());
//
//        if(items.isEmpty()){
//            System.out.println("🛒 Cart is empty.");
//            return;
//        }
//
//        double grandTotal = 0;
//
//        System.out.println("\n=========== YOUR CART ===========");
//
//        System.out.printf("%-15s %-10s %-10s %-10s\n",
//                "Product","Price","Qty","Subtotal");
//
//        for(CartItem item : items){
//
//            System.out.printf("%-15s %-10.2f %-10d %-10.2f\n",
//                    item.getProductName(),
//                    item.getPrice(),
//                    item.getQuantity(),
//                    item.getSubtotal());
//
//            grandTotal += item.getSubtotal();
//        }
//
//        System.out.println("---------------------------------");
//        System.out.println("TOTAL: Rs. %.2f" + grandTotal);
//    }
    
    public List<CartItem> getCartItems(int buyerId){
        return cartDAO.getCartItems(buyerId);
    }
    
//    List<CartItem> items = cartService.getCartItems(buyer.getBuyerId());


    
    
    private void viewCart(){
    	
    	

        ConsoleUtil.header("YOUR CART");


        List<CartItem> items =
            cartService.getCartItems(
                    buyer.getBuyerId());

        if(items.isEmpty()){
//            System.out.println("Cart is empty.");
            ConsoleUtil.warn("Cart is empty.");

            return;
        }
        
        double total = 0;


//        System.out.println("\n=========== YOUR CART ===========");

        System.out.printf("%-6s %-15s %-10s %-10s\n",
                "ID","Product","Qty","Subtotal");

        for(CartItem item : items){
        	
        	total += item.getSubtotal();

            System.out.printf("%-6d %-15s %-10d %-10.2f\n",
                    item.getProductId(),
                    item.getProductName(),
                    item.getQuantity(),
                    item.getSubtotal());
        }

        System.out.println("------------------------------------------------");
        System.out.println("TOTAL: ₹" + total);

        System.out.println("\n1. Increase Quantity");
        System.out.println("2. Decrease Quantity");
        System.out.println("3. Remove Item");
        System.out.println("4. Back");
        
        System.out.print("Choice: ");

        int choice = sc.nextInt();

        if(choice == 4)
            return;

        System.out.print("Enter Product ID: ");
        int productId = sc.nextInt();

        switch(choice){

            case 1:
                cartService.increaseQty(
                        buyer.getBuyerId(),
                        productId);
                ConsoleUtil.success("Quantity increased.");
                break;

            case 2:
                cartService.decreaseQty(
                        buyer.getBuyerId(),
                        productId);
                ConsoleUtil.success("Quantity decreased.");
                break;

            case 3:
                cartService.removeItem(
                        buyer.getBuyerId(),
                        productId);
                ConsoleUtil.success("Item removed.");

                break;
                
            default:
                ConsoleUtil.warn("Invalid option.");
        }
    }

    
    private void viewOrderHistory(){

        List<Order> orders =
                orderService.getOrdersByBuyer(buyer.getBuyerId());

        if(orders.isEmpty()){
            System.out.println("📦 No orders found.");
            return;
        }

        System.out.println("\n=========== ORDER HISTORY ===========");

        System.out.printf("%-10s %-12s %-12s %-20s\n",
                "OrderID", "Total", "Status", "Date");

        for(Order o : orders){

            System.out.printf("%-10d %-12.2f %-12s %-20s\n",
                    o.getOrderId(),
                    o.getTotalAmount(),
                    o.getStatus(),
                    o.getOrderDate());
        }
    }
    
    private void leaveReview(){

        System.out.print("Enter Product ID: ");
        int productId = sc.nextInt();
        sc.nextLine();

        System.out.print("Rating (1-5): ");
        int rating = sc.nextInt();
        sc.nextLine();

        System.out.print("Comment: ");
        String comment = sc.nextLine();

        boolean success =
            reviewService.addReview(
                buyer.getBuyerId(),
                productId,
                rating,
                comment
            );

        if(success)
            System.out.println("✅ Review added!");
    }
    
    private void reviewPurchasedProducts(){
    	
    	
    	while(true) {

        List<Product> purchased =
                orderService.getPurchasedProducts(
                        buyer.getBuyerId()
                );

        if(purchased.isEmpty()){
//            System.out.println("You have not purchased anything yet.");
        	ConsoleUtil.warn("You haven't purchased anything yet.");
        	return;
        }

//        System.out.println("\n====== PURCHASED PRODUCTS ======");

        System.out.printf("%-6s %-25s\n",
                "ID","Product");

        ConsoleUtil.separator();
        
        for(Product purchasedProduct : purchased){

            System.out.println(
                purchasedProduct.getProductId() +
                " - " +
                purchasedProduct.getProductName()
            );
        }
        
        ConsoleUtil.separator();

        System.out.println("1. Write Review \n 0. Back");

        System.out.print("Choice: ");

        int choice = sc.nextInt();
        sc.nextLine();

        if(choice == 0)
            return;

        if(choice != 1){
            ConsoleUtil.error("Invalid choice");
            continue;
        }
        System.out.print("Select Product ID to review: ");
        int productId = sc.nextInt();
        sc.nextLine();

        System.out.print("Rating (1-5): ");
        int rating = sc.nextInt();
        sc.nextLine();

        if(rating < 1 || rating > 5){
            ConsoleUtil.error("Rating must be between 1 and 5.");
            continue;
        }

        System.out.print("Comment: ");
        String comment = sc.nextLine();

        boolean success =
            reviewService.addReview(
                buyer.getBuyerId(),
                productId,
                rating,
                comment
            );

        if(success)
            ConsoleUtil.success("Review added!");

        pause();
        
    	}

    }
    
//    private void viewFavorites(){
//
//        List<Product> list =
//            favService.getAll(buyer.getBuyerId());
//
//        if(list.isEmpty()){
//            ConsoleUtil.warn("Wishlist is empty.");
//            return;
//        }
//        
//        ConsoleUtil.header("YOUR WISHLIST");
//
//
////        for(Favorite f : list){
////
////            System.out.println(
////                f.getProductId() + " | " +
////                f.getProductName() +
////                " | Stock: " + f.getStock()
////            );
////        }
////
////        System.out.println("1.Move to Cart  2.Remove  3.Back");
////
////        int choice = sc.nextInt();
////
////        if(choice==1){
////
////            System.out.print("Enter Product ID: ");
////            int pid = sc.nextInt();
////
////            cartService.addToCart(
////                buyer.getBuyerId(), pid, 1);
////
////        }
////        else if(choice==2){
////
////            System.out.print("Enter Product ID: ");
////            int pid = sc.nextInt();
////
////            favService.remove(
////                buyer.getBuyerId(), pid);
////        }
//        
//        for(Product p : list){
//            System.out.println(
//                    p.getProductId() + " | " +
//                    p.getProductName() + " | Ru:" +
//                    p.getPrice());
//        }
//
//        System.out.println("1. Move to Cart");
//        System.out.println("2. Remove");
//        System.out.println("3. Back");
//
//        int choice = sc.nextInt();
//
//        if(choice == 1){
//
//            System.out.print("Enter product ID: ");
//            int pid = sc.nextInt();
//
//            cartService.addToCart(buyer.getBuyerId(), pid, 1);
//
//            ConsoleUtil.success("Moved to cart!");
//        }
//
//        else if(choice == 2){
//
//            System.out.print("Enter product ID: ");
//            int pid = sc.nextInt();
//
//            favService.remove(
//                    buyer.getBuyerId(), pid);
//        }
//    }
    
    private void viewFavorites(){

        while(true){

            List<Product> list =
                favService.getAll(buyer.getBuyerId());

            ConsoleUtil.header("YOUR WISHLIST");

            if(list.isEmpty()){
                ConsoleUtil.warn("Wishlist is empty.");
                System.out.println("\n1. Back");
                int back = sc.nextInt();
                sc.nextLine(); // buffer clear
                return;
            }

//            ConsoleUtil.header("YOUR WISHLIST");
            
            System.out.printf("%-6s %-25s %-12s\n",
                    "ID","Product","MRP");
            
            ConsoleUtil.separator();



            for(Product p : list){
            	System.out.printf("%-6d %-25s ₹%-12.2f\n",
                        p.getProductId(),
                        p.getProductName(),
                        p.getMrp());
            }
            
            ConsoleUtil.separator();


            System.out.println("\n1. Move to Cart");
            System.out.println("2. Remove");
            System.out.println("3. Back");
            
            System.out.println("Enter Choice:");

            int choice = sc.nextInt();

            switch(choice){

                case 1:
                    System.out.print("Enter product ID: ");
                    int pid = sc.nextInt();

                    boolean added =
                            cartService.addToCart(
                                    buyer.getBuyerId(), pid, 1);

                    if(added)
                        ConsoleUtil.success("Moved to cart!");
                    else
                        ConsoleUtil.error("Failed to move.");

                    pause();
                    break;

                case 2:
                    System.out.print("Enter product ID to Remove: ");
                    pid = sc.nextInt();
                    
                    sc.nextLine();

//                    favService.remove(
//                            buyer.getBuyerId(), pid);
//
//                    ConsoleUtil.success("Removed from wishlist!");
//                    break;
                    
                    System.out.print("Are you sure? (Y/N): ");
                    String confirm = sc.nextLine();

                    if(confirm.equalsIgnoreCase("Y")){

                        favService.remove(
                                buyer.getBuyerId(), pid);

                        ConsoleUtil.success("Removed from wishlist!");
                    }

                    pause();
                    break;

                case 3:
                    return;

                default:
                    ConsoleUtil.error("Invalid choice");
            }
        }
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





}

