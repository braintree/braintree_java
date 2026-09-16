package com.braintreegateway.unittest;

import com.braintreegateway.TransactionSearchRequest;
import com.braintreegateway.Transaction;
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

    @Test
    public void achTypeSameDayTest() {
        TransactionSearchRequest transactionSearchRequest = new TransactionSearchRequest();
        String expectedXml = "<search><ach_type type=\"array\"><item>same_day</item></ach_type></search>";
        assertEquals(expectedXml, transactionSearchRequest.achType().is(Transaction.AchType.SAME_DAY).toXML());
    }

    @Test
    public void achTypeStandardTest() {
        TransactionSearchRequest transactionSearchRequest = new TransactionSearchRequest();
        String expectedXml = "<search><ach_type type=\"array\"><item>standard</item></ach_type></search>";
        assertEquals(expectedXml, transactionSearchRequest.achType().is(Transaction.AchType.STANDARD).toXML());
    }

    @Test
    public void achTypeInBothValuesTest() {
        TransactionSearchRequest transactionSearchRequest = new TransactionSearchRequest();
        String expectedXml = "<search><ach_type type=\"array\"><item>same_day</item><item>standard</item></ach_type></search>";
        assertEquals(expectedXml, transactionSearchRequest.achType().in(Transaction.AchType.SAME_DAY, Transaction.AchType.STANDARD).toXML());
    }
}
