package com.braintreegateway;

import com.braintreegateway.util.Http;

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
