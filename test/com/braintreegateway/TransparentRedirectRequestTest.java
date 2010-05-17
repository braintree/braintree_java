package com.braintreegateway;

import java.net.URLEncoder;

import junit.framework.Assert;

import org.junit.Test;

import com.braintreegateway.exceptions.AuthenticationException;
import com.braintreegateway.exceptions.AuthorizationException;
import com.braintreegateway.exceptions.DownForMaintenanceException;
import com.braintreegateway.exceptions.ForgedQueryStringException;
import com.braintreegateway.exceptions.NotFoundException;
import com.braintreegateway.exceptions.ServerException;
import com.braintreegateway.exceptions.UnexpectedException;

public class TransparentRedirectRequestTest {

    @Test
    public void constructor() {
        Configuration configuration = new Configuration("baseMerchantURL", "integration_public_key", "integration_private_key");
        new TransparentRedirectRequest(configuration, "http_status=200&id=6kdj469tw7yck32j&hash=99c9ff20cd7910a1c1e793ff9e3b2d15586dc6b9");    
    }

    @Test(expected = ForgedQueryStringException.class)
    public void constructorRaisesForgedQueryStringExceptionIfGivenInvalidQueryString() {
        Configuration configuration = new Configuration("baseMerchantURL", "integration_public_key", "integration_private_key");
        new TransparentRedirectRequest(configuration, "http_status=200&id=6kdj469tw7yck32j&hash=99c9ff20cd7910a1c1e793ff9e3b2d15586dc6b9" + "this makes it invalid");    
    }
    
    @Test(expected = ServerException.class)
    public void constructorRaisesServerExceptionIfHttpStatusIs500() {
        Configuration configuration = new Configuration("baseMerchantURL", "integration_public_key", "integration_private_key");
        new TransparentRedirectRequest(configuration, "http_status=500&id=6kdj469tw7yck32j&hash=a839a44ca69d59a3d6f639c294794989676632dc");    
    }

    @Test(expected = AuthenticationException.class)
    public void constructorRaisesAuthenticationExceptionIfHttpStatusIs401() {
        Configuration configuration = new Configuration("baseMerchantURL", "integration_public_key", "integration_private_key");
        new TransparentRedirectRequest(configuration, "http_status=401&id=6kdj469tw7yck32j&hash=5a26e3cde5ebedb0ec1ba8d35724360334fbf419");    
    }

    @Test(expected = AuthorizationException.class)
    public void constructorRaisesAuthorizatonExecptionIfHttpStatusIs403() {
        Configuration configuration = new Configuration("baseMerchantURL", "integration_public_key", "integration_private_key");
        new TransparentRedirectRequest(configuration, "http_status=403&id=6kdj469tw7yck32j&hash=126d5130b71a4907e460fad23876ed70dd41dcd2");    
    }
    
    @Test
    public void constructorRaisesAuthorizatonExecptionWithMessageIfHttpStatusIs403AndMessageIsInQueryString() {
        Configuration configuration = new Configuration("baseMerchantURL", "integration_public_key", "integration_private_key");
        String message = "Invalid params: transaction[bad]";
        try {
            new TransparentRedirectRequest(configuration, String.format("bt_message=%s&http_status=403&id=6kdj469tw7yck32j&hash=126d5130b71a4907e460fad23876ed70dd41dcd2", URLEncoder.encode(message, "UTF-8")));
            Assert.fail();
        } catch (AuthorizationException e) {
            Assert.assertEquals(message, e.getMessage());
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test(expected = DownForMaintenanceException.class)
    public void constructorRaisesDownForMaintenanceExceptionIfHttpStatusIs503() {
        Configuration configuration = new Configuration("baseMerchantURL", "integration_public_key", "integration_private_key");
        new TransparentRedirectRequest(configuration, "http_status=503&id=6kdj469tw7yck32j&hash=1b3d29199a282e63074a7823b76bccacdf732da6");    
    }

    @Test(expected = NotFoundException.class)
    public void constructorRaisesNotFoundExceptionIfHttpStatusIs404() {
        Configuration configuration = new Configuration("baseMerchantURL", "integration_public_key", "integration_private_key");
        new TransparentRedirectRequest(configuration, "http_status=404&id=6kdj469tw7yck32j&hash=0d3724a45cf1cda5524aa68f1f28899d34d2ff3a");    
    }

    @Test(expected = UnexpectedException.class)
    public void constructorRaisesUnexpectedExceptionIfHttpStatusIsUnexpected() {
        Configuration configuration = new Configuration("baseMerchantURL", "integration_public_key", "integration_private_key");
        new TransparentRedirectRequest(configuration, "http_status=600&id=6kdj469tw7yck32j&hash=740633356f93384167d887de0c1d9745e3de8fb6");    
    }
}
