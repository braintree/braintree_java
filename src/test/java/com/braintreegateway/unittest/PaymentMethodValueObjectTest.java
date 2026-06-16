package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.ParseException;

import org.junit.jupiter.api.Test;

import com.braintreegateway.AmexExpressCheckoutCard;
import com.braintreegateway.AndroidPayCard;
import com.braintreegateway.ApplePayCard;
import com.braintreegateway.CreditCard;
import com.braintreegateway.CreditCardVerification;
import com.braintreegateway.CustomActionsPaymentMethod;
import com.braintreegateway.MasterpassCard;
import com.braintreegateway.PayPalAccount;
import com.braintreegateway.SamsungPayCard;
import com.braintreegateway.UsBankAccount;
import com.braintreegateway.VisaCheckoutCard;
import com.braintreegateway.testhelpers.CalendarTestUtils;
import com.braintreegateway.util.SimpleNodeWrapper;

/**
 * Covers getter paths in payment method value objects that require
 * subscriptions, billing addresses, or optional fields.
 */
@SuppressWarnings("deprecation")
public class PaymentMethodValueObjectTest {

    // ── CreditCard ──────────────────────────────────────────────────────────

    @Test
    public void creditCardParsesSubscriptionsAndBillingAddress() {
        SimpleNodeWrapper node = SimpleNodeWrapper.parse(
                "<credit-card>" +
                "<token>cc-token</token>" +
                "<expiration-month>05</expiration-month>" +
                "<expiration-year>2025</expiration-year>" +
                "<billing-address><id>addr-1</id></billing-address>" +
                "<subscriptions type=\"array\">" +
                "<subscription><id>sub-1</id><status>Active</status>" +
                "<transactions type=\"array\"/><add-ons type=\"array\"/>" +
                "<discounts type=\"array\"/><status-history type=\"array\"/></subscription>" +
                "</subscriptions>" +
                "</credit-card>");

        CreditCard card = new CreditCard(node);

        assertAll("credit card optional fields",
                () -> assertNotNull(card.getBillingAddress()),
                () -> assertEquals(1, card.getSubscriptions().size()));
    }

    @Test
    public void creditCardCustomerLocationEnum() {
        assertEquals("international", CreditCard.CustomerLocation.INTERNATIONAL.toString());
        assertEquals("us", CreditCard.CustomerLocation.US.toString());
    }

    // ── ApplePayCard ────────────────────────────────────────────────────────

    @Test
    public void applePayCardParsesSubscriptionsAndBillingAddress() throws ParseException {
        SimpleNodeWrapper node = SimpleNodeWrapper.parse(
                "<apple-pay-card>" +
                "<token>ap-token</token>" +
                "<created-at type=\"datetime\">2018-10-11T21:28:37Z</created-at>" +
                "<updated-at type=\"datetime\">2018-10-12T21:28:37Z</updated-at>" +
                "<customer-id>cust-1</customer-id>" +
                "<default type=\"boolean\">true</default>" +
                "<billing-address><id>addr-1</id></billing-address>" +
                "<subscriptions type=\"array\">" +
                "<subscription><id>sub-1</id><status>Active</status>" +
                "<transactions type=\"array\"/><add-ons type=\"array\"/>" +
                "<discounts type=\"array\"/><status-history type=\"array\"/></subscription>" +
                "</subscriptions>" +
                "</apple-pay-card>");

        ApplePayCard card = new ApplePayCard(node);

        assertAll("apple pay card optional fields",
                () -> assertNotNull(card.getBillingAddress()),
                () -> assertEquals(1, card.getSubscriptions().size()),
                () -> assertEquals("cust-1", card.getCustomerId()),
                () -> assertEquals(CalendarTestUtils.dateTime("2018-10-11T21:28:37Z"), card.getCreatedAt()),
                () -> assertEquals(CalendarTestUtils.dateTime("2018-10-12T21:28:37Z"), card.getUpdatedAt()),
                () -> assertTrue(card.isDefault()));
    }

    // ── AndroidPayCard ──────────────────────────────────────────────────────

    @Test
    public void androidPayCardParsesSubscriptionsAndBillingAddress() throws ParseException {
        SimpleNodeWrapper node = SimpleNodeWrapper.parse(
                "<android-pay-card>" +
                "<token>android-token</token>" +
                "<created-at type=\"datetime\">2018-10-11T21:28:37Z</created-at>" +
                "<updated-at type=\"datetime\">2018-10-12T21:28:37Z</updated-at>" +
                "<customer-id>cust-1</customer-id>" +
                "<default type=\"boolean\">false</default>" +
                "<billing-address><id>addr-1</id></billing-address>" +
                "<subscriptions type=\"array\">" +
                "<subscription><id>sub-1</id><status>Active</status>" +
                "<transactions type=\"array\"/><add-ons type=\"array\"/>" +
                "<discounts type=\"array\"/><status-history type=\"array\"/></subscription>" +
                "</subscriptions>" +
                "</android-pay-card>");

        AndroidPayCard card = new AndroidPayCard(node);

        assertAll("android pay card optional fields",
                () -> assertNotNull(card.getBillingAddress()),
                () -> assertEquals(1, card.getSubscriptions().size()),
                () -> assertEquals("cust-1", card.getCustomerId()),
                () -> assertNotNull(card.getCreatedAt()),
                () -> assertNotNull(card.getUpdatedAt()));
    }

    // ── SamsungPayCard ──────────────────────────────────────────────────────

    @Test
    public void samsungPayCardParsesSubscriptionsAndBillingAddress() throws ParseException {
        SimpleNodeWrapper node = SimpleNodeWrapper.parse(
                "<samsung-pay-card>" +
                "<token>samsung-token</token>" +
                "<bin>540510</bin><last-4>1881</last-4>" +
                "<commercial>Yes</commercial><debit>No</debit>" +
                "<durbin-regulated>No</durbin-regulated><healthcare>No</healthcare>" +
                "<payroll>No</payroll><prepaid>No</prepaid>" +
                "<product-id>a-product</product-id>" +
                "<country-of-issuance>US</country-of-issuance>" +
                "<issuing-bank>Bank</issuing-bank>" +
                "<created-at type=\"datetime\">2018-10-11T21:28:37Z</created-at>" +
                "<updated-at type=\"datetime\">2018-10-12T21:28:37Z</updated-at>" +
                "<customer-id>cust-1</customer-id>" +
                "<default type=\"boolean\">true</default>" +
                "<venmo-sdk type=\"boolean\">false</venmo-sdk>" +
                "<expired type=\"boolean\">false</expired>" +
                "<customer-location>US</customer-location>" +
                "<unique-number-identifier>unique-1</unique-number-identifier>" +
                "<billing-address><id>addr-1</id></billing-address>" +
                "<subscriptions type=\"array\">" +
                "<subscription><id>sub-1</id><status>Active</status>" +
                "<transactions type=\"array\"/><add-ons type=\"array\"/>" +
                "<discounts type=\"array\"/><status-history type=\"array\"/></subscription>" +
                "</subscriptions>" +
                "</samsung-pay-card>");

        SamsungPayCard card = new SamsungPayCard(node);

        assertAll("samsung pay card optional fields",
                () -> assertNotNull(card.getBillingAddress()),
                () -> assertEquals(1, card.getSubscriptions().size()),
                () -> assertNotNull(card.getCreatedAt()),
                () -> assertNotNull(card.getUpdatedAt()),
                () -> assertEquals("cust-1", card.getCustomerId()),
                () -> assertEquals("US", card.getCustomerLocation()),
                () -> assertEquals("540510******1881", card.getMaskedNumber()),
                () -> assertEquals("unique-1", card.getUniqueNumberIdentifier()));
    }

    // ── MasterpassCard ──────────────────────────────────────────────────────

    @Test
    public void masterpassCardParsesSubscriptionsAndBillingAddress() throws ParseException {
        SimpleNodeWrapper node = SimpleNodeWrapper.parse(
                "<masterpass-card>" +
                "<token>mp-token</token>" +
                "<bin>540510</bin><last-4>1881</last-4>" +
                "<commercial>Yes</commercial><debit>No</debit>" +
                "<durbin-regulated>No</durbin-regulated><healthcare>No</healthcare>" +
                "<payroll>No</payroll><prepaid>No</prepaid>" +
                "<product-id>a-product</product-id>" +
                "<country-of-issuance>US</country-of-issuance>" +
                "<issuing-bank>Bank</issuing-bank>" +
                "<created-at type=\"datetime\">2018-10-11T21:28:37Z</created-at>" +
                "<updated-at type=\"datetime\">2018-10-12T21:28:37Z</updated-at>" +
                "<customer-id>cust-1</customer-id>" +
                "<default type=\"boolean\">true</default>" +
                "<venmo-sdk type=\"boolean\">false</venmo-sdk>" +
                "<expired type=\"boolean\">false</expired>" +
                "<customer-location>US</customer-location>" +
                "<unique-number-identifier>unique-1</unique-number-identifier>" +
                "<billing-address><id>addr-1</id></billing-address>" +
                "<subscriptions type=\"array\">" +
                "<subscription><id>sub-1</id><status>Active</status>" +
                "<transactions type=\"array\"/><add-ons type=\"array\"/>" +
                "<discounts type=\"array\"/><status-history type=\"array\"/></subscription>" +
                "</subscriptions>" +
                "</masterpass-card>");

        MasterpassCard card = new MasterpassCard(node);

        assertAll("masterpass card optional fields",
                () -> assertNotNull(card.getBillingAddress()),
                () -> assertEquals(1, card.getSubscriptions().size()),
                () -> assertNotNull(card.getCreatedAt()),
                () -> assertNotNull(card.getUpdatedAt()),
                () -> assertEquals("cust-1", card.getCustomerId()),
                () -> assertEquals("540510******1881", card.getMaskedNumber()),
                () -> assertEquals("unique-1", card.getUniqueNumberIdentifier()));
    }

    // ── VisaCheckoutCard ────────────────────────────────────────────────────

    @Test
    public void visaCheckoutCardParsesSubscriptionsAndBillingAddress() throws ParseException {
        SimpleNodeWrapper node = SimpleNodeWrapper.parse(
                "<visa-checkout-card>" +
                "<token>visa-token</token>" +
                "<bin>411111</bin><last-4>1111</last-4>" +
                "<call-id>a-call-id</call-id>" +
                "<commercial>Yes</commercial><debit>No</debit>" +
                "<durbin-regulated>No</durbin-regulated><healthcare>No</healthcare>" +
                "<payroll>No</payroll><prepaid>No</prepaid>" +
                "<product-id>a-product</product-id>" +
                "<country-of-issuance>US</country-of-issuance>" +
                "<issuing-bank>Bank</issuing-bank>" +
                "<created-at type=\"datetime\">2018-10-11T21:28:37Z</created-at>" +
                "<updated-at type=\"datetime\">2018-10-12T21:28:37Z</updated-at>" +
                "<customer-id>cust-1</customer-id>" +
                "<customer-location>US</customer-location>" +
                "<default type=\"boolean\">false</default>" +
                "<venmo-sdk type=\"boolean\">false</venmo-sdk>" +
                "<expired type=\"boolean\">false</expired>" +
                "<unique-number-identifier>unique-1</unique-number-identifier>" +
                "<billing-address><id>addr-1</id></billing-address>" +
                "<subscriptions type=\"array\">" +
                "<subscription><id>sub-1</id><status>Active</status>" +
                "<transactions type=\"array\"/><add-ons type=\"array\"/>" +
                "<discounts type=\"array\"/><status-history type=\"array\"/></subscription>" +
                "</subscriptions>" +
                "<verifications type=\"array\"/>" +
                "</visa-checkout-card>");

        VisaCheckoutCard card = new VisaCheckoutCard(node);

        assertAll("visa checkout card optional fields",
                () -> assertNotNull(card.getBillingAddress()),
                () -> assertEquals(1, card.getSubscriptions().size()),
                () -> assertNotNull(card.getCreatedAt()),
                () -> assertNotNull(card.getUpdatedAt()),
                () -> assertEquals("a-call-id", card.getCallId()),
                () -> assertEquals("cust-1", card.getCustomerId()),
                () -> assertEquals("411111******1111", card.getMaskedNumber()),
                () -> assertEquals("unique-1", card.getUniqueNumberIdentifier()));
    }

    // ── UsBankAccount ───────────────────────────────────────────────────────

    @Test
    public void usBankAccountParsesSubscriptions() {
        SimpleNodeWrapper node = SimpleNodeWrapper.parse(
                "<us-bank-account>" +
                "<token>usbank-token</token>" +
                "<account-holder-name>Dan Schulman</account-holder-name>" +
                "<account-type>checking</account-type>" +
                "<subscriptions type=\"array\">" +
                "<subscription><id>sub-1</id><status>Active</status>" +
                "<transactions type=\"array\"/><add-ons type=\"array\"/>" +
                "<discounts type=\"array\"/><status-history type=\"array\"/></subscription>" +
                "</subscriptions>" +
                "</us-bank-account>");

        UsBankAccount account = new UsBankAccount(node);

        assertEquals(1, account.getSubscriptions().size());
    }

    // ── PayPalAccount ───────────────────────────────────────────────────────

    @Test
    public void payPalAccountParsesSubscriptions() {
        SimpleNodeWrapper node = SimpleNodeWrapper.parse(
                "<paypal-account>" +
                "<token>pp-token</token>" +
                "<email>payer@example.com</email>" +
                "<subscriptions type=\"array\">" +
                "<subscription><id>sub-1</id><status>Active</status>" +
                "<transactions type=\"array\"/><add-ons type=\"array\"/>" +
                "<discounts type=\"array\"/><status-history type=\"array\"/></subscription>" +
                "</subscriptions>" +
                "</paypal-account>");

        PayPalAccount account = new PayPalAccount(node);

        assertEquals(1, account.getSubscriptions().size());
    }

    // ── AmexExpressCheckoutCard ─────────────────────────────────────────────

    @Test
    public void amexExpressCheckoutCardParsesAllGetters() throws ParseException {
        SimpleNodeWrapper node = SimpleNodeWrapper.parse(
                "<amex-express-checkout-card>" +
                "<token>amex-token</token>" +
                "<bin>371260</bin>" +
                "<card-member-expiry-date>2025-12</card-member-expiry-date>" +
                "<card-member-number>1234567890</card-member-number>" +
                "<card-type>American Express</card-type>" +
                "<expiration-month>12</expiration-month>" +
                "<expiration-year>2025</expiration-year>" +
                "<image-url>https://example.com/amex.png</image-url>" +
                "<source-description>AmEx 1234</source-description>" +
                "<created-at type=\"datetime\">2018-10-11T21:28:37Z</created-at>" +
                "<updated-at type=\"datetime\">2018-10-12T21:28:37Z</updated-at>" +
                "<customer-id>cust-1</customer-id>" +
                "<default type=\"boolean\">true</default>" +
                "<subscriptions type=\"array\">" +
                "<subscription><id>sub-1</id><status>Active</status>" +
                "<transactions type=\"array\"/><add-ons type=\"array\"/>" +
                "<discounts type=\"array\"/><status-history type=\"array\"/></subscription>" +
                "</subscriptions>" +
                "</amex-express-checkout-card>");

        AmexExpressCheckoutCard card = new AmexExpressCheckoutCard(node);

        assertAll("amex express checkout card all getters",
                () -> assertEquals("amex-token", card.getToken()),
                () -> assertEquals("371260", card.getBin()),
                () -> assertEquals("2025-12", card.getCardMemberExpiryDate()),
                () -> assertEquals("1234567890", card.getCardMemberNumber()),
                () -> assertEquals("American Express", card.getCardType()),
                () -> assertEquals("12", card.getExpirationMonth()),
                () -> assertEquals("2025", card.getExpirationYear()),
                () -> assertEquals("https://example.com/amex.png", card.getImageUrl()),
                () -> assertEquals("AmEx 1234", card.getSourceDescription()),
                () -> assertEquals(CalendarTestUtils.dateTime("2018-10-11T21:28:37Z"), card.getCreatedAt()),
                () -> assertEquals(CalendarTestUtils.dateTime("2018-10-12T21:28:37Z"), card.getUpdatedAt()),
                () -> assertEquals("cust-1", card.getCustomerId()),
                () -> assertTrue(card.isDefault()),
                () -> assertEquals(1, card.getSubscriptions().size()));
    }

    // ── CreditCardVerification ──────────────────────────────────────────────

    @Test
    public void creditCardVerificationParsesRiskDataAndBillingAddress() {
        SimpleNodeWrapper node = SimpleNodeWrapper.parse(
                "<verification>" +
                "<id>verif-id</id>" +
                "<amount>10.00</amount>" +
                "<avs-error-response-code>M</avs-error-response-code>" +
                "<avs-postal-code-response-code>M</avs-postal-code-response-code>" +
                "<avs-street-address-response-code>M</avs-street-address-response-code>" +
                "<billing-address><id>addr-1</id></billing-address>" +
                "<created-at type=\"datetime\">2018-10-11T21:28:37Z</created-at>" +
                "<currency-iso-code>USD</currency-iso-code>" +
                "<cvv-response-code>M</cvv-response-code>" +
                "<global-id>global-id-1</global-id>" +
                "<gateway-rejection-reason>cvv</gateway-rejection-reason>" +
                "<merchant-account-id>a-merchant-account</merchant-account-id>" +
                "<processor-response-code>1000</processor-response-code>" +
                "<processor-response-text>Approved</processor-response-text>" +
                "<processor-response-type>approved</processor-response-type>" +
                "<status>verified</status>" +
                "<risk-data><id>risk-id</id><decision>approve</decision>" +
                "<fraud-service-provider>fraud_protection</fraud-service-provider></risk-data>" +
                "<billing><id>addr-1</id></billing>" +
                "<credit-card><token>cc-token</token></credit-card>" +
                "</verification>");

        CreditCardVerification verification = new CreditCardVerification(node);

        assertAll("credit card verification optional fields",
                () -> assertNotNull(verification.getAmount()),
                () -> assertEquals("M", verification.getAvsErrorResponseCode()),
                () -> assertEquals("M", verification.getAvsPostalCodeResponseCode()),
                () -> assertEquals("M", verification.getAvsStreetAddressResponseCode()),
                () -> assertNotNull(verification.getBillingAddress()),
                () -> assertNotNull(verification.getCreatedAt()),
                () -> assertEquals("USD", verification.getCurrencyIsoCode()),
                () -> assertEquals("M", verification.getCvvResponseCode()),
                () -> assertEquals("global-id-1", verification.getGraphQLId()),
                () -> assertNotNull(verification.getRiskData()),
                () -> assertNotNull(verification.getGatewayRejectionReason()),
                () -> assertEquals("1000", verification.getProcessorResponseCode()),
                () -> assertEquals("Approved", verification.getProcessorResponseText()),
                () -> assertNotNull(verification.getProcessorResponseType()),
                () -> assertEquals("a-merchant-account", verification.getMerchantAccountId()),
                () -> assertNotNull(verification.getStatus()));
    }

    // ── CustomActionsPaymentMethod ──────────────────────────────────────────

    @Test
    public void customActionsPaymentMethodParsesAllFields() throws ParseException {
        SimpleNodeWrapper node = SimpleNodeWrapper.parse(
                "<custom-actions-payment-method>" +
                "<token>ca-token</token>" +
                "<global-id>global-id-1</global-id>" +
                "<action-name>an-action</action-name>" +
                "<unique-number-identifier>unique-1</unique-number-identifier>" +
                "<image-url>https://example.com/icon.png</image-url>" +
                "<customer-id>cust-1</customer-id>" +
                "<default type=\"boolean\">true</default>" +
                "<created-at type=\"datetime\">2018-10-11T21:28:37Z</created-at>" +
                "<updated-at type=\"datetime\">2018-10-12T21:28:37Z</updated-at>" +
                "<fields type=\"array\">" +
                "<field><name>field-one</name><display-value>value-one</display-value></field>" +
                "</fields>" +
                "<subscriptions type=\"array\">" +
                "<subscription><id>sub-1</id><status>Active</status>" +
                "<transactions type=\"array\"/><add-ons type=\"array\"/>" +
                "<discounts type=\"array\"/><status-history type=\"array\"/></subscription>" +
                "</subscriptions>" +
                "</custom-actions-payment-method>");

        CustomActionsPaymentMethod pm = new CustomActionsPaymentMethod(node);

        assertAll("custom actions payment method all fields",
                () -> assertEquals("global-id-1", pm.getGlobalId()),
                () -> assertEquals("an-action", pm.getActionName()),
                () -> assertEquals("unique-1", pm.getUniqueNumberIdentifier()),
                () -> assertEquals("https://example.com/icon.png", pm.getImageUrl()),
                () -> assertEquals("cust-1", pm.getCustomerId()),
                () -> assertTrue(pm.isDefault()),
                () -> assertEquals(CalendarTestUtils.dateTime("2018-10-11T21:28:37Z"), pm.getCreatedAt()),
                () -> assertEquals(CalendarTestUtils.dateTime("2018-10-12T21:28:37Z"), pm.getUpdatedAt()),
                () -> assertEquals(1, pm.getFields().size()),
                () -> assertEquals(1, pm.getSubscriptions().size()));
    }
}
