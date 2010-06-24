package com.braintreegateway.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StringUtils {
    public static <T> String classToXMLName(Class<T> klass) {
        return dasherize(klass.getSimpleName()).toLowerCase();
    }
    
    public static String dasherize(String str) {
        return str == null ? null : str.replaceAll("([a-z])([A-Z])", "$1-$2").replaceAll("_", "-").toLowerCase();
    }

    public static String getFullPathOfFile(String filename) {
        return getClassLoader().getResource(filename).getFile();
    }

    private static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }
    
    public static String inputStreamToString(InputStream inputStream) throws IOException {
        InputStreamReader inputReader = new InputStreamReader(inputStream);
        StringBuilder builder = new StringBuilder();
        char[] buffer = new char[0x1000];
        int bytesRead = inputReader.read(buffer, 0, buffer.length);
        while (bytesRead >= 0) {
            builder.append(buffer, 0, bytesRead);
            bytesRead = inputReader.read(buffer, 0, buffer.length);
        }
        return builder.toString();
    }
    
    public static String nullIfEmpty(String str) {
        return str == null || str.length() == 0 ? null : str;
    }

    public static String underscore(String str) {
        return str == null ? null : str.replaceAll("([A-Z])", "_$0").replaceAll("-", "_").toLowerCase();
    }
}
