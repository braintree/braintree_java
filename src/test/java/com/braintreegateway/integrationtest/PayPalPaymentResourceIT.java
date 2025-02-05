package com.braintreegateway.integrationtest;

import com.braintreegateway.testhelpers.TestHelper;

import com.braintreegateway.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PayPalPaymentResourceIT extends IntegrationTest {

    @Test
    public void updatePaymentResource() {
        String nonce = TestHelper.generateOrderPaymentPayPalNonce(gateway);

        PayPalPaymentResourceRequest request = new PayPalPaymentResourceRequest().
            amount(new BigDecimal("100.00")).
            amountBreakdown().
                discount(new BigDecimal("15.00")).
                handling(new BigDecimal("0.00")).
                insurance(new BigDecimal("5.00")).
                itemTotal(new BigDecimal("100.00")).
                shipping(new BigDecimal("10.00")).
                shippingDiscount(new BigDecimal("0.00")).
                taxTotal(new BigDecimal("10.00")).
                done().
            currencyIsoCode("USD").
            customField("0437").
            description("This is a test").
            // lineItem().
            //     description("Shoes").
            //     imageUrl().
            //     name().
            //     productCode().
            //     quantity(new BigDecimal("1.0232")).
            //     totalAmount(new BigDecimal("45.00")).
            //     unitAmount(new BigDecimal("45.00")).
            //     upcCode().
            //     upcType().
            //     url().
            //     done().
            orderId("order-123456789").
            payeeEmail("usd_merchant@example.com").
            paymentMethodNonce(nonce).
            // shipping().
            //     firstName("Andrew").
            //     lastName("Mason").
            //     company("Braintree Shipping").
            //     streetAddress("456 W Main St").
            //     extendedAddress("Apt 2F").
            //     locality("Bartlett").
            //     region("MA").
            //     postalCode("60103").
            //     countryName("Mexico").
            //     countryCodeAlpha2("MX").
            //     countryCodeAlpha3("MEX").
            //     countryCodeNumeric("484").
            //     done().
            shippingOption().
                amount(new BigDecimal("10.00")).
                id("option1").
                label("fast").
                selected(true).
                type("SHIPPING").
                done();

        Result<PaymentMethodNonce> result = gateway.paypalPaymentResource().update(request);
        ValidationError error = result.getErrors()
            .forObject("paypalPaymentResource")
            .getAllValidationErrors()
            .get(0);
        System.out.println("********************************");
        System.out.println(error.getMessage());
        System.out.println("********************************");
        assertTrue(result.isSuccess());
    }
}