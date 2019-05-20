package com.braintreegateway;

import java.util.Map;
import com.braintreegateway.util.NodeWrapper;

public class PaymentMethodNonce {
    private Boolean isDefault;
    private String publicId;
    private Boolean isLocked;
    private Boolean isConsumed;
    private PaymentMethodNonceDetails details;
    private ThreeDSecureInfo threeDSecureInfo;
    private String type;
    private BinData binData;

    public PaymentMethodNonce(NodeWrapper node) {
        isDefault = node.findBoolean("default");
        publicId = node.findString("nonce");
        isLocked = node.findBoolean("locked");
        isConsumed = node.findBoolean("consumed");
        type = node.findString("type");

        NodeWrapper detailsNode = node.findFirst("details");
        if (detailsNode != null && !detailsNode.isBlank()) {
            details = new PaymentMethodNonceDetails(detailsNode);
        }

        NodeWrapper threeDSecureInfoNode = node.findFirst("three-d-secure-info");
        if (threeDSecureInfoNode != null && !threeDSecureInfoNode.isBlank()) {
            threeDSecureInfo = new ThreeDSecureInfo(threeDSecureInfoNode);
        }

        NodeWrapper binDataNode = node.findFirst("bin-data");
        if (binDataNode != null && !binDataNode.isBlank()) {
            binData = new BinData(binDataNode);
        }
    }

    public PaymentMethodNonce(Map<String, Object> map) {
        isDefault = (Boolean) map.get("default");
        publicId = (String) map.get("nonce");
        isLocked = (Boolean) map.get("locked");
        isConsumed = (Boolean) map.get("consumed");
        type = (String) map.get("type");

        Map<String, String> detailsMap = (Map) map.get("details");
        if (detailsMap != null) {
            details = new PaymentMethodNonceDetails(detailsMap);
        }

        Map<String, Object> threeDSecureInfoMap = (Map) map.get("threeDSecureInfo");
        if (threeDSecureInfoMap != null) {
            threeDSecureInfo = new ThreeDSecureInfo(threeDSecureInfoMap);
        }

        Map<String, String> binDataMap = (Map) map.get("binData");
        if (binDataMap != null) {
            binData = new BinData(binDataMap);
        }
    }

    public Boolean isDefault() {
        return isDefault;
    }

    public String getPublicId() {
        return publicId;
    }

    public String getNonce() {
        return getPublicId();
    }

    public Boolean isLocked() {
        return isLocked;
    }

    public Boolean isConsumed() {
        return isConsumed;
    }

    public PaymentMethodNonceDetails getDetails() {
        return details;
    }

    public ThreeDSecureInfo getThreeDSecureInfo() {
        return threeDSecureInfo;
    }

    public String getType() {
        return type;
    }

    public BinData getBinData() {
        return binData;
    }
}
