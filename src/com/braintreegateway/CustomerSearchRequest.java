package com.braintreegateway;

public class CustomerSearchRequest extends SearchRequest {
    public TextNode<CustomerSearchRequest> addressExtendedAddress() {
        return new TextNode<CustomerSearchRequest>("address_extended_address", this);
    }

    public TextNode<CustomerSearchRequest> addressFirstName() {
        return new TextNode<CustomerSearchRequest>("address_first_name", this);
    }

    public TextNode<CustomerSearchRequest> addressLastName() {
        return new TextNode<CustomerSearchRequest>("address_last_name", this);
    }

    public TextNode<CustomerSearchRequest> addressLocality() {
        return new TextNode<CustomerSearchRequest>("address_locality", this);
    }

    public TextNode<CustomerSearchRequest> addressPostalCode() {
        return new TextNode<CustomerSearchRequest>("address_postal_code", this);
    }

    public TextNode<CustomerSearchRequest> addressRegion() {
        return new TextNode<CustomerSearchRequest>("address_region", this);
    }

    public TextNode<CustomerSearchRequest> addressStreetAddress() {
        return new TextNode<CustomerSearchRequest>("address_street_address", this);
    }

    public TextNode<CustomerSearchRequest> cardholderName() {
        return new TextNode<CustomerSearchRequest>("cardholder_name", this);
    }

    public TextNode<CustomerSearchRequest> company() {
        return new TextNode<CustomerSearchRequest>("company", this);
    }

    public EqualityNode<CustomerSearchRequest> creditCardExpirationDate() {
        return new EqualityNode<CustomerSearchRequest>("credit_card_expiration_date", this);
    }

    public TextNode<CustomerSearchRequest> email() {
        return new TextNode<CustomerSearchRequest>("email", this);
    }

    public TextNode<CustomerSearchRequest> fax() {
        return new TextNode<CustomerSearchRequest>("fax", this);
    }

    public TextNode<CustomerSearchRequest> firstName() {
        return new TextNode<CustomerSearchRequest>("first_name", this);
    }

    public TextNode<CustomerSearchRequest> id() {
        return new TextNode<CustomerSearchRequest>("id", this);
    }

    public TextNode<CustomerSearchRequest> lastName() {
        return new TextNode<CustomerSearchRequest>("last_name", this);
    }

    public TextNode<CustomerSearchRequest> paymentMethodToken() {
        return new TextNode<CustomerSearchRequest>("payment_method_token", this);
    }

    public TextNode<CustomerSearchRequest> phone() {
        return new TextNode<CustomerSearchRequest>("phone", this);
    }

    public TextNode<CustomerSearchRequest> website() {
        return new TextNode<CustomerSearchRequest>("website", this);
    }
    
    public MultipleValueNode<CustomerSearchRequest, String> ids() {
        return new MultipleValueNode<CustomerSearchRequest, String>("ids", this);
    }
    
    public PartialMatchNode<CustomerSearchRequest> creditCardNumber() {
        return new PartialMatchNode<CustomerSearchRequest>("credit_card_number", this);
    }

    public DateRangeNode<CustomerSearchRequest> createdAt() {
        return new DateRangeNode<CustomerSearchRequest>("created_at", this);
    }
}
