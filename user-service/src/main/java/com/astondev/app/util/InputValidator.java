package com.astondev.app.util;

public class InputValidator {
    public static boolean isValidAge(String age) {
        try {
            return Integer.parseInt(age) >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
