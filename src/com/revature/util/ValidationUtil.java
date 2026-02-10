package com.revature.util;

import java.util.regex.Pattern;

public class ValidationUtil {

    // ✅ Email regex
    private static final String EMAIL_REGEX =
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    // ✅ Strong password regex
    private static final String PASSWORD_REGEX =
            "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$";

    private static final String Number_REGEX =
    		"^[89][0-9]{9}$";
    public static boolean isValidNumber(String phNumber) {
        return Pattern.matches(Number_REGEX, phNumber);
    }
    public static boolean isValidEmail(String email) {
        return Pattern.matches(EMAIL_REGEX, email);
    }

    public static boolean isValidPassword(String password) {
        return Pattern.matches(PASSWORD_REGEX, password);
    }
}
