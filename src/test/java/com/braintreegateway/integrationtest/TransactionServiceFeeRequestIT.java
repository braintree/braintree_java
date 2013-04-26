package com.braintreegateway.integrationtest;

import com.braintreegateway.TransactionServiceFeeRequest;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class TransactionServiceFeeRequestIT {
    @Test
    public void toXMLIncludesMerchantAccountIdAndAmount() {
        String expected = "<serviceFee><amount>1.00</amount><merchantAccountId>abcdef</merchantAccountId></serviceFee>";
        TransactionServiceFeeRequest request = new TransactionServiceFeeRequest(null).
    		amount(new BigDecimal("1.00")).
    		merchantAccountId("abcdef");

        assertEquals(expected, request.toXML());
    }
}
