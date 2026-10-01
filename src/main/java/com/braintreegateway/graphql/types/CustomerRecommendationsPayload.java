package com.braintreegateway.graphql.types;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import com.braintreegateway.exceptions.ServerException;

import com.braintreegateway.graphql.enums.RecommendedPaymentOption;
import com.braintreegateway.graphql.unions.CustomerRecommendations;
import com.braintreegateway.graphql.types.PaymentOptions;
import com.braintreegateway.util.Experimental;

/**
 * Represents the customer recommendations associated with a PayPal customer session.
 */
@Experimental("This class is experimental and may change in future releases.")
public class CustomerRecommendationsPayload {
  private static final Logger LOGGER = Logger.getLogger("Braintree");

  private final String sessionId;
  private final boolean isInPayPalNetwork;
  private final CustomerRecommendations recommendations;

  public String getSessionId() {
    return sessionId;
  }

  public boolean isInPayPalNetwork() {
      return isInPayPalNetwork;
  }

  public CustomerRecommendations getRecommendations() {
      return recommendations;
  }

  // Constructor for Map response
  public CustomerRecommendationsPayload(Map<String, Object> response) {
    this.sessionId = getValue(response, "generateCustomerRecommendations.sessionId");
    this.isInPayPalNetwork = getValue(response, "generateCustomerRecommendations.isInPayPalNetwork");
    this.recommendations = extractRecommendations(response);
  }

  private static CustomerRecommendations extractRecommendations(Map<String, Object> response) {
    try {
        List<Map<String, Object>> paymentRecommendations = 
            getValue(response, "generateCustomerRecommendations.paymentRecommendations");
    
        List<PaymentRecommendation> paymentRecommendationList = paymentRecommendations.stream()
            .map(recommendationObj -> {
                Integer recommendedPriority = getValue(recommendationObj, "recommendedPriority");
                String paymentOptionString = getValue(recommendationObj, "paymentOption");
                RecommendedPaymentOption paymentOption = parsePaymentOption(paymentOptionString);
                if (paymentOption == null) {
                    return null;
                }
                return new PaymentRecommendation(paymentOption, recommendedPriority);
            })
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
    
        return new CustomerRecommendations(paymentRecommendationList);
    } catch (Exception e) {
      throw new ServerException("Error extracting recommendations: " + e.getMessage());
    }
  }

  private static RecommendedPaymentOption parsePaymentOption(String paymentOptionString) {
    if (paymentOptionString == null || paymentOptionString.trim().isEmpty()) {
      LOGGER.log(Level.WARNING, "Skipping payment recommendation with missing payment option");
      return null;
    }
    try {
      return RecommendedPaymentOption.valueOf(paymentOptionString.trim());
    } catch (IllegalArgumentException e) {
      LOGGER.log(Level.WARNING,
          "Skipping unknown payment recommendation option: " + paymentOptionString);
      return null;
    }
  }

  private static <T> T getValue(Map<String, Object> response, String key) {
      Map<String, Object> map = response;
      String[] keyParts = key.split("\\.");
      for (int k = 0; k < keyParts.length - 1 ; k++) {
        String subKey = keyParts[k];
        map = popValue(map, subKey);
      }
      String lastKey = keyParts[keyParts.length - 1];
      return popValue(map, lastKey);
  }

  private static <T> T popValue(Map<String, Object> response, String key) {
      if (!response.containsKey(key)) {
        throw new ServerException("Couldn't parse response");
      }
      return (T) response.get(key);
  }
}