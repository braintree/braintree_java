package com.braintreegateway;

public interface PaymentMethod {

    String getToken();
    boolean isDefault();
    String getImageUrl();
}
