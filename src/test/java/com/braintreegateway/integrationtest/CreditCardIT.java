package com.braintreegateway.integrationtest;

import com.braintreegateway.*;
import com.braintreegateway.exceptions.ForgedQueryStringException;
import com.braintreegateway.exceptions.NotFoundException;
import com.braintreegateway.test.CreditCardDefaults;
import com.braintreegateway.test.CreditCardNumbers;
import com.braintreegateway.test.VenmoSdk;
import com.braintreegateway.testhelpers.MerchantAccountTestConstants;
import com.braintreegateway.testhelpers.TestHelper;
import com.braintreegateway.testhelpers.HttpHelper;
import com.braintreegateway.util.QueryString;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.*;

public class CreditCardIT extends IntegrationTest implements MerchantAccountTestConstants {

    @SuppressWarnings("deprecation")
    @Test
    public void transparentRedirectURLForCreate() {
        Configuration configuration = gateway.getConfiguration();
        assertEquals(configuration.getBaseURL() + configuration.getMerchantPath() + "/payment_methods/all/create_via_transparent_redirect_request",
                gateway.creditCard().transparentRedirectURLForCreate());
    }

    @SuppressWarnings("deprecation")
    @Test
    public void transparentRedirectURLForUpdate() {
        Configuration configuration = gateway.getConfiguration();
        assertEquals(configuration.getBaseURL() + configuration.getMerchantPath() + "/payment_methods/all/update_via_transparent_redirect_request",
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
        assertTrue(result.isSuccess());
        CreditCard card = result.getTarget();
        assertEquals("John Doe", card.getCardholderName());
        assertEquals("MasterCard", card.getCardType());
        assertEquals(customer.getId(), card.getCustomerId());
        assertEquals("US", card.getCustomerLocation());
        assertEquals("510510", card.getBin());
        assertEquals("05", card.getExpirationMonth());
        assertEquals("2012", card.getExpirationYear());
        assertEquals("05/2012", card.getExpirationDate());
        assertEquals("5100", card.getLast4());
        assertEquals("510510******5100", card.getMaskedNumber());
        assertTrue(card.getToken() != null);
        assertEquals(Calendar.getInstance().get(Calendar.YEAR), card.getCreatedAt().get(Calendar.YEAR));
        assertEquals(Calendar.getInstance().get(Calendar.YEAR), card.getUpdatedAt().get(Calendar.YEAR));
        assertTrue(card.getUniqueNumberIdentifier().matches("\\A\\w{32}\\z"));
        assertFalse(card.isVenmoSdk());
        assertTrue(card.getImageUrl().matches(".*png.*"));
    }

    @Test
    public void createWithMonthAndYear() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest request = new CreditCardRequest().
            customerId(customer.getId()).
            cardholderName("John Doe").
            cvv("123").
            number("5105105105105100").
            expirationMonth("06").
            expirationYear("13");
        Result<CreditCard> result = gateway.creditCard().create(request);
        assertTrue(result.isSuccess());
        CreditCard card = result.getTarget();
        assertEquals(customer.getId(), card.getCustomerId());
        assertEquals("06", card.getExpirationMonth());
        assertEquals("2013", card.getExpirationYear());
        assertEquals("06/2013", card.getExpirationDate());
        assertEquals(Calendar.getInstance().get(Calendar.YEAR), card.getCreatedAt().get(Calendar.YEAR));
        assertEquals(Calendar.getInstance().get(Calendar.YEAR), card.getUpdatedAt().get(Calendar.YEAR));
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
        assertTrue(result.isSuccess());
        CreditCard card = result.getTarget();
        assertEquals("Special Chars <>&\"'", card.getCardholderName());
    }

    @Test
    public void createWithSecurityParams() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest request = new CreditCardRequest().
            customerId(customer.getId()).
            cardholderName("Special Chars").
            number("5105105105105100").
            expirationDate("05/12").
            deviceSessionId("abc123").
            fraudMerchantId("456");
        Result<CreditCard> result = gateway.creditCard().create(request);

        assertTrue(result.isSuccess());
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
        assertTrue(result.isSuccess());
        CreditCard card = result.getTarget();

        Address billingAddress = card.getBillingAddress();
        assertEquals("1 E Main St", billingAddress.getStreetAddress());
        assertEquals("Unit 2", billingAddress.getExtendedAddress());
        assertEquals("Chicago", billingAddress.getLocality());
        assertEquals("Illinois", billingAddress.getRegion());
        assertEquals("60607", billingAddress.getPostalCode());
        assertEquals("United States of America", billingAddress.getCountryName());
        assertEquals("US", billingAddress.getCountryCodeAlpha2());
        assertEquals("USA", billingAddress.getCountryCodeAlpha3());
        assertEquals("840", billingAddress.getCountryCodeNumeric());
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
        assertTrue(result.isSuccess());
        CreditCard card = result.getTarget();

        Address billingAddress = card.getBillingAddress();
        assertEquals(address.getId(), billingAddress.getId());
        assertEquals("11111", billingAddress.getPostalCode());
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
        assertTrue(result.isSuccess());
        CreditCard card = result.getTarget();
        assertEquals("John Doe", card.getCardholderName());
        assertEquals("510510", card.getBin());
        assertEquals("05", card.getExpirationMonth());
        assertEquals("2012", card.getExpirationYear());
        assertEquals("05/2012", card.getExpirationDate());
        assertEquals("5100", card.getLast4());
        assertTrue(card.getToken() != null);
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

        assertTrue(result.isSuccess());
        assertEquals("411111", result.getTarget().getBin());
        assertEquals("1111", result.getTarget().getLast4());
        assertEquals("10/2010", result.getTarget().getExpirationDate());
        assertEquals("Aruba", result.getTarget().getBillingAddress().getCountryName());
        assertEquals("AW", result.getTarget().getBillingAddress().getCountryCodeAlpha2());
        assertEquals("ABW", result.getTarget().getBillingAddress().getCountryCodeAlpha3());
        assertEquals("533", result.getTarget().getBillingAddress().getCountryCodeNumeric());
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
        assertTrue(card.isDefault());
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
        assertTrue(card.isDefault());
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

        assertFalse(result.isSuccess());
        assertEquals(ValidationErrorCode.ADDRESS_INCONSISTENT_COUNTRY, result.getErrors().forObject("creditCard").forObject("billingAddress").onField("base").get(0).getCode());
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

        assertFalse(gateway.creditCard().find(card1.getToken()).isDefault());
        assertTrue(gateway.creditCard().find(card2.getToken()).isDefault());
    }

    @Test
    public void createWithVenmoSdkPaymentMethodCode() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest request = new CreditCardRequest().
            customerId(customer.getId()).
            venmoSdkPaymentMethodCode(VenmoSdk.PaymentMethodCode.Visa.code);

        Result<CreditCard> result = gateway.creditCard().create(request);
        assertTrue(result.isSuccess());
        CreditCard card = result.getTarget();
        assertEquals("411111", card.getBin());
        assertFalse(card.isVenmoSdk());
    }

    @Test
    public void createWithPaymentMethodNonce() {
        String nonce = TestHelper.generateUnlockedNonce(gateway);
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest request = new CreditCardRequest().
            customerId(customer.getId()).
            paymentMethodNonce(nonce);
        Result<CreditCard> result = gateway.creditCard().create(request);
        assertTrue(result.isSuccess());
    }

    @Test
    public void createWithInvalidVenmoSdkPaymentMethodCode() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest request = new CreditCardRequest().
            customerId(customer.getId()).
            venmoSdkPaymentMethodCode(VenmoSdk.PaymentMethodCode.Invalid.code);

        Result<CreditCard> result = gateway.creditCard().create(request);
        ValidationErrorCode errorCode = result.getErrors().forObject("creditCard")
            .onField("venmoSdkPaymentMethodCode").get(0).getCode();

        assertFalse(result.isSuccess());
        assertEquals("Invalid VenmoSDK payment method code", result.getMessage());
        assertEquals(ValidationErrorCode.CREDIT_CARD_INVALID_VENMO_SDK_PAYMENT_METHOD_CODE, errorCode);
    }

    @Test
    public void createWithVenmoSdkSession() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest request = new CreditCardRequest().
            customerId(customer.getId()).
            cardholderName("John Doe").
            number("5105105105105100").
            expirationDate("05/12").
            options().
                venmoSdkSession(VenmoSdk.Session.Valid.value).
                done();

        Result<CreditCard> result = gateway.creditCard().create(request);
        assertTrue(result.isSuccess());
        CreditCard card = result.getTarget();
        assertEquals("510510", card.getBin());
        assertFalse(card.isVenmoSdk());
    }

    @Test
    public void createWithInvalidVenmoSdkSession() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest request = new CreditCardRequest().
            customerId(customer.getId()).
            cardholderName("John Doe").
            number("5105105105105100").
            expirationDate("05/12").
            options().
                venmoSdkSession(VenmoSdk.Session.Invalid.value).
                done();

        Result<CreditCard> result = gateway.creditCard().create(request);
        assertTrue(result.isSuccess());
        CreditCard card = result.getTarget();
        assertEquals("510510", card.getBin());
        assertFalse(card.isVenmoSdk());
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
        assertTrue(result.isSuccess());
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
        assertTrue(updateResult.isSuccess());
        CreditCard updatedCard = updateResult.getTarget();

        assertEquals("Jane Jones", updatedCard.getCardholderName());
        assertEquals("411111", updatedCard.getBin());
        assertEquals("12", updatedCard.getExpirationMonth());
        assertEquals("2005", updatedCard.getExpirationYear());
        assertEquals("12/2005", updatedCard.getExpirationDate());
        assertEquals("1111", updatedCard.getLast4());
        assertTrue(updatedCard.getToken() != card.getToken());

        assertEquals("Italy", updatedCard.getBillingAddress().getCountryName());
        assertEquals("IT", updatedCard.getBillingAddress().getCountryCodeAlpha2());
        assertEquals("ITA", updatedCard.getBillingAddress().getCountryCodeAlpha3());
        assertEquals("380", updatedCard.getBillingAddress().getCountryCodeNumeric());
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

        assertTrue(card1.isDefault());
        assertFalse(card2.isDefault());

        gateway.creditCard().update(card2.getToken(), new CreditCardRequest().options().makeDefault(true).done());
        assertFalse(gateway.creditCard().find(card1.getToken()).isDefault());
        assertTrue(gateway.creditCard().find(card2.getToken()).isDefault());

        gateway.creditCard().update(card1.getToken(), new CreditCardRequest().options().makeDefault(true).done());
        assertTrue(gateway.creditCard().find(card1.getToken()).isDefault());
        assertFalse(gateway.creditCard().find(card2.getToken()).isDefault());
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
        assertTrue(result.isSuccess());
        CreditCard updatedCard = result.getTarget();
        assertEquals("joe cool", updatedCard.getCardholderName());
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

        assertTrue(result.isSuccess());
        CreditCard updatedCreditCard = gateway.creditCard().find(card.getToken());
        assertEquals("411111", updatedCreditCard.getBin());
        assertEquals("1111", updatedCreditCard.getLast4());
        assertEquals("10/2010", updatedCreditCard.getExpirationDate());

        assertEquals("Jersey", updatedCreditCard.getBillingAddress().getCountryName());
        assertEquals("JE", updatedCreditCard.getBillingAddress().getCountryCodeAlpha2());
        assertEquals("JEY", updatedCreditCard.getBillingAddress().getCountryCodeAlpha3());
        assertEquals("832", updatedCreditCard.getBillingAddress().getCountryCodeNumeric());
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

        assertFalse(result.isSuccess());
        assertEquals(
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
        assertTrue(result.isSuccess());
        CreditCard card = result.getTarget();

        String newToken = String.valueOf(new Random().nextInt());
        CreditCardRequest updateRequest = new CreditCardRequest().customerId(customer.getId()).token(newToken);

        Result<CreditCard> updateResult = gateway.creditCard().update(card.getToken(), updateRequest);
        assertTrue(updateResult.isSuccess());
        CreditCard updatedCard = updateResult.getTarget();

        assertEquals(newToken, updatedCard.getToken());
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
        assertTrue(result.isSuccess());
        CreditCard card = result.getTarget();

        CreditCardRequest updateRequest = new CreditCardRequest().cardholderName("Jane Jones");

        Result<CreditCard> updateResult = gateway.creditCard().update(card.getToken(), updateRequest);
        assertTrue(updateResult.isSuccess());
        CreditCard updatedCard = updateResult.getTarget();

        assertEquals("Jane Jones", updatedCard.getCardholderName());
        assertEquals("510510", updatedCard.getBin());
        assertEquals("05", updatedCard.getExpirationMonth());
        assertEquals("2012", updatedCard.getExpirationYear());
        assertEquals("05/2012", updatedCard.getExpirationDate());
        assertEquals("5100", updatedCard.getLast4());
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

        assertNull(updatedCreditCard.getBillingAddress().getFirstName());
        assertEquals("Jones", updatedCreditCard.getBillingAddress().getLastName());
        assertFalse(creditCard.getBillingAddress().getId().equals(updatedCreditCard.getBillingAddress().getId()));
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

        assertEquals("John", updatedCreditCard.getBillingAddress().getFirstName());
        assertEquals("Jones", updatedCreditCard.getBillingAddress().getLastName());
        assertEquals(creditCard.getBillingAddress().getId(), updatedCreditCard.getBillingAddress().getId());
    }

    @Test
    public void updateWillNotUpdatePayPalAccounts() {
        String nonce = TestHelper.generateFuturePaymentPayPalNonce(gateway);
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        assertTrue(customerResult.isSuccess());
        Customer customer = customerResult.getTarget();

        PaymentMethodRequest request = new PaymentMethodRequest().
            customerId(customer.getId()).
            paymentMethodNonce(nonce);

        Result<? extends PaymentMethod> result = gateway.paymentMethod().create(request);
        assertTrue(result.isSuccess());

        CreditCardRequest updateRequest = new CreditCardRequest().
            billingAddress().
                lastName("Jones").
                options().
                    updateExisting(true).
                    done().
                done();

        try {
            gateway.creditCard().update(result.getTarget().getToken(), updateRequest);
            fail("Should throw NotFoundException");
        } catch (NotFoundException e) {
        }
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
        assertEquals("John", updatedCard.getBillingAddress().getFirstName());
        assertEquals("Jones", updatedCard.getBillingAddress().getLastName());
        assertEquals(creditCard.getBillingAddress().getId(), updatedCard.getBillingAddress().getId());
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
        assertTrue(result.isSuccess());
        CreditCard card = result.getTarget();

        CreditCard found = gateway.creditCard().find(card.getToken());

        assertEquals("John Doe", found.getCardholderName());
        assertEquals("510510", found.getBin());
        assertEquals("05", found.getExpirationMonth());
        assertEquals("2012", found.getExpirationYear());
        assertEquals("05/2012", found.getExpirationDate());
        assertEquals("5100", found.getLast4());
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

        assertEquals(subscription.getId(), foundCard.getSubscriptions().get(0).getId());
        assertEquals(new BigDecimal("1.00"), foundCard.getSubscriptions().get(0).getPrice());
        assertEquals("integration_trialless_plan", foundCard.getSubscriptions().get(0).getPlanId());
    }

    @Test(expected = NotFoundException.class)
    public void findWithBadToken() {
        gateway.creditCard().find("badToken");
    }

    @Test
    public void findWithEmptyIds() {
        try {
            gateway.creditCard().find(" ");
            fail("Should throw NotFoundException");
        } catch (NotFoundException e) {
        }
    }

    @Test
    public void findWithPayPalAccountToken() {
        String nonce = TestHelper.generateFuturePaymentPayPalNonce(gateway);
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        assertTrue(customerResult.isSuccess());
        Customer customer = customerResult.getTarget();

        PaymentMethodRequest request = new PaymentMethodRequest().
            customerId(customer.getId()).
            paymentMethodNonce(nonce);

        Result<? extends PaymentMethod> result = gateway.paymentMethod().create(request);
        assertTrue(result.isSuccess());

        try {
            gateway.creditCard().find(result.getTarget().getToken());
            fail("Should throw NotFoundException");
        } catch (NotFoundException e) {
        }
    }

    @Test
    public void fromNonce() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        String nonce = TestHelper.generateUnlockedNonce(gateway, customer.getId(), "4012888888881881");

        CreditCard card = gateway.creditCard().fromNonce(nonce);
        assertNotNull(card);
        assertEquals(card.getMaskedNumber(), "401288******1881");
    }

    @Test
    public void fromNoncePointingToUnlockedSharedCard() {
        String nonce = TestHelper.generateUnlockedNonce(gateway);
        try {
            gateway.creditCard().fromNonce(nonce);
            fail("Should throw NotFoundException");
        } catch (NotFoundException e) {
            assertTrue(e.getMessage().matches(".*not found.*"));
        }
    }

    @Test
    public void fromConsumedNonce() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        String nonce = TestHelper.generateUnlockedNonce(gateway, customer.getId(), "4012888888881881");
        gateway.creditCard().fromNonce(nonce);

        try {
            gateway.creditCard().fromNonce(nonce);
            fail("Should throw NotFoundException");
        } catch (NotFoundException e) {
            assertTrue(e.getMessage().matches(".*consumed.*"));
        }
    }

    @Test(expected=NotFoundException.class)
    public void forwardRaisesException() {
        BraintreeGateway forwardGateway = new BraintreeGateway(
            Environment.DEVELOPMENT,
            "forward_payment_method_merchant_id",
            "forward_payment_method_public_key",
            "forward_payment_method_private_key"
        );

        Customer customer = forwardGateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest request = new CreditCardRequest().
            customerId(customer.getId()).
            cardholderName("John Doe").
            cvv("123").
            number("5105105105105100").
            expirationDate("05/12");
        Result<CreditCard> createResult = forwardGateway.creditCard().create(request);
        assertTrue(createResult.isSuccess());
        CreditCard card = createResult.getTarget();

        PaymentMethodForwardRequest forwardRequest = new PaymentMethodForwardRequest()
            .token(card.getToken())
            .receivingMerchantId("integration_merchant_id");
        Result<PaymentMethodNonce> forwardResult = forwardGateway.creditCard()
            .forward(forwardRequest);
    }

    @Test(expected=NotFoundException.class)
    public void forwardInvalidToken() {
        BraintreeGateway forwardGateway = new BraintreeGateway(
            Environment.DEVELOPMENT,
            "forward_payment_method_merchant_id",
            "forward_payment_method_public_key",
            "forward_payment_method_private_key"
        );

        PaymentMethodForwardRequest forwardRequest = new PaymentMethodForwardRequest()
            .token("invalid")
            .receivingMerchantId("integration_merchant_id");
        Result<PaymentMethodNonce> forwardResult = forwardGateway.creditCard()
            .forward(forwardRequest);
    }

    @Test(expected=NotFoundException.class)
    public void forwardInvalidReceivingMerchantId() {
        BraintreeGateway forwardGateway = new BraintreeGateway(
            Environment.DEVELOPMENT,
            "forward_payment_method_merchant_id",
            "forward_payment_method_public_key",
            "forward_payment_method_private_key"
        );

        Customer customer = forwardGateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest request = new CreditCardRequest().
            customerId(customer.getId()).
            cardholderName("John Doe").
            cvv("123").
            number("5105105105105100").
            expirationDate("05/12");
        Result<CreditCard> createResult = forwardGateway.creditCard().create(request);
        assertTrue(createResult.isSuccess());
        CreditCard card = createResult.getTarget();

        PaymentMethodForwardRequest forwardRequest = new PaymentMethodForwardRequest()
            .token(card.getToken())
            .receivingMerchantId("invalid_merchant_id");
        Result<PaymentMethodNonce> forwardResult = forwardGateway.creditCard()
            .forward(forwardRequest);
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
        assertTrue(result.isSuccess());
        CreditCard card = result.getTarget();

        Result<CreditCard> deleteResult = gateway.creditCard().delete(card.getToken());
        assertTrue(deleteResult.isSuccess());

        try {
            gateway.creditCard().find(card.getToken());
            fail();
        } catch (NotFoundException e) {
        }
    }

    @Test
    public void deleteWillNotDeletePayPalAccount() {
        String nonce = TestHelper.generateFuturePaymentPayPalNonce(gateway);
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        assertTrue(customerResult.isSuccess());
        Customer customer = customerResult.getTarget();

        PaymentMethodRequest request = new PaymentMethodRequest().
            customerId(customer.getId()).
            paymentMethodNonce(nonce);

        Result<? extends PaymentMethod> result = gateway.paymentMethod().create(request);
        assertTrue(result.isSuccess());

        try {
            gateway.creditCard().delete(result.getTarget().getToken());
            fail();
        } catch (NotFoundException e) {
        }
    }

    @Test
    public void checkDuplicateCreditCard() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest request = new CreditCardRequest().
            customerId(customer.getId()).
            cardholderName("John Doe").
            cvv("123").
            number("4012000033330026").
            expirationDate("05/12").
            options().
                failOnDuplicatePaymentMethod(true).
                done();

        gateway.creditCard().create(request);
        Result<CreditCard> result = gateway.creditCard().create(request);

        assertFalse(result.isSuccess());
        assertEquals(
                ValidationErrorCode.CREDIT_CARD_DUPLICATE_CARD_EXISTS,
                result.getErrors().forObject("creditCard").onField("number").get(0).getCode()
        );
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
        assertTrue(result.isSuccess());
    }

    @Test
    public void verifyValidCreditCardWithVerificationWithRiskData() {
        createAdvancedFraudMerchantGateway();
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest request = new CreditCardRequest().
            customerId(customer.getId()).
            cardholderName("John Doe").
            cvv("123").
            number("4111111111111111").
            expirationDate("05/12").
            deviceSessionId("abc123").
            options().
                verifyCard(true).
                done();

        Result<CreditCard> result = gateway.creditCard().create(request);
        assertTrue(result.isSuccess());

        CreditCard card = result.getTarget();
        assertNotNull(card);

        CreditCardVerification verification = card.getVerification();
        assertNotNull(verification);

        RiskData riskData = verification.getRiskData();
        assertNotNull(riskData);

        assertNotNull(riskData.getDecision());
        assertNotNull(riskData.getDeviceDataCaptured());
        assertNotNull(riskData.getId());
    }

    @Test
    public void verifyValidCreditCardWithCustomVerificationAmount() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest request = new CreditCardRequest().
            customerId(customer.getId()).
            cardholderName("John Doe").
            cvv("123").
            number("4111111111111111").
            expirationDate("05/12").
            options().
                verifyCard(true).
                verificationAmount("1.02").
                done();

        Result<CreditCard> result = gateway.creditCard().create(request);
        assertTrue(result.isSuccess());
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
                verificationMerchantAccountId(NON_DEFAULT_MERCHANT_ACCOUNT_ID).
                done();

        Result<CreditCard> result = gateway.creditCard().create(request);
        assertFalse(result.isSuccess());
        assertEquals(NON_DEFAULT_MERCHANT_ACCOUNT_ID, result.getCreditCardVerification().getMerchantAccountId());
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
        assertFalse(result.isSuccess());
        CreditCardVerification verification = result.getCreditCardVerification();
        assertEquals(CreditCardVerification.Status.PROCESSOR_DECLINED, verification.getStatus());
        assertEquals("Do Not Honor", result.getMessage());
        assertNull(verification.getGatewayRejectionReason());
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
        assertFalse(result.isSuccess());
        CreditCardVerification verification = result.getCreditCardVerification();

        assertEquals(Transaction.GatewayRejectionReason.CVV, verification.getGatewayRejectionReason());
    }

    @Test
    public void expiredSearch() {
        ResourceCollection<CreditCard> expiredCards = gateway.creditCard().expired();
        assertTrue(expiredCards.getMaximumSize() > 0);

        List<String> tokens = new ArrayList<String>();
        for (CreditCard card : expiredCards) {
            assertTrue(card.isExpired());
            tokens.add(card.getToken());
        }

        Set<String> uniqueTokens = new HashSet<String>(tokens);
        assertEquals(expiredCards.getMaximumSize(), uniqueTokens.size());
    }

    @Test
    public void expiringBetween() {
        Calendar start = Calendar.getInstance();
        start.set(2010, 0, 1);
        Calendar end = Calendar.getInstance();
        end.set(2010, 11, 30);

        ResourceCollection<CreditCard> expiredCards = gateway.creditCard().expiringBetween(start, end);
        assertTrue(expiredCards.getMaximumSize() > 0);

        List<String> tokens = new ArrayList<String>();
        for (CreditCard card : expiredCards) {
            assertEquals("2010", card.getExpirationYear());
            tokens.add(card.getToken());
        }

        Set<String> uniqueTokens = new HashSet<String>(tokens);
        assertEquals(expiredCards.getMaximumSize(), uniqueTokens.size());
    }

    @Test
    public void commercialCard() {
        BraintreeGateway processingRulesGateway = new BraintreeGateway(Environment.DEVELOPMENT, "processing_rules_merchant_id", "processing_rules_public_key", "processing_rules_private_key");
        Customer customer = processingRulesGateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest request = new CreditCardRequest().
            customerId(customer.getId()).
            number(CreditCardNumbers.CardTypeIndicators.Commercial.getValue()).
            expirationDate("05/12").
            options().
                verifyCard(true).
                done();

        Result<CreditCard> result = processingRulesGateway.creditCard().create(request);
        CreditCard card = result.getTarget();

        assertEquals(CreditCard.Commercial.YES, card.getCommercial());
    }

    @Test
    public void durbinRegulatedCard() {
        BraintreeGateway processingRulesGateway = new BraintreeGateway(Environment.DEVELOPMENT, "processing_rules_merchant_id", "processing_rules_public_key", "processing_rules_private_key");
        Customer customer = processingRulesGateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest request = new CreditCardRequest().
            customerId(customer.getId()).
            number(CreditCardNumbers.CardTypeIndicators.DurbinRegulated.getValue()).
            expirationDate("05/12").
            options().
                verifyCard(true).
                done();

        Result<CreditCard> result = processingRulesGateway.creditCard().create(request);
        CreditCard card = result.getTarget();

        assertEquals(CreditCard.DurbinRegulated.YES, card.getDurbinRegulated());
    }

    @Test
    public void debitCard() {
        BraintreeGateway processingRulesGateway = new BraintreeGateway(Environment.DEVELOPMENT, "processing_rules_merchant_id", "processing_rules_public_key", "processing_rules_private_key");
        Customer customer = processingRulesGateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest request = new CreditCardRequest().
            customerId(customer.getId()).
            number(CreditCardNumbers.CardTypeIndicators.Debit.getValue()).
            expirationDate("05/12").
            options().
                verifyCard(true).
                done();

        Result<CreditCard> result = processingRulesGateway.creditCard().create(request);
        CreditCard card = result.getTarget();

        assertEquals(CreditCard.Debit.YES, card.getDebit());
    }

    @Test
    public void healthcareCard() {
        BraintreeGateway processingRulesGateway = new BraintreeGateway(Environment.DEVELOPMENT, "processing_rules_merchant_id", "processing_rules_public_key", "processing_rules_private_key");
        Customer customer = processingRulesGateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest request = new CreditCardRequest().
            customerId(customer.getId()).
            number(CreditCardNumbers.CardTypeIndicators.Healthcare.getValue()).
            expirationDate("05/12").
            options().
                verifyCard(true).
                done();

        Result<CreditCard> result = processingRulesGateway.creditCard().create(request);
        CreditCard card = result.getTarget();

        assertEquals(CreditCard.Healthcare.YES, card.getHealthcare());
        assertEquals("J3", card.getProductId());
    }

    @Test
    public void payrollCard() {
        BraintreeGateway processingRulesGateway = new BraintreeGateway(Environment.DEVELOPMENT, "processing_rules_merchant_id", "processing_rules_public_key", "processing_rules_private_key");
        Customer customer = processingRulesGateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest request = new CreditCardRequest().
            customerId(customer.getId()).
            number(CreditCardNumbers.CardTypeIndicators.Payroll.getValue()).
            expirationDate("05/12").
            options().
                verifyCard(true).
                done();

        Result<CreditCard> result = processingRulesGateway.creditCard().create(request);
        CreditCard card = result.getTarget();

        assertEquals(CreditCard.Payroll.YES, card.getPayroll());
        assertEquals("MSA", card.getProductId());
    }

    @Test
    public void prepaidCard() {
        BraintreeGateway processingRulesGateway = new BraintreeGateway(Environment.DEVELOPMENT, "processing_rules_merchant_id", "processing_rules_public_key", "processing_rules_private_key");
        Customer customer = processingRulesGateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest request = new CreditCardRequest().
            customerId(customer.getId()).
            number(CreditCardNumbers.CardTypeIndicators.Prepaid.getValue()).
            expirationDate("05/12").
            options().
                verifyCard(true).
                done();

        Result<CreditCard> result = processingRulesGateway.creditCard().create(request);
        CreditCard card = result.getTarget();

        assertEquals(CreditCard.Prepaid.YES, card.getPrepaid());
    }

    @Test
    public void issuingBank() {
        BraintreeGateway processingRulesGateway = new BraintreeGateway(Environment.DEVELOPMENT, "processing_rules_merchant_id", "processing_rules_public_key", "processing_rules_private_key");
        Customer customer = processingRulesGateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest request = new CreditCardRequest().
            customerId(customer.getId()).
            number(CreditCardNumbers.CardTypeIndicators.IssuingBank.getValue()).
            expirationDate("05/12").
            options().
                verifyCard(true).
                done();

        Result<CreditCard> result = processingRulesGateway.creditCard().create(request);
        CreditCard card = result.getTarget();

        assertEquals(CreditCardDefaults.IssuingBank.getValue(), card.getIssuingBank());
    }

    @Test
    public void countryOfIssuance() {
        BraintreeGateway processingRulesGateway = new BraintreeGateway(Environment.DEVELOPMENT, "processing_rules_merchant_id", "processing_rules_public_key", "processing_rules_private_key");
        Customer customer = processingRulesGateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest request = new CreditCardRequest().
            customerId(customer.getId()).
            number(CreditCardNumbers.CardTypeIndicators.CountryOfIssuance.getValue()).
            expirationDate("05/12").
            options().
                verifyCard(true).
                done();

        Result<CreditCard> result = processingRulesGateway.creditCard().create(request);
        CreditCard card = result.getTarget();

        assertEquals(CreditCardDefaults.CountryOfIssuance.getValue(), card.getCountryOfIssuance());
    }

    @Test
    public void negativeCardTypeIndicators() {
        BraintreeGateway processingRulesGateway = new BraintreeGateway(Environment.DEVELOPMENT, "processing_rules_merchant_id", "processing_rules_public_key", "processing_rules_private_key");
        Customer customer = processingRulesGateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest request = new CreditCardRequest().
            customerId(customer.getId()).
            number(CreditCardNumbers.CardTypeIndicators.No.getValue()).
            expirationDate("05/12").
            options().
                verifyCard(true).
                done();

        Result<CreditCard> result = processingRulesGateway.creditCard().create(request);
        CreditCard card = result.getTarget();

        assertEquals(CreditCard.Commercial.NO, card.getCommercial());
        assertEquals(CreditCard.Debit.NO, card.getDebit());
        assertEquals(CreditCard.DurbinRegulated.NO, card.getDurbinRegulated());
        assertEquals(CreditCard.Healthcare.NO, card.getHealthcare());
        assertEquals(CreditCard.Payroll.NO, card.getPayroll());
        assertEquals(CreditCard.Prepaid.NO, card.getPrepaid());
        assertEquals("MSB", card.getProductId());
    }


    @Test
    public void absentCardTypeIndicators() {
        BraintreeGateway processingRulesGateway = new BraintreeGateway(Environment.DEVELOPMENT, "processing_rules_merchant_id", "processing_rules_public_key", "processing_rules_private_key");
        Customer customer = processingRulesGateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest request = new CreditCardRequest().
            customerId(customer.getId()).
            number("5555555555554444").
            expirationDate("05/12").
            options().
                verifyCard(true).
                done();

        Result<CreditCard> result = processingRulesGateway.creditCard().create(request);
        CreditCard card = result.getTarget();

        assertEquals(CreditCard.Commercial.UNKNOWN, card.getCommercial());
        assertEquals(CreditCard.Debit.UNKNOWN, card.getDebit());
        assertEquals(CreditCard.DurbinRegulated.UNKNOWN, card.getDurbinRegulated());
        assertEquals(CreditCard.Healthcare.UNKNOWN, card.getHealthcare());
        assertEquals(CreditCard.Payroll.UNKNOWN, card.getPayroll());
        assertEquals(CreditCard.Prepaid.UNKNOWN, card.getPrepaid());
        assertEquals("Unknown", card.getCountryOfIssuance());
        assertEquals("Unknown", card.getIssuingBank());
        assertEquals("Unknown", card.getProductId());
    }
}
