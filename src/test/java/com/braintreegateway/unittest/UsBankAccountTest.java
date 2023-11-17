package com.braintreegateway.unittest;

import com.braintreegateway.testhelpers.CalendarTestUtils;
import com.braintreegateway.UsBankAccount;
import com.braintreegateway.util.SimpleNodeWrapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UsBankAccountTest {
    @Test
    public void testUsBankAccountAttributes() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String date = sdf.format(new Date());
        String xml = "<us-bank-account>"
                     + "<account-holder-name>Audrey</account-holder-name>"
                     + "<account-type>checking</account-type>"
                     + "<ach-mandate>"
                     + "  <accepted-at type=\"datetime\">" + date + "</accepted-at>"
                     + "  <text>example mandate text</text>"
                     + "</ach-mandate>"
                     + "<bank-name>Wells Fargo</bank-name>"
                     + "<business-name>Big Business</business-name>"
                     + "<created-at type=\"datetime\">" + date + "</created-at>"
                     + "<customer-id>1396526238</customer-id>"
                     + "<default type=\"boolean\">true</default>"
                     + "<description>Fargo Account</description>"
                     + "<first-name>John</first-name>"
                     + "<image-url>https://jsdk.docker.dev:9000/payment_method_logo/us_bank_account.png?environment=test</image-url>"
                     + "<last-4>1234</last-4>"
                     + "<last-name>Doe</last-name>"
                     + "<owner-id>123456</owner-id>"
                     + "<ownership-type>personal</ownership-type>"
                     + "<plaid-verified-at type=\"datetime\">" + date + "</plaid-verified-at>"
                     + "<routing-number>123456789</routing-number>"
                     + "<subscriptions type=\"array\">"
                     + "</subscriptions>"
                     + "<token>ch6byss</token>"
                     + "<updated-at type=\"datetime\">" + date + "</updated-at>"
                     + "<verifications type=\"array\">"
                     + "  <us-bank-account-verification>"
                     + "    <created-at type=\"datetime\">" + date + "</created-at>"
                     + "    <gateway-rejection-reason nil=\"true\"/>"
                     + "    <id>6f34vp3z</id>"
                     + "    <merchant-account-id>ygmxmpdxthqrrtfyisqahvclo</merchant-account-id>"
                     + "    <processor-response-code>1000</processor-response-code>"
                     + "    <processor-response-text>Approved</processor-response-text>"
                     + "    <status>verified</status>"
                     + "    <updated-at type=\"datetime\">" + date + "</updated-at>"
                     + "    <us-bank-account>"
                     + "      <account-holder-name nil=\"true\"/>"
                     + "      <account-type>checking</account-type>"
                     + "      <bank-name>Wells Fargo</bank-name>"
                     + "      <last-4>1234</last-4>"
                     + "      <routing-number>123456789</routing-number>"
                     + "      <token>ch6byss</token>"
                     + "    </us-bank-account>"
                     + "    <verification-determined-at type=\"datetime\">" + date + "</verification-determined-at>"
                     + "    <verification-method>independent_check</verification-method>"
                     + "  </us-bank-account-verification>"
                     + "</verifications>"
                     + "<verifiable type=\"boolean\">true</verifiable>"
                     + "<verified type=\"boolean\">true</verified>"
                   + "</us-bank-account>";

        SimpleNodeWrapper node = SimpleNodeWrapper.parse(xml);
        UsBankAccount usBankAccount = new UsBankAccount(node);

        assertEquals("Audrey", usBankAccount.getAccountHolderName());
        assertEquals("checking", usBankAccount.getAccountType());
        assertNotNull(usBankAccount.getAchMandate());
        assertEquals(CalendarTestUtils.date(date), usBankAccount.getAchMandate().getAcceptedAt());
        assertEquals("example mandate text", usBankAccount.getAchMandate().getText());
        assertEquals("Wells Fargo", usBankAccount.getBankName());
        assertEquals("Big Business", usBankAccount.getBusinessName());
        assertEquals(CalendarTestUtils.dateTime(date), usBankAccount.getCreatedAt());
        assertEquals("1396526238", usBankAccount.getCustomerId());
        assertTrue(usBankAccount.isDefault());
        assertEquals("Fargo Account", usBankAccount.getDescription());
        assertEquals("John", usBankAccount.getFirstName());
        assertEquals("https://jsdk.docker.dev:9000/payment_method_logo/us_bank_account.png?environment=test", usBankAccount.getImageUrl());
        assertEquals("1234", usBankAccount.getLast4());
        assertEquals("Doe", usBankAccount.getLastName());
        assertEquals("123456", usBankAccount.getOwnerId());
        assertEquals("personal", usBankAccount.getOwnershipType());
        assertEquals(CalendarTestUtils.dateTime(date), usBankAccount.getPlaidVerifiedAt());
        assertEquals("123456789", usBankAccount.getRoutingNumber());
        assertEquals(0, usBankAccount.getSubscriptions().size());
        assertEquals("ch6byss", usBankAccount.getToken());
        assertEquals(CalendarTestUtils.dateTime(date), usBankAccount.getUpdatedAt());
        assertEquals(1, usBankAccount.getVerifications().size());
        assertTrue(usBankAccount.isVerifiable());
        assertTrue(usBankAccount.isVerified());
    }
}
