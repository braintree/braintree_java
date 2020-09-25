package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;
import java.util.ArrayList;
import java.util.List;

public class Merchant {

    private String id;
    private String email;
    private String companyName;
    private String countryCodeAlpha3;
    private String countryCodeAlpha2;
    private String countryCodeNumeric;
    private String countryName;
    private OAuthCredentials credentials;
    private List<MerchantAccount> merchantAccounts;

    public Merchant(NodeWrapper node) {
        NodeWrapper merchantNode = node.findFirst("merchant");

        id = merchantNode.findString("id");
        email = merchantNode.findString("email");
        companyName = merchantNode.findString("company-name");
        countryCodeAlpha3 = merchantNode.findString("country-code-alpha3");
        countryCodeAlpha2 = merchantNode.findString("country-code-alpha2");
        countryCodeNumeric = merchantNode.findString("country-code-numeric");
        countryName = merchantNode.findString("country-name");

        credentials = new OAuthCredentials(node.findFirst("credentials"));

        merchantAccounts = new ArrayList<MerchantAccount>();
        for (NodeWrapper merchantAccountsResponse : merchantNode.findAll("merchant-accounts/merchant-account")) {
            merchantAccounts.add(new MerchantAccount(merchantAccountsResponse));
        }
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getCountryCodeAlpha3() {
        return countryCodeAlpha3;
    }

    public String getCountryCodeAlpha2() {
        return countryCodeAlpha2;
    }

    public String getCountryCodeNumeric() {
        return countryCodeNumeric;
    }

    public String getCountryName() {
        return countryName;
    }

    public OAuthCredentials getCredentials() {
        return credentials;
    }

    public List<MerchantAccount> getMerchantAccounts() {
        return merchantAccounts;
    }
}
