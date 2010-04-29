package com.braintreegateway;

public class TransactionSearchRequest extends SearchRequest {
    public TextNode<TransactionSearchRequest> id() {
        return new TextNode<TransactionSearchRequest>("id", this);
    }
    
    public TextNode<TransactionSearchRequest> billingCompany() {
        return new TextNode<TransactionSearchRequest>("billing_company", this);
    }
    
    public TextNode<TransactionSearchRequest> billingCountryName() {
        return new TextNode<TransactionSearchRequest>("billing_country_name", this);
    }

    public TextNode<TransactionSearchRequest> billingExtendedAddress() {
        return new TextNode<TransactionSearchRequest>("billing_extended_address", this);
    }
    
    public TextNode<TransactionSearchRequest> billingFirstName() {
        return new TextNode<TransactionSearchRequest>("billing_first_name", this);
    }
    
    public TextNode<TransactionSearchRequest> billingLastName() {
        return new TextNode<TransactionSearchRequest>("billing_last_name", this);
    }
    
    public TextNode<TransactionSearchRequest> billingLocality() {
        return new TextNode<TransactionSearchRequest>("billing_locality", this);
    }
    
    public TextNode<TransactionSearchRequest> billingPostalCode() {
        return new TextNode<TransactionSearchRequest>("billing_postal_code", this);
    }
    
    public TextNode<TransactionSearchRequest> billingRegion() {
        return new TextNode<TransactionSearchRequest>("billing_region", this);
    }
    
    public TextNode<TransactionSearchRequest> billingStreetAddress() {
        return new TextNode<TransactionSearchRequest>("billing_street_address", this);
    }
    
    public TextNode<TransactionSearchRequest> creditCardCardholderName() {
        return new TextNode<TransactionSearchRequest>("credit_card_cardholder_name", this);
    }
    
    public EqualityNode<TransactionSearchRequest> creditCardExpirationDate() {
        return new TextNode<TransactionSearchRequest>("credit_card_expiration_date", this);
    }
    
    public PartialMatchNode<TransactionSearchRequest> creditCardNumber() {
        return new TextNode<TransactionSearchRequest>("credit_card_number", this);
    }
    
    public TextNode<TransactionSearchRequest> currency() {
        return new TextNode<TransactionSearchRequest>("currency", this);
    }
    
    public TextNode<TransactionSearchRequest> customerCompany() {
        return new TextNode<TransactionSearchRequest>("customer_company", this);
    }
    
    public TextNode<TransactionSearchRequest> customerEmail() {
        return new TextNode<TransactionSearchRequest>("customer_email", this);
    }
    
    public TextNode<TransactionSearchRequest> customerFax() {
        return new TextNode<TransactionSearchRequest>("customer_fax", this);
    }
    
    public TextNode<TransactionSearchRequest> customerFirstName() {
        return new TextNode<TransactionSearchRequest>("customer_first_name", this);
    }
    
    public TextNode<TransactionSearchRequest> customerId() {
        return new TextNode<TransactionSearchRequest>("customer_id", this);
    }
    
    public TextNode<TransactionSearchRequest> customerLastName() {
        return new TextNode<TransactionSearchRequest>("customer_last_name", this);
    }
    
    public TextNode<TransactionSearchRequest> customerPhone() {
        return new TextNode<TransactionSearchRequest>("customer_phone", this);
    }
    
    public TextNode<TransactionSearchRequest> customerWebsite() {
        return new TextNode<TransactionSearchRequest>("customer_website", this);
    }
    
    public TextNode<TransactionSearchRequest> orderId() {
        return new TextNode<TransactionSearchRequest>("order_id", this);
    }
    
    public TextNode<TransactionSearchRequest> paymentMethodToken() {
        return new TextNode<TransactionSearchRequest>("payment_method_token", this);
    }
    
    public TextNode<TransactionSearchRequest> processorAuthorizationCode() {
        return new TextNode<TransactionSearchRequest>("processor_authorization_code", this);
    }
    
    public TextNode<TransactionSearchRequest> shippingCompany() {
        return new TextNode<TransactionSearchRequest>("shipping_company", this);
    }
    
    public TextNode<TransactionSearchRequest> shippingCountryName() {
        return new TextNode<TransactionSearchRequest>("shipping_country_name", this);
    }
    
    public TextNode<TransactionSearchRequest> shippingExtendedAddress() {
        return new TextNode<TransactionSearchRequest>("shipping_extended_address", this);
    }
    
    public TextNode<TransactionSearchRequest> shippingFirstName() {
        return new TextNode<TransactionSearchRequest>("shipping_first_name", this);
    }
    
    public TextNode<TransactionSearchRequest> shippingLastName() {
        return new TextNode<TransactionSearchRequest>("shipping_last_name", this);
    }
    
    public TextNode<TransactionSearchRequest> shippingLocality() {
        return new TextNode<TransactionSearchRequest>("shipping_locality", this);
    }
    
    public TextNode<TransactionSearchRequest> shippingPostalCode() {
        return new TextNode<TransactionSearchRequest>("shipping_postal_code", this);
    }
    
    public TextNode<TransactionSearchRequest> shippingRegion() {
        return new TextNode<TransactionSearchRequest>("shipping_region", this);
    }
    
    public TextNode<TransactionSearchRequest> shippingStreetAddress() {
        return new TextNode<TransactionSearchRequest>("shipping_street_address", this);
    }
}
