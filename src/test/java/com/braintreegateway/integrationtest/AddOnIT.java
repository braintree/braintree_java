package com.braintreegateway.integrationtest;

import com.braintreegateway.AddOn;
import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Environment;
import com.braintreegateway.util.Http;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class AddOnIT {
    private BraintreeGateway gateway;
    private Http http;

    @Before
    public void createGateway() {
        this.gateway = new BraintreeGateway(Environment.DEVELOPMENT, "integration_merchant_id", "integration_public_key", "integration_private_key");
        http = new Http(gateway.getConfiguration());
    }

    @Test
    public void returnsAllAddOns() {
        String addOnId = "an_add_on_id" + String.valueOf(new Random().nextInt());
        FakeModificationRequest addOnRequest = new FakeModificationRequest()
                .amount(new BigDecimal("100.00"))
                .description("java test add-on description")
                .id(addOnId)
                .kind("add_on")
                .name("java test add-on name")
                .neverExpires(false)
                .numberOfBillingCycles(12);
        http.post("/modifications/create_modification_for_tests", addOnRequest);

        List<AddOn> addOns = gateway.addOn().all();
        AddOn actualAddOn = null;
        for (AddOn addOn : addOns) {
            if (addOn.getId().equals(addOnId)) {
                actualAddOn = addOn;
                break;
            }
        }

        assertEquals(new BigDecimal("100.00"), actualAddOn.getAmount());
        assertEquals("java test add-on description", actualAddOn.getDescription());
        assertEquals("add_on", actualAddOn.getKind());
        assertEquals("java test add-on name", actualAddOn.getName());
        assertEquals(false, actualAddOn.neverExpires());
        assertEquals(new Integer("12"), actualAddOn.getNumberOfBillingCycles());
    }
}
