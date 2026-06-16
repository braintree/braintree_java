package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.braintreegateway.AuthenticationInsight;
import com.braintreegateway.util.SimpleNodeWrapper;

public class AuthenticationInsightTest {

    @Test
    public void parsesFromNodeWrapper() {
        SimpleNodeWrapper node = SimpleNodeWrapper.parse(
                "<authentication-insight>" +
                "<regulation-environment>psd2</regulation-environment>" +
                "<sca-indicator>required</sca-indicator>" +
                "<message>an insight message</message>" +
                "</authentication-insight>");

        AuthenticationInsight insight = new AuthenticationInsight(node);

        assertAll("authentication insight from node",
                () -> assertEquals("psd2", insight.getRegulationEnvironment()),
                () -> assertEquals("required", insight.getScaIndicator()),
                () -> assertEquals("an insight message", insight.getMessage()));
    }

    @Test
    public void parsesFromMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("regulationEnvironment", "psd2");
        map.put("scaIndicator", "required");
        map.put("message", "an insight message");

        AuthenticationInsight insight = new AuthenticationInsight(map);

        assertAll("authentication insight from map",
                () -> assertEquals("psd2", insight.getRegulationEnvironment()),
                () -> assertEquals("required", insight.getScaIndicator()),
                () -> assertEquals("an insight message", insight.getMessage()));
    }
}
