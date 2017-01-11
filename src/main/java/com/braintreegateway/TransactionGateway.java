package com.braintreegateway;

import com.braintreegateway.Transaction.Type;
import com.braintreegateway.exceptions.NotFoundException;
import com.braintreegateway.exceptions.DownForMaintenanceException;
import com.braintreegateway.util.Http;
import com.braintreegateway.util.NodeWrapper;
import com.braintreegateway.util.TrUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides methods to interact with {@link Transaction Transactions}.
 * E.g. sales, credits, refunds, searches, etc.
 * This class does not need to be instantiated directly.
 * Instead, use {@link BraintreeGateway#transaction()} to get an instance of this class:
 *
 * <pre>
 * BraintreeGateway gateway = new BraintreeGateway(...);
 * gateway.transaction().create(...)
 * </pre>
 *
 * For more detailed information on {@link Transaction Transactions}, see <a href="http://www.braintreepayments.com/gateway/transaction-api" target="_blank">http://www.braintreepaymentsolutions.com/gateway/transaction-api</a>
 */
public class TransactionGateway {

    private Http http;
    private Configuration configuration;

    public TransactionGateway(Http http, Configuration configuration) {
        this.http = http;
        this.configuration = configuration;
    }

    public Result<Transaction> cloneTransaction(String id, TransactionCloneRequest request) {
        NodeWrapper response = http.post(configuration.getMerchantPath() + "/transactions/" + id + "/clone", request);
        return new Result<Transaction>(response, Transaction.class);
    }

    /**
     * Please use gateway.transparentRedirect().confirmTransaction() instead
     */
    @Deprecated
    public Result<Transaction> confirmTransparentRedirect(String queryString) {
        TransparentRedirectRequest trRequest = new TransparentRedirectRequest(configuration, queryString);
        NodeWrapper node = http.post(configuration.getMerchantPath() + "/transactions/all/confirm_transparent_redirect_request", trRequest);
        return new Result<Transaction>(node, Transaction.class);
    }

    /**
     * Creates a credit {@link Transaction}.
     * @param request the request.
     * @return a {@link Result}
     */
    public Result<Transaction> credit(TransactionRequest request) {
        NodeWrapper response = http.post(configuration.getMerchantPath() + "/transactions", request.type(Type.CREDIT));
        return new Result<Transaction>(response, Transaction.class);
    }

    /**
     * Creates transparent redirect data for a credit.
     * @param trData the request.
     * @param redirectURL the redirect URL.
     * @return a String representing the trData.
     */
    public String creditTrData(TransactionRequest trData, String redirectURL) {
        return new TrUtil(configuration).buildTrData(trData.type(Type.CREDIT), redirectURL);
    }

    /**
     * Finds a {@link Transaction} by id.
     * @param id the id of the {@link Transaction}.
     * @return the {@link Transaction} or raises a {@link com.braintreegateway.exceptions.NotFoundException}.
     */
    public Transaction find(String id) {
        if(id == null || id.trim().equals(""))
            throw new NotFoundException();
        return new Transaction(http.get(configuration.getMerchantPath() + "/transactions/" + id));
    }

    /**
     * Refunds all or part of a previous sale {@link Transaction}.
     * @param id the id of the (sale) {@link Transaction} to refund.
     * @return a {@link Result}.
     */
    public Result<Transaction> refund(String id) {
        NodeWrapper response = http.post(configuration.getMerchantPath() + "/transactions/" + id + "/refund");
        return new Result<Transaction>(response, Transaction.class);
    }

    public Result<Transaction> refund(String id, BigDecimal amount) {
        TransactionRequest request = new TransactionRequest().amount(amount);
        NodeWrapper response = http.post(configuration.getMerchantPath() + "/transactions/" + id + "/refund", request);
        return new Result<Transaction>(response, Transaction.class);
    }

    public Result<Transaction> refund(String id, TransactionRefundRequest request) {
        NodeWrapper response = http.post(configuration.getMerchantPath() + "/transactions/" + id + "/refund", request);
        return new Result<Transaction>(response, Transaction.class);
    }

    /**
     * Creates a sale {@link Transaction}.
     * @param request the request.
     * @return a {@link Result}.
     */
    public Result<Transaction> sale(TransactionRequest request) {
        NodeWrapper response = http.post(configuration.getMerchantPath() + "/transactions", request.type(Type.SALE));
        return new Result<Transaction>(response, Transaction.class);
    }

    /**
     * Creates transparent redirect data for a sale.
     * @param trData the request.
     * @param redirectURL the redirect URL.
     * @return a String representing the trData.
     */
    public String saleTrData(TransactionRequest trData, String redirectURL) {
        return new TrUtil(configuration).buildTrData(trData.type(Type.SALE), redirectURL);
    }

    /**
     * Finds all Transactions that match the query and returns a {@link ResourceCollection}.
     * See: <a href="http://www.braintreepayments.com/gateway/transaction-api#searching" target="_blank">http://www.braintreepaymentsolutions.com/gateway/transaction-api#searching</a>
     * @return a {@link ResourceCollection} or raises a {@link DownForMaintenanceException}.
     */
    public ResourceCollection<Transaction> search(TransactionSearchRequest query) {
        NodeWrapper node = http.post(configuration.getMerchantPath() + "/transactions/advanced_search_ids", query);
        if (node.getElementName().equals("search-results")) {
          return new ResourceCollection<Transaction>(new TransactionPager(this, query), node);
        } else {
          throw new DownForMaintenanceException();
        }
    }

    List<Transaction> fetchTransactions(TransactionSearchRequest query, List<String> ids) {
        query.ids().in(ids);
        NodeWrapper response = http.post(configuration.getMerchantPath() + "/transactions/advanced_search", query);

        if (response.getElementName().equals("credit-card-transactions")) {
            List<Transaction> items = new ArrayList<Transaction>();
            for (NodeWrapper node : response.findAll("transaction")) {
                items.add(new Transaction(node));
            }

            return items;
        } else {
          throw new DownForMaintenanceException();
        }
    }

    /**
     * Cancels a pending release of a transaction with the given id from escrow.
     * @param id of the transaction to cancel release from escrow of.
     * @return a {@link Result}.
     */
    public Result<Transaction> cancelRelease(String id) {
        TransactionRequest request = new TransactionRequest();
        NodeWrapper response = http.put(configuration.getMerchantPath() + "/transactions/" + id + "/cancel_release", request);
        return new Result<Transaction>(response, Transaction.class);
    }

    /**
     * Holds the transaction with the given id for escrow.
     * @param id of the transaction to hold for escrow.
     * @return a {@link Result}.
     */
    public Result<Transaction> holdInEscrow(String id) {
        TransactionRequest request = new TransactionRequest();
        NodeWrapper response = http.put(configuration.getMerchantPath() + "/transactions/" + id + "/hold_in_escrow", request);
        return new Result<Transaction>(response, Transaction.class);
    }

    /**
     * Submits the transaction with the given id for release.
     * @param id of the transaction to submit for release.
     * @return a {@link Result}.
     */
    public Result<Transaction> releaseFromEscrow(String id) {
        TransactionRequest request = new TransactionRequest();
        NodeWrapper response = http.put(configuration.getMerchantPath() + "/transactions/" + id + "/release_from_escrow", request);
        return new Result<Transaction>(response, Transaction.class);
    }

    /**
     * Submits the transaction with the given id for settlement.
     * @param id of the transaction to submit for settlement.
     * @return a {@link Result}.
     */
    public Result<Transaction> submitForSettlement(String id) {
        TransactionRequest request = new TransactionRequest();
        return submitForSettlement(id, request);
    }

    /**
     * Submits the transaction with the given id to be settled for the given amount which must be less than or equal to the authorization amount.
     * @param id of the transaction to submit for settlement.
     * @param amount to settle. must be less than or equal to the authorization amount.
     * @return {@link Result}.
     */
    public Result<Transaction> submitForSettlement(String id, BigDecimal amount) {
        TransactionRequest request = new TransactionRequest().amount(amount);
        return submitForSettlement(id, request);
    }

    /**
     * Submits the transaction with the given id to be settled along with a TransactionRequest object.
     * @param id of the transaction to submit for settlement.
     * @param request the request.
     * @return {@link Result}.
     */
    public Result<Transaction> submitForSettlement(String id, TransactionRequest request) {
        NodeWrapper response = http.put(configuration.getMerchantPath() + "/transactions/" + id + "/submit_for_settlement", request);
        return new Result<Transaction>(response, Transaction.class);
    }

    /**
     * Updates details for a transaction that has been submitted for settlement.
     * @param id of the transaction to update the details for.
     * @param request the request.
     * @return {@link Result}.
     */
    public Result<Transaction> updateDetails(String id, TransactionRequest request) {
        NodeWrapper response = http.put(configuration.getMerchantPath() + "/transactions/" + id + "/update_details", request);
        return new Result<Transaction>(response, Transaction.class);
    }

    /**
     * Please use gateway.transparentRedirect().url() instead
     */
    @Deprecated
    public String transparentRedirectURLForCreate() {
        return configuration.getBaseURL() + configuration.getMerchantPath() + "/transactions/all/create_via_transparent_redirect_request";
    }

    /**
     * Voids the transaction with the given id.
     * @param id of the transaction to void.
     * @return {@link Result}.
     */
    public Result<Transaction> voidTransaction(String id) {
        NodeWrapper response = http.put(configuration.getMerchantPath() + "/transactions/" + id + "/void");
        return new Result<Transaction>(response, Transaction.class);
    }

    /**
     * Submits a partial settlement transaction for the given id.
     * @param id of the transaction to add the partial settlement transaction for.
     * @param amount of the partial settlement
     * @return {@link Result}.
     */
    public Result<Transaction> submitForPartialSettlement(String id, BigDecimal amount) {
        TransactionRequest request = new TransactionRequest().amount(amount);
        return submitForPartialSettlement(id, request);
    }

    /**
     * Submits a partial settlement transaction for the given id.
     * @param id of the transaction to add the partial settlement transaction for.
     * @param request the request.
     * @return {@link Result}.
     */
    public Result<Transaction> submitForPartialSettlement(String id, TransactionRequest request) {
        NodeWrapper response = http.post(configuration.getMerchantPath() + "/transactions/" + id + "/submit_for_partial_settlement", request);
        return new Result<Transaction>(response, Transaction.class);
    }

}
