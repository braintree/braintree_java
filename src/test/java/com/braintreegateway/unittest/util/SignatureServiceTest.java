package com.braintreegateway.unittest.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import com.braintreegateway.util.Hasher;
import com.braintreegateway.util.SignatureService;

public class SignatureServiceTest {

    @Test
    public void signReturnsHashPipeQuery() {
        Hasher hasher = mock(Hasher.class);
        when(hasher.hmacHash("secret-key", "the-query")).thenReturn("abc123");

        String signed = new SignatureService("secret-key", hasher).sign("the-query");

        assertEquals("abc123|the-query", signed);
        verify(hasher).hmacHash("secret-key", "the-query");
    }

    @Test
    public void signedStringContainsPipeDelimiter() {
        Hasher hasher = mock(Hasher.class);
        when(hasher.hmacHash("k", "q")).thenReturn("hash");

        String signed = new SignatureService("k", hasher).sign("q");

        assertTrue(signed.contains("|"), signed);
        assertEquals("hash|q", signed);
    }
}
