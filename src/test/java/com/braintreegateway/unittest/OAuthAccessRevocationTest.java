package com.braintreegateway.unittest;

import com.braintreegateway.OAuthAccessRevocation;
import com.braintreegateway.util.NodeWrapper;
import com.braintreegateway.util.NodeWrapperFactory;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class OAuthAccessRevocationTest {
    @Test
    public void assignsMerchantId() {
        String xmlPayload = "<oauth-application-revocation>" +
            "<merchant-id>abc123def</merchant-id>" +
            "<oauth-application-client-id>uvw789xyz</oauth-application-client-id>" +
            "</oauth-application-revocation>";
        NodeWrapper node = NodeWrapperFactory.instance.create(xmlPayload);
        OAuthAccessRevocation revocation = new OAuthAccessRevocation(node);

        assertEquals(revocation.getMerchantId(), "abc123def");
        assertEquals(revocation.getOauthApplicationClientId(), "uvw789xyz");
    }
}
