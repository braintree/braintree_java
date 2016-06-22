package com.braintreegateway;
import java.util.List;

public class MerchantRequest extends Request {

    public String email;
    public String countryCodeAlpha3;
    public String companyName;
    public String scope;
    public PayPalOnlyAccountRequest payPalOnlyAccountRequest;
    public List<String> paymentMethods;
    public List<String> currencies;

    public MerchantRequest email(String email) {
        this.email = email;
        return this;
    }

    public MerchantRequest countryCodeAlpha3(String countryCodeAlpha3) {
        this.countryCodeAlpha3 = countryCodeAlpha3;
        return this;
    }

    public MerchantRequest companyName(String companyName) {
        this.companyName = companyName;
        return this;
    }

    public MerchantRequest paymentMethods(List<String> paymentMethods) {
        this.paymentMethods = paymentMethods;
        return this;
    }

    public MerchantRequest currencies(List<String> currencies) {
        this.currencies = currencies;
        return this;
    }

    public MerchantRequest scope(String scope) {
        this.scope = scope;
        return this;
    }

    @Override
    public String toXML() {
        return new RequestBuilder("merchant").
            addElement("email", email).
            addElement("countryCodeAlpha3", countryCodeAlpha3).
            addElement("companyName", companyName).
            addElement("paymentMethods", paymentMethods).
            addElement("currencies", currencies).
            addElement("scope", scope).
            addElement("paypalAccount", payPalOnlyAccountRequest).
            toXML();
    }

    public PayPalOnlyAccountRequest payPalAccount() {
        this.payPalOnlyAccountRequest = new PayPalOnlyAccountRequest(this);
        return this.payPalOnlyAccountRequest;
    }
}
