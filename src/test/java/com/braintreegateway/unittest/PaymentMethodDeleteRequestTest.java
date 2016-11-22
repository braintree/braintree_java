package com.braintreegateway.unittest;

import org.junit.Test;

import com.braintreegateway.PaymentMethodDeleteRequest;
import com.braintreegateway.testhelpers.TestHelper;

public class PaymentMethodDeleteRequestTest {

    @Test
    public void toQueryStringIncludesRevokeAllGrants() {
        PaymentMethodDeleteRequest request = new PaymentMethodDeleteRequest().revokeAllGrants(true);
        TestHelper.assertIncludes("revoke_all_grants=true", request.toQueryString());
    }
}
