package com.braintreegateway.integrationtest;

import com.braintreegateway.*;
import com.braintreegateway.util.Http;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

public class AddOnIT {
    private BraintreeGateway gateway;
    private Http http;

    @Before
    public void createGateway() {
        this.gateway = new BraintreeGateway(Environment.DEVELOPMENT, "integration_merchant_id", "integration_public_key", "integration_private_key");
        http = new Http(gateway.getAuthorizationHeader(), gateway.baseMerchantURL(), Environment.DEVELOPMENT.certificateFilenames, BraintreeGateway.VERSION);
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

        Assert.assertEquals(new BigDecimal("100.00"), actualAddOn.getAmount());
        Assert.assertEquals("java test add-on description", actualAddOn.getDescription());
        Assert.assertEquals("add_on", actualAddOn.getKind());
        Assert.assertEquals("java test add-on name", actualAddOn.getName());
        Assert.assertEquals(false, actualAddOn.neverExpires());
        Assert.assertEquals(new Integer("12"), actualAddOn.getNumberOfBillingCycles());
    }
}
