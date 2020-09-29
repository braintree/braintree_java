package com.braintreegateway;

public class ClientTokenOptionsRequest extends Request {
    private Boolean makeDefault;
    private Boolean verifyCard;
    private Boolean failOnDuplicatePaymentMethod;
    private ClientTokenRequest parent;

    public ClientTokenOptionsRequest() {

    }

    public ClientTokenOptionsRequest(ClientTokenRequest parent) {
        this.parent = parent;
    }

    public ClientTokenRequest done() {
        return parent;
    }

    public Boolean getMakeDefault() {
        return makeDefault;
    }

    public ClientTokenOptionsRequest makeDefault(Boolean makeDefault) {
        this.makeDefault = makeDefault;
        return this;
    }

    public Boolean getVerifyCard() {
        return verifyCard;
    }

    public ClientTokenOptionsRequest verifyCard(Boolean verifyCard) {
        this.verifyCard = verifyCard;
        return this;
    }

    public Boolean getFailOnDuplicatePaymentMethod() {
        return failOnDuplicatePaymentMethod;
    }

    public ClientTokenOptionsRequest failOnDuplicatePaymentMethod(Boolean failOnDuplicatePaymentMethod) {
        this.failOnDuplicatePaymentMethod = failOnDuplicatePaymentMethod;
        return this;
    }

    @Override
    public String toXML() {
        return buildRequest("options").toXML();
    }

    protected RequestBuilder buildRequest(String root) {
        return new RequestBuilder(root)
            .addElement("makeDefault", makeDefault)
            .addElement("verifyCard", verifyCard)
            .addElement("failOnDuplicatePaymentMethod", failOnDuplicatePaymentMethod);
    }
}
