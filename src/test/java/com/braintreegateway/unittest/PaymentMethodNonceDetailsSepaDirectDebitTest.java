package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.braintreegateway.PaymentMethodNonceDetailsSepaDirectDebit;
import com.braintreegateway.SepaDirectDebitAccountDetails.MandateType;
import com.braintreegateway.util.SimpleNodeWrapper;

public class PaymentMethodNonceDetailsSepaDirectDebitTest {

    @Test
    public void parsesFromNodeWrapper() {
        SimpleNodeWrapper node = SimpleNodeWrapper.parse(
                "<sepa-debit>" +
                "<bank-reference-token>a-bank-ref-token</bank-reference-token>" +
                "<correlation-id>a-correlation-id</correlation-id>" +
                "<iban-last-chars>1234</iban-last-chars>" +
                "<mandate-type>ONE_OFF</mandate-type>" +
                "<merchant-or-partner-customer-id>cust-123</merchant-or-partner-customer-id>" +
                "</sepa-debit>");

        PaymentMethodNonceDetailsSepaDirectDebit details =
                new PaymentMethodNonceDetailsSepaDirectDebit(node);

        assertAll("sepa direct debit from node",
                () -> assertEquals("a-bank-ref-token", details.getBankReferenceToken()),
                () -> assertEquals("a-correlation-id", details.getCorrelationId()),
                () -> assertEquals("1234", details.getIbanLastChars()),
                () -> assertEquals(MandateType.ONE_OFF.name(), details.getMandateType()),
                () -> assertEquals("cust-123", details.getMerchantOrPartnerCustomerId()));
    }

    @Test
    public void parsesFromMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("bank-reference-token", "a-bank-ref-token");
        map.put("correlation-id", "a-correlation-id");
        map.put("iban-last-chars", "1234");
        map.put("mandate-type", "RECURRENT");
        map.put("merchant-or-partner-customer-id", "cust-123");

        PaymentMethodNonceDetailsSepaDirectDebit details =
                new PaymentMethodNonceDetailsSepaDirectDebit(map);

        assertAll("sepa direct debit from map",
                () -> assertEquals("a-bank-ref-token", details.getBankReferenceToken()),
                () -> assertEquals(MandateType.RECURRENT.name(), details.getMandateType()),
                () -> assertEquals("cust-123", details.getMerchantOrPartnerCustomerId()));
    }
}
