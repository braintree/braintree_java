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
}
