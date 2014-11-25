package com.braintreegateway.util;

import com.braintreegateway.Transaction;
import com.braintreegateway.Dispute;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class EnumUtilsTest {

    @Test
    public void findByNameWithNull() {
        assertNull(EnumUtils.findByName(Transaction.Status.class, null, Transaction.Status.UNRECOGNIZED));
    }

    @Test
    public void findByNameWithExactMatch() {
        assertEquals(Transaction.Status.AUTHORIZED, EnumUtils.findByName(Transaction.Status.class, "AUTHORIZED", Transaction.Status.UNRECOGNIZED));
    }

    @Test
    public void findByNameWithDifferentCase() {
        assertEquals(Transaction.Type.SALE, EnumUtils.findByName(Transaction.Type.class, "saLE", Transaction.Type.UNRECOGNIZED));
    }

    @Test
    public void findByNameDefaultsToProvidedDefaultIfNameDoesNotMatch() {
        assertEquals(Transaction.Status.UNRECOGNIZED, EnumUtils.findByName(Transaction.Status.class, "blah", Transaction.Status.UNRECOGNIZED));
        assertEquals(Transaction.Type.UNRECOGNIZED, EnumUtils.findByName(Transaction.Type.class, "blah", Transaction.Type.UNRECOGNIZED));
    }
}
