package com.braintreegateway;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Random;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.braintreegateway.exceptions.ForgedQueryStringException;
import com.braintreegateway.exceptions.NotFoundException;
import com.braintreegateway.exceptions.UnexpectedException;

public class CreditCardTest {

    private BraintreeGateway gateway;

    @Before
    public void createGateway() {
        this.gateway = new BraintreeGateway(Environment.DEVELOPMENT, "integration_merchant_id", "integration_public_key",
                "integration_private_key");
    }

    @Test
    public void transparentRedirectURLForCreate() {
        Assert.assertEquals(gateway.baseMerchantURL() + "/payment_methods/all/create_via_transparent_redirect_request",
                gateway.creditCard().transparentRedirectURLForCreate());
    }

    @Test
    public void transparentRedirectURLForUpdate() {
        Assert.assertEquals(gateway.baseMerchantURL() + "/payment_methods/all/update_via_transparent_redirect_request",
                gateway.creditCard().transparentRedirectURLForUpdate());
    }

    @Test
    public void trData() {
        String trData = gateway.trData(new CreditCardRequest(), "http://example.com");
        TestHelper.assertValidTrData(gateway.getConfiguration(), trData);
    }

    @Test
    public void create() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest request = new CreditCardRequest().
            customerId(customer.getId()).
            cardholderName("John Doe").
            cvv("123").
            number("5105105105105100").
            expirationDate("05/12");
        Result<CreditCard> result = gateway.creditCard().create(request);
        Assert.assertTrue(result.isSuccess());
        CreditCard card = result.getTarget();
        Assert.assertEquals("John Doe", card.getCardholderName());
        Assert.assertEquals("MasterCard", card.getCardType());
        Assert.assertEquals("510510", card.getBin());
        Assert.assertEquals("05", card.getExpirationMonth());
        Assert.assertEquals("2012", card.getExpirationYear());
        Assert.assertEquals("05/2012", card.getExpirationDate());
        Assert.assertEquals("5100", card.getLast4());
        Assert.assertTrue(card.getToken() != null);
        Assert.assertEquals(Calendar.getInstance().get(Calendar.YEAR), card.getCreatedAt().get(Calendar.YEAR));
        Assert.assertEquals(Calendar.getInstance().get(Calendar.YEAR), card.getUpdatedAt().get(Calendar.YEAR));
    }

    @Test
    public void createWithAddress() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest request = new CreditCardRequest().
            customerId(customer.getId()).
            billingAddress().
                streetAddress("1 E Main St").
                extendedAddress("Unit 2").
                locality("Chicago").
                region("Illinois").
                postalCode("60607").
                countryName("United States of America").
                done().
            cardholderName("John Doe").
            cvv("123").
            number("5105105105105100").
            expirationDate("05/12");
        Result<CreditCard> result = gateway.creditCard().create(request);
        Assert.assertTrue(result.isSuccess());
        CreditCard card = result.getTarget();

        Address billingAddress = card.getBillingAddress();
        Assert.assertEquals("1 E Main St", billingAddress.getStreetAddress());
        Assert.assertEquals("Unit 2", billingAddress.getExtendedAddress());
        Assert.assertEquals("Chicago", billingAddress.getLocality());
        Assert.assertEquals("Illinois", billingAddress.getRegion());
        Assert.assertEquals("60607", billingAddress.getPostalCode());
        Assert.assertEquals("United States of America", billingAddress.getCountryName());
    }

    @Test
    public void createViaTransparentRedirect() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();

        CreditCardRequest trParams = new CreditCardRequest().customerId(customer.getId());

        CreditCardRequest request = new CreditCardRequest().
            cardholderName("John Doe").
            number("5105105105105100").
            expirationDate("05/12");

        String queryString = creditCardViaTR(trParams, request, gateway.creditCard().transparentRedirectURLForCreate());
        Result<CreditCard> result = gateway.creditCard().confirmTransparentRedirect(queryString);
        Assert.assertTrue(result.isSuccess());
        CreditCard card = result.getTarget();
        Assert.assertEquals("John Doe", card.getCardholderName());
        Assert.assertEquals("510510", card.getBin());
        Assert.assertEquals("05", card.getExpirationMonth());
        Assert.assertEquals("2012", card.getExpirationYear());
        Assert.assertEquals("05/2012", card.getExpirationDate());
        Assert.assertEquals("5100", card.getLast4());
        Assert.assertTrue(card.getToken() != null);
    }

    @Test(expected = ForgedQueryStringException.class)
    public void createViaTransparentRedirectThrowsWhenQueryStringHasBeenTamperedWith() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest trParams = new CreditCardRequest().customerId(customer.getId());
        
        String queryString = creditCardViaTR(trParams, new CreditCardRequest(), gateway.creditCard().transparentRedirectURLForCreate());
        gateway.creditCard().confirmTransparentRedirect(queryString + "this makes it invalid");
    }
    
    @Test
    public void update() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest request = new CreditCardRequest().
            customerId(customer.getId()).
            cardholderName("John Doe").
            cvv("123").
            number("5105105105105100").
            expirationDate("05/12");
        Result<CreditCard> result = gateway.creditCard().create(request);
        Assert.assertTrue(result.isSuccess());
        CreditCard card = result.getTarget();

        CreditCardRequest updateRequest = new CreditCardRequest().
            customerId(customer.getId()).
            cardholderName("Jane Jones").
            cvv("321").
            number("4111111111111111").
            expirationDate("12/05");

        Result<CreditCard> updateResult = gateway.creditCard().update(card.getToken(), updateRequest);
        Assert.assertTrue(updateResult.isSuccess());
        CreditCard updatedCard = updateResult.getTarget();

        Assert.assertEquals("Jane Jones", updatedCard.getCardholderName());
        Assert.assertEquals("411111", updatedCard.getBin());
        Assert.assertEquals("12", updatedCard.getExpirationMonth());
        Assert.assertEquals("2005", updatedCard.getExpirationYear());
        Assert.assertEquals("12/2005", updatedCard.getExpirationDate());
        Assert.assertEquals("1111", updatedCard.getLast4());
        Assert.assertTrue(updatedCard.getToken() != card.getToken());
    }

    @Test
    public void updateViaTransparentRedirect() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest createRequest = new CreditCardRequest().
            customerId(customer.getId()).
            cardholderName("John Doe").
            cvv("123").
            number("5105105105105100").
            expirationDate("05/12");
        CreditCard card = gateway.creditCard().create(createRequest).getTarget();

        CreditCardRequest trParams = new CreditCardRequest().
            paymentMethodToken(card.getToken());

        CreditCardRequest request = new CreditCardRequest().
            cardholderName("joe cool");

        String queryString = creditCardViaTR(trParams, request, gateway.creditCard().transparentRedirectURLForUpdate());
        Result<CreditCard> result = gateway.creditCard().confirmTransparentRedirect(queryString);
        Assert.assertTrue(result.isSuccess());
        CreditCard updatedCard = result.getTarget();
        Assert.assertEquals("joe cool", updatedCard.getCardholderName());
    }

    @Test
    public void updateToken() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest request = new CreditCardRequest().
            customerId(customer.getId()).
            cardholderName("John Doe").
            cvv("123").
            number("5105105105105100").
            expirationDate("05/12");
        Result<CreditCard> result = gateway.creditCard().create(request);
        Assert.assertTrue(result.isSuccess());
        CreditCard card = result.getTarget();

        String newToken = String.valueOf(new Random().nextInt());
        CreditCardRequest updateRequest = new CreditCardRequest().customerId(customer.getId()).token(newToken);

        Result<CreditCard> updateResult = gateway.creditCard().update(card.getToken(), updateRequest);
        Assert.assertTrue(updateResult.isSuccess());
        CreditCard updatedCard = updateResult.getTarget();

        Assert.assertEquals(newToken, updatedCard.getToken());
    }

    @Test
    public void updateOnlySomeAttributes() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest request = new CreditCardRequest().
            customerId(customer.getId()).
            cardholderName("John Doe").
            cvv("123").
            number("5105105105105100").
            expirationDate("05/12");
        Result<CreditCard> result = gateway.creditCard().create(request);
        Assert.assertTrue(result.isSuccess());
        CreditCard card = result.getTarget();

        CreditCardRequest updateRequest = new CreditCardRequest().cardholderName("Jane Jones");

        Result<CreditCard> updateResult = gateway.creditCard().update(card.getToken(), updateRequest);
        Assert.assertTrue(updateResult.isSuccess());
        CreditCard updatedCard = updateResult.getTarget();

        Assert.assertEquals("Jane Jones", updatedCard.getCardholderName());
        Assert.assertEquals("510510", updatedCard.getBin());
        Assert.assertEquals("05", updatedCard.getExpirationMonth());
        Assert.assertEquals("2012", updatedCard.getExpirationYear());
        Assert.assertEquals("05/2012", updatedCard.getExpirationDate());
        Assert.assertEquals("5100", updatedCard.getLast4());
    }

    @Test
    public void find() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest request = new CreditCardRequest().
            customerId(customer.getId()).
            cardholderName("John Doe").
            cvv("123").
            number("5105105105105100").
            expirationDate("05/12");
        Result<CreditCard> result = gateway.creditCard().create(request);
        Assert.assertTrue(result.isSuccess());
        CreditCard card = result.getTarget();

        CreditCard found = gateway.creditCard().find(card.getToken());

        Assert.assertEquals("John Doe", found.getCardholderName());
        Assert.assertEquals("510510", found.getBin());
        Assert.assertEquals("05", found.getExpirationMonth());
        Assert.assertEquals("2012", found.getExpirationYear());
        Assert.assertEquals("05/2012", found.getExpirationDate());
        Assert.assertEquals("5100", found.getLast4());
    }

    @Test(expected = NotFoundException.class)
    public void findWithBadToken() {
        gateway.creditCard().find("badToken");
    }

    @Test
    public void delete() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest request = new CreditCardRequest().
            customerId(customer.getId()).
            cardholderName("John Doe").
            cvv("123").
            number("5105105105105100").
            expirationDate("05/12");
        Result<CreditCard> result = gateway.creditCard().create(request);
        Assert.assertTrue(result.isSuccess());
        CreditCard card = result.getTarget();

        Result<CreditCard> deleteResult = gateway.creditCard().delete(card.getToken());
        Assert.assertTrue(deleteResult.isSuccess());

        try {
            gateway.creditCard().find(card.getToken());
            Assert.fail();
        } catch (NotFoundException e) {
        }
    }

    @Test
    public void verifyValidCreditCard() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest request = new CreditCardRequest().
            customerId(customer.getId()).
            cardholderName("John Doe").
            cvv("123").
            number("4111111111111111").
            expirationDate("05/12").
            options().
                verifyCard("true").
                done();

        Result<CreditCard> result = gateway.creditCard().create(request);
        Assert.assertTrue(result.isSuccess());
    }

    @Test
    public void verifyInvalidCreditCard() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest request = new CreditCardRequest().
            customerId(customer.getId()).
            cardholderName("John Doe").
            cvv("123").
            number("5105105105105100").
            expirationDate("05/12").
            options().
                verifyCard("true").
                done();

        Result<CreditCard> result = gateway.creditCard().create(request);
        Assert.assertFalse(result.isSuccess());
        CreditCardVerification verification = result.getCreditCardVerification();
        Assert.assertEquals("processor_declined", verification.getStatus());
    }

    private String creditCardViaTR(Request trParams, CreditCardRequest request, String redirectURL) {
        String response = "";
        try {
            String trData = gateway.trData(trParams, "http://example.com");
            String postData = "tr_data=" + URLEncoder.encode(trData, "UTF-8") + "&";
            postData += request.toQueryString();

            URL url = new URL(redirectURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.addRequestProperty("Accept", "application/xml");
            connection.addRequestProperty("User-Agent", "Braintree Java");
            connection.addRequestProperty("X-ApiVersion", "1");
            connection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.getOutputStream().write(postData.getBytes("UTF-8"));
            connection.getOutputStream().close();
            if (connection.getResponseCode() == 422) {
                connection.getErrorStream();
            } else {
                connection.getInputStream();
            }
            response = connection.getURL().getQuery();
        } catch (IOException e) {
            throw new UnexpectedException(e.getMessage());
        }

        return response;
    }
}
