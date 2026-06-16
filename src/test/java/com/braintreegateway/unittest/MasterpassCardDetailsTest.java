package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.braintreegateway.CreditCard;
import com.braintreegateway.MasterpassCardDetails;
import com.braintreegateway.util.SimpleNodeWrapper;

@SuppressWarnings("deprecation")
public class MasterpassCardDetailsTest {

    private SimpleNodeWrapper node(String indicator, String productId, String countryOfIssuance, String issuingBank) {
        return SimpleNodeWrapper.parse(
                "<masterpass-card>" +
                "<bin>540510</bin>" +
                "<card-type>MasterCard</card-type>" +
                "<cardholder-name>Joe Cardholder</cardholder-name>" +
                "<expiration-month>05</expiration-month>" +
                "<expiration-year>2025</expiration-year>" +
                "<image-url>https://example.com/card.png</image-url>" +
                "<issuer-location>USA</issuer-location>" +
                "<last-4>1881</last-4>" +
                "<token>a_token</token>" +
                "<commercial>" + indicator + "</commercial>" +
                "<debit>" + indicator + "</debit>" +
                "<durbin-regulated>" + indicator + "</durbin-regulated>" +
                "<healthcare>" + indicator + "</healthcare>" +
                "<payroll>" + indicator + "</payroll>" +
                "<prepaid>" + indicator + "</prepaid>" +
                "<product-id>" + productId + "</product-id>" +
                "<country-of-issuance>" + countryOfIssuance + "</country-of-issuance>" +
                "<issuing-bank>" + issuingBank + "</issuing-bank>" +
                "</masterpass-card>");
    }

    @Test
    public void parsesFieldsAndYesIndicators() {
        MasterpassCardDetails details = new MasterpassCardDetails(node("Yes", "a-product-id", "USA", "Some Bank"));

        assertAll("masterpass card fields",
                () -> assertEquals("540510", details.getBin()),
                () -> assertEquals("MasterCard", details.getCardType()),
                () -> assertEquals("Joe Cardholder", details.getCardholderName()),
                () -> assertEquals("05", details.getExpirationMonth()),
                () -> assertEquals("2025", details.getExpirationYear()),
                () -> assertEquals("05/2025", details.getExpirationDate()),
                () -> assertEquals("https://example.com/card.png", details.getImageUrl()),
                () -> assertEquals("1881", details.getLast4()),
                () -> assertEquals("540510******1881", details.getMaskedNumber()),
                () -> assertEquals("a_token", details.getToken()),
                () -> assertEquals(CreditCard.Commercial.YES, details.getCommercial()),
                () -> assertEquals(CreditCard.Debit.YES, details.getDebit()),
                () -> assertEquals(CreditCard.DurbinRegulated.YES, details.getDurbinRegulated()),
                () -> assertEquals(CreditCard.Healthcare.YES, details.getHealthcare()),
                () -> assertEquals(CreditCard.Payroll.YES, details.getPayroll()),
                () -> assertEquals(CreditCard.Prepaid.YES, details.getPrepaid()),
                () -> assertEquals("a-product-id", details.getProductId()),
                () -> assertEquals("USA", details.getCountryOfIssuance()),
                () -> assertEquals("Some Bank", details.getIssuingBank()));
    }

    @Test
    public void parsesNoIndicatorsAndUnknownForEmptyOptionalFields() {
        MasterpassCardDetails details = new MasterpassCardDetails(node("No", "", "", ""));

        assertAll("masterpass no/unknown indicators",
                () -> assertEquals(CreditCard.Commercial.NO, details.getCommercial()),
                () -> assertEquals(CreditCard.Debit.NO, details.getDebit()),
                () -> assertEquals(CreditCard.DurbinRegulated.NO, details.getDurbinRegulated()),
                () -> assertEquals(CreditCard.Healthcare.NO, details.getHealthcare()),
                () -> assertEquals(CreditCard.Payroll.NO, details.getPayroll()),
                () -> assertEquals(CreditCard.Prepaid.NO, details.getPrepaid()),
                () -> assertEquals("Unknown", details.getProductId()),
                () -> assertEquals("Unknown", details.getCountryOfIssuance()),
                () -> assertEquals("Unknown", details.getIssuingBank()));
    }

    @Test
    public void returnsUnknownForUnrecognizedIndicators() {
        MasterpassCardDetails details = new MasterpassCardDetails(node("Maybe", "a-product-id", "USA", "Some Bank"));

        assertAll("masterpass unknown indicators",
                () -> assertEquals(CreditCard.Commercial.UNKNOWN, details.getCommercial()),
                () -> assertEquals(CreditCard.Debit.UNKNOWN, details.getDebit()),
                () -> assertEquals(CreditCard.DurbinRegulated.UNKNOWN, details.getDurbinRegulated()),
                () -> assertEquals(CreditCard.Healthcare.UNKNOWN, details.getHealthcare()),
                () -> assertEquals(CreditCard.Payroll.UNKNOWN, details.getPayroll()),
                () -> assertEquals(CreditCard.Prepaid.UNKNOWN, details.getPrepaid()));
    }
}
