package com.braintreegateway;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.braintreegateway.Transaction.Type;
import com.braintreegateway.util.Http;
import com.braintreegateway.util.NodeWrapper;
import com.braintreegateway.util.QueryString;
import com.braintreegateway.util.TrUtil;

public class TransactionGateway {

    private Http http;
    private Configuration configuration;

    public TransactionGateway(Http http, Configuration configuration) {
        this.http = http;
        this.configuration = configuration;
    }

    public Result<Transaction> confirmTransparentRedirect(String queryString) {
        TransparentRedirectRequest trRequest = new TransparentRedirectRequest(queryString);
        NodeWrapper node = http.post("/transactions/all/confirm_transparent_redirect_request", trRequest);
        return new Result<Transaction>(node, Transaction.class);
    }

    public Result<Transaction> credit(TransactionRequest request) {
        NodeWrapper response = http.post("/transactions", request.type(Type.CREDIT));
        return new Result<Transaction>(response, Transaction.class);
    }

    public String creditTrData(TransactionRequest trData, String redirectURL) {
        return new TrUtil(configuration).buildTrData(trData.type(Type.CREDIT), redirectURL);
    }

    public Transaction find(String id) {
        return new Transaction(http.get("/transactions/" + id));
    }

    public Result<Transaction> refund(String id) {
        NodeWrapper response = http.post("/transactions/" + id + "/refund");
        return new Result<Transaction>(response, Transaction.class);
    }

    public Result<Transaction> sale(TransactionRequest request) {
        NodeWrapper response = http.post("/transactions", request.type(Type.SALE));
        return new Result<Transaction>(response, Transaction.class);
    }

    public String saleTrData(TransactionRequest trData, String redirectURL) {
        return new TrUtil(configuration).buildTrData(trData.type(Type.SALE), redirectURL);
    }

    public PagedCollection search(String query) {
        return search(query, 1);
    }

    public PagedCollection search(String query, int pageNumber) {
        String queryString = new QueryString().append("q", query).append("page", pageNumber).toString();
        NodeWrapper response = http.get("/transactions/all/search?" + queryString);
        int currentPageNumber = response.findInteger("current-page-number");
        int pageSize = response.findInteger("page-size");
        int totalItems = response.findInteger("total-items");

        List<Transaction> transactions = new ArrayList<Transaction>();
        for (NodeWrapper transactionNode : response.findAll("transaction")) {
            transactions.add(new Transaction(transactionNode));
        }

        return new PagedCollection(this, query, transactions, currentPageNumber, totalItems, pageSize);
    }

    public Result<Transaction> submitForSettlement(String id) {
        return submitForSettlement(id, null);
    }

    public Result<Transaction> submitForSettlement(String id, BigDecimal amount) {
        TransactionRequest request = new TransactionRequest().amount(amount);
        NodeWrapper response = http.put("/transactions/" + id + "/submit_for_settlement", request);
        return new Result<Transaction>(response, Transaction.class);
    }

    public String transparentRedirectURLForCreate() {
        return configuration.baseMerchantURL + "/transactions/all/create_via_transparent_redirect_request";
    }

    public Result<Transaction> voidTransaction(String id) {
        NodeWrapper response = http.put("/transactions/" + id + "/void");
        return new Result<Transaction>(response, Transaction.class);
    }

}
