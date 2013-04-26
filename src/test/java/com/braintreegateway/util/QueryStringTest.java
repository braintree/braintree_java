package com.braintreegateway.util;

import com.braintreegateway.CreditCardRequest;
import com.braintreegateway.Request;
import com.braintreegateway.testhelpers.TestHelper;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class QueryStringTest {
    @Test
    public void append() {
        String actual = new QueryString().
            append("foo", "f").
            append("bar", "b").
            toString();
        assertEquals("foo=f&bar=b", actual);
    }

    @Test
    public void appendEmptyStringOrNulls() {
        String actual = new QueryString().
            append("foo", "f").
            append("", "b").
            append("bar", "").
            append("boom", null).
            append("", "c").
            toString();

        assertEquals("foo=f&bar=", actual);
    }

    @Test
    public void appendOtherObjectsWithCanBeConvertedToStrings() {
        String actual = new QueryString().
            append("foo", 10).
            append("bar", new BigDecimal("20.00")).
            toString();

        assertEquals("foo=10&bar=20.00", actual);
    }

    @Test
    public void appendWithRequest() {
        Request request = new CreditCardRequest().cvv("123").cardholderName("Drew");
        String actual = new QueryString().
            append("[credit_card]", request).
            toString();

        TestHelper.assertIncludes("%5Bcredit_card%5D%5Bcardholder_name%5D=Drew", actual);
        TestHelper.assertIncludes("%5Bcredit_card%5D%5Bcvv%5D=123", actual);
    }

    @Test
    public void appendWithMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("name", "john");
        map.put("age", "15");
        String actual = new QueryString().append("transaction[custom_fields]", map).toString();

        TestHelper.assertIncludes("transaction%5Bcustom_fields%5D%5Bage%5D=15", actual);
        TestHelper.assertIncludes("transaction%5Bcustom_fields%5D%5Bname%5D=john", actual);
    }

    @Test
    public void appendWithNestedRequest() {
        Request request = new CreditCardRequest().
            cvv("123").
            cardholderName("Drew").
            billingAddress().
                company("Braintree").
                done().
            options().
                makeDefault(true).
                verifyCard(true).
                done();

        String actual = new QueryString().append("[credit_card]", request).toString();

        TestHelper.assertIncludes("%5Bcredit_card%5D%5Bcardholder_name%5D=Drew", actual);
        TestHelper.assertIncludes("%5Bcredit_card%5D%5Bcvv%5D=123", actual);
        TestHelper.assertIncludes("%5Bcredit_card%5D%5Bbilling_address%5D%5Bcompany%5D=Braintree", actual);
        TestHelper.assertIncludes("%5Bcredit_card%5D%5Boptions%5D%5Bmake_default%5D=true", actual);
        TestHelper.assertIncludes("%5Bcredit_card%5D%5Boptions%5D%5Bverify_card%5D=true", actual);
    }
}
