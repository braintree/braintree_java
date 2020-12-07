package com.braintreegateway.unittest;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.braintreegateway.Installment;
import com.braintreegateway.testhelpers.CalendarTestUtils;
import com.braintreegateway.util.SimpleNodeWrapper;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
}
