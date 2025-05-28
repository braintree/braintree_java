package com.braintreegateway.graphql.enums;

import com.braintreegateway.util.Experimental;

/**
 * Represents available payment options related to PayPal customer session recommendations.
 */
@Experimental("This class is experimental and may change in future releases.")  
public enum RecommendedPaymentOption {
  PAYPAL,
  VENMO
}
