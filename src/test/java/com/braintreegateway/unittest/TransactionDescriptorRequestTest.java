package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.braintreegateway.TransactionDescriptorRequest;
import com.braintreegateway.TransactionRequest;

public class TransactionDescriptorRequestTest {

    @Test
    public void buildsXmlAndChainsBackToParent() {
        TransactionRequest parent = new TransactionRequest();
        TransactionDescriptorRequest request = new TransactionDescriptorRequest(parent);

        TransactionRequest returned = request
                .name("company*product")
                .phone("1234567890")
                .url("example.com")
                .done();

        assertSame(parent, returned);

        String xml = request.toXML();
        assertTrue(xml.contains("<descriptor>"), xml);
        assertTrue(xml.contains("<name>company*product</name>"), xml);
        assertTrue(xml.contains("<phone>1234567890</phone>"), xml);
        assertTrue(xml.contains("<url>example.com</url>"), xml);
    }
}
