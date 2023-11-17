package com.braintreegateway.unittest;

import com.braintreegateway.testhelpers.CalendarTestUtils;
import com.braintreegateway.UsBankAccountDetails;
import com.braintreegateway.util.SimpleNodeWrapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UsBankAccountDetailsTest {
    @Test
    public void testUsBankAccountDetailsAttributes() throws ParseException {
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
                     + "<first-name>John</first-name>"
                     + "<global-id>2211</global-id>"
                     + "<image-url>https://jsdk.docker.dev:9000/payment_method_logo/us_bank_account.png?environment=test</image-url>"
                     + "<last-4>1234</last-4>"
                     + "<last-name>Doe</last-name>"
                     + "<ownership-type>personal</ownership-type>"
                     + "<routing-number>123456789</routing-number>"
                     + "<token>ch6byss</token>"
                     + "<verified type=\"boolean\">true</verified>"
                   + "</us-bank-account>";

        SimpleNodeWrapper node = SimpleNodeWrapper.parse(xml);
        UsBankAccountDetails usBankAccountDetails = new UsBankAccountDetails(node);

        assertEquals("Audrey", usBankAccountDetails.getAccountHolderName());
        assertEquals("checking", usBankAccountDetails.getAccountType());
        assertNotNull(usBankAccountDetails.getAchMandate());
        assertEquals(CalendarTestUtils.date(date), usBankAccountDetails.getAchMandate().getAcceptedAt());
        assertEquals("example mandate text", usBankAccountDetails.getAchMandate().getText());
        assertEquals("Wells Fargo", usBankAccountDetails.getBankName());
        assertEquals("Big Business", usBankAccountDetails.getBusinessName());
        assertEquals("John", usBankAccountDetails.getFirstName());
        assertEquals("2211", usBankAccountDetails.getGlobalId());
        assertEquals("https://jsdk.docker.dev:9000/payment_method_logo/us_bank_account.png?environment=test", usBankAccountDetails.getImageUrl());
        assertEquals("1234", usBankAccountDetails.getLast4());
        assertEquals("Doe", usBankAccountDetails.getLastName());
        assertEquals("personal", usBankAccountDetails.getOwnershipType());
        assertEquals("123456789", usBankAccountDetails.getRoutingNumber());
        assertEquals("ch6byss", usBankAccountDetails.getToken());
        assertTrue(usBankAccountDetails.isVerified());
    }
}
