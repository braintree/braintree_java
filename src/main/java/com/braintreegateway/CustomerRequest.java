package com.braintreegateway;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides a fluent interface to build up requests around {@link Customer Customers}.
 */
public class CustomerRequest extends Request {
    private String deviceData;
    private String company;
    private String customerId;
    private String deviceSessionId;
    private String fraudMerchantId;
    private String email;
    private String fax;
    private String firstName;
    private String id;
    private String lastName;
    private String phone;
    private String website;
    private String paymentMethodNonce;
    private String defaultPaymentMethodToken;
    private Map<String, String> customFields;
    private CreditCardRequest creditCardRequest;
    private RiskDataCustomerRequest riskDataCustomerRequest;
    private CustomerOptionsRequest optionsRequest;
    private TransactionRequest parent;

    public CustomerRequest() {
        this.customFields = new HashMap<String, String>();
    }

    public CustomerRequest(TransactionRequest transactionRequest) {
        this();
        this.parent = transactionRequest;
    }

    public CustomerRequest deviceData(String deviceData) {
        this.deviceData = deviceData;
        return this;
    }

    public CustomerRequest company(String company) {
        this.company = company;
        return this;
    }

    public CreditCardRequest creditCard() {
        creditCardRequest = new CreditCardRequest(this);
        return this.creditCardRequest;
    }

    public CustomerRequest customerId(String customerId) {
        this.customerId = customerId;
        return this;
    }

    public CustomerRequest customField(String apiName, String value) {
        customFields.put(apiName, value);
        return this;
    }

    @Deprecated
    // Merchants should be using deviceData only
    public CustomerRequest deviceSessionId(String deviceSessionId) {
        this.deviceSessionId = deviceSessionId;
        return this;
    }

    @Deprecated
    // Merchants should be using deviceData only
    public CustomerRequest fraudMerchantId(String fraudMerchantId) {
        this.fraudMerchantId = fraudMerchantId;
        return this;
    }

    public TransactionRequest done() {
        return parent;
    }

    public CustomerRequest email(String email) {
        this.email = email;
        return this;
    }

    public CustomerRequest fax(String fax) {
        this.fax = fax;
        return this;
    }

    public CustomerRequest firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public RiskDataCustomerRequest riskData() {
        riskDataCustomerRequest = new RiskDataCustomerRequest(this);
        return this.riskDataCustomerRequest;
    }

    public CustomerOptionsRequest options() {
        this.optionsRequest = new CustomerOptionsRequest(this);
        return optionsRequest;
    }

    public CustomerRequest lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public CustomerRequest id(String id) {
        this.id = id;
        return this;
    }

    public String getId() {
        return id;
    }

    public CustomerRequest phone(String phone) {
        this.phone = phone;
        return this;
    }

    public CustomerRequest website(String website) {
        this.website = website;
        return this;
    }

    public CustomerRequest paymentMethodNonce(String nonce) {
        this.paymentMethodNonce = nonce;
        return this;
    }

    public CustomerRequest defaultPaymentMethodToken(String token) {
        this.defaultPaymentMethodToken = token;
        return this;
    }

    @Override
    public String toXML() {
        return buildRequest("customer").toXML();
    }

    @Override
    public String toQueryString() {
        return toQueryString("customer");
    }

    @Override
    public String toQueryString(String root) {
        return buildRequest(root)
            .addTopLevelElement("customerId", customerId)
            .toQueryString();
    }

    protected RequestBuilder buildRequest(String root) {
        RequestBuilder builder = new RequestBuilder(root)
            .addElement("deviceData", deviceData)
            .addElement("company", company)
            .addElement("email", email)
            .addElement("fax", fax)
            .addElement("firstName", firstName)
            .addElement("id", id)
            .addElement("lastName", lastName)
            .addElement("phone", phone)
            .addElement("website", website)
            .addElement("paymentMethodNonce", paymentMethodNonce)
            .addElement("defaultPaymentMethodToken", defaultPaymentMethodToken)
            .addElement("creditCard", creditCardRequest)
            .addElement("options", optionsRequest)
            .addElement("riskData", riskDataCustomerRequest);

        if (customFields.size() > 0) {
            builder.addElement("customFields", customFields);
        }

        return builder;
    }
}
