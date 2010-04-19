package com.braintreegateway;

import java.net.URLEncoder;
import java.util.Map;

/**
 * Abstract class for fluent interface request builders.
 */
public abstract class Request {
    public abstract String toXML();
    public abstract String toQueryString(String parent);
    public abstract String toQueryString();

    protected String buildXMLElement(Object element) {
        return buildXMLElement("", element);
    }

    protected String buildXMLElement(String name, Map<String, String> map) {
        if (map == null)
            return "";
        String xml = "";
        xml += String.format("<%s>", name);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            xml += buildXMLElement(entry.getKey(), entry.getValue());
        }
        xml += String.format("</%s>", name);
        return xml;
    }

    protected String buildXMLElement(String name, Object element) {
        if (element == null) {
            return "";
        } else if (element instanceof Request) {
            return ((Request) element).toXML();
        } else {
            return String.format("<%s>%s</%s>", xmlEscape(name), element == null ? "" : xmlEscape(element.toString()), xmlEscape(name));
        }
    }

    protected Object buildQueryStringElement(String name, String value) {
        if (value != null) {
            try {
                return String.format("%s=%s&", URLEncoder.encode(name, "UTF-8"), URLEncoder.encode(value, "UTF-8"));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            return "";
        }
    }

    protected String parentBracketChildString(String parent, String child) {
        return String.format("%s[%s]", parent, child);
    }

    protected String wrapInXMLTag(String tagName, String xml) {
        return String.format("<%s>%s</%s>", tagName, xml, tagName);
    }

    protected String wrapInXMLTag(String tagName, String xml, String type) {
        return String.format("<%s type=\"%s\">%s</%s>", tagName, type, xml, tagName);
    }

    protected String xmlEscape(String input) {
        return input.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("'", "&apos;").replaceAll("\"", "&quot;");
    }
}
