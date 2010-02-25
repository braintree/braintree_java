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
      if (map == null) return "";
      String xml = "";
      xml += String.format("<%s>", name);
      for (Map.Entry<String, String> entry : map.entrySet()) {
        xml += String.format("<%s>%s</%s>", entry.getKey(), entry.getValue(), entry.getKey());
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
            return String.format("<%s>%s</%s>", name, element == null ? "" : element.toString(), name);
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
}
