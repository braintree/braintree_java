package com.braintreegateway.graphql.inputs;

import java.util.HashMap;
import java.util.Map;
import com.braintreegateway.Request;
import com.braintreegateway.util.Experimental;

@Experimental("This class is experimental and may change in future releases.")
public class CreateLocalPaymentContextInput extends Request {

    private final MonetaryAmountInput amount;
    private final String type;
    private final PayerInfoInput payerInfo;
    private final String returnUrl;
    private final String cancelUrl;
    private final String merchantAccountId;
    private final String orderId;
    private final String countryCode;
    private final String locale;
    private final String expiryDate;

    @Override
    public Map<String, Object> toGraphQLVariables() {
        Map<String, Object> paymentContext = new HashMap<>();

        if (amount != null) {
            paymentContext.put("amount", amount.toGraphQLVariables());
        }
        if (type != null) {
            paymentContext.put("type", type);
        }
        if (payerInfo != null) {
            paymentContext.put("payerInfo", payerInfo.toGraphQLVariables());
        }
        if (returnUrl != null) {
            paymentContext.put("returnUrl", returnUrl);
        }
        if (cancelUrl != null) {
            paymentContext.put("cancelUrl", cancelUrl);
        }
        if (merchantAccountId != null) {
            paymentContext.put("merchantAccountId", merchantAccountId);
        }
        if (orderId != null) {
            paymentContext.put("orderId", orderId);
        }
        if (countryCode != null) {
            paymentContext.put("countryCode", countryCode);
        }
        if (locale != null) {
            paymentContext.put("locale", locale);
        }
        if (expiryDate != null) {
            paymentContext.put("expiryDate", expiryDate);
        }

        Map<String, Object> variables = new HashMap<>();
        variables.put("paymentContext", paymentContext);

        return variables;
    }

    private CreateLocalPaymentContextInput(Builder builder) {
        this.amount = builder.amount;
        this.type = builder.type;
        this.payerInfo = builder.payerInfo;
        this.returnUrl = builder.returnUrl;
        this.cancelUrl = builder.cancelUrl;
        this.merchantAccountId = builder.merchantAccountId;
        this.orderId = builder.orderId;
        this.countryCode = builder.countryCode;
        this.locale = builder.locale;
        this.expiryDate = builder.expiryDate;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private MonetaryAmountInput amount;
        private String type;
        private PayerInfoInput payerInfo;
        private String returnUrl;
        private String cancelUrl;
        private String merchantAccountId;
        private String orderId;
        private String countryCode;
        private String locale;
        private String expiryDate;

        public Builder amount(MonetaryAmountInput amount) {
            this.amount = amount;
            return this;
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder payerInfo(PayerInfoInput payerInfo) {
            this.payerInfo = payerInfo;
            return this;
        }

        public Builder returnUrl(String returnUrl) {
            this.returnUrl = returnUrl;
            return this;
        }

        public Builder cancelUrl(String cancelUrl) {
            this.cancelUrl = cancelUrl;
            return this;
        }

        public Builder merchantAccountId(String merchantAccountId) {
            this.merchantAccountId = merchantAccountId;
            return this;
        }

        public Builder orderId(String orderId) {
            this.orderId = orderId;
            return this;
        }

        public Builder countryCode(String countryCode) {
            this.countryCode = countryCode;
            return this;
        }

        public Builder locale(String locale) {
            this.locale = locale;
            return this;
        }

        public Builder expiryDate(String expiryDate) {
            this.expiryDate = expiryDate;
            return this;
        }

        public CreateLocalPaymentContextInput build() {
            return new CreateLocalPaymentContextInput(this);
        }
    }
}
