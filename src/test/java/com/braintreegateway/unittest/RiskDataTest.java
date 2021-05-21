package com.braintreegateway.unittest;

import java.text.ParseException;
import java.util.List;

import com.braintreegateway.RiskData;
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
                 "  <decision-reason>eeny</decision-reason>\n" +
                 "  <decision-reason>meeny</decision-reason>\n" +
                 "</decision-reasons>\n" +
                 "</risk-data>\n";

    SimpleNodeWrapper riskDataNode = SimpleNodeWrapper.parse(xml);
    RiskData riskData = new RiskData(riskDataNode);
    List<String> decisions = riskData.getDecisionReasons();

    assertEquals("decision", riskData.getDecision());
    assertEquals("provider", riskData.getFraudServiceProvider());
    assertEquals("heylookanid", riskData.getId());
    assertEquals("50", riskData.getTransactionRiskScore());
    assertTrue(riskData.getDeviceDataCaptured());
    assertEquals(decisions, riskData.getDecisionReasons());
  }
}
