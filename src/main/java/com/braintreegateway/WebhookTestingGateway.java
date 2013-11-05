package com.braintreegateway;

import com.braintreegateway.org.apache.commons.codec.binary.Base64;
import com.braintreegateway.util.Sha1Hasher;

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
        return String.format("%s|%s", configuration.publicKey, new Sha1Hasher().hmacHash(configuration.privateKey, stringToSign));
    }

    public HashMap<String, String> sampleNotification(WebhookNotification.Kind kind, String id) {
        HashMap<String, String> response = new HashMap<String, String>();
        String payload = buildPayload(kind, id);
        response.put("payload", payload);
        response.put("signature", publicKeySignaturePair(payload));

        return response;
    }

    private String subjectXml(WebhookNotification.Kind kind, String id) {
        switch(kind) {
            case SUB_MERCHANT_ACCOUNT_APPROVED: return merchantAccountXmlActive(id);
            case SUB_MERCHANT_ACCOUNT_DECLINED: return merchantAccountXmlDeclined(id);
            case TRANSACTION_DISBURSED: return transactionXml(id);
            case PARTNER_MERCHANT_CONNECTED: return partnerMerchantConnectedXml(id);
            case PARTNER_MERCHANT_DISCONNECTED: return partnerMerchantDisconnectedXml(id);
            case PARTNER_MERCHANT_DECLINED: return partnerMerchantDeclinedXml(id);
            default: return subscriptionXml(id);
        }
    }

    private String[][] TYPE_DATETIME = {{"type", "datetime"}};
    private String[][] TYPE_ARRAY = {{"type", "array"}};
    private String[][] TYPE_SYMBOL = {{"type", "symbol"}};

    private String merchantAccountXmlDeclined(String id) {
        return node("api-error-response",
                node("message", "Credit score is too low"),
                node("errors", TYPE_ARRAY,
                    node("merchant-account",
                        node("errors", TYPE_ARRAY,
                            node("error",
                                node("code", "82621"),
                                node("message", "Credit score is too low"),
                                node("attribute", TYPE_SYMBOL, "base")
                            )
                        )
                    )
                ),
                node("merchant-account",
                    node("id", id),
                    node("status", "suspended"),
                    node("master-merchant-account",
                        node("id", "master_ma_for_" + id),
                        node("status", "suspended")
                    )
                )
        );
    }

    private String merchantAccountXmlActive(String id) {
          return node("merchant-account",
                  node("id", id),
                  node("master-merchant-account",
                      node("id", id),
                      node("status", "active")
                  ),
                  node("status", "active")
        );
    }

    private String subscriptionXml(String id) {
        return node("subscription",
                node("id", id),
                node("transactions", TYPE_ARRAY),
                node("add_ons", TYPE_ARRAY),
                node("discounts", TYPE_ARRAY)
        );
    }

    private String transactionXml(String id) {
        return node("transaction",
                node("id", id),
                node("amount", "100"),
                node("disbursement-details",
                    node("disbursement-date", TYPE_DATETIME, "2013-07-09T18:23:29Z")
                ),
                node("billing"),
                node("credit-card"),
                node("customer"),
                node("descriptor"),
                node("shipping"),
                node("subscription")
        );
    }

    private String partnerMerchantConnectedXml(String id) {
        return node("partner-merchant",
                node("partner-merchant-id", "abc123"),
                node("merchant-public-id", "public_id"),
                node("public-key", "public_key"),
                node("private-key", "private_key"),
                node("client-side-encryption-key", "cse_key")
        );
    }

    private String partnerMerchantDisconnectedXml(String id) {
        return node("partner-merchant",
                node("partner-merchant-id", "abc123")
        );
    }

    private String partnerMerchantDeclinedXml(String id) {
        return node("partner-merchant",
                node("partner-merchant-id", "abc123")
        );
    }

    private static String node(String name, String... contents) {
        return node(name, null, contents);
    }

    private static String node(String name, String[][] attributes, String... contents) {
        StringBuilder buffer = new StringBuilder();
        buffer.append('<').append(name);
        if (attributes != null) {
            for (String[] pair : attributes) {
                buffer.append(" ");
                buffer.append(pair[0]).append("=\"").append(pair[1]).append("\"");
            }
        }
        buffer.append('>');
        for (String content : contents) {
            buffer.append(content);
        }
        buffer.append("</").append(name).append('>');
        return buffer.toString();
    }
}
