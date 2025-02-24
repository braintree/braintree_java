package com.braintreegateway.integrationtest;

import com.braintreegateway.*;
import com.braintreegateway.test.Nonce;

import java.util.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AndroidPayCardIT extends IntegrationTest  {

    @Test
    public void testPrepaidReloadable() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        PaymentMethodRequest request = new PaymentMethodRequest().
            customerId(customer.getId()).
            paymentMethodNonce(Nonce.AndroidPayVisa);

        Result<? extends PaymentMethod> result = gateway.paymentMethod().create(request);
        assertTrue(result.isSuccess());

        AndroidPayCard androidPayCard = (AndroidPayCard) result.getTarget();
        assertNotNull(androidPayCard.getPrepaid());
        assertNotNull(androidPayCard.getPrepaidReloadable());

        AndroidPayCard foundAndroidPayCard = (AndroidPayCard) gateway.paymentMethod().find(androidPayCard.getToken());
        assertEquals(androidPayCard.getToken(), foundAndroidPayCard.getToken());
    }
}
