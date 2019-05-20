package com.braintreegateway;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.braintreegateway.exceptions.UnexpectedException;
import com.fasterxml.jackson.jr.ob.JSON;

public class ThreeDSecureLookupRequest extends Request {
    private ThreeDSecureLookupAdditionalInformation additionalInformation;
    private String amount;
    private String nonce;
    private String authorizationFingerprint;
    private String dfReferenceId;
    private String braintreeLibraryVersion;
    private Map clientMetadata;
    private Boolean challengeRequested;

    public ThreeDSecureLookupRequest() {}

    public ThreeDSecureLookupRequest additionalInformation(ThreeDSecureLookupAdditionalInformation additionalInformation) {
        this.additionalInformation = additionalInformation;
        return this;
    }

    public ThreeDSecureLookupRequest amount(String amount) {
        this.amount = amount;
        return this;
    }

    public ThreeDSecureLookupRequest clientData(String clientData) {
        Map<String, Object> jsonMap;

        try {
            jsonMap = JSON.std.mapFrom(clientData);
        } catch (IOException e) {
            throw new UnexpectedException(e.getMessage(), e);
        }

        this.nonce = (String) jsonMap.get("nonce");
        this.authorizationFingerprint = (String) jsonMap.get("authorizationFingerprint");
        this.braintreeLibraryVersion = (String) jsonMap.get("braintreeLibraryVersion");
        this.dfReferenceId = (String) jsonMap.get("dfReferenceId");
        this.clientMetadata = (Map) jsonMap.get("clientMetadata");
        return this;
    }

    public ThreeDSecureLookupRequest challengeRequested(Boolean challengeRequested) {
        this.challengeRequested = challengeRequested;
        return this;
    }

    private ThreeDSecureLookupAdditionalInformation getAdditionalInformation() {
        return additionalInformation;
    }

    public String getNonce() {
        return nonce;
    }

    private String getAuthorizationFingerprint() {
        return authorizationFingerprint;
    }

    private String getBraintreeLibraryVersion() {
        return braintreeLibraryVersion;
    }

    private String getDfReferenceId() {
        return dfReferenceId;
    }

    public String getAmount() {
        return amount;
    }

    private Map getClientMetadata() {
        return clientMetadata;
    }

    private Boolean getChallengeRequested() {
        return challengeRequested;
    }

    public String toJSON() {
        Object additionalInfo;
        Map<String, Object> jsonMap = new HashMap<>();
        Map<String, Object> metaMap = new HashMap<>();

        if (this.additionalInformation != null) {
            additionalInfo = this.additionalInformation;
        } else {
            additionalInfo = new Object();
        }

        try {
            metaMap.put("platform", "java");
            metaMap.put("sdkVersion", Configuration.VERSION);
            metaMap.put("source", "http");

            jsonMap.put("authorizationFingerprint", getAuthorizationFingerprint());
            jsonMap.put("amount", getAmount());
            jsonMap.put("braintreeLibraryVersion", getBraintreeLibraryVersion());
            jsonMap.put("df_reference_id", getDfReferenceId());
            jsonMap.put("additional_info", additionalInfo);
            jsonMap.put("clientMetadata", getClientMetadata());
            jsonMap.put("_meta", metaMap);
            jsonMap.put("challengeRequested", getChallengeRequested());

            return JSON.std.asString(jsonMap);
        } catch (IOException e) {
            throw new UnexpectedException(e.getMessage(), e);
        }
    }
}
