package com.braintreegateway.unittest;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.braintreegateway.Adjustment;
import com.braintreegateway.testhelpers.CalendarTestUtils;
import com.braintreegateway.util.SimpleNodeWrapper;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AdjustmentTest {

  @Test
  public void testAdjustmentFields() throws ParseException {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    String date = sdf.format(new Date());
    String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                 "<adjustment>\n" +
                 "<kind>REFUND</kind>\n" +
                 "<amount>250.00</amount>\n" +
                 "<projected_disbursement_date type=\"date\">" + date + "</projected_disbursement_date>\n" +
                 "<actual_disbursement_date type=\"date\">" + date + "</actual_disbursement_date>\n" +
                 "</adjustment>\n";

    SimpleNodeWrapper adjustmentNode = SimpleNodeWrapper.parse(xml);
    Adjustment adjustment = new Adjustment(adjustmentNode);
    assertEquals(new BigDecimal("250.00"), adjustment.getAmount());
    assertEquals(Adjustment.KIND.REFUND, adjustment.getKind());
    assertEquals(CalendarTestUtils.dateTime(date), adjustment.getActualDisbursementDate());
    assertEquals(CalendarTestUtils.dateTime(date), adjustment.getProjectedDisbursementDate());
  }
}
