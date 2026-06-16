package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.braintreegateway.TransactionDetails;
import com.braintreegateway.util.SimpleNodeWrapper;

public class TransactionDetailsTest {

    @Test
    public void parsesAllFields() {
        SimpleNodeWrapper node = SimpleNodeWrapper.parse(
                "<transaction>" +
                "<id>a-transaction-id</id>" +
                "<amount>25.00</amount>" +
                "</transaction>");

        TransactionDetails details = new TransactionDetails(node);

        assertAll("transaction details",
                () -> assertEquals("a-transaction-id", details.getId()),
                () -> assertEquals(new BigDecimal("25.00"), details.getAmount()));
    }
}
