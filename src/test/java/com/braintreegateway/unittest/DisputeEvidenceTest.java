package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.ParseException;

import org.junit.jupiter.api.Test;

import com.braintreegateway.DisputeEvidence;
import com.braintreegateway.testhelpers.CalendarTestUtils;
import com.braintreegateway.util.SimpleNodeWrapper;

public class DisputeEvidenceTest {

    @Test
    public void parsesAllFields() throws ParseException {
        SimpleNodeWrapper node = SimpleNodeWrapper.parse(
                "<evidence>" +
                "<created-at type=\"datetime\">2018-10-11T21:28:37Z</created-at>" +
                "<sent-to-processor-at type=\"date\">2018-10-12</sent-to-processor-at>" +
                "<comment>supporting document</comment>" +
                "<id>evidence-id</id>" +
                "<url>https://example.com/evidence.pdf</url>" +
                "<category>GENERAL</category>" +
                "<sequence-number>1</sequence-number>" +
                "</evidence>");

        DisputeEvidence evidence = new DisputeEvidence(node);

        assertAll("dispute evidence fields",
                () -> assertEquals(CalendarTestUtils.dateTime("2018-10-11T21:28:37Z"), evidence.getCreatedAt()),
                () -> assertEquals(CalendarTestUtils.date("2018-10-12"), evidence.getSentToProcessorAt()),
                () -> assertEquals("supporting document", evidence.getComment()),
                () -> assertEquals("evidence-id", evidence.getId()),
                () -> assertEquals("https://example.com/evidence.pdf", evidence.getUrl()),
                () -> assertEquals("GENERAL", evidence.getCategory()),
                () -> assertEquals("1", evidence.getSequenceNumber()));
    }
}
