package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.braintreegateway.TransactionSearchRequest;

public class RangeNodeTest {

    @Test
    public void betweenStringRange() {
        TransactionSearchRequest search = new TransactionSearchRequest();
        search.amount().between("10.00", "20.00");

        String xml = search.toXML();
        assertTrue(xml.contains("<min>10.00</min>"), xml);
        assertTrue(xml.contains("<max>20.00</max>"), xml);
    }

    @Test
    public void betweenIntRange() {
        TransactionSearchRequest search = new TransactionSearchRequest();
        search.amount().between(10, 20);

        String xml = search.toXML();
        assertTrue(xml.contains("<min>10</min>"), xml);
        assertTrue(xml.contains("<max>20</max>"), xml);
    }

    @Test
    public void betweenBigDecimalRange() {
        TransactionSearchRequest search = new TransactionSearchRequest();
        search.amount().between(new BigDecimal("5.00"), new BigDecimal("15.00"));

        String xml = search.toXML();
        assertTrue(xml.contains("<min>5.00</min>"), xml);
        assertTrue(xml.contains("<max>15.00</max>"), xml);
    }

    @Test
    public void greaterThanOrEqualToString() {
        TransactionSearchRequest search = new TransactionSearchRequest();
        search.amount().greaterThanOrEqualTo("10.00");

        assertTrue(search.toXML().contains("<min>10.00</min>"));
    }

    @Test
    public void greaterThanOrEqualToInt() {
        TransactionSearchRequest search = new TransactionSearchRequest();
        search.amount().greaterThanOrEqualTo(10);

        assertTrue(search.toXML().contains("<min>10</min>"));
    }

    @Test
    public void greaterThanOrEqualToBigDecimal() {
        TransactionSearchRequest search = new TransactionSearchRequest();
        search.amount().greaterThanOrEqualTo(new BigDecimal("10.00"));

        assertTrue(search.toXML().contains("<min>10.00</min>"));
    }

    @Test
    public void lessThanOrEqualToString() {
        TransactionSearchRequest search = new TransactionSearchRequest();
        search.amount().lessThanOrEqualTo("20.00");

        assertTrue(search.toXML().contains("<max>20.00</max>"));
    }

    @Test
    public void lessThanOrEqualToInt() {
        TransactionSearchRequest search = new TransactionSearchRequest();
        search.amount().lessThanOrEqualTo(20);

        assertTrue(search.toXML().contains("<max>20</max>"));
    }

    @Test
    public void lessThanOrEqualToBigDecimal() {
        TransactionSearchRequest search = new TransactionSearchRequest();
        search.amount().lessThanOrEqualTo(new BigDecimal("20.00"));

        assertTrue(search.toXML().contains("<max>20.00</max>"));
    }

    @Test
    public void isString() {
        TransactionSearchRequest search = new TransactionSearchRequest();
        search.amount().is("10.00");

        assertTrue(search.toXML().contains("<is>10.00</is>"));
    }

    @Test
    public void isInt() {
        TransactionSearchRequest search = new TransactionSearchRequest();
        search.amount().is(10);

        assertTrue(search.toXML().contains("<is>10</is>"));
    }

    @Test
    public void isBigDecimal() {
        TransactionSearchRequest search = new TransactionSearchRequest();
        search.amount().is(new BigDecimal("10.00"));

        assertTrue(search.toXML().contains("<is>10.00</is>"));
    }
}
