package com.revature.util;

import java.sql.Connection;

import java.sql.DriverManager;

public class DBConnection {

    private static Connection con;

    private DBConnection() {}

    public static Connection getConnection() {
//        Con?nection con = null;

    	try {


            if (con == null || con.isClosed()) {
                Class.forName("oracle.jdbc.driver.OracleDriver");
                con = DriverManager.getConnection(
                        "jdbc:oracle:thin:@localhost:1521:xe",
                        "revshop",
                        "tiger"
                );
                
//                System.out.println("Database connected successfully");
            }
        } catch (Exception e) {
        	System.out.println("Database connection failed");
            e.printStackTrace();
        }
        return con;
    }
}
