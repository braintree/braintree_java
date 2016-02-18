package com.braintreegateway.integrationtest;

import java.lang.reflect.Field;

import com.braintreegateway.*;
import com.braintreegateway.util.NodeWrapperFactory;
import com.braintreegateway.testhelpers.CalendarTestUtils;
import org.junit.Test;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class DisbursementIT extends IntegrationTest {

    @Test
    public void createDisbursement() {
        String xml = "<disbursement>" +
                     "<id>123456</id>" +
                     "<transaction-ids type=\"array\">" +
                     "<item>sub_merchant_transaction</item>" +
                     "</transaction-ids>" +
                     "<success type=\"boolean\">false</success>" +
                     "<retry type=\"boolean\">false</retry>" +
                     "<merchant-account>" +
                     "<id>sandbox_sub_merchant_account</id>" +
                     "<currency-iso-code>USD</currency-iso-code>" +
                     "<sub-merchant-account type=\"boolean\">true</sub-merchant-account>" +
                     "<status>active</status>" +
                     "</merchant-account>" +
                     "<amount>100.00</amount>" +
                     "<disbursement-date type=\"date\">2013-04-10</disbursement-date>" +
                     "<exception-message>invalid_account_number</exception-message>" +
                     "<follow-up-action>update_funding_information</follow-up-action>" +
                     "</disbursement>";

        Disbursement disbursement = new Disbursement(NodeWrapperFactory.instance.create(xml));
        assertNotNull(disbursement);

        List<String> transactionIds = new ArrayList<String>();
        transactionIds.add("sub_merchant_transaction");

        assertEquals("123456", disbursement.getId());
        assertEquals(false, disbursement.getSuccess());
        assertEquals(false, disbursement.getRetry());
        assertEquals(new BigDecimal("100.00"), disbursement.getAmount());
        assertEquals("update_funding_information", disbursement.getFollowUpAction());
        assertEquals("invalid_account_number", disbursement.getExceptionMessage());
        assertEquals(transactionIds, disbursement.getTransactionIds());
        assertEquals("sandbox_sub_merchant_account", disbursement.getMerchantAccount().getId());
        assertEquals(2013, disbursement.getDisbursementDate().get(Calendar.YEAR));
    }

    @Test
    public void findTransactions() {
      String xml = "<disbursement>" +
                   "<id>123456</id>" +
                   "<transaction-ids type=\"array\">" +
                   "<item>sub_merchant_transaction</item>" +
                   "</transaction-ids>" +
                   "<success type=\"boolean\">false</success>" +
                   "<retry type=\"boolean\">false</retry>" +
                   "<merchant-account>" +
                   "<id>sandbox_sub_merchant_account</id>" +
                   "<currency-iso-code>USD</currency-iso-code>" +
                   "<sub-merchant-account type=\"boolean\">true</sub-merchant-account>" +
                   "<status>active</status>" +
                   "</merchant-account>" +
                   "<amount>100.00</amount>" +
                   "<disbursement-date type=\"date\">2013-04-10</disbursement-date>" +
                   "<exception-message>invalid_account_number</exception-message>" +
                   "<follow-up-action>update_funding_information</follow-up-action>" +
                   "</disbursement>";

      Disbursement disbursement = new Disbursement(NodeWrapperFactory.instance.create(xml));
      assertNotNull(disbursement);

      ResourceCollection<Transaction> collection = disbursement.getTransactions(this.gateway);
      assertEquals(1, collection.getMaximumSize());
      assertEquals("sub_merchant_transaction", collection.getFirst().getId());
    }
}
