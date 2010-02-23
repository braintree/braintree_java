package com.braintreegateway;

import junit.framework.Assert;

import org.junit.Test;

public class TransactionStatusTest {
    
    @Test
    public void findByStatus() {
        Assert.assertEquals(Transaction.Status.AUTHORIZED, Transaction.Status.findByStatus("AUTHORIZED"));
    }
    
    @Test
    public void findByStatusIsCaseInsensitive() {
        Assert.assertEquals(Transaction.Status.AUTHORIZED, Transaction.Status.findByStatus("authorized"));
    }

    @Test
    public void findByStatusWithNull() {
        Assert.assertNull(Transaction.Status.findByStatus(null));
    }
    
    @Test
    public void findByStatusFallsBackWhenNotFound() {
        Assert.assertEquals(Transaction.Status.UNRECOGNIZED, Transaction.Status.findByStatus("invalid_status"));
    }
}
