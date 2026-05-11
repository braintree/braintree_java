package com.braintreegateway;

public class ApplePayCardOptionsRequest extends Request {
    private Boolean makeDefault;
    private ApplePayCardRequest parent;
    private Boolean verifyCard;
    private String verificationAccountType; // NEXT_MAJOR_VERSION - This should be enum with [credit, debit]
    private String verificationAmount;
    private String verificationMerchantAccountId;

    public ApplePayCardOptionsRequest() {}

    public ApplePayCardOptionsRequest(ApplePayCardRequest parent) {
        this.parent = parent;
    }

    public ApplePayCardRequest done() {
        return parent;
    }

    public Boolean getMakeDefault() {
        return makeDefault;
    }

    public ApplePayCardOptionsRequest makeDefault(Boolean makeDefault) {
        this.makeDefault = makeDefault;
        return this;
    }
    
    public ApplePayCardOptionsRequest verifyCard(Boolean verifyCard) {
        this.verifyCard = verifyCard;
        return this;
    }

    public ApplePayCardOptionsRequest verificationAccountType(String verificationAccountType) {
        this.verificationAccountType = verificationAccountType;
        return this;
    }

    public ApplePayCardOptionsRequest verificationAmount(String verificationAmount) {
        this.verificationAmount = verificationAmount;
        return this;
    }

    public ApplePayCardOptionsRequest verificationMerchantAccountId(String verificationMerchantAccountId) {
        this.verificationMerchantAccountId = verificationMerchantAccountId;
        return this;
    }

    @Override
    public String toXML() {
        return buildRequest("options").toXML();
    }

    protected RequestBuilder buildRequest(String root) {
        RequestBuilder builder = new RequestBuilder(root);

        builder.addElement("makeDefault", makeDefault);
        builder.addElement("verifyCard", verifyCard);
        builder.addElement("verificationAccountType", verificationAccountType);
        builder.addElement("verificationAmount", verificationAmount);
        builder.addElement("verificationMerchantAccountId", verificationMerchantAccountId);

        return builder;
    }
}
