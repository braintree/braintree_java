package com.braintreegateway;

import java.util.ArrayList;
import com.braintreegateway.util.Http;
import com.braintreegateway.util.NodeWrapper;
import com.braintreegateway.util.StringUtils;

/**
 * Generates client tokens, which are used to authenticate requests made directly
 *   on behalf of merchants
 * This class does not need to be instantiated directly.
 * Instead, use {@link BraintreeGateway#clientToken()} to get an instance of this class:
 *
 * <pre>
 * BraintreeGateway gateway = new BraintreeGateway(...);
 * gateway.clientToken().generate(...)
 * </pre>
 *
 */
public class ClientTokenGateway {

    private Http http;
    private Configuration configuration;

    public ClientTokenGateway(Http http, Configuration configuration) {
        this.http = http;
        this.configuration = configuration;
    }

    public String generate() {
      return generate(new ClientTokenRequest());
    }

    public String generate(ClientTokenRequest request) {
      NodeWrapper response = null;
      verifyOptions(request);
      response = http.post(configuration.getMerchantPath() + "/client_token", request);

      String token = response.findString("value");
      if (token != null) {
          token = StringUtils.unescapeUtf8(token);
      }
      return token;
    }

    private void verifyOptions(ClientTokenRequest request) {
        if (request.getCustomerId() != null || request.getOptions() == null) {
          return;
        }

        ArrayList<String> invalidOptions = new ArrayList<String>();
        if (request.getOptions().getMakeDefault() != null) {
            invalidOptions.add("makeDefault");
        }
        if (request.getOptions().getVerifyCard() != null) {
            invalidOptions.add("verifyCard");
        }
        if (request.getOptions().getFailOnDuplicatePaymentMethod() != null) {
            invalidOptions.add("failOnDuplicatePaymentMethod");
        }

        if (invalidOptions.size() != 0) {
          String message = "Following arguments are invalid without customerId:";
          for(String optionName: invalidOptions) {
            message += " " + optionName;
          }
          throw new IllegalArgumentException(message);
        }
    }
}
