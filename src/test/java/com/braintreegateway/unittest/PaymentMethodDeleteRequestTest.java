package com.braintreegateway.unittest;

import com.braintreegateway.PaymentMethodDeleteRequest;
import com.braintreegateway.testhelpers.TestHelper;

import org.junit.jupiter.api.Test;

public class PaymentMethodDeleteRequestTest {

    @Test
    public void toQueryStringIncludesRevokeAllGrants() {
        PaymentMethodDeleteRequest request = new PaymentMethodDeleteRequest().revokeAllGrants(true);
        TestHelper.assertIncludes("revoke_all_grants=true", request.toQueryString());
    }
}
