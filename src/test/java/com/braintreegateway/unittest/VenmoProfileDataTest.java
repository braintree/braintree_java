package com.braintreegateway.unittest;

import com.braintreegateway.VenmoProfileData;
import com.braintreegateway.util.NodeWrapper;
import com.braintreegateway.util.NodeWrapperFactory;
import com.braintreegateway.Address;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class VenmoProfileDataTest {

    @Test
    public void parsesNodeCorrectly() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<profile-data>" +
            "  <username>some-user-name</username>" +
            "  <first-name>some-first-name</first-name>" +
            "  <last-name>some-last-name</last-name>" +
            "  <phone-number>1234567890</phone-number>" +
            "  <email>some-email</email>" +
            "  <billing-address>" +
            "    <street-address>some-street</street-address>" +
            "    <extended-address>some-extended-address</extended-address>" +
            "    <locality>some-locality</locality>" +
            "    <region>some-region</region>" +
            "    <postal-code>some-code</postal-code>" +
            "  </billing-address>" +
            "  <shipping-address>" +
            "    <street-address>some-street</street-address>" +
            "    <extended-address>some-extended-address</extended-address>" +
            "    <locality>some-locality</locality>" +
            "    <region>some-region</region>" +
            "    <postal-code>some-code</postal-code>" +
            "  </shipping-address>" +
            "</profile-data>";

        NodeWrapper nodeWrapper = NodeWrapperFactory.instance.create(xml);
        VenmoProfileData venmoProfileData = new VenmoProfileData(nodeWrapper);

        assertNotNull(venmoProfileData);
        assertEquals("some-user-name", venmoProfileData.getUsername());
        assertEquals("some-first-name", venmoProfileData.getFirstName());
        assertEquals("some-last-name", venmoProfileData.getLastName());
        assertEquals("1234567890", venmoProfileData.getPhoneNumber());
        assertEquals("some-email", venmoProfileData.getEmail());

        Address billing = venmoProfileData.getBillingAddress();
        assertEquals("some-street", billing.getStreetAddress());
        assertEquals("some-extended-address", billing.getExtendedAddress());
        assertEquals("some-locality", billing.getLocality());
        assertEquals("some-region", billing.getRegion());
        assertEquals("some-code", billing.getPostalCode());

        Address shipping = venmoProfileData.getShippingAddress();
        assertEquals("some-street", shipping.getStreetAddress());
        assertEquals("some-extended-address", shipping.getExtendedAddress());
        assertEquals("some-locality", shipping.getLocality());
        assertEquals("some-region", shipping.getRegion());
        assertEquals("some-code", shipping.getPostalCode());
    }
}
