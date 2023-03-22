package com.braintreegateway.unittest;

import com.braintreegateway.SepaDirectDebitAccount;
import com.braintreegateway.Subscription;
import com.braintreegateway.util.SimpleNodeWrapper;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

public class SepaDirectDebitAccountTest {
    @Test
    public void testSepaDirectDebitAccountAttributes() {
        String xml = "<sepa-debit-account>"
                     + "<bank-reference-token>a-bank-reference-token</bank-reference-token>"
                     + "<created-at type=\"datetime\">2017-06-16T20:44:41Z</created-at>"
                     + "<customer-global-id>a-customer-global-id</customer-global-id>"
                     + "<customer-id>a-customer-id</customer-id>"
                     + "<merchant-or-partner-customer-id>a-merchant-or-partner-customer-id</merchant-or-partner-customer-id>"
                     + "<global-id>a-global-id</global-id>"
                     + "<last-4>1234</last-4>"
                     + "<image-url>https://jsdk.docker.dev:9000/payment_method_logo/sepa_debit_account.png?environment=test</image-url>"
                     + "<default type=\"boolean\">true</default>"
                     + "<mandate-type>ONE_OFF</mandate-type>"
                     + "<merchant-account-id>a-merchant-account-id</merchant-account-id>"
                     + "<subscriptions type='array'><subscription><price>10.00</price></subscription></subscriptions>"
                     + "<token>ch6byss</token>"
                     + "<updated-at type=\"datetime\">2017-06-16T20:44:41Z</updated-at>"
                     + "<view-mandate-url>https://paypal.com/</view-mandate-url>"
                     + "</sepa-debit-account>";

        SimpleNodeWrapper node = SimpleNodeWrapper.parse(xml);
        SepaDirectDebitAccount sepaDirectDebitAccount = new SepaDirectDebitAccount(node);

        assertEquals("a-bank-reference-token", sepaDirectDebitAccount.getBankReferenceToken());
        assertNotNull(sepaDirectDebitAccount.getCreatedAt());
        assertEquals("a-customer-global-id", sepaDirectDebitAccount.getCustomerGlobalId());
        assertEquals("a-customer-id", sepaDirectDebitAccount.getCustomerId());
        assertEquals("a-merchant-or-partner-customer-id", sepaDirectDebitAccount.getMerchantOrPartnerCustomerId());
        assertEquals("a-global-id", sepaDirectDebitAccount.getGlobalId());
        assertEquals("1234", sepaDirectDebitAccount.getLast4());
        assertEquals("https://jsdk.docker.dev:9000/payment_method_logo/sepa_debit_account.png?environment=test", sepaDirectDebitAccount.getImageUrl());
        assertEquals(true, sepaDirectDebitAccount.isDefault());
        assertEquals("ONE_OFF", sepaDirectDebitAccount.getMandateType().name());
        assertEquals("a-merchant-account-id", sepaDirectDebitAccount.getMerchantAccountId());
        List<Subscription> subs = sepaDirectDebitAccount.getSubscriptions();
        assertEquals(1, subs.size());
        assertEquals(new BigDecimal("10.00"), subs.get(0).getPrice());
        assertEquals("ch6byss", sepaDirectDebitAccount.getToken());
        assertNotNull(sepaDirectDebitAccount.getUpdatedAt());
        assertEquals("https://paypal.com/", sepaDirectDebitAccount.getViewMandateUrl());
    }
}
