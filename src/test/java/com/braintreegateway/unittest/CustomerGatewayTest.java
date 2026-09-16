package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.braintreegateway.Configuration;
import com.braintreegateway.Customer;
import com.braintreegateway.CustomerGateway;
import com.braintreegateway.CustomerRequest;
import com.braintreegateway.CustomerSearchRequest;
import com.braintreegateway.Environment;
import com.braintreegateway.Request;
import com.braintreegateway.ResourceCollection;
import com.braintreegateway.Result;
import com.braintreegateway.ValidationError;
import com.braintreegateway.exceptions.NotFoundException;
import com.braintreegateway.util.Http;
import com.braintreegateway.util.SimpleNodeWrapper;

public class CustomerGatewayTest {
    private Http http;
    private Configuration configuration;
    private CustomerGateway gateway;
    private String merchantPath;
    private SimpleNodeWrapper customerNode;
    private SimpleNodeWrapper searchResultsNode;
    private SimpleNodeWrapper pageNode;

    @BeforeEach
    public void setup() {
        http = mock(Http.class);
        configuration = new Configuration(
                Environment.DEVELOPMENT, "integration_merchant_id", "public_key", "private_key");
        gateway = new CustomerGateway(http, configuration);
        merchantPath = configuration.getMerchantPath();

        customerNode = SimpleNodeWrapper.parse("<customer><id>customer_id</id></customer>");
        searchResultsNode = SimpleNodeWrapper.parse(
                "<search-results><page-size type=\"integer\">50</page-size>" +
                "<ids type=\"array\"><item>customer_id</item></ids></search-results>");
        pageNode = SimpleNodeWrapper.parse(
                "<customers type=\"array\"><customer><id>customer_id</id></customer></customers>");

        when(http.get(anyString())).thenReturn(customerNode);
        when(http.post(anyString(), nullable(Request.class))).thenReturn(customerNode);
        when(http.put(anyString(), nullable(Request.class))).thenReturn(customerNode);
        when(http.post(eq(merchantPath + "/customers/advanced_search"), nullable(Request.class))).thenReturn(pageNode);
    }

    @Test
    public void allReturnsResourceCollectionAndPagesResults() {
        when(http.post(merchantPath + "/customers/advanced_search_ids")).thenReturn(searchResultsNode);

        ResourceCollection<Customer> collection = gateway.all();

        assertEquals(1, collection.getMaximumSize());
        assertEquals("customer_id", collection.getFirst().getId());
    }

    @Test
    public void searchReturnsResourceCollection() {
        when(http.post(eq(merchantPath + "/customers/advanced_search_ids"), nullable(Request.class)))
                .thenReturn(searchResultsNode);

        ResourceCollection<Customer> collection = gateway.search(new CustomerSearchRequest());

        assertEquals(1, collection.getMaximumSize());
        assertEquals("customer_id", collection.getFirst().getId());
    }

    @Test
    public void create() {
        Result<Customer> result = gateway.create(new CustomerRequest().firstName("Dan"));

        assertTrue(result.isSuccess());
        assertEquals("customer_id", result.getTarget().getId());
        verify(http).post(eq(merchantPath + "/customers"), nullable(Request.class));
    }

    @Test
    public void createReturnsValidationErrors() {
        SimpleNodeWrapper errorNode = SimpleNodeWrapper.parse(
                "<api-error-response>" +
                "<errors>" +
                "<errors type=\"array\"/>" +
                "<customer>" +
                "<errors type=\"array\">" +
                "<error>" +
                "<code>81608</code>" +
                "<attribute type=\"symbol\">first_name</attribute>" +
                "<message>First name is too long.</message>" +
                "</error>" +
                "</errors>" +
                "</customer>" +
                "</errors>" +
                "<params>" +
                "<controller>customers</controller>" +
                "<action>create</action>" +
                "</params>" +
                "<message>First name is too long.</message>" +
                "</api-error-response>");
        when(http.post(eq(merchantPath + "/customers"), nullable(Request.class))).thenReturn(errorNode);

        Result<Customer> result = gateway.create(new CustomerRequest().firstName("a-name-that-is-too-long"));

        assertFalse(result.isSuccess());
        List<ValidationError> errors = result.getErrors().getAllDeepValidationErrors();
        assertEquals(1, errors.size());
        assertEquals("First name is too long.", errors.get(0).getMessage());
    }

    @Test
    public void delete() {
        gateway.delete("a_customer_id");

        verify(http).delete(merchantPath + "/customers/a_customer_id");
    }

    @Test
    public void findReturnsCustomer() {
        when(http.get(merchantPath + "/customers/customer_id")).thenReturn(customerNode);

        assertEquals("customer_id", gateway.find("customer_id").getId());
        verify(http).get(merchantPath + "/customers/customer_id");
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
    public void findWithAssociationFilterReturnsCustomer() {
        when(http.get(merchantPath + "/customers/customer_id?association_filter_id=a_filter"))
                .thenReturn(customerNode);

        assertEquals("customer_id", gateway.find("customer_id", "a_filter").getId());
        verify(http).get(merchantPath + "/customers/customer_id?association_filter_id=a_filter");
    }

    @Test
    public void findWithAssociationFilterThrowsNotFoundWhenIdIsNull() {
        assertThrows(NotFoundException.class, () -> gateway.find(null, "a_filter"));
    }

    @Test
    public void findWithAssociationFilterThrowsNotFoundWhenIdIsBlank() {
        assertThrows(NotFoundException.class, () -> gateway.find("   ", "a_filter"));
    }

    @Test
    public void findWithAssociationFilterThrowsNotFoundWhenFilterIsNull() {
        assertThrows(NotFoundException.class, () -> gateway.find("customer_id", null));
    }

    @Test
    public void findWithAssociationFilterThrowsNotFoundWhenFilterIsEmpty() {
        assertThrows(NotFoundException.class, () -> gateway.find("customer_id", ""));
    }

    @Test
    public void findWithAssociationFilterThrowsNotFoundWhenFilterContainsInvalidCharacters() {
        assertThrows(NotFoundException.class, () -> gateway.find("customer_id", "a_filter&evil=1"));
    }

    @Test
    public void update() {
        Result<Customer> result = gateway.update("customer_id", new CustomerRequest().firstName("Dan"));

        assertTrue(result.isSuccess());
        assertEquals("customer_id", result.getTarget().getId());
        verify(http).put(eq(merchantPath + "/customers/customer_id"), nullable(Request.class));
    }

    @Test
    public void deleteThrowsNotFoundForTraversalId() {
        assertThrows(NotFoundException.class, () -> gateway.delete("../transactions/a_transaction_id/void"));
    }

    @Test
    public void findThrowsNotFoundForTraversalId() {
        assertThrows(NotFoundException.class, () -> gateway.find("../transactions/a_transaction_id/void"));
    }

    @Test
    public void updateThrowsNotFoundForTraversalId() {
        assertThrows(NotFoundException.class, () ->
                gateway.update("../transactions/a_transaction_id/void", new CustomerRequest().firstName("HackerOne")));
    }
}
