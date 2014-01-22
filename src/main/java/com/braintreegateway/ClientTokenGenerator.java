package com.braintreegateway;

import com.braintreegateway.Configuration;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.Date;
import java.util.ArrayList;
import java.net.URLEncoder;
import com.braintreegateway.util.QueryString;
import com.braintreegateway.util.Sha256Hasher;
import com.braintreegateway.util.SignatureService;
import com.braintreegateway.ClientTokenOptions;

public class ClientTokenGenerator {

    public static String generate(String merchantId, String publicKey, String privateKey, String clientApiUrl, String authUrl, ClientTokenOptions options) {
        verifyOptions(options);
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        df.setTimeZone(tz);
        String dateString = df.format(new Date());

        QueryString payload = new QueryString();
        try {
            payload.appendWithoutEncoding("public_key", publicKey)
                .appendWithoutEncoding("created_at", dateString);

            if (options != null) {
                String customerId = options.getCustomerId();
                if (customerId != null) {
                    payload.appendWithoutEncoding("customer_id", customerId);
                }

                Boolean makeDefault = options.getMakeDefault();
                if (makeDefault != null) {
                    payload.appendWithoutEncoding("credit_card[options][make_default]", makeDefault.booleanValue());
                }
                Boolean verifyCard = options.getVerifyCard();
                if (verifyCard != null) {
                    payload.appendWithoutEncoding("credit_card[options][verify_card]", verifyCard.booleanValue());
                }
                Boolean failOnDuplicatePaymentMethod = options.getFailOnDuplicatePaymentMethod();
                if (failOnDuplicatePaymentMethod != null) {
                    payload.appendWithoutEncoding("credit_card[options][fail_on_duplicate_payment_method]", failOnDuplicatePaymentMethod.booleanValue());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String fingerprint = new SignatureService(privateKey, new Sha256Hasher()).sign(payload.toString());
        return String.format("{\"authorization_fingerprint\": \"%s\", \"client_api_url\": \"%s\", \"auth_url\": \"%s\"}",
                fingerprint, clientApiUrl, authUrl);
    }

    private static void verifyOptions(ClientTokenOptions options) {
      if (options == null) {
        return;
      }
      if (options.getCustomerId() == null) {
        ArrayList<String> invalidOptions = new ArrayList<String>();
        if (options.getVerifyCard() != null) {
          invalidOptions.add("verifyCard");
        }
        if (options.getMakeDefault() != null) {
          invalidOptions.add("makeDefault");
        }
        if (options.getFailOnDuplicatePaymentMethod() != null) {
          invalidOptions.add("failOnDuplicatePaymentMethod");
        }

        if (!invalidOptions.isEmpty()){

          String message = "Cannot pass following options without customerId: ";

          for (String option : invalidOptions) {
            message += " " + option;
          }


          throw new IllegalArgumentException(message);
        }
      }
    }
}
