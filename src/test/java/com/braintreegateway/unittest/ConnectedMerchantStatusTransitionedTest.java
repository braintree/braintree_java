package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.braintreegateway.ConnectedMerchantStatusTransitioned;
import com.braintreegateway.util.SimpleNodeWrapper;

public class ConnectedMerchantStatusTransitionedTest {

    @Test
    public void parsesAllFields() {
        SimpleNodeWrapper node = SimpleNodeWrapper.parse(
                "<connected-merchant-status-transitioned>" +
                "<merchant-public-id>a-merchant-public-id</merchant-public-id>" +
                "<oauth-application-client-id>a-client-id</oauth-application-client-id>" +
                "<status>new_status</status>" +
                "</connected-merchant-status-transitioned>");

        ConnectedMerchantStatusTransitioned transitioned =
                new ConnectedMerchantStatusTransitioned(node);

        assertAll("connected merchant status transitioned fields",
                () -> assertEquals("a-merchant-public-id", transitioned.getMerchantPublicId()),
                () -> assertEquals("a-merchant-public-id", transitioned.getMerchantId()),
                () -> assertEquals("a-client-id", transitioned.getOAuthApplicationClientId()),
                () -> assertEquals("new_status", transitioned.getStatus()));
    }
}
