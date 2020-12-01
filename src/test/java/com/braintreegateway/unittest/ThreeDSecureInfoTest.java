package com.braintreegateway.unittest;

import com.braintreegateway.ThreeDSecureInfo;
import com.braintreegateway.util.SimpleNodeWrapper;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ThreeDSecureInfoTest {
  @Test
  public void includesFields() {
    String xml = "<three-d-secure-info>" +
                 "<liability-shifted>true</liability-shifted>" +
                 "<liability-shift-possible>true</liability-shift-possible>" +
                 "<enrolled>Y</enrolled>" +
                 "<status>status</status>" +
                 "<cavv>imacavv</cavv>" +
                 "<eci-flag>05</eci-flag>" +
                 "<xid>1234</xid>" +
                 "<three-d-secure-version>2.0.0</three-d-secure-version>" +
                 "<ds-transaction-id>5678</ds-transaction-id>" +
                 "<three-d-secure-authentication-id>09</three-d-secure-authentication-id>" +
                 "<acs-transaction-id>ACS123</acs-transaction-id>" +
                 "<pares-status>Y</pares-status>" +
                 "<three-d-secure-server-transaction-id>3DS456</three-d-secure-server-transaction-id>" +
                 "<lookup>" +
                 "  <trans-status>status</trans-status>" +
                 "  <trans-status-reason>reason</trans-status-reason>" +
                 "</lookup>" +
                 "<authentication>" +
                 "  <trans-status>authstatus</trans-status>" +
                 "  <trans-status-reason>authreason</trans-status-reason>" +
                 "</authentication>" +
                 "</three-d-secure-info>";
    SimpleNodeWrapper node = SimpleNodeWrapper.parse(xml);

    ThreeDSecureInfo info = new ThreeDSecureInfo(node);

    assertTrue(info.isLiabilityShifted());
    assertTrue(info.isLiabilityShiftPossible());
    assertEquals("Y", info.getEnrolled());
    assertEquals("status", info.getStatus());
    assertEquals("imacavv", info.getCAVV());
    assertEquals("05", info.getECIFlag());
    assertEquals("1234", info.getXID());
    assertEquals("2.0.0", info.getThreeDSecureVersion());
    assertEquals("5678", info.getDsTransactionId());
    assertEquals("09", info.getThreeDSecureAuthenticationId());
    assertEquals("ACS123", info.getAcsTransactionId());
    assertEquals("3DS456", info.getThreeDSecureServerTransactionId());
    assertEquals("status", info.getThreeDSecureLookupInfo().getTransStatus());
    assertEquals("reason", info.getThreeDSecureLookupInfo().getTransStatusReason());
    assertEquals("authstatus", info.getThreeDSecureAuthenticateInfo().getTransStatus());
    assertEquals("authreason", info.getThreeDSecureAuthenticateInfo().getTransStatusReason());
  }
}
