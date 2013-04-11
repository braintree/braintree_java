package com.braintreegateway;

import com.braintreegateway.testhelpers.CalendarTestUtils;
import com.braintreegateway.util.NodeWrapper;
import org.junit.Test;

import java.text.ParseException;
import java.util.Calendar;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class DepositDetailTest {

    @Test
    public void isValidTrue() throws ParseException {
        NodeWrapper wrapper = mock(NodeWrapper.class);
        Calendar depositDate = CalendarTestUtils.date("2013-04-10");
        when(wrapper.findDate("deposit-date")).thenReturn(depositDate);

        DepositDetail detail = new DepositDetail(wrapper);
        assertTrue(detail.isValid());
    }

    @Test
    public void isValidFalse() throws ParseException {
        NodeWrapper wrapper = mock(NodeWrapper.class);
        when(wrapper.findDate("deposit-date")).thenReturn(null);

        DepositDetail detail = new DepositDetail(wrapper);
        assertFalse(detail.isValid());
    }
}
