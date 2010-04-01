package com.braintreegateway;

import java.util.HashMap;
import java.util.Map;

public class SubscriptionSearchRequest extends Request {
    private Map<String, SearchCriteria> criteria;
    
    public SubscriptionSearchRequest() {
        this.criteria = new HashMap<String, SearchCriteria>();
    }
    
    public TextNode planId() {
        return new TextNode("plan_id", this);
    }
    
    public TextNode daysPastDue() {
        return new TextNode("days_past_due", this);
    }

    public void addCriteria(String nodeName, SearchCriteria searchCriteria) {
        criteria.put(nodeName, searchCriteria);
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
            builder.append(buildXMLElement(key, criteria.get(key).toXML()));
        }
        builder.append("</search>");
        return builder.toString();
    }
}
