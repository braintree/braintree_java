package com.braintreegateway.util;

import com.braintreegateway.CreditCard;
import com.braintreegateway.Transaction;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class StringUtilsTest {

    @Test
    public void underscoreRetursNullForNullString() {
        assertNull(StringUtils.underscore(null));
    }

    @Test
    public void underscoreRetursAlreadyUnderscoredString() {
        assertEquals("foo_bar", StringUtils.underscore("foo_bar"));
    }

    @Test
    public void underscoreWorksForCamelCaseString() {
        assertEquals("first_name", StringUtils.underscore("firstName"));
    }

    @Test
    public void underscoreWorksForDasherizedString() {
        assertEquals("first_name", StringUtils.underscore("first-name"));
    }

    @Test
    public void underscoreWorksForPascalCasedString() {
        assertEquals("first_name", StringUtils.underscore("FirstName"));
    }

    @Test
    public void dasherizeReturnsNullForNullString() {
        assertNull(StringUtils.dasherize(null));
    }

    @Test
    public void dasherizeReturnsAlreadyDasherizedString() {
        assertEquals("foo-bar", StringUtils.dasherize("foo-bar"));
    }

    @Test
    public void dasherizeWorksForCamelCaseString() {
        assertEquals("first-name", StringUtils.dasherize("firstName"));
    }

    @Test
    public void dasherizeWorksForCamelCaseStringWithLeadingUppercaseLetter() {
        assertEquals("first-name", StringUtils.dasherize("FirstName"));
    }

    @Test
    public void dasherizeWorksForUnderscoreString() {
        assertEquals("first-name", StringUtils.dasherize("first_name"));
    }

    @Test
    public void nullIfEmptyReturnsNullForNull() {
        assertNull(StringUtils.nullIfEmpty(null));
    }

    @Test
    public void nullIfEmptyReturnsStringForNonNull() {
        assertEquals("hello", StringUtils.nullIfEmpty("hello"));
    }

    @Test
    public void nullIfEmptyReturnsNullForEmptyString() {
        assertNull(StringUtils.nullIfEmpty(""));
    }

    @Test
    public void streamToStringForOneLine() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("hello world".getBytes());
        assertEquals("hello world", StringUtils.inputStreamToString(inputStream));
    }

    @Test
    public void streamToStringForMultilineStrings() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("foo\r\nbar\nbaz".getBytes());
        assertEquals("foo\r\nbar\nbaz", StringUtils.inputStreamToString(inputStream));
    }

    @Test
    public void classToXMLName() {
        assertEquals("credit-card", StringUtils.classToXMLName(CreditCard.class));
        assertEquals("transaction", StringUtils.classToXMLName(Transaction.class));
    }

    @Test
    public void join() throws Exception {
        String result = StringUtils.join(",", "one", "two", "three");
        assertEquals("one,two,three", result);
        result = StringUtils.join(",", "one", "two", "three");
        assertEquals("one,two,three", result);

        result = StringUtils.join(",");
        assertEquals("", result);

        result = StringUtils.join(",", "one");
        assertEquals("one", result);
    }

}
