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

        TransactionLineItemRequest lineItem = new TransactionLineItemRequest().
            description("Shoes").
            imageUrl("https://example.com/products/23434/pic.png").
            kind(TransactionLineItem.Kind.DEBIT).
            name("Name #1").
            productCode("23434").
            quantity(new BigDecimal("1")).
            totalAmount(new BigDecimal("45.00")).
            unitAmount(new BigDecimal("45.00")).
            unitTaxAmount(new BigDecimal("10.00")).
            upcCode("3878935708DA").
            upcType("UPC-A").
            url("https://example.com/products/23434");

        PayPalPaymentResourceRequest request = new PayPalPaymentResourceRequest().
            amount(new BigDecimal("100.00")).
            amountBreakdown().
                discount(new BigDecimal("15.00")).
                handling(new BigDecimal("0.00")).
                insurance(new BigDecimal("5.00")).
                itemTotal(new BigDecimal("45.00")).
                shipping(new BigDecimal("10.00")).
                shippingDiscount(new BigDecimal("0.00")).
                taxTotal(new BigDecimal("10.00")).
                done().
            currencyIsoCode("USD").
            customField("0437").
            description("This is a test").
            addLineItem(lineItem).
            orderId("order-123456789").
            payeeEmail("bt_buyer_us@paypal.com").
            paymentMethodNonce(nonce).
            shipping().
                firstName("John").
                lastName("Doe").
                streetAddress("123 Division Street").
                extendedAddress("Apt. #1").
                locality("Chicago").
                region("IL").
                postalCode("60618").
                countryName("United States").
                countryCodeAlpha2("US").
                countryCodeAlpha3("USA").
                countryCodeNumeric("484").
                internationalPhone().
                countryCode("1").
                nationalNumber("4081111111").
                done().
                done().
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