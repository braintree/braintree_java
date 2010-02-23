package com.braintreegateway;

import java.util.Calendar;

public class TestHelper {

    public static void assertDatesEqual(Calendar first, Calendar second) {
        if (first == null && second != null) {
            throw new AssertionError("dates are not equal. first is null, second is not");
        }
        else if (first != null && second == null) {
            throw new AssertionError("dates are not equal. second is null, first is not");
        }
        boolean yearsNotEqual = first.get(Calendar.YEAR) != second.get(Calendar.YEAR); 
        boolean monthsNotEqual = first.get(Calendar.MONTH) != second.get(Calendar.MONTH); 
        boolean daysNotEqual = first.get(Calendar.DAY_OF_MONTH) != second.get(Calendar.DAY_OF_MONTH); 
        if (yearsNotEqual || monthsNotEqual || daysNotEqual) {
            StringBuffer buffer = new StringBuffer("dates are not equal. ");
            if (yearsNotEqual) {
                buffer.append("years (" + first.get(Calendar.YEAR) +  ", " + second.get(Calendar.YEAR) + ") not equal.");
            }
            if (monthsNotEqual) {
                buffer.append("months (" + first.get(Calendar.MONTH) +  ", " + second.get(Calendar.MONTH) + ") not equal.");
            }
            if (daysNotEqual) {
                buffer.append("days (" + first.get(Calendar.DAY_OF_MONTH) +  ", " + second.get(Calendar.DAY_OF_MONTH) + ") not equal.");
            }
            throw new AssertionError(buffer.toString());
        }
    }
}
