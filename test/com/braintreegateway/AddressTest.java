package com.braintreegateway;

import java.util.Calendar;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.braintreegateway.exceptions.NotFoundException;


public class AddressTest {

    private BraintreeGateway gateway;

    @Before
    public void createGateway() {
        this.gateway = new BraintreeGateway(Environment.DEVELOPMENT, "integration_merchant_id", "integration_public_key", "integration_private_key");
    }

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
            postalCode("60607").
            countryName("United States of America");

        Result<Address> createResult = gateway.address().create(customer.getId(), request);
        Assert.assertTrue(createResult.isSuccess());
        Address address = createResult.getTarget();
        Assert.assertEquals("Joe", address.getFirstName());
        Assert.assertEquals("Smith", address.getLastName());
        Assert.assertEquals("Smith Co.", address.getCompany());
        Assert.assertEquals("1 E Main St", address.getStreetAddress());
        Assert.assertEquals("Unit 2", address.getExtendedAddress());
        Assert.assertEquals("Chicago", address.getLocality());
        Assert.assertEquals("Illinois", address.getRegion());
        Assert.assertEquals("60607", address.getPostalCode());
        Assert.assertEquals("United States of America", address.getCountryName());
        Assert.assertEquals(Calendar.getInstance().get(Calendar.YEAR), address.getCreatedAt().get(Calendar.YEAR));
        Assert.assertEquals(Calendar.getInstance().get(Calendar.YEAR), address.getUpdatedAt().get(Calendar.YEAR));
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
        Assert.assertTrue(result.isSuccess());
        Address address = result.getTarget();

        AddressRequest updateRequest = new AddressRequest().
            streetAddress("2 E Main St").
            extendedAddress("Unit 3").
            locality("Bartlett").
            region("Mass").
            postalCode("12345").
            countryName("Mexico");

        Result<Address> updateResult = gateway.address().update(address.getCustomerId(), address.getId(), updateRequest);
        Assert.assertTrue(updateResult.isSuccess());
        Address updatedAddress = updateResult.getTarget();

        Assert.assertEquals("2 E Main St", updatedAddress.getStreetAddress());
        Assert.assertEquals("Unit 3", updatedAddress.getExtendedAddress());
        Assert.assertEquals("Bartlett", updatedAddress.getLocality());
        Assert.assertEquals("Mass", updatedAddress.getRegion());
        Assert.assertEquals("12345", updatedAddress.getPostalCode());
        Assert.assertEquals("Mexico", updatedAddress.getCountryName());
    }

    @Test
    public void find() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        AddressRequest request = new AddressRequest().
            streetAddress("1 E Main St");

        Result<Address> createResult = gateway.address().create(customer.getId(), request);
        Assert.assertTrue(createResult.isSuccess());
        Address address = createResult.getTarget();

        Address foundAddress = gateway.address().find(address.getCustomerId(), address.getId());
        Assert.assertEquals("1 E Main St", foundAddress.getStreetAddress());
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
        Assert.assertTrue(createResult.isSuccess());
        Address address = createResult.getTarget();

        Result<Address> deleteResult = gateway.address().delete(address.getCustomerId(), address.getId());
        Assert.assertTrue(deleteResult.isSuccess());

        try {
            gateway.address().find(address.getCustomerId(), address.getId());
            Assert.fail();
        } catch (NotFoundException e) {
        }
    }

    @Test
    public void validationErrors() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        AddressRequest request = new AddressRequest().
            countryName("United States of Hammer");

        Result<Address> createResult = gateway.address().create(customer.getId(), request);
        Assert.assertFalse(createResult.isSuccess());
        Assert.assertNull(createResult.getTarget());
        ValidationErrors errors = createResult.getErrors();
        Assert.assertEquals(ValidationErrorCode.ADDRESS_COUNTRY_NAME_IS_NOT_ACCEPTED, errors.forObject("address").onField("countryName").get(0).getCode());
    }

    @Test
    public void getParamsOnError() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        AddressRequest request = new AddressRequest().
            countryName("United States of Hammer");

        Result<Address> createResult = gateway.address().create(customer.getId(), request);
        Assert.assertFalse(createResult.isSuccess());
        Map<String, String> parameters = createResult.getParameters();
        Assert.assertEquals("integration_merchant_id", parameters.get("merchant_id"));
        Assert.assertEquals(customer.getId(), parameters.get("customer_id"));
        Assert.assertEquals("United States of Hammer", parameters.get("address[country_name]"));
    }
}
