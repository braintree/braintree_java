package com.braintreegateway.unittest;

import com.braintreegateway.exceptions.NotFoundException;
import com.braintreegateway.AddressGateway;
import com.braintreegateway.AddressRequest;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AddressGatewayTest {

    @Test
    public void createNullCustomerIdRaisesNotFoundException() {
        assertThrows(NotFoundException.class, () -> {
            new AddressGateway(null, null).create(null, new AddressRequest());
        });
    }

    @Test
    public void createTraversalCustomerIdRaisesNotFoundException() {
        assertThrows(NotFoundException.class, () -> {
            new AddressGateway(null, null).create("../../foo", new AddressRequest());
        });
    }

    @Test
    public void findTraversalCustomerIdRaisesNotFoundException() {
        assertThrows(NotFoundException.class, () -> {
            new AddressGateway(null, null).find("../../foo", "id");
        });
    }

    @Test
    public void findTraversalAddressIdRaisesNotFoundException() {
        assertThrows(NotFoundException.class, () -> {
            new AddressGateway(null, null).find("customerId", "../../foo");
        });
    }

    @Test
    public void updateSeparatorAddressIdRaisesNotFoundException() {
        assertThrows(NotFoundException.class, () -> {
            new AddressGateway(null, null).update("customerId", "a/b", new AddressRequest());
        });
    }

    @Test
    public void deleteTraversalAddressIdRaisesNotFoundException() {
        assertThrows(NotFoundException.class, () -> {
            new AddressGateway(null, null).delete("customerId", "..");
        });
    }

    @Test
    public void findDotAddressIdRaisesNotFoundException() {
        assertThrows(NotFoundException.class, () -> {
            new AddressGateway(null, null).find("customerId", ".");
        });
    }

    @Test
    public void findDottedAddressIdRaisesNotFoundException() {
        assertThrows(NotFoundException.class, () -> {
            new AddressGateway(null, null).find("customerId", "foo.bar");
        });
    }
}
