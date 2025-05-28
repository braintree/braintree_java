package com.braintreegateway.graphql.unions;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import com.braintreegateway.graphql.types.PaymentOptions;
import com.braintreegateway.graphql.types.PaymentRecommendation;
import com.braintreegateway.util.Experimental;

/**
 * A union of all possible customer recommendations associated with a PayPal customer session.
 */
@Experimental("This class is experimental and may change in future releases.")
public class CustomerRecommendations {
    // Deprecated: Use paymentRecommendations instead.
    private List<PaymentOptions> paymentOptions;
    private List<PaymentRecommendation> paymentRecommendations;

    public List<PaymentOptions> getPaymentOptions() {
        return paymentOptions;
    }

    public List<PaymentRecommendation> getPaymentRecommendations() {
        return paymentRecommendations;
    }

    public CustomerRecommendations(List<PaymentRecommendation> paymentRecommendations) {
        this.paymentRecommendations = paymentRecommendations;
        this.paymentOptions = paymentRecommendations.stream()
            .map(paymentRecommendation -> new PaymentOptions(
                paymentRecommendation.getPaymentOption(),
                paymentRecommendation.getRecommendedPriority()
            ))
            .collect(Collectors.toList());
    }
    
    // Static factory method for PaymentOptions
    public static CustomerRecommendations fromPaymentOptions(List<PaymentOptions> paymentOptions) {
        CustomerRecommendations cr = new CustomerRecommendations(new ArrayList<>());
        cr.paymentOptions = paymentOptions != null ? paymentOptions : new ArrayList<>();
        return cr;
    }

    public CustomerRecommendations() {
        this.paymentRecommendations = new ArrayList<>();
        this.paymentOptions = new ArrayList<>();
    }
}
