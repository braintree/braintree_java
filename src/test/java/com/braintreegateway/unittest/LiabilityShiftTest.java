package com.braintreegateway.unittest;

import java.text.ParseException;
import java.util.List;

import com.braintreegateway.LiabilityShift;
import com.braintreegateway.util.SimpleNodeWrapper;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LiabilityShiftTest {
  @Test
  public void testLiabilityShiftFields() throws ParseException {
    String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                 "<liability-shifts>\n" +
                 "<responsible-party>paypal</responsible-party>\n" +
                 "<conditions type=\"array\">\n" +
                 "  <condition>eeny</condition>\n" +
                 "  <condition>meeny</condition>\n" +
                 "</conditions>\n" +
                 "</liability-shifts>\n";

    SimpleNodeWrapper liabilityShiftNode = SimpleNodeWrapper.parse(xml);
    LiabilityShift liabilityShift = new LiabilityShift(liabilityShiftNode);
    List<String> conditions = liabilityShift.getConditions();

    assertEquals("paypal", liabilityShift.getResponsibleParty());
    assertEquals(conditions, liabilityShift.getConditions());
  }
}
