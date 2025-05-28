package com.braintreegateway.integrationtest;

import com.braintreegateway.*;
import com.braintreegateway.enums.PrepaidReloadable;
import com.braintreegateway.enums.Business;
import com.braintreegateway.enums.Consumer;
import com.braintreegateway.enums.Corporate;
import com.braintreegateway.enums.Purchase;
import com.braintreegateway.test.Nonce;
import com.braintreegateway.exceptions.NotFoundException;
import com.braintreegateway.test.CreditCardDefaults;
import com.braintreegateway.test.CreditCardNumbers;
import com.braintreegateway.testhelpers.MerchantAccountTestConstants;
import com.braintreegateway.testhelpers.TestHelper;
import com.braintreegateway.SandboxValues.CreditCardNumber;

import java.math.BigDecimal;
import java.util.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CreditCardIT extends IntegrationTest implements MerchantAccountTestConstants {

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
                phoneNumber("312-123-4567").
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
        assertEquals("312-123-4567", billingAddress.getPhoneNumber());
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
    public void createWithNetworkTransactionId() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();

        CreditCardRequest request = new CreditCardRequest().
            customerId(customer.getId()).
            number("4111111111111111").
            expirationDate("05/23").
            externalVault().
                networkTransactionId("MCC123456789").
                done();

        Result<CreditCard> result = gateway.creditCard().create(request);
        assertTrue(result.isSuccess());
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
    public void createWithThreeDSecureNonce() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest request = new CreditCardRequest().
            customerId(customer.getId()).
            paymentMethodNonce(Nonce.ThreeDSecureVisaFullAuthentication).
            options().
                verifyCard(true).
                done();

        Result<CreditCard> result = gateway.creditCard().create(request);
        assertTrue(result.isSuccess());

        CreditCard creditCard = (CreditCard)result.getTarget();

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
        CreditCardRequest request = new CreditCardRequest().
            customerId(customer.getId()).
            number("4111111111111111").
            expirationDate("05/22").
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
                done();
        Result<CreditCard> result = gateway.creditCard().create(request);
        assertTrue(result.isSuccess());
    }

    @Test
    public void createWithThreeDSecurePassThruWithoutEciFlag() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest request = new CreditCardRequest().
            customerId(customer.getId()).
            number("4111111111111111").
            expirationDate("05/20").
            threeDSecurePassThruRequest().
                cavv("some-cavv").
                xid("some-xid").
                threeDSecureVersion("2.2.0").
                done().
            options().
                verifyCard(true).
                done();
        Result<CreditCard> result = gateway.creditCard().create(request);
        assertFalse(result.isSuccess());
        assertEquals(
                ValidationErrorCode.VERIFICATION_THREE_D_SECURE_PASS_THRU_ECI_FLAG_IS_REQUIRED,
                result.getErrors().getAllDeepValidationErrors().get(0).getCode());
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

    @Test
    public void updateWithThreeDSecurePassThru() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest request = new CreditCardRequest().
            customerId(customer.getId()).
            number("4111111111111111").
            expirationDate("05/22");

        CreditCard card = gateway.creditCard().create(request).getTarget();

        CreditCardRequest updateRequest = new CreditCardRequest().
            number("4111111111111111").
            expirationDate("10/10").
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
                done();

        Result<CreditCard> updated_result = gateway.creditCard().update(card.getToken(), updateRequest);

        assertTrue(updated_result.isSuccess());
    }

    @Test
    public void updateWithThreeDSecurePassThruWithoutEciFlag() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest request = new CreditCardRequest().
            customerId(customer.getId()).
            number("4111111111111111").
            expirationDate("05/22");

        CreditCard card = gateway.creditCard().create(request).getTarget();

        CreditCardRequest updateRequest = new CreditCardRequest().
            number("4111111111111111").
            expirationDate("10/10").
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
            done();

        Result<CreditCard> updated_result = gateway.creditCard().update(card.getToken(), updateRequest);

        assertFalse(updated_result.isSuccess());
        assertEquals(
            ValidationErrorCode.VERIFICATION_THREE_D_SECURE_PASS_THRU_ECI_FLAG_IS_REQUIRED,
            updated_result.getErrors().getAllDeepValidationErrors().get(0).getCode());
    }

    @Test
    public void updateIncludesRiskDataWhenSkipAdvancedFraudCheckingIsFalse() {
        createFraudProtectionEnterpriseMerchantGateway();
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest createRequest = new CreditCardRequest().
            customerId(customer.getId()).
            cardholderName("John Doe").
            cvv("123").
            number("4111111111111111").
            expirationDate("05/12").
            options().
                done();

        Result<CreditCard> createResult = gateway.creditCard().create(createRequest);
        assertTrue(createResult.isSuccess());

        CreditCard originalCard = createResult.getTarget();
        assertNotNull(originalCard);

        CreditCardRequest updateRequest = new CreditCardRequest().
            expirationDate("06/23").
            options().
                verifyCard(true).
                skipAdvancedFraudChecking(false).
                done();

        Result<CreditCard> updateResult = gateway.creditCard().update(originalCard.getToken(), updateRequest);

        CreditCard updatedCard = updateResult.getTarget();
        assertNotNull(updatedCard);

        CreditCardVerification verification = updatedCard.getVerification();
        assertNotNull(verification);

        RiskData riskData = verification.getRiskData();
        assertNotNull(riskData);
    }

    @Test
    public void updateDoesNotIncludeRiskDataWhenSkipAdvancedFraudCheckingIsTrue() {
        createFraudProtectionEnterpriseMerchantGateway();
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest createRequest = new CreditCardRequest().
            customerId(customer.getId()).
            cardholderName("John Doe").
            cvv("123").
            number("4111111111111111").
            expirationDate("05/12").
            options().
                done();

        Result<CreditCard> createResult = gateway.creditCard().create(createRequest);
        assertTrue(createResult.isSuccess());

        CreditCard originalCard = createResult.getTarget();
        assertNotNull(originalCard);

        CreditCardRequest updateRequest = new CreditCardRequest().
            expirationDate("06/23").
            options().
                verifyCard(true).
                skipAdvancedFraudChecking(true).
                done();

        Result<CreditCard> updateResult = gateway.creditCard().update(originalCard.getToken(), updateRequest);

        CreditCard updatedCard = updateResult.getTarget();
        assertNotNull(updatedCard);

        CreditCardVerification verification = updatedCard.getVerification();
        assertNotNull(verification);

        RiskData riskData = verification.getRiskData();
        assertNull(riskData);
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

    @Test
    public void updateWithAccountTypeCredit() {
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
            number(CreditCardNumber.HIPER.number).
            expirationDate("01/20").
            options().
                verifyCard(true).
                verificationMerchantAccountId("hiper_brl").
                verificationAccountType("credit").
                done();

        CreditCard updatedCreditCard = gateway.creditCard().update(creditCard.getToken(), updateRequest).getTarget();

        assertEquals("credit", updatedCreditCard.getVerification().getCreditCard().getAccountType());
    }

    @Test
    public void updateWithAccountTypeDebit() {
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
            number(CreditCardNumber.HIPER.number).
            expirationDate("01/20").
            options().
                verifyCard(true).
                verificationMerchantAccountId("card_processor_brl").
                verificationAccountType("debit").
                done();

        CreditCard updatedCreditCard = gateway.creditCard().update(creditCard.getToken(), updateRequest).getTarget();

        assertEquals("debit", updatedCreditCard.getVerification().getCreditCard().getAccountType());
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

    @Test
    public void findWithBadToken() {
        assertThrows(NotFoundException.class, () -> {
            gateway.creditCard().find("badToken");
        });
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
        createFraudProtectionEnterpriseMerchantGateway();
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
    }

    @Test
    public void createIncludesRiskDataWhenSkipAdvancedFraudCheckingIsFalse() {
        createFraudProtectionEnterpriseMerchantGateway();
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest request = new CreditCardRequest().
            customerId(customer.getId()).
            cardholderName("John Doe").
            cvv("123").
            number("4111111111111111").
            expirationDate("05/12").
            options().
                verifyCard(true).
                skipAdvancedFraudChecking(false).
                done();

        Result<CreditCard> result = gateway.creditCard().create(request);
        assertTrue(result.isSuccess());

        CreditCard card = result.getTarget();
        assertNotNull(card);

        CreditCardVerification verification = card.getVerification();
        assertNotNull(verification);

        RiskData riskData = verification.getRiskData();
        assertNotNull(riskData);
    }

    @Test
    public void createDoesNotIncludeRiskDataWhenSkipAdvancedFraudCheckingIsTrue() {
        createFraudProtectionEnterpriseMerchantGateway();
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest request = new CreditCardRequest().
            customerId(customer.getId()).
            cardholderName("John Doe").
            cvv("123").
            number("4111111111111111").
            expirationDate("05/12").
            options().
                verifyCard(true).
                skipAdvancedFraudChecking(true).
                done();

        Result<CreditCard> result = gateway.creditCard().create(request);
        assertTrue(result.isSuccess());

        CreditCard card = result.getTarget();
        assertNotNull(card);

        CreditCardVerification verification = card.getVerification();
        assertNotNull(verification);

        RiskData riskData = verification.getRiskData();
        assertNull(riskData);
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
    public void verifyCreditCardAccountTypeCredit() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest request = new CreditCardRequest().
            customerId(customer.getId()).
            cardholderName("John Doe").
            cvv("123").
            number(CreditCardNumber.HIPER.number).
            expirationDate("05/12").
            options().
                verifyCard(true).
                verificationMerchantAccountId("hiper_brl").
                verificationAccountType("credit").
                done();

        Result<CreditCard> result = gateway.creditCard().create(request);
        assertTrue(result.isSuccess());
        assertEquals("credit", result.getTarget().getVerification().getCreditCard().getAccountType());
    }

    @Test
    public void verifyCreditCardAccountTypeDebit() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest request = new CreditCardRequest().
            customerId(customer.getId()).
            cardholderName("John Doe").
            cvv("123").
            number(CreditCardNumber.HIPER.number).
            expirationDate("05/12").
            options().
                verifyCard(true).
                verificationMerchantAccountId("card_processor_brl").
                verificationAccountType("debit").
                done();

        Result<CreditCard> result = gateway.creditCard().create(request);
        assertTrue(result.isSuccess());
        assertEquals("debit", result.getTarget().getVerification().getCreditCard().getAccountType());
    }

    @Test
    public void verifyPrepaidReloadableInVerification() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest request = new CreditCardRequest().
            customerId(customer.getId()).
            cardholderName("John Doe").
            cvv("123").
            number(CreditCardNumbers.CardTypeIndicators.PrepaidReloadable.getValue()).
            expirationDate("05/29").
            options().
                verifyCard(true).
                done();

        Result<CreditCard> result = gateway.creditCard().create(request);
        assertTrue(result.isSuccess());
        assertEquals(PrepaidReloadable.YES, result.getTarget().getVerification().getCreditCard().getPrepaidReloadable());
    }

    @Test
    public void verifyBusinessInVerification() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest request = new CreditCardRequest().
            customerId(customer.getId()).
            cardholderName("John Doe").
            cvv("123").
            number(CreditCardNumbers.CardTypeIndicators.Business.getValue()).
            expirationDate("05/29").
            options().
                verifyCard(true).
                done();

        Result<CreditCard> result = gateway.creditCard().create(request);
        assertTrue(result.isSuccess());
        assertEquals(Business.YES, result.getTarget().getVerification().getCreditCard().getBusiness());
    }

    @Test
    public void verifyConsumerInVerification() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest request = new CreditCardRequest().
            customerId(customer.getId()).
            cardholderName("John Doe").
            cvv("123").
            number(CreditCardNumbers.CardTypeIndicators.Consumer.getValue()).
            expirationDate("05/29").
            options().
                verifyCard(true).
                done();

        Result<CreditCard> result = gateway.creditCard().create(request);
        assertTrue(result.isSuccess());
        assertEquals(Consumer.YES, result.getTarget().getVerification().getCreditCard().getConsumer());
    }

    @Test
    public void verifyCorporateInVerification() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest request = new CreditCardRequest().
            customerId(customer.getId()).
            cardholderName("John Doe").
            cvv("123").
            number(CreditCardNumbers.CardTypeIndicators.Corporate.getValue()).
            expirationDate("05/29").
            options().
                verifyCard(true).
                done();

        Result<CreditCard> result = gateway.creditCard().create(request);
        assertTrue(result.isSuccess());
        assertEquals(Corporate.YES, result.getTarget().getVerification().getCreditCard().getCorporate());
    }

    @Test
    public void verifyPurchaseInVerification() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest request = new CreditCardRequest().
            customerId(customer.getId()).
            cardholderName("John Doe").
            cvv("123").
            number(CreditCardNumbers.CardTypeIndicators.Purchase.getValue()).
            expirationDate("05/29").
            options().
                verifyCard(true).
                done();

        Result<CreditCard> result = gateway.creditCard().create(request);
        assertTrue(result.isSuccess());
        assertEquals(Purchase.YES, result.getTarget().getVerification().getCreditCard().getPurchase());
    }

    @Test
    public void verifyCreditCardWithErrorAccountTypeIsInvalid() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest request = new CreditCardRequest().
            customerId(customer.getId()).
            cardholderName("John Doe").
            cvv("123").
            number(CreditCardNumber.HIPER.number).
            expirationDate("05/12").
            options().
                verifyCard(true).
                verificationMerchantAccountId("hiper_brl").
                verificationAccountType("ach").
                done();

        Result<CreditCard> result = gateway.creditCard().create(request);
        assertFalse(result.isSuccess());
        assertEquals(ValidationErrorCode.CREDIT_CARD_OPTIONS_VERIFICATION_ACCOUNT_TYPE_IS_INVALID,
                result.getErrors().forObject("credit-card").forObject("options").onField("verification-account-type").get(0).getCode());
    }

    @Test
    public void verifyCreditCardWithErrorAccountTypeNotSupported() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest request = new CreditCardRequest().
            customerId(customer.getId()).
            cardholderName("John Doe").
            cvv("123").
            number(CreditCardNumber.VISA.number).
            expirationDate("05/12").
            options().
                verifyCard(true).
                verificationAccountType("credit").
                done();

        Result<CreditCard> result = gateway.creditCard().create(request);
        assertFalse(result.isSuccess());
        assertEquals(ValidationErrorCode.CREDIT_CARD_OPTIONS_VERIFICATION_ACCOUNT_TYPE_NOT_SUPPORTED,
                result.getErrors().forObject("credit-card").forObject("options").onField("verification-account-type").get(0).getCode());
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
    public void businessCard() {
        BraintreeGateway processingRulesGateway = new BraintreeGateway(Environment.DEVELOPMENT, "processing_rules_merchant_id", "processing_rules_public_key", "processing_rules_private_key");
        Customer customer = processingRulesGateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest request = new CreditCardRequest().
            customerId(customer.getId()).
            number(CreditCardNumbers.CardTypeIndicators.Business.getValue()).
            expirationDate("05/12").
            options().
                verifyCard(true).
                done();

        Result<CreditCard> result = processingRulesGateway.creditCard().create(request);
        CreditCard card = result.getTarget();

        assertEquals(Business.YES, card.getBusiness());
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
    public void consumerCard() {
        BraintreeGateway processingRulesGateway = new BraintreeGateway(Environment.DEVELOPMENT, "processing_rules_merchant_id", "processing_rules_public_key", "processing_rules_private_key");
        Customer customer = processingRulesGateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest request = new CreditCardRequest().
            customerId(customer.getId()).
            number(CreditCardNumbers.CardTypeIndicators.Consumer.getValue()).
            expirationDate("05/12").
            options().
                verifyCard(true).
                done();

        Result<CreditCard> result = processingRulesGateway.creditCard().create(request);
        CreditCard card = result.getTarget();

        assertEquals(Consumer.YES, card.getConsumer());
    }

    @Test
    public void corporateCard() {
        BraintreeGateway processingRulesGateway = new BraintreeGateway(Environment.DEVELOPMENT, "processing_rules_merchant_id", "processing_rules_public_key", "processing_rules_private_key");
        Customer customer = processingRulesGateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest request = new CreditCardRequest().
            customerId(customer.getId()).
            number(CreditCardNumbers.CardTypeIndicators.Corporate.getValue()).
            expirationDate("05/12").
            options().
                verifyCard(true).
                done();

        Result<CreditCard> result = processingRulesGateway.creditCard().create(request);
        CreditCard card = result.getTarget();

        assertEquals(Corporate.YES, card.getCorporate());
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
    public void prepaidReloadableCard() {
        BraintreeGateway processingRulesGateway = new BraintreeGateway(Environment.DEVELOPMENT, "processing_rules_merchant_id", "processing_rules_public_key", "processing_rules_private_key");
        Customer customer = processingRulesGateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest request = new CreditCardRequest().
            customerId(customer.getId()).
            number(CreditCardNumbers.CardTypeIndicators.PrepaidReloadable.getValue()).
            expirationDate("05/12").
            options().
                verifyCard(true).
                done();

        Result<CreditCard> result = processingRulesGateway.creditCard().create(request);
        CreditCard card = result.getTarget();

        assertEquals(PrepaidReloadable.YES, card.getPrepaidReloadable());
    }

    @Test
    public void purchaseCard() {
        BraintreeGateway processingRulesGateway = new BraintreeGateway(Environment.DEVELOPMENT, "processing_rules_merchant_id", "processing_rules_public_key", "processing_rules_private_key");
        Customer customer = processingRulesGateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest request = new CreditCardRequest().
            customerId(customer.getId()).
            number(CreditCardNumbers.CardTypeIndicators.Purchase.getValue()).
            expirationDate("05/12").
            options().
                verifyCard(true).
                done();

        Result<CreditCard> result = processingRulesGateway.creditCard().create(request);
        CreditCard card = result.getTarget();

        assertEquals(Purchase.YES, card.getPurchase());
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
        assertEquals(PrepaidReloadable.NO, card.getPrepaidReloadable());
        assertEquals(Business.NO, card.getBusiness());
        assertEquals(Consumer.NO, card.getConsumer());
        assertEquals(Corporate.NO, card.getCorporate());
        assertEquals(Purchase.NO, card.getPurchase());
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
        assertEquals(PrepaidReloadable.UNKNOWN, card.getPrepaidReloadable());
        assertEquals(Business.UNKNOWN, card.getBusiness());
        assertEquals(Consumer.UNKNOWN, card.getConsumer());
        assertEquals(Corporate.UNKNOWN, card.getCorporate());
        assertEquals(Purchase.UNKNOWN, card.getPurchase());
        assertEquals("Unknown", card.getCountryOfIssuance());
        assertEquals("Unknown", card.getIssuingBank());
        assertEquals("Unknown", card.getProductId());
    }

    @Test
    public void findNetworkTokenizedCreditCard() {
        assertTrue(gateway.creditCard().find("network_tokenized_credit_card").isNetworkTokenized());
    }

    @Test
    public void findNonNetworkTokenizedCreditCard() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest request = new CreditCardRequest().
            customerId(customer.getId()).
            cardholderName("John Doe").
            number("5105105105105100").
            expirationDate("05/12");
        CreditCard card = gateway.creditCard().create(request).getTarget();

        assertFalse(gateway.creditCard().find(card.getToken()).isNetworkTokenized());
    }

    @Test
    public void createWithCurrencyIsoCodeSpecified() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest request = new CreditCardRequest().
                customerId(customer.getId()).
                cardholderName("John Doe").
                cvv("123").
                number(CreditCardNumber.VISA.number).
                expirationDate("05/21").
                options().
                    verificationCurrencyIsoCode("USD").
                    verifyCard(true).
                    done();
        Result<CreditCard> result = gateway.creditCard().create(request);
        assertTrue(result.isSuccess());
        CreditCard card = result.getTarget();
        assertEquals("USD", card.getVerification().getCurrencyIsoCode());
    }

    @Test
    public void createWithInvalidCurrencyIsoCodeSpecified() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest request = new CreditCardRequest().
                customerId(customer.getId()).
                cardholderName("John Doe").
                cvv("123").
                number(CreditCardNumber.VISA.number).
                expirationDate("05/21").
                options().
                    verificationCurrencyIsoCode("JP").
                    verifyCard(true).
                    done();
        Result<CreditCard> result = gateway.creditCard().create(request);
        assertFalse(result.isSuccess());
        assertEquals(
                ValidationErrorCode.CREDIT_CARD_OPTIONS_VERIFICATION_INVALID_PRESENTMENT_CURRENCY,
                result.getErrors().getAllDeepValidationErrors().get(0).getCode());
    }

    @Test
    public void updateWithCurrencyIsoCodeSpecified() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest request = new CreditCardRequest().
                customerId(customer.getId()).
                cardholderName("John Doe").
                cvv("123").
                number(CreditCardNumber.VISA.number).
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
                options().
                    verificationCurrencyIsoCode("USD").
                    verifyCard(true).
                    done();

        Result<CreditCard> updateResult = gateway.creditCard().update(card.getToken(), updateRequest);
        assertTrue(updateResult.isSuccess());
        CreditCard updatedCard = updateResult.getTarget();

        assertEquals("USD", updatedCard.getVerification().getCurrencyIsoCode());
    }

    @Test
    public void updateWithInvalidCurrencyIsoCodeSpecified() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest request = new CreditCardRequest().
                customerId(customer.getId()).
                cardholderName("John Doe").
                cvv("123").
                number(CreditCardNumber.VISA.number).
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
                options().
                    verificationCurrencyIsoCode("JP").
                    verifyCard(true).
                    done();

        Result<CreditCard> updateResult = gateway.creditCard().update(card.getToken(), updateRequest);
        assertFalse(updateResult.isSuccess());
        assertEquals(
                ValidationErrorCode.CREDIT_CARD_OPTIONS_VERIFICATION_INVALID_PRESENTMENT_CURRENCY,
                updateResult.getErrors().getAllDeepValidationErrors().get(0).getCode());
    }

    @Test
    public void createIncludesAniInVerificationWhenAccountInformationInquiryIsPresent() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest request = new CreditCardRequest().
            customerId(customer.getId()).
            cardholderName("John Doe").
            cvv("123").
            number("4111111111111111").
            expirationDate("05/12").
            billingAddress().
            firstName("John").
            lastName("Doe").
            done().
            options().
            verifyCard(true).
            accountInformationInquiry("send_data").
            done();

        Result<CreditCard> result = gateway.creditCard().create(request);
        assertTrue(result.isSuccess());

        CreditCard card = result.getTarget();
        assertNotNull(card);

        CreditCardVerification verification = card.getVerification();
        assertNotNull(verification);
        assertNotNull(verification.getAniFirstNameResponseCode());
        assertNotNull(verification.getAniLastNameResponseCode());
    }

    @Test
    public void updateIncludesAniInVerificationWhenAccountInformationInquiryIsPresent() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest createRequest = new CreditCardRequest().
            customerId(customer.getId()).
            cardholderName("John Doe").
            cvv("123").
            number("4111111111111111").
            expirationDate("05/32").
            billingAddress().
            firstName("John").
            lastName("Doe").
            done().
            options().
            done();

        Result<CreditCard> createResult = gateway.creditCard().create(createRequest);
        assertTrue(createResult.isSuccess());

        CreditCard originalCard = createResult.getTarget();
        assertNotNull(originalCard);

        CreditCardRequest updateRequest = new CreditCardRequest().
            options().
            verifyCard(true).
            accountInformationInquiry("send_data").
            done();

        Result<CreditCard> updateResult = gateway.creditCard().update(originalCard.getToken(), updateRequest);

        CreditCard updatedCard = updateResult.getTarget();
        assertNotNull(updatedCard);

        CreditCardVerification verification = updatedCard.getVerification();
        assertNotNull(verification);
        assertNotNull(verification.getAniFirstNameResponseCode());
        assertNotNull(verification.getAniLastNameResponseCode());
    }
}
