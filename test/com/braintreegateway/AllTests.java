package com.braintreegateway;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.braintreegateway.util.CryptoTest;
import com.braintreegateway.util.EnumUtilsTest;
import com.braintreegateway.util.HttpTest;
import com.braintreegateway.util.NodeWrapperTest;
import com.braintreegateway.util.QueryStringTest;
import com.braintreegateway.util.StringUtilsTest;
import com.braintreegateway.util.TrUtilTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    AddressTest.class,
    BraintreeGatewayTest.class,
    CreditCardRequestTest.class,
    CreditCardTest.class,
    CreditCardVerificationTest.class,
    CryptoTest.class,
    CustomerTest.class,
    EnumUtilsTest.class,
    HttpTest.class,
    NodeWrapperTest.class,
    QueryStringTest.class,
    ResourceCollectionTest.class,
    StringUtilsTest.class,
    SubscriptionSearchRequestTest.class,
    SubscriptionTest.class,
    TrUtilTest.class,
    TransactionRequestTest.class,
    TransactionTest.class,
    TransparentRedirectRequestTest.class,
    TransparentRedirectTest.class,
    ValidationErrorCodeTest.class,
    ValidationErrorsTest.class
})
public class AllTests {
}
