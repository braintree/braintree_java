package com.braintreegateway.unittest;

import com.braintreegateway.TransactionSearchRequest;
import com.braintreegateway.CreditCard;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TransactionSearchRequestTest {

    @Test
    public void sepaDirectPayPalV2OrderIdTest() {
        TransactionSearchRequest transactionSearchRequest = new TransactionSearchRequest();

        String expectedXml = "<search><sepa_debit_paypal_v2_order_id><is>hello</is></sepa_debit_paypal_v2_order_id></search>";
        assertEquals(expectedXml, transactionSearchRequest.sepaDirectDebitPayPalV2OrderId().is("hello").toXML());
    }

    @Test
    public void debitNetworkTest() {
        TransactionSearchRequest transactionSearchRequest = new TransactionSearchRequest();
        String expectedXml = "<search><debit_network type=\"array\"><item>STAR</item></debit_network></search>";
        assertEquals(expectedXml, transactionSearchRequest.debitNetwork().is(CreditCard.DebitNetwork.STAR).toXML());
    }
}
