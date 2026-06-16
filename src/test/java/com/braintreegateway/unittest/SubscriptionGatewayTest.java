package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.braintreegateway.Configuration;
import com.braintreegateway.Environment;
import com.braintreegateway.Request;
import com.braintreegateway.ResourceCollection;
import com.braintreegateway.Result;
import com.braintreegateway.Subscription;
import com.braintreegateway.SubscriptionGateway;
import com.braintreegateway.SubscriptionRequest;
import com.braintreegateway.SubscriptionSearchRequest;
import com.braintreegateway.Transaction;
import com.braintreegateway.exceptions.NotFoundException;
import com.braintreegateway.util.Http;
import com.braintreegateway.util.SimpleNodeWrapper;

public class SubscriptionGatewayTest {
    private Http http;
    private Configuration configuration;
    private SubscriptionGateway gateway;
    private String merchantPath;
    private SimpleNodeWrapper subscriptionNode;
    private SimpleNodeWrapper transactionNode;

    @BeforeEach
    public void setup() {
        http = mock(Http.class);
        configuration = new Configuration(
                Environment.DEVELOPMENT, "integration_merchant_id", "public_key", "private_key");
        gateway = new SubscriptionGateway(http, configuration);
        merchantPath = configuration.getMerchantPath();

        subscriptionNode = SimpleNodeWrapper.parse("<subscription><id>subscription_id</id></subscription>");
        transactionNode = SimpleNodeWrapper.parse("<transaction><id>transaction_id</id></transaction>");

        when(http.get(anyString())).thenReturn(subscriptionNode);
        when(http.put(anyString())).thenReturn(subscriptionNode);
        when(http.put(anyString(), nullable(Request.class))).thenReturn(subscriptionNode);
        when(http.post(anyString(), nullable(Request.class))).thenReturn(subscriptionNode);
        // retryCharge posts to /transactions, so return a transaction there
        when(http.post(eq(merchantPath + "/transactions"), nullable(Request.class))).thenReturn(transactionNode);
    }

    private void assertSubscription(Result<Subscription> result) {
        assertTrue(result.isSuccess());
        assertEquals("subscription_id", result.getTarget().getId());
    }

    private void assertTransaction(Result<Transaction> result) {
        assertTrue(result.isSuccess());
        assertEquals("transaction_id", result.getTarget().getId());
    }

    @Test
    public void cancel() {
        assertSubscription(gateway.cancel("a_subscription_id"));
        verify(http).put(merchantPath + "/subscriptions/a_subscription_id/cancel");
    }

    @Test
    public void create() {
        assertSubscription(gateway.create(new SubscriptionRequest().planId("a_plan_id")));
    }

    @Test
    public void delete() {
        Result<Subscription> result = gateway.delete("a_customer_id", "a_subscription_id");

        assertNotNull(result);
        verify(http).delete(merchantPath + "/subscriptions/a_subscription_id");
    }

    @Test
    public void findReturnsSubscription() {
        assertEquals("subscription_id", gateway.find("a_subscription_id").getId());
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
    public void update() {
        assertSubscription(gateway.update("a_subscription_id", new SubscriptionRequest().price(new BigDecimal("10.00"))));
    }

    @Test
    public void searchReturnsResourceCollectionAndPagesResults() {
        SimpleNodeWrapper searchResults = SimpleNodeWrapper.parse(
                "<search-results><page-size type=\"integer\">50</page-size>" +
                "<ids type=\"array\"><item>subscription_id</item></ids></search-results>");
        when(http.post(eq(merchantPath + "/subscriptions/advanced_search_ids"), nullable(Request.class)))
                .thenReturn(searchResults);

        SimpleNodeWrapper page = SimpleNodeWrapper.parse(
                "<subscriptions type=\"array\">" +
                "<subscription><id>subscription_id</id></subscription></subscriptions>");
        when(http.post(eq(merchantPath + "/subscriptions/advanced_search"), nullable(Request.class)))
                .thenReturn(page);

        ResourceCollection<Subscription> collection = gateway.search(new SubscriptionSearchRequest());

        assertEquals(1, collection.getMaximumSize());
        assertEquals("subscription_id", collection.getFirst().getId());
    }

    @Test
    public void retryChargeById() {
        assertTransaction(gateway.retryCharge("a_subscription_id"));
    }

    @Test
    public void retryChargeWithAmount() {
        assertTransaction(gateway.retryCharge("a_subscription_id", new BigDecimal("10.00")));
    }

    @Test
    public void retryChargeWithSubmitForSettlement() {
        assertTransaction(gateway.retryCharge("a_subscription_id", true));
    }

    @Test
    public void retryChargeWithAmountAndSubmitForSettlement() {
        assertTransaction(gateway.retryCharge("a_subscription_id", new BigDecimal("10.00"), true));
    }
}
