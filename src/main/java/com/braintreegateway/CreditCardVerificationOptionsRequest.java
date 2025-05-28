package com.braintreegateway;

public class CreditCardVerificationOptionsRequest extends Request {
    private CreditCardVerificationRequest parent;
    private String accountInformationInquiry;
    private String accountType;
    private String amount;
    private String merchantAccountId;

    public CreditCardVerificationOptionsRequest(CreditCardVerificationRequest parent) {
        this.parent = parent;
    }

    public CreditCardVerificationOptionsRequest merchantAccountId(String merchantAccountId) {
        this.merchantAccountId = merchantAccountId;
        return this;
    }

    public CreditCardVerificationOptionsRequest accountInformationInquiry(String accountInformationInquiry) {
        this.accountInformationInquiry = accountInformationInquiry;
        return this;
    }

    public CreditCardVerificationOptionsRequest amount(String amount) {
        this.amount = amount;
        return this;
    }

    public CreditCardVerificationOptionsRequest accountType(String accountType) {
        this.accountType = accountType;
        return this;
    }

    public CreditCardVerificationRequest done() {
        return parent;
    }

    @Override
    public String toXML() {
        return buildRequest("options").toXML();
    }

    protected RequestBuilder buildRequest(String root) {
        RequestBuilder builder = new RequestBuilder(root)
            .addElement("accountInformationInquiry" , accountInformationInquiry)
            .addElement("accountType", accountType)
            .addElement("amount", amount)
            .addElement("merchantAccountId", merchantAccountId);
        return builder;
    }
}
