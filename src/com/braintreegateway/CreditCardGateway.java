package com.braintreegateway;

import com.braintreegateway.util.Http;
import com.braintreegateway.util.NodeWrapper;

public class CreditCardGateway {
    private String baseMerchantURL;
    private Http http;

    public CreditCardGateway(Http http, Configuration configuration) {
        this.baseMerchantURL = configuration.baseMerchantURL;
        this.http = http;
    }

    public Result<CreditCard> confirmTransparentRedirect(String queryString) {
        TransparentRedirectRequest trRequest = new TransparentRedirectRequest(queryString);
        NodeWrapper node = http.post("/payment_methods/all/confirm_transparent_redirect_request", trRequest);
        return new Result<CreditCard>(node, CreditCard.class);
    }

    public Result<CreditCard> create(CreditCardRequest request) {
        NodeWrapper node = http.post("/payment_methods", request);
        return new Result<CreditCard>(node, CreditCard.class);
    }

    public Result<CreditCard> delete(String token) {
        http.delete("/payment_methods/" + token);
        return new Result<CreditCard>();
    }

    public CreditCard find(String token) {
        return new CreditCard(http.get("/payment_methods/" + token));
    }

    public String transparentRedirectURLForCreate() {
        return baseMerchantURL + "/payment_methods/all/create_via_transparent_redirect_request";
    }

    public String transparentRedirectURLForUpdate() {
        return baseMerchantURL + "/payment_methods/all/update_via_transparent_redirect_request";
    }

    public Result<CreditCard> update(String token, CreditCardRequest request) {
        NodeWrapper node = http.put("/payment_methods/" + token, request);
        return new Result<CreditCard>(node, CreditCard.class);
    }
}
