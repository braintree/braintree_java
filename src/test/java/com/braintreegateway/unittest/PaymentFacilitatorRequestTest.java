package com.braintreegateway.unittest;

import com.braintreegateway.PaymentFacilitatorRequest;
import com.braintreegateway.TransactionRequest;
import com.braintreegateway.util.SimpleNodeWrapper;
import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PaymentFacilitatorRequestTest {

    @Test
    public void toXmlIncludesAllElements() throws IOException, SAXException {
        PaymentFacilitatorRequest request = new PaymentFacilitatorRequest().
                paymentFacilitatorId("98765432109").
                subMerchant().
                    referenceNumber("123456789012345").
                    taxId("99112233445577").
                    legalName("a-sub-merchant").
                address().
                    streetAddress("10880 Ibitinga").
                    locality("Araraquara").
                    region("SP").
                    countryCodeAlpha2("BR").
                    postalCode("13525000").
                    internationalPhone().
                        countryCode("55").
                        nationalNumber("9876543210").
                        done().
                    done().
                done();
    

        String expectedXML =
                "<paymentFacilitator>\n"
                        + "  <paymentFacilitatorId>98765432109</paymentFacilitatorId>\n"
                        + "  <subMerchant>\n"
                        + "    <referenceNumber>123456789012345</referenceNumber>\n"
                        + "    <taxId>99112233445577</taxId>\n"
                        + "    <legalName>a-sub-merchant</legalName>\n"
                        + "    <address>\n"
                        + "      <streetAddress>10880 Ibitinga</streetAddress>\n"
                        + "      <locality>Araraquara</locality>\n"
                        + "      <region>SP</region>\n"
                        + "      <countryCodeAlpha2>BR</countryCodeAlpha2>\n"
                        + "      <postalCode>13525000</postalCode>\n"
                        + "      <internationalPhone>\n"
                        + "        <countryCode>55</countryCode>\n"
                        + "        <nationalNumber>9876543210</nationalNumber>\n"
                        + "      </internationalPhone>\n"
                        + "    </address>\n"
                        + "  </subMerchant>\n"
                        + "</paymentFacilitator>";

        XMLUnit.setIgnoreWhitespace(true);
        XMLAssert.assertXMLEqual(expectedXML, request.toXML());
    }

}