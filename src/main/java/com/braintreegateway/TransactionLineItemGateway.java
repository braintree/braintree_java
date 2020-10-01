package com.braintreegateway;

import com.braintreegateway.exceptions.NotFoundException;
import com.braintreegateway.exceptions.UnexpectedException;
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
 * gateway.transactionLineItem().findAll(...)
 * </pre>
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
     * @return the List&lt;TransactionLineItem&gt; or raises a com.braintreegateway.exceptions.NotFoundException.
     */
    public List<TransactionLineItem> findAll(String transactionId) {
        if (transactionId == null || transactionId.trim().equals("")) {
            throw new NotFoundException();
        }

        NodeWrapper node = http.get(configuration.getMerchantPath() + "/transactions/" + transactionId + "/line_items");

        if (node.getElementName().equals("line-items")) {
            List<TransactionLineItem> transactionLineItems = new ArrayList<TransactionLineItem>();

            for (NodeWrapper transactionLineItemResponse : node.findAll("line-item")) {
                transactionLineItems.add(new TransactionLineItem(transactionLineItemResponse));
            }

            return transactionLineItems;
        } else {
            throw new UnexpectedException("No line items found.");
        }
    }

}
