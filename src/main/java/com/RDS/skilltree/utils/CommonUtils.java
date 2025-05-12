package com.RDS.skilltree.utils;

import java.util.regex.Pattern;

public class CommonUtils {
    private static final Pattern UUID_REGEX =
            Pattern.compile(
                    "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");

    public static boolean isValidUUID(String uuidString) {

        return UUID_REGEX.matcher(uuidString).matches();
    }
}
