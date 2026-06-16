package com.braintreegateway.codec;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import com.braintreegateway.org.apache.commons.codec.EncoderException;

public class EncoderExceptionTest {

    @Test
    public void noArgConstructor() {
        assertNull(new EncoderException().getMessage());
    }

    @Test
    public void messageConstructor() {
        assertEquals("encode failed", new EncoderException("encode failed").getMessage());
    }

    @Test
    public void messageAndCauseConstructor() {
        RuntimeException cause = new RuntimeException("root cause");
        EncoderException ex = new EncoderException("encode failed", cause);
        assertEquals("encode failed", ex.getMessage());
        assertEquals(cause, ex.getCause());
    }

    @Test
    public void causeConstructor() {
        RuntimeException cause = new RuntimeException("root cause");
        EncoderException ex = new EncoderException(cause);
        assertEquals(cause, ex.getCause());
    }
}
