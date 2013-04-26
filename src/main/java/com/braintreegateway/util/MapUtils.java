package com.braintreegateway.util;

import java.util.HashMap;
import java.util.Map;

public class MapUtils {
    public static Map<String, Object> toMap(Object... args) {
        if (args.length % 2 == 1)
            throw new RuntimeException("toMap must be called with an even number of parameters");
        HashMap<String, Object> map = new HashMap<String, Object>();
        for (int i = 0; i < args.length; i += 2) {
            String key = "" + args[i];
            Object value = args[i + 1];
            map.put(key, value);
        }
        return map;
    }

}
