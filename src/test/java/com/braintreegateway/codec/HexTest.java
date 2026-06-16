package com.braintreegateway.codec;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

import com.braintreegateway.org.apache.commons.codec.DecoderException;
import com.braintreegateway.org.apache.commons.codec.EncoderException;
import com.braintreegateway.org.apache.commons.codec.binary.Hex;

public class HexTest {

    @Test
    public void staticEncodeHexProducesLowercaseByDefault() {
        char[] encoded = Hex.encodeHex(new byte[]{(byte) 0xAB, (byte) 0xCD});

        assertArrayEquals(new char[]{'a', 'b', 'c', 'd'}, encoded);
    }

    @Test
    public void staticEncodeHexUppercase() {
        char[] encoded = Hex.encodeHex(new byte[]{(byte) 0xAB, (byte) 0xCD}, false);

        assertArrayEquals(new char[]{'A', 'B', 'C', 'D'}, encoded);
    }

    @Test
    public void staticEncodeHexStringRoundTrips() {
        byte[] original = "hello".getBytes(StandardCharsets.UTF_8);
        String hex = Hex.encodeHexString(original);

        assertEquals("68656c6c6f", hex);
    }

    @Test
    public void staticDecodeHexRoundTrips() throws DecoderException {
        byte[] decoded = Hex.decodeHex("68656c6c6f".toCharArray());

        assertArrayEquals("hello".getBytes(StandardCharsets.UTF_8), decoded);
    }

    @Test
    public void staticDecodeHexThrowsOnOddLength() {
        DecoderException e = assertThrows(DecoderException.class,
                () -> Hex.decodeHex("abc".toCharArray()));
        assertTrue(e.getMessage().contains("Odd number"));
    }

    @Test
    public void staticDecodeHexThrowsOnIllegalCharacter() {
        assertThrows(DecoderException.class,
                () -> Hex.decodeHex("ZZ".toCharArray()));
    }

    @Test
    public void defaultConstructorUsesUtf8() {
        Hex hex = new Hex();

        assertAll("default charset",
                () -> assertEquals(Hex.DEFAULT_CHARSET_NAME, hex.getCharsetName()),
                () -> assertTrue(hex.toString().contains(Hex.DEFAULT_CHARSET_NAME)));
    }

    @Test
    public void charsetConstructorUsesGivenCharset() {
        Hex hex = new Hex("ISO-8859-1");

        assertEquals("ISO-8859-1", hex.getCharsetName());
    }

    @Test
    public void instanceEncodeByteArray() {
        byte[] encoded = new Hex().encode(new byte[]{(byte) 0xAB});

        assertArrayEquals("ab".getBytes(StandardCharsets.UTF_8), encoded);
    }

    @Test
    public void instanceEncodeObjectFromString() throws EncoderException {
        Object result = new Hex().encode((Object) "hi");

        assertTrue(result instanceof char[]);
    }

    @Test
    public void instanceEncodeObjectThrowsForBadType() {
        assertThrows(EncoderException.class, () -> new Hex().encode((Object) Integer.valueOf(1)));
    }

    @Test
    public void instanceDecodeByteArray() throws DecoderException {
        byte[] decoded = new Hex().decode("68656c6c6f".getBytes(StandardCharsets.UTF_8));

        assertArrayEquals("hello".getBytes(StandardCharsets.UTF_8), decoded);
    }

    @Test
    public void instanceDecodeObjectFromString() throws DecoderException {
        byte[] decoded = (byte[]) new Hex().decode((Object) "68656c6c6f");

        assertArrayEquals("hello".getBytes(StandardCharsets.UTF_8), decoded);
    }

    @Test
    public void instanceDecodeObjectFromCharArray() throws DecoderException {
        byte[] decoded = (byte[]) new Hex().decode((Object) "68656c6c6f".toCharArray());

        assertArrayEquals("hello".getBytes(StandardCharsets.UTF_8), decoded);
    }

    @Test
    public void instanceDecodeObjectThrowsForBadType() {
        assertThrows(DecoderException.class, () -> new Hex().decode((Object) Integer.valueOf(1)));
    }

    @Test
    public void instanceDecodeByteArrayThrowsDecoderExceptionForInvalidCharset() {
        assertThrows(DecoderException.class,
                () -> new Hex("INVALID-CHARSET").decode("ab".getBytes(StandardCharsets.UTF_8)));
    }

    @Test
    public void instanceEncodeObjectThrowsEncoderExceptionForInvalidCharset() {
        assertThrows(EncoderException.class,
                () -> new Hex("INVALID-CHARSET").encode((Object) "hello"));
    }
}
