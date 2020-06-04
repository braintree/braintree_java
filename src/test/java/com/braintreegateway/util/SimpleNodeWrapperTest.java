package com.braintreegateway.util;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import static org.junit.Assert.*;

public class SimpleNodeWrapperTest {

    @Test
    public void getsHashFromSimpleXML() {
        SimpleNodeWrapper node = SimpleNodeWrapper.parse("<parent><child>value</child></parent>");

        String actual = StringUtils.toString(node);
        assertEquals("<parent content=[<child content=[value]>]>", actual);
        assertEquals("parent", node.getElementName());
    }

    @Test
    public void parsingFullXmlDoc() {
        SimpleNodeWrapper node = SimpleNodeWrapper.parse("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<add-on>\n" +
                "  <amount>100.00</amount>\n" +
                "  <foo nil='true'></foo>\n" +
                "</add-on>");

        assertEquals("<add-on content=[<amount content=[100.00]>, <foo attributes={nil: true} content=[null]>]>", StringUtils.toString(node));
    }

    @Test
    public void parsingXmlWithListAtRoot() {
        SimpleNodeWrapper node = SimpleNodeWrapper.parse("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<add-ons type=\"array\">\n" +
                "  <add-on>\n" +
                "    <amount>10.00</amount>\n" +
                "  </add-on>\n" +
                "</add-ons>");

        assertEquals("<add-ons attributes={type: array} content=[<add-on content=[<amount content=[10.00]>]>]>", StringUtils.toString(node));
    }

    @Test
    public void parsingXmlWithNilValuesWithoutNilAttr() {
        SimpleNodeWrapper node = SimpleNodeWrapper.parse("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<customer>\n" +
                "  <id>884969</id>\n" +
                "  <merchant-id>integration_merchant_id</merchant-id>\n" +
                "  <first-name nil=\"true\"></first-name>\n" +
                "  <custom-fields>\n" +
                "  </custom-fields>\n" +
                "</customer>");

        assertEquals("<customer content=[<id content=[884969]>, <merchant-id content=[integration_merchant_id]>, <first-name attributes={nil: true} content=[null]>, <custom-fields content=[]>]>", StringUtils.toString(node));
    }

    @Test
    public void moreNestedXml() {
        String xml = "<toplevel><foo type='array'><bar><greeting>hi</greeting><salutation>bye</salutation></bar><bar><greeting>hello</greeting></bar></foo></toplevel>";
        SimpleNodeWrapper node = SimpleNodeWrapper.parse(xml);
        assertEquals("toplevel", node.getElementName());
        assertEquals("<toplevel content=[<foo attributes={type: array} content=[" +
                "<bar content=[<greeting content=[hi]>, <salutation content=[bye]>]>, " +
                "<bar content=[<greeting content=[hello]>]>]>]>", StringUtils.toString(node));
    }

    @Test
    public void findDot() {
        NodeWrapper node = SimpleNodeWrapper.parse("<toplevel>bar</toplevel>");
        assertEquals("bar", node.findString("."));
    }

    @Test
    public void findString() {
        String xml = "<toplevel><foo>bar</foo></toplevel>";
        NodeWrapper node = SimpleNodeWrapper.parse(xml);
        assertEquals("bar", node.findString("foo"));
    }

    @Test
    public void findStringForNull() {
        String xml = "<toplevel><foo nil='true'></foo></toplevel>";
        NodeWrapper node = SimpleNodeWrapper.parse(xml);
        assertEquals(null, node.findString("foo"));
    }

    @Test
    public void findStringWithNoMatchingElements() {
        String xml = "<toplevel><foo>bar</foo></toplevel>";
        NodeWrapper node = SimpleNodeWrapper.parse(xml);
        assertEquals(null, node.findString("blah"));
    }

    @Test
    public void findDate() {
        String xml = "<toplevel><created-at type=\"date\">2010-02-16</created-at></toplevel>";
        NodeWrapper node = SimpleNodeWrapper.parse(xml);
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
        NodeWrapper node = SimpleNodeWrapper.parse(xml);
        assertEquals(null, node.findDate("created-at"));
    }

    @Test
    public void findDateTime() {
        String xml = "<toplevel><created-at type=\"datetime\">2010-02-16T16:32:07Z</created-at></toplevel>";
        NodeWrapper node = SimpleNodeWrapper.parse(xml);
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
        NodeWrapper node = SimpleNodeWrapper.parse(xml);
        assertEquals(null, node.findDateTime("created-at"));
    }

    @Test
    public void findBigDecimal() {
        String xml = "<toplevel><amount>12.59</amount></toplevel>";
        NodeWrapper response = SimpleNodeWrapper.parse(xml);
        assertEquals(new BigDecimal("12.59"), response.findBigDecimal("amount"));
    }

    @Test
    public void findBigDecimalWithNoMatchingElement() {
        String xml = "<toplevel><amount>12.59</amount></toplevel>";
        NodeWrapper response = SimpleNodeWrapper.parse(xml);
        assertEquals(null, response.findBigDecimal("price"));
    }

    @Test
    public void findInteger() {
        String xml = "<toplevel><foo>4</foo></toplevel>";
        NodeWrapper node = SimpleNodeWrapper.parse(xml);
        assertEquals(Integer.valueOf(4), node.findInteger("foo"));
    }

    @Test
    public void findIntegerWithNoMatchingElements() {
        String xml = "<toplevel><foo>4</foo></toplevel>";
        NodeWrapper node = SimpleNodeWrapper.parse(xml);
        assertEquals(null, node.findInteger("blah"));
    }

    @Test
    public void handleXmlCharactersCorrectly() {
        String xml = "<credit-card>\n" +
                "  <bin>510510</bin>\n" +
                "  <cardholder-name>Special Chars &lt;&gt;&amp;&quot;'</cardholder-name>\n" +
                "</credit-card>";
        NodeWrapper node = SimpleNodeWrapper.parse(xml);
        assertEquals("Special Chars <>&\"'", node.findString("cardholder-name"));
    }

    @Test
    public void findAll() {
        String xml = "<toplevel><foo type='array'><bar><greeting>hi</greeting></bar><bar><greeting>hello</greeting></bar></foo></toplevel>";
        NodeWrapper node = SimpleNodeWrapper.parse(xml);
        List<NodeWrapper> nodes = node.findAll("foo/bar");
        assertEquals(2, nodes.size());
        assertEquals("hi", nodes.get(0).findString("greeting"));
        assertEquals("hello", nodes.get(1).findString("greeting"));
    }

    @Test
    public void findAllWithStar() {
        String xml = "<toplevel><foo type='array'><bar><greeting>hi</greeting></bar><bar><greeting>hello</greeting></bar></foo></toplevel>";
        NodeWrapper node = SimpleNodeWrapper.parse(xml);
        List<NodeWrapper> nodes = node.findAll("foo/*");
        assertEquals(2, nodes.size());
        assertEquals("hi", nodes.get(0).findString("greeting"));
        assertEquals("hello", nodes.get(1).findString("greeting"));
    }

    @Test
    public void findAllWithNoMatchingElement() {
        String xml = "<toplevel></toplevel>";
        assertTrue(SimpleNodeWrapper.parse(xml).findAll("foo/bar").isEmpty());
    }

    @Test
    public void findFirst() {
        String xml = "<toplevel><foo type='array'><bar><greeting>hi</greeting></bar><bar><greeting>hello</greeting></bar></foo></toplevel>";
        NodeWrapper node = SimpleNodeWrapper.parse(xml).findFirst("foo/bar");
        assertEquals("hi", node.findString("greeting"));
    }

    @Test
    public void findFirstWithNoMatchingElement() {
        String xml = "<toplevel></toplevel>";
        assertNull(SimpleNodeWrapper.parse(xml).findFirst("foo/bar"));
    }

    @Test
    public void parameters() {
        String xml = "<api-error-response>" +
                "  <params>\n" +
                "    <payment-method-token>99s6</payment-method-token>\n" +
                "    <id>invalid id</id>\n" +
                "    <plan-id>integration_trialless_plan</plan-id>\n" +
                "  </params>\n" +
                "</api-error-response>";
        SimpleNodeWrapper node = SimpleNodeWrapper.parse(xml);
        assertEquals("{id: invalid id, payment_method_token: 99s6, plan_id: integration_trialless_plan}",
                StringUtils.toString(node.findFirst("params").getFormParameters()));
        assertEquals("{params[id]: invalid id, params[payment_method_token]: 99s6, params[plan_id]: integration_trialless_plan}",
                StringUtils.toString(node.getFormParameters()));
    }

    @Test
    public void nestesParameters() {
        String xml = "<api-error-response>" +
                "  <ps>\n" +
                "    <child>\n" +
                "       <grandchild>sonny</grandchild>\n" +
                "    </child>\n" +
                "    <id>invalid id</id>\n" +
                "  </ps>\n" +
                "</api-error-response>";
        SimpleNodeWrapper node = SimpleNodeWrapper.parse(xml);
        assertEquals("{child[grandchild]: sonny, id: invalid id}",
                StringUtils.toString(node.findFirst("ps").getFormParameters()));
    }

    @Test
    public void isBlankWithBlankNode() {
        String xml = "<toplevel><foo nil=\"true\"/></toplevel>";
        NodeWrapper node = SimpleNodeWrapper.parse(xml).findFirst("foo");
        assertTrue(node.isBlank());
    }

    @Test
    public void isBlankWithPresentNode() {
        String xml = "<toplevel><foo>test</foo></toplevel>";
        NodeWrapper node = SimpleNodeWrapper.parse(xml).findFirst("foo");
        assertFalse(node.isBlank());
    }
}
