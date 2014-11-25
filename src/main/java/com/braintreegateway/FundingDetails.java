package com.braintreegateway;

import com.braintreegateway.util.EnumUtils;
import com.braintreegateway.util.NodeWrapper;

public final class FundingDetails {
    private final MerchantAccount.FundingDestination destination;
    private final String email;
    private final String mobilePhone;
    private final String routingNumber;
    private final String accountNumberLast4;
    private final String descriptor;

    public FundingDetails(NodeWrapper node) {
        destination = EnumUtils.findByName(MerchantAccount.FundingDestination.class, node.findString("destination"), MerchantAccount.FundingDestination.UNRECOGNIZED);
        email = node.findString("email");
        mobilePhone = node.findString("mobile-phone");
        routingNumber = node.findString("routing-number");
        accountNumberLast4 = node.findString("account-number-last-4");
        descriptor = node.findString("descriptor");
    }

    public String getEmail() {
        return email;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public String getRoutingNumber() {
        return routingNumber;
    }

    public String getAccountNumberLast4() {
        return accountNumberLast4;
    }

    public MerchantAccount.FundingDestination getDestination() {
        return destination;
    }

    public String getDescriptor() {
        return descriptor;
    }
}
