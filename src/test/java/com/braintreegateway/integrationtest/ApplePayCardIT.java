package com.braintreegateway.integrationtest;

import com.braintreegateway.*;
import com.braintreegateway.test.Nonce;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ApplePayCardIT extends IntegrationTest {
    
    @Test
    public void testBinFields() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        PaymentMethodRequest request = new PaymentMethodRequest().
            customerId(customer.getId()).
            paymentMethodNonce(Nonce.ApplePayVisa);

        Result<? extends PaymentMethod> result = gateway.paymentMethod().create(request);
        assertTrue(result.isSuccess());

        ApplePayCard applePayCard = (ApplePayCard) result.getTarget();
        
        assertNotNull(applePayCard.getPrepaid());
        assertNotNull(applePayCard.getPrepaidReloadable());
        assertNotNull(applePayCard.getBusiness());
        assertNotNull(applePayCard.getConsumer());
        assertNotNull(applePayCard.getCorporate());
        assertNotNull(applePayCard.getPurchase());

        ApplePayCard foundApplePayCard = (ApplePayCard) gateway.paymentMethod().find(applePayCard.getToken());
        assertEquals(applePayCard.getToken(), foundApplePayCard.getToken());
    }
}
