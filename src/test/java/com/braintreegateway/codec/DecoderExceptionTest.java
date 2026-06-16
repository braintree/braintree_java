package com.braintreegateway.codec;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import com.braintreegateway.org.apache.commons.codec.DecoderException;

public class DecoderExceptionTest {

    @Test
    public void noArgConstructor() {
        assertNull(new DecoderException().getMessage());
    }

    @Test
    public void messageConstructor() {
        assertEquals("decode failed", new DecoderException("decode failed").getMessage());
    }

    @Test
    public void messageAndCauseConstructor() {
        RuntimeException cause = new RuntimeException("root cause");
        DecoderException ex = new DecoderException("decode failed", cause);
        assertEquals("decode failed", ex.getMessage());
        assertEquals(cause, ex.getCause());
    }

    @Test
    public void causeConstructor() {
        RuntimeException cause = new RuntimeException("root cause");
        DecoderException ex = new DecoderException(cause);
        assertEquals(cause, ex.getCause());
    }
}
