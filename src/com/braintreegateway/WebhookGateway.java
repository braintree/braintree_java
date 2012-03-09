package com.braintreegateway;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import com.braintreegateway.Webhook;
import com.braintreegateway.exceptions.InvalidSignatureException;
import com.braintreegateway.org.apache.commons.codec.binary.Base64;
import com.braintreegateway.util.Crypto;
import com.braintreegateway.util.NodeWrapper;
import com.braintreegateway.util.NodeWrapperFactory;

public class WebhookGateway {
	private Configuration configuration;

	public WebhookGateway(Configuration configuration) {
		this.configuration = configuration;
	}

	private String buildPayload(Webhook.Kind kind, String id) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		String timestamp = dateFormat.format(cal.getTime());
		String payload = "<notification><timestamp>" + timestamp + "</timestamp><kind>" + kind + "</kind><subject>" + subscriptionXml(id) + "</subject></notification>";
		
		return Base64.encodeBase64String(payload.getBytes()).trim();
	}

	public Notification parse(String signature, String payload) {
		validateSignature(signature, payload);
		String xmlPayload = new String(Base64.decodeBase64(payload));
		NodeWrapper node = NodeWrapperFactory.instance.create(xmlPayload);
		return new Notification(node);
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
		
		String computedSignature = new Crypto().hmacHash(configuration.privateKey, payload);
		if (!computedSignature.equals(matchingSignature)) {
			throw new InvalidSignatureException();
		}
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

	public String verify(String challenge) {
		return publicKeySignaturePair(challenge);
	}
	
	private String publicKeySignaturePair(String stringToSign) {
		return String.format("%s|%s", configuration.publicKey, new Crypto().hmacHash(configuration.privateKey, stringToSign));
	}
}
