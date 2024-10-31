package com.braintreegateway.integrationtest;

import com.braintreegateway.testhelpers.MerchantAccountTestConstants;

import com.braintreegateway.*;
import com.braintreegateway.exceptions.NotFoundException;
import com.braintreegateway.test.Nonce;
import com.braintreegateway.testhelpers.TestHelper;
import com.braintreegateway.SandboxValues.CreditCardNumber;
import com.braintreegateway.testhelpers.ThreeDSecureRequestForTests;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CustomerIT extends IntegrationTest {

    @Test
    public void create() {
        CustomerRequest request = new CustomerRequest().
            firstName("Mark").
            lastName("Jones").
            company("Jones Co.").
            email("mark.jones@example.com").
            fax("419-555-1234").
            phone("614-555-1234").
            internationalPhone().
                countryCode("1").
                nationalNumber("3121234567").
                done().
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
        assertEquals("1", customer.getInternationalPhone().getCountryCode());
        assertEquals("3121234567", customer.getInternationalPhone().getNationalNumber());
        assertEquals("http://example.com", customer.getWebsite());
        assertEquals(Calendar.getInstance().get(Calendar.YEAR), customer.getCreatedAt().get(Calendar.YEAR));
        assertEquals(Calendar.getInstance().get(Calendar.YEAR), customer.getUpdatedAt().get(Calendar.YEAR));
        assertNotNull(customer.getGraphQLId());
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
    public void createWithRiskDataParams() {
        CustomerRequest request = new CustomerRequest().
            creditCard().
                number("4111111111111111").
                expirationDate("05/12").
                done().
            riskData().
                customerBrowser("IE6").
                customerIP("192.168.0.1").
                done();
        Result<Customer> result = gateway.customer().create(request);

        assertTrue(result.isSuccess());
    }

    @Test
    public void createIncludesRiskDataWhenSkipAdvancedFraudCheckingIsFalse() {
        createFraudProtectionEnterpriseMerchantGateway();
        CustomerRequest request = new CustomerRequest().
            creditCard().
              cardholderName("John Doe").
              cvv("123").
              number("4111111111111111").
              expirationDate("05/12").
              options().
                verifyCard(true).
                skipAdvancedFraudChecking(false).
                done().
            done();


        Result<Customer> result = gateway.customer().create(request);
        assertTrue(result.isSuccess());

        Customer customer = result.getTarget();
        assertNotNull(customer);

        CreditCardVerification verification = customer.getCreditCards().get(0).getVerification();
        assertNotNull(verification);

        RiskData riskData = verification.getRiskData();
        assertNotNull(riskData);
    }

    @Test
    public void createDoesNotIncludeRiskDataWhenSkipAdvancedFraudCheckingIsTrue() {
        createFraudProtectionEnterpriseMerchantGateway();
        CustomerRequest request = new CustomerRequest().
            creditCard().
              cardholderName("John Doe").
              cvv("123").
              number("4111111111111111").
              expirationDate("05/12").
              options().
                verifyCard(true).
                skipAdvancedFraudChecking(true).
                done().
            done();


        Result<Customer> result = gateway.customer().create(request);
        assertTrue(result.isSuccess());

        Customer customer = result.getTarget();
        assertNotNull(customer);

        CreditCardVerification verification = customer.getCreditCards().get(0).getVerification();
        assertNotNull(verification);

        RiskData riskData = verification.getRiskData();
        assertNull(riskData);
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
    public void createWithValidCreditCardAndVerificationAmount() {
        CustomerRequest request = new CustomerRequest();
        request.firstName("Fred").
            creditCard().
                cardholderName("Fred Jones").
                number("4111111111111111").
                cvv("123").
                expirationDate("05/12").
                options().
                    verifyCard(true).
                    verificationAmount("6.00").
                    done().
                done().
            lastName("Jones");

        Result<Customer> result = gateway.customer().create(request);
        assertTrue(result.isSuccess());
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
    public void createWithThreeDSecureNonce() {
        CustomerRequest request = new CustomerRequest().
            firstName("Fred").
            creditCard().
              paymentMethodNonce(Nonce.ThreeDSecureVisaFullAuthentication).
              options().
                  verifyCard(true).
                  done().
              done();

        Result<Customer> result = gateway.customer().create(request);
        assertTrue(result.isSuccess());

        Customer customer = result.getTarget();
        CreditCard creditCard = customer.getCreditCards().get(0);
        ThreeDSecureInfo threeDSecureInfo = creditCard.getVerification().getThreeDSecureInfo();

        assertEquals("authenticate_successful", threeDSecureInfo.getStatus());
        assertEquals(true, threeDSecureInfo.isLiabilityShifted());
        assertEquals(true, threeDSecureInfo.isLiabilityShiftPossible());
        assertNotNull(threeDSecureInfo.getEnrolled());
        assertNotNull(threeDSecureInfo.getCAVV());
        assertNotNull(threeDSecureInfo.getECIFlag());
        assertNotNull(threeDSecureInfo.getXID());
        assertNotNull(threeDSecureInfo.getThreeDSecureVersion());
    }

    @Test
    public void createWithThreeDSecureAuthenticationId() {
        CreditCardRequest card_request = new CreditCardRequest().
            number("4111111111111111").
            expirationMonth("05").
            expirationYear("2026");

        String nonceWithout3DS = TestHelper.generateNonceForCreditCard(gateway, card_request, null, false);

        String threeDSecureAuthenticationId = TestHelper.createTest3DS(gateway, null, new ThreeDSecureRequestForTests().
            number("4111111111111111").
            expirationMonth("05").
            expirationYear("2026")
        );

        CustomerRequest request = new CustomerRequest().
            threeDSecureAuthenticationId(threeDSecureAuthenticationId).
            paymentMethodNonce(nonceWithout3DS).
            creditCard().
                options().
                    verifyCard(true).
                    done().
                done();

        Result<Customer> result = gateway.customer().create(request);
        assertTrue(result.isSuccess());

        Customer customer = result.getTarget();
        CreditCard creditCard = customer.getCreditCards().get(0);
        ThreeDSecureInfo threeDSecureInfo = creditCard.getVerification().getThreeDSecureInfo();

        assertEquals("authenticate_successful", threeDSecureInfo.getStatus());
        assertEquals(true, threeDSecureInfo.isLiabilityShifted());
        assertEquals(true, threeDSecureInfo.isLiabilityShiftPossible());
        assertNotNull(threeDSecureInfo.getEnrolled());
        assertNotNull(threeDSecureInfo.getCAVV());
        assertNotNull(threeDSecureInfo.getECIFlag());
        assertNotNull(threeDSecureInfo.getXID());
        assertNotNull(threeDSecureInfo.getThreeDSecureVersion());
    }

    @Test
    public void createWithThreeDSecurePassThru() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        CustomerRequest request = new CustomerRequest().
            firstName("Fred").
            creditCard().
                paymentMethodNonce(Nonce.Transactable).
                threeDSecurePassThruRequest().
                    eciFlag("05").
                    cavv("some-cavv").
                    xid("some-xid").
                    threeDSecureVersion("2.2.0").
                    dsTransactionId("some-ds-transaction-id").
                    authenticationResponse("some-auth-response").
                    directoryResponse("some-directory-response").
                    cavvAlgorithm("algorithm").
                    done().
                options().
                    verifyCard(true).
                    done().
                done();

        Result<Customer> result = gateway.customer().create(request);

        assertTrue(result.isSuccess());
    }

    @Test
    public void createWithThreeDSecurePassThruWithoutEciFlag() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        CustomerRequest request = new CustomerRequest().
            firstName("Fred").
            creditCard().
            paymentMethodNonce(Nonce.Transactable).
              threeDSecurePassThruRequest().
                  cavv("some-cavv").
                  xid("some-xid").
                  threeDSecureVersion("2.2.0").
                  done().
              options().
                  verifyCard(true).
                  done().
              done();

        Result<Customer> result = gateway.customer().create(request);

        assertFalse(result.isSuccess());
        assertEquals(
                ValidationErrorCode.VERIFICATION_THREE_D_SECURE_PASS_THRU_ECI_FLAG_IS_REQUIRED,
                result.getErrors().getAllDeepValidationErrors().get(0).getCode());
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
    public void createWithOrderPaymentPayPalAccountNonceAndPayPalOptions() {
        String nonce = TestHelper.generateOrderPaymentPayPalNonce(gateway);
        CustomerRequest request = new CustomerRequest().
            paymentMethodNonce(nonce).
            options().
                paypal().
                    payeeEmail("payee@example.com").
                    orderId("merchant-order-id").
                    customField("custom merchant field").
                    description("merchant description").
                    amount(new BigDecimal("1.23")).
                    done().
                done();

        Result<Customer> result = gateway.customer().create(request);
        assertTrue(result.isSuccess());
        assertEquals(1, result.getTarget().getPayPalAccounts().size());
    }

    @Test
    public void createWithOneTimePayPalAccountNonceAndShipping() {
        String nonce = TestHelper.generateOneTimePayPalNonce(gateway);
        CustomerRequest request = new CustomerRequest().
            paymentMethodNonce(nonce).
            options().
                paypal().
                    shipping().
                        firstName("Andrew").
                        lastName("Mason").
                        company("Braintree Shipping").
                        streetAddress("456 W Main St").
                        extendedAddress("Apt 2F").
                        locality("Bartlett").
                        region("MA").
                        postalCode("60103").
                        countryName("Mexico").
                        countryCodeAlpha2("MX").
                        countryCodeAlpha3("MEX").
                        countryCodeNumeric("484").
                        done().
                    done().
                done();

        Result<Customer> result = gateway.customer().create(request);
        assertFalse(result.isSuccess());
        assertEquals(1, result.getErrors().getAllDeepValidationErrors().size());
    }

    @Test
    public void createWithUsBankAccountNonce() {
        String nonce = TestHelper.generateValidUsBankAccountNonce(gateway);
        CustomerRequest request = new CustomerRequest().
            paymentMethodNonce(nonce).
            creditCard().
                options().
                    verificationMerchantAccountId(MerchantAccountTestConstants.US_BANK_MERCHANT_ACCOUNT).
                    done().
                done();

        Result<Customer> result = gateway.customer().create(request);
        assertTrue(result.isSuccess());
        assertEquals(1, result.getTarget().getUsBankAccounts().size());

        UsBankAccount usBankAccount = result.getTarget().getUsBankAccounts().get(0);
        assertEquals("021000021", usBankAccount.getRoutingNumber());
        assertEquals("1234", usBankAccount.getLast4());
        assertEquals("checking", usBankAccount.getAccountType());
        assertEquals("Dan Schulman", usBankAccount.getAccountHolderName());
        assertTrue(Pattern.matches(".*CHASE.*", usBankAccount.getBankName()));
    }

    @Test
    public void find() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        Customer foundCustomer = gateway.customer().find(customer.getId());
        assertEquals(customer.getId(), foundCustomer.getId());
    }

    @Test
    public void findUsBankAccountFromCustomer() {
        String nonce = TestHelper.generateValidUsBankAccountNonce(gateway);
        CustomerRequest request = new CustomerRequest().
            paymentMethodNonce(nonce).
            creditCard().
                options().
                    verificationMerchantAccountId(MerchantAccountTestConstants.US_BANK_MERCHANT_ACCOUNT).
                    done().
                done();

        Result<Customer> result = gateway.customer().create(request);
        assertTrue(result.isSuccess());
        assertEquals(1, result.getTarget().getUsBankAccounts().size());

        Customer customer = result.getTarget();
        Customer foundCustomer = gateway.customer().find(customer.getId());
        assertEquals(customer.getId(), foundCustomer.getId());

        UsBankAccount usBankAccount = customer.getUsBankAccounts().get(0);
        assertEquals("021000021", usBankAccount.getRoutingNumber());
        assertEquals("1234", usBankAccount.getLast4());
        assertEquals("checking", usBankAccount.getAccountType());
        assertEquals("Dan Schulman", usBankAccount.getAccountHolderName());
        assertTrue(Pattern.matches(".*CHASE.*", usBankAccount.getBankName()));
    }

    @Test
    public void findCustomerWithAllFilterableAssociationsFilteredOut() {
        CustomerRequest request = new CustomerRequest().
            customField("store_me", "custom value").
            creditCard().
                cardholderName("Fred Jones").
                number("5105105105105100").
                cvv("123").
                expirationDate("05/12").
                billingAddress().
                    firstName("Fred").
                    lastName("Jones").
                    streetAddress("1 E Main St").
                    locality("Chicago").
                    region("Illinois").
                    postalCode("60622").
                    countryName("United States of America").
                    done().
                done().
            lastName("Jones");

        Customer customer = gateway.customer().create(request).getTarget();
        CreditCard card = customer.getCreditCards().get(0);

        String id = "subscription-id-" + new Random().nextInt();
        SubscriptionRequest subscriptionRequest = new SubscriptionRequest().
            id(id).
            planId("integration_trialless_plan").
            paymentMethodToken(card.getToken()).
            price(new BigDecimal("1.00"));
        Subscription subscription = gateway.subscription().create(subscriptionRequest).getTarget();

        Customer foundCustomer = gateway.customer().find(customer.getId(), "customernoassociations");
        assertEquals(0, foundCustomer.getCreditCards().size());
        assertEquals(0, foundCustomer.getPaymentMethods().size());
        assertEquals(0, foundCustomer.getAddresses().size());
        assertEquals(0, foundCustomer.getCustomFields().size());
    }

    @Test
    public void findCustomerWithNestedFilterableAssociationsFilteredOut() {
        CustomerRequest request = new CustomerRequest().
            customField("store_me", "custom value").
            creditCard().
                cardholderName("Fred Jones").
                number("5105105105105100").
                cvv("123").
                expirationDate("05/12").
                billingAddress().
                    firstName("Fred").
                    lastName("Jones").
                    streetAddress("1 E Main St").
                    locality("Chicago").
                    region("Illinois").
                    postalCode("60622").
                    countryName("United States of America").
                    done().
                done().
            lastName("Jones");

        Customer customer = gateway.customer().create(request).getTarget();
        CreditCard card = customer.getCreditCards().get(0);

        String id = "subscription-id-" + new Random().nextInt();
        SubscriptionRequest subscriptionRequest = new SubscriptionRequest().
            id(id).
            planId("integration_trialless_plan").
            paymentMethodToken(card.getToken()).
            price(new BigDecimal("1.00"));
        Subscription subscription = gateway.subscription().create(subscriptionRequest).getTarget();

        Customer foundCustomer = gateway.customer().find(customer.getId(), "customertoplevelassociations");
        assertEquals(1, foundCustomer.getCreditCards().size());
        assertEquals(0, foundCustomer.getCreditCards().get(0).getSubscriptions().size());
        assertEquals(1, foundCustomer.getPaymentMethods().size());
        assertEquals(0, foundCustomer.getPaymentMethods().get(0).getSubscriptions().size());
        assertEquals(1, foundCustomer.getAddresses().size());
        assertEquals(1, foundCustomer.getCustomFields().size());
    }

    @Test
    public void findWithEmptyAssociatedFilterId() {
        CustomerRequest request = new CustomerRequest().
            customField("store_me", "custom value").
            firstName("Jonas").
            lastName("Jones");

        Customer customer = gateway.customer().create(request).getTarget();

        try {
            gateway.customer().find(customer.getId(), "");
            fail("Should throw NotFoundException");
        } catch (NotFoundException e) {
        }

        try {
            gateway.customer().find(customer.getId(), null);
            fail("Should throw NotFoundException");
        } catch (NotFoundException e) {
        }
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
    public void findWithThreeDSecure() {
        CustomerRequest request = new CustomerRequest().
            firstName("Fred").
            creditCard().
              paymentMethodNonce(Nonce.ThreeDSecureVisaFullAuthentication).
              options().
                  verifyCard(true).
                  done().
              done();

        Result<Customer> createResult = gateway.customer().create(request);
        assertTrue(createResult.isSuccess());
        Customer customer = createResult.getTarget();

        Customer foundCustomer = gateway.customer().find(customer.getId());
        ThreeDSecureInfo threeDSecureInfo =
            foundCustomer.getCreditCards().get(0).getVerification().getThreeDSecureInfo();
        assertEquals("authenticate_successful", threeDSecureInfo.getStatus());
        assertEquals(true, threeDSecureInfo.isLiabilityShifted());
        assertEquals(true, threeDSecureInfo.isLiabilityShiftPossible());
        assertNotNull(threeDSecureInfo.getCAVV());
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
        assertNotNull(card.getBin());
        assertNotNull(card.getExpirationMonth());
        assertNotNull(card.getCardholderName());
        assertEquals(1, foundCustomer.getPaymentMethods().size());
    }

    @Test
    public void createWithApplePayCardRequest() {
        CustomerRequest request = new CustomerRequest().
            firstName("Jane").
            lastName("Doe").
            applePayCard().
                billingAddress().
                    postalCode("98126").
                    done().
                cryptogram("01010101010101010101").
                cardholderName("Jane Doe").
                eciIndicator("5").
                expirationMonth("10").
                expirationYear("2024").
                number("4111111111111111").
                options().
                    makeDefault(true).
                    done().
                token(String.valueOf(new Random().nextInt())).
                done();

        Result<Customer> result = gateway.customer().create(request);
        assertTrue(result.isSuccess());

        Customer customer = result.getTarget();
        assertEquals("Jane", customer.getFirstName());
        assertEquals("Doe", customer.getLastName());
        assertNotEquals(0, customer.getApplePayCards());

        ApplePayCard applePayCard = customer.getApplePayCards().get(0);
        assertEquals("98126", applePayCard.getBillingAddress().getPostalCode());
        assertEquals("411111", applePayCard.getBin());
        assertEquals("1111", applePayCard.getLast4());
        assertEquals("10", applePayCard.getExpirationMonth());
        assertEquals("2024", applePayCard.getExpirationYear());
        assertTrue(applePayCard.isDefault());
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
    public void createWithAndroidPayCardRequest() {
        CustomerRequest request = new CustomerRequest().
            firstName("John").
            lastName("Doe").
            androidPayCard().
                billingAddress().
                    postalCode("98126").
                    done().
                cryptogram("01010101010101010101").
                cardholderName("John Doe").
                expirationMonth("05").
                expirationYear("2024").
                googleTransactionId("dontbeevil").
                number("4111111111111111").
                token(String.valueOf(new Random().nextInt())).
                options().
                    makeDefault(true).
                    done().
                done();

        Result<Customer> result = gateway.customer().create(request);
        assertTrue(result.isSuccess());

        Customer customer = result.getTarget();
        assertEquals("John", customer.getFirstName());
        assertEquals("Doe", customer.getLastName());
        assertNotEquals(0, customer.getAndroidPayCards());

        AndroidPayCard androidPayCard = customer.getAndroidPayCards().get(0);
        assertEquals("98126", androidPayCard.getBillingAddress().getPostalCode());
        assertEquals("411111", androidPayCard.getBin());
        assertEquals("1111", androidPayCard.getLast4());
        assertEquals("05", androidPayCard.getExpirationMonth());
        assertEquals("2024", androidPayCard.getExpirationYear());
        assertEquals("dontbeevil", androidPayCard.getGoogleTransactionId());
        assertNotNull(androidPayCard.getToken());
        assertTrue(androidPayCard.isDefault());
    }

    @Test
    public void createWithAndroidPayNetworkTokenRequest() {
        CustomerRequest request = new CustomerRequest().
            firstName("John").
            lastName("Doe").
            androidPayNetworkToken().
                billingAddress().
                    postalCode("98126").
                    done().
                cryptogram("01010101010101010101").
                cardholderName("John Doe").
                eciIndicator("eci-indicator").
                expirationMonth("05").
                expirationYear("2024").
                googleTransactionId("dontbeevil").
                number("4111111111111111").
                token(String.valueOf(new Random().nextInt())).
                options().
                    makeDefault(true).
                    done().
                done();

        Result<Customer> result = gateway.customer().create(request);
        assertTrue(result.isSuccess());

        Customer customer = result.getTarget();
        assertEquals("John", customer.getFirstName());
        assertEquals("Doe", customer.getLastName());
        assertNotEquals(0, customer.getAndroidPayCards());

        AndroidPayCard androidPayCard = customer.getAndroidPayCards().get(0);
        assertEquals("98126", androidPayCard.getBillingAddress().getPostalCode());
        assertEquals("411111", androidPayCard.getBin());
        assertEquals("1111", androidPayCard.getLast4());
        assertEquals("05", androidPayCard.getExpirationMonth());
        assertEquals("2024", androidPayCard.getExpirationYear());
        assertEquals("dontbeevil", androidPayCard.getGoogleTransactionId());
        assertNotNull(androidPayCard.getToken());
        assertTrue(androidPayCard.isDefault());
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
    public void createWithAmexExpressCheckoutCard() {
        CustomerRequest request = new CustomerRequest().
            paymentMethodNonce(Nonce.AmexExpressCheckout);
        Customer customer = gateway.customer().create(request).getTarget();

        Customer foundCustomer = gateway.customer().find(customer.getId());
        assertEquals(customer.getId(), foundCustomer.getId());
        assertNotNull(foundCustomer.getAmexExpressCheckoutCards());
        assertEquals(1, foundCustomer.getAmexExpressCheckoutCards().size());
        AmexExpressCheckoutCard card = foundCustomer.getAmexExpressCheckoutCards().get(0);
        assertNotNull(card);
        assertNotNull(card.getCardMemberNumber());
        assertEquals(1, foundCustomer.getPaymentMethods().size());
    }

    @Test
    public void createWithVenmoAccount() {
        CustomerRequest request = new CustomerRequest()
            .paymentMethodNonce(Nonce.VenmoAccount);
        Customer customer = gateway.customer().create(request).getTarget();

        Customer foundCustomer = gateway.customer().find(customer.getId());
        assertEquals(customer.getId(), foundCustomer.getId());
        assertNotNull(foundCustomer.getVenmoAccounts());
        assertEquals(1, foundCustomer.getVenmoAccounts().size());
        assertEquals(1, foundCustomer.getPaymentMethods().size());

        VenmoAccount account = foundCustomer.getVenmoAccounts().get(0);
        assertNotNull(account);
        assertEquals(account.getUsername(), "venmojoe");
        assertEquals(account.getVenmoUserId(), "1234567891234567891");
    }

    @Test
    public void createWithAccountTypeCredit() {
        CustomerRequest request = new CustomerRequest();
        request.firstName("Fred").
            creditCard().
                cardholderName("Fred Jones").
                number(CreditCardNumber.HIPER.number).
                cvv("123").
                expirationDate("05/12").
                options().
                    verifyCard(true).
                    verificationMerchantAccountId("hiper_brl").
                    verificationAccountType("credit").
                    done().
                done().
            lastName("Jones");

        Result<Customer> result = gateway.customer().create(request);
        assertTrue(result.isSuccess());
        Customer customer = result.getTarget();

        CreditCard creditCard = customer.getCreditCards().get(0);
        assertEquals("credit", creditCard.getVerification().getCreditCard().getAccountType());
    }

    @Test
    public void createWithAccountTypeDebit() {
        CustomerRequest request = new CustomerRequest();
        request.firstName("Fred").
            creditCard().
                cardholderName("Fred Jones").
                number(CreditCardNumber.HIPER.number).
                cvv("123").
                expirationDate("05/12").
                options().
                    verifyCard(true).
                    verificationMerchantAccountId("card_processor_brl").
                    verificationAccountType("debit").
                    done().
                done().
            lastName("Jones");

        Result<Customer> result = gateway.customer().create(request);
        assertTrue(result.isSuccess());
        Customer customer = result.getTarget();

        CreditCard creditCard = customer.getCreditCards().get(0);
        assertEquals("debit", creditCard.getVerification().getCreditCard().getAccountType());
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
            internationalPhone().
                countryCode("1").
                nationalNumber("3121234567").
                done().
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
        assertEquals("1", updatedCustomer.getInternationalPhone().getCountryCode());
        assertEquals("3121234567", updatedCustomer.getInternationalPhone().getNationalNumber());
        assertEquals("http://getbraintree.com", updatedCustomer.getWebsite());
    }

    @Test
    public void updateDefaultPaymentMethodInOptions() {
        CustomerRequest request = new CustomerRequest().
            firstName("Mark").
            lastName("Jones");

        Customer customer = gateway.customer().create(request).getTarget();

        String token1 = "TOKEN-" + new Random().nextInt();

        PaymentMethodRequest request1 = new PaymentMethodRequest().
            paymentMethodNonce(Nonce.TransactableVisa).
            token(token1).
            customerId(customer.getId());

        Result<? extends PaymentMethod> result1 = gateway.paymentMethod().create(request1);
        assertTrue(result1.getTarget().isDefault());

        String token2 = "TOKEN-" + new Random().nextInt();

        PaymentMethodRequest request2 = new PaymentMethodRequest().
            paymentMethodNonce(Nonce.TransactableMasterCard).
            token(token2).
            customerId(customer.getId());

        gateway.paymentMethod().create(request2);
        PaymentMethod newPaymentMethod = gateway.paymentMethod().find(token2);
        assertFalse(newPaymentMethod.isDefault());

        CustomerRequest updateRequest = new CustomerRequest().
            creditCard().
                options().
                    updateExistingToken(token2).
                    makeDefault(true).
                    done().
                done();

        gateway.customer().update(customer.getId(), updateRequest).getTarget();

        newPaymentMethod = gateway.paymentMethod().find(token2);
        assertTrue(newPaymentMethod.isDefault());
    }

    @Test
    public void updateDefaultPaymentMethod() {
        CustomerRequest request = new CustomerRequest().
            firstName("Mark").
            lastName("Jones");

        Customer customer = gateway.customer().create(request).getTarget();

        String token1 = "TOKEN-" + new Random().nextInt();

        PaymentMethodRequest request1 = new PaymentMethodRequest().
            paymentMethodNonce(Nonce.TransactableVisa).
            token(token1).
            customerId(customer.getId());

        Result<? extends PaymentMethod> result1 = gateway.paymentMethod().create(request1);
        assertTrue(result1.getTarget().isDefault());

        String token2 = "TOKEN-" + new Random().nextInt();

        PaymentMethodRequest request2 = new PaymentMethodRequest().
            paymentMethodNonce(Nonce.TransactableMasterCard).
            token(token2).
            customerId(customer.getId());

        gateway.paymentMethod().create(request2);
        PaymentMethod newPaymentMethod = gateway.paymentMethod().find(token2);
        assertFalse(newPaymentMethod.isDefault());

        CustomerRequest updateRequest = new CustomerRequest().
            defaultPaymentMethodToken(token2);

        gateway.customer().update(customer.getId(), updateRequest).getTarget();

        newPaymentMethod = gateway.paymentMethod().find(token2);
        assertTrue(newPaymentMethod.isDefault());
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
    public void updateWithThreeDSecurePassThru() {
        CustomerRequest createRequest = new CustomerRequest().
            firstName("Fred").
            creditCard().
                paymentMethodNonce(Nonce.Transactable).
                done();

        Customer customer = gateway.customer().create(createRequest).getTarget();
        CustomerRequest updateRequest = new CustomerRequest().
            firstName("Jane").
            lastName("Doe").
            creditCard().
                paymentMethodNonce(Nonce.Transactable).
                threeDSecurePassThruRequest().
                    eciFlag("05").
                    cavv("some-cavv").
                    xid("some-xid").
                    threeDSecureVersion("2.2.0").
                    dsTransactionId("some-ds-transaction-id").
                    authenticationResponse("some-auth-response").
                    directoryResponse("some-directory-response").
                    cavvAlgorithm("algorithm").
                    done().
                options().
                    verifyCard(true).
                    done().
                done();

        Customer updatedCustomer = gateway.customer().update(customer.getId(), updateRequest).getTarget();
        assertEquals("Jane", updatedCustomer.getFirstName());
        assertEquals("Doe", updatedCustomer.getLastName());
    }

    @Test
    public void updateWithThreeDSecurePassThruWithoutEciFlag() {
        CustomerRequest createRequest = new CustomerRequest().
            firstName("Fred").
            creditCard().
                paymentMethodNonce(Nonce.Transactable).
                done();

        Customer customer = gateway.customer().create(createRequest).getTarget();
        CustomerRequest updateRequest = new CustomerRequest().
            firstName("Jane").
            lastName("Doe").
            creditCard().
                paymentMethodNonce(Nonce.Transactable).
                threeDSecurePassThruRequest().
                    cavv("some-cavv").
                    xid("some-xid").
                    threeDSecureVersion("2.2.0").
                    dsTransactionId("some-ds-transaction-id").
                    authenticationResponse("some-auth-response").
                    directoryResponse("some-directory-response").
                    cavvAlgorithm("algorithm").
                    done().
                options().
                    verifyCard(true).
                    done().
                done();

        Result<Customer> result = gateway.customer().update(customer.getId(), updateRequest);
        assertFalse(result.isSuccess());
        assertEquals(
                ValidationErrorCode.VERIFICATION_THREE_D_SECURE_PASS_THRU_ECI_FLAG_IS_REQUIRED,
                result.getErrors().getAllDeepValidationErrors().get(0).getCode());
    }

    @Test
    public void updateIncludesRiskDataWhenSkipAdvancedFraudCheckingIsFalse() {
        createFraudProtectionEnterpriseMerchantGateway();
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();

        CustomerRequest updateRequest = new CustomerRequest().
            creditCard().
              cardholderName("John Doe").
              cvv("123").
              number("4111111111111111").
              expirationDate("05/12").
              options().
                verifyCard(true).
                skipAdvancedFraudChecking(false).
                done().
              done();

        Result<Customer> result = gateway.customer().update(customer.getId(), updateRequest);
        assertTrue(result.isSuccess());

        Customer updatedCustomer = result.getTarget();
        assertNotNull(updatedCustomer);

        CreditCardVerification verification = updatedCustomer.getCreditCards().get(0).getVerification();
        assertNotNull(verification);

        RiskData riskData = verification.getRiskData();
        assertNotNull(riskData);
    }

    @Test
    public void updateDoesNotIncludeRiskDataWhenSkipAdvancedFraudCheckingIsTrue() {
        createFraudProtectionEnterpriseMerchantGateway();
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();

        CustomerRequest updateRequest = new CustomerRequest().
            creditCard().
              cardholderName("John Doe").
              cvv("123").
              number("4111111111111111").
              expirationDate("05/12").
              options().
                verifyCard(true).
                skipAdvancedFraudChecking(true).
                done().
              done();

        Result<Customer> result = gateway.customer().update(customer.getId(), updateRequest);
        assertTrue(result.isSuccess());

        Customer updatedCustomer = result.getTarget();
        assertNotNull(updatedCustomer);

        CreditCardVerification verification = updatedCustomer.getCreditCards().get(0).getVerification();
        assertNotNull(verification);

        RiskData riskData = verification.getRiskData();
        assertNull(riskData);
    }

    @Test
    public void updateWithExistingCreditCardFailsOnDuplicatePaymentMethod() {
        CustomerRequest request = new CustomerRequest().
            firstName("Mark").
            lastName("Jones").
            creditCard().
                number("4111111111111111").
                expirationDate("12/12").
                done();

        Customer customer = gateway.customer().create(request).getTarget();
        CreditCard creditCard = customer.getCreditCards().get(0);

        CustomerRequest updateRequest = new CustomerRequest().
            firstName("Jane").
            lastName("Doe").
            creditCard().
                number("4111111111111111").
                expirationDate("12/12").
                options().
                    updateExistingToken(creditCard.getToken()).
                    failOnDuplicatePaymentMethod(true).
                    done().
                done();

        Result<Customer> result = gateway.customer().update(customer.getId(), updateRequest);
        assertFalse(result.isSuccess());
        assertEquals(
                ValidationErrorCode.CREDIT_CARD_DUPLICATE_CARD_EXISTS,
                result.getErrors().forObject("customer").forObject("creditCard").onField("number").get(0).getCode()
        );
    }

    @Test
    public void updateWithExistingCreditCardFailsOnDuplicatePaymentMethodForCustomer() {
        CustomerRequest request = new CustomerRequest().
            firstName("Mark").
            lastName("Jones").
            creditCard().
                number("4111111111111111").
                expirationDate("12/12").
                done();

        Customer customer = gateway.customer().create(request).getTarget();
        CreditCard creditCard = customer.getCreditCards().get(0);

        CustomerRequest updateRequest = new CustomerRequest().
            firstName("Jane").
            lastName("Doe").
            creditCard().
                number("4111111111111111").
                expirationDate("12/12").
                options().
                    updateExistingToken(creditCard.getToken()).
                    failOnDuplicatePaymentMethodForCustomer(true).
                    done().
                done();

        Result<Customer> result = gateway.customer().update(customer.getId(), updateRequest);
        assertFalse(result.isSuccess());
        assertEquals(
                ValidationErrorCode.CREDIT_CARD_DUPLICATE_CARD_EXISTS_FOR_CUSTOMER,
                result.getErrors().forObject("customer").forObject("creditCard").onField("number").get(0).getCode()
        );
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
    public void updateWithNewCreditCardAndVerificationAmount() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();

        CustomerRequest request = new CustomerRequest().
            creditCard().
                number("4111111111111111").
                expirationDate("12/12").
                options().
                    verifyCard(true).
                    verificationAmount("6.00").
                    done().
                done();

        Result<Customer> result = gateway.customer().create(request);
        assertTrue(result.isSuccess());
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
    public void updateWithOrderPaymentPayPalAccountNonceAndPayPalOptions() {
        CustomerRequest request = new CustomerRequest().
            creditCard().
                number("4111111111111111").
                expirationDate("12/12").
                done();

        Customer customer = gateway.customer().create(request).getTarget();
        assertEquals(customer.getDefaultPaymentMethod().getToken(), customer.getCreditCards().get(0).getToken());

        String nonce = TestHelper.generateOrderPaymentPayPalNonce(gateway);
        CustomerRequest updateRequest = new CustomerRequest().
            paymentMethodNonce(nonce).
            options().
                paypal().
                    payeeEmail("payee@example.com").
                    orderId("merchant-order-id").
                    customField("custom merchant field").
                    description("merchant description").
                    amount(new BigDecimal("1.23")).
                    done().
                done();

        Result<Customer> updateResult = gateway.customer().update(customer.getId(), updateRequest);
        assertTrue(updateResult.isSuccess());
    }

    @Test
    public void createWithVerificationCurrencyIsoCodeSpecified() {
        CustomerRequest request = new CustomerRequest();
        request.firstName("Fred").
                lastName("Jones").
                creditCard().
                cardholderName("Fred Jones").
                number("4111111111111111").
                cvv("123").
                expirationDate("05/22").
                options().
                    verifyCard(true).
                    verificationCurrencyIsoCode("USD").
                    done().
                done();

        Result<Customer> result = gateway.customer().create(request);
        assertTrue(result.isSuccess());
        CreditCard creditCard = result.getTarget().getCreditCards().get(0);
        assertEquals("USD", creditCard.getVerification().getCurrencyIsoCode());
    }

    @Test
    public void createWithInvalidVerificationCurrencyIsoCodeSpecified() {
        CustomerRequest request = new CustomerRequest();
        request.firstName("Fred").
                lastName("Jones").
                creditCard().
                cardholderName("Fred Jones").
                number("4111111111111111").
                cvv("123").
                expirationDate("05/22").
                options().
                    verifyCard(true).
                //test account is setup to process USD, supply any currency than USD
                    verificationCurrencyIsoCode("GBP").
                    done().
                done();

        Result<Customer> result = gateway.customer().create(request);
        assertFalse(result.isSuccess());
        assertEquals(ValidationErrorCode.CREDIT_CARD_OPTIONS_VERIFICATION_INVALID_PRESENTMENT_CURRENCY.code,
                result.getErrors().getAllDeepValidationErrors().get(0).getCode().code);
    }

    @Test
    public void createWithNonceAndVerificationCurrencyIsoCodeSpecified() {
        String nonce = TestHelper.generateUnlockedNonce(gateway);
        CustomerRequest request = new CustomerRequest().
                firstName("Fred").
                creditCard().
                paymentMethodNonce(nonce).
                options().
                    verificationCurrencyIsoCode("USD").
                    verifyCard(true).
                    done().
                done();

        Result<Customer> result = gateway.customer().create(request);
        assertTrue(result.isSuccess());
        assertEquals(1, result.getTarget().getCreditCards().size());
        CreditCard creditCard = result.getTarget().getCreditCards().get(0);
        assertEquals("USD", creditCard.getVerification().getCurrencyIsoCode());
    }

    @Test
    public void createWithNonceAndInvalidVerificationCurrencyIsoCodeSpecified() {
        String nonce = TestHelper.generateUnlockedNonce(gateway);
        CustomerRequest request = new CustomerRequest().
                firstName("Fred").
                creditCard().
                paymentMethodNonce(nonce).
                options().
                //test account is setup to process USD, supply any currency than USD
                    verificationCurrencyIsoCode("GBP").
                    verifyCard(true).
                    done().
                done();

        Result<Customer> result = gateway.customer().create(request);
        assertFalse(result.isSuccess());
        assertEquals(ValidationErrorCode.CREDIT_CARD_OPTIONS_VERIFICATION_INVALID_PRESENTMENT_CURRENCY.code,
                result.getErrors().getAllDeepValidationErrors().get(0).getCode().code);
    }

    @Test
    public void createWithTaxIdentifiers() {
        CustomerRequest request = new CustomerRequest().
            taxIdentifier().
                countryCode("US").
                identifier("987654321").
                done().
            taxIdentifier().
                countryCode("CL").
                identifier("123456789").
                done();

        Result<Customer> result = gateway.customer().create(request);
        assertTrue(result.isSuccess());
    }

    @Test
    public void updateWtihTaxIdentifiers() {
        CustomerRequest request = new CustomerRequest().
            firstName("Mark").
            lastName("Jones");

        CustomerRequest updateRequest = new CustomerRequest().
            taxIdentifier().
                countryCode("US").
                identifier("987654321").
                done().
            taxIdentifier().
                countryCode("CL").
                identifier("123456789").
                done();

        Result<Customer> result = gateway.customer().create(request);
        assertTrue(result.isSuccess());

        Customer customer = result.getTarget();

        Result<Customer> updateResult = gateway.customer().update(customer.getId(), updateRequest);
        assertTrue(updateResult.isSuccess());
    }
  
    @Test
    public void updateWithAndroidPayCardRequest() {
        CustomerRequest createRequest = new CustomerRequest().
            firstName("Billy").
            lastName("Bob");

        CustomerRequest updateRequest = new CustomerRequest().
            androidPayCard().
            billingAddress().
                postalCode("98126").
                done().
            cryptogram("01010101010101010101").
            cardholderName("Billy Bob").
            expirationMonth("10").
            expirationYear("2024").
            googleTransactionId("dontbeevil").
            number("4111111111111111").
            options().
                makeDefault(true).
                done().
            token(String.valueOf(new Random().nextInt())).
            done();

        Result<Customer> result = gateway.customer().create(createRequest);
        assertTrue(result.isSuccess());

        Customer customer = result.getTarget();

        Result<Customer> updateResult = gateway.customer().update(customer.getId(), updateRequest);
        assertTrue(updateResult.isSuccess());

        Customer updatedCustomer = updateResult.getTarget();
        assertEquals("Billy", updatedCustomer.getFirstName());
        assertEquals("Bob", updatedCustomer.getLastName());
        assertNotEquals(0, updatedCustomer.getAndroidPayCards());

        AndroidPayCard androidPayCard = updatedCustomer.getAndroidPayCards().get(0);
        assertEquals("98126", androidPayCard.getBillingAddress().getPostalCode());
        assertEquals("411111", androidPayCard.getBin());
        assertEquals("1111", androidPayCard.getLast4());
        assertEquals("10", androidPayCard.getExpirationMonth());
        assertEquals("2024", androidPayCard.getExpirationYear());
        assertEquals("dontbeevil", androidPayCard.getGoogleTransactionId());
        assertNotNull(androidPayCard.getToken());
        assertTrue(androidPayCard.isDefault());
    }

    @Test
    public void updateWithAndroidPayNetworkTokenRequest() {
        CustomerRequest createRequest = new CustomerRequest().
            firstName("Billy").
            lastName("Bob");

        CustomerRequest updateRequest = new CustomerRequest().
            androidPayNetworkToken().
            billingAddress().
                postalCode("98126").
                done().
            cryptogram("01010101010101010101").
            cardholderName("Billy Bob").
            eciIndicator("eci-indicator").
            expirationMonth("10").
            expirationYear("2024").
            googleTransactionId("dontbeevil").
            number("4111111111111111").
            options().
                makeDefault(true).
                done().
            token(String.valueOf(new Random().nextInt())).
            done();

        Result<Customer> result = gateway.customer().create(createRequest);
        assertTrue(result.isSuccess());

        Customer customer = result.getTarget();

        Result<Customer> updateResult = gateway.customer().update(customer.getId(), updateRequest);
        assertTrue(updateResult.isSuccess());

        Customer updatedCustomer = updateResult.getTarget();
        assertEquals("Billy", updatedCustomer.getFirstName());
        assertEquals("Bob", updatedCustomer.getLastName());
        assertNotEquals(0, updatedCustomer.getAndroidPayCards());

        AndroidPayCard androidPayCard = updatedCustomer.getAndroidPayCards().get(0);
        assertEquals("98126", androidPayCard.getBillingAddress().getPostalCode());
        assertEquals("411111", androidPayCard.getBin());
        assertEquals("1111", androidPayCard.getLast4());
        assertEquals("10", androidPayCard.getExpirationMonth());
        assertEquals("2024", androidPayCard.getExpirationYear());
        assertEquals("dontbeevil", androidPayCard.getGoogleTransactionId());
        assertNotNull(androidPayCard.getToken());
        assertTrue(androidPayCard.isDefault());
    }
  
    @Test
    public void updateWithApplePayCardRequest() {
        CustomerRequest createRequest = new CustomerRequest().
            firstName("Billy").
            lastName("Bob");

        CustomerRequest updateRequest = new CustomerRequest().
            applePayCard().
            billingAddress().
                postalCode("98126").
                done().
            cryptogram("01010101010101010101").
            cardholderName("Billy Bob").
            eciIndicator("5").
            expirationMonth("10").
            expirationYear("2024").
            number("4111111111111111").
            options().
                makeDefault(true).
                done().
            token(String.valueOf(new Random().nextInt())).
            done();

        Result<Customer> result = gateway.customer().create(createRequest);
        assertTrue(result.isSuccess());

        Customer customer = result.getTarget();

        Result<Customer> updateResult = gateway.customer().update(customer.getId(), updateRequest);
        assertTrue(updateResult.isSuccess());

        Customer updatedCustomer = updateResult.getTarget();
        assertEquals("Billy", updatedCustomer.getFirstName());
        assertEquals("Bob", updatedCustomer.getLastName());
        assertNotEquals(0, updatedCustomer.getApplePayCards());

        ApplePayCard applePayCard = updatedCustomer.getApplePayCards().get(0);
        assertEquals("98126", applePayCard.getBillingAddress().getPostalCode());
        assertEquals("411111", applePayCard.getBin());
        assertEquals("1111", applePayCard.getLast4());
        assertEquals("10", applePayCard.getExpirationMonth());
        assertEquals("2024", applePayCard.getExpirationYear());
        assertTrue(applePayCard.isDefault());
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
