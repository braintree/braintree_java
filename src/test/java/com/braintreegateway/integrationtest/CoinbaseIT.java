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
import static org.hamcrest.CoreMatchers.*;

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

        CoinbaseDetails details = authResult.getTarget().getCoinbaseDetails();
        assertNotNull(details);
        assertNull(details.getToken());
        assertNotNull(details.getUserId());
        assertThat(details.getUserId(), not(equalTo("")));
        assertNotNull(details.getUserName());
        assertThat(details.getUserName(), not(equalTo("")));
        assertNotNull(details.getUserEmail());
        assertThat(details.getUserEmail(), not(equalTo("")));
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

        PaymentMethod account = gateway.paymentMethod().find(token);
        assertTrue(account instanceof CoinbaseAccount);
        assertNotNull(account);
    }

    @Test
    public void canVault() {
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        String customerId = customerResult.getTarget().getId();

        Result<? extends PaymentMethod> paymentMethodResult = gateway.paymentMethod().create(new PaymentMethodRequest().customerId(customerId).paymentMethodNonce(Nonce.Coinbase));
        assertTrue(paymentMethodResult.isSuccess());

        CoinbaseAccount account = (CoinbaseAccount) paymentMethodResult.getTarget();
        assertNotNull(account);
        assertNotNull(account.getToken());
        assertNotNull(account.getUserId());
        assertThat(account.getUserId(), not(equalTo("")));
        assertNotNull(account.getUserName());
        assertThat(account.getUserName(), not(equalTo("")));
        assertNotNull(account.getUserEmail());
        assertThat(account.getUserEmail(), not(equalTo("")));
    }

    @Rule public ExpectedException exception = ExpectedException.none();

    @Test
    public void onCustomer() {
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest().paymentMethodNonce(Nonce.Coinbase));
        assertTrue(customerResult.isSuccess());
        Customer customer = customerResult.getTarget();

        List<CoinbaseAccount> accounts = gateway.customer().find(customer.getId()).getCoinbaseAccounts();
        assertEquals(1, accounts.size());

        CoinbaseAccount account = accounts.get(0);
        assertNotNull(account);
        assertNotNull(account.getToken());
        assertNotNull(account.getUserId());
        assertThat(account.getUserId(), not(equalTo("")));
        assertNotNull(account.getUserName());
        assertThat(account.getUserName(), not(equalTo("")));
        assertNotNull(account.getUserEmail());
        assertThat(account.getUserEmail(), not(equalTo("")));

        String token = account.getToken();

        gateway.paymentMethod().delete(token);

        exception.expect(NotFoundException.class);
        gateway.paymentMethod().find(token);
    }
}
