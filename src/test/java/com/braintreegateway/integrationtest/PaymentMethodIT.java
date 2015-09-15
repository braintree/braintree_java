package com.braintreegateway.integrationtest;

import java.util.Date;
import java.util.Random;
import com.braintreegateway.*;
import com.braintreegateway.testhelpers.TestHelper;
import com.braintreegateway.exceptions.NotFoundException;
import com.braintreegateway.test.Nonce;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PaymentMethodIT {

    private BraintreeGateway gateway;

    @Before
    public void createGateway() {
        this.gateway = new BraintreeGateway(Environment.DEVELOPMENT, "integration_merchant_id", "integration_public_key", "integration_private_key");
    }

    @Test
    public void createPayPalAccountWithNonce() {
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        assertTrue(customerResult.isSuccess());
        Customer customer = customerResult.getTarget();

        String nonce = TestHelper.generateFuturePaymentPayPalNonce(gateway);
        PaymentMethodRequest request = new PaymentMethodRequest().
            customerId(customer.getId()).
            paymentMethodNonce(nonce);

        Result<? extends PaymentMethod> result = gateway.paymentMethod().create(request);

        assertTrue(result.isSuccess());
        PaymentMethod paymentMethod = result.getTarget();
        assertNotNull(paymentMethod.getToken());
        assertNotNull(paymentMethod.getImageUrl());

        PayPalAccount paypalAccount = (PayPalAccount) paymentMethod;
        assertNotNull(paypalAccount.getEmail());
        assertNotNull(paypalAccount.getImageUrl());
    }

    @Test
    public void createApplePayCardFromNonce() {
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        assertTrue(customerResult.isSuccess());
        Customer customer = customerResult.getTarget();

        String nonce = Nonce.ApplePayAmex;
        PaymentMethodRequest request = new PaymentMethodRequest().
            customerId(customer.getId()).
            paymentMethodNonce(nonce);

        Result<? extends PaymentMethod> result = gateway.paymentMethod().create(request);

        assertTrue(result.isSuccess());
        PaymentMethod paymentMethod = result.getTarget();
        assertNotNull(paymentMethod.getToken());
        assertNotNull(paymentMethod.getImageUrl());

        ApplePayCard applePayCard = (ApplePayCard) paymentMethod;
        assertNotNull(applePayCard.getCardType());
        assertNotNull(applePayCard.getPaymentInstrumentName());
        assertNotNull(applePayCard.getCreatedAt());
        assertNotNull(applePayCard.getUpdatedAt());
        assertNotNull(applePayCard.getExpirationMonth());
        assertNotNull(applePayCard.getExpirationYear());
        assertNotNull(applePayCard.getExpired());
        assertNotNull(applePayCard.getSubscriptions());
        assertNotNull(applePayCard.getLast4());
        assertTrue(applePayCard.getSubscriptions().isEmpty());
    }

    @Test
    public void createApplePayCardFromNonceWithDefault() {
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        assertTrue(customerResult.isSuccess());
        Customer customer = customerResult.getTarget();

        String nonce = Nonce.ApplePayAmex;
        PaymentMethodRequest request = new PaymentMethodRequest().
            customerId(customer.getId()).
            paymentMethodNonce(nonce).
            options().
                makeDefault(true).
                done();

        Result<? extends PaymentMethod> result = gateway.paymentMethod().create(request);

        assertTrue(result.isSuccess());
        PaymentMethod paymentMethod = result.getTarget();
        assertNotNull(paymentMethod.getToken());
        assertNotNull(paymentMethod.getImageUrl());
        assertTrue(paymentMethod.isDefault());
    }

    @Test
    public void createAndroidPayProxyCardFromNonce() {
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        assertTrue(customerResult.isSuccess());
        Customer customer = customerResult.getTarget();

        String nonce = Nonce.AndroidPayDiscover;
        PaymentMethodRequest request = new PaymentMethodRequest().
            customerId(customer.getId()).
            paymentMethodNonce(nonce);

        Result<? extends PaymentMethod> result = gateway.paymentMethod().create(request);

        assertTrue(result.isSuccess());
        PaymentMethod paymentMethod = result.getTarget();
        assertNotNull(paymentMethod.getToken());
        assertNotNull(paymentMethod.getImageUrl());
    }

    @Test
    public void createAndroidPayNetworkTokenFromNonce() {
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        assertTrue(customerResult.isSuccess());
        Customer customer = customerResult.getTarget();

        String nonce = Nonce.AndroidPayMasterCard;
        PaymentMethodRequest request = new PaymentMethodRequest().
            customerId(customer.getId()).
            paymentMethodNonce(nonce);

        Result<? extends PaymentMethod> result = gateway.paymentMethod().create(request);

        assertTrue(result.isSuccess());
        PaymentMethod paymentMethod = result.getTarget();
        assertNotNull(paymentMethod.getToken());
        assertNotNull(paymentMethod.getImageUrl());
    }

    @Test
    public void createAndroidPayCardFromNonceWithDefault() {
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        assertTrue(customerResult.isSuccess());
        Customer customer = customerResult.getTarget();

        String nonce = Nonce.AndroidPay;
        PaymentMethodRequest request = new PaymentMethodRequest().
            customerId(customer.getId()).
            paymentMethodNonce(nonce).
            options().
                makeDefault(true).
                done();

        Result<? extends PaymentMethod> result = gateway.paymentMethod().create(request);

        assertTrue(result.isSuccess());
        PaymentMethod paymentMethod = result.getTarget();
        assertNotNull(paymentMethod.getToken());
        assertNotNull(paymentMethod.getImageUrl());
        assertTrue(paymentMethod.isDefault());

        AndroidPayCard androidPayCard = (AndroidPayCard) paymentMethod;
        assertNotNull(androidPayCard.getSourceCardType());
        assertNotNull(androidPayCard.getSourceCardLast4());
        assertNotNull(androidPayCard.getVirtualCardType());
        assertNotNull(androidPayCard.getVirtualCardLast4());
        assertNotNull(androidPayCard.getCardType());
        assertNotNull(androidPayCard.getLast4());
        assertNotNull(androidPayCard.getExpirationMonth());
        assertNotNull(androidPayCard.getExpirationYear());
        assertNotNull(androidPayCard.getToken());
        assertNotNull(androidPayCard.getGoogleTransactionId());
        assertNotNull(androidPayCard.getBin());
        assertTrue(androidPayCard.isDefault());
        assertNotNull(androidPayCard.getImageUrl());
        assertNotNull(androidPayCard.getCreatedAt());
        assertNotNull(androidPayCard.getUpdatedAt());
        assertNotNull(androidPayCard.getSubscriptions());
        assertTrue(androidPayCard.getSubscriptions().isEmpty());
    }

    @Test
    public void createAbstractPaymentMethod() {
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        assertTrue(customerResult.isSuccess());
        Customer customer = customerResult.getTarget();

        PaymentMethodRequest request = new PaymentMethodRequest().
            customerId(customer.getId()).
            paymentMethodNonce(Nonce.AbstractTransactable);

        Result<? extends PaymentMethod> result = gateway.paymentMethod().create(request);
        assertTrue(result.isSuccess());
        PaymentMethod paymentMethod = result.getTarget();
        assertNotNull(paymentMethod.getToken());
        assertNotNull(paymentMethod.getImageUrl());
    }

    @Test
    public void createPaymentMethodAndMakeDefault() {
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        assertTrue(customerResult.isSuccess());
        Customer customer = customerResult.getTarget();

        CreditCardRequest creditCardRequest = new CreditCardRequest().
            customerId(customer.getId()).
            cardholderName("John Doe").
            cvv("123").
            number("5105105105105100").
            expirationDate("05/12");
        Result<CreditCard> creditCardResult = gateway.creditCard().create(creditCardRequest);
        assertTrue(creditCardResult.isSuccess());

        String nonce = TestHelper.generateFuturePaymentPayPalNonce(gateway);
        PaymentMethodRequest request = new PaymentMethodRequest().
            customerId(customer.getId()).
            paymentMethodNonce(nonce).
            options().
              makeDefault(true).
              done();

        Result<? extends PaymentMethod> result = gateway.paymentMethod().create(request);

        assertTrue(result.isSuccess());
        PaymentMethod paymentMethod = result.getTarget();
        assertTrue(paymentMethod.isDefault());
    }

    @Test
    public void createPaymentMethodWithToken() {
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        assertTrue(customerResult.isSuccess());
        Customer customer = customerResult.getTarget();

        String nonce = TestHelper.generateFuturePaymentPayPalNonce(gateway);
        int r = (int)(Math.random() * 100000);
        String paymentMethodToken = "token-" + r;
        PaymentMethodRequest request = new PaymentMethodRequest().
            customerId(customer.getId()).
            paymentMethodNonce(nonce).
            token(paymentMethodToken);

        Result<? extends PaymentMethod> result = gateway.paymentMethod().create(request);

        assertTrue(result.isSuccess());
        PaymentMethod paymentMethod = result.getTarget();
        assertEquals(paymentMethodToken, paymentMethod.getToken());
    }

    @Test
    public void createPayPalAccountWithOneTimeNonceFails() {
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        assertTrue(customerResult.isSuccess());
        Customer customer = customerResult.getTarget();

        String nonce = TestHelper.generateOneTimePayPalNonce(gateway);
        PaymentMethodRequest request = new PaymentMethodRequest().
            customerId(customer.getId()).
            paymentMethodNonce(nonce);

        Result<? extends PaymentMethod> result = gateway.paymentMethod().create(request);

        assertFalse(result.isSuccess());
        assertEquals(
                ValidationErrorCode.PAYPAL_ACCOUNT_CANNOT_VAULT_ONE_TIME_USE_PAYPAL_ACCOUNT,
                result.getErrors().forObject("paypalAccount").onField("base").get(0).getCode()
                );
    }

    @Test
    public void createEuropeBankAccountWithNonce() {
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        assertTrue(customerResult.isSuccess());
        Customer customer = customerResult.getTarget();

        String nonce = TestHelper.generateEuropeBankAccountNonce(gateway, customer);
        PaymentMethodRequest request = new PaymentMethodRequest().
                customerId(customer.getId()).
                paymentMethodNonce(nonce);

        Result<? extends PaymentMethod> result = gateway.paymentMethod().create(request);
        assertTrue(result.isSuccess());
        PaymentMethod paymentMethod = result.getTarget();
        assertNotNull(paymentMethod.getImageUrl());
    }

    @Test
    public void createCreditCardWithNonce() {
        String nonce = TestHelper.generateUnlockedNonce(gateway, null, SandboxValues.CreditCardNumber.VISA.number);
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        assertTrue(customerResult.isSuccess());
        Customer customer = customerResult.getTarget();

        PaymentMethodRequest request = new PaymentMethodRequest().
            customerId(customer.getId()).
            paymentMethodNonce(nonce);

        Result<? extends PaymentMethod> result = gateway.paymentMethod().create(request);

        assertTrue(result.isSuccess());
        PaymentMethod paymentMethod = result.getTarget();
        assertNotNull(paymentMethod.getToken());

        CreditCard creditCard = (CreditCard) paymentMethod;
        assertEquals("1111", creditCard.getLast4());
    }

    @Test
    public void allowsPassingBillingAddressIdOutsideOfTheNonce() {
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        assertTrue(customerResult.isSuccess());
        Customer customer = customerResult.getTarget();

         CreditCardRequest request = new CreditCardRequest().
            number("4111111111111111").
            expirationMonth("12").
            expirationYear("2020");

        String nonce = TestHelper.generateNonceForCreditCard(gateway, request, customer.getId(), false);

        AddressRequest addressRequest = new AddressRequest().
            firstName("Bobby").
            lastName("Tables");
        Result<Address> addressResult = gateway.address().create(customer.getId(), addressRequest);

        PaymentMethodRequest paymentMethodRequest = new PaymentMethodRequest().
            paymentMethodNonce(nonce).
            customerId(customer.getId()).
            billingAddressId(addressResult.getTarget().getId());
        Result<? extends PaymentMethod> paymentMethodResult = gateway.paymentMethod().create(paymentMethodRequest);

        assertTrue(paymentMethodResult.isSuccess());

        PaymentMethod paymentMethod = paymentMethodResult.getTarget();
        assertTrue(paymentMethod instanceof CreditCard);
        String token = paymentMethod.getToken();

        CreditCard foundCreditCard = gateway.creditCard().find(token);
        assertTrue(foundCreditCard instanceof CreditCard);
        assertEquals("Bobby", foundCreditCard.getBillingAddress().getFirstName());
        assertEquals("Tables", foundCreditCard.getBillingAddress().getLastName());
    }

    @Test
    public void deletePayPalAccount() {
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        assertTrue(customerResult.isSuccess());
        Customer customer = customerResult.getTarget();

        String nonce = TestHelper.generateFuturePaymentPayPalNonce(gateway);
        PaymentMethodRequest request = new PaymentMethodRequest().
            customerId(customer.getId()).
            paymentMethodNonce(nonce);

        Result<? extends PaymentMethod> result = gateway.paymentMethod().create(request);

        assertTrue(result.isSuccess());
        PaymentMethod paymentMethod = result.getTarget();

        Result<? extends PaymentMethod> deleteResult = gateway.paymentMethod().delete(paymentMethod.getToken());
        assertTrue(deleteResult.isSuccess());
    }

    @Test
    public void deleteCreditCard() {
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        assertTrue(customerResult.isSuccess());
        Customer customer = customerResult.getTarget();

        String nonce = TestHelper.generateUnlockedNonce(gateway, null, SandboxValues.CreditCardNumber.VISA.number);
        PaymentMethodRequest request = new PaymentMethodRequest().
            customerId(customer.getId()).
            paymentMethodNonce(nonce);

        Result<? extends PaymentMethod> result = gateway.paymentMethod().create(request);

        assertTrue(result.isSuccess());
        PaymentMethod paymentMethod = result.getTarget();

        Result<? extends PaymentMethod> deleteResult = gateway.paymentMethod().delete(paymentMethod.getToken());
        assertTrue(deleteResult.isSuccess());
    }

    @Test
    public void deleteMissingRaisesNotFoundError() {
        try {
            gateway.paymentMethod().delete("missing");
            fail("Should throw NotFoundException");
        } catch (NotFoundException e) {
        }
    }

    @Test
    public void findAndroidPayCard() {
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        assertTrue(customerResult.isSuccess());
        Customer customer = customerResult.getTarget();

        String nonce = Nonce.AndroidPay;
        PaymentMethodRequest request = new PaymentMethodRequest().
            customerId(customer.getId()).
            paymentMethodNonce(nonce);

        Result<? extends PaymentMethod> result = gateway.paymentMethod().create(request);

        assertTrue(result.isSuccess());
        PaymentMethod paymentMethod = result.getTarget();

        PaymentMethod found = gateway.paymentMethod().find(paymentMethod.getToken());
        assertNotNull(found);
        assertTrue(found instanceof AndroidPayCard);
    }

    @Test
    public void findPayPalAccount() {
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        assertTrue(customerResult.isSuccess());
        Customer customer = customerResult.getTarget();

        String nonce = TestHelper.generateFuturePaymentPayPalNonce(gateway);
        PaymentMethodRequest request = new PaymentMethodRequest().
            customerId(customer.getId()).
            paymentMethodNonce(nonce);

        Result<? extends PaymentMethod> result = gateway.paymentMethod().create(request);

        assertTrue(result.isSuccess());
        PaymentMethod paymentMethod = result.getTarget();

        PaymentMethod found = gateway.paymentMethod().find(paymentMethod.getToken());
        assertNotNull(found);
        assertTrue(found instanceof PayPalAccount);
    }

    @Test
    public void paypalIgnoresPassedBillingAddressId() {
        String nonce = TestHelper.getNonceForPayPalAccount(gateway, "PAYPAL_CONSENT_CODE");
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        assertTrue(customerResult.isSuccess());
        Customer customer = customerResult.getTarget();

        AddressRequest addressRequest = new AddressRequest().
            firstName("Bobby").
            lastName("Tables");
        Result<Address> addressResult = gateway.address().create(customer.getId(), addressRequest);

        PaymentMethodRequest request = new PaymentMethodRequest().
            paymentMethodNonce(nonce).
            customerId(customer.getId()).
            billingAddressId(addressResult.getTarget().getId());

        Result<? extends PaymentMethod> result = gateway.paymentMethod().create(request);
        assertTrue(result.isSuccess());
        assertTrue(result.getTarget() instanceof PayPalAccount);

        PayPalAccount account = (PayPalAccount)result.getTarget();
        assertFalse(account.getImageUrl() == null);

        String token = result.getTarget().getToken();

        PayPalAccount foundAccount = gateway.paypalAccount().find(token);
        assertFalse(foundAccount == null);
    }

    @Test
    public void ignoresPassedBillingAddressParams() {
        String nonce = TestHelper.getNonceForPayPalAccount(gateway, "PAYPAL_CONSENT_CODE");
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        assertTrue(customerResult.isSuccess());
        Customer customer = customerResult.getTarget();

        PaymentMethodRequest request = new PaymentMethodRequest().
            paymentMethodNonce(nonce).
            customerId(customer.getId()).
            billingAddress().
                streetAddress("123 Abc Way").
                done();

        Result<? extends PaymentMethod> result = gateway.paymentMethod().create(request);
        assertTrue(result.isSuccess());
        assertTrue(result.getTarget() instanceof PayPalAccount);

        PayPalAccount account = (PayPalAccount)result.getTarget();
        assertFalse(account.getImageUrl() == null);
        String token = result.getTarget().getToken();

        PayPalAccount foundAccount = gateway.paypalAccount().find(token);
        assertFalse(foundAccount == null);
    }

    @Test
    public void doesNotReturnErrorIfCreditCardOptionsArePresentForPaypalNonce() {
        String nonce = TestHelper.getNonceForPayPalAccount(gateway, "PAYPAL_CONSENT_CODE");
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        assertTrue(customerResult.isSuccess());
        Customer customer = customerResult.getTarget();

        PaymentMethodRequest request = new PaymentMethodRequest().
            paymentMethodNonce(nonce).
            customerId(customer.getId()).
            options().
                verifyCard(true).
                failOnDuplicatePaymentMethod(true).
                verificationMerchantAccountId("not_a_real_merchant_account_id").
                done();

        Result<? extends PaymentMethod> result = gateway.paymentMethod().create(request);
        assertTrue(result.isSuccess());
    }

    @Test
    public void findCreditCard() {
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        assertTrue(customerResult.isSuccess());
        Customer customer = customerResult.getTarget();

        String nonce = TestHelper.generateUnlockedNonce(gateway, null, SandboxValues.CreditCardNumber.VISA.number);
        PaymentMethodRequest request = new PaymentMethodRequest().
            customerId(customer.getId()).
            paymentMethodNonce(nonce);

        Result<? extends PaymentMethod> result = gateway.paymentMethod().create(request);

        assertTrue(result.isSuccess());
        PaymentMethod paymentMethod = result.getTarget();

        PaymentMethod found = gateway.paymentMethod().find(paymentMethod.getToken());
        assertNotNull(found);
        assertTrue(found instanceof CreditCard);
    }

    @Test
    public void findAbstractPaymentMethod() {
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        assertTrue(customerResult.isSuccess());
        Customer customer = customerResult.getTarget();

        PaymentMethodRequest request = new PaymentMethodRequest().
            customerId(customer.getId()).
            paymentMethodNonce(Nonce.AbstractTransactable);

        Result<? extends PaymentMethod> result = gateway.paymentMethod().create(request);

        assertTrue(result.isSuccess());
        PaymentMethod paymentMethod = result.getTarget();

        PaymentMethod found = gateway.paymentMethod().find(paymentMethod.getToken());
        assertNotNull(found);
        assertNotNull(found.getToken());
        assertEquals(found.getToken(), paymentMethod.getToken());
    }


    @Test
    public void findBlankRaisesNotFoundError() {
        try {
            gateway.paymentMethod().find("");
            fail("Should throw NotFoundException");
        } catch (NotFoundException e) {
        }
    }

    @Test
    public void findMissingRaisesNotFoundError() {
        try {
            gateway.paymentMethod().find("missing");
            fail("Should throw NotFoundException");
        } catch (NotFoundException e) {
        }
    }

    @Test
    public void updateUpdatesCreditCards() {
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        Customer customer = customerResult.getTarget();

        CreditCardRequest creditCardRequest = new CreditCardRequest().
            customerId(customer.getId()).
            cardholderName("John Doe").
            cvv("123").
            number("5105105105105100").
            expirationDate("05/12");
        Result<CreditCard> creditCardResult = gateway.creditCard().create(creditCardRequest);
        assertTrue(creditCardResult.isSuccess());

        PaymentMethodRequest updateCardRequest = new PaymentMethodRequest().
            cardholderName("New Holder").
            cvv("456").
            number(SandboxValues.CreditCardNumber.VISA.number).
            expirationDate("06/13");
        String token = creditCardResult.getTarget().getToken();
        Result<? extends PaymentMethod> result = gateway.paymentMethod().update(token, updateCardRequest);

        assertTrue(result.isSuccess());
        assertTrue(result.getTarget() instanceof CreditCard);
        CreditCard creditCard = (CreditCard) result.getTarget();
        assertEquals(creditCard.getCardholderName(), "New Holder");
        assertEquals(creditCard.getBin(), SandboxValues.CreditCardNumber.VISA.number.substring(0, 6));
        assertEquals(creditCard.getExpirationDate(), "06/2013");
    }

    @Test
    public void updateBillingAddressOverwritesOldAddress() {
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        Customer customer = customerResult.getTarget();

        CreditCardRequest creditCardRequest = new CreditCardRequest().
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
        Result<CreditCard> creditCardResult = gateway.creditCard().create(creditCardRequest);
        assertTrue(creditCardResult.isSuccess());

        CreditCard oldCreditCard = creditCardResult.getTarget();

        PaymentMethodRequest updateCardRequest = new PaymentMethodRequest().
            billingAddress().
                region("Illinois").
                done();

        String token = oldCreditCard.getToken();
        Result<? extends PaymentMethod> result = gateway.paymentMethod().update(token, updateCardRequest);

        assertTrue(result.isSuccess());
        assertTrue(result.getTarget() instanceof CreditCard);
        CreditCard creditCard = (CreditCard) result.getTarget();
        assertEquals(creditCard.getBillingAddress().getRegion(),"Illinois");
        assertEquals(creditCard.getBillingAddress().getStreetAddress(), null);
        assertTrue(!creditCard.getBillingAddress().getId().equals(oldCreditCard.getBillingAddress().getId()));
    }

    @Test
    public void updateBillingAddressWhenOptionIsSpecified() {
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        Customer customer = customerResult.getTarget();

        CreditCardRequest creditCardRequest = new CreditCardRequest().
            customerId(customer.getId()).
            cardholderName("John Doe").
            cvv("123").
            number("5105105105105100").
            expirationDate("05/12").
            billingAddress().
                streetAddress("1 E Main St").
                done();
        Result<CreditCard> creditCardResult = gateway.creditCard().create(creditCardRequest);
        assertTrue(creditCardResult.isSuccess());

        CreditCard oldCreditCard = creditCardResult.getTarget();

        PaymentMethodRequest updateCardRequest = new PaymentMethodRequest().
            billingAddress().
                region("Illinois").
                options().
                    updateExisting(true).
                    done().
                done();

        String token = oldCreditCard.getToken();
        Result<? extends PaymentMethod> result = gateway.paymentMethod().update(token, updateCardRequest);

        assertTrue(result.isSuccess());
        assertTrue(result.getTarget() instanceof CreditCard);
        CreditCard creditCard = (CreditCard) result.getTarget();
        assertEquals(creditCard.getBillingAddress().getRegion(),"Illinois");
        assertEquals(creditCard.getBillingAddress().getStreetAddress(),"1 E Main St");
        assertEquals(creditCard.getBillingAddress().getId(), oldCreditCard.getBillingAddress().getId());
    }

    @Test
    public void updatesTheCountryViaCodes() {
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        Customer customer = customerResult.getTarget();
        CreditCardRequest creditCardRequest = new CreditCardRequest().
            customerId(customer.getId()).
            cardholderName("John Doe").
            number(SandboxValues.CreditCardNumber.VISA.number).
            expirationDate("05/12").
            billingAddress().
                streetAddress("1 E Main St").
                done();
        Result<CreditCard> creditCardResult = gateway.creditCard().create(creditCardRequest);
        assertTrue(creditCardResult.isSuccess());

        CreditCard oldCreditCard = creditCardResult.getTarget();

        PaymentMethodRequest updateCardRequest = new PaymentMethodRequest().
            billingAddress().
                countryName("American Samoa").
                countryCodeAlpha2("AS").
                countryCodeAlpha3("ASM").
                countryCodeNumeric("016").
                options().
                    updateExisting(true).
                    done().
                done();

        String token = oldCreditCard.getToken();
        Result<? extends PaymentMethod> result = gateway.paymentMethod().update(token, updateCardRequest);

        assertTrue(result.isSuccess());
        assertTrue(result.getTarget() instanceof CreditCard);
        CreditCard creditCard = (CreditCard) result.getTarget();
        Address billingAddress = creditCard.getBillingAddress();
        assertEquals(billingAddress.getCountryName(), "American Samoa");
        assertEquals(billingAddress.getCountryCodeAlpha2(), "AS");
        assertEquals(billingAddress.getCountryCodeAlpha3(), "ASM");
        assertEquals(billingAddress.getCountryCodeNumeric(), "016");
    }

    @Test
    public void updateCanPassExpirationMonthAndYear() {
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        Customer customer = customerResult.getTarget();
        CreditCardRequest creditCardRequest = new CreditCardRequest().
            customerId(customer.getId()).
            cardholderName("John Doe").
            number(SandboxValues.CreditCardNumber.VISA.number).
            expirationDate("05/12");
        Result<CreditCard> creditCardResult = gateway.creditCard().create(creditCardRequest);
        assertTrue(creditCardResult.isSuccess());

        CreditCard oldCreditCard = creditCardResult.getTarget();

        PaymentMethodRequest updateCardRequest = new PaymentMethodRequest().
            expirationMonth("06").
            expirationYear("2013");

        String token = oldCreditCard.getToken();
        Result<? extends PaymentMethod> result = gateway.paymentMethod().update(token, updateCardRequest);

        assertTrue(result.isSuccess());
        assertTrue(result.getTarget() instanceof CreditCard);
        CreditCard creditCard = (CreditCard) result.getTarget();
        assertEquals(creditCard.getExpirationMonth(), "06");
        assertEquals(creditCard.getExpirationYear(), "2013");
        assertEquals(creditCard.getExpirationDate(), "06/2013");
    }

    @Test
    public void verifiesUpdateIfVerifyCardIsTrue() {
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        Customer customer = customerResult.getTarget();
        CreditCardRequest creditCardRequest = new CreditCardRequest().
            customerId(customer.getId()).
            cardholderName("Original Holder").
            cvv("123").
            number(SandboxValues.CreditCardNumber.VISA.number).
            expirationDate("05/12");
        Result<CreditCard> creditCardResult = gateway.creditCard().create(creditCardRequest);
        assertTrue(creditCardResult.isSuccess());

        CreditCard oldCreditCard = creditCardResult.getTarget();
        PaymentMethodRequest updateCardRequest = new PaymentMethodRequest().
            cardholderName("New Holder").
            cvv("456").
            number(SandboxValues.FailsVerification.MASTER_CARD.number).
            expirationDate("06/2013").
            options().
                verifyCard(true).
                done();

        String token = oldCreditCard.getToken();
        Result<? extends PaymentMethod> result = gateway.paymentMethod().update(token, updateCardRequest);

        assertFalse(result.isSuccess());
        assertEquals(result.getCreditCardVerification().getStatus(), CreditCardVerification.Status.PROCESSOR_DECLINED);
        assertNull(result.getCreditCardVerification().getGatewayRejectionReason());
    }

    @Test
    public void updateCanUpdateTheBillingAddress() {
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        Customer customer = customerResult.getTarget();

        CreditCardRequest creditCardRequest = new CreditCardRequest().
            cardholderName("Original Holder").
            customerId(customer.getId()).
            cvv("123").
            number(SandboxValues.CreditCardNumber.VISA.number).
            expirationDate("05/12").
            billingAddress().
                firstName("Old First Name").
                lastName("Old Last Name").
                company("Old Company").
                streetAddress("123 Old St").
                extendedAddress("Apt Old").
                locality("Old City").
                region("Old State").
                postalCode("12345").
                countryName("Canada").
                done();
        Result<CreditCard> creditCardResult = gateway.creditCard().create(creditCardRequest);
        assertTrue(creditCardResult.isSuccess());

        CreditCard oldCreditCard = creditCardResult.getTarget();

        PaymentMethodRequest updateCardRequest = new PaymentMethodRequest().
            options().
                verifyCard(false).
                done().
            billingAddress().
                firstName("New First Name").
                lastName("New Last Name").
                company("New Company").
                streetAddress("123 New St").
                extendedAddress("Apt New").
                locality("New City").
                region("New State").
                postalCode("56789").
                countryName("United States of America").
                done();

        String token = oldCreditCard.getToken();
        Result<? extends PaymentMethod> result = gateway.paymentMethod().update(token, updateCardRequest);

        assertTrue(result.isSuccess());
        assertTrue(result.getTarget() instanceof CreditCard);
        CreditCard creditCard = (CreditCard) result.getTarget();
        Address billingAddress = creditCard.getBillingAddress();
        assertEquals(billingAddress.getFirstName(), "New First Name");
        assertEquals(billingAddress.getLastName(), "New Last Name");
        assertEquals(billingAddress.getCompany(), "New Company");
        assertEquals(billingAddress.getStreetAddress(), "123 New St");
        assertEquals(billingAddress.getExtendedAddress(), "Apt New");
        assertEquals(billingAddress.getLocality(), "New City");
        assertEquals(billingAddress.getRegion(), "New State");
        assertEquals(billingAddress.getPostalCode(), "56789");
        assertEquals(billingAddress.getCountryName(), "United States of America");
    }

    @Test
    public void returnsAnErrorResponseIfInvalid() {
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        Customer customer = customerResult.getTarget();
        CreditCardRequest creditCardRequest = new CreditCardRequest().
            customerId(customer.getId()).
            cardholderName("Original Holder").
            number(SandboxValues.CreditCardNumber.VISA.number).
            expirationDate("05/12");
        Result<CreditCard> creditCardResult = gateway.creditCard().create(creditCardRequest);
        assertTrue(creditCardResult.isSuccess());

        CreditCard oldCreditCard = creditCardResult.getTarget();

        PaymentMethodRequest updateCardRequest = new PaymentMethodRequest().
            cardholderName("New Holder").
            number("invalid").
            expirationDate("05/2014");

        String token = oldCreditCard.getToken();
        Result<? extends PaymentMethod> result = gateway.paymentMethod().update(token, updateCardRequest);

        assertFalse(result.isSuccess());
        assertEquals(result.getErrors().forObject("creditCard").onField("number").get(0).getMessage(), "Credit card number must be 12-19 digits.");
    }

    @Test
    public void updateCanUpdateTheDefault() {
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        Customer customer = customerResult.getTarget();
        CreditCardRequest creditCardRequest1 = new CreditCardRequest().
            customerId(customer.getId()).
            number(SandboxValues.CreditCardNumber.VISA.number).
            expirationDate("05/09");
        CreditCardRequest creditCardRequest2 = new CreditCardRequest().
            customerId(customer.getId()).
            number(SandboxValues.CreditCardNumber.VISA.number).
            expirationDate("05/09");
        Result<CreditCard> creditCardResult = gateway.creditCard().create(creditCardRequest1);
        CreditCard creditCard1 = creditCardResult.getTarget();
        creditCardResult = gateway.creditCard().create(creditCardRequest2);
        CreditCard creditCard2 = creditCardResult.getTarget();

        assertTrue(creditCard1.isDefault());
        assertFalse(creditCard2.isDefault());

        PaymentMethodRequest updateCardRequest = new PaymentMethodRequest().
            options().
                makeDefault(true).
                done();

        String token = creditCard2.getToken();
        Result<? extends PaymentMethod> result = gateway.paymentMethod().update(token, updateCardRequest);

        PaymentMethod updatedResult1 = gateway.paymentMethod().find(creditCard1.getToken());
        PaymentMethod updatedResult2 = gateway.paymentMethod().find(creditCard2.getToken());
        assertFalse(updatedResult1.isDefault());
        assertTrue(updatedResult2.isDefault());
    }

    @Test
    public void updatesAPayPalAccountToken() {
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        Customer customer = customerResult.getTarget();
        String originalToken = "paypal-account-" + (int)new Date().getTime();
        String nonce = TestHelper.getNonceForPayPalAccount(gateway, "consent-code", originalToken);
        PaymentMethodRequest request = new PaymentMethodRequest().
            paymentMethodNonce(nonce).
            customerId(customer.getId());

        Result<? extends PaymentMethod> originalResult = gateway.paymentMethod().create(request);
        String updatedToken = "UPDATED_TOKEN-" + new Random().nextInt();

        request = new PaymentMethodRequest().token(updatedToken);
        Result<? extends PaymentMethod> updatedResult = gateway.paymentMethod().update(
            originalToken,
            request);

        PayPalAccount updatedPayPalAccount = gateway.paypalAccount().find(updatedToken);
        PayPalAccount originalPayPalAccount = (PayPalAccount)originalResult.getTarget();
        assertEquals(updatedPayPalAccount.getEmail(), originalPayPalAccount.getEmail());

        try {
            gateway.paypalAccount().find(originalToken);
            fail("Didn't throw a NotFoundException for a nonexistent account token on find");
        } catch (NotFoundException ex) {

        }
    }

    @Test
    public void UpdateMakesPaypalAccountDefaultPaymentMethod() {
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        Customer customer = customerResult.getTarget();
        CreditCardRequest creditCardRequest = new CreditCardRequest().
            customerId(customer.getId()).
            number(SandboxValues.CreditCardNumber.VISA.number).
            expirationDate("05/09").
            options().
                makeDefault(true).
                done();
        Result<CreditCard> creditCardResult = gateway.creditCard().create(creditCardRequest);
        assertTrue(creditCardResult.isSuccess());

        String nonce = TestHelper.getNonceForPayPalAccount(gateway, "consent-code");
        PaymentMethodRequest request = new PaymentMethodRequest().
            paymentMethodNonce(nonce).
            customerId(customer.getId());

        Result<? extends PaymentMethod> originalResult = gateway.paymentMethod().create(request);
        String originalToken = originalResult.getTarget().getToken();

        request = new PaymentMethodRequest().
            token(originalToken).
            options().
                makeDefault(true).
                done();
        Result<? extends PaymentMethod> updatedResult = gateway.paymentMethod().update(
            originalToken,
            request);

        PayPalAccount updatedPayPalAccount = gateway.paypalAccount().find(originalToken);
        assertTrue(updatedPayPalAccount.isDefault());

    }

    @Test
    public void returnsErrorIfTokenForAccountIsUsedForUpdate() {
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        Customer customer = customerResult.getTarget();
        String firstToken = "paypal-account-" + new Random().nextInt();
        String secondToken = "paypal-account-" + new Random().nextInt();
        String firstNonce = TestHelper.getNonceForPayPalAccount(
            gateway,
            "consent-code",
            firstToken);

        PaymentMethodRequest request = new PaymentMethodRequest().
            paymentMethodNonce(firstNonce).
            customerId(customer.getId());

        Result<? extends PaymentMethod> firstResult = gateway.paymentMethod().create(request);
        String secondNonce = TestHelper.getNonceForPayPalAccount(
            gateway,
            "consent-code",
            secondToken);

        request = new PaymentMethodRequest().
            paymentMethodNonce(secondNonce).
            customerId(customer.getId());
        Result<? extends PaymentMethod> secondResult = gateway.paymentMethod().create(request);

        request = new PaymentMethodRequest().token(secondToken);
        Result<? extends PaymentMethod> updatedResult = gateway.paymentMethod().update(firstToken, request);

        assertFalse(updatedResult.isSuccess());
        assertEquals(updatedResult.getErrors().getAllDeepValidationErrors().get(0).getCode().code, "92906");
    }

    @Test
    public void updateDoesNotReturnErrorIfCardOptionsArePresentForPaypalNonce() {
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        Customer customer = customerResult.getTarget();
        String originalToken = "paypal-account-" + new Random().nextInt();
        String nonce = TestHelper.getNonceForPayPalAccount(
            gateway,
            "consent-code",
            originalToken);

        PaymentMethodRequest request = new PaymentMethodRequest().
            paymentMethodNonce(nonce).
            customerId(customer.getId()).
            options().
                verifyCard(true).
                failOnDuplicatePaymentMethod(true).
                verificationMerchantAccountId("not_a_real_merchant_account_id").
                done();
        Result<? extends PaymentMethod> result = gateway.paymentMethod().create(request);
        assertTrue(result.isSuccess());
    }

    @Test
    public void allowsPassingTheBillingAddressOutsideNonce() {
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        Customer customer = customerResult.getTarget();
        CreditCardRequest creditCardRequest = new CreditCardRequest().
            number("4111111111111111").
            expirationMonth("12").
            expirationYear("2020");
        String nonce = TestHelper.generateNonceForCreditCard(gateway, creditCardRequest, customer.getId(), false);

        PaymentMethodRequest request = new PaymentMethodRequest().
            paymentMethodNonce(nonce).
            customerId(customer.getId()).
            billingAddress().
                streetAddress("123 Abc Way").
                done();

        Result<? extends PaymentMethod> result = gateway.paymentMethod().create(request);
        assertTrue(result.isSuccess());
        assertTrue(result.getTarget() instanceof CreditCard);

        String token = result.getTarget().getToken();
        CreditCard foundCreditCard = gateway.creditCard().find(token);
        assertFalse(foundCreditCard == null);
        assertEquals(foundCreditCard.getBillingAddress().getStreetAddress(), "123 Abc Way");
    }

    @Test
    public void updateOverridesTheBillingAddressInTheNonce() {
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        Customer customer = customerResult.getTarget();
        CreditCardRequest creditCardRequest = new CreditCardRequest().
            number("4111111111111111").
            expirationMonth("12").
            expirationYear("2020").
            billingAddress().
                streetAddress("456 Xyz Way").
                done();
        String nonce = TestHelper.generateNonceForCreditCard(gateway, creditCardRequest, customer.getId(), false);

        PaymentMethodRequest request = new PaymentMethodRequest().
            paymentMethodNonce(nonce).
            customerId(customer.getId()).
            billingAddress().
                streetAddress("123 Abc Way").
                done();

        Result<? extends PaymentMethod> result = gateway.paymentMethod().create(request);

        assertTrue(result.isSuccess());
        assertTrue(result.getTarget() instanceof CreditCard);
        String token = result.getTarget().getToken();

        CreditCard foundCreditCard = gateway.creditCard().find(token);
        assertFalse(foundCreditCard == null);
        assertEquals(foundCreditCard.getBillingAddress().getStreetAddress(), "123 Abc Way");
    }

    @Test
    public void updateDoesNotOverrideBillingAddressForVaultedCreditCards() {
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        Customer customer = customerResult.getTarget();
        CreditCardRequest creditCardRequest = new CreditCardRequest().
            number("4111111111111111").
            expirationMonth("12").
            expirationYear("2020").
            billingAddress().
                streetAddress("456 Xyz Way").
                done();
        String nonce = TestHelper.generateNonceForCreditCard(gateway, creditCardRequest, customer.getId(), true);

        PaymentMethodRequest request = new PaymentMethodRequest().
            paymentMethodNonce(nonce).
            customerId(customer.getId()).
            billingAddress().
                streetAddress("123 Abc Way").
                done();

        Result<? extends PaymentMethod> result = gateway.paymentMethod().create(request);

        assertTrue(result.isSuccess());
        assertTrue(result.getTarget() instanceof CreditCard);
        String token = result.getTarget().getToken();

        CreditCard foundCreditCard = gateway.creditCard().find(token);
        assertFalse(foundCreditCard == null);
        assertEquals(foundCreditCard.getBillingAddress().getStreetAddress(), "456 Xyz Way");
    }
}
