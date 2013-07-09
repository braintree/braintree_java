package com.braintreegateway;

import com.braintreegateway.org.apache.commons.codec.binary.Base64;
import com.braintreegateway.util.Crypto;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

public class WebhookTestingGateway {
    private Configuration configuration;

    public WebhookTestingGateway(Configuration configuration) {
        this.configuration = configuration;
    }

    private String buildPayload(WebhookNotification.Kind kind, String id) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String timestamp = dateFormat.format(new Date());
        String payload = "<notification><timestamp type=\"datetime\">" + timestamp + "</timestamp><kind>" + kind + "</kind><subject>" + subjectXml(kind, id) + "</subject></notification>";

        return Base64.encodeBase64String(payload.getBytes()).trim();
    }

    private String publicKeySignaturePair(String stringToSign) {
        return String.format("%s|%s", configuration.publicKey, new Crypto().hmacHash(configuration.privateKey, stringToSign));
    }

    public HashMap<String, String> sampleNotification(WebhookNotification.Kind kind, String id) {
        HashMap<String, String> response = new HashMap<String, String>();
        String payload = buildPayload(kind, id);
        response.put("payload", payload);
        response.put("signature", publicKeySignaturePair(payload));

        return response;
    }

    private String subjectXml(WebhookNotification.Kind kind, String id) {
        if (kind == WebhookNotification.Kind.SUB_MERCHANT_ACCOUNT_APPROVED) {
            return merchantAccountXmlActive(id);
        } else if (kind == WebhookNotification.Kind.SUB_MERCHANT_ACCOUNT_DECLINED) {
            return merchantAccountXmlDeclined(id);
        } else {
            return subscriptionXml(id);
        }
    }

    private String merchantAccountXmlDeclined(String id) {
        return "<api-error-response> <message>Credit score is too low</message> <errors> <errors type=\"array\"/> <merchant-account> <errors type=\"array\"> <error> <code>82621</code> <message>Credit score is too low</message> <attribute type=\"symbol\">base</attribute> </error> </errors> </merchant-account> </errors> <merchant-account> <id>" + id + "</id> <status>suspended</status> <master-merchant-account> <id>master_ma_for_" + id + "</id> <status>suspended</status> </master-merchant-account> </merchant-account> </api-error-response>";
    }

    private String merchantAccountXmlActive(String id) {
        return "<merchant-account><id>" + id + "</id><master-merchant-account><id>master_merchant_account</id><status>active</status></master-merchant-account><status>active</status></merchant-account>";
    }

    private String subscriptionXml(String id) {
        return "<subscription><id>" + id + "</id><transactions type=\"array\"></transactions><add_ons type=\"array\"></add_ons><discounts type=\"array\"></discounts></subscription>";
    }
}
