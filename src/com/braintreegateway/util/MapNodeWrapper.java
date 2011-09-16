package com.braintreegateway.util;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.StringReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MapNodeWrapper extends NodeWrapper {

    private static SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();

    private String name;
    private Map<String, String> attributes = new HashMap<String, String>();
    private List<Object> content = new LinkedList<Object>();

    private MapNodeWrapper(String name) {
        this.name = name;
    }

    public static MapNodeWrapper parse(String xml) {
        try {
            InputSource source = new InputSource(new StringReader(xml));
            SAXParser parser = saxParserFactory.newSAXParser();
            MapNodeHandler handler = new MapNodeHandler();
            parser.parse(source, handler);
            return handler.root;
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    @Override
    public List<NodeWrapper> findAll(String expression) {
        String[] paths = expression.split("/");
        LinkedList<String> tokens = new LinkedList<String>(Arrays.asList(paths));
        List<NodeWrapper> nodes = new LinkedList<NodeWrapper>();
        findAll(tokens, nodes);
        return nodes;
    }

    private void findAll(LinkedList<String> tokens, List<NodeWrapper> nodes) {
        if (tokens.isEmpty())
            nodes.add(this);
        else {
            String first = tokens.getFirst();
            if (".".equals(first))
                findAll(restOf(tokens), nodes);
            for (MapNodeWrapper node : childNodes()) {
                if ("*".equals(first) || first.equals(node.name))
                    node.findAll(restOf(tokens), nodes);
            }
        }
    }

    private MapNodeWrapper find(LinkedList<String> tokens) {
        if (tokens.isEmpty())
            return this;
        else {
            String first = tokens.getFirst();
            if (".".equals(first))
                return find(restOf(tokens));
            for (MapNodeWrapper node : childNodes()) {
                if ("*".equals(first) || first.equals(node.name))
                    return node.find(restOf(tokens));
            }
            return null;
        }
    }

    private MapNodeWrapper find(String expression) {
        String[] paths = expression.split("/");
        LinkedList<String> tokens = new LinkedList<String>(Arrays.asList(paths));

        return find(tokens);
    }

    private LinkedList<String> restOf(LinkedList<String> tokens) {
        LinkedList<String> newTokens = new LinkedList<String>(tokens);
        newTokens.removeFirst();
        return newTokens;
    }

    @Override
    public NodeWrapper findFirst(String expression) {
        return find(expression);
    }

    @Override
    public String findString(String expression) {
        MapNodeWrapper node = find(expression);
        if (node == null)
            return null;
        else
            return node.stringValue();
    }

    private String stringValue() {
        if (content.size() == 1 && content.get(0) == null)
            return null;
        String value = "";
        for (Object o : content) {
            value += o.toString();
        }
        return value.trim();
    }

    @Override
    public String getElementName() {
        return name;
    }

    private List<MapNodeWrapper> childNodes() {
        List<MapNodeWrapper> nodes = new LinkedList<MapNodeWrapper>();
        for (Object o : content) {
            if (o instanceof MapNodeWrapper)
                nodes.add((MapNodeWrapper) o);
        }
        return nodes;
    }

    @Override
    public Map<String, String> getFormParameters() {
        Map<String, String> params = new HashMap<String, String>();
        for (MapNodeWrapper node : childNodes()) {
            node.buildParams("", params);
        }
        return params;
    }

    private void buildParams(String prefix, Map<String, String> params) {
        List<MapNodeWrapper> childNodes = childNodes();
        String newPrefix = "".equals(prefix) ? StringUtils.underscore(name) : prefix + "[" + StringUtils.underscore(name) + "]";
        if (childNodes.isEmpty())
            params.put(newPrefix, stringValue());
        else {
            for (MapNodeWrapper childNode : childNodes)
                childNode.buildParams(newPrefix, params);
        }
    }

    @Override
    public String toString() {
        return "<" + name +
                (attributes.isEmpty() ? "" : " attributes=" + StringUtils.toString(attributes)) +
                " content=" + StringUtils.toString(content) + ">";
    }

    private static class MapNodeHandler extends DefaultHandler {
        private static Pattern NON_WHITE_SPACE = Pattern.compile("\\S");

        private Stack<MapNodeWrapper> stack = new Stack<MapNodeWrapper>();
        public MapNodeWrapper root;

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            MapNodeWrapper node = new MapNodeWrapper(qName);

            for (int i = 0; i < attributes.getLength(); i++)
                node.attributes.put(attributes.getQName(i), attributes.getValue(i));

            if ("true".equals(node.attributes.get("nil")))
                node.content.add(null);

            if (!stack.isEmpty())
                stack.peek().content.add(node);

            stack.push(node);
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            MapNodeWrapper pop = stack.pop();
            if (stack.isEmpty())
                root = pop;
        }

        @Override
        public void characters(char[] chars, int start, int length) throws SAXException {
            String value = new String(chars, start, length);

            Matcher matcher = NON_WHITE_SPACE.matcher(value);
            if (value.length() > 0 && matcher.find()) {
                stack.peek().content.add(value);
            }
        }
    }
}
