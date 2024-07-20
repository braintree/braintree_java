package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;

// NEXT_MAJOR_VERSION remove paypalTrackingId and getPaypalTrackingId
public class PackageDetails {
    private String carrier;
    private String id;
    private String paypalTrackingId;
    private String paypalTrackerId;
    private String trackingNumber;

    public PackageDetails(NodeWrapper node) {
        carrier = node.findString("carrier");
        id = node.findString("id");
        paypalTrackerId = node.findString("paypal-tracker-id");
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

    //NEXT_MAJOR_VERSION remove this method
    /**
     * @deprecated - The getPayPalTrackingId is deprecated in favor of getPayPalTrackerId.
     */
    @Deprecated
    public String getPayPalTrackingId() {
        return paypalTrackingId;
    }

    public String getPayPalTrackerId() {
        return paypalTrackerId;
    }
}
