package com.braintreegateway;

public class CreditCardVerificationSearchRequest extends SearchRequest {
    public TextNode<CreditCardVerificationSearchRequest> id() {
        return new TextNode<CreditCardVerificationSearchRequest>("id", this);
    }

    public TextNode<CreditCardVerificationSearchRequest> creditCardCardholderName() {
        return new TextNode<CreditCardVerificationSearchRequest>("credit_card_cardholder_name", this);
    }

    public EqualityNode<CreditCardVerificationSearchRequest> creditCardExpirationDate() {
        return new EqualityNode<CreditCardVerificationSearchRequest>("credit_card_expiration_date", this);
    }

    public PartialMatchNode<CreditCardVerificationSearchRequest> creditCardNumber() {
        return new PartialMatchNode<CreditCardVerificationSearchRequest>("credit_card_number", this);
    }

    public MultipleValueNode<CreditCardVerificationSearchRequest, String> ids() {
        return new MultipleValueNode<CreditCardVerificationSearchRequest, String>("ids", this);
    }

    public MultipleValueNode<CreditCardVerificationSearchRequest, CreditCard.CardType> creditCardCardType() {
        return new MultipleValueNode<CreditCardVerificationSearchRequest, CreditCard.CardType>("credit_card_card_type", this);
    }

    public DateRangeNode<CreditCardVerificationSearchRequest> createdAt() {
        return new DateRangeNode<CreditCardVerificationSearchRequest>("created_at", this);
    }

    public MultipleValueNode<CreditCardVerificationSearchRequest, CreditCardVerification.Status> status() {
        return new MultipleValueNode<CreditCardVerificationSearchRequest, CreditCardVerification.Status>("status", this);
    }

    public TextNode<CreditCardVerificationSearchRequest> billingPostalCode() {
        return new TextNode<CreditCardVerificationSearchRequest>("billing_address_details_postal_code", this);
    }

    public TextNode<CreditCardVerificationSearchRequest> customerEmail() {
        return new TextNode<CreditCardVerificationSearchRequest>("customer_email", this);
    }

    public TextNode<CreditCardVerificationSearchRequest> customerId() {
        return new TextNode<CreditCardVerificationSearchRequest>("customer_id", this);
    }

    public TextNode<CreditCardVerificationSearchRequest> paymentMethodToken() {
        return new TextNode<CreditCardVerificationSearchRequest>("payment_method_token", this);
    }
}
