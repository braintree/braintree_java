package com.braintreegateway;

import java.util.ArrayList;
import java.util.List;

public class PackageTrackingRequest extends Request {

    private String carrier;
    private List<TransactionLineItemRequest> lineItems;
    private Boolean notifyPayer;
    private String trackingNumber;

    public PackageTrackingRequest() {
        lineItems = new ArrayList<TransactionLineItemRequest>();
    }

    /**
     * Tracking Carrier Enum -
     * https://developer.paypal.com/docs/tracking/reference/carriers/
     */
    public PackageTrackingRequest carrier(String carrier) {
        this.carrier = carrier;
        return this;
    }

    public PackageTrackingRequest trackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
        return this;
    }

    public PackageTrackingRequest addLineItem(TransactionLineItemRequest lineItemRequest) {
        this.lineItems.add(lineItemRequest);
        return this;
    }

    public PackageTrackingRequest notifyPayer(Boolean notifyPayer) {
        this.notifyPayer = notifyPayer;
        return this;
    }

    @Override
    public String toXML() {
        return buildRequest("shipment").toXML();
    }

    protected RequestBuilder buildRequest(String root) {
        RequestBuilder builder = new RequestBuilder(root)
                .addElement("trackingNumber", trackingNumber)
                .addElement("carrier", carrier)
                .addElement("notifyPayer", notifyPayer);

        if (!lineItems.isEmpty()) {
            builder.addElement("lineItems", lineItems);
        }

        return builder;
    }

}
