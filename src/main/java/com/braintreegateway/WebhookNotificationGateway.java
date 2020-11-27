package com.braintreegateway;

import com.braintreegateway.exceptions.InvalidChallengeException;
import com.braintreegateway.exceptions.InvalidSignatureException;
import com.braintreegateway.org.apache.commons.codec.binary.Base64;
import com.braintreegateway.util.Crypto;
import com.braintreegateway.util.NodeWrapper;
import com.braintreegateway.util.NodeWrapperFactory;
import com.braintreegateway.util.Sha1Hasher;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebhookNotificationGateway {
    
    private static final Pattern PAYLOAD_CHARS_PATTERN = Pattern.compile("[^A-Za-z0-9+=/\n]");
    private static final Pattern CHALLENGE_CHARS_PATTERN = Pattern.compile("^[a-f0-9]{20,32}$");
    
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
        Matcher m = PAYLOAD_CHARS_PATTERN.matcher(payload);
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
        if (!CHALLENGE_CHARS_PATTERN.matcher(challenge).matches()) {
          throw new InvalidChallengeException("challenge contains non-hex characters");
        }
        return publicKeySignaturePair(challenge);
    }

    private String publicKeySignaturePair(String stringToSign) {
        return String.format("%s|%s", configuration.getPublicKey(), new Sha1Hasher().hmacHash(configuration.getPrivateKey(), stringToSign));
    }
}
