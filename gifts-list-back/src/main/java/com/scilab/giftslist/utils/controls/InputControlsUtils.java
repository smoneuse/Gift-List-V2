package com.scilab.giftslist.utils.controls;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public final class InputControlsUtils {
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =   Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private InputControlsUtils(){}

    public static boolean isAValidEmail(String str){
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(str);
        return matcher.matches();
    }

    public static boolean checkNoSpace(String str){
        return !str.contains(StringUtils.SPACE);
    }
}
