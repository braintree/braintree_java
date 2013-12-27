package com.braintreegateway.integrationtest;

import com.braintreegateway.*;
import org.junit.Before;
import org.junit.Test;
import com.braintreegateway.exceptions.NotFoundException;

import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

public class MerchantAccountIT {

    private BraintreeGateway gateway;

    @Before
    public void createGateway() {
        this.gateway = new BraintreeGateway(Environment.DEVELOPMENT, "integration_merchant_id", "integration_public_key",
                "integration_private_key");
    }

    @Test
    public void deprecatedCreateSucceeds() {
        Result<MerchantAccount> result = gateway.merchantAccount().create(deprecatedCreationRequest());

        assertTrue("merchant account creation should succeed", result.isSuccess());

        MerchantAccount ma = result.getTarget();
        assertEquals("account status should be pending", MerchantAccount.Status.PENDING, ma.getStatus());
        assertEquals("sandbox_master_merchant_account", ma.getMasterMerchantAccount().getId());
        assertTrue(ma.isSubMerchant());
        assertFalse(ma.getMasterMerchantAccount().isSubMerchant());
    }

    @Test
    public void createRequiresNoId() {
        Result<MerchantAccount> result = gateway.merchantAccount().create(creationRequest());

        assertTrue("merchant account creation should succeed", result.isSuccess());

        MerchantAccount ma = result.getTarget();
        assertEquals("account status should be pending", MerchantAccount.Status.PENDING, ma.getStatus());
        assertEquals("sandbox_master_merchant_account", ma.getMasterMerchantAccount().getId());
        assertTrue(ma.isSubMerchant());
        assertFalse(ma.getMasterMerchantAccount().isSubMerchant());
    }

    @Test
    public void createWillUseIdIfPassed() {
        int randomNumber = new Random().nextInt() % 10000;
        String subMerchantAccountId = String.format("sub_merchant_account_id_%d", randomNumber);
        MerchantAccountRequest request = creationRequest().id(subMerchantAccountId);
        Result<MerchantAccount> result = gateway.merchantAccount().create(request);

        assertTrue("merchant account creation should succeed", result.isSuccess());
        MerchantAccount ma = result.getTarget();
        assertEquals("account status should be pending", MerchantAccount.Status.PENDING, ma.getStatus());
        assertEquals("submerchant id should be assigned", subMerchantAccountId, ma.getId());
        assertEquals("sandbox_master_merchant_account", ma.getMasterMerchantAccount().getId());
        assertTrue(ma.isSubMerchant());
        assertFalse(ma.getMasterMerchantAccount().isSubMerchant());
    }

    @Test
    public void handlesUnsuccessfulResults() {
        Result<MerchantAccount> result = gateway.merchantAccount().create(new MerchantAccountRequest());
        List<ValidationError> errors = result.getErrors().forObject("merchant-account").onField("master_merchant_account_id");
        assertEquals(1, errors.size());
        assertEquals(ValidationErrorCode.MERCHANT_ACCOUNT_MASTER_MERCHANT_ACCOUNT_ID_IS_REQUIRED, errors.get(0).getCode());
    }

    @Test
    public void createAcceptsBankFundingDestination() {
        MerchantAccountRequest request = creationRequest().
            funding().
                destination(MerchantAccount.FundingDestination.BANK).
                routingNumber("122100024").
                accountNumber("98479798798").
                done();
        Result<MerchantAccount> result = gateway.merchantAccount().create(request);

        assertTrue("merchant account creation should succeed", result.isSuccess());
    }

    @Test
    public void createAcceptsEmailFundingDestination() {
        MerchantAccountRequest request = creationRequest().
            funding().
                destination(MerchantAccount.FundingDestination.EMAIL).
                email("joe@bloggs.com").
                done();
        Result<MerchantAccount> result = gateway.merchantAccount().create(request);

        assertTrue("merchant account creation should succeed", result.isSuccess());
    }

    @Test
    public void createAcceptsMobilePhoneFundingDestination() {
        MerchantAccountRequest request = creationRequest().
            funding().
                destination(MerchantAccount.FundingDestination.MOBILE_PHONE).
                mobilePhone("3125551212").
                done();
        Result<MerchantAccount> result = gateway.merchantAccount().create(request);

        assertTrue("merchant account creation should succeed", result.isSuccess());
    }

    @Test
    public void findMerchantAccountWithGivenToken() {
        Result<MerchantAccount> result = gateway.merchantAccount().create(creationRequest());
        assertTrue("merchant account creation should succeed", result.isSuccess());
        MerchantAccount ma = result.getTarget();
        assertEquals("account status should be pending", MerchantAccount.Status.PENDING, ma.getStatus());

        String merchantAccountId = ma.getId();

        Result<MerchantAccount> found_result = gateway.merchantAccount().find(merchantAccountId);
        MerchantAccount found_ma = found_result.getTarget();
        assertEquals("found account status should be active", MerchantAccount.Status.ACTIVE, found_ma.getStatus());
        assertEquals("found account individual first name should match original", "Job", found_ma.getIndividualDetails().getFirstName());
        assertEquals("found account individual last name should match original", "Leoggs", found_ma.getIndividualDetails().getLastName());
    }

    @Test
    public void findThrowsExceptionIfMerchantNotFound() {
        try {
            gateway.merchantAccount().find("non-existent");
            fail("Should throw NotFoundException");
        } catch (NotFoundException e) {
        }
    }

    @Test
    public void updateUpdatesAllFields() {
        Result<MerchantAccount> result = gateway.merchantAccount().create(deprecatedCreationRequest());
        assertTrue("merchant account creation should succeed", result.isSuccess());
        MerchantAccountRequest request = creationRequest().
            masterMerchantAccountId(null);
        Result<MerchantAccount> updateResult = gateway.merchantAccount().update(result.getTarget().getId(), request);
        assertTrue("merchant account update should succeed", updateResult.isSuccess());
        MerchantAccount merchantAccount = updateResult.getTarget();
        assertEquals("Job", merchantAccount.getIndividualDetails().getFirstName());
        assertEquals("Leoggs", merchantAccount.getIndividualDetails().getLastName());
        assertEquals("job@leoggs.com", merchantAccount.getIndividualDetails().getEmail());
        assertEquals("5555551212", merchantAccount.getIndividualDetails().getPhone());
        assertEquals("193 Credibility St.", merchantAccount.getIndividualDetails().getAddress().getStreetAddress());
        assertEquals("60611", merchantAccount.getIndividualDetails().getAddress().getPostalCode());
        assertEquals("Avondale", merchantAccount.getIndividualDetails().getAddress().getLocality());
        assertEquals("IN", merchantAccount.getIndividualDetails().getAddress().getRegion());
        assertEquals("1985-09-10", merchantAccount.getIndividualDetails().getDateOfBirth());
        assertEquals("Calculon", merchantAccount.getBusinessDetails().getLegalName());
        assertEquals("Calculon", merchantAccount.getBusinessDetails().getDbaName());
        assertEquals("123456780", merchantAccount.getBusinessDetails().getTaxId());
        assertEquals("135 Credibility St.", merchantAccount.getBusinessDetails().getAddress().getStreetAddress());
        assertEquals("60602", merchantAccount.getBusinessDetails().getAddress().getPostalCode());
        assertEquals("Gary", merchantAccount.getBusinessDetails().getAddress().getLocality());
        assertEquals("OH", merchantAccount.getBusinessDetails().getAddress().getRegion());
        assertEquals(MerchantAccount.FundingDestination.EMAIL, merchantAccount.getFundingDetails().getDestination());
        assertEquals("joe+funding@bloggs.com", merchantAccount.getFundingDetails().getEmail());
        assertEquals("3125551212", merchantAccount.getFundingDetails().getMobilePhone());
        assertEquals("122100024", merchantAccount.getFundingDetails().getRoutingNumber());
        assertEquals("8799", merchantAccount.getFundingDetails().getAccountNumberLast4());
    }

    @Test
    public void createHandlesRequiredValidationErrors() {
        MerchantAccountRequest request = new MerchantAccountRequest().
            tosAccepted(true).
            masterMerchantAccountId("sandbox_master_merchant_account");
        Result<MerchantAccount> result = gateway.merchantAccount().create(request);
        assertFalse(result.isSuccess());
        ValidationErrors errors = result.getErrors().forObject("merchant-account");
        assertEquals(ValidationErrorCode.MERCHANT_ACCOUNT_INDIVIDUAL_FIRST_NAME_IS_REQUIRED,
            errors.forObject("individual").onField("first-name").get(0).getCode());
        assertEquals(ValidationErrorCode.MERCHANT_ACCOUNT_INDIVIDUAL_LAST_NAME_IS_REQUIRED,
            errors.forObject("individual").onField("last-name").get(0).getCode());
        assertEquals(ValidationErrorCode.MERCHANT_ACCOUNT_INDIVIDUAL_DATE_OF_BIRTH_IS_REQUIRED,
            errors.forObject("individual").onField("date-of-birth").get(0).getCode());
        assertEquals(ValidationErrorCode.MERCHANT_ACCOUNT_INDIVIDUAL_EMAIL_IS_REQUIRED,
            errors.forObject("individual").onField("email").get(0).getCode());
        assertEquals(ValidationErrorCode.MERCHANT_ACCOUNT_INDIVIDUAL_ADDRESS_STREET_ADDRESS_IS_REQUIRED,
            errors.forObject("individual").forObject("address").onField("street-address").get(0).getCode());
        assertEquals(ValidationErrorCode.MERCHANT_ACCOUNT_INDIVIDUAL_ADDRESS_LOCALITY_IS_REQUIRED,
            errors.forObject("individual").forObject("address").onField("locality").get(0).getCode());
        assertEquals(ValidationErrorCode.MERCHANT_ACCOUNT_INDIVIDUAL_ADDRESS_POSTAL_CODE_IS_REQUIRED,
            errors.forObject("individual").forObject("address").onField("postal-code").get(0).getCode());
        assertEquals(ValidationErrorCode.MERCHANT_ACCOUNT_INDIVIDUAL_ADDRESS_REGION_IS_REQUIRED,
            errors.forObject("individual").forObject("address").onField("region").get(0).getCode());
        assertEquals(ValidationErrorCode.MERCHANT_ACCOUNT_FUNDING_DESTINATION_IS_REQUIRED,
            errors.forObject("funding").onField("destination").get(0).getCode());
    }

    @Test
    public void createHandlesInvalidValidationErrors() {
        MerchantAccountRequest request = new MerchantAccountRequest().
            individual().
                firstName("<>").
                lastName("<>").
                email("bad").
                phone("999").
                address().
                    streetAddress("nope").
                    postalCode("1").
                    region("QQ").
                    done().
                dateOfBirth("hah").
                ssn("12345").
                done().
            business().
                taxId("bad").
                dbaName("{}``").
                legalName("``{}").
                address().
                    streetAddress("nope").
                    postalCode("1").
                    region("QQ").
                    done().
                done().
            funding().
                destination(MerchantAccount.FundingDestination.UNRECOGNIZED).
                routingNumber("LEATHER").
                accountNumber("BACK POCKET").
                email("BILLFOLD").
                mobilePhone("TRIFOLD").
                done().
            tosAccepted(true).
            masterMerchantAccountId("sandbox_master_merchant_account");
        Result<MerchantAccount> result = gateway.merchantAccount().create(request);
        assertFalse(result.isSuccess());
        ValidationErrors errors = result.getErrors().forObject("merchant-account");
        assertEquals(ValidationErrorCode.MERCHANT_ACCOUNT_INDIVIDUAL_FIRST_NAME_IS_INVALID,
            errors.forObject("individual").onField("first-name").get(0).getCode());
        assertEquals(ValidationErrorCode.MERCHANT_ACCOUNT_INDIVIDUAL_LAST_NAME_IS_INVALID,
            errors.forObject("individual").onField("last-name").get(0).getCode());
        assertEquals(ValidationErrorCode.MERCHANT_ACCOUNT_INDIVIDUAL_DATE_OF_BIRTH_IS_INVALID,
            errors.forObject("individual").onField("date-of-birth").get(0).getCode());
        assertEquals(ValidationErrorCode.MERCHANT_ACCOUNT_INDIVIDUAL_PHONE_IS_INVALID,
            errors.forObject("individual").onField("phone").get(0).getCode());
        assertEquals(ValidationErrorCode.MERCHANT_ACCOUNT_INDIVIDUAL_SSN_IS_INVALID,
            errors.forObject("individual").onField("ssn").get(0).getCode());
        assertEquals(ValidationErrorCode.MERCHANT_ACCOUNT_INDIVIDUAL_EMAIL_IS_INVALID,
            errors.forObject("individual").onField("email").get(0).getCode());
        assertEquals(ValidationErrorCode.MERCHANT_ACCOUNT_INDIVIDUAL_ADDRESS_STREET_ADDRESS_IS_INVALID,
            errors.forObject("individual").forObject("address").onField("street-address").get(0).getCode());
        assertEquals(ValidationErrorCode.MERCHANT_ACCOUNT_INDIVIDUAL_ADDRESS_POSTAL_CODE_IS_INVALID,
            errors.forObject("individual").forObject("address").onField("postal-code").get(0).getCode());
        assertEquals(ValidationErrorCode.MERCHANT_ACCOUNT_INDIVIDUAL_ADDRESS_REGION_IS_INVALID,
            errors.forObject("individual").forObject("address").onField("region").get(0).getCode());
        assertEquals(ValidationErrorCode.MERCHANT_ACCOUNT_BUSINESS_DBA_NAME_IS_INVALID,
            errors.forObject("business").onField("dba-name").get(0).getCode());
        assertEquals(ValidationErrorCode.MERCHANT_ACCOUNT_BUSINESS_LEGAL_NAME_IS_INVALID,
            errors.forObject("business").onField("legal-name").get(0).getCode());
        assertEquals(ValidationErrorCode.MERCHANT_ACCOUNT_BUSINESS_TAX_ID_IS_INVALID,
            errors.forObject("business").onField("tax-id").get(0).getCode());
        assertEquals(ValidationErrorCode.MERCHANT_ACCOUNT_BUSINESS_ADDRESS_STREET_ADDRESS_IS_INVALID,
            errors.forObject("business").forObject("address").onField("street-address").get(0).getCode());
        assertEquals(ValidationErrorCode.MERCHANT_ACCOUNT_BUSINESS_ADDRESS_POSTAL_CODE_IS_INVALID,
            errors.forObject("business").forObject("address").onField("postal-code").get(0).getCode());
        assertEquals(ValidationErrorCode.MERCHANT_ACCOUNT_BUSINESS_ADDRESS_REGION_IS_INVALID,
            errors.forObject("business").forObject("address").onField("region").get(0).getCode());
        assertEquals(ValidationErrorCode.MERCHANT_ACCOUNT_FUNDING_DESTINATION_IS_INVALID,
            errors.forObject("funding").onField("destination").get(0).getCode());
        assertEquals(ValidationErrorCode.MERCHANT_ACCOUNT_FUNDING_ACCOUNT_NUMBER_IS_INVALID,
            errors.forObject("funding").onField("account-number").get(0).getCode());
        assertEquals(ValidationErrorCode.MERCHANT_ACCOUNT_FUNDING_ROUTING_NUMBER_IS_INVALID,
            errors.forObject("funding").onField("routing-number").get(0).getCode());
        assertEquals(ValidationErrorCode.MERCHANT_ACCOUNT_FUNDING_EMAIL_IS_INVALID,
            errors.forObject("funding").onField("email").get(0).getCode());
        assertEquals(ValidationErrorCode.MERCHANT_ACCOUNT_FUNDING_MOBILE_PHONE_IS_INVALID,
            errors.forObject("funding").onField("mobile-phone").get(0).getCode());
    }

    private MerchantAccountRequest deprecatedCreationRequest() {
        return new MerchantAccountRequest().
            applicantDetails().
                firstName("Joe").
                lastName("Bloggs").
                email("joe@bloggs.com").
                phone("555-555-5555").
                address().
                    streetAddress("123 Credibility St.").
                    postalCode("60606").
                    locality("Chicago").
                    region("IL").
                    done().
                dateOfBirth("10/9/1980").
                ssn("123-45-7890").
                routingNumber("122100024").
                accountNumber("98479798798").
                taxId("123456789").
                companyName("Calculon's Drama School").
                done().
            tosAccepted(true).
            masterMerchantAccountId("sandbox_master_merchant_account");
    }

    private MerchantAccountRequest creationRequest() {
        return new MerchantAccountRequest().
            individual().
                firstName("Job").
                lastName("Leoggs").
                email("job@leoggs.com").
                phone("555-555-1212").
                address().
                    streetAddress("193 Credibility St.").
                    postalCode("60611").
                    locality("Avondale").
                    region("IN").
                    done().
                dateOfBirth("10/9/1985").
                ssn("123-45-1235").
                done().
            business().
                taxId("123456780").
                dbaName("Calculon").
                legalName("Calculon").
                address().
                    streetAddress("135 Credibility St.").
                    postalCode("60602").
                    locality("Gary").
                    region("OH").
                    done().
                done().
            funding().
                destination(MerchantAccount.FundingDestination.EMAIL).
                routingNumber("122100024").
                accountNumber("98479798799").
                email("joe+funding@bloggs.com").
                mobilePhone("3125551212").
                done().
            tosAccepted(true).
            masterMerchantAccountId("sandbox_master_merchant_account");
    }
}

