package com.braintreegateway;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchRequest extends Request {
    private Map<String, List<SearchCriteria>> criteria;
    private Map<String, List<SearchCriteria>> rangeCriteria;
    private Map<String, SearchCriteria> multiValueCriteria;
    private Map<String, String> keyValueCriteria;
    
    public SearchRequest() {
        this.criteria = new HashMap<String, List<SearchCriteria>>();
        this.rangeCriteria = new HashMap<String, List<SearchCriteria>>();
        this.multiValueCriteria = new HashMap<String, SearchCriteria>();
        this.keyValueCriteria = new HashMap<String, String>();
    }
    
    public void addCriteria(String nodeName, SearchCriteria searchCriteria) {
        if (!criteria.containsKey(nodeName)) {
            criteria.put(nodeName, new ArrayList<SearchCriteria>());
        }
        criteria.get(nodeName).add(searchCriteria);
    }
    
    public void addRangeCriteria(String nodeName, SearchCriteria searchCriteria) {
        if (!rangeCriteria.containsKey(nodeName)) {
            rangeCriteria.put(nodeName, new ArrayList<SearchCriteria>());
        }
        rangeCriteria.get(nodeName).add(searchCriteria);
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
            builder.append(String.format("<%s>", RequestBuilder.xmlEscape(key)));
            for (SearchCriteria criterium : criteria.get(key)) {
                builder.append(criterium.toXML());
            }
            builder.append(String.format("</%s>", RequestBuilder.xmlEscape(key)));
        }
        for (String key : rangeCriteria.keySet()) {
            builder.append(String.format("<%s>", RequestBuilder.xmlEscape(key)));
            for (SearchCriteria criterium : rangeCriteria.get(key)) {
                builder.append(criterium.toXML());
            }
            builder.append(String.format("</%s>", RequestBuilder.xmlEscape(key)));
        }
        for (String key : multiValueCriteria.keySet()) {
            builder.append(RequestBuilder.wrapInXMLTag(key, multiValueCriteria.get(key).toXML(), "array"));
        }
        for (String key : keyValueCriteria.keySet()) {
            builder.append(RequestBuilder.wrapInXMLTag(key, keyValueCriteria.get(key)));
        }
        builder.append("</search>");
        return builder.toString();
    }
}
