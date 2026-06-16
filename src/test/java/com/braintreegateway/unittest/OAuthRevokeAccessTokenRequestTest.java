package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.braintreegateway.OAuthRevokeAccessTokenRequest;

public class OAuthRevokeAccessTokenRequestTest {

    @Test
    public void buildsXmlWithToken() {
        String xml = new OAuthRevokeAccessTokenRequest()
                .token("a-token")
                .toXML();

        assertTrue(xml.contains("<credentials>"), xml);
        assertTrue(xml.contains("<token>a-token</token>"), xml);
    }
}
