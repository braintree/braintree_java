package com.braintreegateway;

import com.braintreegateway.util.Http;

// NEXT_MAJOR_VERSION Remove this class as legacy Ideal has been removed/disabled in the Braintree Gateway
/**
 * @deprecated If you're looking to accept iDEAL as a payment method contact us at accounts@braintreepayments.com for a solution.
 */
public class IdealPaymentGateway {
    private BraintreeGateway gateway;
    private Http http;
    private Configuration configuration;

    public IdealPaymentGateway(BraintreeGateway gateway, Http http, Configuration configuration) {
        this.gateway = gateway;
        this.http = http;
        this.configuration = configuration;
    }

    public IdealPayment find(String idealPaymentId) {
        return new IdealPayment(http.get(configuration.getMerchantPath() + "/ideal_payments/" + idealPaymentId));
    }

    public Result<Transaction> sale(String idealPaymentId, TransactionRequest transactionRequest) {
        transactionRequest
            .paymentMethodNonce(idealPaymentId)
            .options()
                .submitForSettlement(true);
        return gateway.transaction().sale(transactionRequest);
    }
}
