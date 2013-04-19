package com.braintreegateway.util;

public class EnumUtils {

    public static <T extends Enum<T>> T findByName(Class<T> enumType, String name) {
        if (name == null) {
            return null;
        }
        try {
            return Enum.valueOf(enumType, name.toUpperCase().replaceAll(" ", "_"));
        } catch (IllegalArgumentException e) {
            return Enum.valueOf(enumType, "UNRECOGNIZED");
        }
    }
}
