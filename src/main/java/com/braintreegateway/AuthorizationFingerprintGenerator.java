package com.braintreegateway;

import com.braintreegateway.Configuration;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.Date;
import java.net.URLEncoder;
import com.braintreegateway.util.QueryString;
import com.braintreegateway.util.Sha256Hasher;
import com.braintreegateway.util.SignatureService;
import com.braintreegateway.AuthorizationFingerprintOptions;

public class AuthorizationFingerprintGenerator {

    public static String generate(String merchantId, String publicKey, String privateKey, AuthorizationFingerprintOptions options) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        df.setTimeZone(tz);
        String dateString = df.format(new Date());

        QueryString payload = new QueryString();
        try {
            payload.append("merchant_id", merchantId)
                .append("public_key", publicKey)
                .append("created_at", dateString);

            if (options != null) {
                String customerId = options.getCustomerId();
                if (customerId != null) {
                    payload.append("customer_id", customerId);
                }

                Boolean makeDefault = options.getMakeDefault();
                if (makeDefault != null) {
                    payload.append("credit_card[options][make_default]", makeDefault.booleanValue());
                }
                Boolean verifyCard = options.getVerifyCard();
                if (verifyCard != null) {
                    payload.append("credit_card[options][verify_card]", verifyCard.booleanValue());
                }
                Boolean failOnDuplicatePaymentMethod = options.getFailOnDuplicatePaymentMethod();
                if (failOnDuplicatePaymentMethod != null) {
                    payload.append("credit_card[options][fail_on_duplicate_payment_method]", failOnDuplicatePaymentMethod.booleanValue());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return new SignatureService(privateKey, new Sha256Hasher()).sign(payload.toString());
    }
}
