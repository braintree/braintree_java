package com.braintreegateway;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.braintreegateway.exceptions.ForgedQueryStringException;
import com.braintreegateway.exceptions.NotFoundException;

@SuppressWarnings("deprecation")
public class CustomerTest {

    private BraintreeGateway gateway;

    @Before
    public void createGateway() {
        this.gateway = new BraintreeGateway(Environment.DEVELOPMENT, "integration_merchant_id", "integration_public_key", "integration_private_key");
    }

    @Test
    public void transparentRedirectURLForCreate() {
        Assert.assertEquals(gateway.baseMerchantURL() + "/customers/all/create_via_transparent_redirect_request",
                gateway.customer().transparentRedirectURLForCreate());
    }

    @Test
    public void transparentRedirectURLForUpdate() {
        Assert.assertEquals(gateway.baseMerchantURL() + "/customers/all/update_via_transparent_redirect_request",
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
        Assert.assertTrue(result.isSuccess());
        Customer customer = result.getTarget();

        Assert.assertEquals("Mark", customer.getFirstName());
        Assert.assertEquals("Jones", customer.getLastName());
        Assert.assertEquals("Jones Co.", customer.getCompany());
        Assert.assertEquals("mark.jones@example.com", customer.getEmail());
        Assert.assertEquals("419-555-1234", customer.getFax());
        Assert.assertEquals("614-555-1234", customer.getPhone());
        Assert.assertEquals("http://example.com", customer.getWebsite());
        Assert.assertEquals(Calendar.getInstance().get(Calendar.YEAR), customer.getCreatedAt().get(Calendar.YEAR));
        Assert.assertEquals(Calendar.getInstance().get(Calendar.YEAR), customer.getUpdatedAt().get(Calendar.YEAR));
    }

    @Test
    public void createViaTransparentRedirect() {
        CustomerRequest trParams = new CustomerRequest();
        CustomerRequest request = new CustomerRequest().firstName("John").lastName("Doe");

        String queryString = TestHelper.simulateFormPostForTR(gateway, trParams, request, gateway.customer().transparentRedirectURLForCreate());
        Result<Customer> result = gateway.customer().confirmTransparentRedirect(queryString);

        Assert.assertTrue(result.isSuccess());
        Customer customer = result.getTarget();
        Assert.assertEquals("John", customer.getFirstName());
        Assert.assertEquals("Doe", customer.getLastName());
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

        Assert.assertTrue(result.isSuccess());
        Customer customer = result.getTarget();
        Assert.assertEquals("John", customer.getFirstName());
        Assert.assertEquals("Doe", customer.getLastName());
        Assert.assertEquals("1111", customer.getCreditCards().get(0).getLast4());
    }

    @Test
    public void createWithBlanks() {
        CustomerRequest request = new CustomerRequest();
        Result<Customer> result = gateway.customer().create(request);
        Assert.assertTrue(result.isSuccess());
        Customer customer = result.getTarget();
        Assert.assertEquals(null, customer.getFirstName());
        Assert.assertEquals(null, customer.getLastName());
        Assert.assertEquals(null, customer.getCompany());
        Assert.assertEquals(null, customer.getEmail());
        Assert.assertEquals(null, customer.getFax());
        Assert.assertEquals(null, customer.getPhone());
        Assert.assertEquals(null, customer.getWebsite());
    }

    @Test
    public void createWithCustomFields() {
        CustomerRequest request = new CustomerRequest().
            customField("store_me", "custom value").
            customField("another_stored_field", "custom value2");
        Result<Customer> result = gateway.customer().create(request);
        Assert.assertTrue(result.isSuccess());

        Map<String, String> expected = new HashMap<String, String>();
        expected.put("store_me", "custom value");
        expected.put("another_stored_field", "custom value2");

        Customer customer = result.getTarget();
        Assert.assertEquals(expected, customer.getCustomFields());
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
        Assert.assertTrue(result.isSuccess());
        Customer customer = result.getTarget();

        Assert.assertEquals("Fred", customer.getFirstName());
        Assert.assertEquals("Jones", customer.getLastName());
        Assert.assertEquals(1, customer.getCreditCards().size());

        CreditCard creditCard = customer.getCreditCards().get(0);
        Assert.assertEquals("Fred Jones", creditCard.getCardholderName());
        Assert.assertEquals("510510", creditCard.getBin());
        Assert.assertEquals("5100", creditCard.getLast4());
        Assert.assertEquals("05/2012", creditCard.getExpirationDate());
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
        Assert.assertTrue(result.isSuccess());
        Customer customer = result.getTarget();

        Assert.assertEquals("Fred", customer.getFirstName());
        Assert.assertEquals("Jones", customer.getLastName());
        Assert.assertEquals(1, customer.getCreditCards().size());

        CreditCard creditCard = customer.getCreditCards().get(0);
        Assert.assertEquals("Fred Jones", creditCard.getCardholderName());
        Assert.assertEquals("411111", creditCard.getBin());
        Assert.assertEquals("1111", creditCard.getLast4());
        Assert.assertEquals("05/2012", creditCard.getExpirationDate());
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
        Assert.assertFalse(result.isSuccess());
        CreditCardVerification verification = result.getCreditCardVerification();
        Assert.assertEquals(CreditCardVerification.Status.PROCESSOR_DECLINED, verification.getStatus());
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
        Assert.assertTrue(result.isSuccess());
        Customer customer = gateway.customer().create(request).getTarget();

        Assert.assertEquals("Fred", customer.getFirstName());
        Assert.assertEquals("Jones", customer.getLastName());
        Assert.assertEquals(1, customer.getCreditCards().size());

        CreditCard creditCard = customer.getCreditCards().get(0);
        Assert.assertEquals("Fred Jones", creditCard.getCardholderName());
        Assert.assertEquals("510510", creditCard.getBin());
        Assert.assertEquals("5100", creditCard.getLast4());
        Assert.assertEquals("05/2012", creditCard.getExpirationDate());

        Address billingAddress = creditCard.getBillingAddress();
        Assert.assertEquals("1 E Main St", billingAddress.getStreetAddress());
        Assert.assertEquals("Unit 2", billingAddress.getExtendedAddress());
        Assert.assertEquals("Chicago", billingAddress.getLocality());
        Assert.assertEquals("Illinois", billingAddress.getRegion());
        Assert.assertEquals("60607", billingAddress.getPostalCode());
        Assert.assertEquals("United States of America", billingAddress.getCountryName());
        Assert.assertEquals("US", billingAddress.getCountryCodeAlpha2());
        Assert.assertEquals("USA", billingAddress.getCountryCodeAlpha3());
        Assert.assertEquals("840", billingAddress.getCountryCodeNumeric());

        Assert.assertEquals(1, customer.getAddresses().size());
        Address address = customer.getAddresses().get(0);
        Assert.assertEquals("1 E Main St", address.getStreetAddress());
        Assert.assertEquals("Unit 2", address.getExtendedAddress());
        Assert.assertEquals("Chicago", address.getLocality());
        Assert.assertEquals("Illinois", address.getRegion());
        Assert.assertEquals("60607", address.getPostalCode());
        Assert.assertEquals("United States of America", address.getCountryName());
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
        Assert.assertFalse(result.isSuccess());
        Assert.assertEquals(
            ValidationErrorCode.ADDRESS_INCONSISTENT_COUNTRY,
            result.getErrors().forObject("customer").forObject("creditCard").forObject("billingAddress").onField("base").get(0).getCode()
        );
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
        
        Assert.assertTrue(result.isSuccess());
        Assert.assertEquals("John", result.getTarget().getFirstName());
        Assert.assertEquals("Fred", result.getTarget().getLastName());

        Assert.assertEquals("United States of America", result.getTarget().getCreditCards().get(0).getBillingAddress().getCountryName());
        Assert.assertEquals("US", result.getTarget().getCreditCards().get(0).getBillingAddress().getCountryCodeAlpha2());
        Assert.assertEquals("USA", result.getTarget().getCreditCards().get(0).getBillingAddress().getCountryCodeAlpha3());
        Assert.assertEquals("840", result.getTarget().getCreditCards().get(0).getBillingAddress().getCountryCodeNumeric());
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
        
        Assert.assertFalse(result.isSuccess());
        
        Assert.assertEquals(
            ValidationErrorCode.ADDRESS_INCONSISTENT_COUNTRY,
            result.getErrors().forObject("customer").forObject("creditCard").forObject("billingAddress").onField("base").get(0).getCode()
        );
    }

    @Test
    public void find() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        Customer foundCustomer = gateway.customer().find(customer.getId());
        Assert.assertEquals(customer.getId(), foundCustomer.getId());
    }
    
    @Test
    public void searchOnAllTextFields()
    {
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
            paymentMethodToken().is(creditCardToken).
            creditCardNumber().is("4111111111111111").
            creditCardExpirationDate().is("05/2010");

        ResourceCollection<Customer> collection = gateway.customer().search(searchRequest);

        Assert.assertEquals(1, collection.getMaximumSize());
        Assert.assertEquals(customer.getId(), collection.getFirst().getId());
    }
    
    @Test
    public void searchOnCreatedAt() {
        CustomerRequest request = new CustomerRequest();

         Customer customer = gateway.customer().create(request).getTarget();

         Calendar createdAt = customer.getCreatedAt();
         
         Calendar threeHoursEarlier = ((Calendar)createdAt.clone());
         threeHoursEarlier.add(Calendar.HOUR_OF_DAY, -3);
         
         Calendar oneHourEarlier = ((Calendar)createdAt.clone());
         oneHourEarlier.add(Calendar.HOUR_OF_DAY, -1);
         
         Calendar oneHourLater = ((Calendar)createdAt.clone());
         oneHourLater.add(Calendar.HOUR_OF_DAY, 1);
         
         CustomerSearchRequest searchRequest = new CustomerSearchRequest().
             id().is(customer.getId()).
             createdAt().between(oneHourEarlier, oneHourLater);

         Assert.assertEquals(1, gateway.customer().search(searchRequest).getMaximumSize());
         
         searchRequest = new CustomerSearchRequest().
             id().is(customer.getId()).
             createdAt().greaterThanOrEqualTo(oneHourEarlier);

         Assert.assertEquals(1, gateway.customer().search(searchRequest).getMaximumSize());
         
         searchRequest = new CustomerSearchRequest().
             id().is(customer.getId()).
             createdAt().lessThanOrEqualTo(oneHourLater);

         Assert.assertEquals(1, gateway.customer().search(searchRequest).getMaximumSize());
         
         searchRequest = new CustomerSearchRequest().
             id().is(customer.getId()).
             createdAt().between(threeHoursEarlier, oneHourEarlier);

         Assert.assertEquals(0, gateway.customer().search(searchRequest).getMaximumSize());
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
        Assert.assertTrue(updateResult.isSuccess());
        Customer updatedCustomer = updateResult.getTarget();
        Assert.assertEquals("Drew", updatedCustomer.getFirstName());
        Assert.assertEquals("Olson", updatedCustomer.getLastName());
        Assert.assertEquals("Braintree", updatedCustomer.getCompany());
        Assert.assertEquals("drew.olson@example.com", updatedCustomer.getEmail());
        Assert.assertEquals("555-555-5555", updatedCustomer.getFax());
        Assert.assertEquals("555-555-5554", updatedCustomer.getPhone());
        Assert.assertEquals("http://getbraintree.com", updatedCustomer.getWebsite());
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
        
        Assert.assertEquals("Jane", updatedCustomer.getFirstName());
        Assert.assertEquals("Doe", updatedCustomer.getLastName());
        Assert.assertEquals("10/2010", updatedCreditCard.getExpirationDate());
        Assert.assertEquals("11111", updatedAddress.getPostalCode());
        Assert.assertEquals("Kiribati", updatedAddress.getCountryName());
        Assert.assertEquals("KI", updatedAddress.getCountryCodeAlpha2());
        Assert.assertEquals("KIR", updatedAddress.getCountryCodeAlpha3());
        Assert.assertEquals("296", updatedAddress.getCountryCodeNumeric());
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

        Assert.assertEquals(address.getId(), updatedAddress.getId());
        Assert.assertEquals("John", updatedAddress.getFirstName());
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
        Assert.assertFalse(result.isSuccess());
        Assert.assertEquals(
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
        
        Assert.assertEquals("Jane", updatedCustomer.getFirstName());
        Assert.assertEquals("Doe", updatedCustomer.getLastName());
        Assert.assertEquals("10/2010", updatedCreditCard.getExpirationDate());
        Assert.assertEquals("11111", updatedAddress.getPostalCode());
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

        Assert.assertTrue(result.isSuccess());
        Customer customer = result.getTarget();
        Assert.assertEquals("Drew", customer.getFirstName());
        Assert.assertEquals("Olson", customer.getLastName());
        Assert.assertEquals("Braintree", customer.getCompany());
        Assert.assertEquals("drew.olson@example.com", customer.getEmail());
        Assert.assertEquals("555-555-5555", customer.getFax());
        Assert.assertEquals("555-555-5554", customer.getPhone());
        Assert.assertEquals("http://getbraintree.com", customer.getWebsite());
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
        
        Assert.assertTrue(result.isSuccess());
        Customer updatedCustomer = gateway.customer().find(customer.getId());
        Assert.assertEquals("Jane", updatedCustomer.getFirstName());
        Assert.assertEquals("Dough", updatedCustomer.getLastName());
        Assert.assertEquals("Mexico", updatedCustomer.getCreditCards().get(0).getBillingAddress().getCountryName());
        Assert.assertEquals("MX", updatedCustomer.getCreditCards().get(0).getBillingAddress().getCountryCodeAlpha2());
        Assert.assertEquals("MEX", updatedCustomer.getCreditCards().get(0).getBillingAddress().getCountryCodeAlpha3());
        Assert.assertEquals("484", updatedCustomer.getCreditCards().get(0).getBillingAddress().getCountryCodeNumeric());
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
        
        Assert.assertFalse(result.isSuccess());
        Assert.assertEquals(
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
        Assert.assertTrue(result.isSuccess());

        Customer customer = result.getTarget();

        String newId = String.valueOf(rand.nextInt());
        CustomerRequest updateRequest = new CustomerRequest().
            id(newId);

        Customer updatedCustomer = gateway.customer().update(customer.getId(), updateRequest).getTarget();
        Assert.assertEquals(newId, updatedCustomer.getId());
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
        Assert.assertTrue(result.isSuccess());

        Customer customer = result.getTarget();

        CustomerRequest updateRequest = new CustomerRequest().
            lastName("Olson").
            company("Braintree");

        Result<Customer> updateResult = gateway.customer().update(customer.getId(), updateRequest);
        Assert.assertTrue(updateResult.isSuccess());
        Customer updatedCustomer = updateResult.getTarget();

        Assert.assertEquals("Mark", updatedCustomer.getFirstName());
        Assert.assertEquals("Olson", updatedCustomer.getLastName());
        Assert.assertEquals("Braintree", updatedCustomer.getCompany());
        Assert.assertEquals("mark.jones@example.com", updatedCustomer.getEmail());
        Assert.assertEquals("419-555-1234", updatedCustomer.getFax());
        Assert.assertEquals("614-555-1234", updatedCustomer.getPhone());
        Assert.assertEquals("http://example.com", updatedCustomer.getWebsite());
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
        Assert.assertTrue(result.isSuccess());
        try {
            gateway.customer().find(customer.getId());
            Assert.fail();
        } catch (NotFoundException e) {
        }
    }
    
    @Test
    public void all() {
        ResourceCollection<Customer> resourceCollection = gateway.customer().all();

        Assert.assertTrue(resourceCollection.getMaximumSize() > 0);
        Assert.assertNotNull(resourceCollection.getFirst());
    }
}
