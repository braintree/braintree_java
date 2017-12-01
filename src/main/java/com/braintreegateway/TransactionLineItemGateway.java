package com.braintreegateway;

import com.braintreegateway.exceptions.NotFoundException;
import com.braintreegateway.util.Http;
import com.braintreegateway.util.NodeWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides methods to interact with {@link TransactionLineItem TransactionLineItems}.
 * This class does not need to be instantiated directly.
 * Instead, use {@link BraintreeGateway#transactionLineItem()} to get an instance of this class:
 *
 * <pre>
 * BraintreeGateway gateway = new BraintreeGateway(...);
 * gateway.transactionLineItem().finalAll(...)
 * </pre>
 *
 * For more detailed information on {@link TransactionLineItem TransactionLineItems}, see <a href="http://www.braintreepayments.com/gateway/transaction-api" target="_blank">http://www.braintreepaymentsolutions.com/gateway/transaction-api</a>
 */
public class TransactionLineItemGateway {

    private Http http;
    private Configuration configuration;

    public TransactionLineItemGateway(Http http, Configuration configuration) {
        this.http = http;
        this.configuration = configuration;
    }

    /**
     * Finds all {@link TransactionLineItem} for a {@link Transaction} by id.
     * @param transactionId the id of the {@link Transaction}.
     * @return the {@link List<TransactionLineItem>} or raises a {@link com.braintreegateway.exceptions.NotFoundException}.
     */
    public List<TransactionLineItem> findAll(String transactionId) {
        if(transactionId == null || transactionId.trim().equals(""))
            throw new NotFoundException();

        NodeWrapper node = http.get(configuration.getMerchantPath() + "/transactions/" + transactionId + "/line_items");

        List<TransactionLineItem> transactionLineItems = new ArrayList<TransactionLineItem>();

        for (NodeWrapper transactionLineItemResponse : node.findAll("line-item")) {
            transactionLineItems.add(new TransactionLineItem(transactionLineItemResponse));
        }

        return transactionLineItems;
    }

}
