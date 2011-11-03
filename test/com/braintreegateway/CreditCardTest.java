package com.braintreegateway;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.braintreegateway.exceptions.ForgedQueryStringException;
import com.braintreegateway.exceptions.NotFoundException;

public class CreditCardTest {

    private BraintreeGateway gateway;

    @Before
    public void createGateway() {
        this.gateway = new BraintreeGateway(Environment.DEVELOPMENT, "integration_merchant_id", "integration_public_key",
                "integration_private_key");
    }

    @SuppressWarnings("deprecation")
    @Test
    public void transparentRedirectURLForCreate() {
        Assert.assertEquals(gateway.baseMerchantURL() + "/payment_methods/all/create_via_transparent_redirect_request",
                gateway.creditCard().transparentRedirectURLForCreate());
    }

    @SuppressWarnings("deprecation")
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
        Assert.assertEquals(customer.getId(), card.getCustomerId());
        Assert.assertEquals("US", card.getCustomerLocation());
        Assert.assertEquals("510510", card.getBin());
        Assert.assertEquals("05", card.getExpirationMonth());
        Assert.assertEquals("2012", card.getExpirationYear());
        Assert.assertEquals("05/2012", card.getExpirationDate());
        Assert.assertEquals("5100", card.getLast4());
        Assert.assertEquals("510510******5100", card.getMaskedNumber());
        Assert.assertTrue(card.getToken() != null);
        Assert.assertEquals(Calendar.getInstance().get(Calendar.YEAR), card.getCreatedAt().get(Calendar.YEAR));
        Assert.assertEquals(Calendar.getInstance().get(Calendar.YEAR), card.getUpdatedAt().get(Calendar.YEAR));
    }

    @Test
    public void createWithXmlCharacters() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest request = new CreditCardRequest().
            customerId(customer.getId()).
            cardholderName("Special Chars <>&\"'").
            number("5105105105105100").
            expirationDate("05/12");
        Result<CreditCard> result = gateway.creditCard().create(request);
        Assert.assertTrue(result.isSuccess());
        CreditCard card = result.getTarget();
        Assert.assertEquals("Special Chars <>&\"'", card.getCardholderName());
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
                countryCodeAlpha2("US").
                countryCodeAlpha3("USA").
                countryCodeNumeric("840").
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
        Assert.assertEquals("US", billingAddress.getCountryCodeAlpha2());
        Assert.assertEquals("USA", billingAddress.getCountryCodeAlpha3());
        Assert.assertEquals("840", billingAddress.getCountryCodeNumeric());
    }

    @Test
    public void createWithExistingAddress() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        Address address = gateway.address().create(customer.getId(), new AddressRequest().postalCode("11111")).getTarget();
        CreditCardRequest request = new CreditCardRequest().
            customerId(customer.getId()).
            billingAddressId(address.getId()).
            cardholderName("John Doe").
            cvv("123").
            number("5105105105105100").
            expirationDate("05/12");
        Result<CreditCard> result = gateway.creditCard().create(request);
        Assert.assertTrue(result.isSuccess());
        CreditCard card = result.getTarget();

        Address billingAddress = card.getBillingAddress();
        Assert.assertEquals(address.getId(), billingAddress.getId());
        Assert.assertEquals("11111", billingAddress.getPostalCode());
    }

    @SuppressWarnings("deprecation")
    @Test
    public void createViaTransparentRedirect() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();

        CreditCardRequest trParams = new CreditCardRequest().customerId(customer.getId());

        CreditCardRequest request = new CreditCardRequest().
            cardholderName("John Doe").
            number("5105105105105100").
            expirationDate("05/12");

        String queryString = TestHelper.simulateFormPostForTR(gateway, trParams, request, gateway.creditCard().transparentRedirectURLForCreate());
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
    
    
    @Test
    public void createCreditCardFromTransparentRedirectWithCountry() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest request = new CreditCardRequest();
        CreditCardRequest trParams = new CreditCardRequest().
            customerId(customer.getId()).
            number("4111111111111111").
            expirationDate("10/10").
            billingAddress().
                countryName("Aruba").
                countryCodeAlpha2("AW").
                countryCodeAlpha3("ABW").
                countryCodeNumeric("533").
                done();
        String queryString = TestHelper.simulateFormPostForTR(gateway, trParams, request, gateway.transparentRedirect().url());
        
        Result<CreditCard> result = gateway.transparentRedirect().confirmCreditCard(queryString);
        
        Assert.assertTrue(result.isSuccess());
        Assert.assertEquals("411111", result.getTarget().getBin());
        Assert.assertEquals("1111", result.getTarget().getLast4());
        Assert.assertEquals("10/2010", result.getTarget().getExpirationDate());
        Assert.assertEquals("Aruba", result.getTarget().getBillingAddress().getCountryName());
        Assert.assertEquals("AW", result.getTarget().getBillingAddress().getCountryCodeAlpha2());
        Assert.assertEquals("ABW", result.getTarget().getBillingAddress().getCountryCodeAlpha3());
        Assert.assertEquals("533", result.getTarget().getBillingAddress().getCountryCodeNumeric());
    }

    @SuppressWarnings("deprecation")
    @Test
    public void createViaTransparentRedirectWithMakeDefaultFlagInTRParams() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();

        CreditCardRequest request1 = new CreditCardRequest().
            customerId(customer.getId()).
            number("5105105105105100").
            expirationDate("05/12");

        gateway.creditCard().create(request1);

        CreditCardRequest trParams = new CreditCardRequest().
            customerId(customer.getId()).
            options().
                makeDefault(true).
                done();

        CreditCardRequest request2 = new CreditCardRequest().
            number("5105105105105100").
            expirationDate("05/12");

        String queryString = TestHelper.simulateFormPostForTR(gateway, trParams, request2, gateway.creditCard().transparentRedirectURLForCreate());
        CreditCard card = gateway.creditCard().confirmTransparentRedirect(queryString).getTarget();
        Assert.assertTrue(card.isDefault());
    }

    @SuppressWarnings("deprecation")
    @Test
    public void createViaTransparentRedirectWithMakeDefaultFlagInRequest() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();

        CreditCardRequest request1 = new CreditCardRequest().
            customerId(customer.getId()).
            number("5105105105105100").
            expirationDate("05/12");

        gateway.creditCard().create(request1);

        CreditCardRequest trParams = new CreditCardRequest().
            customerId(customer.getId());

        CreditCardRequest request2 = new CreditCardRequest().
            number("5105105105105100").
            expirationDate("05/12").
            options().
                makeDefault(true).
                done();

        String queryString = TestHelper.simulateFormPostForTR(gateway, trParams, request2, gateway.creditCard().transparentRedirectURLForCreate());
        CreditCard card = gateway.creditCard().confirmTransparentRedirect(queryString).getTarget();
        Assert.assertTrue(card.isDefault());
    }
    
    @Test
    public void createCreditCardFromTransparentRedirectWithInconsistentCountries() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest request = new CreditCardRequest();
        CreditCardRequest trParams = new CreditCardRequest().
            customerId(customer.getId()).
            number("4111111111111111").
            expirationDate("10/10").
            billingAddress().
                countryName("Aruba").
                countryCodeAlpha2("US").
                done();
        String queryString = TestHelper.simulateFormPostForTR(gateway, trParams, request, gateway.transparentRedirect().url());
        
        Result<CreditCard> result = gateway.transparentRedirect().confirmCreditCard(queryString);
        
        Assert.assertFalse(result.isSuccess());
        Assert.assertEquals(ValidationErrorCode.ADDRESS_INCONSISTENT_COUNTRY, result.getErrors().forObject("creditCard").forObject("billingAddress").onField("base").get(0).getCode());
    }

    @SuppressWarnings("deprecation")
    @Test(expected = ForgedQueryStringException.class)
    public void createViaTransparentRedirectThrowsWhenQueryStringHasBeenTamperedWith() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest trParams = new CreditCardRequest().customerId(customer.getId());
        
        String queryString = TestHelper.simulateFormPostForTR(gateway, trParams, new CreditCardRequest(), gateway.creditCard().transparentRedirectURLForCreate());
        gateway.creditCard().confirmTransparentRedirect(queryString + "this makes it invalid");
    }
    
    @Test 
    public void createWithDefaultFlag() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();

        CreditCardRequest request1 = new CreditCardRequest().
            customerId(customer.getId()).
            cardholderName("John Doe").
            number("5105105105105100").
            expirationDate("05/12");

        CreditCardRequest request2 = new CreditCardRequest().
            customerId(customer.getId()).
            cardholderName("John Doe").
            number("5105105105105100").
            expirationDate("05/12").
            options().
                makeDefault(true).
                done();

        CreditCard card1 = gateway.creditCard().create(request1).getTarget();
        CreditCard card2 = gateway.creditCard().create(request2).getTarget();
        
        Assert.assertFalse(gateway.creditCard().find(card1.getToken()).isDefault());
        Assert.assertTrue(gateway.creditCard().find(card2.getToken()).isDefault());
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
            expirationDate("12/05").
            billingAddress().
                countryName("Italy").
                countryCodeAlpha2("IT").
                countryCodeAlpha3("ITA").
                countryCodeNumeric("380").
                done();
                

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

        Assert.assertEquals("Italy", updatedCard.getBillingAddress().getCountryName());
        Assert.assertEquals("IT", updatedCard.getBillingAddress().getCountryCodeAlpha2());
        Assert.assertEquals("ITA", updatedCard.getBillingAddress().getCountryCodeAlpha3());
        Assert.assertEquals("380", updatedCard.getBillingAddress().getCountryCodeNumeric());
    }

    @Test
    public void updateWithDefaultFlag() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest request = new CreditCardRequest().
            customerId(customer.getId()).
            number("5105105105105100").
            expirationDate("05/12");
        
        CreditCard card1 = gateway.creditCard().create(request).getTarget();
        CreditCard card2 = gateway.creditCard().create(request).getTarget();
        
        Assert.assertTrue(card1.isDefault());
        Assert.assertFalse(card2.isDefault());
        
        gateway.creditCard().update(card2.getToken(), new CreditCardRequest().options().makeDefault(true).done());
        Assert.assertFalse(gateway.creditCard().find(card1.getToken()).isDefault());
        Assert.assertTrue(gateway.creditCard().find(card2.getToken()).isDefault());
        
        gateway.creditCard().update(card1.getToken(), new CreditCardRequest().options().makeDefault(true).done());
        Assert.assertTrue(gateway.creditCard().find(card1.getToken()).isDefault());
        Assert.assertFalse(gateway.creditCard().find(card2.getToken()).isDefault());
    }
    
    @SuppressWarnings("deprecation")
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

        String queryString = TestHelper.simulateFormPostForTR(gateway, trParams, request, gateway.creditCard().transparentRedirectURLForUpdate());
        Result<CreditCard> result = gateway.creditCard().confirmTransparentRedirect(queryString);
        Assert.assertTrue(result.isSuccess());
        CreditCard updatedCard = result.getTarget();
        Assert.assertEquals("joe cool", updatedCard.getCardholderName());
    }
    
    @Test
    public void updateCountryFromTransparentRedirect() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest request = new CreditCardRequest().
            customerId(customer.getId()).
            number("5105105105105100").
            expirationDate("05/12");
        CreditCard card = gateway.creditCard().create(request).getTarget();
        
        CreditCardRequest updateRequest = new CreditCardRequest();
        CreditCardRequest trParams = new CreditCardRequest().
            paymentMethodToken(card.getToken()).
            number("4111111111111111").
            expirationDate("10/10").
            billingAddress().
                countryName("Jersey").
                countryCodeAlpha2("JE").
                countryCodeAlpha3("JEY").
                countryCodeNumeric("832").
                done();
                
        String queryString = TestHelper.simulateFormPostForTR(gateway, trParams, updateRequest, gateway.transparentRedirect().url());
        
        Result<CreditCard> result = gateway.transparentRedirect().confirmCreditCard(queryString);
        
        Assert.assertTrue(result.isSuccess());
        CreditCard updatedCreditCard = gateway.creditCard().find(card.getToken());
        Assert.assertEquals("411111", updatedCreditCard.getBin());
        Assert.assertEquals("1111", updatedCreditCard.getLast4());
        Assert.assertEquals("10/2010", updatedCreditCard.getExpirationDate());

        Assert.assertEquals("Jersey", updatedCreditCard.getBillingAddress().getCountryName());
        Assert.assertEquals("JE", updatedCreditCard.getBillingAddress().getCountryCodeAlpha2());
        Assert.assertEquals("JEY", updatedCreditCard.getBillingAddress().getCountryCodeAlpha3());
        Assert.assertEquals("832", updatedCreditCard.getBillingAddress().getCountryCodeNumeric());
    }

    
    @Test
    public void updateCountryFromTransparentRedirectWithInvalidCountry() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest request = new CreditCardRequest().
            customerId(customer.getId()).
            number("5105105105105100").
            expirationDate("05/12");
        CreditCard card = gateway.creditCard().create(request).getTarget();
        
        CreditCardRequest updateRequest = new CreditCardRequest();
        CreditCardRequest trParams = new CreditCardRequest().
            paymentMethodToken(card.getToken()).
            number("4111111111111111").
            expirationDate("10/10").
            billingAddress().
                countryCodeAlpha2("zz").
                done();
                
        String queryString = TestHelper.simulateFormPostForTR(gateway, trParams, updateRequest, gateway.transparentRedirect().url());
        
        Result<CreditCard> result = gateway.transparentRedirect().confirmCreditCard(queryString);
        
        Assert.assertFalse(result.isSuccess());
        Assert.assertEquals(
            ValidationErrorCode.ADDRESS_COUNTRY_CODE_ALPHA2_IS_NOT_ACCEPTED,
            result.getErrors().forObject("creditCard").forObject("billingAddress").onField("countryCodeAlpha2").get(0).getCode()
        );
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
    public void updateWithBillingAddressCreatesNewAddressByDefault() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest request = new CreditCardRequest().
            customerId(customer.getId()).
            number("5105105105105100").
            expirationDate("05/12").
            billingAddress().
                firstName("John").
                done();
        
        CreditCard creditCard = gateway.creditCard().create(request).getTarget();

        CreditCardRequest updateRequest = new CreditCardRequest().
            billingAddress().
                lastName("Jones").
                done();

        CreditCard updatedCreditCard = gateway.creditCard().update(creditCard.getToken(), updateRequest).getTarget();

        Assert.assertNull(updatedCreditCard.getBillingAddress().getFirstName());
        Assert.assertEquals("Jones", updatedCreditCard.getBillingAddress().getLastName());
        Assert.assertFalse(creditCard.getBillingAddress().getId().equals(updatedCreditCard.getBillingAddress().getId()));
    }

    @Test
    public void updateWithBillingAddressUpdatesAddressWhenUpdateExistingIsTrue() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest request = new CreditCardRequest().
            customerId(customer.getId()).
            number("5105105105105100").
            expirationDate("05/12").
            billingAddress().
                firstName("John").
                done();
        
        CreditCard creditCard = gateway.creditCard().create(request).getTarget();

        CreditCardRequest updateRequest = new CreditCardRequest().
            billingAddress().
                lastName("Jones").
                options().
                    updateExisting(true).
                    done().
                done();

        CreditCard updatedCreditCard = gateway.creditCard().update(creditCard.getToken(), updateRequest).getTarget();

        Assert.assertEquals("John", updatedCreditCard.getBillingAddress().getFirstName());
        Assert.assertEquals("Jones", updatedCreditCard.getBillingAddress().getLastName());
        Assert.assertEquals(creditCard.getBillingAddress().getId(), updatedCreditCard.getBillingAddress().getId());
    }

    @SuppressWarnings("deprecation")
    @Test
    public void updateWithBillingAddressUpdatesAddressWhenUpdateExistingIsTrueForTransparentRedirect() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest request = new CreditCardRequest().
            customerId(customer.getId()).
            number("5105105105105100").
            expirationDate("05/12").
            billingAddress().
                firstName("John").
                done();
        
        CreditCard creditCard = gateway.creditCard().create(request).getTarget();

        CreditCardRequest trParams = new CreditCardRequest().
            paymentMethodToken(creditCard.getToken()).
            billingAddress().
                options().
                    updateExisting(true).
                    done().
                done();

        CreditCardRequest updateRequest = new CreditCardRequest().
            billingAddress().
                lastName("Jones").
                done();

        String queryString = TestHelper.simulateFormPostForTR(gateway, trParams, updateRequest, gateway.creditCard().transparentRedirectURLForUpdate());
        CreditCard updatedCard = gateway.creditCard().confirmTransparentRedirect(queryString).getTarget();
        Assert.assertEquals("John", updatedCard.getBillingAddress().getFirstName());
        Assert.assertEquals("Jones", updatedCard.getBillingAddress().getLastName());
        Assert.assertEquals(creditCard.getBillingAddress().getId(), updatedCard.getBillingAddress().getId());
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
    
    @Test
    public void findReturnsAssociatedSubscriptions() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest cardRequest = new CreditCardRequest().
            customerId(customer.getId()).
            cardholderName("John Doe").
            cvv("123").
            number("5105105105105100").
            expirationDate("05/12");
        CreditCard card = gateway.creditCard().create(cardRequest).getTarget();
        String id = "subscription-id-" + new Random().nextInt();
        SubscriptionRequest subscriptionRequest = new SubscriptionRequest().
            id(id).
            planId("integration_trialless_plan").
            paymentMethodToken(card.getToken()).
            price(new BigDecimal("1.00"));
        Subscription subscription = gateway.subscription().create(subscriptionRequest).getTarget();
        
        CreditCard foundCard = gateway.creditCard().find(card.getToken());
        
        Assert.assertEquals(subscription.getId(), foundCard.getSubscriptions().get(0).getId());
        Assert.assertEquals(new BigDecimal("1.00"), foundCard.getSubscriptions().get(0).getPrice());
        Assert.assertEquals("integration_trialless_plan", foundCard.getSubscriptions().get(0).getPlanId());
    }

    @Test(expected = NotFoundException.class)
    public void findWithBadToken() {
        gateway.creditCard().find("badToken");
    }

    @Test
    public void findWithEmptyIds() {
        try {
            gateway.creditCard().find(" ");
            Assert.fail("Should throw NotFoundException");
        } catch (NotFoundException e) {
        }
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
                verifyCard(true).
                done();

        Result<CreditCard> result = gateway.creditCard().create(request);
        Assert.assertTrue(result.isSuccess());
    }
    
    @Test
    public void verifyCreditCardAgainstSpecificMerchantAccount() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest request = new CreditCardRequest().
            customerId(customer.getId()).
            cardholderName("John Doe").
            cvv("123").
            number("5105105105105100").
            expirationDate("05/12").
            options().
                verifyCard(true).
                verificationMerchantAccountId(MerchantAccount.NON_DEFAULT_MERCHANT_ACCOUNT_ID).
                done();

        Result<CreditCard> result = gateway.creditCard().create(request);
        Assert.assertFalse(result.isSuccess());
        Assert.assertEquals(MerchantAccount.NON_DEFAULT_MERCHANT_ACCOUNT_ID, result.getCreditCardVerification().getMerchantAccountId());
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
                verifyCard(true).
                done();

        Result<CreditCard> result = gateway.creditCard().create(request);
        Assert.assertFalse(result.isSuccess());
        CreditCardVerification verification = result.getCreditCardVerification();
        Assert.assertEquals(CreditCardVerification.Status.PROCESSOR_DECLINED, verification.getStatus());
        Assert.assertEquals("Do Not Honor", result.getMessage());
        Assert.assertNull(verification.getGatewayRejectionReason());
    }
    
    @Test
    public void verificationExposesgatewayRejectionReason() {
        BraintreeGateway processingRulesGateway = new BraintreeGateway(Environment.DEVELOPMENT, "processing_rules_merchant_id", "processing_rules_public_key", "processing_rules_private_key");
        Customer customer = processingRulesGateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest request = new CreditCardRequest().
            customerId(customer.getId()).
            cardholderName("John Doe").
            cvv("200").
            number("4111111111111111").
            expirationDate("05/12").
            options().
                verifyCard(true).
                done();

        Result<CreditCard> result = processingRulesGateway.creditCard().create(request);
        Assert.assertFalse(result.isSuccess());
        CreditCardVerification verification = result.getCreditCardVerification();

        Assert.assertEquals(Transaction.GatewayRejectionReason.CVV, verification.getGatewayRejectionReason());
    }
    
    @Test
    public void expiredSearch() {
        ResourceCollection<CreditCard> expiredCards = gateway.creditCard().expired();
        Assert.assertTrue(expiredCards.getMaximumSize() > 0);
        
        List<String> tokens = new ArrayList<String>();
        for (CreditCard card : expiredCards) {
            Assert.assertTrue(card.isExpired());
            tokens.add(card.getToken());
        }

        Set<String> uniqueTokens = new HashSet<String>(tokens);
        Assert.assertEquals(expiredCards.getMaximumSize(), uniqueTokens.size());
    }
    
    @Test
    public void expiringBetween() {
        Calendar start = Calendar.getInstance();
        start.set(2010, 0, 1);
        Calendar end = Calendar.getInstance();
        end.set(2010, 11, 30);
        
        ResourceCollection<CreditCard> expiredCards = gateway.creditCard().expiringBetween(start, end);
        Assert.assertTrue(expiredCards.getMaximumSize() > 0);
        
        List<String> tokens = new ArrayList<String>();
        for (CreditCard card : expiredCards) {
            Assert.assertEquals("2010", card.getExpirationYear());
            tokens.add(card.getToken());
        }

        Set<String> uniqueTokens = new HashSet<String>(tokens);
        Assert.assertEquals(expiredCards.getMaximumSize(), uniqueTokens.size());
    }
}
