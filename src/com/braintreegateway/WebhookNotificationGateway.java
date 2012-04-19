package com.braintreegateway;

import com.braintreegateway.exceptions.InvalidSignatureException;
import com.braintreegateway.org.apache.commons.codec.binary.Base64;
import com.braintreegateway.util.Crypto;
import com.braintreegateway.util.NodeWrapper;
import com.braintreegateway.util.NodeWrapperFactory;

public class WebhookNotificationGateway {
    private Configuration configuration;

    public WebhookNotificationGateway(Configuration configuration) {
        this.configuration = configuration;
    }

    public WebhookNotification parse(String signature, String payload) {
        validateSignature(signature, payload);
        String xmlPayload = new String(Base64.decodeBase64(payload));
        NodeWrapper node = NodeWrapperFactory.instance.create(xmlPayload);
        return new WebhookNotification(node);
    }

    private void validateSignature(String signature, String payload) {
        String matchingSignature = null;

        String[] signaturePairs = signature.split("&");
        for (String signaturePair : signaturePairs) {
            if (signaturePair.indexOf("|") >= 0) {
                String[] candidatePair = signaturePair.split("\\|");
                if (this.configuration.publicKey.equals(candidatePair[0])) {
                    matchingSignature = candidatePair[1];
                    break;
                }
            }
        }

        Crypto crypto = new Crypto();
        String computedSignature = crypto.hmacHash(configuration.privateKey, payload);
        if (!crypto.secureCompare(computedSignature, matchingSignature)) {
            throw new InvalidSignatureException();
        }
    }

    public String verify(String challenge) {
        return publicKeySignaturePair(challenge);
    }

    private String publicKeySignaturePair(String stringToSign) {
        return String.format("%s|%s", configuration.publicKey, new Crypto().hmacHash(configuration.privateKey, stringToSign));
    }
}
