package com.braintreegateway.integrationtest;

import java.util.List;

import com.braintreegateway.*;
import com.braintreegateway.exceptions.NotFoundException;
import com.braintreegateway.SandboxValues.TransactionAmount;
import com.braintreegateway.test.*;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class CoinbaseIT extends IntegrationTest{

    @Test
    public void canCreateTransaction_noLongerSupported() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            paymentMethodNonce(Nonce.Coinbase);

        Result<Transaction> authResult = gateway.transaction().sale(request);
        assertFalse(authResult.isSuccess());
        assertEquals(ValidationErrorCode.PAYMENT_METHOD_NO_LONGER_SUPPORTED,
                authResult.getErrors().forObject("transaction").onField("base").get(0).getCode());
    }

    @Test
    public void canVaultOnTransactionCreate_noLongerSupported() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            paymentMethodNonce(Nonce.Coinbase).
            options().
                submitForSettlement(true).
                storeInVaultOnSuccess(true).
                done();

        Result<Transaction> authResult = gateway.transaction().sale(request);
        assertFalse(authResult.isSuccess());
        assertEquals(ValidationErrorCode.PAYMENT_METHOD_NO_LONGER_SUPPORTED,
                authResult.getErrors().forObject("transaction").onField("base").get(0).getCode());
    }

    @Test
    public void canVault_noLongerSupported() {
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        String customerId = customerResult.getTarget().getId();

        Result<? extends PaymentMethod> paymentMethodResult = gateway.paymentMethod().create(new PaymentMethodRequest().customerId(customerId).paymentMethodNonce(Nonce.Coinbase));
        assertFalse(paymentMethodResult.isSuccess());
        assertEquals(ValidationErrorCode.PAYMENT_METHOD_NO_LONGER_SUPPORTED,
                paymentMethodResult.getErrors().forObject("coinbaseAccount").onField("base").get(0).getCode());
    }

    @Test
    public void onCustomer_noLongerSupported() {
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest().paymentMethodNonce(Nonce.Coinbase));
        assertFalse(customerResult.isSuccess());
        assertEquals(ValidationErrorCode.PAYMENT_METHOD_NO_LONGER_SUPPORTED,
                customerResult.getErrors().forObject("coinbaseAccount").onField("base").get(0).getCode());
    }

    @Test
    public void updateUpdatesCoinbaseAccount_noLongerSupported() {
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        String customerId = customerResult.getTarget().getId();

        PaymentMethodRequest request = new PaymentMethodRequest().customerId(customerId).paymentMethodNonce(Nonce.Coinbase);
        Result<? extends PaymentMethod> paymentMethodResult = gateway.paymentMethod().create(request);
        assertFalse(paymentMethodResult.isSuccess());
        assertEquals(ValidationErrorCode.PAYMENT_METHOD_NO_LONGER_SUPPORTED,
                paymentMethodResult.getErrors().forObject("coinbaseAccount").onField("base").get(0).getCode());
    }

}
