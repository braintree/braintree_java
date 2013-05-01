package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;

/**
 * An address can belong to:
 * <ul>
 * <li>a CreditCard as the billing address
 * <li>a Customer as an address
 * <li>a Transaction as a billing or shipping address
 * </ul>
 * 
 */
public class Descriptor {

    private String name;
    private String phone;

    public Descriptor(NodeWrapper node) {
        name = node.findString("name");
        phone = node.findString("phone");
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }
}
