package com.braintreegateway;

import java.util.ArrayList;

/**
 * Provides a fluent interface to build up requests for client tokens, which are
 *   used to authenticate requests clients make directly on behalf of merchants
 */
public class ClientTokenRequest extends Request {
    private static final int DEFAULT_VERSION = 2;

    private String customerId;
    private ArrayList<String> domains;
    private String merchantAccountId;
    private ClientTokenOptionsRequest optionsRequest;
    private int version;

    public String getCustomerId() {
        return customerId;
    }

    public String getMerchantAccountId() {
        return merchantAccountId;
    }

    public ClientTokenOptionsRequest getOptions() {
        return optionsRequest;
    }

    public ClientTokenRequest customerId(String customerId) {
        this.customerId = customerId;
        return this;
    }

    public ClientTokenRequest domains(ArrayList<String> domains) {
        this.domains = domains;
        return this;
    }

    public ClientTokenRequest merchantAccountId(String merchantAccountId) {
        this.merchantAccountId = merchantAccountId;
        return this;
    }

    public ClientTokenRequest options(ClientTokenOptionsRequest optionsRequest) {
        this.optionsRequest = optionsRequest;
        return this;
    }

    public ClientTokenRequest version(int version) {
        this.version = version;
        return this;
    }

    @Override
    public String toXML() {
        return buildRequest("clientToken").toXML();
    }

    protected RequestBuilder buildRequest(String root) {
        RequestBuilder builder = new RequestBuilder(root);

        if (customerId != null) {
            builder.addElement("customerId", customerId);
        }

        if (domains != null) {
            builder.addElement("domains", domains);
        }

        if (merchantAccountId != null) {
            builder.addElement("merchantAccountId", merchantAccountId);
        }

        if (optionsRequest != null) {
            builder.addElement("options", optionsRequest);
        }

        if (version != 0) {
            builder.addElement("version", version);
        } else {
            builder.addElement("version", DEFAULT_VERSION);
        }

        return builder;
    }
}
