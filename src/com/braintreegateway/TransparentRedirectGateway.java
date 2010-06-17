package com.braintreegateway;

import com.braintreegateway.util.Http;
import com.braintreegateway.util.NodeWrapper;
import com.braintreegateway.util.TrUtil;

public class TransparentRedirectGateway {
    private Http http;
    private Configuration configuration;
    
    public TransparentRedirectGateway(Http http, Configuration configuration) {
        this.http = http;
        this.configuration = configuration;
    }
    
    public String url() {
        return new TrUtil(configuration).url();
    }

    public Result<Transaction> confirmTransaction(String queryString) {
        return confirmTr(Transaction.class, queryString);
    }
    
    private <T> Result<T> confirmTr(Class<T> klass, String queryString) {
        TransparentRedirectRequest trRequest = new TransparentRedirectRequest(configuration, queryString);
        NodeWrapper node = http.post("/transparent_redirect_requests/" + trRequest.getId() + "/confirm", trRequest);
        return new Result<T>(node, klass);
    }
}
