package com.braintreegateway;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;

public class TransactionServiceFeeRequestTest {
    @Test
    public void toXMLIncludesMerchantAccountIdAndAmount() {
        String expected = "<serviceFee><amount>1.00</amount><merchantAccountId>abcdef</merchantAccountId></serviceFee>";
        TransactionServiceFeeRequest request = new TransactionServiceFeeRequest(null).
    		amount(new BigDecimal("1.00")).
    		merchantAccountId("abcdef");
        
        Assert.assertEquals(expected, request.toXML());
    }
} 