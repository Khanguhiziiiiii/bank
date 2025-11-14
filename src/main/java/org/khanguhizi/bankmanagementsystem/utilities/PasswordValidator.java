package org.khanguhizi.bankmanagementsystem.utilities;

import java.util.regex.Pattern;

public class PasswordValidator {

    private static final String PASSWORD_PATTERN =
            "^(?=.*[0-9])" +           // at least one digit
            "(?=.*[a-z])" +            // at least one lowercase letter
            "(?=.*[A-Z])" +            // at least one uppercase letter
            "(?=.*[@#$%^&+=!()?*._-])" + // at least one special char
            "(?=\\S+$)" +              // no spaces
            ".{8,}$";                  // minimum 8 chars

    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    public static boolean isStrongPassword(String password) {
        return pattern.matcher(password).matches();
    }
}

