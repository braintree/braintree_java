package com.braintreegateway;

import com.braintreegateway.util.Http;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

public class DiscountTest {
    private BraintreeGateway gateway;
    private Http http;

    @Before
    public void createGateway() {
        this.gateway = new BraintreeGateway(Environment.DEVELOPMENT, "integration_merchant_id", "integration_public_key", "integration_private_key");
        http = new Http(gateway.getAuthorizationHeader(), gateway.baseMerchantURL(), Environment.DEVELOPMENT.certificateFilenames, BraintreeGateway.VERSION);
    }

    @Test
    public void returnsAllDiscounts() {
        String discountId = "a_discount_id" + String.valueOf(new Random().nextInt());
        FakeModificationRequest discountRequest = new FakeModificationRequest()
                .amount(new BigDecimal("100.00"))
                .description("java test discount description")
                .id(discountId)
                .kind("discount")
                .name("java test discount name")
                .neverExpires(false)
                .numberOfBillingCycles(12);
        http.post("/modifications/create_modification_for_tests", discountRequest);

        List<Discount> discounts = gateway.discount().all();
        Discount actualDiscount = null;
        for (Discount discount : discounts) {
            if (discount.getId().equals(discountId)) {
                actualDiscount = discount;
                break;
            }
        }

        Assert.assertEquals(new BigDecimal("100.00"), actualDiscount.getAmount());
        Assert.assertEquals("java test discount description", actualDiscount.getDescription());
        Assert.assertEquals("discount", actualDiscount.getKind());
        Assert.assertEquals("java test discount name", actualDiscount.getName());
        Assert.assertEquals(false, actualDiscount.neverExpires());
        Assert.assertEquals(new Integer("12"), actualDiscount.getNumberOfBillingCycles());
    }
}
