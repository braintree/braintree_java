package com.braintreegateway;

import java.util.HashMap;
import java.util.Map;

public class TransactionOptionsPayPalRequest extends Request {
    private TransactionOptionsRequest parent;
    private String customField;
    private String description;
    private String payeeId;
    private String payeeEmail;
    private String recipentEmail; 
    private TransactionOptionsRecipientPhoneRequest recipientPhone;   
    private Map<String, String> supplementaryData;

    public TransactionOptionsPayPalRequest(TransactionOptionsRequest parent) {
        this.parent = parent;
        this.supplementaryData = new HashMap<String, String>();
    }

    public TransactionOptionsRequest done() {
        return parent;
    }

    public TransactionOptionsPayPalRequest customField(String customField) {
        this.customField = customField;
        return this;
    }

    public TransactionOptionsPayPalRequest description(String description) {
        this.description = description;
        return this;
    }

    public TransactionOptionsPayPalRequest payeeId(String payeeId) {
        this.payeeId = payeeId;
        return this;
    }

    public TransactionOptionsPayPalRequest payeeEmail(String payeeEmail) {
        this.payeeEmail = payeeEmail;
        return this;
    } 

    public TransactionOptionsPayPalRequest recipentEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
        return this;
    } 
    
    public TransactionOptionsPayPalRequest recipentPhone() {
        transactionOptionsRecipientPhoneRequest = new TransactionOptionsRecipientPhoneRequest(this);
        return transactionOptionsRecipientPhoneRequest; 
    }  

    public TransactionOptionsPayPalRequest supplementaryData(String key, String value) {
        this.supplementaryData.put(key, value);
        return this;
    }

    @Override
    public String toXML() {
        return buildRequest("paypal").toXML();
    }

    @Override
    public String toQueryString() {
        return toQueryString("paypal");
    }

    @Override
    public String toQueryString(String root) {
        return buildRequest(root).toQueryString();
    }

    protected RequestBuilder buildRequest(String root) {
        RequestBuilder builder = new RequestBuilder(root)
            .addElement("customField", customField)
            .addElement("description", description)
            .addElement("payeeId", payeeId)
            .addElement("payeeEmail", payeeEmail)
            .addElement("recipientEmail", recipientEmail)
            .addElement("recipientPhone", transactionOptionsRecipientPhoneRequest);

        if (!supplementaryData.isEmpty()) {
            builder.addElement("supplementaryData", supplementaryData);
        }

        return builder;
    }
}
