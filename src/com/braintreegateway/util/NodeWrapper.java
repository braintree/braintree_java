package com.braintreegateway.util;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;


public abstract class NodeWrapper {

    public abstract List<NodeWrapper> findAll(String expression);

    public List<String> findAllStrings(String expression) {
        List<String> strings = new ArrayList<String>();

        for (NodeWrapper node : findAll(expression)) {
            strings.add(node.findString("."));
        }

        return strings;
    }

    public BigDecimal findBigDecimal(String expression) {
        String value = findString(expression);
        return value == null ? null : new BigDecimal(value);
    }

    public boolean findBoolean(String expression) {
        String value = findString(expression);
        return Boolean.valueOf(value);
    }

    public Calendar findDate(String expression) {
        try {
            String dateString = findString(expression);
            if (dateString == null) {
                return null;
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            calendar.setTime(dateFormat.parse(dateString));
            return calendar;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Calendar findDateTime(String expression) {
        try {
            String dateString = findString(expression);
            if (dateString == null) {
                return null;
            }
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            dateTimeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            calendar.setTime(dateTimeFormat.parse(dateString));
            return calendar;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Integer findInteger(String expression) {
        String value = findString(expression);
        return value == null ? null : new Integer(value);
    }

    public abstract NodeWrapper findFirst(String expression);

    public abstract String findString(String expression);

    public abstract String getElementName(); //TODO MDM Rename to getName

    public boolean isSuccess() {
        return getElementName() != "api-error-response";
    }

    public Map<String, String> findMap(String expression) {
        Map<String, String> map = new HashMap<String, String>();

        for (NodeWrapper mapNode : findAll(expression)) {
            map.put(StringUtils.underscore(mapNode.getElementName()), mapNode.findString("."));
        }

        return map;
    }

    public abstract Map<String, String> getFormParameters();
}
