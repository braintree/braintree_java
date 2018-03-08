package com.braintreegateway.unittest;

import org.junit.Test;
import static org.junit.Assert.*;

import com.braintreegateway.OAuthAccessRevocation;
import com.braintreegateway.util.NodeWrapper;
import com.braintreegateway.util.NodeWrapperFactory;

public class OAuthAccessRevocationTest {
    @Test
    public void assignsMerchantId() {
        String xmlPayload = "<oauth-application-revocation>" +
            "<merchant-id>abc123def</merchant-id>" +
            "</oauth-application-revocation>";
        NodeWrapper node = NodeWrapperFactory.instance.create(xmlPayload);
        OAuthAccessRevocation revocation = new OAuthAccessRevocation(node);

        assertEquals(revocation.getMerchantId(), "abc123def");
    }
}
