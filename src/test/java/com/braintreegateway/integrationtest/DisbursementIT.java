package com.braintreegateway.integrationtest;

import java.lang.reflect.Field;

import com.braintreegateway.*;
import com.braintreegateway.util.NodeWrapperFactory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DisbursementIT {
    private BraintreeGateway gateway;

    @Before
    public void setUp() {
        this.gateway = new BraintreeGateway(Environment.DEVELOPMENT, "integration_merchant_id", "integration_public_key", "integration_private_key");
    }

    @Test
    public void findMerchantAccount() {
      String xml = "<disbursement>" +
                    "<merchant-account-id>sandbox_sub_merchant_account</merchant-account-id>" +
                    "<id>123456</id>" +
                    "<message>invalid_account_number</message>" +
                    "<amount>100.00</amount>" +
                    "<disbursement-date>2013-04-10</disbursement-date>" +
                    "<follow-up-action>update</follow-up-action>" +
                    "</disbursement>";

      Disbursement disbursement = new Disbursement(NodeWrapperFactory.instance.create(xml));
      assertNotNull(disbursement);
      assertEquals("sandbox_sub_merchant_account", disbursement.getMerchantAccount(gateway).getId());
    }

    @Test
    public void merchantAccountMemoized() throws Exception{
      String xml = "<disbursement>" +
                    "<merchant-account-id>sandbox_sub_merchant_account</merchant-account-id>" +
                    "<id>123456</id>" +
                    "<message>invalid_account_number</message>" +
                    "<amount>100.00</amount>" +
                    "<disbursement-date>2013-04-10</disbursement-date>" +
                    "<follow-up-action>update</follow-up-action>" +
                    "</disbursement>";

      Disbursement disbursement = new Disbursement(NodeWrapperFactory.instance.create(xml));
      MerchantAccount firstMerchantAccount = disbursement.getMerchantAccount(gateway);

      Field field = Disbursement.class.getDeclaredField("merchantAccountId");
      field.setAccessible(true);
      field.set(disbursement, "non existent");

      assertEquals(firstMerchantAccount, disbursement.getMerchantAccount(gateway));
    }

    @Test
    public void findTransactions() {
      String xml = "<disbursement>" +
                    "<merchant-account-id>sandbox_sub_merchant_account</merchant-account-id>" +
                    "<id>123456</id>" +
                    "<message>invalid_account_number</message>" +
                    "<amount>100.00</amount>" +
                    "<disbursement-date>2013-04-10</disbursement-date>" +
                    "<follow-up-action>update</follow-up-action>" +
                    "</disbursement>";

      Disbursement disbursement = new Disbursement(NodeWrapperFactory.instance.create(xml));
      assertNotNull(disbursement);

      ResourceCollection<Transaction> collection = disbursement.getTransactions(this.gateway);
      assertEquals(1, collection.getMaximumSize());
      assertEquals("sub_merchant_transaction", collection.getFirst().getId());
    }
}
