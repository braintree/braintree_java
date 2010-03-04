package com.braintreegateway;

import org.junit.Assert;
import org.junit.Test;

public class TransactionRequestTest {
    @Test
    public void toQueryStringWithNestedCustomer() {
        TransactionRequest request = new TransactionRequest().
            customer().
                firstName("Drew").
                done();

        Assert.assertEquals("transaction%5Bcustomer%5D%5Bfirst_name%5D=Drew", request.toQueryString());
    }
}
