package com.braintreegateway.util;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class XmlNodeWrapperTest {

    @Test
    public void findString() {
        NodeWrapper node = new XmlNodeWrapper("<toplevel><foo>bar</foo></toplevel>");
        Assert.assertEquals("bar", node.findString("foo"));
    }

    @Test
    public void findStringWithNoMatchingElements() {
        NodeWrapper node = new XmlNodeWrapper("<toplevel><foo>bar</foo></toplevel>");
        Assert.assertEquals(null, node.findString("blah"));
    }

    @Test
    public void findStringWithBustedXPathExpression() {
        NodeWrapper node = new XmlNodeWrapper("<toplevel><foo>bar</foo></toplevel>");
        Assert.assertEquals(null, node.findString("$busted"));
    }

    @Test
    public void findDate() {
        String xml = "<toplevel><created-at type=\"date\">2010-02-16</created-at></toplevel>";
        NodeWrapper node = new XmlNodeWrapper(xml);
        Calendar expected = Calendar.getInstance();
        expected.set(2010, 1, 16);
        expected.setTimeZone(TimeZone.getTimeZone("UTC"));
        Calendar actual = node.findDate("created-at");
        Assert.assertEquals(2010, actual.get(Calendar.YEAR));
        Assert.assertEquals(Calendar.FEBRUARY, actual.get(Calendar.MONTH));
        Assert.assertEquals(16, actual.get(Calendar.DAY_OF_MONTH));
        Assert.assertEquals(TimeZone.getTimeZone("UTC"), actual.getTimeZone());
    }
    
    @Test
    public void findDateWithNoMatchingElement() {
        String xml = "<toplevel><foo>bar</foo></toplevel>";
        NodeWrapper node = new XmlNodeWrapper(xml);
        Assert.assertEquals(null, node.findDate("created-at"));
    }

    @Test
    public void findDateTime() {
        String xml = "<toplevel><created-at type=\"datetime\">2010-02-16T16:32:07Z</created-at></toplevel>";
        NodeWrapper node = new XmlNodeWrapper(xml);
        Calendar expected = Calendar.getInstance();
        expected.setTimeZone(TimeZone.getTimeZone("UTC"));
        expected.set(2010, 1, 16, 16, 32, 7);
        expected.set(Calendar.MILLISECOND, 0);
        Calendar actual = node.findDateTime("created-at");
        
        Assert.assertEquals(2010, actual.get(Calendar.YEAR));
        Assert.assertEquals(Calendar.FEBRUARY, actual.get(Calendar.MONTH));
        Assert.assertEquals(16, actual.get(Calendar.DAY_OF_MONTH));
        Assert.assertEquals(16, actual.get(Calendar.HOUR_OF_DAY));
        Assert.assertEquals(32, actual.get(Calendar.MINUTE));
        Assert.assertEquals(07, actual.get(Calendar.SECOND));
        Assert.assertEquals(TimeZone.getTimeZone("UTC"), actual.getTimeZone());
    }

    @Test
    public void findDateTimeWithNoMatchingElement() {
        String xml = "<toplevel><foo>bar</foo></toplevel>";
        NodeWrapper node = new XmlNodeWrapper(xml);
        Assert.assertEquals(null, node.findDateTime("created-at"));
    }

    @Test
    public void findBigDecimal() {
        String xml = "<toplevel><amount>12.59</amount></toplevel>";
        NodeWrapper response = new XmlNodeWrapper(xml);
        Assert.assertEquals(new BigDecimal("12.59"), response.findBigDecimal("amount"));
    }

    @Test
    public void findBigDecimalWithNoMatchingElement() {
        String xml = "<toplevel><amount>12.59</amount></toplevel>";
        NodeWrapper response = new XmlNodeWrapper(xml);
        Assert.assertEquals(null, response.findBigDecimal("price"));
    }

    @Test
    public void findBigDecimalWithMalformedXPath() {
        String xml = "<toplevel><amount>12.59</amount></toplevel>";
        NodeWrapper response = new XmlNodeWrapper(xml);
        Assert.assertEquals(null, response.findBigDecimal("$##@busted"));
    }

    @Test
    public void findInteger() {
        String xml = "<toplevel><foo>4</foo></toplevel>";
        NodeWrapper node = new XmlNodeWrapper(xml);
        Assert.assertEquals(new Integer(4), node.findInteger("foo"));
    }

    @Test
    public void findIntegerWithNoMatchingElements() {
        String xml = "<toplevel><foo>4</foo></toplevel>";
        NodeWrapper node = new XmlNodeWrapper(xml);
        Assert.assertEquals(null, node.findInteger("blah"));
    }

    @Test
    public void findIntegerWithBustedXPathExpression() {
        String xml = "<toplevel><foo>4</foo></toplevel>";
        NodeWrapper node = new XmlNodeWrapper(xml);
        Assert.assertEquals(null, node.findInteger("$busted"));
    }

    @Test
    public void findAll() {
        String xml = "<toplevel><foo><bar><greeting>hi</greeting></bar><bar><greeting>hello</greeting></bar></foo></toplevel>";
        NodeWrapper node = new XmlNodeWrapper(xml);
        List<NodeWrapper> nodes = node.findAll("foo/bar");
        Assert.assertEquals(2, nodes.size());
        Assert.assertEquals("hi", nodes.get(0).findString("greeting"));
        Assert.assertEquals("hello", nodes.get(1).findString("greeting"));
    }

    @Test
    public void findAllWithNoMatchingElement() {
        String xml = "<toplevel></toplevel>";
        Assert.assertTrue(new XmlNodeWrapper(xml).findAll("foo/bar").isEmpty());
    }

    @Test
    public void findAllWithMalformedXPath() {
        String xml = "<toplevel></toplevel>";
        Assert.assertTrue(new XmlNodeWrapper(xml).findAll("foo/bar").isEmpty());
    }

    @Test
    public void findFirst() {
        String xml = "<toplevel><foo><bar><greeting>hi</greeting></bar><bar><greeting>hello</greeting></bar></foo></toplevel>";
        NodeWrapper node = new XmlNodeWrapper(xml).findFirst("foo/bar");
        Assert.assertEquals("hi", node.findString("greeting"));
    }

    @Test
    public void findFirstWithNoMatchingElement() {
        String xml = "<toplevel></toplevel>";
        Assert.assertNull(new XmlNodeWrapper(xml).findFirst("foo/bar"));
    }

    @Test
    public void findFirstWithMalformedXPath() {
        String xml = "<toplevel></toplevel>";
        Assert.assertNull(new XmlNodeWrapper(xml).findFirst("$#busted"));
    }

    @Test
    public void getElementName() {
        String xml = "<toplevel></toplevel>";
        Assert.assertEquals("toplevel", new XmlNodeWrapper(xml).getElementName());
    }

    @Test
    public void getElementNameForNestedResponse() {
        String xml = "<toplevel><foo>bar</foo></toplevel>";
        Assert.assertEquals("foo", new XmlNodeWrapper(xml).findFirst("foo").getElementName());
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
        XmlNodeWrapper node = new XmlNodeWrapper(xml);
        assertEquals("{id: invalid id, payment_method_token: 99s6, plan_id: integration_trialless_plan}",
                StringUtils.toString(node.findFirst("params").getFormParameters()));
        assertEquals("{params[id]: invalid id, params[payment_method_token]: 99s6, params[plan_id]: integration_trialless_plan}",
                StringUtils.toString(node.getFormParameters()));
    }
}
