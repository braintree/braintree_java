package com.braintreegateway.util;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.StringReader;
import java.util.*;

public class MapNodeWrapper extends NodeWrapper {
    private Map<String, Object> map;
    private String name;

    private MapNodeWrapper() {
    }

    public static MapNodeWrapper parse(String xml) {
        try {
            InputSource source = new InputSource(new StringReader(xml));
            SAXParserFactory parserFactory = SAXParserFactory.newInstance();
            SAXParser parser = parserFactory.newSAXParser();
            MapNodeHandler handler = new MapNodeHandler();
            parser.parse(source, handler);
            return handler.root;
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    @Override
    public List<NodeWrapper> findAll(String expression) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public NodeWrapper findFirst(String expression) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String findString(String expression) {
        Object value = map.get(expression);
        if (value == null)
            return null;
        else
            return value.toString();
    }

    @Override
    public String getElementName() {
        return name;
    }

    @Override
    public boolean isSuccess() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Map<String, String> findMap(String expression) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Map<String, String> getFormParameters() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Map<String, Object> getMap() {
        return map;
    }

    private static class MapNodeHandler extends DefaultHandler {
        private Stack<Object> stack = new Stack<Object>();
        public MapNodeWrapper root;

        private MapNodeHandler() {
            root = new MapNodeWrapper();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            boolean first = stack.empty();
            if(first)
                stack.push(qName);

            if(stack.peek() instanceof String)
                stack.push(new HashMap<String, Object>());

            if(!first)
                stack.push(qName);

            if("array".equals(attributes.getValue("type")))
                stack.push(new ArrayList<Object>());
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            Object value = stack.pop();
            String key = (String)stack.pop();
            if(stack.empty())
            {
                root.name = key;
                root.map = (HashMap<String, Object>)value;
            }
            else
            {
                Object collection = stack.peek();
                if(collection instanceof Map)
                    ((HashMap<String, Object>) collection).put(key, value);
                else
                    ((List<Object>)collection).add(value);
            }
        }

        @Override
        public void characters(char[] chars, int start, int length) throws SAXException {
            stack.push(new String(chars, start, length));
        }
    }
}
