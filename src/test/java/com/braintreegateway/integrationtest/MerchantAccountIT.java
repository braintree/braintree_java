package com.braintreegateway.integrationtest;

import com.braintreegateway.*;
import org.junit.Before;
import org.junit.Test;

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
    public void createRequiresNoId() {
        Result<MerchantAccount> result = gateway.merchantAccount().create(creationRequest());

        assertTrue("merchant Account creation should succeed", result.isSuccess());

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

        assertTrue("merchant Account creation should succeed", result.isSuccess());
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

    private MerchantAccountRequest creationRequest() {
        return new MerchantAccountRequest().
            applicantDetails().
                firstName("Joe").
                lastName("Bloggs").
                email("joe@bloggs.com").
                address().
                    streetAddress("123 Credibility St.").
                    postalCode("60606").
                    locality("Chicago").
                    region("IL").
                    done().
                dateOfBirth("10/9/1980").
                ssn("123-456-7890").
                routingNumber("122100024").
                accountNumber("98479798798").
                taxId("123456789").
                companyName("Calculon's Drama School").
                done().
            tosAccepted(true).
            masterMerchantAccountId("sandbox_master_merchant_account");
    }
}

