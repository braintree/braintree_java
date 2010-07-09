package com.braintreegateway;

public class TransactionOptionsRequest extends Request {
    private boolean addBillingAddressToPaymentMethod;
    private TransactionRequest parent;
    private boolean storeInVault;
    private boolean storeShippingAddressInVault;
    private boolean submitForSettlement;

    public TransactionOptionsRequest(TransactionRequest parent) {
        this.parent = parent;
    }

    public TransactionOptionsRequest addBillingAddressToPaymentMethod(boolean addBillingAddressToPaymentMethod) {
        this.addBillingAddressToPaymentMethod = addBillingAddressToPaymentMethod;
        return this;
    }

    public TransactionRequest done() {
        return parent;
    }

    public TransactionOptionsRequest storeInVault(boolean storeInVault) {
        this.storeInVault = storeInVault;
        return this;
    }

    public TransactionOptionsRequest storeShippingAddressInVault(boolean storeShippingAddressInVault) {
        this.storeShippingAddressInVault = storeShippingAddressInVault;
        return this;
    }

    public TransactionOptionsRequest submitForSettlement(boolean submitForSettlement) {
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
