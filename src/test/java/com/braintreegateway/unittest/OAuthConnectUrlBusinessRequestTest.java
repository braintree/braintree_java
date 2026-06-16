package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.braintreegateway.OAuthConnectUrlBusinessRequest;
import com.braintreegateway.OAuthConnectUrlRequest;

public class OAuthConnectUrlBusinessRequestTest {

    @Test
    public void buildsQueryStringForAllFieldsAndChainsBackToParent() {
        OAuthConnectUrlRequest parent = new OAuthConnectUrlRequest();
        OAuthConnectUrlBusinessRequest request = new OAuthConnectUrlBusinessRequest(parent);

        OAuthConnectUrlRequest returned = request
                .name("Braintree")
                .registeredAs("Braintree LLC")
                .industry("ecommerce")
                .description("payments")
                .streetAddress("123 Main St")
                .locality("Chicago")
                .region("IL")
                .postalCode("60622")
                .country("USA")
                .annualVolumeAmount("1000000")
                .averageTransactionAmount("100")
                .maximumTransactionAmount("10000")
                .shipPhysicalGoods(true)
                .fulfillmentCompletedIn(5)
                .currency("USD")
                .website("https://example.com")
                .establishedOn("2010-01")
                .done();

        assertSame(parent, returned);

        String queryString = request.toQueryString("business");
        // Assert the full URL-encoded nested key/value pairs (%5B / %5D are the [ ] around each key).
        assertAll("business query string params",
                () -> assertTrue(queryString.contains("business%5Bname%5D=Braintree"), queryString),
                () -> assertTrue(queryString.contains("business%5Bregistered_as%5D=Braintree+LLC"), queryString),
                () -> assertTrue(queryString.contains("business%5Bindustry%5D=ecommerce"), queryString),
                () -> assertTrue(queryString.contains("business%5Bdescription%5D=payments"), queryString),
                () -> assertTrue(queryString.contains("business%5Bstreet_address%5D=123+Main+St"), queryString),
                () -> assertTrue(queryString.contains("business%5Blocality%5D=Chicago"), queryString),
                () -> assertTrue(queryString.contains("business%5Bregion%5D=IL"), queryString),
                () -> assertTrue(queryString.contains("business%5Bpostal_code%5D=60622"), queryString),
                () -> assertTrue(queryString.contains("business%5Bcountry%5D=USA"), queryString),
                () -> assertTrue(queryString.contains("business%5Bannual_volume_amount%5D=1000000"), queryString),
                () -> assertTrue(queryString.contains("business%5Baverage_transaction_amount%5D=100"), queryString),
                () -> assertTrue(queryString.contains("business%5Bmaximum_transaction_amount%5D=10000"), queryString),
                () -> assertTrue(queryString.contains("business%5Bship_physical_goods%5D=true"), queryString),
                () -> assertTrue(queryString.contains("business%5Bfulfillment_completed_in%5D=5"), queryString),
                () -> assertTrue(queryString.contains("business%5Bcurrency%5D=USD"), queryString),
                () -> assertTrue(queryString.contains("business%5Bwebsite%5D=https%3A%2F%2Fexample.com"), queryString),
                () -> assertTrue(queryString.contains("business%5Bestablished_on%5D=2010-01"), queryString));
    }
}
