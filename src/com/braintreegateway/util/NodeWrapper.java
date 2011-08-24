package com.braintreegateway.util;

import java.io.StringReader;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;


public class NodeWrapper {
    private static final String ALL_LEAF_NODES_XPATH = ".//*[not(*)]";
    private XPath xpath;
    private Node node;

    public NodeWrapper(String xml) {
        InputSource source = new InputSource(new StringReader(xml));
        xpath = XPathFactory.newInstance().newXPath();

        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document parse = builder.parse(source);
            node = parse.getDocumentElement();
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public NodeWrapper(Node node) {
        this.node = node;
        xpath = XPathFactory.newInstance().newXPath();
    }

    public List<NodeWrapper> findAll(String expression) {
        try {
            NodeList nodes = (NodeList) xpath.evaluate(expression, node, XPathConstants.NODESET);
            List<NodeWrapper> nodeWrappers = new ArrayList<NodeWrapper>();
            for (int i = 0; i < nodes.getLength(); i++) {
                nodeWrappers.add(new NodeWrapper(nodes.item(i)));
            }
            return nodeWrappers;
        } catch (XPathExpressionException e) {
            return new ArrayList<NodeWrapper>();
        }
    }

    public List<String> findAllStrings(String expression) {
        List<String> strings = new ArrayList<String>();
        
        for (NodeWrapper node : findAll(expression)) {
            strings.add(node.findString("."));
        }
        
        return strings;
    }
    
    public BigDecimal findBigDecimal(String expression) {
        String value = findString(expression);
        return value == null ? null : new BigDecimal(value);
    }

    public boolean findBoolean(String expression) {
        String value = findString(expression);
        return new Boolean(value).booleanValue();
    }
    
    public Calendar findDate(String expression) {
        try {
            String dateString = StringUtils.nullIfEmpty(xpath.evaluate(expression, node));
            if (dateString == null) {
                return null;
            }            
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            format.setTimeZone(TimeZone.getTimeZone("UTC"));
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            calendar.setTime(format.parse(dateString));
            return calendar;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Calendar findDateTime(String expression) {
        try {
            String dateString = StringUtils.nullIfEmpty(xpath.evaluate(expression, node));
            if (dateString == null) {
                return null;
            }
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            format.setTimeZone(TimeZone.getTimeZone("UTC"));
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            calendar.setTime(format.parse(dateString));
            return calendar;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Integer findInteger(String expression) {
        String value = findString(expression);
        return value == null ? null : new Integer(value);
    }

    public NodeWrapper findFirst(String expression) {
        try {
            Node firstNode = (Node) xpath.evaluate(expression, node, XPathConstants.NODE);
            if (firstNode == null) {
                return null;
            }
            return new NodeWrapper(firstNode);
        } catch (XPathExpressionException e) {
            return null;
        }
    }

    public String findString(String expression) {
        try {
            return StringUtils.nullIfEmpty(xpath.evaluate(expression, node));
        } catch (XPathExpressionException e) {
            return null;
        }
    }

    public String getElementName() {

        return node.getNodeName();
    }

    public boolean isSuccess() {
        return getElementName() != "api-error-response";
    }

    private boolean isSameNode(NodeWrapper other) {
        return node.isSameNode(other.node);
    }

    private NodeWrapper getParent() {
        return new NodeWrapper(node.getParentNode());
    }

    private String xpathToNode(NodeWrapper node) {
        if (this.isSameNode(node))
            return "";

        return xpathToNode(node.getParent()) + "/" + node.getElementName();
    }

    private String getFormElementName(NodeWrapper leafNode) {
        String[] nodes = xpathToNode(leafNode).split("/");
        String formElementName = nodes[1];

        for (int i = 2; i < nodes.length; i++) {
            formElementName += "[" + nodes[i] + "]";
        }

        formElementName = StringUtils.underscore(formElementName);

        return formElementName;
    }

    public Map<String, String> getFormParameters() {
        Map<String, String> formParameters = new HashMap<String, String>();

        for (NodeWrapper paramNode : findAll(ALL_LEAF_NODES_XPATH)) {
            formParameters.put(getFormElementName(paramNode), paramNode.findString("."));
        }

        return formParameters;
    }

    public Map<String, String> findMap(String expression) {
        Map<String, String> map = new HashMap<String, String>();

        for (NodeWrapper mapNode : findAll(expression)) {
            map.put(StringUtils.underscore(mapNode.getElementName()), mapNode.findString("."));
        }

        return map;
    }
}
