package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;
import com.braintreegateway.util.PaymentMethodParser;

public class PaymentMethodCustomerDataUpdatedMetadata {
    private String token;
    private PaymentMethod paymentMethod;
    private String datetimeUpdated;
    private EnrichedCustomerData enrichedCustomerData;

    public PaymentMethodCustomerDataUpdatedMetadata(NodeWrapper node) {
        this.token = node.findString("token");
        this.datetimeUpdated = node.findString("datetime-updated");

        NodeWrapper paymentMethodNode = node.findFirst("payment-method");
        this.paymentMethod = PaymentMethodParser.parsePaymentMethod(paymentMethodNode.getChildren().get(0)).getTarget();

        NodeWrapper enrichedCustomerDataNode = node.findFirst("enriched-customer-data");
        this.enrichedCustomerData = new EnrichedCustomerData(enrichedCustomerDataNode);
    }

    public String getToken() {
        return token;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public String getDatetimeUpdated() {
        return datetimeUpdated;
    }

    public EnrichedCustomerData getEnrichedCustomerData() {
        return enrichedCustomerData;
    }
 }
