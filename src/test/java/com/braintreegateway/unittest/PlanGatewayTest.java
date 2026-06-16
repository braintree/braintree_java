package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.braintreegateway.Configuration;
import com.braintreegateway.Environment;
import com.braintreegateway.Plan;
import com.braintreegateway.PlanGateway;
import com.braintreegateway.PlanRequest;
import com.braintreegateway.Request;
import com.braintreegateway.Result;
import com.braintreegateway.exceptions.NotFoundException;
import com.braintreegateway.util.Http;
import com.braintreegateway.util.SimpleNodeWrapper;

public class PlanGatewayTest {
    private Http http;
    private Configuration configuration;
    private PlanGateway gateway;
    private String merchantPath;
    private SimpleNodeWrapper planNode;

    @BeforeEach
    public void setup() {
        http = mock(Http.class);
        configuration = new Configuration(
                Environment.DEVELOPMENT, "integration_merchant_id", "public_key", "private_key");
        gateway = new PlanGateway(http, configuration);
        merchantPath = configuration.getMerchantPath();

        planNode = SimpleNodeWrapper.parse("<plan><id>plan_id</id></plan>");

        when(http.get(anyString())).thenReturn(planNode);
        when(http.post(anyString(), nullable(Request.class))).thenReturn(planNode);
        when(http.put(anyString(), nullable(Request.class))).thenReturn(planNode);
    }

    @Test
    public void allReturnsPlans() {
        SimpleNodeWrapper plans = SimpleNodeWrapper.parse(
                "<plans type=\"array\">" +
                "<plan><id>plan_one</id></plan>" +
                "<plan><id>plan_two</id></plan>" +
                "</plans>");
        when(http.get(merchantPath + "/plans")).thenReturn(plans);

        List<Plan> result = gateway.all();

        assertEquals(2, result.size());
        assertEquals("plan_one", result.get(0).getId());
        assertEquals("plan_two", result.get(1).getId());
    }

    @Test
    public void allReturnsEmptyListWhenNoPlans() {
        when(http.get(merchantPath + "/plans")).thenReturn(SimpleNodeWrapper.parse("<plans type=\"array\"></plans>"));

        assertTrue(gateway.all().isEmpty());
    }

    @Test
    public void create() {
        Result<Plan> result = gateway.create(new PlanRequest().name("a-plan").billingFrequency(1));

        assertTrue(result.isSuccess());
        assertEquals("plan_id", result.getTarget().getId());
    }

    @Test
    public void findReturnsPlan() {
        when(http.get(merchantPath + "/plans/plan_id")).thenReturn(planNode);

        assertEquals("plan_id", gateway.find("plan_id").getId());
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
        Result<Plan> result = gateway.update("plan_id", new PlanRequest().price(new BigDecimal("10.00")));

        assertTrue(result.isSuccess());
        assertEquals("plan_id", result.getTarget().getId());
    }
}
