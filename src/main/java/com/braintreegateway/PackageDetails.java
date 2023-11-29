package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;

public class PackageDetails {
    private String carrier;
    private String id;
    private String paypalTrackingId;
    private String trackingNumber;

    public PackageDetails(NodeWrapper node) {
        carrier = node.findString("carrier");
        id = node.findString("id");
        paypalTrackingId = node.findString("paypal-tracking-id");
        trackingNumber = node.findString("tracking-number");
    }

    public String getId() {
        return id;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public String getCarrier() {
        return carrier;
    }

    public String getPayPalTrackingId() {
        return paypalTrackingId;
    }
}
