package com.braintreegateway;

public class UsBankAccountVerificationSearchRequest extends SearchRequest {
    public TextNode<UsBankAccountVerificationSearchRequest> id() {
        return new TextNode<UsBankAccountVerificationSearchRequest>("id", this);
    }

    public TextNode<UsBankAccountVerificationSearchRequest> accountHolderName() {
        return new TextNode<UsBankAccountVerificationSearchRequest>("account_holder_name", this);
    }

    public TextNode<UsBankAccountVerificationSearchRequest> customerEmail() {
        return new TextNode<UsBankAccountVerificationSearchRequest>("customer_email", this);
    }

    public TextNode<UsBankAccountVerificationSearchRequest> customerId() {
        return new TextNode<UsBankAccountVerificationSearchRequest>("customer_id", this);
    }

    public TextNode<UsBankAccountVerificationSearchRequest> paymentMethodToken() {
        return new TextNode<UsBankAccountVerificationSearchRequest>("payment_method_token", this);
    }

    public TextNode<UsBankAccountVerificationSearchRequest> routingNumber() {
        return new TextNode<UsBankAccountVerificationSearchRequest>("routing_number", this);
    }

    public MultipleValueNode<UsBankAccountVerificationSearchRequest, String> ids() {
        return new MultipleValueNode<UsBankAccountVerificationSearchRequest, String>("ids", this);
    }

    public MultipleValueNode<UsBankAccountVerificationSearchRequest, UsBankAccountVerification.Status> status() {
        return new MultipleValueNode<UsBankAccountVerificationSearchRequest, UsBankAccountVerification.Status>("status", this);
    }

    public MultipleValueNode<UsBankAccountVerificationSearchRequest, UsBankAccountVerification.VerificationMethod> verificationMethod() {
        return new MultipleValueNode<UsBankAccountVerificationSearchRequest, UsBankAccountVerification.VerificationMethod>("verification_method", this);
    }

    public DateRangeNode<UsBankAccountVerificationSearchRequest> createdAt() {
        return new DateRangeNode<UsBankAccountVerificationSearchRequest>("created_at", this);
    }

    public EqualityNode<UsBankAccountVerificationSearchRequest> accountType() {
        return new EqualityNode<UsBankAccountVerificationSearchRequest>("account_type", this);
    }

    public EndsWithNode<UsBankAccountVerificationSearchRequest> accountNumber() {
        return new EndsWithNode<UsBankAccountVerificationSearchRequest>("account_number", this);
    }
}
