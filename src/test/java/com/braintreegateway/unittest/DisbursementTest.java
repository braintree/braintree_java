package com.braintreegateway.unittest;

import com.braintreegateway.Disbursement;
import com.braintreegateway.util.SimpleNodeWrapper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DisbursementTest {

    @Test
    public void dibursementTypeIsSet() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<dibursement>\n" +
            "   <id>12345</id>\n" +
            "   <merchant-account>\n" +
            "       <status>active</status>\n" +
            "       <id>merchant_account_token</id>\n" +
            "       <currency-iso-code>USD</currency-iso-code>\n" +
            "       <default type=\"boolean\">false</default>\n" +
            "       <sub-merchant-account type=\"boolean\">false</sub-merchant-account>\n" +
            "   </merchant-account>\n" +
            "   <exception-message nil=\"true\"/>\n" +
            "   <amount>100.00</amount>\n" +
            "   <disbursement-type>credit</disbursement-type>\n" +
            "   <success type=\"boolean\">true</success>\n" +
            "   <retry type=\"boolean\">true</retry>\n" +
            "   <transaction-ids type=\"array\">\n" +
            "       <item>asdf</item>\n" +
            "       <item>qwer</item>\n" +
            "   </transaction-ids>\n" +
            "</dibursement>\n";

        SimpleNodeWrapper disbursementNode = SimpleNodeWrapper.parse(xml);
        Disbursement disbursement = new Disbursement(disbursementNode);

        assertEquals(Disbursement.DisbursementType.CREDIT, disbursement.getDisbursementType());
    }

    @Test
    public void dibursementTypeIsSetToUknown() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<dibursement>\n" +
            "   <id>12345</id>\n" +
            "   <merchant-account>\n" +
            "       <status>active</status>\n" +
            "       <id>merchant_account_token</id>\n" +
            "       <currency-iso-code>USD</currency-iso-code>\n" +
            "       <default type=\"boolean\">false</default>\n" +
            "       <sub-merchant-account type=\"boolean\">false</sub-merchant-account>\n" +
            "   </merchant-account>\n" +
            "   <exception-message nil=\"true\"/>\n" +
            "   <amount>100.00</amount>\n" +
            "   <success type=\"boolean\">true</success>\n" +
            "   <retry type=\"boolean\">true</retry>\n" +
            "   <transaction-ids type=\"array\">\n" +
            "       <item>asdf</item>\n" +
            "       <item>qwer</item>\n" +
            "   </transaction-ids>\n" +
            "</dibursement>\n";

        SimpleNodeWrapper disbursementNode = SimpleNodeWrapper.parse(xml);
        Disbursement disbursement = new Disbursement(disbursementNode);

        assertEquals(Disbursement.DisbursementType.UNKNOWN, disbursement.getDisbursementType());
    }
}
