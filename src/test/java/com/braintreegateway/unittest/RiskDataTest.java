package com.braintreegateway.unittest;

import java.text.ParseException;
import java.util.List;
import java.util.Arrays;

import com.braintreegateway.RiskData;
import com.braintreegateway.LiabilityShift;
import com.braintreegateway.util.SimpleNodeWrapper;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RiskDataTest {
  @Test
  public void testRiskDataFields() throws ParseException {
    String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                 "<risk-data>\n" +
                 "<decision>decision</decision>\n" +
                 "<device-data-captured>true</device-data-captured>\n" +
                 "<fraud-service-provider>provider</fraud-service-provider>\n" +
                 "<id>heylookanid</id>\n" +
                 "<transaction-risk-score>50</transaction-risk-score>\n" +
                 "<decision-reasons type=\"array\">\n" +
                 "  <decision-reason>decision-reason1</decision-reason>\n" +
                 "  <decision-reason>decision-reason2</decision-reason>\n" +
                 "</decision-reasons>\n" +
                 "<liability-shift>\n" +
                 "<responsible-party>paypal</responsible-party>\n" +
                 "<conditions type=\"array\">\n" +
                 "  <condition>condition1</condition>\n" +
                 "  <condition>condition2</condition>\n" +
                 "</conditions>\n" +
                 "</liability-shift>\n" +
                 "</risk-data>\n";

    SimpleNodeWrapper riskDataNode = SimpleNodeWrapper.parse(xml);
    RiskData riskData = new RiskData(riskDataNode);
    LiabilityShift liabilityShift = riskData.getLiabilityShift();

    assertEquals("decision", riskData.getDecision());
    assertEquals("provider", riskData.getFraudServiceProvider());
    assertEquals("heylookanid", riskData.getId());
    assertEquals("50", riskData.getTransactionRiskScore());
    assertTrue(riskData.getDeviceDataCaptured());
    assertEquals(Arrays.asList("decision-reason1", "decision-reason2"), riskData.getDecisionReasons());
    assertEquals(liabilityShift, riskData.getLiabilityShift());
  }
}
