package com.braintreegateway;

import java.util.HashMap;
import java.util.Map;

public class SearchRequest extends Request {
    private Map<String, SearchCriteria> criteria;
    private Map<String, SearchCriteria> multiValueCriteria;
    private Map<String, String> keyValueCriteria;
    
    public SearchRequest() {
        this.criteria = new HashMap<String, SearchCriteria>();
        this.multiValueCriteria = new HashMap<String, SearchCriteria>();
        this.keyValueCriteria = new HashMap<String, String>();
    }
    
    public void addCriteria(String nodeName, SearchCriteria searchCriteria) {
        criteria.put(nodeName, searchCriteria);
    }
    
    public void addMultipleValueCriteria(String nodeName, SearchCriteria searchCriteria) {
        multiValueCriteria.put(nodeName, searchCriteria);
    }
    
    public void addKeyValueCriteria(String nodeName, String value) {
        keyValueCriteria.put(nodeName, value);
    }
    
    @Override
    public String toQueryString(String parent) {
        return null;
    }
    
    @Override
    public String toQueryString() {
        return null;
    }
    
    @Override
    public String toXML() {
        StringBuilder builder = new StringBuilder();
        builder.append("<search>");
        for (String key : criteria.keySet()) {
            builder.append(wrapInXMLTag(key, criteria.get(key).toXML()));
        }
        for (String key : multiValueCriteria.keySet()) {
            builder.append(wrapInXMLTag(key, multiValueCriteria.get(key).toXML(), "array"));
        }
        for (String key : keyValueCriteria.keySet()) {
            builder.append(wrapInXMLTag(key, keyValueCriteria.get(key)));
        }
        builder.append("</search>");
        return builder.toString();
    }
}
