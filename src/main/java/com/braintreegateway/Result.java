package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class Result<T> {

    private UsBankAccountVerification usBankAccountVerification;
    private CreditCardVerification creditCardVerification;
    private Transaction transaction;
    private Subscription subscription;
    private ValidationErrors errors;
    private Map<String, String> parameters;
    private String message;
    private T target;

    public static <T> T newInstanceFromNode(Class<T> klass, NodeWrapper node) {
        Throwable cause = null;
        try {
            return klass.getConstructor(NodeWrapper.class).newInstance(node);
        }
        catch (InstantiationException e) { cause = e; }
        catch (IllegalAccessException e) { cause = e; }
        catch (InvocationTargetException e) { cause = e;  }
        catch (NoSuchMethodException e) { cause = e; }

        throw new IllegalArgumentException("Unknown klass: " + klass, cause);
    }

    public Result() {
    }

    public Result(ValidationErrors errors) {
        this.errors = errors;
    }

    public Result(T target) {
        this.target = target;
    }

    public Result(NodeWrapper node, Class<T> klass) {
        if (node.isSuccess()) {
            this.target = newInstanceFromNode(klass, node);
        } else {
            this.errors = new ValidationErrors(node);

            NodeWrapper usBankAccountVerificationNode = node.findFirst("us-bank-account-verification");
            if (usBankAccountVerificationNode != null) {
                this.usBankAccountVerification = new UsBankAccountVerification(usBankAccountVerificationNode);
            }

            NodeWrapper verificationNode = node.findFirst("verification");
            if (verificationNode != null) {
                this.creditCardVerification = new CreditCardVerification(verificationNode);
            }

            NodeWrapper transactionNode = node.findFirst("transaction");
            if (transactionNode != null) {
                this.transaction = new Transaction(transactionNode);
            }
            NodeWrapper subscriptionNode = node.findFirst("subscription");
            if (subscriptionNode != null) {
                this.subscription = new Subscription(subscriptionNode);
            }
            this.parameters = node.findFirst("params").getFormParameters();
            this.message = node.findString("message");
        }
    }

    public UsBankAccountVerification getUsBankAccountVerification() {
        return usBankAccountVerification;
    }

    public CreditCardVerification getCreditCardVerification() {
        return creditCardVerification;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public Transaction getTransaction() {
        return transaction;
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

    public String getMessage() {
        return message;
    }
}
