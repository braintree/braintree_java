package com.braintreegateway;

import com.braintreegateway.util.QueryString;

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
        StringBuilder builder = new StringBuilder();
        builder.append("<options>");
        builder.append(buildXMLElement("storeInVault", storeInVault));
        builder.append(buildXMLElement("addBillingAddressToPaymentMethod", addBillingAddressToPaymentMethod));
        builder.append(buildXMLElement("storeShippingAddressInVault", storeShippingAddressInVault));
        builder.append(buildXMLElement("submitForSettlement", submitForSettlement));
        builder.append("</options>");
        return builder.toString();
    }

    @Override
    public String toQueryString(String root) {
        return new QueryString().
            append(parentBracketChildString(root, "store_in_vault"), storeInVault).
            append(parentBracketChildString(root, "add_billing_address_to_payment_method"), addBillingAddressToPaymentMethod).
            append(parentBracketChildString(root, "store_shipping_address_in_vault"), storeShippingAddressInVault).
            append(parentBracketChildString(root, "submit_for_settlement"), submitForSettlement).
            toString();
    }

    @Override
    public String toQueryString() {
        return toQueryString("options");
    }
}
