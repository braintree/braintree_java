package com.braintreegateway;

public class TransactionOptionsRequest extends Request {
    private Boolean addBillingAddressToPaymentMethod;
    private TransactionRequest parent;
    private Boolean storeInVault;
    private Boolean storeShippingAddressInVault;
    private Boolean submitForSettlement;

    public TransactionOptionsRequest(TransactionRequest parent) {
        this.parent = parent;
    }

    public TransactionOptionsRequest addBillingAddressToPaymentMethod(Boolean addBillingAddressToPaymentMethod) {
        this.addBillingAddressToPaymentMethod = addBillingAddressToPaymentMethod;
        return this;
    }

    public TransactionRequest done() {
        return parent;
    }

    public TransactionOptionsRequest storeInVault(Boolean storeInVault) {
        this.storeInVault = storeInVault;
        return this;
    }

    public TransactionOptionsRequest storeShippingAddressInVault(Boolean storeShippingAddressInVault) {
        this.storeShippingAddressInVault = storeShippingAddressInVault;
        return this;
    }

    public TransactionOptionsRequest submitForSettlement(Boolean submitForSettlement) {
        this.submitForSettlement = submitForSettlement;
        return this;
    }

    @Override
    public String toXML() {
        return buildRequest("options").toXML();
    }

    @Override
    public String toQueryString() {
        return toQueryString("options");
    }
    
    @Override
    public String toQueryString(String root) {
        return buildRequest(root).toQueryString();
    }
    
    protected RequestBuilder buildRequest(String root) {
        return new RequestBuilder(root).
            addElement("storeInVault", storeInVault).
            addElement("addBillingAddressToPaymentMethod", addBillingAddressToPaymentMethod).
            addElement("storeShippingAddressInVault", storeShippingAddressInVault).
            addElement("submitForSettlement", submitForSettlement);
    }
}
