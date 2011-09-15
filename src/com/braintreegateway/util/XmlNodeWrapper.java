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


public class XmlNodeWrapper extends NodeWrapper {
    private static final String ALL_LEAF_NODES_XPATH = ".//*[not(*)]";
    private XPath xpath;
    private Node node;

    public XmlNodeWrapper(String xml) {
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

    public XmlNodeWrapper(Node node) {
        this.node = node;
        xpath = XPathFactory.newInstance().newXPath();
    }

    public List<NodeWrapper> findAll(String expression) {
        try {
            NodeList nodes = (NodeList) xpath.evaluate(expression, node, XPathConstants.NODESET);
            List<NodeWrapper> nodeWrappers = new ArrayList<NodeWrapper>();
            for (int i = 0; i < nodes.getLength(); i++) {
                nodeWrappers.add(new XmlNodeWrapper(nodes.item(i)));
            }
            return nodeWrappers;
        } catch (XPathExpressionException e) {
            return new ArrayList<NodeWrapper>();
        }
    }

    public NodeWrapper findFirst(String expression) {
        try {
            Node firstNode = (Node) xpath.evaluate(expression, node, XPathConstants.NODE);
            if (firstNode == null) {
                return null;
            }
            return new XmlNodeWrapper(firstNode);
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

    private boolean isSameNode(XmlNodeWrapper other) {
        return node.isSameNode(other.node);
    }

    private XmlNodeWrapper getParent() {
        return new XmlNodeWrapper(node.getParentNode());
    }

    private String xpathToNode(XmlNodeWrapper node) {
        if (this.isSameNode(node))
            return "";

        return xpathToNode(node.getParent()) + "/" + node.getElementName();
    }

    private String getFormElementName(XmlNodeWrapper leafNode) {
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
            formParameters.put(getFormElementName((XmlNodeWrapper)paramNode), paramNode.findString("."));
        }

        return formParameters;
    }
}
