package com.braintreegateway.unittest;

import com.braintreegateway.UsBankAccount;
import com.braintreegateway.util.SimpleNodeWrapper;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UsBankAccountTest {
    @Test
    public void testUsBankAccountAttributes() {
        String xml = "<us-bank-account>"
                     + "<routing-number>123456789</routing-number>"
                     + "<last-4>1234</last-4>"
                     + "<account-type>checking</account-type>"
                     + "<account-holder-name>Audrey</account-holder-name>"
                     + "<bank-name>Wells Fargo</bank-name>"
                     + "<ach-mandate>"
                     + "  <accepted-at type=\"datetime\">2016-11-16T23:22:48Z</accepted-at>"
                     + "  <text>example mandate text</text>"
                     + "</ach-mandate>"
                     + "<default type=\"boolean\">true</default>"
                     + "<token>ch6byss</token>"
                     + "<customer-id>1396526238</customer-id>"
                     + "<image-url>https://jsdk.docker.dev:9000/payment_method_logo/us_bank_account.png?environment=test</image-url>"
                     + "<verified type=\"boolean\">true</verified>"
                     + "<verifications type=\"array\">"
                     + "  <us-bank-account-verification>"
                     + "    <status>verified</status>"
                     + "    <gateway-rejection-reason nil=\"true\"/>"
                     + "    <merchant-account-id>ygmxmpdxthqrrtfyisqahvclo</merchant-account-id>"
                     + "    <processor-response-code>1000</processor-response-code>"
                     + "    <processor-response-text>Approved</processor-response-text>"
                     + "    <id>6f34vp3z</id>"
                     + "    <verification-method>independent_check</verification-method>"
                     + "    <verification-determined-at type=\"datetime\">2018-11-16T23:22:48Z</verification-determined-at>"
                     + "    <us-bank-account>"
                     + "      <token>ch6byss</token>"
                     + "      <last-4>1234</last-4>"
                     + "      <account-type>checking</account-type>"
                     + "      <account-holder-name nil=\"true\"/>"
                     + "      <bank-name>Wells Fargo</bank-name>"
                     + "      <routing-number>123456789</routing-number>"
                     + "    </us-bank-account>"
                     + "    <created-at type=\"datetime\">2018-04-12T19:54:16Z</created-at>"
                     + "    <updated-at type=\"datetime\">2018-04-12T19:54:16Z</updated-at>"
                     + "  </us-bank-account-verification>"
                     + "</verifications>"
                   + "</us-bank-account>";

        SimpleNodeWrapper node = SimpleNodeWrapper.parse(xml);
        UsBankAccount usBankAccount = new UsBankAccount(node);

        assertEquals(1, usBankAccount.getVerifications().size());
        assertEquals("123456789", usBankAccount.getRoutingNumber());
        assertEquals("1234", usBankAccount.getLast4());
        assertEquals("ch6byss", usBankAccount.getToken());
        assertEquals("Wells Fargo", usBankAccount.getBankName());
        assertEquals("Audrey", usBankAccount.getAccountHolderName());
        assertEquals("checking", usBankAccount.getAccountType());
        assertEquals(true, usBankAccount.isVerified());
    }
}
