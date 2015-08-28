package com.braintreegateway.integrationtest;

import com.braintreegateway.*;
import com.braintreegateway.exceptions.ForgedQueryStringException;
import com.braintreegateway.exceptions.NotFoundException;
import com.braintreegateway.test.Nonce;
import com.braintreegateway.test.VenmoSdk;
import com.braintreegateway.testhelpers.TestHelper;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

@SuppressWarnings("deprecation")
public class CustomerIT {

    private BraintreeGateway gateway;

    @Before
    public void createGateway() {
        this.gateway = new BraintreeGateway(Environment.DEVELOPMENT, "integration_merchant_id", "integration_public_key", "integration_private_key");
    }

    @Test
    public void transparentRedirectURLForCreate() {
        Configuration configuration = gateway.getConfiguration();
        assertEquals(configuration.getBaseURL() + configuration.getMerchantPath() + "/customers/all/create_via_transparent_redirect_request",
                gateway.customer().transparentRedirectURLForCreate());
    }

    @Test
    public void transparentRedirectURLForUpdate() {
        Configuration configuration = gateway.getConfiguration();
        assertEquals(configuration.getBaseURL() + configuration.getMerchantPath() + "/customers/all/update_via_transparent_redirect_request",
                gateway.customer().transparentRedirectURLForUpdate());
    }

    @Test
    public void create() {
        CustomerRequest request = new CustomerRequest().
            firstName("Mark").
            lastName("Jones").
            company("Jones Co.").
            email("mark.jones@example.com").
            fax("419-555-1234").
            phone("614-555-1234").
            website("http://example.com");
        Result<Customer> result = gateway.customer().create(request);
        assertTrue(result.isSuccess());
        Customer customer = result.getTarget();

        assertEquals("Mark", customer.getFirstName());
        assertEquals("Jones", customer.getLastName());
        assertEquals("Jones Co.", customer.getCompany());
        assertEquals("mark.jones@example.com", customer.getEmail());
        assertEquals("419-555-1234", customer.getFax());
        assertEquals("614-555-1234", customer.getPhone());
        assertEquals("http://example.com", customer.getWebsite());
        assertEquals(Calendar.getInstance().get(Calendar.YEAR), customer.getCreatedAt().get(Calendar.YEAR));
        assertEquals(Calendar.getInstance().get(Calendar.YEAR), customer.getUpdatedAt().get(Calendar.YEAR));
    }

    @Test
    public void createWithAccessToken() {
        BraintreeGateway oauthGateway = new BraintreeGateway("client_id$development$integration_client_id", "client_secret$development$integration_client_secret");

        String code = TestHelper.createOAuthGrant(oauthGateway, "integration_merchant_id", "read_write");

        OAuthCredentialsRequest oauthRequest = new OAuthCredentialsRequest().
             code(code).
             scope("read_write");

        Result<OAuthCredentials> accessTokenResult = oauthGateway.oauth().createTokenFromCode(oauthRequest);

        BraintreeGateway gateway = new BraintreeGateway(accessTokenResult.getTarget().getAccessToken());

        CustomerRequest request = new CustomerRequest().
            firstName("Mark").
            lastName("Jones").
            company("Jones Co.").
            email("mark.jones@example.com").
            fax("419-555-1234").
            phone("614-555-1234").
            website("http://example.com");
        Result<Customer> result = gateway.customer().create(request);
        assertTrue(result.isSuccess());
        Customer customer = result.getTarget();

        assertEquals("Mark", customer.getFirstName());
        assertEquals("Jones", customer.getLastName());
        assertEquals("Jones Co.", customer.getCompany());
        assertEquals("mark.jones@example.com", customer.getEmail());
    }

    @Test
    public void createViaTransparentRedirect() {
        CustomerRequest trParams = new CustomerRequest();
        CustomerRequest request = new CustomerRequest().firstName("John").lastName("Doe");

        String queryString = TestHelper.simulateFormPostForTR(gateway, trParams, request, gateway.customer().transparentRedirectURLForCreate());
        Result<Customer> result = gateway.customer().confirmTransparentRedirect(queryString);

        assertTrue(result.isSuccess());
        Customer customer = result.getTarget();
        assertEquals("John", customer.getFirstName());
        assertEquals("Doe", customer.getLastName());
    }

    @Test(expected = ForgedQueryStringException.class)
    public void createViaTransparentRedirectThrowsWhenQueryStringHasBeenTamperedWith() {
        String queryString = TestHelper.simulateFormPostForTR(gateway, new CustomerRequest(), new CustomerRequest(), gateway.customer().transparentRedirectURLForCreate());
        gateway.customer().confirmTransparentRedirect(queryString + "this make it invalid");
    }

    @Test
    public void createViaTransparentRedirectWithNesting() {
        CustomerRequest trParams = new CustomerRequest();
        CustomerRequest request = new CustomerRequest().
            firstName("John").
            lastName("Doe").
            creditCard().
                number("4111111111111111").
                expirationDate("11/12").
                done();

        String queryString = TestHelper.simulateFormPostForTR(gateway, trParams, request, gateway.customer().transparentRedirectURLForCreate());
        Result<Customer> result = gateway.customer().confirmTransparentRedirect(queryString);

        assertTrue(result.isSuccess());
        Customer customer = result.getTarget();
        assertEquals("John", customer.getFirstName());
        assertEquals("Doe", customer.getLastName());
        assertEquals("1111", customer.getCreditCards().get(0).getLast4());
    }

    @Test
    public void createWithBlanks() {
        CustomerRequest request = new CustomerRequest();
        Result<Customer> result = gateway.customer().create(request);
        assertTrue(result.isSuccess());
        Customer customer = result.getTarget();
        assertEquals(null, customer.getFirstName());
        assertEquals(null, customer.getLastName());
        assertEquals(null, customer.getCompany());
        assertEquals(null, customer.getEmail());
        assertEquals(null, customer.getFax());
        assertEquals(null, customer.getPhone());
        assertEquals(null, customer.getWebsite());
    }

    @Test
    public void createWithSecurityParams() {
        CustomerRequest request = new CustomerRequest().
            creditCard().
                cardholderName("Fred Jones").
                number("5105105105105100").
                cvv("123").
                expirationDate("05/12").
                deviceSessionId("abc123").
                fraudMerchantId("456").
                done();
        Result<Customer> result = gateway.customer().create(request);

        assertTrue(result.isSuccess());
    }

    @Test
    public void createWithCustomFields() {
        CustomerRequest request = new CustomerRequest().
            customField("store_me", "custom value").
            customField("another_stored_field", "custom value2");
        Result<Customer> result = gateway.customer().create(request);
        assertTrue(result.isSuccess());

        Map<String, String> expected = new HashMap<String, String>();
        expected.put("store_me", "custom value");
        expected.put("another_stored_field", "custom value2");

        Customer customer = result.getTarget();
        assertEquals(expected, customer.getCustomFields());
    }

    @Test
    public void createWithCreditCard() {
        CustomerRequest request = new CustomerRequest();
        request.firstName("Fred").
            creditCard().
                cardholderName("Fred Jones").
                number("5105105105105100").
                cvv("123").
                expirationDate("05/12").
                done().
            lastName("Jones");

        Result<Customer> result = gateway.customer().create(request);
        assertTrue(result.isSuccess());
        Customer customer = result.getTarget();

        assertEquals("Fred", customer.getFirstName());
        assertEquals("Jones", customer.getLastName());
        assertEquals(1, customer.getCreditCards().size());

        CreditCard creditCard = customer.getCreditCards().get(0);
        assertEquals("Fred Jones", creditCard.getCardholderName());
        assertEquals("510510", creditCard.getBin());
        assertEquals("5100", creditCard.getLast4());
        assertEquals("05/2012", creditCard.getExpirationDate());
        assertTrue(creditCard.getUniqueNumberIdentifier().matches("\\A\\w{32}\\z"));
    }

    @Test
    public void createWithDuplicateCreditCard() {
        CustomerRequest customerRequest = new CustomerRequest();
        customerRequest.firstName("Fred").
            creditCard().
                cardholderName("John Doe").
                number("4012000033330026").
                cvv("200").
                expirationDate("05/12").
                options().
                    failOnDuplicatePaymentMethod(true).
                    done().
                done().
            lastName("Jones");

        gateway.customer().create(customerRequest);
        Result<Customer> result = gateway.customer().create(customerRequest);

        assertFalse(result.isSuccess());
        assertEquals(
                ValidationErrorCode.CREDIT_CARD_DUPLICATE_CARD_EXISTS,
                result.getErrors().forObject("customer").forObject("creditCard").onField("number").get(0).getCode()
        );
    }

    @Test
    public void createWithValidCreditCardAndVerification() {
        CustomerRequest request = new CustomerRequest();
        request.firstName("Fred").
            creditCard().
                cardholderName("Fred Jones").
                number("4111111111111111").
                cvv("123").
                expirationDate("05/12").
                options().
                    verifyCard(true).
                    done().
                done().
            lastName("Jones");

        Result<Customer> result = gateway.customer().create(request);
        assertTrue(result.isSuccess());
        Customer customer = result.getTarget();

        assertEquals("Fred", customer.getFirstName());
        assertEquals("Jones", customer.getLastName());
        assertEquals(1, customer.getCreditCards().size());

        CreditCard creditCard = customer.getCreditCards().get(0);
        assertEquals("Fred Jones", creditCard.getCardholderName());
        assertEquals("411111", creditCard.getBin());
        assertEquals("1111", creditCard.getLast4());
        assertEquals("05/2012", creditCard.getExpirationDate());
    }

    @Test
    public void createWithInvalidCreditCardAndVerification() {
        CustomerRequest request = new CustomerRequest();
        request.firstName("Fred").
            creditCard().
                cardholderName("Fred Jones").
                number("5105105105105100").
                cvv("123").
                expirationDate("05/12").
                options().
                    verifyCard(true).
                    done().
                done().
            lastName("Jones");

        Result<Customer> result = gateway.customer().create(request);
        assertFalse(result.isSuccess());
        CreditCardVerification verification = result.getCreditCardVerification();
        assertEquals(CreditCardVerification.Status.PROCESSOR_DECLINED, verification.getStatus());
    }

    @Test
    public void createWithCreditCardAndBillingAddress() {
        CustomerRequest request = new CustomerRequest();
        request.firstName("Fred").
            creditCard().
                cardholderName("Fred Jones").
                number("5105105105105100").
                cvv("123").
                expirationDate("05/12").
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
                done().
            lastName("Jones");

        Result<Customer> result = gateway.customer().create(request);
        assertTrue(result.isSuccess());
        Customer customer = gateway.customer().create(request).getTarget();

        assertEquals("Fred", customer.getFirstName());
        assertEquals("Jones", customer.getLastName());
        assertEquals(1, customer.getCreditCards().size());

        CreditCard creditCard = customer.getCreditCards().get(0);
        assertEquals("Fred Jones", creditCard.getCardholderName());
        assertEquals("510510", creditCard.getBin());
        assertEquals("5100", creditCard.getLast4());
        assertEquals("05/2012", creditCard.getExpirationDate());

        Address billingAddress = creditCard.getBillingAddress();
        assertEquals("1 E Main St", billingAddress.getStreetAddress());
        assertEquals("Unit 2", billingAddress.getExtendedAddress());
        assertEquals("Chicago", billingAddress.getLocality());
        assertEquals("Illinois", billingAddress.getRegion());
        assertEquals("60607", billingAddress.getPostalCode());
        assertEquals("United States of America", billingAddress.getCountryName());
        assertEquals("US", billingAddress.getCountryCodeAlpha2());
        assertEquals("USA", billingAddress.getCountryCodeAlpha3());
        assertEquals("840", billingAddress.getCountryCodeNumeric());

        assertEquals(1, customer.getAddresses().size());
        Address address = customer.getAddresses().get(0);
        assertEquals("1 E Main St", address.getStreetAddress());
        assertEquals("Unit 2", address.getExtendedAddress());
        assertEquals("Chicago", address.getLocality());
        assertEquals("Illinois", address.getRegion());
        assertEquals("60607", address.getPostalCode());
        assertEquals("United States of America", address.getCountryName());
    }

    @Test
    public void createWithCreditCardAndBillingAddressWithErrors() {
        CustomerRequest request = new CustomerRequest().
            firstName("Fred").
            creditCard().
                cardholderName("Fred Jones").
                number("5105105105105100").
                cvv("123").
                expirationDate("05/12").
                billingAddress().
                    countryName("United States of America").
                    countryCodeAlpha2("MX").
                    done().
                done();

        Result<Customer> result = gateway.customer().create(request);
        assertFalse(result.isSuccess());
        assertEquals(
            ValidationErrorCode.ADDRESS_INCONSISTENT_COUNTRY,
            result.getErrors().forObject("customer").forObject("creditCard").forObject("billingAddress").onField("base").get(0).getCode()
        );
    }

    @Test
    public void createWithVenmoSdkPaymentMethodCode() {
        CustomerRequest request = new CustomerRequest().
            firstName("Fred").
            creditCard().
                venmoSdkPaymentMethodCode(VenmoSdk.generateTestPaymentMethodCode("5105105105105100")).
                done();

        Result<Customer> result = gateway.customer().create(request);
        assertTrue(result.isSuccess());
        assertEquals("510510", result.getTarget().getCreditCards().get(0).getBin());
    }

    @Test
    public void createWithVenmoSdkSession() {
        CustomerRequest request = new CustomerRequest().
            firstName("Fred").
            creditCard().
                number("5105105105105100").
                cvv("123").
                expirationDate("05/12").
                options().
                  venmoSdkSession(VenmoSdk.Session.Valid.value).
                  done().
                done();

        Result<Customer> result = gateway.customer().create(request);
        assertTrue(result.isSuccess());
        assertTrue(result.getTarget().getCreditCards().get(0).isVenmoSdk());
    }

    @Test
    public void createWithPaymentMethodNonce() {
        String nonce = TestHelper.generateUnlockedNonce(gateway);
        CustomerRequest request = new CustomerRequest().
            firstName("Fred").
            creditCard().
              paymentMethodNonce(nonce).
              done();

        Result<Customer> result = gateway.customer().create(request);
        assertTrue(result.isSuccess());
        assertEquals(1, result.getTarget().getCreditCards().size());
    }


    @Test
    public void createWithFuturePaymentPayPalAccountNonce() {
        String nonce = TestHelper.generateFuturePaymentPayPalNonce(gateway);
        CustomerRequest request = new CustomerRequest().
            paymentMethodNonce(nonce);

        Result<Customer> result = gateway.customer().create(request);
        assertTrue(result.isSuccess());
        assertEquals(1, result.getTarget().getPayPalAccounts().size());
    }

    @Test
    public void createWithOneTimePayPalAccountNonce() {
        String nonce = TestHelper.generateOneTimePayPalNonce(gateway);
        CustomerRequest request = new CustomerRequest().
            paymentMethodNonce(nonce);

        Result<Customer> result = gateway.customer().create(request);
        assertFalse(result.isSuccess());
        assertEquals(1, result.getErrors().getAllDeepValidationErrors().size());
    }

    @Test
    public void createCustomerFromTransparentRedirect() {
        CustomerRequest request = new CustomerRequest().firstName("John");
        CustomerRequest trParams = new CustomerRequest().
            lastName("Fred").
            creditCard().
                cardholderName("Fred Jones").
                number("5105105105105100").
                cvv("123").
                expirationDate("05/12").
                billingAddress().
                    countryName("United States of America").
                    countryCodeAlpha2("US").
                    countryCodeAlpha3("USA").
                    countryCodeNumeric("840").
                    done().
                done();
        String queryString = TestHelper.simulateFormPostForTR(gateway, trParams, request, gateway.transparentRedirect().url());

        Result<Customer> result = gateway.transparentRedirect().confirmCustomer(queryString);

        assertTrue(result.isSuccess());
        assertEquals("John", result.getTarget().getFirstName());
        assertEquals("Fred", result.getTarget().getLastName());

        assertEquals("United States of America", result.getTarget().getCreditCards().get(0).getBillingAddress().getCountryName());
        assertEquals("US", result.getTarget().getCreditCards().get(0).getBillingAddress().getCountryCodeAlpha2());
        assertEquals("USA", result.getTarget().getCreditCards().get(0).getBillingAddress().getCountryCodeAlpha3());
        assertEquals("840", result.getTarget().getCreditCards().get(0).getBillingAddress().getCountryCodeNumeric());
    }

    @Test
    public void createCustomerFromTransparentRedirectWithErrors() {
        CustomerRequest request = new CustomerRequest().firstName("John");
        CustomerRequest trParams = new CustomerRequest().
            lastName("Fred").
            creditCard().
                cardholderName("Fred Jones").
                number("5105105105105100").
                cvv("123").
                expirationDate("05/12").
                billingAddress().
                    countryName("United States of America").
                    countryCodeAlpha2("MX").
                    done().
                done();
        String queryString = TestHelper.simulateFormPostForTR(gateway, trParams, request, gateway.transparentRedirect().url());

        Result<Customer> result = gateway.transparentRedirect().confirmCustomer(queryString);

        assertFalse(result.isSuccess());

        assertEquals(
            ValidationErrorCode.ADDRESS_INCONSISTENT_COUNTRY,
            result.getErrors().forObject("customer").forObject("creditCard").forObject("billingAddress").onField("base").get(0).getCode()
        );
    }

    @Test
    public void find() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        Customer foundCustomer = gateway.customer().find(customer.getId());
        assertEquals(customer.getId(), foundCustomer.getId());
    }

    @Test
    public void findWithEmptyIds() {
        try {
            gateway.customer().find(" ");
            fail("Should throw NotFoundException");
        } catch (NotFoundException e) {
        }

    }

    @Test
    public void createWithApplePayCard() {
        CustomerRequest request = new CustomerRequest().
            paymentMethodNonce(Nonce.ApplePayVisa);
        Customer customer = gateway.customer().create(request).getTarget();

        Customer foundCustomer = gateway.customer().find(customer.getId());
        assertEquals(customer.getId(), foundCustomer.getId());
        assertNotNull(foundCustomer.getApplePayCards());
        assertEquals(1, foundCustomer.getApplePayCards().size());
        ApplePayCard card = foundCustomer.getApplePayCards().get(0);
        assertNotNull(card);
        assertNotNull(card.getExpirationMonth());
        assertEquals(1, foundCustomer.getPaymentMethods().size());
    }

    @Test
    public void createWithAndroidPayProxyCard() {
        CustomerRequest request = new CustomerRequest().
            paymentMethodNonce(Nonce.AndroidPayDiscover);
        Customer customer = gateway.customer().create(request).getTarget();

        Customer foundCustomer = gateway.customer().find(customer.getId());
        assertEquals(customer.getId(), foundCustomer.getId());
        assertNotNull(foundCustomer.getAndroidPayCards());
        assertEquals(1, foundCustomer.getAndroidPayCards().size());
        AndroidPayCard card = foundCustomer.getAndroidPayCards().get(0);
        assertNotNull(card);
        assertNotNull(card.getGoogleTransactionId());
        assertEquals(1, foundCustomer.getPaymentMethods().size());
    }

    @Test
    public void createWithAndroidPayNetworkToken() {
        CustomerRequest request = new CustomerRequest().
            paymentMethodNonce(Nonce.AndroidPayMasterCard);
        Customer customer = gateway.customer().create(request).getTarget();

        Customer foundCustomer = gateway.customer().find(customer.getId());
        assertEquals(customer.getId(), foundCustomer.getId());
        assertNotNull(foundCustomer.getAndroidPayCards());
        assertEquals(1, foundCustomer.getAndroidPayCards().size());
        AndroidPayCard card = foundCustomer.getAndroidPayCards().get(0);
        assertNotNull(card);
        assertNotNull(card.getGoogleTransactionId());
        assertEquals(1, foundCustomer.getPaymentMethods().size());
    }

    @Test
    public void findDuplicateCreditCardsGivenPaymentMethodToken() {
        CustomerRequest request = new CustomerRequest().
                creditCard().
                    number("4012000033330026").
                    expirationDate("05/2010").
                    done();

        Customer jim = gateway.customer().create(request.firstName("Jim")).getTarget();
        Customer joe = gateway.customer().create(request.firstName("Joe")).getTarget();

        CustomerSearchRequest searchRequest = new CustomerSearchRequest().
            paymentMethodTokenWithDuplicates().is(jim.getCreditCards().get(0).getToken());

        ResourceCollection<Customer> collection = gateway.customer().search(searchRequest);

        List<String> customerIds = new ArrayList<String>();
        for (Customer customer : collection) {
            customerIds.add(customer.getId());
        }

        assertTrue(customerIds.contains(jim.getId()));
        assertTrue(customerIds.contains(joe.getId()));
    }

    @Test
    public void searchOnAllTextFields() {
        String creditCardToken = String.valueOf(new Random().nextInt());

        CustomerRequest request = new CustomerRequest().
            firstName("Timmy").
            lastName("O'Toole").
            company("O'Toole and Sons").
            email("timmy@example.com").
            website("http://example.com").
            fax("3145551234").
            phone("5551231234").
            creditCard().
                cardholderName("Tim Toole").
                number("4111111111111111").
                expirationDate("05/2010").
                token(creditCardToken).
                billingAddress().
                    firstName("Thomas").
                    lastName("Otool").
                    streetAddress("1 E Main St").
                    extendedAddress("Suite 3").
                    locality("Chicago").
                    region("Illinois").
                    postalCode("60622").
                    countryName("United States of America").
                    done().
                done();

        Customer customer = gateway.customer().create(request).getTarget();

        CustomerSearchRequest searchRequest = new CustomerSearchRequest().
            id().is(customer.getId()).
            firstName().is("Timmy").
            lastName().is("O'Toole").
            company().is("O'Toole and Sons").
            email().is("timmy@example.com").
            phone().is("5551231234").
            fax().is("3145551234").
            website().is("http://example.com").
            addressFirstName().is("Thomas").
            addressLastName().is("Otool").
            addressStreetAddress().is("1 E Main St").
            addressPostalCode().is("60622").
            addressExtendedAddress().is("Suite 3").
            addressLocality().is("Chicago").
            addressRegion().is("Illinois").
            addressCountryName().is("United States of America").
            paymentMethodToken().is(creditCardToken).
            cardholderName().is("Tim Toole").
            creditCardNumber().is("4111111111111111").
            creditCardExpirationDate().is("05/2010");

        ResourceCollection<Customer> collection = gateway.customer().search(searchRequest);

        assertEquals(1, collection.getMaximumSize());
        assertEquals(customer.getId(), collection.getFirst().getId());
    }

    @Test
    public void searchOnCreatedAt() {
        CustomerRequest request = new CustomerRequest();

        Customer customer = gateway.customer().create(request).getTarget();

        Calendar createdAt = customer.getCreatedAt();

        Calendar threeHoursEarlier = ((Calendar) createdAt.clone());
        threeHoursEarlier.add(Calendar.HOUR_OF_DAY, -3);

        Calendar oneHourEarlier = ((Calendar) createdAt.clone());
        oneHourEarlier.add(Calendar.HOUR_OF_DAY, -1);

        Calendar oneHourLater = ((Calendar) createdAt.clone());
        oneHourLater.add(Calendar.HOUR_OF_DAY, 1);

        CustomerSearchRequest searchRequest = new CustomerSearchRequest().
             id().is(customer.getId()).
             createdAt().between(oneHourEarlier, oneHourLater);

        assertEquals(1, gateway.customer().search(searchRequest).getMaximumSize());

        searchRequest = new CustomerSearchRequest().
            id().is(customer.getId()).
            createdAt().greaterThanOrEqualTo(oneHourEarlier);

        assertEquals(1, gateway.customer().search(searchRequest).getMaximumSize());

        searchRequest = new CustomerSearchRequest().
            id().is(customer.getId()).
            createdAt().lessThanOrEqualTo(oneHourLater);

        assertEquals(1, gateway.customer().search(searchRequest).getMaximumSize());

        searchRequest = new CustomerSearchRequest().
            id().is(customer.getId()).
            createdAt().between(threeHoursEarlier, oneHourEarlier);

        assertEquals(0, gateway.customer().search(searchRequest).getMaximumSize());
    }

    @Test
    public void searchOnPayPalAccountEmail() {
        String nonce = TestHelper.generateFuturePaymentPayPalNonce(gateway);
        CustomerRequest request = new CustomerRequest().
            paymentMethodNonce(nonce);

        Result<Customer> result = gateway.customer().create(request);
        assertTrue(result.isSuccess());
        Customer customer = result.getTarget();

        CustomerSearchRequest searchRequest = new CustomerSearchRequest().
             id().is(customer.getId()).
             paypalAccountEmail().is("jane.doe@example.com");

        assertEquals(1, gateway.customer().search(searchRequest).getMaximumSize());
    }

    @Test
    public void update() {
        CustomerRequest request = new CustomerRequest().
            firstName("Mark").
            lastName("Jones").
            company("Jones Co.").
            email("mark.jones@example.com").
            fax("419-555-1234").
            phone("614-555-1234").
            website("http://example.com");
        Customer customer = gateway.customer().create(request).getTarget();

        CustomerRequest updateRequest = new CustomerRequest().
            firstName("Drew").
            lastName("Olson").
            company("Braintree").
            email("drew.olson@example.com").
            fax("555-555-5555").
            phone("555-555-5554").
            website("http://getbraintree.com");

        Result<Customer> updateResult = gateway.customer().update(customer.getId(), updateRequest);
        assertTrue(updateResult.isSuccess());
        Customer updatedCustomer = updateResult.getTarget();
        assertEquals("Drew", updatedCustomer.getFirstName());
        assertEquals("Olson", updatedCustomer.getLastName());
        assertEquals("Braintree", updatedCustomer.getCompany());
        assertEquals("drew.olson@example.com", updatedCustomer.getEmail());
        assertEquals("555-555-5555", updatedCustomer.getFax());
        assertEquals("555-555-5554", updatedCustomer.getPhone());
        assertEquals("http://getbraintree.com", updatedCustomer.getWebsite());
    }

    @Test
    public void updateWithExistingCreditCardAndAddress() {
        CustomerRequest request = new CustomerRequest().
            firstName("Mark").
            lastName("Jones").
            company("Jones Co.").
            email("mark.jones@example.com").
            fax("419-555-1234").
            phone("614-555-1234").
            website("http://example.com").
            creditCard().
                number("4111111111111111").
                expirationDate("12/12").
                billingAddress().
                    postalCode("44444").
                    done().
                done();

        Customer customer = gateway.customer().create(request).getTarget();
        CreditCard creditCard = customer.getCreditCards().get(0);

        CustomerRequest updateRequest = new CustomerRequest().
            firstName("Jane").
            lastName("Doe").
            creditCard().
                expirationDate("10/10").
                options().
                    updateExistingToken(creditCard.getToken()).
                    done().
                billingAddress().
                    postalCode("11111").
                    countryName("Kiribati").
                    countryCodeAlpha2("KI").
                    countryCodeAlpha3("KIR").
                    countryCodeNumeric("296").
                    options().
                        updateExisting(true).
                        done().
                    done().
                done();

        Customer updatedCustomer = gateway.customer().update(customer.getId(), updateRequest).getTarget();
        CreditCard updatedCreditCard = updatedCustomer.getCreditCards().get(0);
        Address updatedAddress = updatedCreditCard.getBillingAddress();

        assertEquals("Jane", updatedCustomer.getFirstName());
        assertEquals("Doe", updatedCustomer.getLastName());
        assertEquals("10/2010", updatedCreditCard.getExpirationDate());
        assertEquals("11111", updatedAddress.getPostalCode());
        assertEquals("Kiribati", updatedAddress.getCountryName());
        assertEquals("KI", updatedAddress.getCountryCodeAlpha2());
        assertEquals("KIR", updatedAddress.getCountryCodeAlpha3());
        assertEquals("296", updatedAddress.getCountryCodeNumeric());
    }

    @Test
    public void updateWithNewCreditCardAndExistingAddress() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        AddressRequest addressRequest = new AddressRequest().
            firstName("John");
        Address address = gateway.address().create(customer.getId(), addressRequest).getTarget();

        CustomerRequest updateRequest = new CustomerRequest().
            creditCard().
                number("4111111111111111").
                expirationDate("12/12").
                billingAddressId(address.getId()).
                done();

        Customer updatedCustomer = gateway.customer().update(customer.getId(), updateRequest).getTarget();

        Address updatedAddress = updatedCustomer.getCreditCards().get(0).getBillingAddress();

        assertEquals(address.getId(), updatedAddress.getId());
        assertEquals("John", updatedAddress.getFirstName());
    }

    @Test
    public void updateWithExistingCreditCardAndAddressAndValidationErrors() {
        CustomerRequest request = new CustomerRequest().
            firstName("Mark").
            lastName("Jones").
            company("Jones Co.").
            email("mark.jones@example.com").
            fax("419-555-1234").
            phone("614-555-1234").
            website("http://example.com").
            creditCard().
                number("4111111111111111").
                expirationDate("12/12").
                billingAddress().
                    postalCode("44444").
                    done().
                done();

        Customer customer = gateway.customer().create(request).getTarget();

        CustomerRequest updateRequest = new CustomerRequest().
            firstName("Janie").
            lastName("Dylan").
            creditCard().
                billingAddress().
                    countryCodeAlpha2("KI").
                    countryCodeAlpha3("USA").
                    done().
                done();

        Result<Customer> result = gateway.customer().update(customer.getId(), updateRequest);
        assertFalse(result.isSuccess());
        assertEquals(
            ValidationErrorCode.ADDRESS_INCONSISTENT_COUNTRY,
            result.getErrors().forObject("customer").forObject("creditCard").forObject("billingAddress").onField("base").get(0).getCode()
        );
    }

    @Test
    public void updateViaTrWithExistingCreditCardAndAddress() {
        CustomerRequest request = new CustomerRequest().
            firstName("Mark").
            lastName("Jones").
            company("Jones Co.").
            email("mark.jones@example.com").
            fax("419-555-1234").
            phone("614-555-1234").
            website("http://example.com").
            creditCard().
                number("4111111111111111").
                expirationDate("12/12").
                billingAddress().
                    postalCode("44444").
                    done().
                done();

        Customer customer = gateway.customer().create(request).getTarget();
        CreditCard creditCard = customer.getCreditCards().get(0);

        CustomerRequest trParams = new CustomerRequest().
            customerId(customer.getId()).
            firstName("Jane").
            lastName("Doe").
            creditCard().
                expirationDate("10/10").
                options().
                    updateExistingToken(creditCard.getToken()).
                    done().
                billingAddress().
                    postalCode("11111").
                    options().
                        updateExisting(true).
                        done().
                    done().
                done();

        String queryString = TestHelper.simulateFormPostForTR(gateway, trParams, new CustomerRequest(), gateway.customer().transparentRedirectURLForUpdate());

        Customer updatedCustomer = gateway.customer().confirmTransparentRedirect(queryString).getTarget();
        CreditCard updatedCreditCard = updatedCustomer.getCreditCards().get(0);
        Address updatedAddress = updatedCreditCard.getBillingAddress();

        assertEquals("Jane", updatedCustomer.getFirstName());
        assertEquals("Doe", updatedCustomer.getLastName());
        assertEquals("10/2010", updatedCreditCard.getExpirationDate());
        assertEquals("11111", updatedAddress.getPostalCode());
    }

    @Test
    public void updateViaTransparentRedirect() {
        CustomerRequest createRequest = new CustomerRequest().
            firstName("Mark").
            lastName("Jones").
            company("Jones Co.").
            email("mark.jones@example.com").
            fax("419-555-1234").
            phone("614-555-1234").
            website("http://example.com");

        Customer createdCustomer = gateway.customer().create(createRequest).getTarget();

        CustomerRequest request = new CustomerRequest().
            firstName("Drew").
            lastName("Olson").
            company("Braintree").
            email("drew.olson@example.com").
            fax("555-555-5555").
            phone("555-555-5554").
            website("http://getbraintree.com");

        CustomerRequest trParams = new CustomerRequest().customerId(createdCustomer.getId());

        String queryString = TestHelper.simulateFormPostForTR(gateway, trParams, request, gateway.customer().transparentRedirectURLForUpdate());
        Result<Customer> result = gateway.customer().confirmTransparentRedirect(queryString);

        assertTrue(result.isSuccess());
        Customer customer = result.getTarget();
        assertEquals("Drew", customer.getFirstName());
        assertEquals("Olson", customer.getLastName());
        assertEquals("Braintree", customer.getCompany());
        assertEquals("drew.olson@example.com", customer.getEmail());
        assertEquals("555-555-5555", customer.getFax());
        assertEquals("555-555-5554", customer.getPhone());
        assertEquals("http://getbraintree.com", customer.getWebsite());
    }

    @Test
    public void updateCustomerFromTransparentRedirectWithAddress() {
        CustomerRequest request = new CustomerRequest().
            firstName("John").
            lastName("Doe").
            creditCard().
                number("4111111111111111").
                expirationDate("12/12").
                billingAddress().
                    countryName("United States of America").
                    countryCodeAlpha2("US").
                    countryCodeAlpha3("USA").
                    countryCodeNumeric("840").
                    done().
                done();

        Customer customer = gateway.customer().create(request).getTarget();

        CustomerRequest updateRequest = new CustomerRequest().
            firstName("Jane");

        CustomerRequest trParams = new CustomerRequest().
            customerId(customer.getId()).
            lastName("Dough").
            creditCard().
                options().
                    updateExistingToken(customer.getCreditCards().get(0).getToken()).
                    done().
                billingAddress().
                    countryName("Mexico").
                    countryCodeAlpha2("MX").
                    countryCodeAlpha3("MEX").
                    countryCodeNumeric("484").
                    options().
                        updateExisting(true).
                        done().
                    done().
                done();

        String queryString = TestHelper.simulateFormPostForTR(gateway, trParams, updateRequest, gateway.transparentRedirect().url());

        Result<Customer> result = gateway.transparentRedirect().confirmCustomer(queryString);

        assertTrue(result.isSuccess());
        Customer updatedCustomer = gateway.customer().find(customer.getId());
        assertEquals("Jane", updatedCustomer.getFirstName());
        assertEquals("Dough", updatedCustomer.getLastName());
        assertEquals("Mexico", updatedCustomer.getCreditCards().get(0).getBillingAddress().getCountryName());
        assertEquals("MX", updatedCustomer.getCreditCards().get(0).getBillingAddress().getCountryCodeAlpha2());
        assertEquals("MEX", updatedCustomer.getCreditCards().get(0).getBillingAddress().getCountryCodeAlpha3());
        assertEquals("484", updatedCustomer.getCreditCards().get(0).getBillingAddress().getCountryCodeNumeric());
    }

    @Test
    public void updateCustomerFromTransparentRedirectWithAddressWithErrors() {
        CustomerRequest request = new CustomerRequest().
            firstName("John").
            lastName("Doe").
            creditCard().
                number("4111111111111111").
                expirationDate("12/12").
                billingAddress().
                    countryName("United States of America").
                    countryCodeAlpha2("US").
                    countryCodeAlpha3("USA").
                    countryCodeNumeric("840").
                    done().
                done();

        Customer customer = gateway.customer().create(request).getTarget();

        CustomerRequest updateRequest = new CustomerRequest().
            firstName("Jane");

        CustomerRequest trParams = new CustomerRequest().
            customerId(customer.getId()).
            creditCard().
                billingAddress().
                    countryCodeAlpha2("MX").
                    countryCodeAlpha3("USA").
                    done().
                done();

        String queryString = TestHelper.simulateFormPostForTR(gateway, trParams, updateRequest, gateway.transparentRedirect().url());

        Result<Customer> result = gateway.transparentRedirect().confirmCustomer(queryString);

        assertFalse(result.isSuccess());
        assertEquals(
            ValidationErrorCode.ADDRESS_INCONSISTENT_COUNTRY,
            result.getErrors().forObject("customer").forObject("creditCard").forObject("billingAddress").onField("base").get(0).getCode()
        );
    }

    @Test
    public void updateToken() {
        Random rand = new Random();
        String oldId = String.valueOf(rand.nextInt());
        CustomerRequest request = new CustomerRequest().
            id(oldId);
        Result<Customer> result = gateway.customer().create(request);
        assertTrue(result.isSuccess());

        Customer customer = result.getTarget();

        String newId = String.valueOf(rand.nextInt());
        CustomerRequest updateRequest = new CustomerRequest().
            id(newId);

        Customer updatedCustomer = gateway.customer().update(customer.getId(), updateRequest).getTarget();
        assertEquals(newId, updatedCustomer.getId());
    }

    @Test
    public void updateOnlySomeAttributes() {
        CustomerRequest request = new CustomerRequest().
            firstName("Mark").
            lastName("Jones").
            company("Jones Co.").
            email("mark.jones@example.com").
            fax("419-555-1234").
            phone("614-555-1234").
            website("http://example.com");
        Result<Customer> result = gateway.customer().create(request);
        assertTrue(result.isSuccess());

        Customer customer = result.getTarget();

        CustomerRequest updateRequest = new CustomerRequest().
            lastName("Olson").
            company("Braintree");

        Result<Customer> updateResult = gateway.customer().update(customer.getId(), updateRequest);
        assertTrue(updateResult.isSuccess());
        Customer updatedCustomer = updateResult.getTarget();

        assertEquals("Mark", updatedCustomer.getFirstName());
        assertEquals("Olson", updatedCustomer.getLastName());
        assertEquals("Braintree", updatedCustomer.getCompany());
        assertEquals("mark.jones@example.com", updatedCustomer.getEmail());
        assertEquals("419-555-1234", updatedCustomer.getFax());
        assertEquals("614-555-1234", updatedCustomer.getPhone());
        assertEquals("http://example.com", updatedCustomer.getWebsite());
    }

    @Test
    public void updateWithFuturePaymentPayPalAccountNonce() {
        CustomerRequest request = new CustomerRequest().
            creditCard().
                number("4111111111111111").
                expirationDate("12/12").
                done();

        Customer customer = gateway.customer().create(request).getTarget();
        assertEquals(customer.getDefaultPaymentMethod().getToken(), customer.getCreditCards().get(0).getToken());

        String nonce = TestHelper.generateFuturePaymentPayPalNonce(gateway);
        CustomerRequest updateRequest = new CustomerRequest().
            paymentMethodNonce(nonce);

        Result<Customer> updateResult = gateway.customer().update(customer.getId(), updateRequest);
        assertTrue(updateResult.isSuccess());

        customer = updateResult.getTarget();
        assertEquals(1, customer.getPayPalAccounts().size());
    }

    @Test
    public void updateWithOneTimePayPalAccountNonce() {
        CustomerRequest request = new CustomerRequest().
            creditCard().
                number("4111111111111111").
                expirationDate("12/12").
                done();

        Customer customer = gateway.customer().create(request).getTarget();
        assertEquals(customer.getDefaultPaymentMethod().getToken(), customer.getCreditCards().get(0).getToken());

        String nonce = TestHelper.generateOneTimePayPalNonce(gateway);
        CustomerRequest updateRequest = new CustomerRequest().
            paymentMethodNonce(nonce);

        Result<Customer> updateResult = gateway.customer().update(customer.getId(), updateRequest);
        assertFalse(updateResult.isSuccess());
    }

    @Test
    public void delete() {
        CustomerRequest request = new CustomerRequest().
            firstName("Mark").
            lastName("Jones").
            company("Jones Co.").
            email("mark.jones@example.com").
            fax("419-555-1234").
            phone("614-555-1234").
            website("http://example.com");
        Customer customer = gateway.customer().create(request).getTarget();

        Result<Customer> result = gateway.customer().delete(customer.getId());
        assertTrue(result.isSuccess());
        try {
            gateway.customer().find(customer.getId());
            fail();
        } catch (NotFoundException e) {
        }
    }

    @Test
    public void all() {
        ResourceCollection<Customer> resourceCollection = gateway.customer().all();

        assertTrue(resourceCollection.getMaximumSize() > 0);
        assertNotNull(resourceCollection.getFirst());
    }
}
