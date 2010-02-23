package com.braintreegateway;

import org.junit.Assert;
import org.junit.Test;

public class CreditCardRequestTest {
    @Test
    public void toQueryString() {
        CreditCardRequest request = new CreditCardRequest().
            cardholderName("Drew").
            billingAddress().
                region("Chicago").
                done();
        Assert.assertEquals("credit_card%5Bbilling_address%5D%5Bregion%5D=Chicago&credit_card%5Bcardholder_name%5D=Drew", request.toQueryString());
    }

    @Test
    public void toQueryStringWithParent() {
        CreditCardRequest request = new CreditCardRequest().
            cardholderName("Drew").
            billingAddress().
                region("Chicago").
                done();
        Assert.assertEquals("customer%5Bcredit_card%5D%5Bbilling_address%5D%5Bregion%5D=Chicago&customer%5Bcredit_card%5D%5Bcardholder_name%5D=Drew", request.toQueryString("customer[credit_card]"));
    }
}
