package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.braintreegateway.Customer;
import com.braintreegateway.util.SimpleNodeWrapper;

public class CustomerTest {

    @Test
    public void parsesScalarFieldsAndInternationalPhone() {
        SimpleNodeWrapper node = SimpleNodeWrapper.parse(
                "<customer>" +
                "<company>Acme Inc</company>" +
                "<email>dan@example.com</email>" +
                "<fax>3125559999</fax>" +
                "<first-name>Dan</first-name>" +
                "<global-id>global-id-1</global-id>" +
                "<id>customer-id-1</id>" +
                "<international-phone>" +
                "<country-code>1</country-code>" +
                "<national-number>3125551234</national-number>" +
                "</international-phone>" +
                "<last-name>Schulman</last-name>" +
                "<phone>3125551234</phone>" +
                "<website>https://example.com</website>" +
                "</customer>");

        Customer customer = new Customer(node);

        assertAll("customer scalar fields",
                () -> assertEquals("Acme Inc", customer.getCompany()),
                () -> assertEquals("dan@example.com", customer.getEmail()),
                () -> assertEquals("3125559999", customer.getFax()),
                () -> assertEquals("Dan", customer.getFirstName()),
                () -> assertEquals("global-id-1", customer.getGraphQLId()),
                () -> assertEquals("customer-id-1", customer.getId()),
                () -> assertEquals("Schulman", customer.getLastName()),
                () -> assertEquals("3125551234", customer.getPhone()),
                () -> assertEquals("https://example.com", customer.getWebsite()),
                () -> assertEquals("1", customer.getInternationalPhone().getCountryCode()),
                () -> assertEquals("3125551234", customer.getInternationalPhone().getNationalNumber()));
    }

    @Test
    public void parsesNestedPaymentMethodCollections() {
        SimpleNodeWrapper node = SimpleNodeWrapper.parse(
                "<customer><id>cust-1</id>" +
                "<credit-cards type=\"array\">" +
                "<credit-card><token>cc-token</token></credit-card>" +
                "</credit-cards>" +
                "<paypal-accounts type=\"array\">" +
                "<paypal-account><token>pp-token</token></paypal-account>" +
                "</paypal-accounts>" +
                "<apple-pay-cards type=\"array\">" +
                "<apple-pay-card><token>ap-token</token></apple-pay-card>" +
                "</apple-pay-cards>" +
                "<android-pay-cards type=\"array\">" +
                "<android-pay-card><token>android-token</token></android-pay-card>" +
                "</android-pay-cards>" +
                "<venmo-accounts type=\"array\">" +
                "<venmo-account><token>venmo-token</token></venmo-account>" +
                "</venmo-accounts>" +
                "<us-bank-accounts type=\"array\">" +
                "<us-bank-account><token>usbank-token</token></us-bank-account>" +
                "</us-bank-accounts>" +
                "<addresses type=\"array\">" +
                "<address><id>addr-1</id></address>" +
                "</addresses>" +
                "</customer>");

        Customer customer = new Customer(node);

        assertAll("nested collections",
                () -> assertEquals(1, customer.getCreditCards().size()),
                () -> assertEquals(1, customer.getPayPalAccounts().size()),
                () -> assertEquals(1, customer.getApplePayCards().size()),
                () -> assertEquals(1, customer.getAndroidPayCards().size()),
                () -> assertEquals(1, customer.getVenmoAccounts().size()),
                () -> assertEquals(1, customer.getUsBankAccounts().size()),
                () -> assertEquals(1, customer.getAddresses().size()));
    }

    @Test
    public void parsesDeprecatedAndAdditionalPaymentMethodCollections() {
        SimpleNodeWrapper node = SimpleNodeWrapper.parse(
                "<customer><id>cust-1</id>" +
                "<amex-express-checkout-cards type=\"array\">" +
                "<amex-express-checkout-card><token>amex-token</token>" +
                "<bin>371260</bin><expiration-month>12</expiration-month>" +
                "<expiration-year>2025</expiration-year></amex-express-checkout-card>" +
                "</amex-express-checkout-cards>" +
                "<visa-checkout-cards type=\"array\">" +
                "<visa-checkout-card><token>visa-token</token></visa-checkout-card>" +
                "</visa-checkout-cards>" +
                "<masterpass-cards type=\"array\">" +
                "<masterpass-card><token>mp-token</token>" +
                "<commercial>Yes</commercial><debit>No</debit>" +
                "<durbin-regulated>No</durbin-regulated><healthcare>No</healthcare>" +
                "<payroll>No</payroll><prepaid>No</prepaid>" +
                "<product-id>abc</product-id><country-of-issuance>US</country-of-issuance>" +
                "<issuing-bank>Bank</issuing-bank></masterpass-card>" +
                "</masterpass-cards>" +
                "<sepa-debit-accounts type=\"array\">" +
                "<sepa-debit-account><token>sepa-token</token></sepa-debit-account>" +
                "</sepa-debit-accounts>" +
                "<samsung-pay-cards type=\"array\">" +
                "<samsung-pay-card><token>samsung-token</token>" +
                "<commercial>Yes</commercial><debit>No</debit>" +
                "<durbin-regulated>No</durbin-regulated><healthcare>No</healthcare>" +
                "<payroll>No</payroll><prepaid>No</prepaid>" +
                "<product-id>abc</product-id><country-of-issuance>US</country-of-issuance>" +
                "<issuing-bank>Bank</issuing-bank></samsung-pay-card>" +
                "</samsung-pay-cards>" +
                "<custom-actions-payment-methods type=\"array\">" +
                "<custom-actions-payment-method><token>ca-token</token></custom-actions-payment-method>" +
                "</custom-actions-payment-methods>" +
                "<created-at type=\"datetime\">2018-10-11T21:28:37Z</created-at>" +
                "<updated-at type=\"datetime\">2018-10-12T21:28:37Z</updated-at>" +
                "</customer>");

        Customer customer = new Customer(node);

        assertAll("additional collections",
                () -> assertEquals(1, customer.getAmexExpressCheckoutCards().size()),
                () -> assertEquals(1, customer.getVisaCheckoutCards().size()),
                () -> assertEquals(1, customer.getMasterpassCards().size()),
                () -> assertEquals(1, customer.getSepaDirectDebitAccounts().size()),
                () -> assertEquals(1, customer.getSamsungPayCards().size()),
                () -> assertEquals(1, customer.getCustomActionsPaymentMethods().size()),
                () -> assertNotNull(customer.getCreatedAt()),
                () -> assertNotNull(customer.getUpdatedAt()));
    }

    @Test
    public void getPaymentMethodsAggregatesAllPaymentMethodTypes() {
        SimpleNodeWrapper node = SimpleNodeWrapper.parse(
                "<customer><id>cust-1</id>" +
                "<credit-cards type=\"array\">" +
                "<credit-card><token>cc-token</token></credit-card>" +
                "</credit-cards>" +
                "<paypal-accounts type=\"array\">" +
                "<paypal-account><token>pp-token</token></paypal-account>" +
                "</paypal-accounts>" +
                "</customer>");

        Customer customer = new Customer(node);
        assertEquals(2, customer.getPaymentMethods().size());
    }

    @Test
    public void getDefaultPaymentMethodReturnsDefaultCard() {
        SimpleNodeWrapper node = SimpleNodeWrapper.parse(
                "<customer><id>cust-1</id>" +
                "<credit-cards type=\"array\">" +
                "<credit-card><token>cc-default</token>" +
                "<default type=\"boolean\">true</default></credit-card>" +
                "</credit-cards>" +
                "</customer>");

        Customer customer = new Customer(node);

        assertNotNull(customer.getDefaultPaymentMethod());
        assertEquals("cc-default", customer.getDefaultPaymentMethod().getToken());
    }

    @Test
    public void getDefaultPaymentMethodReturnsNullWhenNoneIsDefault() {
        SimpleNodeWrapper node = SimpleNodeWrapper.parse(
                "<customer><id>cust-1</id>" +
                "<credit-cards type=\"array\">" +
                "<credit-card><token>cc-token</token>" +
                "<default type=\"boolean\">false</default></credit-card>" +
                "</credit-cards>" +
                "</customer>");

        assertNull(new Customer(node).getDefaultPaymentMethod());
    }

    @Test
    public void parsesEmptyCollectionsWhenAbsent() {
        SimpleNodeWrapper node = SimpleNodeWrapper.parse("<customer><id>cust-1</id></customer>");

        Customer customer = new Customer(node);

        assertAll("empty collections",
                () -> assertTrue(customer.getCreditCards().isEmpty()),
                () -> assertTrue(customer.getPayPalAccounts().isEmpty()),
                () -> assertTrue(customer.getAddresses().isEmpty()),
                () -> assertTrue(customer.getCustomFields().isEmpty()));
    }
}
