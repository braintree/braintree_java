package com.braintreegateway;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import com.braintreegateway.exceptions.InvalidChallengeException;
import com.braintreegateway.exceptions.InvalidSignatureException;
import com.braintreegateway.org.apache.commons.codec.binary.Base64;
import com.braintreegateway.util.Crypto;
import com.braintreegateway.util.Sha1Hasher;
import com.braintreegateway.util.NodeWrapper;
import com.braintreegateway.util.NodeWrapperFactory;

public class WebhookNotificationGateway {
    private Configuration configuration;

    public WebhookNotificationGateway(Configuration configuration) {
        this.configuration = configuration;
    }

    public WebhookNotification parse(String signature, String payload) {
        if (signature == null) {
            throw new InvalidSignatureException("signature cannot be null");
        }
        if (payload == null) {
            throw new InvalidSignatureException("payload cannot be null");
        }
        Pattern p = Pattern.compile("[^A-Za-z0-9+=/\n]");
        Matcher m = p.matcher(payload);
        if (m.find()) {
          throw new InvalidSignatureException("payload contains illegal characters");
        }
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
                if (this.configuration.getPublicKey().equals(candidatePair[0])) {
                    matchingSignature = candidatePair[1];
                    break;
                }
            }
        }

        if (matchingSignature == null) {
          throw new InvalidSignatureException("no matching public key");
        }

        if (!(matchSignature(payload, matchingSignature) || matchSignature(payload + "\n", matchingSignature))) {
            throw new InvalidSignatureException("signature does not match payload - one has been modified");
        }
    }

    private Boolean matchSignature(String payload, String matchingSignature) {
      String computedSignature = new Sha1Hasher().hmacHash(configuration.getPrivateKey(), payload);
      return new Crypto().secureCompare(computedSignature, matchingSignature);
    }

    public String verify(String challenge) {
        if (!challenge.matches("^[a-f0-9]{20,32}$")) {
          throw new InvalidChallengeException("challenge contains non-hex characters");
        }
        return publicKeySignaturePair(challenge);
    }

    private String publicKeySignaturePair(String stringToSign) {
        return String.format("%s|%s", configuration.getPublicKey(), new Sha1Hasher().hmacHash(configuration.getPrivateKey(), stringToSign));
    }
}
