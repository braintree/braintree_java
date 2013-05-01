package com.braintreegateway;

import java.util.List;

public class SearchNode<T extends SearchRequest> {
    protected String nodeName;
    protected T parent;

    public SearchNode(String nodeName, T parent) {
        this.nodeName = nodeName;
        this.parent = parent;
    }

    protected T assembleCriteria(String operation, String value) {
        this.parent.addCriteria(this.nodeName, new SearchCriteria(operation, defaultWithEmptyString(value)));
        return this.parent;
    }
    
    protected T assembleMultiValueCriteria(List<?> items) {
        this.parent.addMultipleValueCriteria(this.nodeName, new SearchCriteria(items));
        return this.parent;
    }
    
    private String defaultWithEmptyString(String value) {
    	if (value == null) {
    		return "";
    	} else {
    		return value;
    	}
    }
}
