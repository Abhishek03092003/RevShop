package com.revature.util;

public class ConsoleUtil {

    public static void header(String title){
        System.out.println("\n==========================================================");
        System.out.println("   " + title);
        System.out.println("============================================================");
    }

    public static void success(String msg){
        System.out.println("✅ " + msg);
    }

    public static void error(String msg){
        System.out.println("❌ " + msg);
    }

    public static void warn(String msg){
        System.out.println("⚠ " + msg);
    }
    
    public static void line(){
        System.out.println("=============================================================");
    }
    
    public static void separator(){
        System.out.println("------------------------------------------------");
    }



}
