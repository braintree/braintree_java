package com.braintreegateway;

import com.braintreegateway.util.QueryString;


/**
 * Provides a fluent interface to build up requests around {@link CreditCard CreditCards}.
 */
public class CreditCardRequest extends Request {
    private CreditCardAddressRequest billingAddressRequest;
    private String cardholderName;
    private String customerId;
    private String cvv;
    private String expirationDate;
    private String number;
    private CreditCardOptionsRequest optionsRequest;
    private CustomerRequest parent;
    private String token;
    private String paymentMethodToken;

    public CreditCardRequest() {
    }

    public CreditCardRequest(CustomerRequest parent) {
        this.parent = parent;
    }

    public CreditCardAddressRequest billingAddress() {
        billingAddressRequest = new CreditCardAddressRequest(this);
        return billingAddressRequest;
    }

    public CreditCardRequest cardholderName(String cardholderName) {
        this.cardholderName = cardholderName;
        return this;
    }

    public CreditCardRequest customerId(String customerId) {
        this.customerId = customerId;
        return this;
    }

    public CreditCardRequest cvv(String cvv) {
        this.cvv = cvv;
        return this;
    }

    public CustomerRequest done() {
        return parent;
    }

    public CreditCardRequest expirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
        return this;
    }

    public String getCustomerId() {
        return customerId;
    }
    
    @Override
    public String getKind() {
        if (this.paymentMethodToken == null) {
            return "create_payment_method";
        } else {
            return "update_payment_method";
        }
    }

    public String getToken() {
        return token;
    }

    public CreditCardRequest number(String number) {
        this.number = number;
        return this;
    }

    public CreditCardOptionsRequest options() {
        this.optionsRequest = new CreditCardOptionsRequest(this);
        return optionsRequest;
    }

    public CreditCardRequest paymentMethodToken(String paymentMethodToken) {
        this.paymentMethodToken = paymentMethodToken;
        return this;
    }

    public CreditCardRequest token(String token) {
        this.token = token;
        return this;
    }

    @Override
    public String toXML() {
        StringBuilder builder = new StringBuilder();
        builder.append("<creditCard>");
        builder.append(buildXMLElement(billingAddressRequest));
        builder.append(buildXMLElement(optionsRequest));
        builder.append(buildXMLElement("customerId", customerId));
        builder.append(buildXMLElement("cardholderName", cardholderName));
        builder.append(buildXMLElement("cvv", cvv));
        builder.append(buildXMLElement("number", number));
        builder.append(buildXMLElement("expirationDate", expirationDate));
        builder.append(buildXMLElement("token", token));
        builder.append("</creditCard>");
        return builder.toString();
    }

    public String toQueryString() {
        return toQueryString("credit_card");
    }

    public String toQueryString(String root) {
        return new QueryString().
            append(parentBracketChildString(root, "billing_address"), billingAddressRequest).
            append(parentBracketChildString(root, "cardholder_name"), cardholderName).
            append(parentBracketChildString(root, "customer_id"), customerId).
            append(parentBracketChildString(root, "cvv"), cvv).
            append(parentBracketChildString(root, "expiration_date"), expirationDate).
            append(parentBracketChildString(root, "number"), number).
            append(parentBracketChildString(root, "options"), optionsRequest).
            append(parentBracketChildString(root, "token"), token).
            append("payment_method_token", paymentMethodToken).
            toString();
    }
}
