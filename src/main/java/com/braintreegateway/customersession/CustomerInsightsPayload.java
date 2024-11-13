package com.braintreegateway.customersession;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CustomerInsightsPayload {
    private Boolean isInPayPalNetwork;
    private CustomerInsights insights;

    public CustomerInsightsPayload isInPayPalNetwork(Boolean isInPayPalNetwork) {
        this.isInPayPalNetwork = isInPayPalNetwork;
        return this;
    }

    public CustomerInsightsPayload insights(CustomerInsights insights) {
        this.insights = insights;
        return this;
    }

    public CustomerInsightsPayload(Map<String, Object> data) {
        Map<String, Object> customerInsights = (Map<String, Object>)data.get("customerInsights");
        this.isInPayPalNetwork = (Boolean) customerInsights.get("isInPayPalNetwork");
        this.insights = new CustomerInsights()
                .paymentRecommendations(
                        getPaymentRecommendations(
                                (Map<String, Object>) customerInsights.get("insights")
                        )
                );
    }

    private static List<PaymentRecommendation> getPaymentRecommendations(Map<String, Object> insightObj) {
        List<Map<String, Object>> recommendationObjs = (List<Map<String, Object>>) insightObj.get("paymentRecommendations");

        List<PaymentRecommendation> paymentRecommendations = recommendationObjs.stream().map( recommendationObj -> {
            Integer recommendedPriority = (Integer) recommendationObj.get("recommendedPriority");
            InsightPaymentOption paymentOption = InsightPaymentOption.valueOf((String) recommendationObj.get("paymentOption"));
            return new PaymentRecommendation()
                    .recommendedPriority(recommendedPriority)
                    .paymentOption(paymentOption);
        }).collect(Collectors.toList());
        
        return paymentRecommendations;
    }

    public Boolean isInPayPalNetwork() {
        return isInPayPalNetwork;
    }

    public CustomerInsights getInsights() {
        return insights;
    }
}
