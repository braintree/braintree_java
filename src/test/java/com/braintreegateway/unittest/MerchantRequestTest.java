package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import com.braintreegateway.MerchantRequest;
import com.braintreegateway.PayPalOnlyAccountRequest;

public class MerchantRequestTest {

    @Test
    public void buildsXmlForAllFieldsIncludingNestedPayPalAccount() {
        MerchantRequest request = new MerchantRequest()
                .email("merchant@example.com")
                .countryCodeAlpha3("USA")
                .companyName("Acme Inc")
                .scope("read_write")
                .paymentMethods(Arrays.asList("credit_card", "paypal"))
                .currencies(Arrays.asList("USD", "EUR"));

        PayPalOnlyAccountRequest paypal = request.payPalAccount();
        assertSame(request, paypal.done());

        String xml = request.toXML();
        assertAll("merchant request xml",
                () -> assertTrue(xml.contains("<merchant>"), xml),
                () -> assertTrue(xml.contains("<email>merchant@example.com</email>"), xml),
                () -> assertTrue(xml.contains("<countryCodeAlpha3>USA</countryCodeAlpha3>"), xml),
                () -> assertTrue(xml.contains("<companyName>Acme Inc</companyName>"), xml),
                () -> assertTrue(xml.contains("<scope>read_write</scope>"), xml),
                () -> assertTrue(xml.contains("<paymentMethods type=\"array\">"), xml),
                () -> assertTrue(xml.contains("<item>credit_card</item>"), xml),
                () -> assertTrue(xml.contains("<currencies type=\"array\">"), xml),
                () -> assertTrue(xml.contains("<item>USD</item>"), xml),
                () -> assertTrue(xml.contains("<paypalAccount>"), xml));
    }
}
