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
    private String email;
    private String authorizationFingerprint;
    private String dfReferenceId;
    private String braintreeLibraryVersion;
    private ThreeDSecureLookupAddress billingAddress;
    private Map clientMetadata;
    private Boolean challengeRequested;
    private Boolean exemptionRequested;

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

    public ThreeDSecureLookupRequest exemptionRequested(Boolean exemptionRequested) {
        this.exemptionRequested = exemptionRequested;
        return this;
    }

    public ThreeDSecureLookupRequest email(String email) {
        this.email = email;
        return this;
    }

    public ThreeDSecureLookupRequest billingAddress(ThreeDSecureLookupAddress billingAddress) {
        this.billingAddress = billingAddress;
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

    private Boolean getExemptionRequested() { return exemptionRequested; }

    private String getEmail() { return email; }

    public String toJSON() {
        Map<String, Object> additionalInfo;
        Map<String, Object> jsonMap = new HashMap<>();
        Map<String, Object> metaMap = new HashMap<>();

        if (this.additionalInformation != null) {
            additionalInfo = this.additionalInformation.toMap();
        } else {
            additionalInfo = new HashMap<>();
        }

        try {
            metaMap.put("platform", "java");
            metaMap.put("sdkVersion", Configuration.VERSION);
            metaMap.put("source", "http");

            jsonMap.put("authorizationFingerprint", getAuthorizationFingerprint());
            jsonMap.put("email", getEmail());
            jsonMap.put("amount", getAmount());
            jsonMap.put("braintreeLibraryVersion", getBraintreeLibraryVersion());
            jsonMap.put("df_reference_id", getDfReferenceId());
            jsonMap.put("additional_info", additionalInfo);
            jsonMap.put("clientMetadata", getClientMetadata());
            jsonMap.put("_meta", metaMap);
            jsonMap.put("challengeRequested", getChallengeRequested());
            jsonMap.put("exemptionRequested", getExemptionRequested());

            if (billingAddress != null) {
                jsonMap.put("billingGivenName", billingAddress.getGivenName());
                jsonMap.put("billingSurname", billingAddress.getSurname());
                jsonMap.put("billingPhoneNumber", billingAddress.getPhoneNumber());
                jsonMap.put("billingCity", billingAddress.getLocality());
                jsonMap.put("billingCountryCode", billingAddress.getCountryCodeAlpha2());
                jsonMap.put("billingLine1", billingAddress.getStreetAddress());
                jsonMap.put("billingLine2", billingAddress.getExtendedAddress());
                jsonMap.put("billingPostalCode", billingAddress.getPostalCode());
                jsonMap.put("billingState", billingAddress.getRegion());
            }

            return JSON.std.asString(jsonMap);
        } catch (IOException e) {
            throw new UnexpectedException(e.getMessage(), e);
        }
    }
}
