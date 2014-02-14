package com.braintreegateway.integrationtest;

import com.braintreegateway.*;
import com.braintreegateway.util.NodeWrapperFactory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TransferIT {
    private BraintreeGateway gateway;

    @Before
    public void setUp() {
        this.gateway = new BraintreeGateway(Environment.DEVELOPMENT, "integration_merchant_id", "integration_public_key", "integration_private_key");
    }

    @Test
    public void findMerchantAccount() {
      String xml = "<transfer>" +
                    "<merchant-account-id>sandbox_sub_merchant_account</merchant-account-id>" +
                    "<id>123456</id>" +
                    "<message>invalid_account_number</message>" +
                    "<amount>100.00</amount>" +
                    "<disbursement-date>2013-04-10</disbursement-date>" +
                    "<follow-up-action>update</follow-up-action>" +
                    "</transfer>";

      Transfer transfer = new Transfer(NodeWrapperFactory.instance.create(xml));
      assertNotNull(transfer);
      assertEquals("sandbox_sub_merchant_account", transfer.getMerchantAccount(gateway).getId());
    }

    @Test
    public void findTransactions() {
      String xml = "<transfer>" +
                    "<merchant-account-id>sandbox_sub_merchant_account</merchant-account-id>" +
                    "<id>123456</id>" +
                    "<message>invalid_account_number</message>" +
                    "<amount>100.00</amount>" +
                    "<disbursement-date>2013-04-10</disbursement-date>" +
                    "<follow-up-action>update</follow-up-action>" +
                    "</transfer>";

      Transfer transfer = new Transfer(NodeWrapperFactory.instance.create(xml));
      assertNotNull(transfer);

      ResourceCollection<Transaction> collection = transfer.getTransactions(this.gateway);
      assertEquals(1, collection.getMaximumSize());
      assertEquals("sub_merchant_transaction", collection.getFirst().getId());
    }
}
