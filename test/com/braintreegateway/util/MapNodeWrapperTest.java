package com.braintreegateway.util;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MapNodeWrapperTest {

    @Test
    public void getsHashFromSimpleXML() {
        MapNodeWrapper node = MapNodeWrapper.parse("<parent><child>value</child></parent>");

        String actual = MapUtils.mapToString(node.getMap());
        assertEquals("{child: value}", actual);
        assertEquals("parent", node.getElementName());
    }

    @Test
    public void moreNestedXml() {
        String xml = "<toplevel><foo type='array'><bar><greeting>hi</greeting><salutation>bye</salutation></bar><bar><greeting>hello</greeting></bar></foo></toplevel>";
        MapNodeWrapper node = MapNodeWrapper.parse(xml);
        assertEquals("toplevel", node.getElementName());
        assertEquals("{foo: [{greeting: hi, salutation: bye}, {greeting: hello}]}", MapUtils.mapToString(node.getMap()));
    }

    @Test
    public void findString() {
        String xml = "<toplevel><foo>bar</foo></toplevel>";
        NodeWrapper node = MapNodeWrapper.parse(xml);
        assertEquals("bar", node.findString("foo"));
    }

    @Test
    public void findStringWithNoMatchingElements() {
        String xml = "<toplevel><foo>bar</foo></toplevel>";
        NodeWrapper node = MapNodeWrapper.parse(xml);
        assertEquals(null, node.findString("blah"));
    }

    @Test
    public void findDate() {
        String xml = "<toplevel><created-at type=\"date\">2010-02-16</created-at></toplevel>";
        NodeWrapper node = MapNodeWrapper.parse(xml);
        Calendar expected = Calendar.getInstance();
        expected.set(2010, 1, 16);
        expected.setTimeZone(TimeZone.getTimeZone("UTC"));
        Calendar actual = node.findDate("created-at");
        assertEquals(2010, actual.get(Calendar.YEAR));
        assertEquals(Calendar.FEBRUARY, actual.get(Calendar.MONTH));
        assertEquals(16, actual.get(Calendar.DAY_OF_MONTH));
        assertEquals(TimeZone.getTimeZone("UTC"), actual.getTimeZone());
    }

    @Test
    public void findDateWithNoMatchingElement() {
        String xml = "<toplevel><foo>bar</foo></toplevel>";
        NodeWrapper node = MapNodeWrapper.parse(xml);
        assertEquals(null, node.findDate("created-at"));
    }

    @Test
    public void findDateTime() {
        String xml = "<toplevel><created-at type=\"datetime\">2010-02-16T16:32:07Z</created-at></toplevel>";
        NodeWrapper node = MapNodeWrapper.parse(xml);
        Calendar expected = Calendar.getInstance();
        expected.setTimeZone(TimeZone.getTimeZone("UTC"));
        expected.set(2010, 1, 16, 16, 32, 7);
        expected.set(Calendar.MILLISECOND, 0);
        Calendar actual = node.findDateTime("created-at");

        assertEquals(2010, actual.get(Calendar.YEAR));
        assertEquals(Calendar.FEBRUARY, actual.get(Calendar.MONTH));
        assertEquals(16, actual.get(Calendar.DAY_OF_MONTH));
        assertEquals(16, actual.get(Calendar.HOUR_OF_DAY));
        assertEquals(32, actual.get(Calendar.MINUTE));
        assertEquals(07, actual.get(Calendar.SECOND));
        assertEquals(TimeZone.getTimeZone("UTC"), actual.getTimeZone());
    }

    @Test
    public void findDateTimeWithNoMatchingElement() {
        String xml = "<toplevel><foo>bar</foo></toplevel>";
        NodeWrapper node = MapNodeWrapper.parse(xml);
        assertEquals(null, node.findDateTime("created-at"));
    }

    @Test
    public void findBigDecimal() {
        String xml = "<toplevel><amount>12.59</amount></toplevel>";
        NodeWrapper response = MapNodeWrapper.parse(xml);
        assertEquals(new BigDecimal("12.59"), response.findBigDecimal("amount"));
    }

    @Test
    public void findBigDecimalWithNoMatchingElement() {
        String xml = "<toplevel><amount>12.59</amount></toplevel>";
        NodeWrapper response = MapNodeWrapper.parse(xml);
        assertEquals(null, response.findBigDecimal("price"));
    }

    @Test
    public void findInteger() {
        String xml = "<toplevel><foo>4</foo></toplevel>";
        NodeWrapper node = MapNodeWrapper.parse(xml);
        assertEquals(new Integer(4), node.findInteger("foo"));
    }

    @Test
    public void findIntegerWithNoMatchingElements() {
        String xml = "<toplevel><foo>4</foo></toplevel>";
        NodeWrapper node = MapNodeWrapper.parse(xml);
        assertEquals(null, node.findInteger("blah"));
    }

//    @Test
//    public void findAll() {
//        String xml = "<toplevel><foo><bar><greeting>hi</greeting></bar><bar><greeting>hello</greeting></bar></foo></toplevel>";
//        NodeWrapper node = MapNodeWrapper.parse(xml);
//        List<NodeWrapper> nodes = node.findAll("foo/bar");
//        assertEquals(2, nodes.size());
//        assertEquals("hi", nodes.get(0).findString("greeting"));
//        assertEquals("hello", nodes.get(1).findString("greeting"));
//    }
//
//    @Test
//    public void findAllWithNoMatchingElement() {
//        String xml = "<toplevel></toplevel>";
//        assertTrue(MapNodeWrapper.parse(xml).findAll("foo/bar").isEmpty());
//    }
//
//    @Test
//    public void findAllWithMalformedXPath() {
//        String xml = "<toplevel></toplevel>";
//        assertTrue(MapNodeWrapper.parse(xml).findAll("foo/bar").isEmpty());
//    }
//
//    @Test
//    public void findFirst() {
//        String xml = "<toplevel><foo><bar><greeting>hi</greeting></bar><bar><greeting>hello</greeting></bar></foo></toplevel>";
//        NodeWrapper node = MapNodeWrapper.parse(xml).findFirst("foo/bar");
//        assertEquals("hi", node.findString("greeting"));
//    }
//
//    @Test
//    public void findFirstWithNoMatchingElement() {
//        String xml = "<toplevel></toplevel>";
//        assertNull(MapNodeWrapper.parse(xml).findFirst("foo/bar"));
//    }
//
//    @Test
//    public void findFirstWithMalformedXPath() {
//        String xml = "<toplevel></toplevel>";
//        assertNull(MapNodeWrapper.parse(xml).findFirst("$#busted"));
//    }
//
//    public void getElementName() {
//        String xml = "<toplevel></toplevel>";
//        assertEquals("toplevel", MapNodeWrapper.parse(xml).getElementName());
//    }
}
