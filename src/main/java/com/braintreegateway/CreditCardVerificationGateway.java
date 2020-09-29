package com.braintreegateway;

import com.braintreegateway.exceptions.NotFoundException;
import com.braintreegateway.util.Http;
import com.braintreegateway.util.NodeWrapper;
import java.util.ArrayList;
import java.util.List;

public class CreditCardVerificationGateway {
    private Configuration configuration;
    private Http http;

    public CreditCardVerificationGateway(Http http, Configuration configuration) {
        this.configuration = configuration;
        this.http = http;
    }

    List<CreditCardVerification> fetchCreditCardVerifications(CreditCardVerificationSearchRequest query, List<String> ids) {
        query.ids().in(ids);
        NodeWrapper response = http.post(configuration.getMerchantPath() + "/verifications/advanced_search", query);

        List<CreditCardVerification> items = new ArrayList<CreditCardVerification>();
        for (NodeWrapper node : response.findAll("verification")) {
            items.add(new CreditCardVerification(node));
        }

        return items;
    }

    public CreditCardVerification find(String id) {
        if (id == null || id.trim().equals("")) {
            throw new NotFoundException();
        }

        return new CreditCardVerification(http.get(configuration.getMerchantPath() + "/verifications/" + id));
    }

    public ResourceCollection<CreditCardVerification> search(CreditCardVerificationSearchRequest query) {
        NodeWrapper node = http.post(configuration.getMerchantPath() + "/verifications/advanced_search_ids", query);
        return new ResourceCollection<CreditCardVerification>(new CreditCardVerificationPager(this, query), node);
    }

    public Result<CreditCardVerification> create(CreditCardVerificationRequest request) {
        NodeWrapper response = http.post(configuration.getMerchantPath() + "/verifications", request);
        return new Result<CreditCardVerification>(response, CreditCardVerification.class);
    }
}
