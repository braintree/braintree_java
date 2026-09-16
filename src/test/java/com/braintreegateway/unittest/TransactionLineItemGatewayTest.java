package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.braintreegateway.Configuration;
import com.braintreegateway.Environment;
import com.braintreegateway.TransactionLineItem;
import com.braintreegateway.TransactionLineItemGateway;
import com.braintreegateway.exceptions.NotFoundException;
import com.braintreegateway.exceptions.UnexpectedException;
import com.braintreegateway.util.Http;
import com.braintreegateway.util.SimpleNodeWrapper;

public class TransactionLineItemGatewayTest {
    private Http http;
    private Configuration configuration;
    private TransactionLineItemGateway gateway;
    private String merchantPath;

    @BeforeEach
    public void setup() {
        http = mock(Http.class);
        configuration = new Configuration(
                Environment.DEVELOPMENT, "integration_merchant_id", "public_key", "private_key");
        gateway = new TransactionLineItemGateway(http, configuration);
        merchantPath = configuration.getMerchantPath();
    }

    @Test
    public void findAllThrowsNotFoundWhenTransactionIdIsNull() {
        assertThrows(NotFoundException.class, () -> gateway.findAll(null));
    }

    @Test
    public void findAllThrowsNotFoundWhenTransactionIdIsEmpty() {
        assertThrows(NotFoundException.class, () -> gateway.findAll(""));
    }

    @Test
    public void findAllThrowsNotFoundWhenTransactionIdIsBlank() {
        assertThrows(NotFoundException.class, () -> gateway.findAll("   "));
    }

    @Test
    public void findAllReturnsLineItems() {
        SimpleNodeWrapper node = SimpleNodeWrapper.parse(
                "<line-items type=\"array\">" +
                "<line-item><name>Item One</name><quantity>1</quantity></line-item>" +
                "<line-item><name>Item Two</name><quantity>2</quantity></line-item>" +
                "</line-items>");
        when(http.get(merchantPath + "/transactions/a_transaction_id/line_items")).thenReturn(node);

        List<TransactionLineItem> lineItems = gateway.findAll("a_transaction_id");

        assertEquals(2, lineItems.size());
        assertEquals("Item One", lineItems.get(0).getName());
        assertEquals("Item Two", lineItems.get(1).getName());
        verify(http).get(merchantPath + "/transactions/a_transaction_id/line_items");
    }

    @Test
    public void findAllReturnsEmptyListWhenNoLineItems() {
        SimpleNodeWrapper node = SimpleNodeWrapper.parse("<line-items type=\"array\"></line-items>");
        when(http.get(merchantPath + "/transactions/a_transaction_id/line_items")).thenReturn(node);

        List<TransactionLineItem> lineItems = gateway.findAll("a_transaction_id");

        assertTrue(lineItems.isEmpty());
    }

    @Test
    public void findAllThrowsUnexpectedWhenResponseIsNotLineItems() {
        SimpleNodeWrapper node = SimpleNodeWrapper.parse("<api-error-response></api-error-response>");
        when(http.get(merchantPath + "/transactions/a_transaction_id/line_items")).thenReturn(node);

        Exception e = assertThrows(UnexpectedException.class, () -> gateway.findAll("a_transaction_id"));
        assertEquals("No line items found.", e.getMessage());
    }

    @Test
    public void findAllThrowsNotFoundForTraversalId() {
        assertThrows(NotFoundException.class, () -> gateway.findAll("../../foo"));
    }

}
