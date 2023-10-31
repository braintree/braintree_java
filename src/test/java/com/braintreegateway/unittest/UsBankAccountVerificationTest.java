package com.braintreegateway.unittest;

import java.util.Calendar;

import com.braintreegateway.UsBankAccountVerification;
import com.braintreegateway.util.SimpleNodeWrapper;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UsBankAccountVerificationTest {
    @Test
    public void testVerificationAttributes() {
        String xml = "<us-bank-account-verification>"
                     + "  <status>verified</status>"
                     + "  <gateway-rejection-reason nil=\"true\"/>"
                     + "  <merchant-account-id>ygmxmpdxthqrrtfyisqahvclo</merchant-account-id>"
                     + "  <processor-response-code>1000</processor-response-code>"
                     + "  <processor-response-text>Approved</processor-response-text>"
                     + "  <additional-processor-response>Invalid routing number</additional-processor-response>"
                     + "  <id>6f34vp3z</id>"
                     + "  <verification-method>independent_check</verification-method>"
                     + "  <verification-add-ons>customer_verification</verification-add-ons>"
                     + "  <verification-determined-at type=\"datetime\">2018-11-16T23:22:48Z</verification-determined-at>"
                     + "  <us-bank-account>"
                     + "    <token>ch6byss</token>"
                     + "    <last-4>1234</last-4>"
                     + "    <account-type>checking</account-type>"
                     + "    <account-holder-name nil=\"true\"/>"
                     + "    <bank-name>Wells Fargo</bank-name>"
                     + "    <routing-number>123456789</routing-number>"
                     + "  </us-bank-account>"
                     + "  <created-at type=\"datetime\">2018-04-12T19:54:16Z</created-at>"
                     + "  <updated-at type=\"datetime\">2018-04-12T19:54:16Z</updated-at>"
                     + "</us-bank-account-verification>";

        SimpleNodeWrapper node = SimpleNodeWrapper.parse(xml);
        UsBankAccountVerification verification = new UsBankAccountVerification(node);

        assertEquals(UsBankAccountVerification.Status.VERIFIED, verification.getStatus());
        assertEquals(UsBankAccountVerification.VerificationMethod.INDEPENDENT_CHECK, verification.getVerificationMethod());
        assertEquals(UsBankAccountVerification.VerificationAddOns.CUSTOMER_VERIFICATION, verification.getVerificationAddOns());
        assertEquals("1000", verification.getProcessorResponseCode());
        assertEquals("Invalid routing number", verification.getAdditionalProcessorResponse());
        assertEquals("6f34vp3z", verification.getId());
        assertEquals(2018, verification.getCreatedAt().get(Calendar.YEAR));
        assertEquals(4, verification.getCreatedAt().get(Calendar.MONTH)+1);
        assertEquals(12, verification.getCreatedAt().get(Calendar.DAY_OF_MONTH));
        assertEquals(7, verification.getCreatedAt().get(Calendar.HOUR));
        assertEquals(54, verification.getCreatedAt().get(Calendar.MINUTE));
        assertEquals(16, verification.getCreatedAt().get(Calendar.SECOND));

        assertEquals(2018, verification.getVerificationDeterminedAt().get(Calendar.YEAR));
        assertEquals(11, verification.getVerificationDeterminedAt().get(Calendar.MONTH)+1);
        assertEquals(16, verification.getVerificationDeterminedAt().get(Calendar.DAY_OF_MONTH));
        assertEquals(11, verification.getVerificationDeterminedAt().get(Calendar.HOUR));
        assertEquals(22, verification.getVerificationDeterminedAt().get(Calendar.MINUTE));
        assertEquals(48, verification.getVerificationDeterminedAt().get(Calendar.SECOND));

        assertEquals("ch6byss", verification.getUsBankAccount().getToken());
    }
}
