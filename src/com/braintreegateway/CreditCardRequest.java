package com.braintreegateway;

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
            return TransparentRedirectGateway.CREATE_PAYMENT_METHOD;
        } else {
            return TransparentRedirectGateway.UPDATE_PAYMENT_METHOD;
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
        return buildRequest("creditCard").toXML();
    }

    public String toQueryString() {
        return toQueryString("creditCard");
    }

    public String toQueryString(String root) {
        return buildRequest(root).
            addTopLevelElement("payment_method_token", paymentMethodToken).
            toQueryString();
    }
    
    protected RequestBuilder buildRequest(String root) {
        return new RequestBuilder(root).
            addElement("billingAddress", billingAddressRequest).
            addElement("options", optionsRequest).
            addElement("customerId", customerId).
            addElement("cardholderName", cardholderName).
            addElement("cvv", cvv).
            addElement("number", number).
            addElement("expirationDate", expirationDate).
            addElement("token", token);
    }
}
