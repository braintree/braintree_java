package com.braintreegateway.unittest;

import com.braintreegateway.SepaDirectDebitAccountRequest;
import com.braintreegateway.SepaDirectDebitAccountOptionsRequest;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SepaDirectDebitAccountRequestTest {

    @Test
    public void token() {
        SepaDirectDebitAccountRequest sepaDirectDebitAccountRequest = new SepaDirectDebitAccountRequest();
        sepaDirectDebitAccountRequest.token("123");

        String expectedXml = "<sepaDebitAccount><token>123</token></sepaDebitAccount>";
        assertEquals(expectedXml, sepaDirectDebitAccountRequest.toXML());
    }

    @Test
    public void options() {
        SepaDirectDebitAccountRequest sepaDirectDebitAccountRequest = new SepaDirectDebitAccountRequest();
        SepaDirectDebitAccountOptionsRequest options = sepaDirectDebitAccountRequest.options();
        options.makeDefault(true).done();

        String expectedXml = "<sepaDebitAccount><options><makeDefault>true</makeDefault></options></sepaDebitAccount>";
        assertEquals(expectedXml, sepaDirectDebitAccountRequest.toXML());
    }
}
