package com.braintreegateway.integrationtest;

import com.braintreegateway.*;
import com.braintreegateway.util.NodeWrapper;
import org.junit.Test;
import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UnknownPaymentMethodIT extends IntegrationTest {

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
