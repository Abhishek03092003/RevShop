package com.revature.ui;

import java.util.Scanner;

import com.revature.model.Buyer;
import com.revature.model.Seller;
import com.revature.model.User;
import com.revature.service.BuyerService;
import com.revature.service.CartService;
import com.revature.service.SellerService;
import com.revature.service.UserService;
import com.revature.util.ConsoleUtil;
import com.revature.util.ValidationUtil;

public class AuthMenu {

    private Scanner sc = new Scanner(System.in);
    private UserService userService = new UserService();

    private SellerService sellerService = new SellerService();
    private BuyerService buyerService = new BuyerService();

    private CartService cartService = new CartService();


    public void start() {
    	
    	while(true) {
    		
    		ConsoleUtil.header("      RevShop       ");
    		
	        System.out.println("1. Register");
	        System.out.println("2. Login");
	        System.out.println("3. Forgot Password");
	        System.out.println("4. Exit");
	        System.out.print("Enter choice here: ");
	
	
	        int choice = sc.nextInt();
	        sc.nextLine();
	
	        switch (choice) {
            case 1:
                register();
                break;

            case 2:
                login();
                break;
                
            case 3:
            	forgotPassword();
                break;

            case 4:
                System.out.println("Exiting application...");
                System.exit(0);

            default:
                System.out.println("Invalid choice");
        }
	    }
    }


    private void register() {
    	
    	ConsoleUtil.header("USER REGISTRATION");


        System.out.print("Name: ");
        String name = sc.nextLine();

        String email;

        while(true){

            System.out.print("Email: ");
            email = sc.nextLine().trim().toLowerCase();

            // ✅ format check first
            if(!ValidationUtil.isValidEmail(email)){
//                System.out.println("❌ Invalid email format.");
            	ConsoleUtil.error("Invalid Email Format");

                continue;
            }

            // ✅ duplicate check second
            if(userService.emailExists(email)){
            	ConsoleUtil.error("Email already registered.");
                pause();
                return; // STOP REGISTRATION
            }

            break; // safe email
        }

        //--------------------------------------

        String password;

        while(true){

            System.out.print("Password: ");
            password = sc.nextLine();

            if(ValidationUtil.isValidPassword(password))
                break;

            ConsoleUtil.error(
                    "Password must contain:\n" +
                    "• 8+ characters\n" +
                    "• Uppercase\n" +
                    "• Lowercase\n" +
                    "• Number\n" +
                    "• Special character"
            );
        }

        //--------------------------------------

        String role;

        while(true){

            System.out.print("Role (BUYER/SELLER): ");
            role = sc.nextLine().toUpperCase();

            if(role.equals("BUYER") || role.equals("SELLER"))
                break;

            ConsoleUtil.error("Invalid role.");
         }

        //--------------------------------------
        // CREATE USER
        //--------------------------------------

        User user = new User(name, email, password, role);

        boolean created = userService.register(user);

        if(!created){
        	 ConsoleUtil.error("Registration failed.");
        	 return;
        }

        int userId = userService.getUserIdByEmail(email);

        //--------------------------------------
        // BUYER FLOW
        //--------------------------------------

        if(role.equals("BUYER")){
        	
        	ConsoleUtil.header("BUYER DETAILS");

            System.out.print("Shipping Address: ");
            String shipping = sc.nextLine();

            System.out.print("Billing Address: ");
            String billing = sc.nextLine();

            Buyer buyer = new Buyer();
            buyer.setUserId(userId);
            buyer.setShippingAddress(shipping);
            buyer.setBillingAddress(billing);

            buyerService.createBuyer(buyer);

            Buyer savedBuyer =
                    buyerService.getBuyerByUserId(userId);

            cartService.createCart(savedBuyer.getBuyerId());
        }

        //--------------------------------------
        // SELLER FLOW
        //--------------------------------------

        else{
        	
        	 ConsoleUtil.header("SELLER DETAILS");

            System.out.print("Business name: ");
            String business = sc.nextLine();

            System.out.print("GST number: ");
            String gst = sc.nextLine();

            System.out.print("Business Address: ");
            String address = sc.nextLine();

            sellerService.createSeller(userId, business, gst, address);
        }
        
        
        ConsoleUtil.line();

        ConsoleUtil.success("Registration successful!");

        pause();
    }


    private void login() {
    	
    	ConsoleUtil.header("LOGIN");
    	
    	
        System.out.print("Email: ");
        String email = sc.nextLine().trim().toLowerCase();;
        System.out.print("Password: ");
        String pass = sc.nextLine();

        User user = userService.login(email, pass);

        if (user == null) {
        	ConsoleUtil.error("Invalid credentials.");
            pause();
            return;
        }
        
        if ("BUYER".equals(user.getRole())) {

            Buyer buyer = buyerService.getBuyerByUserId(user.getUserId());
//            Seller seller = sellerService.getSellerIdByUserId(user.getUserId());

            if(buyer == null){
//                System.out.println("Buyer profile not found!");
//                return;
                
                Buyer newBuyer = new Buyer();
                newBuyer.setUserId(user.getUserId());
                newBuyer.setShippingAddress("Default Address");
                newBuyer.setBillingAddress("Default Address");

                buyerService.createBuyer(newBuyer);

                buyer =
                    buyerService.getBuyerByUserId(user.getUserId());

                // ALSO CREATE CART
                cartService.createCart(buyer.getBuyerId());
            }

            new BuyerMenu(user,buyer).show();

        } else if ("SELLER".equals(user.getRole())) {

        	Seller seller =
        	        sellerService.getSellerIdByUserId(
        	                user.getUserId());

        	    if(seller == null){
        	    	ConsoleUtil.error("Seller profile missing.");
        	    	return;
        	    }

        	    new SellerMenu(user,seller).show();
//        	    System.out.println(
//        	    	    "\nWelcome Seller " + user.getName());
        }

        
    }
    
    private void forgotPassword(){
    	
        ConsoleUtil.header("RESET PASSWORD");


        System.out.print("Enter registered email: ");
        String email = sc.nextLine();

        System.out.print("Enter new password: ");
        String pass = sc.nextLine();
        
        if(!ValidationUtil.isValidPassword(pass)){
            ConsoleUtil.error("Weak password.");
            return;
        }

        if(userService.forgotPassword(email, pass))
            ConsoleUtil.success("Password reset successful!");
        else
            ConsoleUtil.error("Email not found.");
        
        pause();
    }
    
    private void pause(){
        System.out.println("\nPress ENTER to continue...");
        sc.nextLine();
    }

}

