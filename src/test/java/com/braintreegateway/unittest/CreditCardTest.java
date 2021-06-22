package com.braintreegateway.unittest;

import com.braintreegateway.CreditCard;
import com.braintreegateway.util.SimpleNodeWrapper;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CreditCardTest {
    @Test
    public void testVerificationIsTheLatestVerification() {
        String xml = "<credit-card>"
                     + "<verifications>"
                     + "    <verification>"
                     + "        <created-at type=\"datetime\">2014-11-20T17:27:15Z</created-at>"
                     + "        <id>123</id>"
                     + "    </verification>"
                     + "    <verification>"
                     + "        <created-at type=\"datetime\">2014-11-20T17:27:18Z</created-at>"
                     + "        <id>932</id>"
                     + "    </verification>"
                     + "    <verification>"
                     + "        <created-at type=\"datetime\">2014-11-20T17:27:17Z</created-at>"
                     + "        <id>456</id>"
                     + "    </verification>"
                     + "</verifications>"
                   + "</credit-card>";

        SimpleNodeWrapper creditCardNode = SimpleNodeWrapper.parse(xml);
        CreditCard card = new CreditCard(creditCardNode);

        assertEquals("932", card.getVerification().getId());
    }
}
