package com.braintreegateway;

import com.braintreegateway.Configuration;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.Date;
import java.net.URLEncoder;
import com.braintreegateway.util.QueryString;
import com.braintreegateway.util.Crypto;

public class AuthorizationFingerprint {

  public static String generate(String merchantId, String publicKey, String privateKey, String customerId) {
      TimeZone tz = TimeZone.getTimeZone("UTC");
      DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
      df.setTimeZone(tz);
      String dateString = df.format(new Date());

      QueryString payload = new QueryString();
      try {
          payload.append("merchant_id", URLEncoder.encode(merchantId, "UTF-8"))
              .append("public_key", URLEncoder.encode(publicKey, "UTF-8"))
              .append("created_at", dateString);

          if (customerId != null) {
              payload.append("customer_id", URLEncoder.encode(customerId, "UTF-8"));
          }
      } catch (Exception e) {
          throw new RuntimeException(e);
      }

      String signature = new Crypto().hmacHashSha256(privateKey, payload.toString());
      return signature + "|" + payload;
  }
}
