package com.braintreegateway;

import java.util.Map;

import com.braintreegateway.util.NodeWrapper;

public class Result<T> {

    private CreditCardVerification creditCardVerification;
    private ValidationErrors errors;
    private Map<String, String> parameters;
    private T target;

    @SuppressWarnings("unchecked")
    public static <T> T newInstanceFromNode(Class<T> klass, NodeWrapper node) {
        if (klass == CreditCard.class) {
            return (T) new CreditCard(node);
        } else if (klass == Address.class) {
            return (T) new Address(node);
        } else if (klass == Customer.class) {
            return (T) new Customer(node);
        } else if (klass == Subscription.class) {
            return (T) new Subscription(node);
        } else if (klass == Transaction.class) {
            return (T) new Transaction(node);
        }
        throw new IllegalArgumentException("Unknown klass: " + klass);
    }

    public Result() {
    }

    public Result(NodeWrapper node, Class<T> klass) {
        if (node.isSuccess()) {
            this.target = newInstanceFromNode(klass, node);
        } else {
            this.errors = new ValidationErrors(node);
            this.creditCardVerification = new CreditCardVerification(node);
            this.parameters = node.findFirst("params").getFormParameters();
        }
    }

    public CreditCardVerification getCreditCardVerification() {
        return creditCardVerification;
    }

    public ValidationErrors getErrors() {
        return errors;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public T getTarget() {
        return target;
    }

    public boolean isSuccess() {
        return errors == null;
    }
}
