package com.braintreegateway.unittest;

import com.braintreegateway.BinData;
import com.braintreegateway.CreditCard;
import com.braintreegateway.PaymentMethodNonce;
import com.braintreegateway.util.NodeWrapper;
import com.braintreegateway.util.NodeWrapperFactory;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
    }
}
