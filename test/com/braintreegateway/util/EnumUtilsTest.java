package com.braintreegateway.util;

import junit.framework.Assert;

import org.junit.Test;

import com.braintreegateway.Transaction;

public class EnumUtilsTest {

    @Test
    public void findByNameWithNull() {
        Assert.assertNull(EnumUtils.findByName(Transaction.Status.class, null));
    }

    @Test
    public void findByNameWithExactMatch() {
        Assert.assertEquals(Transaction.Status.AUTHORIZED, EnumUtils.findByName(Transaction.Status.class, "AUTHORIZED"));
    }
    
    @Test
    public void findByNameWithDifferentCase() {
        Assert.assertEquals(Transaction.Type.SALE, EnumUtils.findByName(Transaction.Type.class, "saLE"));
    }

    @Test
    public void findByNameDefaultsToUnrecognizedIfNameDoesNotMatch() {
        Assert.assertEquals(Transaction.Status.UNRECOGNIZED, EnumUtils.findByName(Transaction.Status.class, "blah"));
        Assert.assertEquals(Transaction.Type.UNRECOGNIZED, EnumUtils.findByName(Transaction.Type.class, "blah"));
    }

}
