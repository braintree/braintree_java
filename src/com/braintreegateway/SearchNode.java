package com.braintreegateway;

import java.util.List;

public class SearchNode<T extends SearchRequest> {
    private String nodeName;
    private T parent;

    public SearchNode(String nodeName, T parent) {
        this.nodeName = nodeName;
        this.parent = parent;
    }

    protected T assembleCriteria(String operation, String value) {
        this.parent.addCriteria(this.nodeName, new SearchCriteria(operation, value));
        return this.parent;
    }
    
    protected T assembleMultiValueCriteria(List<?> items) {
        this.parent.addMultipleValueCriteria(this.nodeName, new SearchCriteria(items));
        return this.parent;
    }
}
