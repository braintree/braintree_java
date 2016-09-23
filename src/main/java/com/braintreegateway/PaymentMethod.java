package com.braintreegateway;

import java.util.List;

public interface PaymentMethod {

    String getToken();
    boolean isDefault();
    String getImageUrl();

    String getCustomerId();
    List<Subscription> getSubscriptions();
}
