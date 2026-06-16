package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.braintreegateway.OAuthConnectUrlRequest;
import com.braintreegateway.OAuthConnectUrlUserRequest;

public class OAuthConnectUrlUserRequestTest {

    @Test
    public void buildsQueryStringForAllFieldsAndChainsBackToParent() {
        OAuthConnectUrlRequest parent = new OAuthConnectUrlRequest();
        OAuthConnectUrlUserRequest request = new OAuthConnectUrlUserRequest(parent);

        OAuthConnectUrlRequest returned = request
                .country("USA")
                .email("dan@example.com")
                .firstName("Dan")
                .lastName("Schulman")
                .phone("3125551234")
                .dobYear("1980")
                .dobMonth("01")
                .dobDay("02")
                .streetAddress("123 Main St")
                .locality("Chicago")
                .region("IL")
                .postalCode("60622")
                .done();

        assertSame(parent, returned);

        String queryString = request.toQueryString("user");
        // Assert the full URL-encoded nested key/value pairs (%5B / %5D are the [ ] around each key).
        assertAll("user query string params",
                () -> assertTrue(queryString.contains("user%5Bcountry%5D=USA"), queryString),
                () -> assertTrue(queryString.contains("user%5Bemail%5D=dan%40example.com"), queryString),
                () -> assertTrue(queryString.contains("user%5Bfirst_name%5D=Dan"), queryString),
                () -> assertTrue(queryString.contains("user%5Blast_name%5D=Schulman"), queryString),
                () -> assertTrue(queryString.contains("user%5Bphone%5D=3125551234"), queryString),
                () -> assertTrue(queryString.contains("user%5Bdob_year%5D=1980"), queryString),
                () -> assertTrue(queryString.contains("user%5Bdob_month%5D=01"), queryString),
                () -> assertTrue(queryString.contains("user%5Bdob_day%5D=02"), queryString),
                () -> assertTrue(queryString.contains("user%5Bstreet_address%5D=123+Main+St"), queryString),
                () -> assertTrue(queryString.contains("user%5Blocality%5D=Chicago"), queryString),
                () -> assertTrue(queryString.contains("user%5Bregion%5D=IL"), queryString),
                () -> assertTrue(queryString.contains("user%5Bpostal_code%5D=60622"), queryString));
    }
}
