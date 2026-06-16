package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.braintreegateway.CreditCard;
import com.braintreegateway.SamsungPayCardDetails;
import com.braintreegateway.util.SimpleNodeWrapper;

@SuppressWarnings("deprecation")
public class SamsungPayCardDetailsTest {

    private SimpleNodeWrapper node(String commercial, String productId, String countryOfIssuance, String issuingBank) {
        return SimpleNodeWrapper.parse(
                "<samsung-pay-card>" +
                "<bin>401288</bin>" +
                "<card-type>Visa</card-type>" +
                "<cardholder-name>Joe Cardholder</cardholder-name>" +
                "<expiration-month>05</expiration-month>" +
                "<expiration-year>2025</expiration-year>" +
                "<image-url>https://example.com/card.png</image-url>" +
                "<issuer-location>USA</issuer-location>" +
                "<last-4>1881</last-4>" +
                "<token>a_token</token>" +
                "<commercial>" + commercial + "</commercial>" +
                "<debit>" + commercial + "</debit>" +
                "<durbin-regulated>" + commercial + "</durbin-regulated>" +
                "<healthcare>" + commercial + "</healthcare>" +
                "<payroll>" + commercial + "</payroll>" +
                "<prepaid>" + commercial + "</prepaid>" +
                "<product-id>" + productId + "</product-id>" +
                "<country-of-issuance>" + countryOfIssuance + "</country-of-issuance>" +
                "<issuing-bank>" + issuingBank + "</issuing-bank>" +
                "</samsung-pay-card>");
    }

    @Test
    public void parsesFieldsAndYesIndicators() {
        SamsungPayCardDetails details = new SamsungPayCardDetails(node("Yes", "a-product-id", "USA", "Some Bank"));

        assertEquals("401288", details.getBin());
        assertEquals("Visa", details.getCardType());
        assertEquals("Joe Cardholder", details.getCardholderName());
        assertEquals("05", details.getExpirationMonth());
        assertEquals("2025", details.getExpirationYear());
        assertEquals("05/2025", details.getExpirationDate());
        assertEquals("https://example.com/card.png", details.getImageUrl());
        assertEquals("1881", details.getLast4());
        assertEquals("401288******1881", details.getMaskedNumber());
        assertEquals("a_token", details.getToken());

        assertEquals(CreditCard.Commercial.YES, details.getCommercial());
        assertEquals(CreditCard.Debit.YES, details.getDebit());
        assertEquals(CreditCard.DurbinRegulated.YES, details.getDurbinRegulated());
        assertEquals(CreditCard.Healthcare.YES, details.getHealthcare());
        assertEquals(CreditCard.Payroll.YES, details.getPayroll());
        assertEquals(CreditCard.Prepaid.YES, details.getPrepaid());

        assertEquals("a-product-id", details.getProductId());
        assertEquals("USA", details.getCountryOfIssuance());
        assertEquals("Some Bank", details.getIssuingBank());
    }

    @Test
    public void parsesNoIndicatorsAndUnknownForEmptyOptionalFields() {
        SamsungPayCardDetails details = new SamsungPayCardDetails(node("No", "", "", ""));

        assertEquals(CreditCard.Commercial.NO, details.getCommercial());
        assertEquals(CreditCard.Debit.NO, details.getDebit());
        assertEquals(CreditCard.DurbinRegulated.NO, details.getDurbinRegulated());
        assertEquals(CreditCard.Healthcare.NO, details.getHealthcare());
        assertEquals(CreditCard.Payroll.NO, details.getPayroll());
        assertEquals(CreditCard.Prepaid.NO, details.getPrepaid());

        assertEquals("Unknown", details.getProductId());
        assertEquals("Unknown", details.getCountryOfIssuance());
        assertEquals("Unknown", details.getIssuingBank());
    }

    @Test
    public void returnsUnknownForUnrecognizedIndicators() {
        SamsungPayCardDetails details = new SamsungPayCardDetails(node("Maybe", "a-product-id", "USA", "Some Bank"));

        assertEquals(CreditCard.Commercial.UNKNOWN, details.getCommercial());
        assertEquals(CreditCard.Debit.UNKNOWN, details.getDebit());
        assertEquals(CreditCard.DurbinRegulated.UNKNOWN, details.getDurbinRegulated());
        assertEquals(CreditCard.Healthcare.UNKNOWN, details.getHealthcare());
        assertEquals(CreditCard.Payroll.UNKNOWN, details.getPayroll());
        assertEquals(CreditCard.Prepaid.UNKNOWN, details.getPrepaid());
    }
}
