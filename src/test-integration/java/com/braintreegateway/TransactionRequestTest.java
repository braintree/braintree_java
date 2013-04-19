package com.braintreegateway;

import com.braintreegateway.testhelpers.TestHelper;
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
    
    @Test
    public void customFieldsEscapesKeysAndValues() {
        TransactionRequest request = new TransactionRequest().customField("ke&y", "va<lue");
        TestHelper.assertIncludes("<customFields><ke&amp;y>va&lt;lue</ke&amp;y></customFields>", request.toXML());
    }
}
