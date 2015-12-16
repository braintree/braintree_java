package com.braintreegateway.unittest;

import com.braintreegateway.*;
import com.braintreegateway.testhelpers.TestHelper;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class BraintreeGatewayTest {

    @Test
    public void testStringEnvironmentConstructor() {
       BraintreeGateway gateway = new BraintreeGateway("development", "merchant_id", "public_key", "private_key");
       assertEquals(Environment.DEVELOPMENT, gateway.getConfiguration().getEnvironment());
    }
}
