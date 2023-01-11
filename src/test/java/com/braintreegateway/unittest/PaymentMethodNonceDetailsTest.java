package com.braintreegateway.unittest;

import com.braintreegateway.PaymentMethodNonceDetails;
import com.braintreegateway.util.NodeWrapper;
import com.braintreegateway.util.NodeWrapperFactory;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PaymentMethodNonceDetailsTest {

    @Test
    public void parsesSepaDirectDebitNodesCorrectly() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<payment-method-nonce-details>" +
                "  <bank-reference-token>a-bank-reference-token</bank-reference-token>" +
                "  <correlation-id>a-correlation-id</correlation-id>" +
                "  <iban-last-chars>5678</iban-last-chars>" +
                "  <mandate-type>RECURRENT</mandate-type>" +
                "  <merchant-or-partner-customer-id>a-mp-customer-id</merchant-or-partner-customer-id>" +
                "</payment-method-nonce-details>";

        NodeWrapper nodeWrapper = NodeWrapperFactory.instance.create(xml);
        PaymentMethodNonceDetails paymentMethodNonceDetails = new PaymentMethodNonceDetails(nodeWrapper);

        assertNotNull(paymentMethodNonceDetails);
        assertEquals("a-bank-reference-token", paymentMethodNonceDetails.getSepaDirectDebit().getBankReferenceToken());
        assertEquals("a-correlation-id", paymentMethodNonceDetails.getSepaDirectDebit().getCorrelationId());
        assertEquals("5678", paymentMethodNonceDetails.getSepaDirectDebit().getIbanLastChars());
        assertEquals("RECURRENT", paymentMethodNonceDetails.getSepaDirectDebit().getMandateType());
        assertEquals("a-mp-customer-id", paymentMethodNonceDetails.getSepaDirectDebit().getMerchantOrPartnerCustomerId());
    }
}
