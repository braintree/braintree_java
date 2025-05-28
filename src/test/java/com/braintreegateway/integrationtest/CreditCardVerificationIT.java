package com.braintreegateway.integrationtest;
import com.braintreegateway.*;
import com.braintreegateway.enums.PrepaidReloadable;
import com.braintreegateway.enums.Business;
import com.braintreegateway.enums.Consumer;
import com.braintreegateway.enums.Corporate;
import com.braintreegateway.enums.Purchase;
import com.braintreegateway.test.Nonce;
import com.braintreegateway.testhelpers.TestHelper;
import com.braintreegateway.testhelpers.ThreeDSecureRequestForTests;
import com.braintreegateway.SandboxValues.CreditCardNumber;
import com.braintreegateway.SandboxValues.FailsVerification;
import com.braintreegateway.util.NodeWrapper;
import com.braintreegateway.util.NodeWrapperFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CreditCardVerificationIT extends IntegrationTest {

    @Test
    public void createVerification() {
        CreditCardVerificationRequest request = new CreditCardVerificationRequest().
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                cvv("123").
                billingAddress().
                    company("Braintree").
                    countryCodeAlpha2("US").
                    countryCodeAlpha3("USA").
                    countryCodeNumeric("840").
                    countryName("United States of America").
                    extendedAddress("Unit B").
                    firstName("John").
                    lastName("Smith").
                    locality("San Francisco").
                    postalCode("60606").
                    region("CA").
                    streetAddress("123 Townsend St").
                    done().
                done().
            options().
                amount("5.00").
                done();

        Result<CreditCardVerification> result = gateway.creditCardVerification().create(request);
        assertTrue(result.isSuccess());
        CreditCardVerification verification = result.getTarget();
        assertEquals(verification.getBillingAddress().getPostalCode(), "60606");
        assertEquals("1000", verification.getProcessorResponseCode());
        assertEquals("Approved", verification.getProcessorResponseText());
        assertEquals(ProcessorResponseType.APPROVED, verification.getProcessorResponseType());
        assertNotNull(verification.getGraphQLId());
    }

    @Test
    public void createVerificationforAni() {
        CreditCardVerificationRequest request = new CreditCardVerificationRequest().
            creditCard().
            number("4012000033330026").
            expirationDate("05/2029").
            cvv("123").
            done();

        Result<CreditCardVerification> result = gateway.creditCardVerification().create(request);
        assertTrue(result.isSuccess());
        CreditCardVerification verification = result.getTarget();
        assertEquals("1000", verification.getProcessorResponseCode());
        assertEquals("Approved", verification.getProcessorResponseText());
        assertEquals("I", verification.getAniFirstNameResponseCode());
        assertEquals("I", verification.getAniLastNameResponseCode());
        assertEquals(ProcessorResponseType.APPROVED, verification.getProcessorResponseType());
        assertNotNull(verification.getGraphQLId());
    }

    @Test
    public void createVerificationforAniWhenAccountInformationInquiryIsSentInOptions() {
        CreditCardVerificationRequest request = new CreditCardVerificationRequest().
            creditCard().
            number("4012000033330026").
            expirationDate("05/2029").
            cvv("123").
            done().
            options().
              accountInformationInquiry("send_data").
            done();

        Result<CreditCardVerification> result = gateway.creditCardVerification().create(request);
        assertTrue(result.isSuccess());
        CreditCardVerification verification = result.getTarget();
        assertEquals("1000", verification.getProcessorResponseCode());
        assertEquals("Approved", verification.getProcessorResponseText());
        assertEquals("I", verification.getAniFirstNameResponseCode());
        assertEquals("I", verification.getAniLastNameResponseCode());
        assertEquals(ProcessorResponseType.APPROVED, verification.getProcessorResponseType());
        assertNotNull(verification.getGraphQLId());
    }

    @Test
    public void createVerificationNetworkResponseCodeText() {
        CreditCardVerificationRequest request = new CreditCardVerificationRequest().
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                cvv("123").
                billingAddress().
                    company("Braintree").
                    countryCodeAlpha2("US").
                    countryCodeAlpha3("USA").
                    countryCodeNumeric("840").
                    countryName("United States of America").
                    extendedAddress("Unit B").
                    firstName("John").
                    lastName("Smith").
                    locality("San Francisco").
                    postalCode("60606").
                    region("CA").
                    streetAddress("123 Townsend St").
                    done().
                done().
            options().
                amount("5.00").
                done();

        Result<CreditCardVerification> result = gateway.creditCardVerification().create(request);
        assertTrue(result.isSuccess());
        CreditCardVerification verification = result.getTarget();
        assertEquals("1000", verification.getProcessorResponseCode());
        assertEquals("Approved", verification.getProcessorResponseText());
        assertEquals(ProcessorResponseType.APPROVED, verification.getProcessorResponseType());
        assertEquals("XX", verification.getNetworkResponseCode());
        assertEquals("sample network response text", verification.getNetworkResponseText());
    }

    @Test
    public void verificationReturnsBinExtended() {
        CreditCardVerificationRequest request = new CreditCardVerificationRequest().
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                cvv("123").
                billingAddress().
                    company("Braintree").
                    countryCodeAlpha2("US").
                    countryCodeAlpha3("USA").
                    countryCodeNumeric("840").
                    countryName("United States of America").
                    extendedAddress("Unit B").
                    firstName("John").
                    lastName("Smith").
                    locality("San Francisco").
                    postalCode("60606").
                    region("CA").
                    streetAddress("123 Townsend St").
                    done().
                done().
            options().
                amount("5.00").
                done();

        Result<CreditCardVerification> result = gateway.creditCardVerification().create(request);
        assertTrue(result.isSuccess());
        CreditCardVerification verification = result.getTarget();
        assertEquals(CreditCardNumber.VISA.number.substring(0, 8), verification.getCreditCard().getBinExtended());
    }

    @Test
    public void createVerificationDeclined() {
        CreditCardVerificationRequest request = new CreditCardVerificationRequest().
            creditCard().
                number(FailsVerification.MASTER_CARD.number).
                expirationDate("05/2009").
                cvv("123").
                billingAddress().
                    company("Braintree").
                    countryCodeAlpha2("US").
                    countryCodeAlpha3("USA").
                    countryCodeNumeric("840").
                    countryName("United States of America").
                    extendedAddress("Unit B").
                    firstName("John").
                    lastName("Smith").
                    locality("San Francisco").
                    postalCode("60606").
                    region("CA").
                    streetAddress("123 Townsend St").
                    done().
                done().
            options().
                amount("5.00").
                done();

        Result<CreditCardVerification> result = gateway.creditCardVerification().create(request);
        assertFalse(result.isSuccess());
        CreditCardVerification verification = result.getCreditCardVerification();
        assertEquals(verification.getBillingAddress().getPostalCode(), "60606");
        assertEquals("2000", verification.getProcessorResponseCode());
        assertEquals("Do Not Honor", verification.getProcessorResponseText());
        assertEquals(ProcessorResponseType.SOFT_DECLINED, verification.getProcessorResponseType());
    }

    @Test
    public void createVerificationFailsForInvalidOptions() {
        CreditCardVerificationRequest request = new CreditCardVerificationRequest().
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                cvv("123").
                billingAddress().
                    company("Braintree").
                    countryCodeAlpha2("US").
                    countryCodeAlpha3("USA").
                    countryCodeNumeric("840").
                    countryName("United States of America").
                    extendedAddress("Unit B").
                    firstName("John").
                    lastName("Smith").
                    locality("San Francisco").
                    postalCode("60606").
                    region("CA").
                    streetAddress("123 Townsend St").
                    done().
                done().
            options().
                amount("-5.00").
                done();

        Result<CreditCardVerification> result = gateway.creditCardVerification().create(request);
        assertFalse(result.isSuccess());
        assertEquals(ValidationErrorCode.VERIFICATION_OPTIONS_AMOUNT_CANNOT_BE_NEGATIVE,
                result.getErrors().
                    forObject("verification").
                    forObject("options").
                    onField("amount").
                    get(0).
                    getCode());
    }

    @Test
    public void createVerificationWithExternalVaultAndRiskData() {
        CreditCardVerificationRequest request = new CreditCardVerificationRequest().
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                cvv("123").
                billingAddress().
                    company("Braintree").
                    countryCodeAlpha2("US").
                    countryCodeAlpha3("USA").
                    countryCodeNumeric("840").
                    countryName("United States of America").
                    extendedAddress("Unit B").
                    firstName("John").
                    lastName("Smith").
                    locality("San Francisco").
                    postalCode("60606").
                    region("CA").
                    streetAddress("123 Townsend St").
                    done().
                done().
            options().
                amount("5.00").
                done().
            externalVault().
                previousNetworkTransactionId("previousTransactionId").
                willVault().
                done().
            riskData().
                customerBrowser("customer-browser-user-agent").
                customerIP("127.0.0.1").
                done();


        TestHelper.assertIncludes("previousTransactionId", request.toXML());
        TestHelper.assertIncludes("will_vault", request.toXML());
        TestHelper.assertIncludes("customer-browser-user-agent", request.toXML());
        TestHelper.assertIncludes("127.0.0.1", request.toXML());
    }

    @Test
    public void createVerificationWithThreeDSecureAuthenticationId() {

        String threeDSecureAuthenticationId = TestHelper.createTest3DS(gateway, "three_d_secure_merchant_account", new ThreeDSecureRequestForTests().
            number(CreditCardNumber.VISA.number).
            expirationMonth("05").
            expirationYear("2029")
        );
        CreditCardVerificationRequest request = new CreditCardVerificationRequest().
            threeDSecureAuthenticationID(threeDSecureAuthenticationId).
            options().
                merchantAccountId("three_d_secure_merchant_account").
                done().
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2029").
                done();

        Result<CreditCardVerification> result = gateway.creditCardVerification().create(request);
        assertTrue(result.isSuccess());
    }

    // NEXT_MAJOR_VERSION remove this test
    // threeDSecureToken has been deprecated in favor of threeDSecureAuthenticationID
    @Test
    public void createVerificationWithThreeDSecureToken() {

        String threeDSecureToken = TestHelper.createTest3DS(gateway, "three_d_secure_merchant_account", new ThreeDSecureRequestForTests().
            number(CreditCardNumber.VISA.number).
            expirationMonth("05").
            expirationYear("2029")
        );
        CreditCardVerificationRequest request = new CreditCardVerificationRequest().
            options().
                merchantAccountId("three_d_secure_merchant_account").
                done().
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2029").
                done().
            threeDSecureToken(threeDSecureToken);

        Result<CreditCardVerification> result = gateway.creditCardVerification().create(request);
        assertTrue(result.isSuccess());
    }

    @Test
    public void createVerificationWithThreeDSecurePassThruRequest() {
        CreditCardVerificationRequest request = new CreditCardVerificationRequest().
            options().
                merchantAccountId("three_d_secure_merchant_account").
                done().
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2029").
                done().
            verificationThreeDSecurePassThruRequest().
                eciFlag("05").
                cavv("some-cavv").
                xid("some-xid").
                threeDSecureVersion("2.2.0").
                dsTransactionId("some-ds-transaction-id").
                authenticationResponse("some-auth-response").
                directoryResponse("some-directory-response").
                cavvAlgorithm("algorithm").
                done();

        Result<CreditCardVerification> result = gateway.creditCardVerification().create(request);
        assertTrue(result.isSuccess());
    }

    @Test
    public void createVerificationWithPaymentMethodNonce() {
        CreditCardVerificationRequest request = new CreditCardVerificationRequest().
            paymentMethodNonce(TestHelper.generateUnlockedNonce(gateway));

        Result<CreditCardVerification> result = gateway.creditCardVerification().create(request);
        assertTrue(result.isSuccess());
    }

    @Test
    public void createVerificationWithThreeDSecureWith3DSNonce() {
        CreditCardVerificationRequest request = new CreditCardVerificationRequest().
            paymentMethodNonce(Nonce.ThreeDSecureVisaFullAuthentication);

        Result<CreditCardVerification> result = gateway.creditCardVerification().create(request);
        assertTrue(result.isSuccess());
    }

    @Test
    public void createVerificationWithIntendedTransactionSource() {
        CreditCardVerificationRequest request = new CreditCardVerificationRequest().
            paymentMethodNonce(Nonce.ThreeDSecureVisaFullAuthentication).
            intendedTransactionSource("installment");

        Result<CreditCardVerification> result = gateway.creditCardVerification().create(request);
        assertTrue(result.isSuccess());
    }


    @Test
    public void constructFromResponse() {
        StringBuilder builder = new StringBuilder();
        builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        builder.append("<api-error-response>");
        builder.append("  <verification>");
        builder.append("    <amount>1.00</amount>");
        builder.append("    <currency-iso-code>USD</currency-iso-code>");
        builder.append("    <avs-error-response-code nil=\"true\"></avs-error-response-code>");
        builder.append("    <avs-postal-code-response-code>I</avs-postal-code-response-code>");
        builder.append("    <status>processor_declined</status>");
        builder.append("    <processor-response-code>2000</processor-response-code>");
        builder.append("    <avs-street-address-response-code>I</avs-street-address-response-code>");
        builder.append("    <processor-response-text>Do Not Honor</processor-response-text>");
        builder.append("    <cvv-response-code>M</cvv-response-code>");
        builder.append("    <id>verification_id</id>");
        builder.append("    <credit-card>");
        builder.append("      <cardholder-name>Joe Johnson</cardholder-name>");
        builder.append("      <number>4111111111111111</number>");
        builder.append("      <expiration-date>12/2012</expiration-date>");
        builder.append("      <prepaid>Unknown</prepaid>");
        builder.append("      <prepaid-reloadable>Unknown</prepaid-reloadable>");
        builder.append("      <business>Unknown</business>");
        builder.append("      <consumer>Unknown</consumer>");
        builder.append("      <corporate>Unknown</corporate>");
        builder.append("      <purchase>Unknown</purchase>");
        builder.append("    </credit-card>");
        builder.append("    <billing>");
        builder.append("      <postal-code>60601</postal-code>");
        builder.append("    </billing>");
        builder.append("  </verification>");
        builder.append("  <errors>");
        builder.append("    <errors type=\"array\"/>");
        builder.append("  </errors>");
        builder.append("</api-error-response>");

        NodeWrapper verificationNode = (NodeWrapperFactory.instance.create(builder.toString())).findFirst("verification");
        CreditCardVerification verification = new CreditCardVerification(verificationNode);
        assertEquals(new BigDecimal("1.00"), verification.getAmount());
        assertEquals("USD", verification.getCurrencyIsoCode());
        assertEquals(null, verification.getAvsErrorResponseCode());
        assertEquals("I", verification.getAvsPostalCodeResponseCode());
        assertEquals(CreditCardVerification.Status.PROCESSOR_DECLINED, verification.getStatus());
        assertEquals("2000", verification.getProcessorResponseCode());
        assertEquals("I", verification.getAvsStreetAddressResponseCode());
        assertEquals("Do Not Honor", verification.getProcessorResponseText());
        assertEquals("M", verification.getCvvResponseCode());
        assertEquals(CreditCard.Prepaid.UNKNOWN, verification.getCreditCard().getPrepaid());
        assertEquals(PrepaidReloadable.UNKNOWN, verification.getCreditCard().getPrepaidReloadable());
        assertEquals(Business.UNKNOWN, verification.getCreditCard().getBusiness());
        assertEquals(Consumer.UNKNOWN, verification.getCreditCard().getConsumer());
        assertEquals(Corporate.UNKNOWN, verification.getCreditCard().getCorporate());
        assertEquals(Purchase.UNKNOWN, verification.getCreditCard().getPurchase());

    }

    @Test
    public void searchOnVerificationId() {
        CustomerRequest request = new CustomerRequest().
            creditCard().
                number("4000111111111115").
                expirationDate("11/12").
                options().
                    verifyCard(true).
                    done().
                done();

        Result<Customer> result = gateway.customer().create(request);
        assertFalse(result.isSuccess());
        CreditCardVerification verification = result.getCreditCardVerification();

        CreditCardVerificationSearchRequest searchRequest = new CreditCardVerificationSearchRequest().
            id().is(verification.getId());

        ResourceCollection<CreditCardVerification> collection = gateway.creditCardVerification().search(searchRequest);

        assertEquals(1, collection.getMaximumSize());
        assertEquals(verification.getId(), collection.getFirst().getId());
    }

    @Test
    public void searchOnPaymentMethodToken() {
        String paymentMethodToken  = new Random().nextInt() + "token";
        CustomerRequest request = new CustomerRequest().
            email("mark.a@example.com").
            creditCard().
                token(paymentMethodToken).
                number("4111111111111111").
                expirationDate("11/12").
                cardholderName("Tom Smith").
                billingAddress().
                    postalCode("44444").
                    done().
                options().
                    verifyCard(true).
                    done().
                done();

        Result<Customer> result = gateway.customer().create(request);
        assertTrue(result.isSuccess());

        CreditCardVerificationSearchRequest searchRequest = new CreditCardVerificationSearchRequest().
            paymentMethodToken().is(paymentMethodToken);

        ResourceCollection<CreditCardVerification> collection = gateway.creditCardVerification().search(searchRequest);

        assertEquals(collection.getMaximumSize(), 1);
        assertEquals(paymentMethodToken, collection.getFirst().getCreditCard().getToken());
    }

    @Test
    public void searchOnAllTextFields() {
        CustomerRequest request = new CustomerRequest().
            email("mark.a@example.com").
            creditCard().
                number("4111111111111111").
                expirationDate("11/12").
                cardholderName("Tom Smith").
                billingAddress().
                    postalCode("44444").
                    done().
                options().
                    verifyCard(true).
                    done().
                done();

        Result<Customer> result = gateway.customer().create(request);
        assertTrue(result.isSuccess());
        CreditCardVerification verification = result.getCreditCardVerification();

        CreditCardVerificationSearchRequest searchRequest = new CreditCardVerificationSearchRequest().
            customerId().is(result.getTarget().getId()).
            creditCardCardholderName().is("Tom Smith").
            creditCardExpirationDate().is("11/2012").
            creditCardNumber().is("4111111111111111").
            billingPostalCode().is("44444").
            customerEmail().is("mark.a@example.com");

        ResourceCollection<CreditCardVerification> collection = gateway.creditCardVerification().search(searchRequest);

        assertEquals(result.getTarget().getCreditCards().get(0).getToken(), collection.getFirst().getCreditCard().getToken());
    }

    @Test
    public void searchOnMultipleValueFields() {
        CustomerRequest requestOne = new CustomerRequest().
            creditCard().
                number("4000111111111115").
                expirationDate("11/12").
                options().
                    verifyCard(true).
                    done().
                done();

        Result<Customer> resultOne = gateway.customer().create(requestOne);
        assertFalse(resultOne.isSuccess());
        CreditCardVerification verificationOne = resultOne.getCreditCardVerification();

        CustomerRequest requestTwo = new CustomerRequest().
            creditCard().
                number("5105105105105100").
                expirationDate("06/12").
                options().
                    verifyCard(true).
                    done().
                done();

        Result<Customer> resultTwo = gateway.customer().create(requestTwo);
        assertFalse(resultTwo.isSuccess());
        CreditCardVerification verificationTwo = resultTwo.getCreditCardVerification();

        CreditCardVerificationSearchRequest searchRequest = new CreditCardVerificationSearchRequest().
            ids().in(verificationOne.getId(), verificationTwo.getId()).
            creditCardCardType().in(CreditCard.CardType.VISA, CreditCard.CardType.MASTER_CARD).
            status().in(verificationOne.getStatus(), verificationTwo.getStatus());

        ResourceCollection<CreditCardVerification> collection = gateway.creditCardVerification().search(searchRequest);

        assertEquals(2, collection.getMaximumSize());
        List<String> expectedIds = new ArrayList<String>(Arrays.asList(verificationOne.getId(), verificationTwo.getId()));
        assertTrue(TestHelper.listIncludes(expectedIds, collection.getFirst().getId()));
    }

    @Test
    public void searchOnRangeFields() {
        CustomerRequest request = new CustomerRequest().
            creditCard().
                number("4000111111111115").
                expirationDate("11/12").
                cardholderName("Tom Smith").
                options().
                    verifyCard(true).
                    done().
                done();

        Result<Customer> result = gateway.customer().create(request);
        assertFalse(result.isSuccess());
        CreditCardVerification verification = result.getCreditCardVerification();

        Calendar createdAt = verification.getCreatedAt();

        Calendar threeDaysEarlier = ((Calendar) createdAt.clone());
        threeDaysEarlier.add(Calendar.DAY_OF_MONTH, -3);

        Calendar oneDayEarlier = ((Calendar) createdAt.clone());
        oneDayEarlier.add(Calendar.DAY_OF_MONTH, -1);

        Calendar oneDayLater = ((Calendar) createdAt.clone());
        oneDayLater.add(Calendar.DAY_OF_MONTH, 1);

        CreditCardVerificationSearchRequest searchRequest = new CreditCardVerificationSearchRequest().
           id().is(verification.getId()).
           createdAt().between(oneDayEarlier, oneDayLater);

        assertEquals(1, gateway.creditCardVerification().search(searchRequest).getMaximumSize());

        searchRequest = new CreditCardVerificationSearchRequest().
           id().is(verification.getId()).
           createdAt().greaterThanOrEqualTo(oneDayEarlier);

        assertEquals(1, gateway.creditCardVerification().search(searchRequest).getMaximumSize());

        searchRequest = new CreditCardVerificationSearchRequest().
           id().is(verification.getId()).
           createdAt().lessThanOrEqualTo(oneDayLater);

        assertEquals(1, gateway.creditCardVerification().search(searchRequest).getMaximumSize());

        searchRequest = new CreditCardVerificationSearchRequest().
           id().is(verification.getId()).
           createdAt().between(threeDaysEarlier, oneDayEarlier);

        assertEquals(0, gateway.creditCardVerification().search(searchRequest).getMaximumSize());
    }

    @Test
    public void verificationHasCardTypeIndicators() {
        CustomerRequest request = new CustomerRequest().
            creditCard().
                number("4000111111111115").
                expirationDate("11/12").
                cardholderName("Tom Smith").
                options().
                    verifyCard(true).
                    done().
                done();

        Result<Customer> result = gateway.customer().create(request);
        CreditCardVerification verification = result.getCreditCardVerification();

        assertEquals(CreditCard.Commercial.UNKNOWN, verification.getCreditCard().getCommercial());
        assertEquals(CreditCard.Debit.UNKNOWN, verification.getCreditCard().getDebit());
        assertEquals(CreditCard.DurbinRegulated.UNKNOWN, verification.getCreditCard().getDurbinRegulated());
        assertEquals(CreditCard.Healthcare.UNKNOWN, verification.getCreditCard().getHealthcare());
        assertEquals(CreditCard.Payroll.UNKNOWN, verification.getCreditCard().getPayroll());
        assertEquals(CreditCard.Prepaid.UNKNOWN, verification.getCreditCard().getPrepaid());
        assertEquals(PrepaidReloadable.UNKNOWN, verification.getCreditCard().getPrepaidReloadable());
        assertEquals("Unknown", verification.getCreditCard().getCountryOfIssuance());
        assertEquals("Unknown", verification.getCreditCard().getIssuingBank());
        assertEquals("Unknown", verification.getCreditCard().getProductId());
    }

    @Test
    public void createVerificationWithAccountTypeCredit() {
        CreditCardVerificationRequest request = new CreditCardVerificationRequest().
            creditCard().
                number(CreditCardNumber.HIPER.number).
                expirationDate("05/2009").
                cvv("123").
                done().
            options().
                amount("5.00").
                merchantAccountId("hiper_brl").
                accountType("credit").
                done();

        Result<CreditCardVerification> result = gateway.creditCardVerification().create(request);
        assertTrue(result.isSuccess());
        CreditCardVerification verification = result.getTarget();
        assertEquals("1000", verification.getProcessorResponseCode());
        assertEquals("Approved", verification.getProcessorResponseText());
        assertEquals("credit", verification.getCreditCard().getAccountType());
    }

    @Test
    public void createVerificationWithAccountTypeDebit() {
        CreditCardVerificationRequest request = new CreditCardVerificationRequest().
            creditCard().
                number(CreditCardNumber.HIPER.number).
                expirationDate("05/2009").
                cvv("123").
                done().
            options().
                amount("5.00").
                merchantAccountId("card_processor_brl").
                accountType("debit").
                done();

        Result<CreditCardVerification> result = gateway.creditCardVerification().create(request);
        assertTrue(result.isSuccess());
        CreditCardVerification verification = result.getTarget();
        assertEquals("1000", verification.getProcessorResponseCode());
        assertEquals("Approved", verification.getProcessorResponseText());
        assertEquals("debit", verification.getCreditCard().getAccountType());
    }

    @Test
    public void createVerificationWithErrorAccountTypeIsInvalid() {
        CreditCardVerificationRequest request = new CreditCardVerificationRequest().
            creditCard().
                number(CreditCardNumber.HIPER.number).
                expirationDate("05/2009").
                cvv("123").
                done().
            options().
                amount("5.00").
                merchantAccountId("hiper_brl").
                accountType("ach").
                done();

        Result<CreditCardVerification> result = gateway.creditCardVerification().create(request);
        assertFalse(result.isSuccess());
        assertEquals(ValidationErrorCode.VERIFICATION_OPTIONS_ACCOUNT_TYPE_IS_INVALID,
                result.getErrors().
                    forObject("verification").
                    forObject("options").
                    onField("account-type").
                    get(0).
                    getCode());
    }

    @Test
    public void createVerificationWithErrorAccountTypeNotSupported() {
        CreditCardVerificationRequest request = new CreditCardVerificationRequest().
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                cvv("123").
                done().
            options().
                amount("5.00").
                accountType("credit").
                done();

        Result<CreditCardVerification> result = gateway.creditCardVerification().create(request);
        assertFalse(result.isSuccess());
        assertEquals(ValidationErrorCode.VERIFICATION_OPTIONS_ACCOUNT_TYPE_NOT_SUPPORTED,
                result.getErrors().
                    forObject("verification").
                    forObject("options").
                    onField("account-type").
                    get(0).
                    getCode());
    }

    @Test
    public void createVerificationWithVisaReturnsNetworkTransactionId() {
        CreditCardVerificationRequest request = new CreditCardVerificationRequest().
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                cvv("123").
                done().
            options().
                amount("5.00").
                done();

        Result<CreditCardVerification> result = gateway.creditCardVerification().create(request);
        assertTrue(result.isSuccess());
        CreditCardVerification verification = result.getTarget();
        assertNotNull(verification.getNetworkTransactionId());
    }

    @Test
    public void createVerificationWithMasterCardReturnsNetworkTransactionId() {
        CreditCardVerificationRequest request = new CreditCardVerificationRequest().
            creditCard().
                number(CreditCardNumber.MASTER_CARD.number).
                expirationDate("05/2009").
                cvv("123").
                done().
            options().
                amount("5.00").
                done();

        Result<CreditCardVerification> result = gateway.creditCardVerification().create(request);
        assertTrue(result.isSuccess());
        CreditCardVerification verification = result.getTarget();
        assertNotNull(verification.getNetworkTransactionId());
    }
}
