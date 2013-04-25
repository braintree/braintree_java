package com.braintreegateway.integrationtest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.braintreegateway.*;
import com.braintreegateway.testhelpers.TestHelper;
import com.braintreegateway.util.NodeWrapper;
import com.braintreegateway.util.NodeWrapperFactory;

public class CreditCardVerificationIT {

    private BraintreeGateway gateway;

    @Before
    public void createGateway() {
        this.gateway = new BraintreeGateway(Environment.DEVELOPMENT, "integration_merchant_id", "integration_public_key", "integration_private_key");
    }

    @Test
    public void constructFromResponse() {
        StringBuilder builder = new StringBuilder();
        builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        builder.append("<api-error-response>");
        builder.append("  <verification>");
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
        Assert.assertEquals(null, verification.getAvsErrorResponseCode());
        Assert.assertEquals("I", verification.getAvsPostalCodeResponseCode());
        Assert.assertEquals(CreditCardVerification.Status.PROCESSOR_DECLINED, verification.getStatus());
        Assert.assertEquals("2000", verification.getProcessorResponseCode());
        Assert.assertEquals("I", verification.getAvsStreetAddressResponseCode());
        Assert.assertEquals("Do Not Honor", verification.getProcessorResponseText());
        Assert.assertEquals("M", verification.getCvvResponseCode());
        Assert.assertEquals(CreditCard.Prepaid.UNKNOWN, verification.getCreditCard().getPrepaid());
    }

    @Test
    public void searchOnAllTextFields()
    {
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
        Assert.assertFalse(result.isSuccess());
        CreditCardVerification verification = result.getCreditCardVerification();

        CreditCardVerificationSearchRequest searchRequest = new CreditCardVerificationSearchRequest().
            id().is(verification.getId()).
            creditCardCardholderName().is("Tom Smith").
            creditCardExpirationDate().is("11/2012").
            creditCardNumber().is("4000111111111115");

        ResourceCollection<CreditCardVerification> collection = gateway.creditCardVerification().search(searchRequest);

        Assert.assertEquals(1, collection.getMaximumSize());
        Assert.assertEquals(verification.getId(), collection.getFirst().getId());
    }

    @Test
    public void searchOnMultipleValueFields()
    {
        CustomerRequest requestOne = new CustomerRequest().
            creditCard().
                number("4000111111111115").
                expirationDate("11/12").
                options().
                    verifyCard(true).
                    done().
                done();

        Result<Customer> resultOne = gateway.customer().create(requestOne);
        Assert.assertFalse(resultOne.isSuccess());
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
        Assert.assertFalse(resultTwo.isSuccess());
        CreditCardVerification verificationTwo = resultTwo.getCreditCardVerification();

        CreditCardVerificationSearchRequest searchRequest = new CreditCardVerificationSearchRequest().
            ids().in(verificationOne.getId(), verificationTwo.getId()).
            creditCardCardType().in(CreditCard.CardType.VISA, CreditCard.CardType.MASTER_CARD);

        ResourceCollection<CreditCardVerification> collection = gateway.creditCardVerification().search(searchRequest);

        Assert.assertEquals(2, collection.getMaximumSize());
        List<String> expectedIds = new ArrayList<String>(Arrays.asList(verificationOne.getId(), verificationTwo.getId()));
        Assert.assertTrue(TestHelper.listIncludes(expectedIds, collection.getFirst().getId()));
    }

    @Test
    public void searchOnRangeFields()
    {
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
        Assert.assertFalse(result.isSuccess());
        CreditCardVerification verification = result.getCreditCardVerification();

        Calendar createdAt = verification.getCreatedAt();

        Calendar threeDaysEarlier = ((Calendar)createdAt.clone());
        threeDaysEarlier.add(Calendar.DAY_OF_MONTH, -3);

        Calendar oneDayEarlier = ((Calendar)createdAt.clone());
        oneDayEarlier.add(Calendar.DAY_OF_MONTH, -1);

        Calendar oneDayLater = ((Calendar)createdAt.clone());
        oneDayLater.add(Calendar.DAY_OF_MONTH, 1);

        CreditCardVerificationSearchRequest searchRequest = new CreditCardVerificationSearchRequest().
           id().is(verification.getId()).
           createdAt().between(oneDayEarlier, oneDayLater);

        Assert.assertEquals(1, gateway.creditCardVerification().search(searchRequest).getMaximumSize());

        searchRequest = new CreditCardVerificationSearchRequest().
           id().is(verification.getId()).
           createdAt().greaterThanOrEqualTo(oneDayEarlier);

        Assert.assertEquals(1, gateway.creditCardVerification().search(searchRequest).getMaximumSize());

        searchRequest = new CreditCardVerificationSearchRequest().
           id().is(verification.getId()).
           createdAt().lessThanOrEqualTo(oneDayLater);

        Assert.assertEquals(1, gateway.creditCardVerification().search(searchRequest).getMaximumSize());

        searchRequest = new CreditCardVerificationSearchRequest().
           id().is(verification.getId()).
           createdAt().between(threeDaysEarlier, oneDayEarlier);

        Assert.assertEquals(0, gateway.creditCardVerification().search(searchRequest).getMaximumSize());
     }

    @Test
    public void verificationHasCardTypeIndicators()
    {
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

        Assert.assertEquals(CreditCard.Commercial.UNKNOWN, verification.getCreditCard().getCommercial());
        Assert.assertEquals(CreditCard.Debit.UNKNOWN, verification.getCreditCard().getDebit());
        Assert.assertEquals(CreditCard.DurbinRegulated.UNKNOWN, verification.getCreditCard().getDurbinRegulated());
        Assert.assertEquals(CreditCard.Healthcare.UNKNOWN, verification.getCreditCard().getHealthcare());
        Assert.assertEquals(CreditCard.Payroll.UNKNOWN, verification.getCreditCard().getPayroll());
        Assert.assertEquals(CreditCard.Prepaid.UNKNOWN, verification.getCreditCard().getPrepaid());
        Assert.assertEquals("Unknown", verification.getCreditCard().getCountryOfIssuance());
        Assert.assertEquals("Unknown", verification.getCreditCard().getIssuingBank());
    }
}
