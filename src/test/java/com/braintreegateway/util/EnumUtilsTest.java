package com.braintreegateway.util;

import com.braintreegateway.Transaction;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class EnumUtilsTest {

    @Test
    public void findByNameWithNull() {
        assertNull(EnumUtils.findByName(Transaction.Status.class, null));
    }

    @Test
    public void findByNameWithExactMatch() {
        assertEquals(Transaction.Status.AUTHORIZED, EnumUtils.findByName(Transaction.Status.class, "AUTHORIZED"));
    }

    @Test
    public void findByNameWithDifferentCase() {
        assertEquals(Transaction.Type.SALE, EnumUtils.findByName(Transaction.Type.class, "saLE"));
    }

    @Test
    public void findByNameDefaultsToUnrecognizedIfNameDoesNotMatch() {
        assertEquals(Transaction.Status.UNRECOGNIZED, EnumUtils.findByName(Transaction.Status.class, "blah"));
        assertEquals(Transaction.Type.UNRECOGNIZED, EnumUtils.findByName(Transaction.Type.class, "blah"));
    }

}
