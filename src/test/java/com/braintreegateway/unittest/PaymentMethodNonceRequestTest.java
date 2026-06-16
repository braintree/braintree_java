package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.braintreegateway.PaymentMethodNonceRequest;

public class PaymentMethodNonceRequestTest {

    @Test
    public void buildsXmlAndExposesGetters() {
        PaymentMethodNonceRequest request = new PaymentMethodNonceRequest()
                .paymentMethodToken("a-token")
                .merchantAccountId("a-merchant-account")
                .authenticationInsight(true);

        // Exercise nested authenticationInsightOptions() builder.
        request.authenticationInsightOptions().amount(new BigDecimal("10.00")).done();

        assertAll("nonce request getters",
                () -> assertEquals("a-token", request.getPaymentMethodToken()),
                () -> assertEquals("a-merchant-account", request.getMerchantAccountId()),
                () -> assertEquals(true, request.getAuthenticationInsight()));

        assertTrue(request.getAuthenticationInsightOptions() != null);

        String xml = request.toXML();
        assertTrue(xml.contains("<payment-method-nonce>"), xml);
        assertTrue(xml.contains("<merchant-account-id>a-merchant-account</merchant-account-id>"), xml);
        assertTrue(xml.contains("<authentication-insight>true</authentication-insight>"), xml);
        assertTrue(xml.contains("<authenticationInsightOptions>"), xml);
        assertTrue(xml.contains("<amount>10.00</amount>"), xml);
    }
}
