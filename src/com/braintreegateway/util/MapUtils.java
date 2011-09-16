package com.braintreegateway.util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
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

    public static String mapToString(Map<String, Object> map) {
        LinkedList<String> pairs = new LinkedList<String>();
        for (String s : map.keySet())
        {
            Object value = map.get(s);
            String valueStr = toString(value);
            pairs.add(s + ": " + valueStr);
        }

        return "{" + StringUtils.join(", ", pairs.toArray()) + "}";
    }

    private static String toString(Object value) {
        if(value instanceof Map)
            return mapToString((Map<String, Object>) value);
        else if(value instanceof List)
            return listToString((List<Object>) value);
        else
            return value.toString();
    }

    private static String listToString(List<Object> value) {
        String[] valueStrings = new String[value.size()];
        for (int i = 0; i < valueStrings.length; i++) {
            valueStrings[i] = toString(value.get(i));
        }
        return "[" + StringUtils.join(", ", valueStrings) + "]";
    }
}
