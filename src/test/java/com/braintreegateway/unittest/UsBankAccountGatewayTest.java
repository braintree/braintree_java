package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Configuration;
import com.braintreegateway.Environment;
import com.braintreegateway.Result;
import com.braintreegateway.Transaction;
import com.braintreegateway.TransactionGateway;
import com.braintreegateway.TransactionRequest;
import com.braintreegateway.UsBankAccount;
import com.braintreegateway.UsBankAccountGateway;
import com.braintreegateway.util.Http;
import com.braintreegateway.util.SimpleNodeWrapper;

public class UsBankAccountGatewayTest {
    private BraintreeGateway braintreeGateway;
    private Http http;
    private Configuration configuration;
    private UsBankAccountGateway gateway;
    private String merchantPath;

    @BeforeEach
    public void setup() {
        braintreeGateway = mock(BraintreeGateway.class);
        http = mock(Http.class);
        configuration = new Configuration(
                Environment.DEVELOPMENT, "integration_merchant_id", "public_key", "private_key");
        gateway = new UsBankAccountGateway(braintreeGateway, http, configuration);
        merchantPath = configuration.getMerchantPath();
    }

    @Test
    public void findReturnsUsBankAccount() {
        SimpleNodeWrapper node = SimpleNodeWrapper.parse(
                "<us-bank-account>" +
                "<token>a_token</token>" +
                "<account-holder-name>Dan Schulman</account-holder-name>" +
                "<account-type>checking</account-type>" +
                "</us-bank-account>");
        when(http.get(merchantPath + "/payment_methods/us_bank_account/a_token")).thenReturn(node);

        UsBankAccount usBankAccount = gateway.find("a_token");

        assertEquals("a_token", usBankAccount.getToken());
        assertEquals("Dan Schulman", usBankAccount.getAccountHolderName());
        assertEquals("checking", usBankAccount.getAccountType());
        verify(http).get(merchantPath + "/payment_methods/us_bank_account/a_token");
    }

    @Test
    public void saleDelegatesToTransactionGatewayWithTokenAndSubmitForSettlement() {
        TransactionGateway transactionGateway = mock(TransactionGateway.class);
        Result<Transaction> expected = new Result<Transaction>();
        when(braintreeGateway.transaction()).thenReturn(transactionGateway);
        when(transactionGateway.sale(any(TransactionRequest.class))).thenReturn(expected);

        TransactionRequest request = new TransactionRequest().amount(new java.math.BigDecimal("10.00"));
        Result<Transaction> result = gateway.sale("a_token", request);

        assertSame(expected, result);

        ArgumentCaptor<TransactionRequest> captor = ArgumentCaptor.forClass(TransactionRequest.class);
        verify(transactionGateway).sale(captor.capture());

        // The same request instance is forwarded, with the token and submit-for-settlement applied.
        assertSame(request, captor.getValue());
        String xml = captor.getValue().toXML();
        assertTrue(xml.contains("<paymentMethodToken>a_token</paymentMethodToken>"),
                "expected paymentMethodToken to be set, got: " + xml);
        assertTrue(xml.contains("<submitForSettlement>true</submitForSettlement>"),
                "expected submitForSettlement(true) to be set, got: " + xml);
    }
}
