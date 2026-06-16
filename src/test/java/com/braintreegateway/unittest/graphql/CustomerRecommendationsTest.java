package com.braintreegateway.unittest.graphql;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.braintreegateway.graphql.enums.RecommendedPaymentOption;
import com.braintreegateway.graphql.types.PaymentOptions;
import com.braintreegateway.graphql.types.PaymentRecommendation;
import com.braintreegateway.graphql.unions.CustomerRecommendations;

public class CustomerRecommendationsTest {

    @Test
    public void constructorFromPaymentRecommendationsDerivesBothLists() {
        List<PaymentRecommendation> recommendations = Arrays.asList(
                new PaymentRecommendation(RecommendedPaymentOption.PAYPAL, 1),
                new PaymentRecommendation(RecommendedPaymentOption.VENMO, 2));

        CustomerRecommendations cr = new CustomerRecommendations(recommendations);

        assertAll("from payment recommendations",
                () -> assertEquals(2, cr.getPaymentRecommendations().size()),
                () -> assertEquals(RecommendedPaymentOption.PAYPAL,
                        cr.getPaymentRecommendations().get(0).getPaymentOption()),
                () -> assertEquals(2, cr.getPaymentOptions().size()),
                () -> assertEquals(RecommendedPaymentOption.PAYPAL,
                        cr.getPaymentOptions().get(0).getPaymentOption()),
                () -> assertEquals(1,
                        cr.getPaymentOptions().get(0).getRecommendedPriority()));
    }

    @Test
    public void fromPaymentOptionsSetsPaymentOptionsDirectly() {
        List<PaymentOptions> options = Arrays.asList(
                new PaymentOptions(RecommendedPaymentOption.VENMO, 1));

        CustomerRecommendations cr = CustomerRecommendations.fromPaymentOptions(options);

        assertAll("from payment options",
                () -> assertEquals(1, cr.getPaymentOptions().size()),
                () -> assertEquals(RecommendedPaymentOption.VENMO,
                        cr.getPaymentOptions().get(0).getPaymentOption()),
                () -> assertTrue(cr.getPaymentRecommendations().isEmpty()));
    }

    @Test
    public void fromPaymentOptionsWithNullUsesEmptyList() {
        CustomerRecommendations cr = CustomerRecommendations.fromPaymentOptions(null);

        assertTrue(cr.getPaymentOptions().isEmpty());
    }

    @Test
    public void noArgConstructorInitializesEmptyLists() {
        CustomerRecommendations cr = new CustomerRecommendations();

        assertAll("no-arg constructor",
                () -> assertTrue(cr.getPaymentRecommendations().isEmpty()),
                () -> assertTrue(cr.getPaymentOptions().isEmpty()));
    }

    @Test
    public void constructorWithEmptyListProducesEmptyLists() {
        CustomerRecommendations cr = new CustomerRecommendations(Collections.emptyList());

        assertAll("empty list constructor",
                () -> assertTrue(cr.getPaymentRecommendations().isEmpty()),
                () -> assertTrue(cr.getPaymentOptions().isEmpty()));
    }
}
