package com.astondev.app.utils;

public class InputValidator {
    public static boolean isValidAge(String age) {
        try {
            return Integer.parseInt(age) >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidId(String id) {
        try {
            return Long.parseLong(id) >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
