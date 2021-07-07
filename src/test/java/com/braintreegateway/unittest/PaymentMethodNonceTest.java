package com.braintreegateway.unittest;

import com.braintreegateway.BinData;
import com.braintreegateway.CreditCard;
import com.braintreegateway.PaymentMethodNonce;
import com.braintreegateway.util.NodeWrapper;
import com.braintreegateway.util.NodeWrapperFactory;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PaymentMethodNonceTest {

    @Test
    public void parsesNodeCorrectly() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<payment-method-nonce>" +
                "  <type>CreditCard</type>" +
                "  <nonce>fake-valid-nonce</nonce>" +
                "  <description>ending in 22</description>" +
                "  <consumed type=\"boolean\">false</consumed>" +
                "  <three-d-secure-info nil=\"true\"/>" +
                "  <details>" +
                "    <bin>422222</bin>" +
                "    <last-two>22</last-two>" +
                "    <last-four>2222</last-four>" +
                "    <card-type>Visa</card-type>" +
                "  </details>" +
                "  <bin-data>" +
                "    <healthcare>Yes</healthcare>" +
                "    <debit>No</debit>" +
                "    <durbin-regulated>Unknown</durbin-regulated>" +
                "    <commercial>Unknown</commercial>" +
                "    <payroll>Unknown</payroll>" +
                "    <issuing-bank>Unknown</issuing-bank>" +
                "    <country-of-issuance>Something</country-of-issuance>" +
                "    <prepaid>Yes</prepaid>" +
                "    <product-id>123</product-id>" +
                "  </bin-data>" +
                "</payment-method-nonce>";

        NodeWrapper nodeWrapper = NodeWrapperFactory.instance.create(xml);
        PaymentMethodNonce paymentMethodNonce = new PaymentMethodNonce(nodeWrapper);

        assertNotNull(paymentMethodNonce);
        assertEquals("CreditCard", paymentMethodNonce.getType());
        assertEquals("fake-valid-nonce", paymentMethodNonce.getNonce());
        assertEquals(false, paymentMethodNonce.isConsumed());
        assertNotNull(paymentMethodNonce.getBinData());
        BinData binData = paymentMethodNonce.getBinData();
        assertEquals(CreditCard.Healthcare.YES, binData.getHealthcare());
        assertEquals(CreditCard.Debit.NO, binData.getDebit());
        assertEquals(CreditCard.DurbinRegulated.UNKNOWN, binData.getDurbinRegulated());
        assertEquals(CreditCard.Commercial.UNKNOWN, binData.getCommercial());
        assertEquals(CreditCard.Payroll.UNKNOWN, binData.getPayroll());
        assertEquals("Unknown", binData.getIssuingBank());
        assertEquals("Something", binData.getCountryOfIssuance());
        assertEquals("123", binData.getProductId());
        assertEquals(CreditCard.Prepaid.YES, binData.getPrepaid());
        assertNotNull(paymentMethodNonce.getDetails());
        assertEquals("422222", paymentMethodNonce.getDetails().getBin());
        assertEquals("22", paymentMethodNonce.getDetails().getLastTwo());
        assertEquals("2222", paymentMethodNonce.getDetails().getLastFour());
        assertEquals("Visa", paymentMethodNonce.getDetails().getCardType());
    }

    @Test
    public void parsesNodeCorrectlyWithDetailsMissing() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<payment-method-nonce>" +
                "  <type>CreditCard</type>" +
                "  <nonce>fake-valid-nonce</nonce>" +
                "  <description>ending in 22</description>" +
                "  <consumed type=\"boolean\">false</consumed>" +
                "  <three-d-secure-info nil=\"true\"/>" +
                "  <authentication-insight nil=\"true\"/>" +
                "</payment-method-nonce>";

        NodeWrapper nodeWrapper = NodeWrapperFactory.instance.create(xml);
        PaymentMethodNonce paymentMethodNonce = new PaymentMethodNonce(nodeWrapper);

        assertNotNull(paymentMethodNonce);
        assertEquals("CreditCard", paymentMethodNonce.getType());
        assertEquals("fake-valid-nonce", paymentMethodNonce.getNonce());
        assertEquals(false, paymentMethodNonce.isConsumed());
        assertNull(paymentMethodNonce.getThreeDSecureInfo());
        assertNull(paymentMethodNonce.getDetails());
        assertNull(paymentMethodNonce.getBinData());
        assertNull(paymentMethodNonce.getAuthenticationInsight());
    }

    @Test
    public void ParsesNodeCorrectlyWithNilValues() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<payment-method-nonce>" +
                "  <type>CreditCard</type>" +
                "  <nonce>fake-valid-nonce</nonce>" +
                "  <description>ending in 22</description>" +
                "  <consumed type=\"boolean\">false</consumed>" +
                "  <three-d-secure-info nil=\"true\"/>" +
                "  <details nil=\"true\"/>" +
                "  <bin-data nil=\"true\"/>" +
                "  <authentication-insight nil=\"true\"/>" +
                "</payment-method-nonce>";

        NodeWrapper nodeWrapper = NodeWrapperFactory.instance.create(xml);
        PaymentMethodNonce paymentMethodNonce = new PaymentMethodNonce(nodeWrapper);

        assertNotNull(paymentMethodNonce);
        assertEquals("CreditCard", paymentMethodNonce.getType());
        assertEquals("fake-valid-nonce", paymentMethodNonce.getNonce());
        assertEquals(false, paymentMethodNonce.isConsumed());
        assertNull(paymentMethodNonce.getThreeDSecureInfo());
        assertNull(paymentMethodNonce.getDetails());
        assertNull(paymentMethodNonce.getBinData());
        assertNull(paymentMethodNonce.getAuthenticationInsight());
    }

    @Test
    public void parsesNodeCorrectlyWithVenmoNonce() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<payment-method-nonce>" +
                "  <type>VenmoAccount</type>" +
                "  <nonce>fake-venmo-account-nonce</nonce>" +
                "  <description></description>" +
                "  <consumed type=\"boolean\">false</consumed>" +
                "  <details>" +
                "    <last-two>22</last-two>" +
                "    <username>venmojoe</username>" +
                "    <venmo-user-id>Venmo-Joe-1</venmo-user-id>" +
                "  </details>" +
                "</payment-method-nonce>";

        NodeWrapper nodeWrapper = NodeWrapperFactory.instance.create(xml);
        PaymentMethodNonce paymentMethodNonce = new PaymentMethodNonce(nodeWrapper);

        assertNotNull(paymentMethodNonce);
        assertEquals("VenmoAccount", paymentMethodNonce.getType());
        assertEquals("fake-venmo-account-nonce", paymentMethodNonce.getNonce());
        assertEquals(false, paymentMethodNonce.isConsumed());
        assertNotNull(paymentMethodNonce.getDetails());
        assertEquals("22", paymentMethodNonce.getDetails().getLastTwo());
        assertEquals("venmojoe", paymentMethodNonce.getDetails().getUsername());
        assertEquals("Venmo-Joe-1", paymentMethodNonce.getDetails().getVenmoUserId());
    }

    @Test
    public void parsesNodeCorrectlyWithApplePayNonce() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<payment-method-nonce>" +
                "  <type>ApplePayCard</type>" +
                "  <nonce>fake-apple-pay-visa-nonce</nonce>" +
                "  <description></description>" +
                "  <consumed type=\"boolean\">false</consumed>" +
                "  <details>" +
                "    <card-type>Visa</card-type>" +
                "    <cardholder-name>Visa Apple Pay Cardholder</cardholder-name>" +
                "    <payment-instrument-name>Visa 8886</payment-instrument-name>" +
                "    <dpan-last-two>81</dpan-last-two>" +
                "  </details>" +
                "</payment-method-nonce>";

        NodeWrapper nodeWrapper = NodeWrapperFactory.instance.create(xml);
        PaymentMethodNonce paymentMethodNonce = new PaymentMethodNonce(nodeWrapper);

        assertNotNull(paymentMethodNonce);
        assertEquals("ApplePayCard", paymentMethodNonce.getType());
        assertEquals("fake-apple-pay-visa-nonce", paymentMethodNonce.getNonce());
        assertEquals(false, paymentMethodNonce.isConsumed());
        assertNotNull(paymentMethodNonce.getDetails());
        assertEquals("Visa", paymentMethodNonce.getDetails().getCardType());
        assertEquals("Visa Apple Pay Cardholder", paymentMethodNonce.getDetails().getCardholderName());
        assertEquals("Visa 8886", paymentMethodNonce.getDetails().getPaymentInstrumentName());
        assertEquals("81", paymentMethodNonce.getDetails().getDpanLastTwo());
    }

    @Test
    public void parsesNodeCorrectlyWithPayPalNonce() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<payment-method-nonce>" +
                "  <type>PayPalAccount</type>" +
                "  <nonce>fake-paypal-billing-agreement-nonce</nonce>" +
                "  <description></description>" +
                "  <consumed type=\"boolean\">false</consumed>" +
                "  <details>" +
                "    <email>jane.doe@paypal.com</email>" +
                "    <correlation-id>46676383-b632-4b80-8cfd-d7a35d960888</correlation-id>" +
                "    <payer-info>" +
                "      <email>jane2.doe@paypal.com</email>" +
                "      <first-name>first</first-name>" +
                "      <last-name>last</last-name>" +
                "      <payer-id>pay-123</payer-id>" +
                "      <country-code>US</country-code>" +
                "    </payer-info>" +
                "  </details>" +
                "</payment-method-nonce>";

        NodeWrapper nodeWrapper = NodeWrapperFactory.instance.create(xml);
        PaymentMethodNonce paymentMethodNonce = new PaymentMethodNonce(nodeWrapper);

        assertNotNull(paymentMethodNonce);
        assertEquals("PayPalAccount", paymentMethodNonce.getType());
        assertEquals("fake-paypal-billing-agreement-nonce", paymentMethodNonce.getNonce());
        assertEquals(false, paymentMethodNonce.isConsumed());
        assertNotNull(paymentMethodNonce.getDetails());
        assertEquals("jane.doe@paypal.com", paymentMethodNonce.getDetails().getEmail());

        assertEquals("jane2.doe@paypal.com", paymentMethodNonce.getDetails().getPayerInfo().getEmail());
        assertEquals("first", paymentMethodNonce.getDetails().getPayerInfo().getFirstName());
        assertEquals("last", paymentMethodNonce.getDetails().getPayerInfo().getLastName());
        assertEquals("pay-123", paymentMethodNonce.getDetails().getPayerInfo().getPayerId());
        assertEquals("US", paymentMethodNonce.getDetails().getPayerInfo().getCountryCode());
    }
}
