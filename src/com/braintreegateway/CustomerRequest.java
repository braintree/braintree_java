package com.braintreegateway;

import java.util.Map;
import java.util.HashMap;

import com.braintreegateway.util.QueryString;


/**
 * Provides a fluent interface to build up requests around {@link Customer Customers}.
 */
public class CustomerRequest extends Request {
    private String company;
    private String customerId;
    private String email;
    private String fax;
    private String firstName;
    private String id;
    private String lastName;
    private String phone;
    private String website;
    private Map<String, String> customFields;
    private CreditCardRequest creditCardRequest;
    private TransactionRequest parent;

    public CustomerRequest() {
        this.customFields = new HashMap<String, String>();
    }

    public CustomerRequest(TransactionRequest transactionRequest) {
        this();
        this.parent = transactionRequest;
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
    
    @Override
    public String getKind() {
        if (this.customerId == null) {
            return TransparentRedirectGateway.CREATE_CUSTOMER;
        } else {
            return TransparentRedirectGateway.UPDATE_CUSTOMER;
        }
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

    @Override
    public String toXML() {
        StringBuilder builder = new StringBuilder();
        builder.append("<customer>");
        builder.append(buildXMLElement("company", company));
        builder.append(buildXMLElement("email", email));
        builder.append(buildXMLElement("fax", fax));
        builder.append(buildXMLElement("firstName", firstName));
        builder.append(buildXMLElement("id", id));
        builder.append(buildXMLElement("lastName", lastName));
        builder.append(buildXMLElement("phone", phone));
        builder.append(buildXMLElement("website", website));
        if (customFields.size() > 0) {
            builder.append(buildXMLElement("customFields", customFields));
        }
        builder.append(buildXMLElement(creditCardRequest));
        builder.append("</customer>");
        return builder.toString();
    }

    public String toQueryString() {
        return toQueryString("customer");
    }

    public String toQueryString(String root) {
        QueryString queryString = new QueryString().
            append("customer_id", customerId).
            append(parentBracketChildString(root, "credit_card"), creditCardRequest).
            append(parentBracketChildString(root, "company"), company).
            append(parentBracketChildString(root, "email"), email).
            append(parentBracketChildString(root, "fax"), fax).
            append(parentBracketChildString(root, "first_name"), firstName).
            append(parentBracketChildString(root, "id"), id).
            append(parentBracketChildString(root, "last_name"), lastName).
            append(parentBracketChildString(root, "phone"), phone).
            append(parentBracketChildString(root, "website"), website);
            if (customFields.size() > 0) {
                queryString.append(parentBracketChildString(root, "custom_fields"), customFields);
            }
            return queryString.toString();
    }
}
