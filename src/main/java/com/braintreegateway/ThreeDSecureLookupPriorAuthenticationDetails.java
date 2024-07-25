package com.braintreegateway;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class ThreeDSecureLookupPriorAuthenticationDetails {
    private String acsTransactionId;
    private String authenticationMethod;
    private String authenticationTime;
    private String dsTransactionId;

    public ThreeDSecureLookupPriorAuthenticationDetails acsTransactionId(String acsTransactionId) {
        this.acsTransactionId = acsTransactionId;
        return this;
    }

    public ThreeDSecureLookupPriorAuthenticationDetails authenticationMethod(String authenticationMethod) {
        this.authenticationMethod = authenticationMethod;
        return this;
    }

    public ThreeDSecureLookupPriorAuthenticationDetails authenticationTime(Calendar authenticationTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        this.authenticationTime = dateFormat.format(authenticationTime.getTime());
        return this;
    }

    public ThreeDSecureLookupPriorAuthenticationDetails dsTransactionId(String dsTransactionId) {
        this.dsTransactionId = dsTransactionId;
        return this;
    }

    public String getAcsTransactionId() {
        return acsTransactionId;
    }

    public String getAuthenticationMethod() {
        return authenticationMethod;
    }

    public String getAuthenticationTime() {
        return authenticationTime;
    }

    public String getDsTransactionId() {
        return dsTransactionId;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> jsonMap = new HashMap<String, Object>();

        jsonMap.put("acs_transaction_id", getAcsTransactionId());
        jsonMap.put("authentication_method", getAuthenticationMethod());
        jsonMap.put("authentication_time", getAuthenticationTime());
        jsonMap.put("ds_transaction_id", getDsTransactionId());

        jsonMap.values().removeAll(Collections.singleton(null));

        return jsonMap;
    }
}
