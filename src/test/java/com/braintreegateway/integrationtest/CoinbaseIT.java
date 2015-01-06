package com.braintreegateway.integrationtest;

import java.util.List;

import com.braintreegateway.*;
import com.braintreegateway.exceptions.NotFoundException;
import com.braintreegateway.SandboxValues.TransactionAmount;
import com.braintreegateway.test.*;

import org.junit.Test;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import static org.junit.Assert.*;

public class CoinbaseIT {

    private BraintreeGateway gateway;

    @Before
    public void createGateway() {
        this.gateway = new BraintreeGateway(Environment.DEVELOPMENT, "integration_merchant_id", "integration_public_key", "integration_private_key");
    }

    @Test
    public void canCreateTransaction() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            paymentMethodNonce(Nonce.Coinbase);

        Result<Transaction> authResult = gateway.transaction().sale(request);
        assertTrue(authResult.isSuccess());
        assertNotNull(authResult.getTarget().getCoinbaseDetails());
        assertNull(authResult.getTarget().getCoinbaseDetails().getToken());
    }

    @Test
    public void canVaultOnTransactionCreate() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            paymentMethodNonce(Nonce.Coinbase).
            options().
                submitForSettlement(true).
                storeInVaultOnSuccess(true).
                done();

        Result<Transaction> authResult = gateway.transaction().sale(request);
        assertTrue(authResult.isSuccess());

        Transaction transaction = authResult.getTarget();
        assertNotNull(transaction);
        CoinbaseDetails details = transaction.getCoinbaseDetails();
        assertNotNull(details);
        String token = details.getToken();
        assertNotNull(token);

        CoinbaseAccount account = gateway.coinbaseAccount().find(token);
        assertNotNull(account);
    }

    @Rule public ExpectedException exception = ExpectedException.none();

    @Test
    public void canVault() {
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        assertTrue(customerResult.isSuccess());
        Customer customer = customerResult.getTarget();

        PaymentMethodRequest paymentMethodRequest = new PaymentMethodRequest()
            .customerId(customer.getId())
            .paymentMethodNonce(Nonce.Coinbase);
        Result<? extends PaymentMethod> paymentMethodCreateResult = gateway.paymentMethod().create(paymentMethodRequest);
        assertTrue(paymentMethodCreateResult.isSuccess());
        String paymentMethodToken = paymentMethodCreateResult.getTarget().getToken();

        PaymentMethod paymentMethodFindResult = gateway.paymentMethod().find(paymentMethodToken);
        assertNotNull(paymentMethodFindResult);

        List<CoinbaseAccount> accounts = gateway.customer().find(customer.getId()).getCoinbaseAccounts();
        assertEquals(1, accounts.size());
        assertEquals(accounts.get(0).getToken(), paymentMethodToken);

        gateway.coinbaseAccount().delete(paymentMethodToken);

        exception.expect(NotFoundException.class);
        gateway.coinbaseAccount().find(paymentMethodToken);
    }
}
