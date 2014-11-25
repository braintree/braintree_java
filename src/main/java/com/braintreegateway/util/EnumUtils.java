package com.braintreegateway.util;

public class EnumUtils {

    public static <T extends Enum<T>> T findByName(Class<T> enumType, String name, T defaultValue) {
        if (name == null) {
            return null;
        }
        try {
            return Enum.valueOf(enumType, name.toUpperCase().replaceAll(" ", "_"));
        } catch (IllegalArgumentException e) {
            return defaultValue;
        }
    }
}
