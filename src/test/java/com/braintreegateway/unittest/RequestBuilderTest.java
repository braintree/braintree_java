package com.braintreegateway.unittest;

import com.braintreegateway.RequestBuilder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RequestBuilderTest {

    @Test
    public void toXml() {
        RequestBuilder builder = new RequestBuilder("myparent");
        builder.addElement("name", "value");
        String result = builder.toXML();
        assertEquals("<myparent><name>value</name></myparent>", result);
    }

    // reveal protected methods for test
    private static class Open extends RequestBuilder {
        public Open() {
            super("open");
        }
        public static String publicBuildXmlElement(String name, Object object) {
            return buildXMLElement(name, object);
        }
        public static String formatMap(String name, Map<String,Object> map) {
            return formatAsXML(name, map);
        }
    }

    @Test
    public void list() {
       Open builder = new Open();
       List<String> items = new ArrayList<String>();
       items.add("Chicken");
       items.add("Rabbit");
       String element = builder.publicBuildXmlElement("animals", items);
       assertEquals("<animals type=\"array\"><item>Chicken</item><item>Rabbit</item></animals>", element);
    }

    @Test
    public void map() {
        Open builder = new Open();
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("color", "green");
        map.put("insect", "bee");
        String element = builder.formatMap("examples", map);
        assertEquals("<examples><color>green</color><insect>bee</insect></examples>", element);
    }
}
