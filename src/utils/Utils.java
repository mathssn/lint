package src.utils;

public class Utils {
    
    public static boolean isDigit(String str) {
        if (checkEmptyOrNull(str)) {return false;}
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isAlphabetic(String str) {
        if (checkEmptyOrNull(str)) {return false;}
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isAlphabetic(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isAlphaNum(String str) {
        if (checkEmptyOrNull(str)) {return false;}
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isAlphabetic(str.charAt(i)) && !Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean checkEmptyOrNull(String str) {
        if (str.isEmpty() || str == null) {
            return true;
        }
        return false;
    }
}
