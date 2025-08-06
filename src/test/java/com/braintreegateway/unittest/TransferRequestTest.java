package com.braintreegateway.unittest;

import java.io.IOException;
import com.braintreegateway.TransferRequest;
import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

public class TransferRequestTest {

    @Test
    public void toXmlIncludesAllElements() throws IOException, SAXException {
        TransferRequest transferRequest = new TransferRequest();

        String[] transferTypes = {"account_to_account", "person_to_person", "wallet_transfer", "fund_transfer", "fund_disbursement", "payroll_disbursement", "prepaid_top_up"};

        for(String transferType : transferTypes) {
            transferRequest.type(transferType).done();

            String expectedXML =
                "<transfer>" +
                    "<type>" + transferType + "</type>" +
                "</transfer>";

            XMLUnit.setIgnoreWhitespace(true);
            XMLAssert.assertXMLEqual(expectedXML, transferRequest.toXML());
        }
    }
}
