package com.braintreegateway.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import junit.framework.Assert;

import org.junit.Test;

import com.braintreegateway.util.StringUtils;

public class StringUtilsTest {

    @Test
    public void underscoreRetursNullForNullString() {
        Assert.assertNull(StringUtils.underscore(null));
    }

    @Test
    public void underscoreRetursAlreadyUnderscoredString() {
        Assert.assertEquals("foo_bar", StringUtils.underscore("foo_bar"));
    }

    @Test
    public void underscoreWorksForCamelCaseString() {
        Assert.assertEquals("first_name", StringUtils.underscore("firstName"));
    }

    @Test
    public void underscoreWorksForDasherizedString() {
        Assert.assertEquals("first_name", StringUtils.underscore("first-name"));
    }

    @Test
    public void dasherizeRetursNullForNullString() {
        Assert.assertNull(StringUtils.dasherize(null));
    }

    @Test
    public void dasherizeRetursAlreadyUnderscoredString() {
        Assert.assertEquals("foo-bar", StringUtils.dasherize("foo-bar"));
    }

    @Test
    public void dasherizeWorksForCamelCaseString() {
        Assert.assertEquals("first-name", StringUtils.dasherize("firstName"));
    }

    @Test
    public void dasherizeWorksForUnderscoreString() {
        Assert.assertEquals("first-name", StringUtils.dasherize("first_name"));
    }

    @Test
    public void nullIfEmptyReturnsNullForNull() {
        Assert.assertNull(StringUtils.nullIfEmpty(null));
    }

    @Test
    public void nullIfEmptyReturnsStringForNonNull() {
        Assert.assertEquals("hello", StringUtils.nullIfEmpty("hello"));
    }

    @Test
    public void nullIfEmptyReturnsNullForEmptyString() {
        Assert.assertNull(StringUtils.nullIfEmpty(""));
    }

    @Test
    public void streamToStringForOneLine() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("hello world".getBytes());
        Assert.assertEquals("hello world", StringUtils.inputStreamToString(inputStream));
    }

    @Test
    public void streamToStringForMultilineStrings() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("foo\r\nbar\nbaz".getBytes());
        Assert.assertEquals("foo\r\nbar\nbaz", StringUtils.inputStreamToString(inputStream));
    }
}
