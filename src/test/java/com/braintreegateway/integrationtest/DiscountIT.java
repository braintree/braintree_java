package com.braintreegateway.integrationtest;

import com.braintreegateway.Discount;
import com.braintreegateway.util.Http;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class DiscountIT extends IntegrationTestNew {

    private Http http;

    @BeforeEach
    public void createHttp() {
        http = new Http(this.gateway.getConfiguration());
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
        http.post(gateway.getConfiguration().getMerchantPath() + "/modifications/create_modification_for_tests", discountRequest);

        List<Discount> discounts = gateway.discount().all();
        Discount actualDiscount = null;
        for (Discount discount : discounts) {
            if (discount.getId().equals(discountId)) {
                actualDiscount = discount;
                break;
            }
        }

        assertEquals(new BigDecimal("100.00"), actualDiscount.getAmount());
        assertEquals("java test discount description", actualDiscount.getDescription());
        assertEquals("discount", actualDiscount.getKind());
        assertEquals("java test discount name", actualDiscount.getName());
        assertEquals(false, actualDiscount.neverExpires());
        assertEquals(Integer.valueOf("12"), actualDiscount.getNumberOfBillingCycles());
    }
}
