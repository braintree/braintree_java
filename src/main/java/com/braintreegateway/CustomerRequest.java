package com.braintreegateway;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provides a fluent interface to build up requests around {@link Customer Customers}.
 */
public class CustomerRequest extends Request {
    private String company;
    private String customerId;
    private String defaultPaymentMethodToken;
    private String deviceData;
    private String deviceSessionId;
    private String email;
    private String fax;
    private String firstName;
    private String fraudMerchantId;
    private String id;
    private String lastName;
    private String paymentMethodNonce;
    private String phone;
    private String threeDSecureAuthenticationId;
    private String website;
    private AndroidPayCardRequest androidPayCardRequest;
    private AndroidPayNetworkTokenRequest androidPayNetworkTokenRequest;
    private ApplePayCardRequest applePayCardRequest;
    private CreditCardRequest creditCardRequest;
    private CustomerOptionsRequest optionsRequest;
    private List<TaxIdentifierRequest> taxIdentifierRequests;
    private Map<String, String> customFields;
    private RiskDataCustomerRequest riskDataCustomerRequest;
    private TransactionRequest parent;

    public CustomerRequest() {
        this.customFields = new HashMap<String, String>();
        this.taxIdentifierRequests = new ArrayList<TaxIdentifierRequest>();
    }
    
    public CustomerRequest(TransactionRequest transactionRequest) {
        this();
        this.parent = transactionRequest;
    }

    public AndroidPayCardRequest androidPayCard() {
        androidPayCardRequest = new AndroidPayCardRequest(this);
        return this.androidPayCardRequest;
    }

    public AndroidPayNetworkTokenRequest androidPayNetworkToken() {
        androidPayNetworkTokenRequest = new AndroidPayNetworkTokenRequest(this);
        return this.androidPayNetworkTokenRequest;
    }
  
    public ApplePayCardRequest applePayCard() {
        applePayCardRequest = new ApplePayCardRequest(this);
        return this.applePayCardRequest;
    }

    public CreditCardRequest creditCard() {
        creditCardRequest = new CreditCardRequest(this);
        return this.creditCardRequest;
    }

    public CustomerRequest company(String company) {
        this.company = company;
        return this;
    }

    public CustomerRequest customerId(String customerId) {
        this.customerId = customerId;
        return this;
    }
    
    public CustomerRequest customField(String apiName, String value) {
        customFields.put(apiName, value);
        return this;
    }

    public CustomerRequest defaultPaymentMethodToken(String token) {
        this.defaultPaymentMethodToken = token;
        return this;
    }

    public CustomerRequest deviceData(String deviceData) {
        this.deviceData = deviceData;
        return this;
    }

    @Deprecated
    // Merchants should be using deviceData only
    public CustomerRequest deviceSessionId(String deviceSessionId) {
        this.deviceSessionId = deviceSessionId;
        return this;
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

    @Deprecated
    // Merchants should be using deviceData only
    public CustomerRequest fraudMerchantId(String fraudMerchantId) {
        this.fraudMerchantId = fraudMerchantId;
        return this;
    }
  
    public CustomerRequest id(String id) {
        this.id = id;
        return this;
    }

    public CustomerRequest lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public CustomerRequest paymentMethodNonce(String nonce) {
        this.paymentMethodNonce = nonce;
        return this;
    }

    public CustomerRequest phone(String phone) {
        this.phone = phone;
        return this;
    }
    
    public CustomerRequest website(String website) {
        this.website = website;
        return this;
    }

    public CustomerOptionsRequest options() {
        this.optionsRequest = new CustomerOptionsRequest(this);
        return optionsRequest;
    }

    public RiskDataCustomerRequest riskData() {
        riskDataCustomerRequest = new RiskDataCustomerRequest(this);
        return this.riskDataCustomerRequest;
    }

    public String getId() {
        return id;
    }
    
    public CustomerRequest threeDSecureAuthenticationId(String threeDSecureAuthenticationId) {
        this.threeDSecureAuthenticationId = threeDSecureAuthenticationId;
        return this;
    }

    public TaxIdentifierRequest taxIdentifier() {
        TaxIdentifierRequest taxIdentiferRequest = new TaxIdentifierRequest(this);
        taxIdentifierRequests.add(taxIdentiferRequest);
        return taxIdentiferRequest;
    }

    public TransactionRequest done() {
        return parent;
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
            .addElement("androidPayCard", androidPayCardRequest)
            .addElement("androidPayNetworkToken", androidPayNetworkTokenRequest)
            .addElement("applePayCard", applePayCardRequest)
            .addElement("company", company)
            .addElement("creditCard", creditCardRequest)
            .addElement("defaultPaymentMethodToken", defaultPaymentMethodToken)
            .addElement("deviceData", deviceData)
            .addElement("email", email)
            .addElement("fax", fax)
            .addElement("firstName", firstName)
            .addElement("id", id)
            .addElement("lastName", lastName)
            .addElement("options", optionsRequest)
            .addElement("paymentMethodNonce", paymentMethodNonce)
            .addElement("phone", phone)
            .addElement("riskData", riskDataCustomerRequest)
            .addElement("threeDSecureAuthenticationId", threeDSecureAuthenticationId)
            .addElement("website", website);

        if (customFields.size() > 0) {
            builder.addElement("customFields", customFields);
        }

        if (!taxIdentifierRequests.isEmpty()) {
            builder.addElement("taxIdentifiers", taxIdentifierRequests);
        }

        return builder;
    }
}
