package com.braintreegateway;

public class CreditCardOptions {
    private Boolean failOnDuplicatePaymentMethod;
    private Boolean verifyCard;
    private Boolean makeDefault;

    public CreditCardOptions() {}

    public CreditCardOptions verifyCard(Boolean verifyCard) {
        this.verifyCard = verifyCard;
        return this;
    }

    public CreditCardOptions makeDefault(Boolean makeDefault) {
        this.makeDefault = makeDefault;
        return this;
    }

    public CreditCardOptions failOnDuplicatePaymentMethod(Boolean failOnDuplicatePaymentMethod) {
        this.failOnDuplicatePaymentMethod = failOnDuplicatePaymentMethod;
        return this;
    }

    public Boolean getFailOnDuplicatePaymentMethod () {
        return failOnDuplicatePaymentMethod;
    }

    public Boolean getVerifyCard () {
        return verifyCard;
    }

    public Boolean getMakeDefault() {
        return makeDefault;
    }
}
