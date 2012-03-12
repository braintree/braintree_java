package com.braintreegateway;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import com.braintreegateway.org.apache.commons.codec.binary.Base64;
import com.braintreegateway.util.Crypto;

public class WebhookTestingGateway {
    private Configuration configuration;

    public WebhookTestingGateway(Configuration configuration) {
        this.configuration = configuration;
    }

    private String buildPayload(Webhook.Kind kind, String id) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        String timestamp = dateFormat.format(cal.getTime());
        String payload = "<notification><timestamp>" + timestamp + "</timestamp><kind>" + kind + "</kind><subject>" + subscriptionXml(id) + "</subject></notification>";

        return Base64.encodeBase64String(payload.getBytes()).trim();
    }

    private String publicKeySignaturePair(String stringToSign) {
        return String.format("%s|%s", configuration.publicKey, new Crypto().hmacHash(configuration.privateKey, stringToSign));
    }

    public HashMap<String, String> sampleNotification(Webhook.Kind kind, String id) {
        HashMap<String, String> response = new HashMap<String, String>();
        String payload = buildPayload(kind, id);
        response.put("payload", payload);
        response.put("signature", publicKeySignaturePair(payload));

        return response;
    }

    private String subscriptionXml(String id) {
        return "<subscription><id>" + id + "</id><transactions type=\"array\"></transactions><add_ons type=\"array\"></add_ons><discounts type=\"array\"></discounts></subscription>";
    }
}