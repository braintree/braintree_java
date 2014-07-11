package com.braintreegateway;

public class CustomerSearchRequest extends SearchRequest {
    public TextNode<CustomerSearchRequest> addressCountryName() {
        return textNode("address_country_name");
    }

    public TextNode<CustomerSearchRequest> addressExtendedAddress() {
        return textNode("address_extended_address");
    }

    public TextNode<CustomerSearchRequest> addressFirstName() {
        return textNode("address_first_name");
    }

    public TextNode<CustomerSearchRequest> addressLastName() {
        return textNode("address_last_name");
    }

    public TextNode<CustomerSearchRequest> addressLocality() {
        return textNode("address_locality");
    }

    public TextNode<CustomerSearchRequest> addressPostalCode() {
        return textNode("address_postal_code");
    }

    public TextNode<CustomerSearchRequest> addressRegion() {
        return textNode("address_region");
    }

    public TextNode<CustomerSearchRequest> addressStreetAddress() {
        return textNode("address_street_address");
    }

    public TextNode<CustomerSearchRequest> cardholderName() {
        return textNode("cardholder_name");
    }

    public TextNode<CustomerSearchRequest> company() {
        return textNode("company");
    }

    public EqualityNode<CustomerSearchRequest> creditCardExpirationDate() {
        return new EqualityNode<CustomerSearchRequest>("credit_card_expiration_date", this);
    }

    public TextNode<CustomerSearchRequest> email() {
        return textNode("email");
    }

    public TextNode<CustomerSearchRequest> fax() {
        return textNode("fax");
    }

    public TextNode<CustomerSearchRequest> firstName() {
        return textNode("first_name");
    }

    public TextNode<CustomerSearchRequest> id() {
        return textNode("id");
    }

    public TextNode<CustomerSearchRequest> lastName() {
        return textNode("last_name");
    }

    public TextNode<CustomerSearchRequest> paymentMethodToken() {
        return textNode("payment_method_token");
    }

    public TextNode<CustomerSearchRequest> paypalAccountEmail() {
        return textNode("paypal_account_email");
    }

    public TextNode<CustomerSearchRequest> phone() {
        return textNode("phone");
    }

    public TextNode<CustomerSearchRequest> website() {
        return textNode("website");
    }

    public IsNode<CustomerSearchRequest> paymentMethodTokenWithDuplicates() {
        return new IsNode<CustomerSearchRequest>("payment_method_token_with_duplicates", this);
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

    private TextNode<CustomerSearchRequest> textNode(String fieldName) {
        return new TextNode<CustomerSearchRequest>(fieldName, this);
    }
}
