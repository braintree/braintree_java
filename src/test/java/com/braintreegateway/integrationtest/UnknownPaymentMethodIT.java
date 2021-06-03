package com.braintreegateway.integrationtest;

import com.braintreegateway.*;
import com.braintreegateway.util.NodeWrapper;
import java.util.Collections;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UnknownPaymentMethodIT extends IntegrationTestNew {

    @Test
    public void findReturnsNoSubscriptionsAssociatedWithUnknownPaymentMethod() {
        NodeWrapper wrapper = mock(NodeWrapper.class);
        UnknownPaymentMethod unknownPaymentMethod = new UnknownPaymentMethod(wrapper);

        PaymentMethodGateway gateway = mock(PaymentMethodGateway.class);
        when(gateway.find("token")).thenReturn(unknownPaymentMethod);

        PaymentMethod foundPaymentMethod = gateway.find("token");
        assertEquals(foundPaymentMethod.getSubscriptions(), Collections.EMPTY_LIST);
    }
}
