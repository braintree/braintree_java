package com.braintreegateway;

import com.braintreegateway.exceptions.NotFoundException;
import com.braintreegateway.util.Http;
import com.braintreegateway.util.NodeWrapper;

import java.util.ArrayList;
import java.util.List;

public class UsBankAccountVerificationGateway {
    private Http http;
    private Configuration configuration;

    public UsBankAccountVerificationGateway(Http http, Configuration configuration) {
        this.http = http;
        this.configuration = configuration;
    }

    public UsBankAccountVerification find(String id) {
        if(id == null || id.trim().equals(""))
            throw new NotFoundException();

        return new UsBankAccountVerification(http.get(configuration.getMerchantPath() + "/us_bank_account_verifications/" + id));
    }

    public ResourceCollection<UsBankAccountVerification> search(UsBankAccountVerificationSearchRequest query) {
        NodeWrapper node = http.post(configuration.getMerchantPath() + "/us_bank_account_verifications/advanced_search_ids", query);
        return new ResourceCollection<UsBankAccountVerification>(new UsBankAccountVerificationPager(this, query), node);
    }

    public Result<UsBankAccountVerification> confirmMicroTransferAmounts(String id, UsBankAccountVerificationConfirmRequest request) {
        NodeWrapper nodeWrapper = http.put(configuration.getMerchantPath() + "/us_bank_account_verifications/" + id + "/confirm_micro_transfer_amounts", request);
        return new Result<UsBankAccountVerification>(nodeWrapper, UsBankAccountVerification.class);
    }

    List<UsBankAccountVerification> fetchUsBankAccountVerifications(UsBankAccountVerificationSearchRequest query, List<String> ids) {
        query.ids().in(ids);
        NodeWrapper response = http.post(configuration.getMerchantPath() + "/us_bank_account_verifications/advanced_search", query);

        List<UsBankAccountVerification> items = new ArrayList<UsBankAccountVerification>();
        for (NodeWrapper node : response.findAll("us-bank-account-verification")) {
            items.add(new UsBankAccountVerification(node));
        }

        return items;
    }
}
