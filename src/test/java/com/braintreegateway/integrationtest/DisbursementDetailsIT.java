package com.braintreegateway.integrationtest;

import com.braintreegateway.DisbursementDetails;
import com.braintreegateway.testhelpers.CalendarTestUtils;
import com.braintreegateway.util.NodeWrapper;
import org.junit.Test;

import java.text.ParseException;
import java.util.Calendar;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DisbursementDetailsIT {
    @Test
    public void isValidTrue() throws ParseException {
        NodeWrapper wrapper = mock(NodeWrapper.class);
        Calendar disbursementDate = CalendarTestUtils.date("2013-04-10");
        when(wrapper.findDate("disbursement-date")).thenReturn(disbursementDate);

        DisbursementDetails detail = new DisbursementDetails(wrapper);
        assertTrue(detail.isValid());
    }

    @Test
    public void isValidFalse() throws ParseException {
        NodeWrapper wrapper = mock(NodeWrapper.class);
        when(wrapper.findDate("disbursement-date")).thenReturn(null);

        DisbursementDetails detail = new DisbursementDetails(wrapper);
        assertFalse(detail.isValid());
    }
}
