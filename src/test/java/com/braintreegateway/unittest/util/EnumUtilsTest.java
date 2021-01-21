package com.braintreegateway.unittest.util;

import com.braintreegateway.CreditCard;
import com.braintreegateway.Transaction;
import com.braintreegateway.util.EnumUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class EnumUtilsTest {

    public enum TestEnum {
        VALUE1,
        VALUE2,
        VALUE3;

        @Override
        public String toString() {
            return null;
        }
    }

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
    
    @Test
    public void findByNameWithWhiteSpace() {
        assertEquals(Transaction.Status.AUTHORIZATION_EXPIRED, EnumUtils.findByName(Transaction.Status.class, "AUTHORIZATION EXPIRED", Transaction.Status.UNRECOGNIZED));
    }

    @Test
    public void findByToString_returnsEnumWithExactMatch() {
        assertEquals(CreditCard.Commercial.NO, EnumUtils.findByToString(CreditCard.Commercial.values(), "No", CreditCard.Commercial.UNKNOWN));
    }

    @Test
    public void findByToString_returnsDefaultIfNullName() {
        assertEquals(CreditCard.Commercial.UNKNOWN, EnumUtils.findByToString(CreditCard.Commercial.values(), null, CreditCard.Commercial.UNKNOWN));
    }

    @Test
    public void findByToString_returnsDefaultIfNullValues() {
        assertEquals(CreditCard.Commercial.UNKNOWN, EnumUtils.findByToString(null, "No", CreditCard.Commercial.UNKNOWN));
    }

    @Test
    public void findByToString_returnsDefaultIfCaseNotAMatch() {
        assertEquals(CreditCard.Commercial.UNKNOWN, EnumUtils.findByToString(CreditCard.Commercial.values(), "NO", CreditCard.Commercial.UNKNOWN));
    }

    @Test
    public void findByToString_returnsDefaultIfNotFound() {
        assertEquals(CreditCard.Commercial.UNKNOWN, EnumUtils.findByToString(CreditCard.Commercial.values(), "404", CreditCard.Commercial.UNKNOWN));
    }

    @Test
    public void findByToString_returnsNullIfNotAMatchAndDefaultValueIsNull() {
        assertNull(EnumUtils.findByToString(CreditCard.Commercial.values(), "404", null));
    }

    @Test
    public void findByToString_returnsDefaultIfToStringIsNull() {
        assertEquals(TestEnum.VALUE3, EnumUtils.findByToString(TestEnum.values(), "VALUE1", TestEnum.VALUE3));
    }
}
