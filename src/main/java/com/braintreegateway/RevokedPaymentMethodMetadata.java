package com.braintreegateway;

import com.braintreegateway.exceptions.UnexpectedException;
import com.braintreegateway.util.NodeWrapper;
import com.braintreegateway.util.PaymentMethodParser;

public class RevokedPaymentMethodMetadata {
    private String customerId;
    private String token;
    private PaymentMethod revokedPaymentMethod;

    public RevokedPaymentMethodMetadata(NodeWrapper node) {
        if (node.getChildren().size() == 0) throw new UnexpectedException("Couldn't parse webhook");

        revokedPaymentMethod = PaymentMethodParser.parsePaymentMethod(node.getChildren().get(0)).getTarget();
        customerId = revokedPaymentMethod.getCustomerId();
        token = revokedPaymentMethod.getToken();
    }

    public String getToken() {
        return token;
    }

    public String getCustomerId() {
        return customerId;
    }

    public PaymentMethod getRevokedPaymentMethod() {
        return revokedPaymentMethod;
    }
}
