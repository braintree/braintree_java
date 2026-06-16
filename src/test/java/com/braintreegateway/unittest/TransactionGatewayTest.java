package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.braintreegateway.Configuration;
import com.braintreegateway.Environment;
import com.braintreegateway.PackageTrackingRequest;
import com.braintreegateway.Request;
import com.braintreegateway.ResourceCollection;
import com.braintreegateway.Result;
import com.braintreegateway.Transaction;
import com.braintreegateway.TransactionCloneRequest;
import com.braintreegateway.TransactionGateway;
import com.braintreegateway.TransactionRefundRequest;
import com.braintreegateway.TransactionRequest;
import com.braintreegateway.TransactionSearchRequest;
import com.braintreegateway.TransactionVoidRequest;
import com.braintreegateway.exceptions.NotFoundException;
import com.braintreegateway.exceptions.UnexpectedException;
import com.braintreegateway.util.Http;
import com.braintreegateway.util.SimpleNodeWrapper;

public class TransactionGatewayTest {
    private Http http;
    private Configuration configuration;
    private TransactionGateway gateway;
    private String merchantPath;
    private SimpleNodeWrapper transactionNode;

    @BeforeEach
    public void setup() {
        http = mock(Http.class);
        configuration = new Configuration(
                Environment.DEVELOPMENT, "integration_merchant_id", "public_key", "private_key");
        gateway = new TransactionGateway(http, configuration);
        merchantPath = configuration.getMerchantPath();

        transactionNode = SimpleNodeWrapper.parse(
                "<transaction><id>transaction_id</id><type>sale</type><amount>10.00</amount></transaction>");

        when(http.get(anyString())).thenReturn(transactionNode);
        when(http.post(anyString())).thenReturn(transactionNode);
        when(http.post(anyString(), nullable(Request.class))).thenReturn(transactionNode);
        when(http.put(anyString(), nullable(Request.class))).thenReturn(transactionNode);
    }

    private void assertSuccess(Result<Transaction> result) {
        assertTrue(result.isSuccess());
        assertEquals("transaction_id", result.getTarget().getId());
    }

    @Test
    public void adjustAuthorizationWithAmount() {
        assertSuccess(gateway.adjustAuthorization("an_id", new BigDecimal("10.00")));
    }

    @Test
    public void adjustAuthorizationWithRequest() {
        assertSuccess(gateway.adjustAuthorization("an_id", new TransactionRequest().amount(new BigDecimal("10.00"))));
    }

    @Test
    public void cloneTransaction() {
        assertSuccess(gateway.cloneTransaction("an_id", new TransactionCloneRequest().amount(new BigDecimal("10.00"))));
    }

    @Test
    public void credit() {
        assertSuccess(gateway.credit(new TransactionRequest().amount(new BigDecimal("10.00"))));
    }

    @Test
    public void findReturnsTransaction() {
        assertEquals("transaction_id", gateway.find("an_id").getId());
    }

    @Test
    public void findThrowsNotFoundWhenIdIsNull() {
        assertThrows(NotFoundException.class, () -> gateway.find(null));
    }

    @Test
    public void findThrowsNotFoundWhenIdIsBlank() {
        assertThrows(NotFoundException.class, () -> gateway.find("   "));
    }

    @Test
    public void refundFull() {
        assertSuccess(gateway.refund("an_id"));
    }

    @Test
    public void refundWithAmount() {
        assertSuccess(gateway.refund("an_id", new BigDecimal("5.00")));
    }

    @Test
    public void refundWithRequest() {
        assertSuccess(gateway.refund("an_id", new TransactionRefundRequest().amount(new BigDecimal("5.00"))));
    }

    @Test
    public void sale() {
        assertSuccess(gateway.sale(new TransactionRequest().amount(new BigDecimal("10.00"))));
    }

    @Test
    public void searchReturnsResourceCollectionAndPagesResults() {
        SimpleNodeWrapper searchResults = SimpleNodeWrapper.parse(
                "<search-results><page-size type=\"integer\">50</page-size>" +
                "<ids type=\"array\"><item>transaction_id</item></ids></search-results>");
        when(http.post(eq(merchantPath + "/transactions/advanced_search_ids"), nullable(Request.class)))
                .thenReturn(searchResults);

        SimpleNodeWrapper page = SimpleNodeWrapper.parse(
                "<credit-card-transactions type=\"array\">" +
                "<transaction><id>transaction_id</id></transaction></credit-card-transactions>");
        when(http.post(eq(merchantPath + "/transactions/advanced_search"), nullable(Request.class)))
                .thenReturn(page);

        ResourceCollection<Transaction> collection = gateway.search(new TransactionSearchRequest());

        assertEquals(1, collection.getMaximumSize());
        assertEquals("transaction_id", collection.getFirst().getId());
    }

    @Test
    public void searchThrowsUnexpectedWhenNoSearchResults() {
        when(http.post(eq(merchantPath + "/transactions/advanced_search_ids"), nullable(Request.class)))
                .thenReturn(transactionNode);

        Exception e = assertThrows(UnexpectedException.class,
                () -> gateway.search(new TransactionSearchRequest()));
        assertEquals("No search results found.", e.getMessage());
    }

    @Test
    public void fetchTransactionsThrowsUnexpectedWhenNoCreditCardTransactions() {
        SimpleNodeWrapper searchResults = SimpleNodeWrapper.parse(
                "<search-results><page-size type=\"integer\">50</page-size>" +
                "<ids type=\"array\"><item>transaction_id</item></ids></search-results>");
        when(http.post(eq(merchantPath + "/transactions/advanced_search_ids"), nullable(Request.class)))
                .thenReturn(searchResults);
        when(http.post(eq(merchantPath + "/transactions/advanced_search"), nullable(Request.class)))
                .thenReturn(transactionNode);

        ResourceCollection<Transaction> collection = gateway.search(new TransactionSearchRequest());

        Exception e = assertThrows(UnexpectedException.class, () -> collection.getFirst());
        assertEquals("No credit card transaction results found.", e.getMessage());
    }

    @Test
    public void cancelRelease() {
        assertSuccess(gateway.cancelRelease("an_id"));
    }

    @Test
    public void releaseFromEscrow() {
        assertSuccess(gateway.releaseFromEscrow("an_id"));
    }

    @Test
    public void submitForSettlementById() {
        assertSuccess(gateway.submitForSettlement("an_id"));
    }

    @Test
    public void submitForSettlementWithAmount() {
        assertSuccess(gateway.submitForSettlement("an_id", new BigDecimal("10.00")));
    }

    @Test
    public void submitForSettlementWithRequest() {
        assertSuccess(gateway.submitForSettlement("an_id", new TransactionRequest().amount(new BigDecimal("10.00"))));
    }

    @Test
    public void updateDetails() {
        assertSuccess(gateway.updateDetails("an_id", new TransactionRequest().amount(new BigDecimal("10.00"))));
    }

    @Test
    public void voidTransactionById() {
        assertSuccess(gateway.voidTransaction("an_id"));
    }

    @Test
    public void voidTransactionWithRequest() {
        assertSuccess(gateway.voidTransaction("an_id", new TransactionVoidRequest()));
    }

    @Test
    public void submitForPartialSettlementWithAmount() {
        assertSuccess(gateway.submitForPartialSettlement("an_id", new BigDecimal("5.00")));
    }

    @Test
    public void submitForPartialSettlementWithRequest() {
        assertSuccess(gateway.submitForPartialSettlement("an_id", new TransactionRequest().amount(new BigDecimal("5.00"))));
    }

    @Test
    public void updateCustomFields() {
        assertSuccess(gateway.updateCustomFields("an_id", new TransactionRequest()));
    }

    @Test
    public void packageTracking() {
        assertSuccess(gateway.packageTracking("an_id", new PackageTrackingRequest()));
    }
}
