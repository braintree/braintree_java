package com.braintreegateway;

import java.util.Calendar;

import junit.framework.Assert;

import com.braintreegateway.Transaction.Status;
import com.braintreegateway.util.Crypto;

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
    
    public static void assertValidTrData(Configuration configuration, String trData) {
        String[] dataSections = trData.split("\\|");
        String trHash = dataSections[0];
        String trContent = dataSections[1];
        Assert.assertEquals(trHash, new Crypto().hmacHash(configuration.privateKey, trContent));
    }
    
    public static boolean pagedCollectionContains(PagedCollection<Subscription> collection, Subscription item) {
        for (Subscription subscription : collection.getItems()) {
            if (subscription.getId().equals(item.getId())) {
                return true;
            }
        }
        
        if (collection.isLastPage()) {
            return false;
        }
        
        return pagedCollectionContains(collection.getNextPage(), item);
    }
    
    public static boolean pagedCollectionContainsStatus(PagedCollection<Transaction> collection, Status status) {
        for (Transaction transaction : collection.getItems()) {
            if (transaction.getStatus().equals(status)) {
                return true;
            }
        }
        
        if (collection.isLastPage()) {
            return false;
        }
        
        return pagedCollectionContainsStatus(collection.getNextPage(), status);
    }
}
