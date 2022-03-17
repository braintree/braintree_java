package com.braintreegateway.integrationtest;

import com.braintreegateway.*;
import com.braintreegateway.exceptions.NotFoundException;

import java.util.Calendar;
import java.util.Map;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AddressIT extends IntegrationTest {

    @Test
    public void create() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        AddressRequest request = new AddressRequest().
            firstName("Joe").
            lastName("Smith").
            company("Smith Co.").
            streetAddress("1 E Main St").
            extendedAddress("Unit 2").
            locality("Chicago").
            region("Illinois").
            phoneNumber("8675309").
            postalCode("60607").
            countryName("United States of America").
            countryCodeAlpha2("US").
            countryCodeAlpha3("USA").
            countryCodeNumeric("840");

        Result<Address> createResult = gateway.address().create(customer.getId(), request);
        assertTrue(createResult.isSuccess());
        Address address = createResult.getTarget();
        assertEquals("Joe", address.getFirstName());
        assertEquals("Smith", address.getLastName());
        assertEquals("Smith Co.", address.getCompany());
        assertEquals("1 E Main St", address.getStreetAddress());
        assertEquals("Unit 2", address.getExtendedAddress());
        assertEquals("Chicago", address.getLocality());
        assertEquals("8675309", address.getPhoneNumber());
        assertEquals("Illinois", address.getRegion());
        assertEquals("60607", address.getPostalCode());
        assertEquals("United States of America", address.getCountryName());
        assertEquals("US", address.getCountryCodeAlpha2());
        assertEquals("USA", address.getCountryCodeAlpha3());
        assertEquals("840", address.getCountryCodeNumeric());
        assertEquals(Calendar.getInstance().get(Calendar.YEAR), address.getCreatedAt().get(Calendar.YEAR));
        assertEquals(Calendar.getInstance().get(Calendar.YEAR), address.getUpdatedAt().get(Calendar.YEAR));
    }

    @Test
    public void update() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        AddressRequest request = new AddressRequest().
            streetAddress("1 E Main St").
            extendedAddress("Unit 2").
            locality("Chicago").
            region("Illinois").
            postalCode("60607").
            countryName("United States of America");

        Result<Address> result = gateway.address().create(customer.getId(), request);
        assertTrue(result.isSuccess());
        Address address = result.getTarget();

        AddressRequest updateRequest = new AddressRequest().
            streetAddress("2 E Main St").
            extendedAddress("Unit 3").
            locality("Bartlett").
            region("Mass").
            postalCode("12345").
            countryName("Mexico").
            countryCodeAlpha2("MX").
            countryCodeAlpha3("MEX").
            countryCodeNumeric("484");

        Result<Address> updateResult = gateway.address().update(address.getCustomerId(), address.getId(), updateRequest);
        assertTrue(updateResult.isSuccess());
        Address updatedAddress = updateResult.getTarget();

        assertEquals("2 E Main St", updatedAddress.getStreetAddress());
        assertEquals("Unit 3", updatedAddress.getExtendedAddress());
        assertEquals("Bartlett", updatedAddress.getLocality());
        assertEquals("Mass", updatedAddress.getRegion());
        assertEquals("12345", updatedAddress.getPostalCode());
        assertEquals("Mexico", updatedAddress.getCountryName());
        assertEquals("MX", updatedAddress.getCountryCodeAlpha2());
        assertEquals("MEX", updatedAddress.getCountryCodeAlpha3());
        assertEquals("484", updatedAddress.getCountryCodeNumeric());
    }

    @Test
    public void find() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        AddressRequest request = new AddressRequest().
            streetAddress("1 E Main St");

        Result<Address> createResult = gateway.address().create(customer.getId(), request);
        assertTrue(createResult.isSuccess());
        Address address = createResult.getTarget();

        Address foundAddress = gateway.address().find(address.getCustomerId(), address.getId());
        assertEquals("1 E Main St", foundAddress.getStreetAddress());
    }

    @Test
    public void findWithEmptyIds() {
        try {
            gateway.address().find(" ", "address_id");
            fail("Should throw NotFoundException");
        } catch (NotFoundException e) {
        }

        try {
            gateway.address().find("customer_id", " ");
            fail("Should throw NotFoundException");
        } catch (NotFoundException e) {
        }
    }

    @Test
    public void delete() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        AddressRequest request = new AddressRequest().
            streetAddress("1 E Main St").
            extendedAddress("Unit 2").
            locality("Chicago").
            region("Illinois").
            postalCode("60607").
            countryName("United States of America");

        Result<Address> createResult = gateway.address().create(customer.getId(), request);
        assertTrue(createResult.isSuccess());
        Address address = createResult.getTarget();

        Result<Address> deleteResult = gateway.address().delete(address.getCustomerId(), address.getId());
        assertTrue(deleteResult.isSuccess());

        try {
            gateway.address().find(address.getCustomerId(), address.getId());
            fail();
        } catch (NotFoundException e) {
        }
    }

    @Test
    public void validationErrors() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        AddressRequest request = new AddressRequest().
            countryName("Tunisia").
            countryCodeAlpha2("US");

        Result<Address> createResult = gateway.address().create(customer.getId(), request);
        assertFalse(createResult.isSuccess());
        assertNull(createResult.getTarget());
        ValidationErrors errors = createResult.getErrors();
        assertEquals(ValidationErrorCode.ADDRESS_INCONSISTENT_COUNTRY, errors.forObject("address").onField("base").get(0).getCode());
    }

    @Test
    public void validationErrorsOnCountryCodeAlpha2() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        AddressRequest request = new AddressRequest().
            countryCodeAlpha2("ZZ");

        Result<Address> createResult = gateway.address().create(customer.getId(), request);
        assertFalse(createResult.isSuccess());
        assertNull(createResult.getTarget());
        ValidationErrors errors = createResult.getErrors();
        assertEquals(ValidationErrorCode.ADDRESS_COUNTRY_CODE_ALPHA2_IS_NOT_ACCEPTED, errors.forObject("address").onField("countryCodeAlpha2").get(0).getCode());
    }

    @Test
    public void validationErrorsOnCountryCodeAlpha3() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        AddressRequest request = new AddressRequest().
            countryCodeAlpha3("ZZZ");

        Result<Address> createResult = gateway.address().create(customer.getId(), request);
        assertFalse(createResult.isSuccess());
        assertNull(createResult.getTarget());
        ValidationErrors errors = createResult.getErrors();
        assertEquals(ValidationErrorCode.ADDRESS_COUNTRY_CODE_ALPHA3_IS_NOT_ACCEPTED, errors.forObject("address").onField("countryCodeAlpha3").get(0).getCode());
    }

    @Test
    public void validationErrorsOnCountryCodeNumeric() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        AddressRequest request = new AddressRequest().
            countryCodeNumeric("000");

        Result<Address> createResult = gateway.address().create(customer.getId(), request);
        assertFalse(createResult.isSuccess());
        assertNull(createResult.getTarget());
        ValidationErrors errors = createResult.getErrors();
        assertEquals(ValidationErrorCode.ADDRESS_COUNTRY_CODE_NUMERIC_IS_NOT_ACCEPTED, errors.forObject("address").onField("countryCodeNumeric").get(0).getCode());
    }

    @Test
    public void getParamsOnError() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        AddressRequest request = new AddressRequest().
            countryName("United States of Hammer");

        Result<Address> createResult = gateway.address().create(customer.getId(), request);
        assertFalse(createResult.isSuccess());
        Map<String, String> parameters = createResult.getParameters();
        assertEquals("integration_merchant_id", parameters.get("merchant_id"));
        assertEquals(customer.getId(), parameters.get("customer_id"));
        assertEquals("United States of Hammer", parameters.get("address[country_name]"));
    }
}
