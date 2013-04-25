package com.braintreegateway.integrationtest;

import org.junit.Assert;
import org.junit.Test;
import com.braintreegateway.CreditCardRequest;

public class CreditCardRequestIT {
    @Test
    public void toXmlEscapesXmlChars() {
        CreditCardRequest request = new CreditCardRequest().
            cardholderName("Special Xml Chars <>&\"'");
         Assert.assertEquals("<creditCard><cardholderName>Special Xml Chars &lt;&gt;&amp;&quot;&apos;</cardholderName></creditCard>", request.toXML());
    }

    @Test
    public void toQueryString() {
        CreditCardRequest request = new CreditCardRequest().
            cardholderName("Drew").
            billingAddress().
                region("Chicago").
                done();
        Assert.assertEquals("credit_card%5Bcardholder_name%5D=Drew&credit_card%5Bbilling_address%5D%5Bregion%5D=Chicago", request.toQueryString());
    }

    @Test
    public void toQueryStringWithParent() {
        CreditCardRequest request = new CreditCardRequest().
            cardholderName("Drew").
            billingAddress().
                region("Chicago").
                done();
        Assert.assertEquals("customer%5Bcredit_card%5D%5Bcardholder_name%5D=Drew&customer%5Bcredit_card%5D%5Bbilling_address%5D%5Bregion%5D=Chicago", request.toQueryString("customer[credit_card]"));
    }
}
