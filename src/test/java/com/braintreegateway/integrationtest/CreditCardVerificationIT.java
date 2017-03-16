package com.braintreegateway.integrationtest;

import com.braintreegateway.*;
import com.braintreegateway.testhelpers.TestHelper;
import com.braintreegateway.SandboxValues.CreditCardNumber;
import com.braintreegateway.util.NodeWrapper;
import com.braintreegateway.util.NodeWrapperFactory;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.*;

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
        assertEquals("Unknown", verification.getCreditCard().getCountryOfIssuance());
        assertEquals("Unknown", verification.getCreditCard().getIssuingBank());
        assertEquals("Unknown", verification.getCreditCard().getProductId());
    }
}
