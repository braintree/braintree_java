package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import com.braintreegateway.CreditCard;
import com.braintreegateway.CreditCardGateway;
import com.braintreegateway.Configuration;
import com.braintreegateway.Environment;
import com.braintreegateway.ResourceCollection;
import com.braintreegateway.util.Http;
import com.braintreegateway.util.SimpleNodeWrapper;

public class ExpiredCreditCardPagerTest {

    @Test
    public void expiredReturnedResourceCollectionWiredToExpiredCreditCardPager() {
        Http http = mock(Http.class);
        Configuration configuration = new Configuration(
                Environment.DEVELOPMENT, "integration_merchant_id", "public_key", "private_key");
        CreditCardGateway gateway = new CreditCardGateway(http, configuration);
        String merchantPath = configuration.getMerchantPath();

        // expired() creates a ResourceCollection backed by ExpiredCreditCardPager, covering the constructor path.
        when(http.post(merchantPath + "/payment_methods/all/expired_ids")).thenReturn(
                SimpleNodeWrapper.parse(
                        "<ids>" +
                        "<page-size type=\"integer\">50</page-size>" +
                        "<ids type=\"array\"></ids>" +
                        "</ids>"));

        ResourceCollection<CreditCard> collection = gateway.expired();

        assertEquals(0, collection.getMaximumSize());
        verify(http).post(merchantPath + "/payment_methods/all/expired_ids");
    }
}
