package com.braintreegateway.unittest;

import com.braintreegateway.DisputeSearchRequest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DisputeSearchRequestTest {

    @Test
    public void evidenceSubmittableTest() {
        DisputeSearchRequest disputeSearchRequest = new DisputeSearchRequest();

        String expectedXml = "<search><evidence_submittable>true</evidence_submittable></search>";
        assertEquals(expectedXml, disputeSearchRequest.evidenceSubmittable().is("true").toXML());
    }
}
