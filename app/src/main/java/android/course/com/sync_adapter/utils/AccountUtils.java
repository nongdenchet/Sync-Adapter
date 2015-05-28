package android.course.com.sync_adapter.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by nongdenchet on 5/28/15.
 */
public class AccountUtils {
    /**
     * Check if a string is valid for username and password
     * @param string
     * the string input
     *
     * @return
     * true or false
     */
    public static boolean checkValidNameOrPassword(String string) {
        return string.length() >= 8;
    }

    /**
     * Check if a string is format as email
     * @param email
     * the string input
     *
     * @return
     * true or false
     */
    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }
}
