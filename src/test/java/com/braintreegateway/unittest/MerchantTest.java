package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.braintreegateway.Merchant;
import com.braintreegateway.util.SimpleNodeWrapper;

public class MerchantTest {

    @Test
    public void parsesMerchantCredentialsAndMerchantAccounts() {
        SimpleNodeWrapper node = SimpleNodeWrapper.parse(
                "<response>" +
                "<merchant>" +
                "<id>merchant_id</id>" +
                "<email>merchant@example.com</email>" +
                "<company-name>Acme Inc</company-name>" +
                "<country-code-alpha3>USA</country-code-alpha3>" +
                "<country-code-alpha2>US</country-code-alpha2>" +
                "<country-code-numeric>840</country-code-numeric>" +
                "<country-name>United States of America</country-name>" +
                "<merchant-accounts type=\"array\">" +
                "<merchant-account><id>ma_id</id><status>active</status></merchant-account>" +
                "</merchant-accounts>" +
                "</merchant>" +
                "<credentials>" +
                "<access-token>access_token</access-token>" +
                "<refresh-token>refresh_token</refresh-token>" +
                "<token-type>bearer</token-type>" +
                "</credentials>" +
                "</response>");

        Merchant merchant = new Merchant(node);

        assertAll("merchant fields",
                () -> assertEquals("merchant_id", merchant.getId()),
                () -> assertEquals("merchant@example.com", merchant.getEmail()),
                () -> assertEquals("Acme Inc", merchant.getCompanyName()),
                () -> assertEquals("USA", merchant.getCountryCodeAlpha3()),
                () -> assertEquals("US", merchant.getCountryCodeAlpha2()),
                () -> assertEquals("840", merchant.getCountryCodeNumeric()),
                () -> assertEquals("United States of America", merchant.getCountryName()),
                () -> assertEquals("access_token", merchant.getCredentials().getAccessToken()),
                () -> assertEquals(1, merchant.getMerchantAccounts().size()),
                () -> assertEquals("ma_id", merchant.getMerchantAccounts().get(0).getId()));
    }
}
