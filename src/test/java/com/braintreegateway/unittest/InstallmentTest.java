package com.braintreegateway.unittest;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.braintreegateway.Adjustment;
import com.braintreegateway.Installment;
import com.braintreegateway.testhelpers.CalendarTestUtils;
import com.braintreegateway.util.SimpleNodeWrapper;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class InstallmentTest {

  @Test
  public void testInstallmentFields() throws ParseException {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    String date = sdf.format(new Date());
    String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                 "<tinstallment>\n" +
                 "<id>test_id</id>\n" +
                 "<amount>250.00</amount>\n" +
                 "<projected_disbursement_date type=\"date\">" + date + "</projected_disbursement_date>\n" +
                 "<actual_disbursement_date type=\"date\">" + date + "</actual_disbursement_date>\n" +
                 "</tinstallment>\n";

    SimpleNodeWrapper installmentNode = SimpleNodeWrapper.parse(xml);
    Installment installment = new Installment(installmentNode);
    assertEquals(new BigDecimal("250.00"), installment.getAmount());
    assertEquals("test_id", installment.getId());
    assertEquals(CalendarTestUtils.dateTime(date), installment.getActualDisbursementDate());
    assertEquals(CalendarTestUtils.dateTime(date), installment.getProjectedDisbursementDate());
  }

  @Test
  public void parsesAdjustments() {
    SimpleNodeWrapper node = SimpleNodeWrapper.parse(
            "<installment>" +
            "<id>inst-id</id>" +
            "<amount>10.00</amount>" +
            "<projected_disbursement_date type=\"datetime\">2024-01-15T00:00:00Z</projected_disbursement_date>" +
            "<actual_disbursement_date type=\"datetime\">2024-01-16T00:00:00Z</actual_disbursement_date>" +
            "<adjustments type=\"array\">" +
            "<adjustment><amount>2.00</amount><kind>REFUND</kind></adjustment>" +
            "</adjustments>" +
            "</installment>");

    Installment installment = new Installment(node);
    Adjustment adjustment = installment.getAdjustments().get(0);

    assertAll("adjustment fields",
            () -> assertEquals(1, installment.getAdjustments().size()),
            () -> assertEquals(new BigDecimal("2.00"), adjustment.getAmount()),
            () -> assertEquals(Adjustment.KIND.REFUND, adjustment.getKind()));
  }
}
