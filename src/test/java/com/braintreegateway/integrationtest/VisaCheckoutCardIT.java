package com.braintreegateway.integrationtest;

import com.braintreegateway.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// Visa Checkout is no longer supported for creating new transactions.
// Search functionality is retained for historical transactions only.
public class VisaCheckoutCardIT extends IntegrationTest {

    @Test
    public void searchOnPaymentInstrumentTypeIsVisaCheckoutCard() {
        TransactionSearchRequest searchRequest = new TransactionSearchRequest().
            paymentInstrumentType().is(PaymentInstrumentType.VISA_CHECKOUT_CARD);

        ResourceCollection<Transaction> collection = gateway.transaction().search(searchRequest);

        assertNotNull(collection);
    }
}
