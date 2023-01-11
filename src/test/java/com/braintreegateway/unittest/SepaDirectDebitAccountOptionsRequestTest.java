package com.braintreegateway.unittest;

import com.braintreegateway.SepaDirectDebitAccountRequest;
import com.braintreegateway.SepaDirectDebitAccountOptionsRequest;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SepaDirectDebitAccountOptionsRequestTest {

    @Test
    public void makeDefault() {
        SepaDirectDebitAccountOptionsRequest sepaDirectDebitAccountOptionsRequest = new SepaDirectDebitAccountOptionsRequest();
        sepaDirectDebitAccountOptionsRequest.makeDefault(true);
        assertEquals(true, sepaDirectDebitAccountOptionsRequest.getMakeDefault());

        String expectedXml = "<options><makeDefault>true</makeDefault></options>";
        assertEquals(expectedXml, sepaDirectDebitAccountOptionsRequest.toXML());
    }

    @Test
    public void done() {
        SepaDirectDebitAccountRequest sepaDirectDebitAccountRequest = new SepaDirectDebitAccountRequest();
        SepaDirectDebitAccountOptionsRequest sepaDirectDebitAccountOptionsRequest = new SepaDirectDebitAccountOptionsRequest(sepaDirectDebitAccountRequest);

        SepaDirectDebitAccountRequest parent = sepaDirectDebitAccountOptionsRequest.done();
        assertEquals(parent, sepaDirectDebitAccountRequest);

        String expectedXml = "<options></options>";
        assertEquals(expectedXml, sepaDirectDebitAccountOptionsRequest.toXML());
    }

}
