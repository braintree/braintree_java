package com.braintreegateway;

import com.braintreegateway.exceptions.UnexpectedException;
import com.fasterxml.jackson.jr.ob.JSON;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class ThreeDSecureLookupRequest extends Request {
    private ThreeDSecureLookupAdditionalInformation additionalInformation;
    private String amount;
    private String authorizationFingerprint;
    private ThreeDSecureLookupAddress billingAddress;
    private String braintreeLibraryVersion;
    private Boolean cardAddChallengeRequested;
    private Boolean challengeRequested;
    private Map clientMetadata;
    private Boolean dataOnlyRequested;
    private String dfReferenceId;
    private String email;
    private Boolean exemptionRequested;
    private String merchantAccountId;
    private String merchantInitiatedRequestType;
    private String merchantOnRecordName;
    private String nonce;
    private ThreeDSecureLookupPriorAuthenticationDetails priorAuthenticationDetails;
    private String priorAuthenticationId;
    private String requestedExemptionType;

    private String browserAcceptHeader;
    private String browserColorDepth;
    private Boolean browserJavaEnabled;
    private Boolean browserJavascriptEnabled;
    private String browserLanguage;
    private String browserScreenHeight;
    private String browserScreenWidth;
    private String browserTimeZone;
    private String deviceChannel;
    private String ipAddress;
    private String userAgent;

    public ThreeDSecureLookupRequest() {

    }

    public ThreeDSecureLookupRequest additionalInformation(ThreeDSecureLookupAdditionalInformation additionalInformation) {
        this.additionalInformation = additionalInformation;
        return this;
    }

    public ThreeDSecureLookupRequest amount(String amount) {
        this.amount = amount;
        return this;
    }

    public ThreeDSecureLookupRequest billingAddress(ThreeDSecureLookupAddress billingAddress) {
        this.billingAddress = billingAddress;
        return this;
    }

    public ThreeDSecureLookupRequest cardAddChallengeRequested(Boolean cardAddChallengeRequested) {
        this.cardAddChallengeRequested = cardAddChallengeRequested;
        return this;
    }

    public ThreeDSecureLookupRequest challengeRequested(Boolean challengeRequested) {
        this.challengeRequested = challengeRequested;
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

    public ThreeDSecureLookupRequest dataOnlyRequested(Boolean dataOnlyRequested) {
        this.dataOnlyRequested = dataOnlyRequested;
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

    public ThreeDSecureLookupRequest merchantAccountId(String merchantAccountId) {
        this.merchantAccountId = merchantAccountId;
        return this;
    }

    public ThreeDSecureLookupRequest merchantInitiatedRequestType(String merchantInitiatedRequestType) {
        this.merchantInitiatedRequestType = merchantInitiatedRequestType;
        return this;
    }

    public ThreeDSecureLookupRequest merchantOnRecordName(String merchantOnRecordName) {
        this.merchantOnRecordName = merchantOnRecordName;
        return this;
    }
  
    public ThreeDSecureLookupRequest priorAuthenticationDetails(ThreeDSecureLookupPriorAuthenticationDetails priorAuthenticationDetails) {
        this.priorAuthenticationDetails = priorAuthenticationDetails;
        return this;
    }

    public ThreeDSecureLookupRequest priorAuthenticationId(String priorAuthenticationId) {
        this.priorAuthenticationId = priorAuthenticationId;
        return this;
    }

    public ThreeDSecureLookupRequest requestedExemptionType(String requestedExemptionType) {
        this.requestedExemptionType = requestedExemptionType;
        return this;
    }

    public ThreeDSecureLookupRequest browserJavaEnabled(Boolean enabled) {
        this.browserJavaEnabled = enabled;
        return this;
    }

    public ThreeDSecureLookupRequest browserAcceptHeader(String header) {
        this.browserAcceptHeader = header;
        return this;
    }

    public ThreeDSecureLookupRequest browserLanguage(String language) {
        this.browserLanguage = language;
        return this;
    }

    public ThreeDSecureLookupRequest browserColorDepth(String depth) {
        this.browserColorDepth = depth;
        return this;
    }

    public ThreeDSecureLookupRequest browserJavascriptEnabled(Boolean enabled) {
        this.browserJavascriptEnabled = enabled;
        return this;
    }

    public ThreeDSecureLookupRequest browserScreenHeight(String height) {
        this.browserScreenHeight = height;
        return this;
    }

    public ThreeDSecureLookupRequest browserScreenWidth(String width) {
        this.browserScreenWidth = width;
        return this;
    }

    public ThreeDSecureLookupRequest browserTimeZone(String zone) {
        this.browserTimeZone = zone;
        return this;
    }

    public ThreeDSecureLookupRequest deviceChannel(String channel) {
        this.deviceChannel = channel;
        return this;
    }

    public ThreeDSecureLookupRequest ipAddress(String address) {
        this.ipAddress = address;
        return this;
    }

    public ThreeDSecureLookupRequest userAgent(String agent) {
        this.userAgent = agent;
        return this;
    }

    public String getAmount() {
        return amount;
    }

    private String getAuthorizationFingerprint() {
        return authorizationFingerprint;
    }

    private String getBraintreeLibraryVersion() {
        return braintreeLibraryVersion;
    }

    private Boolean getCardAddChallengeRequested() {
        return cardAddChallengeRequested;
    }

    private Boolean getChallengeRequested() {
        return challengeRequested;
    }

    private Map getClientMetadata() {
        return clientMetadata;
    }

    private Boolean getDataOnlyRequested() {
        return dataOnlyRequested;
    }

    private String getDfReferenceId() {
        return dfReferenceId;
    }

    private String getEmail() {
        return email;
    }

    private Boolean getExemptionRequested() {
        return exemptionRequested;
    }

    private String getMerchantAccountId() {
        return merchantAccountId;
    }

    private String getMerchantInitiatedRequestType() {
        return merchantInitiatedRequestType;
    }

    private String getMerchantOnRecordName() {
        return merchantOnRecordName;
    }

    public String getNonce() {
        return nonce;
    }

    private String getPriorAuthenticationId() {
        return priorAuthenticationId;
    }

    private String getRequestedExemptionType() {
        return requestedExemptionType;
    }

    private String getBrowserAcceptHeader() {
        return browserAcceptHeader;
    }

    private String getBrowserColorDepth() {
        return browserColorDepth;
    }

    private Boolean getBrowserJavaEnabled() {
        return browserJavaEnabled;
    }

    private Boolean getBrowserJavascriptEnabled() {
        return browserJavascriptEnabled;
    }

    private String getBrowserLanguage() {
        return browserLanguage;
    }

    private String getBrowserScreenHeight() {
        return browserScreenHeight;
    }

    private String getBrowserScreenWidth() {
        return browserScreenWidth;
    }

    private String getBrowserTimeZone() {
        return browserTimeZone;
    }

    private String getDeviceChannel() {
        return deviceChannel;
    }

    private String getIpAddress() {
        return ipAddress;
    }

    private String getUserAgent() {
        return userAgent;
    }

    public String toJSON() {
        Map<String, Object> additionalInfo;
        Map<String, Object> priorAuthenticationDetails;

        Map<String, Object> jsonMap = new HashMap<>();
        Map<String, Object> metaMap = new HashMap<>();

        if (this.additionalInformation != null) {
            additionalInfo = this.additionalInformation.toMap();
        } else {
            additionalInfo = new HashMap<>();
        }

        if (this.priorAuthenticationDetails != null) {
            priorAuthenticationDetails = this.priorAuthenticationDetails.toMap();
        } else {
            priorAuthenticationDetails = new HashMap<>();
        }

        try {
            metaMap.put("platform", "java");
            metaMap.put("sdkVersion", Configuration.VERSION);
            metaMap.put("source", "http");

            if (billingAddress != null) {
                additionalInfo.put("billingCity", billingAddress.getLocality());
                additionalInfo.put("billingCountryCode", billingAddress.getCountryCodeAlpha2());
                additionalInfo.put("billingGivenName", billingAddress.getGivenName());
                additionalInfo.put("billingLine1", billingAddress.getStreetAddress());
                additionalInfo.put("billingLine2", billingAddress.getExtendedAddress());
                additionalInfo.put("billingPhoneNumber", billingAddress.getPhoneNumber());
                additionalInfo.put("billingPostalCode", billingAddress.getPostalCode());
                additionalInfo.put("billingState", billingAddress.getRegion());
                additionalInfo.put("billingSurname", billingAddress.getSurname());
            }

            jsonMap.put("_meta", metaMap);
            jsonMap.put("additional_info", additionalInfo);
            jsonMap.put("authorizationFingerprint", getAuthorizationFingerprint());
            jsonMap.put("amount", getAmount());
            jsonMap.put("braintreeLibraryVersion", getBraintreeLibraryVersion());
            jsonMap.put("cardAddChallengeRequested", getCardAddChallengeRequested());
            jsonMap.put("challengeRequested", getChallengeRequested());
            jsonMap.put("clientMetadata", getClientMetadata());
            jsonMap.put("dataOnlyRequested", getDataOnlyRequested());
            jsonMap.put("df_reference_id", getDfReferenceId());
            jsonMap.put("email", getEmail());
            jsonMap.put("exemptionRequested", getExemptionRequested());
            jsonMap.put("merchantAccountId", getMerchantAccountId());
            jsonMap.put("merchantInitiatedRequestType", getMerchantInitiatedRequestType());
            jsonMap.put("merchantOnRecordName", getMerchantOnRecordName());
            jsonMap.put("prior_authentication_details", priorAuthenticationDetails);
            jsonMap.put("priorAuthenticationId", getPriorAuthenticationId());
            jsonMap.put("requestedExemptionType", getRequestedExemptionType());

            jsonMap.put("browserColorDepth", getBrowserColorDepth());
            jsonMap.put("browserHeader", getBrowserAcceptHeader());
            jsonMap.put("browserJavaEnabled", getBrowserJavaEnabled());
            jsonMap.put("browserJavascriptEnabled", getBrowserJavascriptEnabled());
            jsonMap.put("browserLanguage", getBrowserLanguage());
            jsonMap.put("browserScreenHeight", getBrowserScreenHeight());
            jsonMap.put("browserScreenWidth", getBrowserScreenWidth());
            jsonMap.put("browserTimeZone", getBrowserTimeZone());
            jsonMap.put("deviceChannel", getDeviceChannel());
            jsonMap.put("ipAddress", getIpAddress());
            jsonMap.put("userAgent", getUserAgent());

            return JSON.std.asString(jsonMap);
        } catch (IOException e) {
            throw new UnexpectedException(e.getMessage(), e);
        }
    }
}
