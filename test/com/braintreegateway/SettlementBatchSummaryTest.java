package com.braintreegateway;

import java.util.Calendar;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.braintreegateway.SandboxValues.CreditCardNumber;
import com.braintreegateway.SandboxValues.TransactionAmount;

public class SettlementBatchSummaryTest {
    
    private BraintreeGateway gateway;

    @Before
    public void createGateway() {
        this.gateway = new BraintreeGateway(Environment.DEVELOPMENT, "integration_merchant_id", "integration_public_key", "integration_private_key");
    }
    
    @Test
    public void returnEmptyCollectionIfThereIsNoData() {
        Calendar settlementDate = Calendar.getInstance();
        settlementDate.add(Calendar.YEAR, -5);

        Result<SettlementBatchSummary> result = gateway.settlementBatchSummary().generate(settlementDate);
        Assert.assertTrue(result.isSuccess());
    }
    
    @Test
    public void returnsDataForTheGivenSettlementDate() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            creditCard().
                number(CreditCardNumber.VISA.number).
                cvv("321").
                expirationDate("05/2009").
                done().
            options().
               submitForSettlement(true).
               done();

        Result<Transaction> result = gateway.transaction().sale(request);
        Assert.assertTrue(result.isSuccess());
        
        TestHelper.settle(gateway, result.getTarget().getId());
        
        Result<SettlementBatchSummary> summaryResult = gateway.settlementBatchSummary().generate(Calendar.getInstance());
        Assert.assertTrue(summaryResult.isSuccess());
        
        Assert.assertTrue(summaryResult.getTarget().getRecords().size() > 0);
        
        Map<String, String> first = summaryResult.getTarget().getRecords().get(0);
        Assert.assertTrue(first.containsKey("kind"));
        Assert.assertTrue(first.containsKey("count"));
        Assert.assertTrue(first.containsKey("amount_settled"));
        Assert.assertTrue(first.containsKey("merchant_account_id"));
    }
    
    @Test
    public void returnsDataGroupedByTheGivenCustomField() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            creditCard().
                number(CreditCardNumber.VISA.number).
                cvv("321").
                expirationDate("05/2009").
                done().
            customField("store_me", "1").
            options().
               submitForSettlement(true).
               done();
    
        Result<Transaction> result = gateway.transaction().sale(request);
        Assert.assertTrue(result.isSuccess());
        
        TestHelper.settle(gateway, result.getTarget().getId());
        
        Result<SettlementBatchSummary> summaryResult = gateway.settlementBatchSummary().generate(Calendar.getInstance(), "store_me");
        Assert.assertTrue(summaryResult.isSuccess());
        
        Assert.assertTrue(summaryResult.getTarget().getRecords().size() > 0);
        
        Map<String, String> first = summaryResult.getTarget().getRecords().get(0);
        Assert.assertTrue(first.containsKey("store_me"));
    }
}
